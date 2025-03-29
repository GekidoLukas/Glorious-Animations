package net.gekidolukas.glorious_animations.mixin;

import net.gekidolukas.glorious_animations.CommonAnimations;
import net.gekidolukas.glorious_animations.interfaces.SwingTypeGetter;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.network.packet.s2c.play.BlockBreakingProgressS2CPacket;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {



    @Inject(method = {"onBlockBreakingProgress"}, at = {@At(value = "HEAD")})
    private void doBreak(BlockBreakingProgressS2CPacket packet, CallbackInfo ci){

        int breakerId = packet.getEntityId();
        BlockPos pos = packet.getPos();
        int progress = packet.getProgress();
        Entity entity = MinecraftClient.getInstance().world.getEntityById(breakerId);
        if (entity instanceof PlayerEntity player) {
            ((SwingTypeGetter)player).setBlockBreakingTicks(10);
        }
    }

    @Inject(method = "onEntityStatus", at = @At("HEAD"))
    private void onEntityStatus(EntityStatusS2CPacket packet, CallbackInfo ci) {
        if (packet.getStatus() == 35) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.world != null) {
                AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) packet.getEntity(client.world);
                if (player != null) {
                    ((SwingTypeGetter)player).setTotemTicks(CommonAnimations.TOTEM_REVIVE.getLength() - 10);
                }
            }
        }
    }

    @Inject(method = "onEntitySpawn", at = @At("HEAD"))
    private void onEntitySpawn(EntitySpawnS2CPacket packet, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return;

        Entity entity = packet.getEntityType().create(client.world);

        if (entity instanceof TridentEntity) {
            Vec3d tridentPos = new Vec3d(packet.getX(),packet.getY(),packet.getZ());
            for(PlayerEntity player : client.world.getPlayers()) {
                if(player.getEyePos().distanceTo(tridentPos) < 1.0f) {
                    ((SwingTypeGetter)player).setPostTridentThrowTicks(10);
                }
            }
        }
        if(entity != null) entity.discard();
    }


    @Inject(method = "onBlockUpdate", at = @At("HEAD"))
    private void onEntitySpawn(BlockUpdateS2CPacket packet, CallbackInfo ci) {
        BlockPos pos = packet.getPos();
        BlockState newState = packet.getState();
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
}
