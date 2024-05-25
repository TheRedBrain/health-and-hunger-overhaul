package com.github.theredbrain.healthandhungeroverhaul.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(
        name = "server"
)
public class ServerConfig implements ConfigData {
    public boolean jumping_requires_stamina = true;
    public boolean sprinting_requires_stamina = true;
    @Comment("""
            The following values are subtracted from the players stamina each tick (20 times per second)
            """)
    public float stamina_cost_sprinting = 0.05F;
    public float stamina_cost_sneaking = 0.05F;
    public float stamina_cost_walking = 0.0F;
    public float stamina_cost_swimming = 0.0F;
    public float stamina_cost_walking_underwater = 0.0F;
    public float stamina_cost_walking_in_water = 0.05F;
    public float stamina_cost_climbing = 0.05F;
    @Comment("""
            The following values are subtracted from the players stamina each time the action is performed
            """)
    public float stamina_cost_sprint_jumping = 1.0F;
    public float stamina_cost_jumping = 1.0F;
    public ServerConfig() {

    }
}
