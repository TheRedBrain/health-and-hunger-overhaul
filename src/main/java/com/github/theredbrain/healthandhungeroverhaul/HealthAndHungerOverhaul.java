package com.github.theredbrain.healthandhungeroverhaul;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HealthAndHungerOverhaul implements ModInitializer {
	public static final String MOD_ID = "healthandhungeroverhaul";
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