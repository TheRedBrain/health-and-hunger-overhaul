package com.github.theredbrain.healthregenerationoverhaul.config;

import com.github.theredbrain.healthregenerationoverhaul.HealthRegenerationOverhaul;
import me.fzzyhmstrs.fzzy_config.annotations.ConvertFrom;
import me.fzzyhmstrs.fzzy_config.config.Config;

@ConvertFrom(fileName = "server.json5", folder = "healthregenerationoverhaul")
public class ServerConfig extends Config {

	public ServerConfig() {
		super(HealthRegenerationOverhaul.identifier("server"));
	}

	public boolean disable_vanilla_food_system = true;
}