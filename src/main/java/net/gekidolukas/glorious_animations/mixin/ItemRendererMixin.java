package net.gekidolukas.glorious_animations.mixin;

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
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    // Mapping von Items zu den entsprechenden BlockStates
    private static final Map<Item, BlockState> ITEM_TO_BLOCK_MAP = new HashMap<>();

    static {
        // Beispiel: Das Laternen-Item soll als Blockmodell der Laterne gerendert werden.
        ITEM_TO_BLOCK_MAP.put(
                Registries.ITEM.get(Identifier.of("minecraft", "lantern")),
                net.minecraft.block.Blocks.LANTERN.getDefaultState()
        );
        // Weitere Items können hier ergänzt werden
    }

    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At("HEAD"), cancellable = true)
    private void onRenderItem(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel oldModel, CallbackInfo ci) {
        if (GloriousAnimConfig.isLanternItem(stack.getItem()) && stack.getItem() instanceof BlockItem blockItem &&
                (renderMode == ModelTransformationMode.THIRD_PERSON_LEFT_HAND ||
                 renderMode == ModelTransformationMode.THIRD_PERSON_RIGHT_HAND)) {

            MinecraftClient client = MinecraftClient.getInstance();
            BlockRenderManager blockRenderManager = client.getBlockRenderManager();
            BlockState state = blockItem.getBlock().getDefaultState();
            BakedModel model = blockRenderManager.getModel(state);


            matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(7));
            matrices.scale(0.75f,0.75f,0.75f);
            matrices.translate(-0.5,-0.5,-0.5);
            matrices.translate(0,-0.45,0.1);
            // Rendern des Blockmodells statt des normalen Item-Modells
            blockRenderManager.getModelRenderer().render(
                    matrices.peek(),
                    vertexConsumers.getBuffer(RenderLayers.getBlockLayer(state)),
                    state,
                    model,
                    1.0F, 1.0F, 1.0F,
                    light,
                    overlay
            );
            // Verhindere das Standard-Rendering
            ci.cancel();
        }
    }
}
