package com.njdaeger.enhanceddebugstick.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.njdaeger.enhanceddebugstick.api.session.Preference;
import com.njdaeger.enhanceddebugstick.i18n.Translation;
import com.njdaeger.enhanceddebugstick.session.DefaultPreferences;
import com.njdaeger.pdk.command.brigadier.ICommandContext;
import com.njdaeger.pdk.command.brigadier.arguments.AbstractStringTypedArgument;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.njdaeger.enhanceddebugstick.util.Util.MESSAGE_COMPONENT_SERIALIZER;

public class PreferenceArgument extends AbstractStringTypedArgument<Preference<?>> {

    private static final DynamicCommandExceptionType UNKNOWN_PREFERENCE = new DynamicCommandExceptionType((o) -> MESSAGE_COMPONENT_SERIALIZER.serialize(Translation.COMMAND_ERRORS_UNKNOWN_PREFERENCE.get().apply(o.toString())));

    private final List<Preference<?>> preferences;

    public PreferenceArgument() {
        //shift mode has its own specific branch in the command, so we remove it from the generic preference argument.
        this.preferences = Preference.getPreferences().stream().filter(m -> !m.getKey().equalsIgnoreCase(DefaultPreferences.SHIFT_MODE.getKey())).toList();
    }

    @Override
    public List<Preference<?>> listBasicSuggestions(ICommandContext commandContext) {
        if (!commandContext.isPlayer()) return List.of();
        return preferences;
    }

    @Override
    public String convertToNative(Preference<?> preference) {
        return preference.getKey();
    }

    @Override
    public Preference<?> convertToCustom(@Nullable CommandSender source, String nativeType, StringReader reader) throws CommandSyntaxException {
        var preference = preferences.stream().filter(p -> p.getKey().equalsIgnoreCase(nativeType)).findFirst();
        if (preference.isEmpty()) {
            reader.setCursor(reader.getCursor() - nativeType.length());
            throw UNKNOWN_PREFERENCE.createWithContext(reader, nativeType);
        }
        return preference.get();
    }
}
