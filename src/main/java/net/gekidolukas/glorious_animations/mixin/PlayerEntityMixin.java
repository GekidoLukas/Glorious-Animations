package net.gekidolukas.glorious_animations.mixin;

import net.gekidolukas.glorious_animations.interfaces.SwingTypeGetter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.BlockBreakingProgressS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements SwingTypeGetter {


    private int blockBreakingTicks = 0;
    private int interactTicks = 0;
    private int dropTicks = 0;
    private int totemTicks = 0;
    private int postTridentThrowTicks = 0;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }


    @Override
    public void setBlockBreakingTicks(int blockBreakingTicks) {
        this.blockBreakingTicks = blockBreakingTicks;
    }

    @Override
    public int getBlockBreakingTicks() {
        return blockBreakingTicks;
    }

    @Override
    public void setInteractTicks(int afterInteractTicks) {
        this.interactTicks = afterInteractTicks;
    }

    @Override
    public int getInteractTicks() {
        return interactTicks;
    }

    @Override
    public void setDropTicks(int afterDropTicks) {
        this.dropTicks = afterDropTicks;
    }

    @Override
    public int getDropTicks() {
        return dropTicks;
    }

    @Override
    public void setTotemTicks(int totemTicks) {
        this.totemTicks = totemTicks;
    }
    @Override
    public int getTotemTicks() {
        return totemTicks;
    }

    @Override
    public void setPostTridentThrowTicks(int postTridentThrowTicks) {
        this.postTridentThrowTicks = postTridentThrowTicks;
    }
    @Override
    public int getPostTridentThrowTicks() {
        return postTridentThrowTicks;
    }

    @Inject(method = {"tick"}, at = {@At(value = "HEAD")})
    private void onTick(CallbackInfo ci){
        PlayerEntity player = (PlayerEntity) (Object)this;

        if(((SwingTypeGetter)player).getBlockBreakingTicks() > 0) {
            ((SwingTypeGetter)player).setBlockBreakingTicks(((SwingTypeGetter)player).getBlockBreakingTicks() - 1);
        }
        if(((SwingTypeGetter)player).getInteractTicks() > 0) {
            ((SwingTypeGetter)player).setInteractTicks(((SwingTypeGetter)player).getInteractTicks() - 1);
        }
        if(((SwingTypeGetter)player).getDropTicks() > 0) {
            ((SwingTypeGetter)player).setDropTicks(((SwingTypeGetter)player).getDropTicks() - 1);
        }
        if(((SwingTypeGetter)player).getTotemTicks() > 0) {
            ((SwingTypeGetter)player).setTotemTicks(((SwingTypeGetter)player).getTotemTicks() - 1);
        }
        if(((SwingTypeGetter)player).getPostTridentThrowTicks() > 0) {
            ((SwingTypeGetter)player).setPostTridentThrowTicks(((SwingTypeGetter)player).getPostTridentThrowTicks() - 1);
        }
    }



}
