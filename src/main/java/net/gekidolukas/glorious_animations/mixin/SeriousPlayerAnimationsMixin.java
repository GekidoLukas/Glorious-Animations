package net.gekidolukas.glorious_animations.mixin;

import com.mojang.authlib.GameProfile;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.api.layered.modifier.AdjustmentModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Vec3f;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import net.gekidolukas.glorious_animations.AdvancedLayer;
import net.gekidolukas.glorious_animations.CommonAnimations;
import static net.gekidolukas.glorious_animations.CommonAnimations.*;

import net.gekidolukas.glorious_animations.IExampleAnimatedPlayer;
import net.gekidolukas.glorious_animations.compat.*;
import net.gekidolukas.glorious_animations.config.GloriousAnimConfig;
import net.gekidolukas.glorious_animations.interfaces.SwingTypeGetter;
import net.gekidolukas.glorious_animations.interfaces.TorsoPosGetter;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.entity.mob.ZombieHorseEntity;
import net.minecraft.entity.passive.DonkeyEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.MuleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.ChestBoatEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.item.*;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static dev.kosmx.playerAnim.api.TransformType.POSITION;
import static dev.kosmx.playerAnim.api.TransformType.ROTATION;
import static dev.kosmx.playerAnim.core.util.Ease.INOUTSINE;
import static java.lang.Math.*;
import static net.gekidolukas.glorious_animations.GloriousAnimations.*;
import static net.minecraft.util.Hand.MAIN_HAND;
import static net.minecraft.util.Hand.OFF_HAND;


@Unique
@Mixin(AbstractClientPlayerEntity.class)

public abstract class SeriousPlayerAnimationsMixin extends PlayerEntity implements IExampleAnimatedPlayer, TorsoPosGetter {



    @Shadow @Final public ClientWorld clientWorld;
    @Shadow public abstract void tick();



    public SeriousPlayerAnimationsMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }



    @Unique
    AdvancedLayer OVERLAY_LAYER = new AdvancedLayer(); //TODO Change into AttackLayer and InteractHold Layer
    @Unique
    AdvancedLayer ATTACK_LAYER = new AdvancedLayer();
    @Unique
    AdvancedLayer PASSIVE_LAYER = new AdvancedLayer();
    @Unique
    AdvancedLayer LEFT_HOLD_LAYER = new AdvancedLayer();
    @Unique
    AdvancedLayer RIGHT_HOLD_LAYER = new AdvancedLayer();
    @Unique
    AdvancedLayer MAIN_LAYER = new AdvancedLayer();


    @Unique
    public boolean isStillSprinting() {
        if(this.isSprinting()) {
            sprintTicks = 5;
        }
        return sprintTicks > 0;
    }

    private int sprintTicks = 0;


    public Vec3f torso2 = new Vec3f(0, 0, 0);
    public Vec3f torsoRotation2 = new Vec3f(0, 0, 0);
    public Vec3f torsoPos = new Vec3f(0, 0, 0);
    public Vec3f torsoRotation = new Vec3f(0, 0, 0);
    public Vec3f zero = new Vec3f(0, 0, 0);


    public KeyframeAnimation.AnimationBuilder builder = null;





    




    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void init(ClientWorld world, GameProfile profile, CallbackInfo info) {
        CommonAnimations.reloadAnimationVariables();


        PlayerAnimationAccess.getPlayerAnimLayer((AbstractClientPlayerEntity) (Object) this).addAnimLayer(1, MAIN_LAYER.animationContainer);
        MAIN_LAYER.animationContainer.addModifierLast(MAIN_LAYER.speedModifier);
        MAIN_LAYER.animationContainer.addModifierLast(MAIN_LAYER.mirrorModifier);
        MAIN_LAYER.mirrorModifier.setEnabled(false);


        PlayerAnimationAccess.getPlayerAnimLayer((AbstractClientPlayerEntity) (Object) this).addAnimLayer(2, OVERLAY_LAYER.animationContainer);
        OVERLAY_LAYER.animationContainer.addModifierLast(RightBowModifier);
        OVERLAY_LAYER.animationContainer.addModifierLast(LeftBowModifier);
        RightBowModifier.enabled = false;
        LeftBowModifier.enabled = false;
        OVERLAY_LAYER.animationContainer.addModifierLast(OVERLAY_LAYER.speedModifier);
        OVERLAY_LAYER.animationContainer.addModifierLast(OVERLAY_LAYER.mirrorModifier);
        OVERLAY_LAYER.mirrorModifier.setEnabled(false);

        PlayerAnimationAccess.getPlayerAnimLayer((AbstractClientPlayerEntity) (Object) this).addAnimLayer(3, ATTACK_LAYER.animationContainer);
        ATTACK_LAYER.animationContainer.addModifierLast(ATTACK_LAYER.mirrorModifier);
        ATTACK_LAYER.animationContainer.addModifierLast(ATTACK_LAYER.speedModifier);
        ATTACK_LAYER.mirrorModifier.setEnabled(false);
        ATTACK_LAYER.animationContainer.addModifierLast(RightHandSwingModifier);
        ATTACK_LAYER.animationContainer.addModifierLast(LeftHandSwingModifier);
        RightHandSwingModifier.enabled = false;
        LeftHandSwingModifier.enabled = false;

        PlayerAnimationAccess.getPlayerAnimLayer((AbstractClientPlayerEntity) (Object) this).addAnimLayer(4, PASSIVE_LAYER.animationContainer);
        PASSIVE_LAYER.animationContainer.addModifierLast(PASSIVE_LAYER.mirrorModifier);
        PASSIVE_LAYER.animationContainer.addModifierLast(PASSIVE_LAYER.speedModifier);
        PASSIVE_LAYER.mirrorModifier.setEnabled(false);

        PlayerAnimationAccess.getPlayerAnimLayer((AbstractClientPlayerEntity) (Object) this).addAnimLayer(5, RIGHT_HOLD_LAYER.animationContainer);
        RIGHT_HOLD_LAYER.animationContainer.addModifierLast(RIGHT_HOLD_LAYER.mirrorModifier);
        RIGHT_HOLD_LAYER.animationContainer.addModifierLast(RIGHT_HOLD_LAYER.speedModifier);
        RIGHT_HOLD_LAYER.mirrorModifier.setEnabled(false);

        PlayerAnimationAccess.getPlayerAnimLayer((AbstractClientPlayerEntity) (Object) this).addAnimLayer(6, LEFT_HOLD_LAYER.animationContainer);
        LEFT_HOLD_LAYER.animationContainer.addModifierLast(LEFT_HOLD_LAYER.mirrorModifier);
        LEFT_HOLD_LAYER.animationContainer.addModifierLast(LEFT_HOLD_LAYER.speedModifier);
        LEFT_HOLD_LAYER.mirrorModifier.setEnabled(false);


        MAIN_LAYER.currentAnimation = IDLE_STANDING;
        OVERLAY_LAYER.currentAnimation = BLANK_LOOP;
        ATTACK_LAYER.currentAnimation = BLANK_LOOP;
        PASSIVE_LAYER.currentAnimation = BLANK_LOOP;
        RIGHT_HOLD_LAYER.currentAnimation = BLANK_LOOP;
        LEFT_HOLD_LAYER.currentAnimation = BLANK_LOOP;


    }


    @Override
    public ModifierLayer<IAnimation> seriousplayeranimations_getModAnimation() {
        return MAIN_LAYER.animationContainer;
    }


    //region Interface overrides
    @Override
    public void disableArms(boolean b) {this.disableArms = b;}
    public boolean disableArms = false;

    @Override
    public void disableRightArmB(boolean b) {this.disableRightArmB = b;}
    public boolean disableRightArmB = false;

    @Override
    public void disableLeftArmB(boolean b) {this.disableLeftArmB = b;}
    public boolean disableLeftArmB = false;

    @Override
    public void disableMainArmB(boolean b) {this.disableMainArmB = b;}
    public boolean disableMainArmB = false;

    @Override
    public void disableOffArmB(boolean b) {this.disableOffArmB = b;}
    public boolean disableOffArmB = false;

    @Override
    public void disableAnimationB(boolean b) {this.disableAnimationB = b;}
    public boolean disableAnimationB = false;

    @Override
    public void disableOverlayB(boolean b) {this.disableOverlayB = b;}
    public boolean disableOverlayB = false;

    @Override
    public void armPosMain(BipedEntityModel.ArmPose pos) {this.armPosMain = pos;}
    public BipedEntityModel.ArmPose armPosMain = BipedEntityModel.ArmPose.EMPTY;

    @Override
    public void armPosOff(BipedEntityModel.ArmPose pos) {this.armPosOff = pos;}
    public BipedEntityModel.ArmPose armPosOff = BipedEntityModel.ArmPose.EMPTY;

    @Override
    public Vec3f getTorsoPos() { return this.torsoPos; }

    @Override
    public Vec3f getTorsoRotation() { return this.torsoRotation; }
    //endregion




    boolean modified = false;
    float byaw = 0;
    float prevbyaw = 0;
    float hyaw = 0;
    float vx = 0;
    float vy = 0;
    float vz = 0;
    double moveSpeed = 0;
    float turn = 0;
    boolean prevOnGround = false;
    Vec3d lastPos = new Vec3d(0, 0, 0);
    boolean swordSeq = true;
    String modifyId = "";
    String prevModifyId = "";
