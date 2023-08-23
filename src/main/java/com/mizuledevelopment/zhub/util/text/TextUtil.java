package com.mizuledevelopment.zhub.util.text;

import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class TextUtil {

    private TextUtil() {
    }

    public static Component parse(
            final String input,
            final MessageType messageType
    ) {
        return parse(input, messageType, TagResolver.empty());
    }

    public static Component parse(
            final String input,
            final MessageType messageType,
            final @NotNull TagResolver... resolvers
    ) {
        return switch (messageType) {
            case MINIMESSAGE -> MiniMessage.miniMessage().deserialize(input, TagResolver.resolver(
                    getResolvers(resolvers)
            ));
            case LEGACY -> LegacyComponentSerializer.legacyAmpersand().deserializeOr(input, Component.empty());
            case LEGACY_SECTION -> LegacyComponentSerializer.legacySection().deserializeOr(input, Component.empty());
            case GSON -> GsonComponentSerializer.gson().deserializeOr(input, Component.empty());
            case PLAIN -> PlainTextComponentSerializer.plainText().deserializeOr(input, Component.empty());
            default -> Component.text(input);
        };
    }

    public static Component parse(
            final String input, final MessageType messageType, final Audience audience
    ) {
        return parse(input, messageType, audience, getResolvers(TagResolver.empty()));
    }

    public static Component parse(
            final String input,
            final MessageType messageType,
            final Audience audience,
            final @NotNull TagResolver... resolvers
    ) {
        return switch (messageType) {
            case MINIMESSAGE -> MiniMessage.miniMessage().deserialize(input, TagResolver.resolver(
                    getResolvers(resolvers),
                    audienceResolver(audience)
            ));
            case LEGACY -> LegacyComponentSerializer.legacyAmpersand().deserializeOr(input, Component.empty());
            case LEGACY_SECTION -> LegacyComponentSerializer.legacySection().deserializeOr(input, Component.empty());
            case GSON -> GsonComponentSerializer.gson().deserializeOr(input, Component.empty());
            case PLAIN -> PlainTextComponentSerializer.plainText().deserializeOr(input, Component.empty());
            default -> Component.text(input);
        };
    }

    public static boolean isNullOrEmpty(final String text) {
        return text == null || text.isBlank();
    }

    public static TagResolver getResolvers(final TagResolver @Nullable ... tagResolvers) {
        final List<TagResolver> resolvers = new ArrayList<>();
        resolvers.add(TagResolver.standard());
//        resolvers.add(TagResolver.resolver(Set.of("a", "link", "url"), Text::createLinkTag));
        if (miniPlaceholdersAvailable()) {
            resolvers.add(MiniPlaceholders.getGlobalPlaceholders());
        }
        if (tagResolvers != null) {
            resolvers.addAll(Arrays.asList(tagResolvers));
        }

        return TagResolver.resolver(resolvers);
    }

    public static boolean miniPlaceholdersAvailable() {
        return Bukkit.getPluginManager().isPluginEnabled("MiniPlaceholders");
    }

    private static TagResolver audienceResolver(final Audience audience) {
        if (!miniPlaceholdersAvailable()) {
            return TagResolver.empty();
        }
        return TagResolver.resolver(
                MiniPlaceholders.getAudiencePlaceholders(audience),
                MiniPlaceholders.getAudienceGlobalPlaceholders(audience)
        );
    }
}
