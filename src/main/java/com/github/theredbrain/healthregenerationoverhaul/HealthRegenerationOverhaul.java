package com.github.theredbrain.healthregenerationoverhaul;

import com.github.theredbrain.healthregenerationoverhaul.config.ServerConfig;
import com.github.theredbrain.healthregenerationoverhaul.config.ServerConfigWrapper;
import com.google.gson.Gson;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HealthRegenerationOverhaul implements ModInitializer {
	public static final String MOD_ID = "healthregenerationoverhaul";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ServerConfig serverConfig;

	public static RegistryEntry<EntityAttribute> HEALTH_REGENERATION;
	public static RegistryEntry<EntityAttribute> HEALTH_TICK_THRESHOLD;
	public static RegistryEntry<EntityAttribute> HEALTH_REGENERATION_DELAY_THRESHOLD;
  
	@Override
	public void onInitialize() {
		LOGGER.info("Regenerating health was overhauled!");

		// Config
		AutoConfig.register(ServerConfigWrapper.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
		serverConfig = ((ServerConfigWrapper)AutoConfig.getConfigHolder(ServerConfigWrapper.class).getConfig()).server;

		PayloadTypeRegistry.playS2C().register(ServerConfigSyncPacket.PACKET_ID, ServerConfigSyncPacket.PACKET_CODEC);
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayNetworking.send(handler.player, new ServerConfigSyncPacket(serverConfig));
		});

	}

	public record ServerConfigSyncPacket(ServerConfig serverConfig) implements CustomPayload {
		public static final CustomPayload.Id<ServerConfigSyncPacket> PACKET_ID = new CustomPayload.Id<>(identifier("server_config_sync"));
		public static final PacketCodec<RegistryByteBuf, ServerConfigSyncPacket> PACKET_CODEC = PacketCodec.of(ServerConfigSyncPacket::write, ServerConfigSyncPacket::new);

		public ServerConfigSyncPacket(RegistryByteBuf registryByteBuf) {
			this(new Gson().fromJson(registryByteBuf.readString(), ServerConfig.class));
		}

		private void write(RegistryByteBuf registryByteBuf) {
			registryByteBuf.writeString(new Gson().toJson(serverConfig));
		}

		@Override
		public CustomPayload.Id<? extends CustomPayload> getId() {
			return PACKET_ID;
		}
	}

	public static Identifier identifier(String path) {
		return Identifier.of(MOD_ID, path);
	}
}