package net.gekidolukas.sincereplayeranimations.compat;

import immersive_melodies.item.InstrumentItem;
import net.minecraft.item.Item;

public class ImmersiveMelodiesItemCheck {

    public static boolean check(Item item) {
        return item instanceof InstrumentItem;

    }

}
