package com.github.theredbrain.healthregenerationoverhaul.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(
        name = "server"
)
public class ServerConfig implements ConfigData {
    @Comment("""
            When set to false, re-enables the vanilla food system.
            
            Note that a sufficiently full hunger bar and your health regeneration attribute will both regenerate your health.
            """)
    public boolean disable_vanilla_food_system = true;
    public ServerConfig() {

    }
}
