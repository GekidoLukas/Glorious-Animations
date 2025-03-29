package net.gekidolukas.glorious_animations.mixin;

import net.gekidolukas.glorious_animations.CommonAnimations;
import net.gekidolukas.glorious_animations.interfaces.SwingTypeGetter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow @Nullable public ClientPlayerEntity player;


    @Inject(method = {"doAttack"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;swingHand(Lnet/minecraft/util/Hand;)V", shift = At.Shift.BEFORE)})
    private void doAttack(CallbackInfoReturnable<Boolean> cir){

        if(player != null && player instanceof ClientPlayerEntity clientPlayerEntity)
        {
//            ((SwingTypeGetter)player).setAttackTicks(10);
        }
    }


    @Inject(method = {"handleBlockBreaking"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;swingHand(Lnet/minecraft/util/Hand;)V", shift = At.Shift.BEFORE)})
    private void blockBreakSwing(boolean breaking, CallbackInfo ci){

        if(player != null && player instanceof ClientPlayerEntity clientPlayerEntity)
        {
//            ((torsoPosGetter)clientPlayerEntity).setBlockBreakingTicks(10);
            ((SwingTypeGetter)player).setBlockBreakingTicks(10);
        }
    }

    @Inject(method = {"handleInputEvents"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;swingHand(Lnet/minecraft/util/Hand;)V", shift = At.Shift.BEFORE)})
    private void doDrop(CallbackInfo ci){

        if(player != null && player instanceof ClientPlayerEntity clientPlayerEntity)
        {
            ((SwingTypeGetter)player).setDropTicks(CommonAnimations.DROP_ITEM.getLength());
        }
    }

}
