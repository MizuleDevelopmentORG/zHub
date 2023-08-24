package com.mizuledevelopment.zhub.config.impl;

import com.google.common.base.Splitter;
import com.mizuledevelopment.zhub.config.IConfig;
import com.mizuledevelopment.zhub.config.util.ConfigurateSerializers;
import com.mizuledevelopment.zhub.config.util.ConfigurationSaveTask;
import com.mizuledevelopment.zhub.util.FileUtil;
import io.leangen.geantyref.TypeToken;
import org.bukkit.Location;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.loader.HeaderMode;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ConfigFile implements IConfig<CommentedConfigurationNode, CommentedConfigurationNode> {
    @ApiStatus.Internal
    public static final ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor();
    private static final ObjectMapper.Factory MAPPER_FACTORY = ObjectMapper.factoryBuilder()
            .build();
    private static final TypeSerializerCollection SERIALIZERS = TypeSerializerCollection.defaults().childBuilder()
            .registerAnnotatedObjects(MAPPER_FACTORY)
            .registerAll(ConfigurateSerializers.getCollection())
            .registerAll(TypeSerializerCollection.defaults())
            .build();
    private final YamlConfigurationLoader config;
    private final String name;
    private final AtomicInteger pendingWrites = new AtomicInteger(0);
    private final AtomicBoolean transaction = new AtomicBoolean(false);
    private CommentedConfigurationNode root;
    private Runnable saveHook;

    public ConfigFile(
            final String name, final Path path,
            final @Nullable Class<?> clazz
    ) {
        final Path kek = path.resolve(name);
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            assert clazz != null;
            FileUtil.copyFromJar(clazz, name, kek);
            this.config = YamlConfigurationLoader.builder().defaultOptions(opts -> {
                        return opts.shouldCopyDefaults(true).serializers(SERIALIZERS);
                    })
                    .headerMode(HeaderMode.PRESERVE)
                    .nodeStyle(NodeStyle.BLOCK)
                    .indent(2)
                    .path(kek)
                    .build();
            this.name = name;
            if (this.pendingWrites.get() != 0) {
                throw new RuntimeException("ignore");
            }
            this.root = this.config.load();
            this.config.save(this.root);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public CommentedConfigurationNode handle() {
        return this.root;
    }

    @Override
    public String getString(final String path, final String def) {
        final String result = this.resolvePath(path).getString();

        if (result == null || result.isBlank()) {
            if (def != null)
                this.set(path, def);
            return def;
        } else {
            return result;
        }
    }

    @Override
    public int getInt(final String path, final int def) {
        return this.resolvePath(path).getInt(def);
    }

    @Override
    public long getLong(final String path, final long def) {
        return this.resolvePath(path).getLong(def);
    }

    @Override
    public double getDouble(final String path, final double def) {
        return this.resolvePath(path).getDouble(def);
    }

    @Override
    public float getFloat(final String path, final float def) {
        return this.resolvePath(path).getFloat(def);
    }

    @Override
    public boolean getBoolean(final String path, final boolean def) {
        return this.resolvePath(path).getBoolean(def);
    }

    @Override
    public Object get(final String path, final Object def) {
        Object result = null;
        try {
            result = this.resolvePath(path).get(TypeToken.get(def.getClass()));
        } catch (final SerializationException e) {
            throw new RuntimeException(e);
        }
        if (result != null) {
            return result;
        } else {
            if (def != null)
                this.set(path, def);
            return def;
        }
    }

    @Override
    public List<String> getStringList(final String path, final List<String> def) {
        List<String> result = null;
        try {
            result = this.resolvePath(path).getList(String.class);
        } catch (final SerializationException e) {
            throw new RuntimeException(e);
        }

        if (result != null) {
            return result;
        } else {
            if (def != null)
                this.set(path, def);
            return def;
        }
    }

    @Override
    public List<Integer> getIntList(final String path, final List<Integer> def) {
        List<Integer> result = null;
        try {
            result = this.resolvePath(path).getList(Integer.class);
        } catch (final SerializationException e) {
            throw new RuntimeException(e);
        }

        if (result != null) {
            return result;
        } else {
            if (def != null)
                this.set(path, def);
            return def;
        }
    }

    @Override
    public <K> List<K> getList(final String path, final List<K> def) {
        List<K> result = null;
        try {
            result = (List<K>) this.resolvePath(path).getList(TypeToken.get(def.getClass()));
        } catch (final SerializationException e) {
            throw new RuntimeException(e);
        }
        if (result != null) {
            return result;
        } else {
            if (def != null)
                this.set(path, def);
            return def;
        }
    }

    @Override
    public void set(final String path, final Object object) {
        this.setAndCache(path, object);
    }

    void setAndCache(final String path, final Object object) {
        try {
            this.resolvePath(path).set(object);
        } catch (final SerializationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CommentedConfigurationNode section(final String path) {
        return this.resolvePath(path).virtual() ? null : this.resolvePath(path);
    }

    @Override
    public void reload() {
        try {
            this.root = this.config.load();
            this.config.save(this.root);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public YamlConfigurationLoader loader() {
        return this.config;
    }

    @Override
    public String toString() {
        try {
            return YamlConfigurationLoader.builder().buildAndSaveString(this.root);
        } catch (final ConfigurateException e) {
            throw new RuntimeException(e);
        }
    }

    public CommentedConfigurationNode resolvePath(final String path) {
        if (this.root == null) {
            throw new RuntimeException("Config is not loaded.");
        }

        return this.root.node(Splitter.on('.').splitToList(path).toArray());
    }

    /**
     * Begins a transaction.
     *
     * <p>
     * A transaction informs Essentials to pause the saving of data. This is
     * being used when bulk operations are being done and data shouldn't be saved until
     * after the transaction has been completed.
     */
    public void startTransaction() {
        this.transaction.set(true);
    }

    public void stopTransaction() {
        this.stopTransaction(false);
    }

    public void stopTransaction(final boolean blocking) {
        this.transaction.set(false);
        if (blocking) {
            this.blockingSave();
        } else {
            this.save();
        }
    }

    public boolean isTransaction() {
        return this.transaction.get();
    }

    public void setSaveHook(final Runnable saveHook) {
        this.saveHook = saveHook;
    }

    public synchronized void save() {
        if (!this.transaction.get()) {
            this.delaySave();
        }
    }

    public synchronized void blockingSave() {
        try {
            this.delaySave().get();
        } catch (final InterruptedException | ExecutionException e) {
            // Essentials.getWrappedLogger().log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private Future<?> delaySave() {
        if (this.saveHook != null) {
            this.saveHook.run();
        }

        final CommentedConfigurationNode node = this.root.copy();

        this.pendingWrites.incrementAndGet();

        return EXECUTOR.submit(new ConfigurationSaveTask(this.name, this.config, node, this.pendingWrites));
    }

    public CommentedConfigurationNode getInternal(final String path) {
        final CommentedConfigurationNode node = this.resolvePath(path);
        if (node.virtual()) {
            return null;
        }
        return node;
    }
}