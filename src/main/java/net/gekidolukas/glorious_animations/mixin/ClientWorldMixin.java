package net.gekidolukas.glorious_animations.mixin;

import net.gekidolukas.glorious_animations.interfaces.SwingTypeGetter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin {

    @Inject(method = "addEntity", at = @At("HEAD"))
    private void onAddEntity(Entity entity, CallbackInfo ci) {
        if (entity instanceof ItemEntity item) {
            MinecraftClient client = MinecraftClient.getInstance();

            if (client.world != null) {
                PlayerEntity closestPlayer = null;
                double closestDistance = Double.MAX_VALUE;

                Vec3d itemPos = item.getPos();

                // Überprüfe alle Spieler in der Client-Welt
                for (PlayerEntity player : client.world.getPlayers()) {
                    Vec3d eyePos = player.getEyePos();
                    double distance = itemPos.distanceTo(eyePos);

                    if (distance < closestDistance) {
                        closestDistance = distance;
                        closestPlayer = player;
                    }
                }

                // Schwellenwert für die Zuordnung (in Blöcken)
                double threshold = 2.0;

                if (closestPlayer != null && closestDistance <= threshold) {
                    ((SwingTypeGetter)closestPlayer).setBlockBreakingTicks(10);

                }
            }
        }
    }

}
