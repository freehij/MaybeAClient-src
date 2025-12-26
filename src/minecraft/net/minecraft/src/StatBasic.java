package net.minecraft.src;

public class StatBasic {
    public final int field_25071_d;
    public final String field_25070_e;
    public String field_25069_f;

    public StatBasic(int var1, String var2) {
        this.field_25071_d = var1;
        this.field_25070_e = var2;
    }

    public StatBasic func_25068_c() {
        StatList.func_25152_a(this);
        return this;
    }

    public boolean func_25067_a() {
        return false;
    }
}
