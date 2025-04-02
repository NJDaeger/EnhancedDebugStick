package com.njdaeger.enhanceddebugstick.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.njdaeger.enhanceddebugstick.api.EnhancedDebugStickApi;
import com.njdaeger.enhanceddebugstick.api.mode.DebugModeType;
import com.njdaeger.enhanceddebugstick.api.mode.ShiftMode;
import com.njdaeger.enhanceddebugstick.i18n.Translation;
import com.njdaeger.enhanceddebugstick.session.DefaultPreferences;
import com.njdaeger.pdk.command.brigadier.ICommandContext;
import com.njdaeger.pdk.command.brigadier.arguments.AbstractStringTypedArgument;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.njdaeger.enhanceddebugstick.util.Util.MESSAGE_COMPONENT_SERIALIZER;

public class DebugModeArgument extends AbstractStringTypedArgument<DebugModeType<?,?>> {

    private static final DynamicCommandExceptionType NO_PERMISSION_FOR_MODE = new DynamicCommandExceptionType((o) -> MESSAGE_COMPONENT_SERIALIZER.serialize(Translation.COMMAND_ERRORS_NO_MODE_PERMISSION.get().apply((DebugModeType<?, ?>) o)));
    private static final DynamicCommandExceptionType UNKNOWN_DEBUG_MODE = new DynamicCommandExceptionType((o) -> MESSAGE_COMPONENT_SERIALIZER.serialize(Translation.COMMAND_ERRORS_UNKNOWN_DEBUG_MODE.get().apply(o.toString())));

    private final List<DebugModeType<?, ?>> debugModes;
    private final EnhancedDebugStickApi plugin;

    public DebugModeArgument(EnhancedDebugStickApi plugin) {
        this.debugModes = new ArrayList<>(DebugModeType.getDebugModes());
        this.plugin = plugin;
    }

    @Override
    public List<DebugModeType<?, ?>> listBasicSuggestions(ICommandContext commandContext) {
        if (!commandContext.isPlayer()) return List.of();

        var session = plugin.getDebugSession(commandContext.asPlayerOrNull().getUniqueId());
        if (session != null && session.getPreference(DefaultPreferences.SHIFT_MODE) == ShiftMode.COMMAND) {
            return debugModes.stream().filter(mode -> commandContext.hasPermission(mode.getBasePermission())).toList();
        }
        return List.of();
    }

    @Override
    public String convertToNative(DebugModeType<?, ?> debugModeType) {
        return debugModeType.getNiceName();
    }

    @Override
    public DebugModeType<?, ?> convertToCustom(@Nullable CommandSender source, String nativeType, StringReader reader) throws CommandSyntaxException {
        var mode = DebugModeType.getDebugMode(nativeType);
        if (mode == null) {
            reader.setCursor(reader.getCursor() - nativeType.length());
            throw UNKNOWN_DEBUG_MODE.createWithContext(reader, nativeType);
        }
        if (!source.hasPermission(mode.getBasePermission())) {
            reader.setCursor(reader.getCursor() - nativeType.length());
            throw NO_PERMISSION_FOR_MODE.createWithContext(reader, mode);
        }
        return mode;
    }
}
