package net.gekidolukas.glorious_animations;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import net.minecraft.util.Identifier;

import static dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry.getAnimation;
import static net.gekidolukas.glorious_animations.GloriousAnimations.MOD_ID;

public class CommonAnimations {


    public static KeyframeAnimation BLANK_LOOP = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "blank_loop"));
    public static KeyframeAnimation GENERIC_HANDSWING = null;

    //region MOVEMENT
    public static KeyframeAnimation IDLE_STANDING = null;
    public static KeyframeAnimation IDLE_CREATIVE_FLYING = null;
    public static KeyframeAnimation IDLE_CREATIVE_FLYING_ITEM = null;
    public static KeyframeAnimation WALKING = null;
    public static KeyframeAnimation WALKING_BACKWARDS = null;
    public static KeyframeAnimation RUNNING = null;
    public static KeyframeAnimation TURN_LEFT = null;
    public static KeyframeAnimation TURN_RIGHT = null;
    public static KeyframeAnimation IDLE_SNEAK = null;
    public static KeyframeAnimation WALKING_SNEAK = null;
    public static KeyframeAnimation WALKING_SNEAK_BACKWARDS = null;
    public static KeyframeAnimation FALLING = null;
    public static KeyframeAnimation FALLING_MACE = null;
    public static KeyframeAnimation ELYTRA = null;
    public static KeyframeAnimation IDLE_IN_WATER = null;
    public static KeyframeAnimation FORWARD_IN_WATER = null;
    public static KeyframeAnimation BACKWARDS_IN_WATER = null;
    public static KeyframeAnimation UP_IN_WATER = null;
    public static KeyframeAnimation SWIMMING = null;
    public static KeyframeAnimation CRAWLING = null;
    public static KeyframeAnimation IDLE_CRAWLING = null;
    public static KeyframeAnimation CRAWLING_BACKWARDS = null;
    public static KeyframeAnimation IDLE_CLIMBING = null;
    public static KeyframeAnimation IDLE_CLIMBING_SNEAK = null;
    public static KeyframeAnimation CLIMBING = null;
    public static KeyframeAnimation CLIMBING_SNEAK = null;
    public static KeyframeAnimation CLIMBING_BACKWARDS = null;
    //endregion

    //region EATING
    public static KeyframeAnimation EATING = null;
    public static KeyframeAnimation EATING_RIGHT = null;
    public static KeyframeAnimation EATING_LEFT = null;
    public static KeyframeAnimation EATING_RIGHT_SNEAK = null;
    public static KeyframeAnimation EATING_LEFT_SNEAK = null;
    //endregion

    //region SPECIAL ANIMATIONS
    public static KeyframeAnimation TOTEM_REVIVE = null;
    public static KeyframeAnimation SLEEPING = null;
    public static KeyframeAnimation PARAGLIDER = null;
    public static KeyframeAnimation DROP_ITEM = null;
    public static KeyframeAnimation DROP_ITEM_SNEAK = null;
    //endregion

    //region HOLD POSES
    public static KeyframeAnimation LANTERN_HOLD = null;
    public static KeyframeAnimation TORCH_HOLD = null;
    //endregion

    //region VEHICLE STUFF
    public static KeyframeAnimation HORSE_IDLE = null;
    public static KeyframeAnimation HORSE_RUNNING = null;
    public static KeyframeAnimation HORSE_PICKAXE = null;
    public static KeyframeAnimation MINECART_IDLE = null;
    public static KeyframeAnimation MINECART_PICKAXE = null;
    public static KeyframeAnimation BOAT1 = null;
    //endregion

    //region ATTACKS, BREAKS
    public static KeyframeAnimation PICKAXE_ATTACK = null;
    public static KeyframeAnimation PICKAXE_ATTACK_SNEAK = null;
    public static KeyframeAnimation PICKAXE_BREAK = null;
    public static KeyframeAnimation PICKAXE_BREAK_SNEAK = null;
    public static KeyframeAnimation AXE_ATTACK = null;
    public static KeyframeAnimation AXE_ATTACK_SNEAK = null;
    public static KeyframeAnimation AXE_BREAK = null;
    public static KeyframeAnimation AXE_BREAK_SNEAK = null;
    public static KeyframeAnimation SHOVEL_ATTACK = null;
    public static KeyframeAnimation SHOVEL_ATTACK_SNEAK = null;
    public static KeyframeAnimation SHOVEL_BREAK = null;
    public static KeyframeAnimation SHOVEL_BREAK_SNEAK = null;
    public static KeyframeAnimation HOE_ATTACK = null;
    public static KeyframeAnimation HOE_ATTACK_SNEAK = null;
    public static KeyframeAnimation HOE_BREAK = null;
    public static KeyframeAnimation HOE_BREAK_SNEAK = null;
    public static KeyframeAnimation SWORD_ATTACK = null;
    public static KeyframeAnimation SWORD_ATTACK_2 = null;
    public static KeyframeAnimation SWORD_ATTACK_SNEAK = null;
    public static KeyframeAnimation SWORD_ATTACK_SNEAK_2 = null;
    public static KeyframeAnimation TRIDENT_DRAW = null;
    public static KeyframeAnimation TRIDENT_THROW = null;
    public static KeyframeAnimation SPEAR_ATTACK = null;
    public static KeyframeAnimation SPEAR_ATTACK_SNEAK = null;
    public static KeyframeAnimation SHIELD = null;
    public static KeyframeAnimation SHIELD_SNEAK = null;
    public static KeyframeAnimation BOW_IDLE = null;
    public static KeyframeAnimation BOW_SNEAK = null;
    public static KeyframeAnimation BOW_SNEAK_WALKING = null;
    public static KeyframeAnimation MACE_FALL_ATTACK = null;
    public static KeyframeAnimation FIST_ATTACK = null;
    //endregion


    public static void reloadAnimationVariables() {

        BLANK_LOOP = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "blank_loop"));
        GENERIC_HANDSWING = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "generic_handswing"));

        //region MOVEMENT
        IDLE_STANDING = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "idle_standing"));
        IDLE_SNEAK = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "idle_sneak"));

        TURN_LEFT = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "turn_left"));
        TURN_RIGHT = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "turn_right"));

        IDLE_CREATIVE_FLYING = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "idle_creative_flying"));
        IDLE_CREATIVE_FLYING_ITEM = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "idle_creative_flying_item"));

        WALKING = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "walking"));
        WALKING_BACKWARDS = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "walking_backwards"));
        WALKING_SNEAK = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "walking_sneak"));
        WALKING_SNEAK_BACKWARDS = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "walking_sneak_backwards"));

        RUNNING = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "running"));


        FALLING = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "falling"));
        FALLING_MACE = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "falling_mace"));
        ELYTRA = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "elytra"));

        IDLE_IN_WATER = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "idle_in_water"));
        FORWARD_IN_WATER = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "forward_in_water"));
        BACKWARDS_IN_WATER = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "backwards_in_water"));
        UP_IN_WATER = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "up_in_water"));
        SWIMMING = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "swimming"));

        CRAWLING = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "crawling"));
        CRAWLING_BACKWARDS = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "crawling_backwards"));
        IDLE_CRAWLING = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "idle_crawling"));


        CLIMBING = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "climbing"));
        CLIMBING_SNEAK = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "climbing_sneak"));
        IDLE_CLIMBING = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "idle_climbing"));
        IDLE_CLIMBING_SNEAK = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "idle_climbing_sneak"));
        CLIMBING_BACKWARDS = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "climbing_backwards"));

        //endregion

        //region EATING

        EATING = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "eating"));
        EATING_RIGHT = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "eating_right"));
        EATING_LEFT = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "eating_left"));
        EATING_RIGHT_SNEAK = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "eating_right_sneak"));
        EATING_LEFT_SNEAK = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "eating_left_sneak"));


        //endregion

        //region SPECIAL ANIMATIONS
        TOTEM_REVIVE = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "totem_revive"));
        SLEEPING = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "sleeping"));
        PARAGLIDER = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "paraglider"));

        DROP_ITEM = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "drop_item"));
        DROP_ITEM_SNEAK = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "drop_item_sneak"));


        //endregion

        //region HOLD POSES
        LANTERN_HOLD = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "lantern_hold"));
        TORCH_HOLD = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "torch_hold"));
        //endregion

        //region VEHICLE STUFF
        HORSE_IDLE = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "horse_idle"));
        HORSE_RUNNING = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "horse_running"));
        HORSE_PICKAXE = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "horse_pickaxe"));
        MINECART_IDLE = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "minecart_idle"));
        MINECART_PICKAXE = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "minecart_pickaxe"));
        BOAT1 = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "boat1"));


        //endregions

        //region ATTACKS, BREAKS
        PICKAXE_ATTACK = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "pickaxe_attack"));
        PICKAXE_ATTACK_SNEAK = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "pickaxe_attack_sneak"));
        PICKAXE_BREAK = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "pickaxe_break"));
        PICKAXE_BREAK_SNEAK = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "pickaxe_break_sneak"));

        AXE_ATTACK = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "axe_attack"));
        AXE_ATTACK_SNEAK = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "axe_attack_sneak"));
        AXE_BREAK = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "axe_break"));
        AXE_BREAK_SNEAK = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "axe_break_sneak"));

        SHOVEL_ATTACK = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "shovel_attack"));
        SHOVEL_ATTACK_SNEAK = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "shovel_attack_sneak"));
        SHOVEL_BREAK = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "shovel_break"));
        SHOVEL_BREAK_SNEAK = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "shovel_break_sneak"));

        HOE_ATTACK = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "hoe_attack"));
        HOE_ATTACK_SNEAK = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "hoe_attack_sneak"));
        HOE_BREAK = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "hoe_break"));
        HOE_BREAK_SNEAK = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "hoe_break_sneak"));

        SWORD_ATTACK = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "sword_attack"));
        SWORD_ATTACK_2 = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "sword_attack2"));
        SWORD_ATTACK_SNEAK = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "sword_attack_sneak"));
        SWORD_ATTACK_SNEAK_2 = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "sword_attack_sneak2"));

        TRIDENT_DRAW = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "trident_draw"));
        TRIDENT_THROW = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "trident_throw"));
        SPEAR_ATTACK = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "spear_attack"));
        SPEAR_ATTACK_SNEAK = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "spear_attack_sneak"));

        SHIELD = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "shield"));
        SHIELD_SNEAK = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "shield_sneak"));

        BOW_IDLE = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "bow_idle"));
        BOW_SNEAK = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "bow_sneak"));
        BOW_SNEAK_WALKING = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "bow_sneak_walking"));

        MACE_FALL_ATTACK = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "mace_fall_attack"));

        FIST_ATTACK = (KeyframeAnimation) getAnimation(Identifier.of(MOD_ID, "fist_attack"));


        //endregion

    }
}
