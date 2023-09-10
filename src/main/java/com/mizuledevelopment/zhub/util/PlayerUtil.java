package com.mizuledevelopment.zhub.util;

import com.mizuledevelopment.zhub.util.text.MessageType;
import com.mizuledevelopment.zhub.util.text.TextUtil;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.CommandSender;

public class PlayerUtil {

    public static void sendMessage(final CommandSender sender, final String message) {
        sendMessage(sender, message, TagResolver.empty());
    }

    public static void sendMessage(final CommandSender sender, final String message, final TagResolver... resolvers ) {
        sender.sendMessage(TextUtil.parse(message, MessageType.MINIMESSAGE, sender, resolvers));
    }
}
