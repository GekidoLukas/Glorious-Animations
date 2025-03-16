package net.gekidolukas.glorious_animations.compat;

import eu.midnightdust.swordblocking.SwordBlockingClient;
import net.minecraft.entity.LivingEntity;

public class SwordBlockingCheck {

    public static boolean check(LivingEntity player) {
        return SwordBlockingClient.isWeaponBlocking(player);
    }


}
