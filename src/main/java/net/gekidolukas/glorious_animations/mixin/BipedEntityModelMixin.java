package net.gekidolukas.glorious_animations.mixin;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BipedEntityModel.class)
public abstract class BipedEntityModelMixin<T extends LivingEntity> {



    @WrapOperation(method = {"animateArms"}, at = {@At(value = "INVOKE",target = "Lnet/minecraft/util/math/MathHelper;sin(F)F", ordinal = 0)})
    private float preventBodyMovement(float value, Operation<Float> original){



        return 0;
    }
}
