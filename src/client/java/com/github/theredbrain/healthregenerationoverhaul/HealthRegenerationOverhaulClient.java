package com.github.theredbrain.healthregenerationoverhaul;

import com.github.theredbrain.healthregenerationoverhaul.config.ClientConfig;
import com.github.theredbrain.healthregenerationoverhaul.config.ClientConfigWrapper;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class HealthRegenerationOverhaulClient implements ClientModInitializer {
	public static ClientConfig clientConfig;
	@Override
	public void onInitializeClient() {
		// Config
		AutoConfig.register(ClientConfigWrapper.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
		clientConfig = ((ClientConfigWrapper)AutoConfig.getConfigHolder(ClientConfigWrapper.class).getConfig()).client;

		// Packets
		ClientPlayNetworking.registerGlobalReceiver(HealthRegenerationOverhaul.ServerConfigSyncPacket.PACKET_ID, (payload, context) -> {
			HealthRegenerationOverhaul.serverConfig = payload.serverConfig();
		});
	}
}