//    String boatString = "";
    int flychecker = 0;
    double vyfly = 0;
    boolean isMovingBackwards = false;
    Item activeItem = null;
    boolean crouched = false;
    double bodyYawRadians = 0;
    int animationTick = 0;
    Hand rightHand = MAIN_HAND;
    Hand leftHand = OFF_HAND;
    Vec3d pos;


    public void disableMainArm() {
        builder = MAIN_LAYER.currentAnimation.mutableCopy();
        var rightArm = builder.getPart("rightArm");
        assert rightArm != null;
        var leftArm = builder.getPart("leftArm");
        assert leftArm != null;

        if (rightHand == MAIN_HAND) {
            rightArm.pitch.setEnabled(false);
            rightArm.yaw.setEnabled(false);
            rightArm.roll.setEnabled(false);
            MAIN_LAYER.currentAnimation = builder.build();
        } else if (leftHand == MAIN_HAND) {
            leftArm.pitch.setEnabled(false);
            leftArm.yaw.setEnabled(false);
            leftArm.roll.setEnabled(false);
            MAIN_LAYER.currentAnimation = builder.build();
        }

    }

    public void disableOffArm() {
        builder = MAIN_LAYER.currentAnimation.mutableCopy();
        var rightArm = builder.getPart("rightArm");
        assert rightArm != null;
        var leftArm = builder.getPart("leftArm");
        assert leftArm != null;


        if (rightHand == OFF_HAND) {
            rightArm.pitch.setEnabled(false);
            rightArm.yaw.setEnabled(false);
            rightArm.roll.setEnabled(false);
            MAIN_LAYER.currentAnimation = builder.build();
        } else if (leftHand == OFF_HAND) {
            leftArm.pitch.setEnabled(false);
            leftArm.yaw.setEnabled(false);
            leftArm.roll.setEnabled(false);
            MAIN_LAYER.currentAnimation = builder.build();
        }

    }

    public void disableRightArm() {
        builder = MAIN_LAYER.currentAnimation.mutableCopy();
        var rightArm = builder.getPart("rightArm");
        assert rightArm != null;
        rightArm.pitch.setEnabled(false);
        rightArm.yaw.setEnabled(false);
        rightArm.roll.setEnabled(false);
        MAIN_LAYER.currentAnimation = builder.build();

    }


    public void disableLeftArm() {
        builder = MAIN_LAYER.currentAnimation.mutableCopy();
        var leftArm = builder.getPart("leftArm");
        assert leftArm != null;
        leftArm.pitch.setEnabled(false);
        leftArm.yaw.setEnabled(false);
        leftArm.roll.setEnabled(false);
        MAIN_LAYER.currentAnimation = builder.build();
    }


    public void disableBothArms() {
        builder = MAIN_LAYER.currentAnimation.mutableCopy();
        var rightArm = builder.getPart("rightArm");
        assert rightArm != null;
        var leftArm = builder.getPart("leftArm");
        assert leftArm != null;
        rightArm.pitch.setEnabled(false);
        rightArm.yaw.setEnabled(false);
        rightArm.roll.setEnabled(false);
        leftArm.pitch.setEnabled(false);
        leftArm.yaw.setEnabled(false);
        leftArm.roll.setEnabled(false);
        MAIN_LAYER.currentAnimation = builder.build();
    }

    public void disableRightArmOverlayPos() {
        builder = MAIN_LAYER.currentAnimation.mutableCopy();
        var rightArm = builder.getPart("rightArm");
        assert rightArm != null;
        rightArm.x.setEnabled(false);
        rightArm.y.setEnabled(false);
        rightArm.z.setEnabled(false);
        MAIN_LAYER.currentAnimation = builder.build();
    }

    public void disableLeftArmOverlayPos() {
        builder = MAIN_LAYER.currentAnimation.mutableCopy();
        var leftArm = builder.getPart("leftArm");
        assert leftArm != null;
        leftArm.x.setEnabled(false);
        leftArm.y.setEnabled(false);
        leftArm.z.setEnabled(false);
        MAIN_LAYER.currentAnimation = builder.build();
    }


    public void loopedToolAnimation(KeyframeAnimation overlay, KeyframeAnimation overlay_sneak, String id, int fade, float speed, int priority) {
        ATTACK_LAYER.fadeTime = fade;
        ATTACK_LAYER.animationSpeed = speed;
        ATTACK_LAYER.priority = priority;
        ATTACK_LAYER.mirrorModifier.setEnabled(rightHand != MAIN_HAND);
        if (crouched) {
            ATTACK_LAYER.currentAnimation = overlay_sneak;
            ATTACK_LAYER.currentAnimationId = id + "_sneak";
        } else {
            ATTACK_LAYER.currentAnimation = overlay;
            ATTACK_LAYER.currentAnimationId = id;
        }


    }


