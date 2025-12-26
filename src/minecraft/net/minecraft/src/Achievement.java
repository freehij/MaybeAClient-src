package net.minecraft.src;

public class Achievement extends StatBasic {
    public final int field_25075_a;
    public final int field_25074_b;
    public final Achievement field_25076_c;

    public Achievement(int var1, String var2, int var3, int var4, Achievement var5) {
        super(var1, var2);
        this.field_25075_a = var3 + 46;
        this.field_25074_b = var4 + 23;
        this.field_25076_c = var5;
    }

    public boolean func_25067_a() {
        return true;
    }
}
