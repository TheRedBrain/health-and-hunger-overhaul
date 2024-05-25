package com.github.theredbrain.healthregenerationoverhaul;

import net.fabricmc.api.ModInitializer;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HealthRegenerationOverhaul implements ModInitializer {
	public static final String MOD_ID = "healthregenerationoverhaul";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static EntityAttribute HEALTH_REGENERATION;
	public static EntityAttribute HEALTH_TICK_THRESHOLD;
	@Override
	public void onInitialize() {
		LOGGER.info("Hunger no more!");

	}

	public static Identifier identifier(String path) {
		return new Identifier(MOD_ID, path);
	}
}