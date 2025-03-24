package net.gekidolukas.glorious_animations.config;

import com.google.common.collect.Lists;
import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.List;

public class GloriousAnimConfig extends MidnightConfig {
    private static final String ITEM_LISTS = "item_lists";

    @Comment(category = ITEM_LISTS, centered = true) private static Comment item_list_desc;

    @Entry(category = ITEM_LISTS) private static final List<Identifier> sword_attack_items = Lists.newArrayList(
            Identifier.ofVanilla("wooden_sword"),
            Identifier.ofVanilla("stone_sword"),
            Identifier.ofVanilla("gold_sword"),
            Identifier.ofVanilla("iron_sword"),
            Identifier.ofVanilla("diamond_sword"),
            Identifier.ofVanilla("netherite_sword")
    );
    public static boolean hasSwordAttackAnimation(Item item) {
        return sword_attack_items.contains(Registries.ITEM.getId(item));
    }


    @Entry(category = ITEM_LISTS) private static final List<Identifier> axe_attack_items = Lists.newArrayList(
            Identifier.ofVanilla("wooden_axe"),
            Identifier.ofVanilla("stone_axe"),
            Identifier.ofVanilla("gold_axe"),
            Identifier.ofVanilla("iron_axe"),
            Identifier.ofVanilla("diamond_axe"),
            Identifier.ofVanilla("netherite_axe")
    );
    public static boolean hasAxeAttackAnimation(Item item) {
        return axe_attack_items.contains(Registries.ITEM.getId(item));
    }

    @Entry(category = ITEM_LISTS) private static final List<Identifier> pickaxe_attack_items = Lists.newArrayList(
            Identifier.ofVanilla("wooden_pickaxe"),
            Identifier.ofVanilla("stone_pickaxe"),
            Identifier.ofVanilla("gold_pickaxe"),
            Identifier.ofVanilla("iron_pickaxe"),
            Identifier.ofVanilla("diamond_pickaxe"),
            Identifier.ofVanilla("netherite_pickaxe")
    );
    public static boolean hasPickaxeAttackAnimation(Item item) {
        return pickaxe_attack_items.contains(Registries.ITEM.getId(item));
    }

    @Entry(category = ITEM_LISTS) private static final List<Identifier> shovel_attack_items = Lists.newArrayList(
            Identifier.ofVanilla("wooden_shovel"),
            Identifier.ofVanilla("stone_shovel"),
            Identifier.ofVanilla("gold_shovel"),
            Identifier.ofVanilla("iron_shovel"),
            Identifier.ofVanilla("diamond_shovel"),
            Identifier.ofVanilla("netherite_shovel")
    );
    public static boolean hasShovelAttackAnimation(Item item) {
        return shovel_attack_items.contains(Registries.ITEM.getId(item));
    }

    @Entry(category = ITEM_LISTS) private static final List<Identifier> hoe_attack_items = Lists.newArrayList(
            Identifier.ofVanilla("wooden_hoe"),
            Identifier.ofVanilla("stone_hoe"),
            Identifier.ofVanilla("gold_hoe"),
            Identifier.ofVanilla("iron_hoe"),
            Identifier.ofVanilla("diamond_hoe"),
            Identifier.ofVanilla("netherite_hoe")
    );
    public static boolean hasHoeAttackAnimation(Item item) {
        return hoe_attack_items.contains(Registries.ITEM.getId(item));
    }

    @Entry(category = ITEM_LISTS) private static final List<Identifier> mace_attack_items = Lists.newArrayList(
            Identifier.ofVanilla("mace")
    );
    public static boolean hasMaceAttackAnimation(Item item) {
        return mace_attack_items.contains(Registries.ITEM.getId(item));
    }

    @Entry(category = ITEM_LISTS) private static final List<Identifier> punch_attack_items = Lists.newArrayList(
            Identifier.ofVanilla("air")
    );
    public static boolean hasPunchAttackAnimation(Item item) {
        return punch_attack_items.contains(Registries.ITEM.getId(item));
    }


    @Entry(category = ITEM_LISTS) private static final List<Identifier> sword_break_items = Lists.newArrayList(
            Identifier.ofVanilla("wooden_sword"),
            Identifier.ofVanilla("stone_sword"),
            Identifier.ofVanilla("gold_sword"),
            Identifier.ofVanilla("iron_sword"),
            Identifier.ofVanilla("diamond_sword"),
            Identifier.ofVanilla("netherite_sword")
    );
    public static boolean hasSwordBreakAnimation(Item item) {
        return sword_break_items.contains(Registries.ITEM.getId(item));
    }


    @Entry(category = ITEM_LISTS) private static final List<Identifier> axe_break_items = Lists.newArrayList(
            Identifier.ofVanilla("wooden_axe"),
            Identifier.ofVanilla("stone_axe"),
            Identifier.ofVanilla("gold_axe"),
            Identifier.ofVanilla("iron_axe"),
            Identifier.ofVanilla("diamond_axe"),
            Identifier.ofVanilla("netherite_axe")
    );
    public static boolean hasAxeBreakAnimation(Item item) {
        return axe_break_items.contains(Registries.ITEM.getId(item));
    }

    @Entry(category = ITEM_LISTS) private static final List<Identifier> pickaxe_break_items = Lists.newArrayList(
            Identifier.ofVanilla("wooden_pickaxe"),
            Identifier.ofVanilla("stone_pickaxe"),
            Identifier.ofVanilla("gold_pickaxe"),
            Identifier.ofVanilla("iron_pickaxe"),
            Identifier.ofVanilla("diamond_pickaxe"),
            Identifier.ofVanilla("netherite_pickaxe")
    );
    public static boolean hasPickaxeBreakAnimation(Item item) {
        return pickaxe_break_items.contains(Registries.ITEM.getId(item));
    }

    @Entry(category = ITEM_LISTS) private static final List<Identifier> shovel_break_items = Lists.newArrayList(
            Identifier.ofVanilla("wooden_shovel"),
            Identifier.ofVanilla("stone_shovel"),
            Identifier.ofVanilla("gold_shovel"),
            Identifier.ofVanilla("iron_shovel"),
            Identifier.ofVanilla("diamond_shovel"),
            Identifier.ofVanilla("netherite_shovel")
    );
    public static boolean hasShovelBreakAnimation(Item item) {
        return shovel_break_items.contains(Registries.ITEM.getId(item));
    }
    @Entry(category = ITEM_LISTS) private static final List<Identifier> hoe_break_items = Lists.newArrayList(
            Identifier.ofVanilla("wooden_hoe"),
            Identifier.ofVanilla("stone_hoe"),
            Identifier.ofVanilla("gold_hoe"),
            Identifier.ofVanilla("iron_hoe"),
            Identifier.ofVanilla("diamond_hoe"),
            Identifier.ofVanilla("netherite_hoe")
    );
    public static boolean hasHoeBreakAnimation(Item item) {
        return hoe_break_items.contains(Registries.ITEM.getId(item));
    }


    @Entry(category = ITEM_LISTS) private static final List<Identifier> lantern_items = Lists.newArrayList(
            Identifier.ofVanilla("soul_lantern"),
            Identifier.ofVanilla("lantern")
    );
    public static boolean isLanternItem(Item item) {
        return lantern_items.contains(Registries.ITEM.getId(item));
    }
}