//    public Boolean attackHandIsRight() {
//        return pl
//    }


    public void genericHandswing() {
        if (rightHand.equals(MAIN_HAND)) {
            OVERLAY_LAYER.currentAnimation = GENERIC_HANDSWING;
            OVERLAY_LAYER.currentAnimationId = "generic_handswing_right";
            OVERLAY_LAYER.mirrorModifier.setEnabled(false);
            OVERLAY_LAYER.animationSpeed = 2;
            OVERLAY_LAYER.fadeTime = 2;
            RightHandSwingModifier.enabled = true;

        }
        else if (leftHand.equals(MAIN_HAND)){
            OVERLAY_LAYER.currentAnimation = GENERIC_HANDSWING;
            OVERLAY_LAYER.currentAnimationId = "generic_handswing_left";
            OVERLAY_LAYER.mirrorModifier.setEnabled(true);
            OVERLAY_LAYER.animationSpeed = 2;
            OVERLAY_LAYER.fadeTime = 2;
            LeftHandSwingModifier.enabled = true;

        }



    }


    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        animate();

        if(sprintTicks > 0) {
            this.sprintTicks--;
        }
    }




    AdjustmentModifier RightBowModifier = new AdjustmentModifier((partName) -> {
        float rotationX = 0;
        float rotationY = 0;
        float rotationZ = 0;
        float offsetX = 0;
        float offsetY = 0;
        float offsetZ = 0;
        var pitch = getPitch();
        pitch = (float) Math.toRadians(pitch);
        switch (partName) {
            case "rightArm" -> {
                rotationZ = -pitch;
                offsetY = pitch;
            }
            case "leftArm" -> {
                rotationZ = (float) (-pitch * 0.25);
                offsetY = -pitch;
            }


            default -> {
                return Optional.empty();
            }
        }

        return Optional.of(new AdjustmentModifier.PartModifier(
                new Vec3f(rotationX, rotationY, rotationZ),
                new Vec3f(offsetX, offsetY, offsetZ))
        );
    });

    AdjustmentModifier LeftBowModifier = new AdjustmentModifier((partName) -> {
        float rotationX = 0;
        float rotationY = 0;
        float rotationZ = 0;
        float offsetX = 0;
        float offsetY = 0;
        float offsetZ = 0;
        var pitch = getPitch();
        pitch = (float) Math.toRadians(pitch);
        switch (partName) {
            case "leftArm" -> {
                rotationZ = pitch;
                offsetY = -pitch;
            }
            case "rightArm" -> {
                rotationZ = (float) (pitch * 0.25);
                offsetY = pitch;
            }


            default -> {
                return Optional.empty();
            }
        }

        return Optional.of(new AdjustmentModifier.PartModifier(
                new Vec3f(rotationX, rotationY, rotationZ),
                new Vec3f(offsetX, offsetY, offsetZ))
        );
    });

    AdjustmentModifier RightHandSwingModifier = new AdjustmentModifier((partName) -> {
        float rotationX = 0;
        float rotationY = 0;
        float rotationZ = 0;
        float offsetX = 0;
        float offsetY = 0;
        float offsetZ = 0;
        var pitch = getPitch();
        pitch = (float) Math.toRadians(pitch);
        if (partName.equals("rightArm")) {
            rotationX = pitch;
        } else {
            return Optional.empty();
        }

        return Optional.of(new AdjustmentModifier.PartModifier(
                new Vec3f(rotationX, rotationY, rotationZ),
                new Vec3f(offsetX, offsetY, offsetZ))
        );
    });

    AdjustmentModifier LeftHandSwingModifier = new AdjustmentModifier((partName) -> {
        float rotationX = 0;
        float rotationY = 0;
        float rotationZ = 0;
        float offsetX = 0;
        float offsetY = 0;
        float offsetZ = 0;
        var pitch = getPitch();
        pitch = (float) Math.toRadians(pitch);
        if (partName.equals("leftArm")) {
            rotationX = pitch;
        } else {
            return Optional.empty();
        }

        return Optional.of(new AdjustmentModifier.PartModifier(
                new Vec3f(rotationX, rotationY, rotationZ),
                new Vec3f(offsetX, offsetY, offsetZ))
        );
    });


    public void animate() {
        AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) (Object)this;

        animationTick++;

        LeftHandSwingModifier.enabled = false;
        RightHandSwingModifier.enabled = false;

        if (getMainArm() == Arm.LEFT) {
            rightHand = OFF_HAND;
            leftHand = MAIN_HAND;
        } else {
            rightHand = MAIN_HAND;
            leftHand = OFF_HAND;
        }


        MAIN_LAYER.speedModifier.speed = MAIN_LAYER.animationSpeed;
        OVERLAY_LAYER.speedModifier.speed = OVERLAY_LAYER.animationSpeed;
        ATTACK_LAYER.speedModifier.speed = ATTACK_LAYER.animationSpeed;
        PASSIVE_LAYER.speedModifier.speed = PASSIVE_LAYER.animationSpeed;
        RIGHT_HOLD_LAYER.speedModifier.speed = RIGHT_HOLD_LAYER.animationSpeed;
        LEFT_HOLD_LAYER.speedModifier.speed = LEFT_HOLD_LAYER.animationSpeed;

        byaw = bodyYaw;
        hyaw = headYaw;

        pos = getPos();
        vx = (float) (pos.x - lastPos.x);
        vy = (float) (pos.y - lastPos.y);
        vz = (float) (pos.z - lastPos.z);
        moveSpeed = sqrt(vx*vx + vz*vz);
        turn = byaw - prevbyaw;
        bodyYawRadians = toRadians(bodyYaw + 90);
        Vector3f movementVector = new Vector3f(vx, 0, vz);
        Vector3f lookVector = new Vector3f((float) cos(bodyYawRadians), 0, (float) sin(bodyYawRadians));
        isMovingBackwards = movementVector.length() > 0 && movementVector.dot(lookVector) < 0;



        OVERLAY_LAYER.currentAnimation = BLANK_LOOP;
        OVERLAY_LAYER.currentAnimationId = "blank_loop";
        OVERLAY_LAYER.fadeTime = 10;
        OVERLAY_LAYER.animationSpeed = 1;
        OVERLAY_LAYER.priority = 0;

        ATTACK_LAYER.currentAnimation = BLANK_LOOP;
        ATTACK_LAYER.currentAnimationId = "blank_loop";
        ATTACK_LAYER.fadeTime = 10;
        ATTACK_LAYER.animationSpeed = 1;
        ATTACK_LAYER.priority = 0;

        PASSIVE_LAYER.currentAnimation = BLANK_LOOP;
        PASSIVE_LAYER.currentAnimationId = "blank_loop";
        PASSIVE_LAYER.fadeTime = 10;
        PASSIVE_LAYER.animationSpeed = 1;
        PASSIVE_LAYER.priority = 0;

        RIGHT_HOLD_LAYER.currentAnimation = BLANK_LOOP;
        RIGHT_HOLD_LAYER.currentAnimationId = "blank_loop";
        RIGHT_HOLD_LAYER.fadeTime = 10;
        RIGHT_HOLD_LAYER.animationSpeed = 1;
        RIGHT_HOLD_LAYER.priority = 0;

        LEFT_HOLD_LAYER.currentAnimation = BLANK_LOOP;
        LEFT_HOLD_LAYER.currentAnimationId = "blank_loop";
        LEFT_HOLD_LAYER.fadeTime = 10;
        LEFT_HOLD_LAYER.animationSpeed = 1;
        LEFT_HOLD_LAYER.priority = 0;


        crouched = isInSneakingPose();

        vyfly = Math.round(vy * 1000.0) / 1000.0;
        if ((vyfly == 0.0 || Math.abs(vyfly) == 0.375 || player.getAbilities().flying) && !isOnGround() && !isInsideWaterOrBubbleColumn()) {
            flychecker++;
        } else if (Math.abs(vyfly) > 0.375 || isOnGround()) {
            flychecker = 0;
        }


        if(flychecker > 10) {
            MAIN_LAYER.currentAnimation = IDLE_CREATIVE_FLYING;
            MAIN_LAYER.currentAnimationId = "idle_creative_flying";

            MAIN_LAYER.fadeTime = 5;
            MAIN_LAYER.animationSpeed = 1;
            MAIN_LAYER.priority = 0;
        }
        else {
            if(!crouched) {
                if(!isMovingBackwards) {
                    // Sprinting
                    if(isStillSprinting() || moveSpeed > 0.23)
                    {

                        if (((3 / 0.28) * moveSpeed) > 1) {
                            MAIN_LAYER.animationSpeed = (float) ((3 / 0.28) * moveSpeed);
                        } else {
                            MAIN_LAYER.animationSpeed = 1 ;
                        }
                        MAIN_LAYER.currentAnimation = RUNNING;
                        MAIN_LAYER.currentAnimationId = "running";
                        MAIN_LAYER.fadeTime = 5;
                        MAIN_LAYER.priority = 0;

                    }
                    // Walking
                    else if(moveSpeed > 0)
                    {
                        if (((9 / 0.22) * moveSpeed) > 1) {
                            MAIN_LAYER.animationSpeed = (float) ((9 / 0.22) * moveSpeed);
                        } else {
                            MAIN_LAYER.animationSpeed = 2;
                        }
                        MAIN_LAYER.currentAnimation = WALKING;
                        MAIN_LAYER.currentAnimationId = "walking";
//                    LOGGER.info("Walking");

                        MAIN_LAYER.fadeTime = 7;
                        MAIN_LAYER.priority = 0;
                    }
                    // Standing and Turning
                    else
                    {
                        if (turn != 0) {
                            MAIN_LAYER.currentAnimation = (turn < 0) ? TURN_LEFT : TURN_RIGHT;
                            MAIN_LAYER.currentAnimationId = (turn < 0) ? "turn_left" : "turn_right";
                            MAIN_LAYER.priority = 0;


                            if ((abs((((float) 1 / 2) * turn)) > 5)) {
                                MAIN_LAYER.animationSpeed = 2f;
                            } else {
                                MAIN_LAYER.animationSpeed = abs((((float) 1 / 2) * turn) );
                            }

                        } else {
                            MAIN_LAYER.currentAnimation = IDLE_STANDING;
                            MAIN_LAYER.currentAnimationId = "idle_standing";

                            if (MAIN_LAYER.prevAnimationId.equals("idle_sneak")
                                    || MAIN_LAYER.prevAnimationId.equals("walking_sneak")
                                    || MAIN_LAYER.prevAnimationId.equals("jump")
                            ) {

                                MAIN_LAYER.fadeTime = 1;
                            } else {
                                MAIN_LAYER.fadeTime = 10;
                            }
                            MAIN_LAYER.animationSpeed = 1;
                            MAIN_LAYER.priority = 0;
                        }
                    }
                }
                // Walking Backwards
                else
                {
                    if (((4 / 0.22) * moveSpeed) > 1) {
                        MAIN_LAYER.animationSpeed = (float) ((4 / 0.22) * moveSpeed);
                    } else {
                        MAIN_LAYER.animationSpeed = 2;
                    }
                    if (vy > 0) {
                        MAIN_LAYER.animationSpeed = 0.1F;
                    }
                    MAIN_LAYER.currentAnimation = WALKING_BACKWARDS;
                    MAIN_LAYER.currentAnimationId = "walking_backwards";

                    MAIN_LAYER.fadeTime = 7;
                    MAIN_LAYER.priority = 0;

                }
            }
            else {
                // Standing while Sneaking
                if(moveSpeed == 0) {
                    MAIN_LAYER.currentAnimation = IDLE_SNEAK;
                    MAIN_LAYER.currentAnimationId = "idle_sneak";

                    MAIN_LAYER.animationSpeed = 1;

                    if (MAIN_LAYER.prevAnimationId.equals("walking_sneak") || MAIN_LAYER.prevAnimationId.equals("walking_sneak_backwards")) {
                        MAIN_LAYER.fadeTime = 10;
                    } else {
                        MAIN_LAYER.fadeTime = 1;
                    }
                    MAIN_LAYER.priority = 0;
                }
                // Walking while Sneaking
                else {
                    if(!isMovingBackwards) {
                        MAIN_LAYER.currentAnimation = WALKING_SNEAK;
                        MAIN_LAYER.currentAnimationId = "walking_sneak";

                        MAIN_LAYER.animationSpeed = (float) ((2 / 0.06) * moveSpeed);
                        if (MAIN_LAYER.animationSpeed < 1) {
                            MAIN_LAYER.animationSpeed = 1 ;
                        }
                        if (MAIN_LAYER.prevAnimationId.equals("idle_sneak") || MAIN_LAYER.prevAnimationId.equals("walking_sneak_backwards")) {
                            MAIN_LAYER.fadeTime = 7;
                        } else {
                            MAIN_LAYER.fadeTime = 1;
                        }
                        MAIN_LAYER.priority = 0;

                    }
                    // Walking backwards while Sneaking
                    else
                    {
                        MAIN_LAYER.currentAnimation = WALKING_SNEAK_BACKWARDS;
                        MAIN_LAYER.currentAnimationId = "walking_sneak_backwards";

                        MAIN_LAYER.animationSpeed = (float) ((2 / 0.06) * moveSpeed );
                        if (MAIN_LAYER.animationSpeed < 1) {
                            MAIN_LAYER.animationSpeed = 1 ;
                        }
                        if (MAIN_LAYER.prevAnimationId.equals("idle_sneak")
                                || MAIN_LAYER.prevAnimationId.equals("walking_sneak")) {
                            MAIN_LAYER.fadeTime = 7;
                        } else {
                            MAIN_LAYER.fadeTime = 1;
                        }
                        MAIN_LAYER.priority = 0;
                    }
                }

            }
        }




        //jumping
        if (vy > 0 && !isOnGround()) {
            MAIN_LAYER.animationSpeed = MAIN_LAYER.currentAnimationId.equals("walking") ? 4 : 1;
            MAIN_LAYER.priority = 0;
        }



        //falling
        if (vy < -0.6 && !hasVehicle() && !isOnGround()) {

            if(getMainHandStack().getItem() instanceof MaceItem ) {
                MAIN_LAYER.currentAnimation = FALLING_MACE;
                MAIN_LAYER.currentAnimationId = "falling_mace";
            } else {
                MAIN_LAYER.currentAnimation = FALLING;
                MAIN_LAYER.currentAnimationId = "falling";
            }


            MAIN_LAYER.fadeTime = 8;
            MAIN_LAYER.animationSpeed = 1;
            MAIN_LAYER.priority = 0;
        } else if (!isOnGround()) {
            MAIN_LAYER.animationSpeed = (MAIN_LAYER.currentAnimationId.equals("walking")) ? 4 : 1;
            MAIN_LAYER.priority = 0;
        }

        //climbing
        if (!isOnGround() && !hasVehicle()) {
            var block = clientWorld.getBlockState(getBlockPos()).getBlock();
            if ((block instanceof LadderBlock || block instanceof VineBlock)) {
                MAIN_LAYER.animationSpeed = 3 ;
                MAIN_LAYER.fadeTime = 7;
                MAIN_LAYER.priority = 0;
                if (!(getActiveItem().getItem() instanceof BowItem)){
                    String blockStateString = String.valueOf(clientWorld.getBlockState(getBlockPos()));
                    byaw = getBodyYaw();
                    hyaw = getHeadYaw();
                    if (blockStateString.contains("facing=north") || blockStateString.contains("south=true")) {
                        byaw = 0;

                    } else if (blockStateString.contains("facing=south") || blockStateString.contains("north=true")) {
                        byaw = 180;

                    } else if (blockStateString.contains("facing=west") || blockStateString.contains("east=true")) {
                        byaw = 270;

                    } else if (blockStateString.contains("facing=east") || blockStateString.contains("west=true")) {
                        byaw = 90;

                    }

                    byaw = ((byaw % 360) + 360) % 360;
                    hyaw = ((hyaw % 360) + 360) % 360;
                    setBodyYaw(byaw);
                    hyaw = hyaw - byaw;
                    hyaw = ((hyaw % 360) + 360) % 360;

                    if (hyaw > 90 && hyaw <= 180) {
                        setHeadYaw(byaw + 90);
                    } else if (hyaw > 180 && hyaw < 270) {
                        setHeadYaw(byaw + 270);
                    }
                }

                if (isClimbing() && vy > 0) {
                    if (crouched) {
                        MAIN_LAYER.currentAnimation = CLIMBING_SNEAK;
                        MAIN_LAYER.currentAnimationId = "climbing_sneak";
                    } else {
                        MAIN_LAYER.currentAnimation = CLIMBING;
                        MAIN_LAYER.currentAnimationId = "climbing";
                    }
                } else if (isClimbing() && vy < 0) {
                    MAIN_LAYER.currentAnimation = CLIMBING_BACKWARDS;
                    MAIN_LAYER.currentAnimationId = "climbing_backwards";
                } else if (crouched) {
                    MAIN_LAYER.currentAnimation = IDLE_CLIMBING_SNEAK;
                    MAIN_LAYER.currentAnimationId = "idle_climbing_sneak";
                } else {
                    MAIN_LAYER.currentAnimation = IDLE_CLIMBING;
                    MAIN_LAYER.currentAnimationId = "idle_climbing";
                }
            } else if ((block instanceof TwistingVinesPlantBlock
                    || block instanceof WeepingVinesPlantBlock
                    || block instanceof TwistingVinesBlock
                    || block instanceof WeepingVinesBlock
                    || block instanceof ScaffoldingBlock)
                    ) {
                MAIN_LAYER.animationSpeed = 3;
                MAIN_LAYER.fadeTime = 7;
                MAIN_LAYER.priority = 0;
                if (!(getActiveItem().getItem() instanceof BowItem)) {
                    byaw = ((float) toDegrees(atan2((getBlockPos().getZ() + 0.5 - pos.z), (getBlockPos().getX()) + 0.5 - pos.x)) - 90);
                    hyaw = headYaw;
                    byaw = ((byaw % 360) + 360) % 360;
                    hyaw = ((hyaw % 360) + 360) % 360;
                    setBodyYaw(byaw);
                    hyaw = hyaw - byaw;
                    hyaw = ((hyaw % 360) + 360) % 360;

                    if (hyaw > 90 && hyaw <= 180) {
                        setHeadYaw(byaw + 90);
                    } else if (hyaw > 180 && hyaw < 270) {
                        setHeadYaw(byaw + 270);
                    }
                }

                if (isClimbing()) {
                    if (vy > 0) {
                        MAIN_LAYER.currentAnimation = crouched ? CLIMBING_SNEAK : CLIMBING;
                        MAIN_LAYER.currentAnimationId = crouched ? "climbing_sneak" : "climbing";
                    } else if (vy < 0) {
                        MAIN_LAYER.currentAnimation = CLIMBING_BACKWARDS;
                        MAIN_LAYER.currentAnimationId = "climbing_backwards";
                    } else {
                        MAIN_LAYER.currentAnimation = crouched ? IDLE_CLIMBING_SNEAK : IDLE_CLIMBING;
                        MAIN_LAYER.currentAnimationId = crouched ? "idle_climbing_sneak" : "idle_climbing";
                    }
                } else {
                    MAIN_LAYER.currentAnimation = crouched ? IDLE_CLIMBING_SNEAK : IDLE_CLIMBING;
                    MAIN_LAYER.currentAnimationId = crouched ? "idle_climbing_sneak" : "idle_climbing";
                }

            } else if (block instanceof PowderSnowBlock) {
                MAIN_LAYER.fadeTime = 7;
                MAIN_LAYER.priority = 0;

                if ((String.valueOf(getArmorItems())).contains("leather_boots")) {
                    if (vy > 0) {
                        if (crouched) {
                            MAIN_LAYER.currentAnimation = CLIMBING_SNEAK;
                            MAIN_LAYER.currentAnimationId = "climbing_sneak";
                        } else {
                            MAIN_LAYER.currentAnimation = CLIMBING;
                            MAIN_LAYER.currentAnimationId = "climbing";
                        }
                    } else if (vy < 0) {
                        MAIN_LAYER.currentAnimation = CLIMBING_BACKWARDS;
                        MAIN_LAYER.currentAnimationId = "climbing_backwards";
                    } else if (crouched) {
                        MAIN_LAYER.currentAnimation = IDLE_CLIMBING_SNEAK;
                        MAIN_LAYER.currentAnimationId = "idle_climbing_sneak";
                    } else {
                        MAIN_LAYER.currentAnimation = IDLE_CLIMBING;
                        MAIN_LAYER.currentAnimationId = "idle_climbing";
                    }
                }
            }

        }

        //crawling
        if (isCrawling()) {
            if (moveSpeed > 0 && !isMovingBackwards){
                MAIN_LAYER.currentAnimation = CRAWLING;
                MAIN_LAYER.currentAnimationId = "crawling";

                MAIN_LAYER.animationSpeed = (float) ((2 / 0.06) * moveSpeed);
                if (MAIN_LAYER.animationSpeed < 1) {
                    MAIN_LAYER.animationSpeed = 1;
                }
            } else if (moveSpeed > 0){
                MAIN_LAYER.currentAnimation = CRAWLING_BACKWARDS;
                MAIN_LAYER.currentAnimationId = "crawling_backwards";

                MAIN_LAYER.animationSpeed = (float) ((2 / 0.06) * moveSpeed);
                if (MAIN_LAYER.animationSpeed < 1) {
                    MAIN_LAYER.animationSpeed = 1 ;
                }
            } else {
                MAIN_LAYER.currentAnimation = IDLE_CRAWLING;
                MAIN_LAYER.currentAnimationId = "idle_crawling";

                MAIN_LAYER.animationSpeed = 2 ;
            }

            MAIN_LAYER.fadeTime = 7;
            MAIN_LAYER.priority = 0;


        }


        //in water
        if ((isInsideWaterOrBubbleColumn() || isInLava()) && !isOnGround() && !isInSwimmingPose()){
            if (moveSpeed > 0 && !isMovingBackwards) {
                MAIN_LAYER.currentAnimation = FORWARD_IN_WATER;
                MAIN_LAYER.currentAnimationId = "forward_in_water";

                MAIN_LAYER.animationSpeed = 1 ;
                MAIN_LAYER.fadeTime = 7;
                MAIN_LAYER.priority = 0;
            } else if (moveSpeed > 0) {
                MAIN_LAYER.currentAnimation = BACKWARDS_IN_WATER;
                MAIN_LAYER.currentAnimationId = "backwards_in_water";

                MAIN_LAYER.animationSpeed = 1;
                MAIN_LAYER.fadeTime = 7;
                MAIN_LAYER.priority = 0;
            } else if (vy > 0) {
                MAIN_LAYER.currentAnimation = UP_IN_WATER;
                MAIN_LAYER.currentAnimationId = "up_in_water";

                MAIN_LAYER.animationSpeed = 1 ;
                MAIN_LAYER.fadeTime = 7;
                MAIN_LAYER.priority = 0;
            } else {
                MAIN_LAYER.currentAnimation = IDLE_IN_WATER;
                MAIN_LAYER.currentAnimationId = "idle_in_water";

                MAIN_LAYER.fadeTime = 5;
                MAIN_LAYER.animationSpeed = 1;
                MAIN_LAYER.priority = 0;
            }
        }
        else if (isInsideWaterOrBubbleColumn() && isInSwimmingPose()) {
            MAIN_LAYER.currentAnimation = SWIMMING;
            MAIN_LAYER.currentAnimationId = "swimming";

            MAIN_LAYER.animationSpeed = 1 ;
            MAIN_LAYER.fadeTime = 7;
            MAIN_LAYER.priority = 0;
        }


        //riding
        if (hasVehicle()) {
            var vehicle = getVehicle();
            if(vehicle instanceof MinecartEntity) {
                MAIN_LAYER.currentAnimation = MINECART_IDLE;
                MAIN_LAYER.currentAnimationId = "minecart_idle";

                MAIN_LAYER.animationSpeed = 1;
                MAIN_LAYER.fadeTime = 2;
                MAIN_LAYER.priority = 0;
            } else if (vehicle instanceof HorseEntity
                    || vehicle instanceof SkeletonHorseEntity
                    || vehicle instanceof ZombieHorseEntity
                    || vehicle instanceof DonkeyEntity
                    || vehicle instanceof MuleEntity) {
                if (moveSpeed > 0 && !isMovingBackwards) {
                    MAIN_LAYER.currentAnimation = HORSE_RUNNING;
                    MAIN_LAYER.currentAnimationId = "horse_running";

                } else {
                    MAIN_LAYER.fadeTime = 5;
                    MAIN_LAYER.currentAnimation = HORSE_IDLE;
                    MAIN_LAYER.currentAnimationId = "horse_idle";

                }
                if (isUsingItem()) {
                    MAIN_LAYER.currentAnimation = HORSE_IDLE;
                    MAIN_LAYER.currentAnimationId = "horse_idle";

                }

                MAIN_LAYER.animationSpeed = 1;
                MAIN_LAYER.fadeTime = 1;
                MAIN_LAYER.priority = 0;
            } else if (vehicle instanceof BoatEntity || vehicle instanceof ChestBoatEntity) {
//                boatString = String.valueOf(getVehicle());
                MAIN_LAYER.currentAnimation = BOAT1;
                MAIN_LAYER.currentAnimationId = "boat1";

                MAIN_LAYER.animationSpeed = 1;
                MAIN_LAYER.fadeTime = 1;
                MAIN_LAYER.priority = 0;
            } else {
                MAIN_LAYER.currentAnimation = HORSE_IDLE;
                MAIN_LAYER.currentAnimationId = "horse_idle";

                MAIN_LAYER.animationSpeed = 1;
                MAIN_LAYER.fadeTime = 1;
                MAIN_LAYER.priority = 0;
            }
        }

        //elytra
        if (isFallFlying()) {
            MAIN_LAYER.currentAnimation = ELYTRA;
            MAIN_LAYER.currentAnimationId = "elytra";

            MAIN_LAYER.animationSpeed = 1;
            MAIN_LAYER.priority = 0;
            MAIN_LAYER.fadeTime = 5;
        }

        //sleeping
        if (isSleeping()) {
            MAIN_LAYER.fadeTime = 2;
            MAIN_LAYER.animationSpeed = 2;
            MAIN_LAYER.priority = 0;
            MAIN_LAYER.disableAnimation();

            OVERLAY_LAYER.currentAnimation = SLEEPING;
            OVERLAY_LAYER.currentAnimationId = "sleeping";

        }

        if (isUsingItem()) {
            activeItem = getActiveItem().getItem();


            if (activeItem.getComponents().contains(DataComponentTypes.FOOD)) {
                //eating


                if (true) {
                    OVERLAY_LAYER.fadeTime = 9;
                    OVERLAY_LAYER.animationSpeed = 1;
                    OVERLAY_LAYER.priority = 0;



                    if (getActiveHand().equals(rightHand)) {
                        OVERLAY_LAYER.currentAnimation = EATING_RIGHT;
                        OVERLAY_LAYER.currentAnimationId = "eating_right";

                        disableRightArmOverlayPos();
                        OVERLAY_LAYER.mirrorModifier.setEnabled(false);
                    } else if (getActiveHand().equals(leftHand)) {
                        OVERLAY_LAYER.currentAnimation = EATING_RIGHT;
                        OVERLAY_LAYER.currentAnimationId = "eating_left";

                        disableLeftArmOverlayPos();
                        OVERLAY_LAYER.mirrorModifier.setEnabled(true);
                    }






                }

            } else
            if (isUsingSpyglass()) {
                //spyglass
                if (getActiveHand().equals(MAIN_HAND)) {
                    disableMainArmB = true;
                    //modifyId = "spy_right";
                } else {
                    disableOffArmB = true;
                    //modifyId = "spy_left";
                }
                MAIN_LAYER.fadeTime = 1;
                MAIN_LAYER.priority = 0;
            } else
            if (activeItem instanceof TridentItem) {
                //trident
                if (true){
                    OVERLAY_LAYER.fadeTime = 5;
                    OVERLAY_LAYER.animationSpeed = 1;

                    if (getActiveHand().equals(rightHand)) {
                        if (crouched){
                            disableRightArmOverlayPos();
                            disableLeftArmOverlayPos();
                        } else {
                            OVERLAY_LAYER.currentAnimation = TRIDENT_DRAW;
                            OVERLAY_LAYER.currentAnimationId = "right_trident";
                        }
                        setBodyYaw(hyaw+55);
                        OVERLAY_LAYER.mirrorModifier.setEnabled(false);
                    } else if (getActiveHand().equals(leftHand)) {
                        if (crouched){
                            disableRightArmOverlayPos();
                            disableLeftArmOverlayPos();
                        } else {
                            OVERLAY_LAYER.currentAnimation = TRIDENT_DRAW;
                            OVERLAY_LAYER.currentAnimationId = "left_trident";
                        }
                        setBodyYaw(hyaw-55);
                        OVERLAY_LAYER.mirrorModifier.setEnabled(true);
                    }

                } else {
                    if (getActiveHand().equals(MAIN_HAND)) {
                        disableMainArmB = true;
                        //modifyId = "trident_right";
                    } else {
                        disableOffArmB = true;
                        //modifyId = "trident_left";
                    }
                    MAIN_LAYER.priority = 0;
                    MAIN_LAYER.fadeTime = 1;
                }


            } else
            if (activeItem instanceof BrushItem) {
                //brush
                if (getActiveHand().equals(MAIN_HAND)) {
                    disableMainArmB = true;
                    //modifyId = "brush_right";
                } else {
                    disableOffArmB = true;
                    //modifyId = "brush_left";
                }


                MAIN_LAYER.priority = 0;
            } else
            if (activeItem instanceof GoatHornItem) {
                //goat horn
                if (getActiveHand().equals(MAIN_HAND)) {
                    disableMainArmB = true;
                    //modifyId = "horn_right";
                } else {
                    disableOffArmB = true;
                    //modifyId = "horn_left";
                }
                MAIN_LAYER.fadeTime = 1;
                MAIN_LAYER.priority = 0;
            } else
            if (activeItem instanceof BowItem) {
                //bow
                if (hasVehicle() || isCrawling()) {
                    disableRightArm();
                    disableLeftArm();
                    modifyId = "bow_idle";
                    MAIN_LAYER.fadeTime = 1;


                } else
                {
                    OVERLAY_LAYER.fadeTime = 6;
                    OVERLAY_LAYER.animationSpeed = 1;
                    OVERLAY_LAYER.priority = 0;


                    if (getActiveHand().equals(rightHand)) {
                        if (crouched){
                            if(moveSpeed > 0 ) { //TODO Why does this not work tf?
                                OVERLAY_LAYER.currentAnimation = BOW_SNEAK_WALKING;
                                OVERLAY_LAYER.currentAnimationId = "right_bow_sneak_walking";
                            } else {
                                OVERLAY_LAYER.currentAnimation = BOW_SNEAK;
                                OVERLAY_LAYER.currentAnimationId = "right_bow_sneak";
                            }
                            OVERLAY_LAYER.fadeTime = 1;
                        } else {
                            OVERLAY_LAYER.currentAnimation = BOW_IDLE;
                            OVERLAY_LAYER.currentAnimationId = "right_bow_idle";
                        }
                        disableRightArmOverlayPos();

                        RightBowModifier.enabled = true;
                        setBodyYaw(hyaw-90);
                        OVERLAY_LAYER.mirrorModifier.setEnabled(false);
                    } else if (getActiveHand().equals(leftHand)) {
                        if (crouched){
                            if(moveSpeed > 0 ) {
                                OVERLAY_LAYER.currentAnimation = BOW_SNEAK_WALKING;
                                OVERLAY_LAYER.currentAnimationId = "left_bow_sneak_walking";
                            } else {
                                OVERLAY_LAYER.currentAnimation = BOW_SNEAK;
                                OVERLAY_LAYER.currentAnimationId = "left_bow_sneak";
                            }
                            OVERLAY_LAYER.fadeTime = 1;
                        } else {
                            OVERLAY_LAYER.currentAnimation = BOW_IDLE;
                            OVERLAY_LAYER.currentAnimationId = "left_bow_idle";
                        }
                        disableLeftArmOverlayPos();
                        LeftBowModifier.enabled = true;
                        setBodyYaw(hyaw+90);
                        OVERLAY_LAYER.mirrorModifier.setEnabled(true);
                    }
                }


            } else
            if (activeItem instanceof ShieldItem) {
                //shield
                if (true) {
                    OVERLAY_LAYER.fadeTime = 5;
                    OVERLAY_LAYER.animationSpeed = 1;
                    OVERLAY_LAYER.priority = 0;

                    //ShieldModifier.enabled = true;


                    if (getActiveHand().equals(rightHand)) {
                        if (crouched){
                            OVERLAY_LAYER.currentAnimation = SHIELD_SNEAK;
                            OVERLAY_LAYER.currentAnimationId = "right_shield_sneak";
                            OVERLAY_LAYER.fadeTime = 1;
                        } else {
                            OVERLAY_LAYER.currentAnimation = SHIELD;
                            OVERLAY_LAYER.currentAnimationId = "right_shield";
                        }

                        OVERLAY_LAYER.mirrorModifier.setEnabled(false);
                    } else if (getActiveHand().equals(leftHand)) {
                        if (crouched){
                            OVERLAY_LAYER.currentAnimation = SHIELD_SNEAK;
                            OVERLAY_LAYER.currentAnimationId = "left_shield_sneak";
                            OVERLAY_LAYER.fadeTime = 1;
                        } else {
                            OVERLAY_LAYER.currentAnimation = SHIELD;
                            OVERLAY_LAYER.currentAnimationId = "left_shield";
                        }


                        OVERLAY_LAYER.mirrorModifier.setEnabled(true);
                    }
                } else {
                    if (getActiveHand().equals(MAIN_HAND)) {
                        disableMainArmB = true;
                        //modifyId = "shield_right";
                    } else {
                        disableOffArmB = true;
                        //modifyId = "shield_left";
                    }
                    MAIN_LAYER.priority = 0;
                    MAIN_LAYER.fadeTime = 1;
                }


            } else
            if (activeItem instanceof CrossbowItem) {
                //crossbow
                disableArms = true;


                //modifyId = "crossbow_charging";
                //priority = 0;
            } else
            if (SUPPLEMENTARIES_COMPAT) {
                //flute
                if (SupplementariesFluteCheck.check(activeItem)){
                    disableArms = true;
                    //modifyId = "flute";
                    //priority = 0;
                    //fadeTime = 1;
                }
            } else {

                if (getActiveHand().equals(MAIN_HAND)) {
                    disableMainArmB = true;
                    //modifyId = "general_right";
                } else {
                    disableOffArmB = true;
                    //modifyId = "general_left";
                }

                MAIN_LAYER.priority = 0;
            }
        } else {
            modifyId = "";
        }


        //totem
        if (((SwingTypeGetter)player).getTotemTicks() > 0) {
            MAIN_LAYER.currentAnimation = TOTEM_REVIVE;
            MAIN_LAYER.currentAnimationId = "totem_revive";

            MAIN_LAYER.animationSpeed = 1;
            MAIN_LAYER.priority = 0;
            MAIN_LAYER.fadeTime = 5;
        }





        //handswinging
        if(handSwinging) {
//            LOGGER.info("HandswingTicks: " + handSwingTicks);
            List<ItemEntity> itemEntities = getNearbyItemEntities(player.getPos(),16.0d);

            for(var item : itemEntities) {
                Vec3d eyePos = player.getEyePos();
                double distance = item.getPos().distanceTo(eyePos);
                double speed = player.getVelocity().length();
                if(distance < speed * 10 + 1 && item.getItemAge() < 4) {
                    ((SwingTypeGetter)player).setDropTicks(CommonAnimations.DROP_ITEM.getLength());
                }
            }
            //TODO Distinguish between using, Attacking and breaking Block

            if(((SwingTypeGetter)player).getDropTicks() > 0) {
                loopedToolAnimation(DROP_ITEM, DROP_ITEM_SNEAK, "drop_item", 1, 1, 0);
            }
            else if(((SwingTypeGetter)player).getBlockBreakingTicks() > 0) {

                //sword break
                if (GloriousAnimConfig.hasSwordBreakAnimation(getMainHandStack().getItem()) && !isUsingItem() && preferredHand.equals(MAIN_HAND)) {
                    loopedToolAnimation(PICKAXE_BREAK, PICKAXE_BREAK_SNEAK, "pickaxe_break", 1, 2, 0);

                }
                //pickaxe
                else if (GloriousAnimConfig.hasPickaxeBreakAnimation(getMainHandStack().getItem()) && preferredHand.equals(MAIN_HAND)) {
                    loopedToolAnimation(PICKAXE_BREAK, PICKAXE_BREAK_SNEAK, "pickaxe_break", 1, 2, 0);
                }
                //axe break
                else if (GloriousAnimConfig.hasAxeBreakAnimation(getMainHandStack().getItem()) && preferredHand.equals(MAIN_HAND)) {
                    loopedToolAnimation(AXE_BREAK, AXE_BREAK_SNEAK, "axe_break", 1, 1.5f, 0);
                }
                //shovel
                else if (GloriousAnimConfig.hasShovelBreakAnimation(getMainHandStack().getItem()) && preferredHand.equals(MAIN_HAND)) {
                    loopedToolAnimation(SHOVEL_BREAK, SHOVEL_BREAK_SNEAK, "shovel",  1, 1.5f, 0);
                }
                //hoe
                else if (GloriousAnimConfig.hasHoeBreakAnimation(getMainHandStack().getItem()) && preferredHand.equals(MAIN_HAND)) {
                    loopedToolAnimation(SHOVEL_BREAK, SHOVEL_BREAK_SNEAK, "hoe",  1, 1.5f, 0); //TODO ADD
                }
                else {
                    genericHandswing();
                }
            }
            else {
                //sword attack
                if (GloriousAnimConfig.hasSwordAttackAnimation(getMainHandStack().getItem()) && !isUsingItem() && rightHand.equals(MAIN_HAND)) {
                    ATTACK_LAYER.animationSpeed = 1.4f ;

                    ATTACK_LAYER.fadeTime = 0;
                    ATTACK_LAYER.priority = 1;

                    ATTACK_LAYER.mirrorModifier.setEnabled(rightHand != MAIN_HAND);

                    if (swordSeq) {

                        if (crouched) {
                            ATTACK_LAYER.currentAnimation = SWORD_ATTACK_SNEAK;
                            ATTACK_LAYER.currentAnimationId = "sword_attack_sneak";
                        } else {
                            ATTACK_LAYER.currentAnimation = SWORD_ATTACK;
                            ATTACK_LAYER.currentAnimationId = "sword_attack";
                        }


                    } else {
                        if (crouched) {
                            ATTACK_LAYER.currentAnimation = SWORD_ATTACK_SNEAK_2;
                            ATTACK_LAYER.currentAnimationId = "sword_attack_sneak";
                        } else {
                            ATTACK_LAYER.currentAnimation = SWORD_ATTACK_2;
                            ATTACK_LAYER.currentAnimationId = "sword_attack";
                        }

                    }




                }
                //pickaxe
                else if (GloriousAnimConfig.hasPickaxeAttackAnimation(getMainHandStack().getItem()) && preferredHand.equals(MAIN_HAND)) {
                    loopedToolAnimation(PICKAXE_ATTACK, PICKAXE_ATTACK_SNEAK, "pickaxe_attack", 1, 2, 0);
                }
                //spear-like
                else if (GloriousAnimConfig.hasSpearAttackAnimation(getMainHandStack().getItem()) && preferredHand.equals(MAIN_HAND)) {
                    loopedToolAnimation(SPEAR_ATTACK, SPEAR_ATTACK_SNEAK, "spear_attack", 1, 2, 0);
                }
                //axe
                else if (GloriousAnimConfig.hasAxeAttackAnimation(getMainHandStack().getItem()) && preferredHand.equals(MAIN_HAND)) {
                    loopedToolAnimation(AXE_ATTACK, AXE_ATTACK_SNEAK, "axe_attack", 1, 1.5f, 0);
                }
                //shovel
                else if (GloriousAnimConfig.hasShovelAttackAnimation(getMainHandStack().getItem()) && preferredHand.equals(MAIN_HAND)) {
                    loopedToolAnimation(SHOVEL_ATTACK, SHOVEL_ATTACK_SNEAK, "shovel",  1, 1.5f, 0);
                }
                //hoe
                else if (GloriousAnimConfig.hasHoeAttackAnimation(getMainHandStack().getItem()) && preferredHand.equals(MAIN_HAND)) {
                    loopedToolAnimation(SHOVEL_ATTACK, SHOVEL_ATTACK_SNEAK, "hoe",  1, 1.5f, 0); //TODO ADD
                }
                //mace
                else if (GloriousAnimConfig.hasMaceAttackAnimation(getMainHandStack().getItem()) && !hasVehicle() && !isOnGround() &&  preferredHand.equals(MAIN_HAND)) {
                    loopedToolAnimation(MACE_FALL_ATTACK, MACE_FALL_ATTACK, "mace",  3, 1.5f, 0);
                }
                else if (GloriousAnimConfig.hasPunchAttackAnimation(getMainHandStack().getItem())) {
                    loopedToolAnimation(FIST_ATTACK, FIST_ATTACK, "punch",  1, 1.5f, 0);
                }
                else {
                    genericHandswing();
                }
            }

//            if(isUsingItem()) {
//                ATTACK_LAYER.animationContainer.setAnimation(null);
//                ATTACK_LAYER.currentAnimation = BLANK_LOOP;
//                ATTACK_LAYER.currentAnimationId = "blank_loop";
//            }

        }

        //trident_throw
        if (((SwingTypeGetter)player).getPostTridentThrowTicks() > 0) {
            OVERLAY_LAYER.currentAnimation = TRIDENT_THROW;
            OVERLAY_LAYER.currentAnimationId = "trident_throw";

            OVERLAY_LAYER.animationSpeed = 2.0f;
            OVERLAY_LAYER.prevPriority = 0;
            OVERLAY_LAYER.fadeTime = 5;
        }

        //Lantern
        if(GloriousAnimConfig.isLanternItem(player.getStackInHand(rightHand).getItem())) {

            if(OVERLAY_LAYER.currentAnimationId.contains("bow")) {
                RIGHT_HOLD_LAYER.currentAnimation = BLANK_LOOP;
                RIGHT_HOLD_LAYER.currentAnimationId = "blank_loop";
            } else {
                RIGHT_HOLD_LAYER.currentAnimation = LANTERN_HOLD;
                RIGHT_HOLD_LAYER.currentAnimationId = "lantern_hold_right";
                RIGHT_HOLD_LAYER.mirrorModifier.setEnabled(false);

                RIGHT_HOLD_LAYER.animationSpeed = 1.0f;
                RIGHT_HOLD_LAYER.priority = 0;
                RIGHT_HOLD_LAYER.fadeTime = 5;
            }

        }
        if(GloriousAnimConfig.isLanternItem(player.getStackInHand(leftHand).getItem())) {

            if(OVERLAY_LAYER.currentAnimationId.contains("bow")) {
                LEFT_HOLD_LAYER.currentAnimation = BLANK_LOOP;
                LEFT_HOLD_LAYER.currentAnimationId = "blank_loop";
            } else {
                LEFT_HOLD_LAYER.currentAnimation = LANTERN_HOLD;
                LEFT_HOLD_LAYER.currentAnimationId = "lantern_hold_left";
                LEFT_HOLD_LAYER.mirrorModifier.setEnabled(true);


                LEFT_HOLD_LAYER.animationSpeed = 1.0f;
                LEFT_HOLD_LAYER.priority = 0;
                LEFT_HOLD_LAYER.fadeTime = 5;
            }
        }


        //Torch
        if(GloriousAnimConfig.isTorchItem(player.getStackInHand(rightHand).getItem())) {

            if(OVERLAY_LAYER.currentAnimationId.contains("bow")) {
                RIGHT_HOLD_LAYER.currentAnimation = BLANK_LOOP;
                RIGHT_HOLD_LAYER.currentAnimationId = "blank_loop";
            } else {
                RIGHT_HOLD_LAYER.currentAnimation = TORCH_HOLD;
                RIGHT_HOLD_LAYER.currentAnimationId = "torch_hold_right";
                RIGHT_HOLD_LAYER.mirrorModifier.setEnabled(rightHand != MAIN_HAND);

                RIGHT_HOLD_LAYER.animationSpeed = 1.0f;
                RIGHT_HOLD_LAYER.priority = 0;
                RIGHT_HOLD_LAYER.fadeTime = 5;
            }


        }
        if(GloriousAnimConfig.isTorchItem(player.getStackInHand(leftHand).getItem())) {

            if(OVERLAY_LAYER.currentAnimationId.contains("bow")) {
                LEFT_HOLD_LAYER.currentAnimation = BLANK_LOOP;
                LEFT_HOLD_LAYER.currentAnimationId = "blank_loop";
            } else {
                LEFT_HOLD_LAYER.currentAnimation = TORCH_HOLD;
                LEFT_HOLD_LAYER.currentAnimationId = "torch_hold_left";
                LEFT_HOLD_LAYER.mirrorModifier.setEnabled(rightHand == MAIN_HAND);


                LEFT_HOLD_LAYER.animationSpeed = 1.0f;
                LEFT_HOLD_LAYER.priority = 0;
                LEFT_HOLD_LAYER.fadeTime = 5;
            }


        }





        //region Mod Compats
        Item item = getMainHandStack().getItem();
        if (!getMainHandStack().isEmpty()) {


            if (SWORDBLOCKING_COMPAT) {
                if (SwordBlockingCheck.check(this)) {
                    OVERLAY_LAYER.disableAnimation();
                    disableMainArmB = true;
                    //modifyId = "sword_block";
                    //if (!Objects.equals(prevModifyId, modifyId)) {
                    //    fadeTime = 1;
                    //}
                }
            }

            if (OLDCOMBATMOD_COMPAT) {
                if (OldCombatModCheck.check(this)) {
                    OVERLAY_LAYER.disableAnimation();
                    disableMainArmB = true;
                    //modifyId = "sword_block";
                    //if (!Objects.equals(prevModifyId, modifyId)) {
                    //    fadeTime = 1;
                    //}
                }
            }


            if (IMMERSIVE_MELODIES_COMPAT) {
                if (ImmersiveMelodiesItemCheck.check(item)) {
                    disableArms = true;
                }
            }

            if(ULTRACRAFT_COMPAT) {
                if (UltracraftCheck.isGenericGun(item)) {
                    disableMainArmB = true;

                } else if (UltracraftCheck.isNailgun(item)) {
                    disableArms = true;
                }
            }

            if (armPosMain.equals(BipedEntityModel.ArmPose.CROSSBOW_HOLD) || armPosMain.equals(BipedEntityModel.ArmPose.CROSSBOW_CHARGE)) {
                disableArms = true;
            } else if (armPosMain.equals(BipedEntityModel.ArmPose.BOW_AND_ARROW) && !(item instanceof BowItem)) {
                disableArms = true;
            }

            if (TRIGGERHAPPY_COMPAT) {
                if (TriggerHappyCheck.checkOneHanded(armPosMain)) {
                    disableMainArmB = true;
                } else if (TriggerHappyCheck.checkTwoHanded(armPosMain)) {
                    disableArms = true;
                }
            }
        }

        if (!getOffHandStack().isEmpty()) {
            item = getOffHandStack().getItem();

            if (IMMERSIVE_MELODIES_COMPAT) {
                if (ImmersiveMelodiesItemCheck.check(item)) {
                    disableArms = true;
                }
            }



            if (armPosOff.equals(BipedEntityModel.ArmPose.CROSSBOW_HOLD) || armPosOff.equals(BipedEntityModel.ArmPose.CROSSBOW_CHARGE)) {
                disableArms = true;
            } else if (armPosOff.equals(BipedEntityModel.ArmPose.BOW_AND_ARROW) && !(item instanceof BowItem)) {
                disableArms = true;
            }

            if (TRIGGERHAPPY_COMPAT) {
                if (TriggerHappyCheck.checkOneHanded(armPosOff)) {
                    disableOffArmB = true;
                } else if (TriggerHappyCheck.checkTwoHanded(armPosOff)) {
                    disableArms = true;
                }
            }

        }

        if (CARRYON_COMPAT) {
            CarryOnCheck.check((AbstractClientPlayerEntity) (Object) this);
        }

        //endregion

        //region Disable Body Parts
        if (disableRightArmB) {
            disableRightArm();
            disableRightArmB = false;
            modifyId = "disable_right";
            if (!Objects.equals(prevModifyId, modifyId)) {
                MAIN_LAYER.fadeTime = 1;
            }
        }
        if (disableLeftArmB) {
            disableLeftArm();
            disableLeftArmB = false;
            modifyId = "disable_left";
            if (!Objects.equals(prevModifyId, modifyId)) {
                MAIN_LAYER.fadeTime = 1;
            }
        }
        if (disableMainArmB) {
            disableMainArm();
            disableMainArmB = false;
            modifyId = "disable_main";
            if (!Objects.equals(prevModifyId, modifyId)) {
                MAIN_LAYER.fadeTime = 1;
            }
        }
        if (disableOffArmB) {
            disableOffArm();
            disableOffArmB = false;
            modifyId = "disable_off";
            if (!Objects.equals(prevModifyId, modifyId)) {
                MAIN_LAYER.fadeTime = 1;
            }
        }
        if (disableArms) {
            disableBothArms();
            disableArms = false;
            modifyId = "disable_both";
            if (!Objects.equals(prevModifyId, modifyId)) {
                MAIN_LAYER.fadeTime = 1;
            }

        }
        if (disableAnimationB) {
            MAIN_LAYER.disableAnimation();
            disableAnimationB = false;
        }
        if (disableOverlayB) {
            OVERLAY_LAYER.disableAnimation();
            disableAnimationB = false;
        }

        //endregion




        //region Apply Current Animation
        if ((!Objects.equals(MAIN_LAYER.currentAnimationId, MAIN_LAYER.prevAnimationId) && MAIN_LAYER.priority >= MAIN_LAYER.prevPriority) || !MAIN_LAYER.animationContainer.isActive() || !Objects.equals(modifyId, prevModifyId)) {

            MAIN_LAYER.animationContainer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(MAIN_LAYER.fadeTime, INOUTSINE), new KeyframeAnimationPlayer(MAIN_LAYER.currentAnimation));
            animationTick = 0;

            MAIN_LAYER.prevAnimationId = MAIN_LAYER.currentAnimationId;
            MAIN_LAYER.prevAnimation = MAIN_LAYER.currentAnimation;
            prevModifyId = modifyId;
            MAIN_LAYER.prevPriority = MAIN_LAYER.priority;
            modified = true;
        }

        lastPos = new Vec3d(pos.x, pos.y, pos.z);
        prevOnGround = isOnGround();
        prevbyaw = byaw;

        //endregion

        //region Apply Overlay Animation
        if ((!Objects.equals(OVERLAY_LAYER.currentAnimationId, OVERLAY_LAYER.prevAnimationId) && OVERLAY_LAYER.priority >= OVERLAY_LAYER.prevPriority) || !OVERLAY_LAYER.animationContainer.isActive()){
            RightBowModifier.enabled = false;
            LeftBowModifier.enabled = false;
            //ShieldModifier.enabled = false;

            if(OVERLAY_LAYER.prevAnimationId.contains("trident") && !OVERLAY_LAYER.currentAnimationId.contains("trident")) {
                OVERLAY_LAYER.fadeTime = 3;
            }

            OVERLAY_LAYER.animationContainer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(OVERLAY_LAYER.fadeTime, INOUTSINE), new KeyframeAnimationPlayer(OVERLAY_LAYER.currentAnimation), true);


            OVERLAY_LAYER.prevAnimationId = OVERLAY_LAYER.currentAnimationId;
            OVERLAY_LAYER.prevPriority = OVERLAY_LAYER.priority;
        }


        //endregion


        //region Apply AttackLayer Animation

        if(!(OVERLAY_LAYER.currentAnimationId.isEmpty() || OVERLAY_LAYER.currentAnimationId.equals("blank_loop"))) {
            ATTACK_LAYER.currentAnimation = BLANK_LOOP;
            ATTACK_LAYER.currentAnimationId = "blank_loop";
            ATTACK_LAYER.fadeTime = 6;
            ATTACK_LAYER.animationContainer.setAnimation(null);
        }

        if ((!Objects.equals(ATTACK_LAYER.currentAnimationId, ATTACK_LAYER.prevAnimationId) && ATTACK_LAYER.priority >= ATTACK_LAYER.prevPriority) || !ATTACK_LAYER.animationContainer.isActive()){

            if (ATTACK_LAYER.currentAnimationId.equals("sword_attack") || ATTACK_LAYER.currentAnimationId.equals("sword_attack_sneak")) {
                swordSeq = !swordSeq;
                ATTACK_LAYER.animationContainer.setAnimation(null);
                ATTACK_LAYER.animationContainer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(ATTACK_LAYER.fadeTime, INOUTSINE), new KeyframeAnimationPlayer(ATTACK_LAYER.currentAnimation));

            }else {
                ATTACK_LAYER.animationContainer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(ATTACK_LAYER.fadeTime, INOUTSINE), new KeyframeAnimationPlayer(ATTACK_LAYER.currentAnimation), true);
            }

            ATTACK_LAYER.prevAnimationId = ATTACK_LAYER.currentAnimationId;
            ATTACK_LAYER.prevPriority = ATTACK_LAYER.priority;
        }

        if(handSwingTicks < 1 && (ATTACK_LAYER.currentAnimationId.equals("generic_handswing_right") || ATTACK_LAYER.currentAnimationId.equals("generic_handswing_left"))) {
            ATTACK_LAYER.animationContainer.setAnimation(null);
            ATTACK_LAYER.animationContainer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(ATTACK_LAYER.fadeTime, INOUTSINE), new KeyframeAnimationPlayer(ATTACK_LAYER.currentAnimation), true);
        }

