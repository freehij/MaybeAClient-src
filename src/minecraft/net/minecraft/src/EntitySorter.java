package net.minecraft.src;

import java.util.Comparator;

public class EntitySorter implements Comparator {
    private Entity entityForSorting;

    public EntitySorter(Entity var1) {
        this.entityForSorting = var1;
    }

    public int sortByDistanceToEntity(WorldRenderer var1, WorldRenderer var2) {
    	
    	float dist1 = var1.distanceToEntitySquared(this.entityForSorting);
    	float dist2 = var2.distanceToEntitySquared(this.entityForSorting);
    	
        return dist1 < dist2 ? -1 : (dist1 == dist2 ? 0 : 1);
    }

    // $FF: synthetic method
    // $FF: bridge method
    public int compare(Object var1, Object var2) {
        return this.sortByDistanceToEntity((WorldRenderer)var1, (WorldRenderer)var2);
    }
}
