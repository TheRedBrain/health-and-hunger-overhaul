package com.github.theredbrain.healthregenerationoverhaul;

import com.github.theredbrain.healthregenerationoverhaul.config.ServerConfig;
import com.github.theredbrain.healthregenerationoverhaul.config.ServerConfigWrapper;
import com.google.gson.Gson;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HealthRegenerationOverhaul implements ModInitializer {
	public static final String MOD_ID = "healthregenerationoverhaul";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ServerConfig serverConfig;
	private static PacketByteBuf serverConfigSerialized = PacketByteBufs.create();

	public static EntityAttribute HEALTH_REGENERATION;
	public static EntityAttribute HEALTH_TICK_THRESHOLD;
	@Override
	public void onInitialize() {
		LOGGER.info("Regenerating health was overhauled!");

		// Config
		AutoConfig.register(ServerConfigWrapper.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
		serverConfig = ((ServerConfigWrapper)AutoConfig.getConfigHolder(ServerConfigWrapper.class).getConfig()).server;

		serverConfigSerialized = ServerConfigSync.write(serverConfig);

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			sender.sendPacket(ServerConfigSync.ID, serverConfigSerialized); // TODO convert to packet
		});
	}

	public static Identifier identifier(String path) {
		return Identifier.of(MOD_ID, path);
	}

	public static class ServerConfigSync {
		public static Identifier ID = identifier("server_config_sync");

		public static PacketByteBuf write(ServerConfig serverConfig) {
			var gson = new Gson();
			var json = gson.toJson(serverConfig);
			var buffer = PacketByteBufs.create();
			buffer.writeString(json);
			return buffer;
		}

		public static ServerConfig read(PacketByteBuf buffer) {
			var gson = new Gson();
			var json = buffer.readString();
			return gson.fromJson(json, ServerConfig.class);
		}
	}
}