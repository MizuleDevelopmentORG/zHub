package com.mizuledevelopment.zhub.util.text;

import com.mizuledevelopment.zhub.util.PlaceholderUtil;
import io.github.miniplaceholders.api.MiniPlaceholders;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
        if (input != null) {
            return parse(input, messageType, TagResolver.empty());
        }
        return null;
    }

    public static Component parse(
        final String input,
        final MessageType messageType,
        final @NotNull TagResolver... resolvers
    ) {
        if (input != null) {
            return switch (messageType) {
                case MINIMESSAGE -> MiniMessage.miniMessage().deserialize(input, TagResolver.resolver(
                    getResolvers(resolvers)
                ));
                case LEGACY -> LegacyComponentSerializer.legacyAmpersand().deserializeOr(input, Component.empty());
                case LEGACY_SECTION ->
                    LegacyComponentSerializer.legacySection().deserializeOr(input, Component.empty());
                case GSON -> GsonComponentSerializer.gson().deserializeOr(input, Component.empty());
                case PLAIN -> PlainTextComponentSerializer.plainText().deserializeOr(input, Component.empty());
                default -> Component.text(input);
            };
        }
        return null;
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
                audienceResolver(audience),
                papiResolver(audience instanceof Player ? (Player) audience : null)
            ));
            case LEGACY -> LegacyComponentSerializer.legacyAmpersand().deserializeOr(PlaceholderUtil.replacePlaceholders(audience instanceof Player ? (Player) audience : null, input), Component.empty());
            case LEGACY_SECTION -> LegacyComponentSerializer.legacySection().deserializeOr(PlaceholderUtil.replacePlaceholders(audience instanceof Player ? (Player) audience : null, input), Component.empty());
            case GSON -> GsonComponentSerializer.gson().deserializeOr(input, Component.empty());
            case PLAIN -> PlainTextComponentSerializer.plainText().deserializeOr(PlaceholderUtil.replacePlaceholders(audience instanceof Player ? (Player) audience : null, input), Component.empty());
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

    public static boolean placeholderApiAvailable() {
        return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    private static TagResolver papiResolver(final Player player) {
        if (!placeholderApiAvailable()) {
            return TagResolver.empty();
        }
        if (player == null) {
            return TagResolver.empty();
        }
        return TagResolver.resolver("papi", (argumentQueue, context) -> {
            // Get the string placeholder that they want to use.
            final String papiPlaceholder = argumentQueue.popOr("papi tag requires an argument").value();

            // Then get PAPI to parse the placeholder for the given player.
            final String parsedPlaceholder = PlaceholderAPI.setPlaceholders(player, '%' + papiPlaceholder + '%');

            // We need to turn this ugly legacy string into a nice component.
            final Component componentPlaceholder = LegacyComponentSerializer.legacySection().deserialize(parsedPlaceholder);

            // Finally, return the tag instance to insert the placeholder!
            return Tag.selfClosingInserting(componentPlaceholder);
        });
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
