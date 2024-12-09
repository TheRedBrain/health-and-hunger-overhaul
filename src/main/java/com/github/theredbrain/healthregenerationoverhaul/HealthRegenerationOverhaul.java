package com.github.theredbrain.healthregenerationoverhaul;

import com.github.theredbrain.healthregenerationoverhaul.config.ServerConfig;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HealthRegenerationOverhaul implements ModInitializer {
	public static final String MOD_ID = "healthregenerationoverhaul";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ServerConfig SERVER_CONFIG;

	public static EntityAttribute HEALTH_REGENERATION;
	public static EntityAttribute HEALTH_TICK_THRESHOLD;
	public static EntityAttribute HEALTH_REGENERATION_DELAY_THRESHOLD;
	public static EntityAttribute RESERVED_HEALTH;

	@Override
	public void onInitialize() {
		LOGGER.info("Regenerating health was overhauled!");
		SERVER_CONFIG = ConfigApiJava.registerAndLoadConfig(ServerConfig::new);

	}

	public static Identifier identifier(String path) {
		return Identifier.of(MOD_ID, path);
	}

}