package com.njdaeger.enhanceddebugstick;

public final class Util {

    private Util() {}

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
