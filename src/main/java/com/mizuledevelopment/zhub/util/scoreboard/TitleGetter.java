package com.mizuledevelopment.zhub.util.scoreboard;

import com.google.common.base.Preconditions;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface TitleGetter {

    static TitleGetter forStaticString(final String staticString) {
        Preconditions.checkNotNull(staticString);
        return player -> Component.text(staticString);
    }

    Component getTitle(
            final @NotNull Player player
    );
}