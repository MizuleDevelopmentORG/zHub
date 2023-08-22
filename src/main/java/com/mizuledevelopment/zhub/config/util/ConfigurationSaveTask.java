package com.mizuledevelopment.zhub.config.util;

import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;

import java.util.concurrent.atomic.AtomicInteger;

public class ConfigurationSaveTask implements Runnable {
    private final ConfigurationLoader<?> loader;
    private final ConfigurationNode node;
    private final AtomicInteger pendingWrites;
    private final String name;

    public ConfigurationSaveTask(final String name, final ConfigurationLoader<?> loader, final ConfigurationNode node,
                                 final AtomicInteger pendingWrites) {
        this.loader = loader;
        this.name = name;
        this.node = node;
        this.pendingWrites = pendingWrites;
    }

    @Override
    public void run() {
        synchronized (this.loader) {
            // Check if there are more writes in queue.
            // If that's the case, we shouldn't bother writing data which is already
            // out-of-date.
            if (this.pendingWrites.get() > 1) {
                this.pendingWrites.decrementAndGet();
            }

            try {
                this.loader.save(this.node);
//                LOGGER.debug("Saving {}", this.name);
            } catch (final ConfigurateException e) {
                // who gives a shit
//                LOGGER.error("Error whilst trying to save {}", this.name, e);
            } finally {
                this.pendingWrites.decrementAndGet();
            }
        }
    }
}