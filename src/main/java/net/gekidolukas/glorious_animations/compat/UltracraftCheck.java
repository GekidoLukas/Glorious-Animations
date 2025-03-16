package net.gekidolukas.glorious_animations.compat;

import absolutelyaya.ultracraft.item.AbstractNailgunItem;
import absolutelyaya.ultracraft.item.AbstractWeaponItem;
import net.minecraft.item.Item;

public class UltracraftCheck {

    public static boolean isGenericGun(Item item) {
        if (item instanceof AbstractWeaponItem w) {
            if (w.shouldAim()) {
                return true;
            }
        }


        return false;

    }

    public static boolean isNailgun(Item item) {

        if (item instanceof AbstractNailgunItem) {
            return true;
        }
        return false;


    }

}
