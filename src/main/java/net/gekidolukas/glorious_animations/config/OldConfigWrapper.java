package net.gekidolukas.glorious_animations.config;

import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;

import static net.gekidolukas.glorious_animations.GloriousAnimations.MOD_ID;

@Config(name = MOD_ID)
public class OldConfigWrapper extends PartitioningSerializer.GlobalData {
    @ConfigEntry.Category("client")
    @ConfigEntry.Gui.TransitiveObject
    public OldClientConfig client = new OldClientConfig();
}