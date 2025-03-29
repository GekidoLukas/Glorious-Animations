package net.gekidolukas.glorious_animations;

import eu.midnightdust.lib.config.MidnightConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.gekidolukas.glorious_animations.config.GloriousAnimConfig;
import net.gekidolukas.glorious_animations.config.OldConfigWrapper;
import net.gekidolukas.glorious_animations.interfaces.SwingTypeGetter;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GloriousAnimations implements ModInitializer, ClientModInitializer {
	public static final String MOD_ID = "glorious_animations";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
//	public static OldClientConfig config;
	public static boolean CARRYON_COMPAT;
	public static boolean SUPPLEMENTARIES_COMPAT;
	public static boolean IMMERSIVE_MELODIES_COMPAT;
	public static boolean ULTRACRAFT_COMPAT;
	public static boolean SWORDBLOCKING_COMPAT;
	public static boolean OLDCOMBATMOD_COMPAT;
	public static boolean TRIGGERHAPPY_COMPAT;

	@Override
	public void onInitialize() {
		LOGGER.info("Loading up some Serious Animations.");

		MidnightConfig.init(MOD_ID, GloriousAnimConfig.class);

//		AutoConfig.register(OldConfigWrapper.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
//		config = AutoConfig.getConfigHolder(OldConfigWrapper.class).getConfig().client;

        CARRYON_COMPAT = FabricLoader.getInstance().isModLoaded("carryon");
        SUPPLEMENTARIES_COMPAT = FabricLoader.getInstance().isModLoaded("supplementaries");
		IMMERSIVE_MELODIES_COMPAT = FabricLoader.getInstance().isModLoaded("immersive_melodies");
		ULTRACRAFT_COMPAT = FabricLoader.getInstance().isModLoaded("ultracraft");
		SWORDBLOCKING_COMPAT = FabricLoader.getInstance().isModLoaded("swordblocking");
		OLDCOMBATMOD_COMPAT = FabricLoader.getInstance().isModLoaded("oldcombatmod");
		TRIGGERHAPPY_COMPAT = FabricLoader.getInstance().isModLoaded("triggerhappy");

	}




	@Override
	public void onInitializeClient()  {
		LOGGER.info("Client Init");

		ClientTickEvents.END_CLIENT_TICK.register((minecraftClient -> {
			LOGGER.info("WHAT");
//			if (minecraftClient.world != null) {
//				List<AbstractClientPlayerEntity> players = minecraftClient.world.getPlayers();
//				for (PlayerEntity player : players) {
//					if(((SwingTypeGetter)player).getAttackTicks() > 0) {
//						((SwingTypeGetter)player).setAttackTicks(((SwingTypeGetter)player).getAttackTicks() - 1);
//					}
//					if(((SwingTypeGetter)player).getBlockBreakingTicks() > 0) {
//						((SwingTypeGetter)player).setBlockBreakingTicks(((SwingTypeGetter)player).getBlockBreakingTicks() - 1);
//					}
//				}
//			}
		}));



	}
}