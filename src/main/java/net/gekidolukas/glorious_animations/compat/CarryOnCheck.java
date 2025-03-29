package net.gekidolukas.glorious_animations.compat;

import net.gekidolukas.glorious_animations.CommonAnimations;
import net.gekidolukas.glorious_animations.interfaces.TorsoPosGetter;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import tschipp.carryon.common.carry.CarryOnData;
import tschipp.carryon.common.carry.CarryOnDataManager;

public class CarryOnCheck {

    public static void check(AbstractClientPlayerEntity player) {
        CarryOnData carry = CarryOnDataManager.getCarryData(player);
        if (carry.isCarrying() && !player.isInSwimmingPose() && !player.isFallFlying()) {
            ((TorsoPosGetter)player).disableArms(true);
        }

    }




}