//        if(!(OVERLAY_LAYER.currentAnimationId.isEmpty() || OVERLAY_LAYER.currentAnimationId.equals("blank_loop")) ) {
//            ATTACK_LAYER.animationContainer.setAnimation(null);
//            ATTACK_LAYER.animationContainer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(3, INOUTSINE), new KeyframeAnimationPlayer(BLANK_LOOP));
//        }

        //endregion

        //region Apply Passive Overlay Animation
        if ((!Objects.equals(PASSIVE_LAYER.currentAnimationId, PASSIVE_LAYER.prevAnimationId) && PASSIVE_LAYER.priority >= PASSIVE_LAYER.prevPriority) || !PASSIVE_LAYER.animationContainer.isActive()){

            PASSIVE_LAYER.animationContainer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(PASSIVE_LAYER.fadeTime, INOUTSINE), new KeyframeAnimationPlayer(PASSIVE_LAYER.currentAnimation), true);


            PASSIVE_LAYER.prevAnimationId = PASSIVE_LAYER.currentAnimationId;
            PASSIVE_LAYER.prevPriority = PASSIVE_LAYER.priority;
        }
        //endregion

        //region Apply LANTERN Overlay Animation
        if ((!Objects.equals(RIGHT_HOLD_LAYER.currentAnimationId, RIGHT_HOLD_LAYER.prevAnimationId) && RIGHT_HOLD_LAYER.priority >= RIGHT_HOLD_LAYER.prevPriority) || !PASSIVE_LAYER.animationContainer.isActive()){

            RIGHT_HOLD_LAYER.animationContainer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(RIGHT_HOLD_LAYER.fadeTime, INOUTSINE), new KeyframeAnimationPlayer(RIGHT_HOLD_LAYER.currentAnimation), true);


            RIGHT_HOLD_LAYER.prevAnimationId = RIGHT_HOLD_LAYER.currentAnimationId;
            RIGHT_HOLD_LAYER.prevPriority = RIGHT_HOLD_LAYER.priority;
        }
        //endregion

        //region Apply TORCH Overlay Animation
        if ((!Objects.equals(LEFT_HOLD_LAYER.currentAnimationId, LEFT_HOLD_LAYER.prevAnimationId) && LEFT_HOLD_LAYER.priority >= LEFT_HOLD_LAYER.prevPriority) || !PASSIVE_LAYER.animationContainer.isActive()){

            LEFT_HOLD_LAYER.animationContainer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(LEFT_HOLD_LAYER.fadeTime, INOUTSINE), new KeyframeAnimationPlayer(LEFT_HOLD_LAYER.currentAnimation), true);


            LEFT_HOLD_LAYER.prevAnimationId = LEFT_HOLD_LAYER.currentAnimationId;
            LEFT_HOLD_LAYER.prevPriority = LEFT_HOLD_LAYER.priority;
        }
        //endregion

        //region Write TorsoPos
        if (MAIN_LAYER.animationContainer.isActive()){
            torso2 = OVERLAY_LAYER.animationContainer.get3DTransform("torso", POSITION, 0, zero);
            if (torso2.getZ() == 0 && torso2.getY() == 0) {
                torsoPos = MAIN_LAYER.animationContainer.get3DTransform("torso", POSITION, 0, zero);
            } else {
                torsoPos = torso2;
            }

            torsoRotation2 = OVERLAY_LAYER.animationContainer.get3DTransform("torso", ROTATION, 0,zero);
            if (torsoRotation2.getX() == 0) {
                torsoRotation = MAIN_LAYER.animationContainer.get3DTransform("torso", ROTATION, 0, zero);
            } else {
                torsoRotation = torsoRotation2;
            }

        }
        //endregion



    }

    @Unique
    private static List<ItemEntity> getNearbyItemEntities(Vec3d playerPos, double radius) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return List.of();

        ClientWorld world = client.world;

        Box searchBox = new Box(
                playerPos.x - radius, playerPos.y - radius, playerPos.z - radius,
                playerPos.x + radius, playerPos.y + radius, playerPos.z + radius
        );

        List<ItemEntity> items = world.getEntitiesByClass(ItemEntity.class, searchBox, item -> true);
        return items;
    }


}