package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;

public class XBlah {
    private Map field_25102_a = new HashMap();
    private Map field_25101_b = new HashMap();

    public void func_25100_a(StatBasic var1, int var2) {
        Integer var3 = (Integer)this.field_25101_b.get(var1);
        if (var3 != null) {
            var2 += var3;
        }

        this.field_25101_b.put(var1, var2);
    }
}
