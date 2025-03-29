package net.gekidolukas.glorious_animations.mixin;

import net.gekidolukas.glorious_animations.GloriousAnimations;
import net.gekidolukas.glorious_animations.config.GloriousAnimConfig;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {


    private static final float ROTATION_MULTIPLIER = 5.0f;
    private static final float MOTION_MULTIPLIER = 10.0f;
    private static final double SMOOTHING_FACTOR = 0.01;
    private static final double POSITION_THRESHOLD = 0.001;


    @Unique
    private static final Map<Integer, Float> lastYawMap = new HashMap<>();
    @Unique
    private static final Map<Integer, Double> lastXMap = new HashMap<>();
    private static final Map<Integer, Double> lastZMap = new HashMap<>();



    @Inject(
            method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;III)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onRenderItem(LivingEntity entity, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, World world, int light, int overlay, int seed, CallbackInfo ci) {
        if(entity instanceof PlayerEntity player) {
            MinecraftClient client = MinecraftClient.getInstance();
            float currentYaw = player.bodyYaw;
            int entityId = entity.getId();
            float lastYaw = lastYawMap.getOrDefault(entityId, currentYaw);
            float smoothYaw = MathHelper.lerp((float) SMOOTHING_FACTOR, lastYaw, currentYaw);
            float deltaYaw = smoothYaw - currentYaw;
            deltaYaw = Math.abs(deltaYaw) < 45 ? deltaYaw : deltaYaw < 0 ? -45 : 45;

            double currentX = entity.getX();
            double currentZ = entity.getZ();

            double lastX = lastXMap.getOrDefault(entityId, currentX);
            double lastZ = lastZMap.getOrDefault(entityId, currentZ);
            double smoothX = lastX * (1 - SMOOTHING_FACTOR) + currentX * SMOOTHING_FACTOR;
            double smoothZ = lastZ * (1 - SMOOTHING_FACTOR) + currentZ * SMOOTHING_FACTOR;

            double dx = currentX - smoothX;
            double dz = currentZ - smoothZ;

            if (Math.abs(dx) < POSITION_THRESHOLD) dx = 0;
            if (Math.abs(dz) < POSITION_THRESHOLD) dz = 0;

            if((renderMode == ModelTransformationMode.THIRD_PERSON_LEFT_HAND ||
                    renderMode == ModelTransformationMode.THIRD_PERSON_RIGHT_HAND)) {
                if (GloriousAnimConfig.isLanternItem(stack.getItem()) && stack.getItem() instanceof BlockItem blockItem) {
                    float yawRad = (float) Math.toRadians(currentYaw);
                    double forwardMotion = -(dx * Math.sin(yawRad) - dz * Math.cos(yawRad));
                    float rawExtraPitchOffset = (float) (-forwardMotion * MOTION_MULTIPLIER);
                    float extraPitchOffset = Math.abs(rawExtraPitchOffset) > 0.001f ? rawExtraPitchOffset : 0f;
                    extraPitchOffset = Math.abs(extraPitchOffset) < 45 ? extraPitchOffset : extraPitchOffset < 0 ? -45 : 45;


                    BlockRenderManager blockRenderManager = client.getBlockRenderManager();
                    BlockState state = blockItem.getBlock().getDefaultState();
                    BakedModel model = blockRenderManager.getModel(state);

//                    player.sendMessage(Text.literal("Yaw: " + deltaYaw + " Pitch: " + extraPitchOffset ), true);
                    matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(deltaYaw));
                    matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(extraPitchOffset));
                    matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(7));
                    matrices.scale(0.75f,0.75f,0.75f);
                    matrices.translate(-0.5,-0.5,-0.5);
                    matrices.translate(0,-0.45,0.1);
                    blockRenderManager.getModelRenderer().render(
                            matrices.peek(),
                            vertexConsumers.getBuffer(RenderLayers.getBlockLayer(state)),
                            state,
                            model,
                            1.0F, 1.0F, 1.0F,
                            light,
                            overlay
                    );
                    ci.cancel();
                }

                if (GloriousAnimConfig.isTorchItem(stack.getItem()) && stack.getItem() instanceof BlockItem blockItem) {

                    BlockRenderManager blockRenderManager = client.getBlockRenderManager();
                    BlockState state = blockItem.getBlock().getDefaultState();
                    BakedModel model = blockRenderManager.getModel(state);

                    matrices.scale(0.75f,0.75f,0.75f);
                    matrices.translate(-0.5,-0.5,-0.5);
                    matrices.translate(0,0.40,0.15);
                    blockRenderManager.getModelRenderer().render(
                            matrices.peek(),
                            vertexConsumers.getBuffer(RenderLayers.getBlockLayer(state)),
                            state,
                            model,
                            1.0F, 1.0F, 1.0F,
                            light,
                            overlay
                    );
                    ci.cancel();
                }
            }

            lastYawMap.put(entityId, smoothYaw);
            lastXMap.put(entityId, smoothX);
            lastZMap.put(entityId, smoothZ);


        }
    }
}
