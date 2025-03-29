package net.gekidolukas.glorious_animations.mixin;

import net.gekidolukas.glorious_animations.CommonAnimations;
import net.gekidolukas.glorious_animations.interfaces.SwingTypeGetter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.network.packet.s2c.play.BlockBreakingProgressS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
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
            System.out.println("Spieler " + player.getGameProfile().getName() + " bearbeitet einen Block bei " + pos + " mit Fortschritt: " + progress);
            ((SwingTypeGetter)player).setBlockBreakingTicks(10); //TODO For all Actions and for the client Player itself
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
}
