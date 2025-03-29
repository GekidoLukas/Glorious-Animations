package net.gekidolukas.glorious_animations.mixin;

import net.gekidolukas.glorious_animations.CommonAnimations;
import net.gekidolukas.glorious_animations.interfaces.SwingTypeGetter;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

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

                for (PlayerEntity player : client.world.getPlayers()) {
                    Vec3d eyePos = player.getEyePos();
                    double distance = itemPos.distanceTo(eyePos);

                    if (distance < closestDistance) {
                        closestDistance = distance;
                        closestPlayer = player;
                    }
                }

                double threshold = 2.0;

                if (closestPlayer != null && closestDistance <= threshold) {
                    ((SwingTypeGetter)closestPlayer).setBlockBreakingTicks(10);

                }
            }
        }
    }

    @Inject(method = "setBlockState", at = @At("HEAD"))
    private void onBlockStateChange(BlockPos pos, BlockState newState, int flags, int maxUpdateDepth, CallbackInfoReturnable<Boolean> cir) {
        if (newState.isAir()) {
            MinecraftClient client = MinecraftClient.getInstance();

            for (PlayerEntity player : client.world.getPlayers()) {
                Vec3d eyePos = player.getCameraPosVec(1.0F);
                Vec3d lookVec = player.getRotationVec(1.0F);
                double reachDistance = player.getAttributeValue(EntityAttributes.PLAYER_BLOCK_INTERACTION_RANGE);
                Vec3d target = eyePos.add(lookVec.multiply(reachDistance));

                HitResult result = client.world.raycast(
                        new RaycastContext(
                                eyePos,
                                target,
                                RaycastContext.ShapeType.OUTLINE,
                                RaycastContext.FluidHandling.NONE,
                                player
                        )
                );

                if (result.getType() == HitResult.Type.BLOCK) {
                    BlockHitResult blockResult = (BlockHitResult) result;
                    if (blockResult.getBlockPos().equals(pos)) {
                        ((SwingTypeGetter)player).setBlockBreakingTicks(10);
                    }
                }
            }
        }
    }

//    @Inject(method = "<init>", at = @At("TAIL"))
    private void onLoad(ClientPlayNetworkHandler networkHandler, ClientWorld.Properties properties, RegistryKey registryRef, RegistryEntry dimensionTypeEntry, int loadDistance, int simulationDistance, Supplier profiler, WorldRenderer worldRenderer, boolean debugWorld, long seed, CallbackInfo ci) {
//        CommonAnimations.reloadAnimationVariables();
    }

}
