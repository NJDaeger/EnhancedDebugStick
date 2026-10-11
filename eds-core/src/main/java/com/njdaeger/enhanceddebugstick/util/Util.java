package com.njdaeger.enhanceddebugstick.util;

import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public final class Util {

    private Util() {}

    public static final LegacyComponentSerializer LEGACY_COMPONENT_SERIALIZER = LegacyComponentSerializer.builder().character('&').build();
    public static final MessageComponentSerializer MESSAGE_COMPONENT_SERIALIZER = MessageComponentSerializer.message();

    public static String format(String string) {
        String[] split = string.split("_");
        StringBuilder result = new StringBuilder();
        for (String str : split) {
            result.append(Character.toUpperCase(str.charAt(0))).append(str.substring(1));
            result.append(" ");
        }
        return result.toString().trim();
    }



}
