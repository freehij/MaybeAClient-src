package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class AchievementList {
    public static List field_25196_a = new ArrayList();
    public static Achievement field_25195_b = new Achievement(5242880, StatCollector.func_25200_a("achievement.openInventory"), 0, 0, (Achievement)null);
    public static Achievement field_25198_c;
    public static Achievement field_25197_d;

    static {
        field_25198_c = new Achievement(5242881, StatCollector.func_25200_a("achievement.mineWood"), 4, 1, field_25195_b);
        field_25197_d = new Achievement(5242881, StatCollector.func_25200_a("achievement.buildWorkBench"), 8, -1, field_25198_c);
    }
}
