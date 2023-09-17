package com.mizuledevelopment.zhub;

import cloud.commandframework.CommandTree;
import cloud.commandframework.brigadier.CloudBrigadierManager;
import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.exceptions.ArgumentParseException;
import cloud.commandframework.exceptions.CommandExecutionException;
import cloud.commandframework.exceptions.InvalidCommandSenderException;
import cloud.commandframework.exceptions.InvalidSyntaxException;
import cloud.commandframework.exceptions.NoPermissionException;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.execution.FilteringCommandSuggestionProcessor;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.minecraft.extras.AudienceProvider;
import cloud.commandframework.minecraft.extras.MinecraftExceptionHandler;
import cloud.commandframework.minecraft.extras.MinecraftHelp;
import cloud.commandframework.paper.PaperCommandManager;
import cloud.commandframework.services.types.ConsumerService;
import com.mizuledevelopment.zhub.command.SetSpawnCommand;
import com.mizuledevelopment.zhub.command.SpawnCommand;
import com.mizuledevelopment.zhub.command.zHubCommand;
import com.mizuledevelopment.zhub.config.impl.ConfigFile;
import com.mizuledevelopment.zhub.item.api.HotbarHandler;
import com.mizuledevelopment.zhub.listener.player.PlayerListener;
import com.mizuledevelopment.zhub.listener.server.ServerListener;
import com.mizuledevelopment.zhub.pvp.PvPManager;
import com.mizuledevelopment.zhub.scoreboard.HubScoreboardAdapter;
import com.mizuledevelopment.zhub.scoreboard.ScoreboardHandler;
import com.mizuledevelopment.zhub.scoreboard.ScoreboardListener;
import com.mizuledevelopment.zhub.tab.TabHandler;
import com.mizuledevelopment.zhub.task.LocationTask;
import com.mizuledevelopment.zhub.util.color.Color;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.util.ComponentMessageThrowable;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static net.kyori.adventure.text.Component.*;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public final class zHub extends JavaPlugin {

    private static zHub instance;
    private final Map<String, ConfigFile> configs = new HashMap<>();
    private final NamespacedKey namespacedKey = new NamespacedKey(this, "hub");
    private PvPManager pvpManager;
    private TabHandler tabHandler;
    private HotbarHandler hotbarHandler;
    private PaperCommandManager<CommandSender> commandManager;
    private MinecraftHelp<CommandSender> minecraftHelp;

    public static zHub instance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        final long time = System.currentTimeMillis();
        saveDefaultConfig();
        /*

         */
//        if (!new LicenseChecker(getDescription().getName()).check()) {
//            getServer().getPluginManager().disablePlugin(this);
//            return;
//        }
//         this.command();
        this.tabHandler = new TabHandler(this);
        this.listener(Bukkit.getPluginManager());
        this.pvpManager = new PvPManager();
        this.hotbarHandler = new HotbarHandler(this);
        ScoreboardHandler.configure(new HubScoreboardAdapter(this));
        new ScoreboardHandler();
        new LocationTask(this).runTaskTimerAsynchronously(this, 0, 20);
        setupCloud();

        Bukkit.getConsoleSender().sendMessage(Color.translate("&8[&bzHub&8] &7Successfully enabled. It took me &b" + (System.currentTimeMillis() - time) + " &7ms"));
    }

    private void listener(final @NotNull PluginManager pluginManager) {
        Arrays.asList(
            new ServerListener(this),
            new PlayerListener(this),
            new ScoreboardListener()
        ).forEach(listener -> pluginManager.registerEvents(listener, this));
    }

    public TabHandler tabHandler() {
        return this.tabHandler;
    }

    public ConfigFile config(final String name) {
        return this.configs.computeIfAbsent(name, cfg -> {
            if (!cfg.endsWith(".yml")) cfg = cfg + ".yml";
            return new ConfigFile(cfg, getDataFolder().toPath(), zHub.class);
        });
    }

    public List<ConfigFile> configs() {
        return new ArrayList<>(this.configs.values());
    }

    public NamespacedKey getNamespacedKey() {
        return namespacedKey;
    }

    public PvPManager getPvpManager() {
        return pvpManager;
    }

    public HotbarHandler hotbarHandler() {
        return this.hotbarHandler;
    }

    private void setupCloud() {
        final Function<CommandTree<CommandSender>, CommandExecutionCoordinator<CommandSender>> executionCoordinatorFunction =
            AsynchronousCommandExecutionCoordinator.<CommandSender>builder().withAsynchronousParsing().build();
        final Function<CommandSender, CommandSender> mapperFunction = Function.identity();
        try {
            this.commandManager = new PaperCommandManager<>(
                this,
                executionCoordinatorFunction,
                mapperFunction,
                mapperFunction
            );
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }

        this.commandManager.commandSuggestionProcessor(new FilteringCommandSuggestionProcessor<>(
            FilteringCommandSuggestionProcessor.Filter.<CommandSender>contains(true).andTrimBeforeLastSpace()));

        if (this.commandManager.hasCapability(CloudBukkitCapabilities.NATIVE_BRIGADIER) || this.commandManager.hasCapability(CloudBukkitCapabilities.COMMODORE_BRIGADIER)) {
            this.commandManager.registerBrigadier();
            final CloudBrigadierManager<CommandSender, ?> cloudMgr = Objects.requireNonNull(this.commandManager.brigadierManager());
            cloudMgr.setNativeNumberSuggestions(false);
        }
        if (this.commandManager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
            this.commandManager.registerAsynchronousCompletions();
        }
        this.commandManager.registerCommandPostProcessor(context -> {
            final boolean implemented = context.getCommand().getCommandMeta().getOrDefault(CommandMeta.Key.of(Boolean.class, "implemented"), true);
            if (!implemented) {
                context.getCommandContext().getSender().sendMessage(text("This command is not implemented yet.", RED));
                ConsumerService.interrupt();
            }
        });

        this.minecraftHelp = new MinecraftHelp<>("zhub", AudienceProvider.nativeAudience(), this.commandManager);
        new MinecraftExceptionHandler<CommandSender>().withInvalidSyntaxHandler()
            .withInvalidSenderHandler().withArgumentParsingHandler()
            .withCommandExecutionHandler()
            .withDecorator(component -> Component.empty().append(text("[zHub]", TextColor.color(0x2F83FF), TextDecoration.BOLD))
                .append(Component.space()).append(component))
            .apply(this.commandManager, AudienceProvider.nativeAudience());

        this.commandManager.registerExceptionHandler(NoPermissionException.class,
            (source, exception) -> {
                source.sendMessage(text("You do not have permission to execute this command.", RED));
            });
        this.commandManager.registerExceptionHandler(ArgumentParseException.class,
            (source, exception) -> {
                source.sendMessage(Objects.requireNonNull(ComponentMessageThrowable.getOrConvertMessage(exception.getCause())).colorIfAbsent(RED));
            });
        this.commandManager.registerExceptionHandler(CommandExecutionException.class,
            (source, exception) -> {
                if (exception.getCause() instanceof UnsupportedOperationException) {
                    source.sendMessage(text("This command is not supported by the server.", RED));
                }
            });
        this.commandManager.registerExceptionHandler(UnsupportedOperationException.class,
            (source, exception) -> {
                source.sendMessage(text(exception.getMessage(), RED));
            });
        this.commandManager.registerExceptionHandler(InvalidCommandSenderException.class,
            (source, exc) -> {
                final Class<?> requiredSender = exc.getRequiredSender();
                if (ConsoleCommandSender.class.isAssignableFrom(requiredSender)) {
                    source.sendMessage(text()
                        .append(text("This command can only be executed by Console.", RED))
                    );
                } else if (Player.class.isAssignableFrom(requiredSender)) {
                    source.sendMessage(text()
                        .append(text("This command can only be executed by players.", RED))
                    );
                } else {
                    source.sendMessage(text()
                        .append(text("This command can only be executed by ", RED))
                        .append(text(requiredSender.getSimpleName(), WHITE))
                        .append(text("s.", RED))
                    );
                }
            });
        this.commandManager.registerExceptionHandler(InvalidSyntaxException.class,
            (source, exception) -> {
                final String[] syntax = exception.getCorrectSyntax().split(" ");
                source.sendMessage(text()
                    .append(text("Usage: ", RED))
                    .append(text("/" + syntax[0], RED))
                    .append(Component.space())
                    .append(text(String.join(" ", Arrays.copyOfRange(syntax, 1, syntax.length)), WHITE))
                );
            });

        List.of(
            new zHubCommand(),
            new SetSpawnCommand(this),
            new SpawnCommand(this)
        ).forEach(command -> command.register(this.commandManager));
    }

    public PaperCommandManager<CommandSender> commandManager() {
        return this.commandManager;
    }

    public MinecraftHelp<CommandSender> minecraftHelp() {
        return this.minecraftHelp;
    }
}
