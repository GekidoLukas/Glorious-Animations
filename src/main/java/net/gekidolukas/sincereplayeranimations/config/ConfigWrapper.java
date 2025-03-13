package net.gekidolukas.sincereplayeranimations.config;

import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;

import static net.gekidolukas.sincereplayeranimations.SincerePlayerAnimations.MOD_ID;

@Config(name = MOD_ID)
public class ConfigWrapper extends PartitioningSerializer.GlobalData {
    @ConfigEntry.Category("client")
    @ConfigEntry.Gui.TransitiveObject
    public ClientConfig client = new ClientConfig();
}