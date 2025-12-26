package net.minecraft.src;

public class StatCollector {
    private static StringTranslate field_25201_a = StringTranslate.getInstance();

    public static String func_25200_a(String var0) {
        return field_25201_a.translateKey(var0);
    }

    public static String func_25199_a(String var0, Object... var1) {
        return field_25201_a.translateKeyFormat(var0, var1);
    }
}
