package net.gekidolukas.glorious_animations.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(method = {"doAttack"}, at = {@At(value = "HEAD")})
    private void doAttack(CallbackInfoReturnable<Boolean> cir){

        if(MinecraftClient.getInstance().player != null)
        {
//            MinecraftClient.getInstance().player.setAfterAttackTicks(10);
        }
    }

}
