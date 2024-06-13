package com.github.theredbrain.healthregenerationoverhaul.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(
        name = "client"
)
public class ClientConfig implements ConfigData {
    @Comment("""
            Replaces the vanilla health bar with a simpler and more scalable bar.
            All other options in this config only affect the new health bar
            """)
    public boolean enable_alternative_health_bar = false;
    public boolean show_full_health_bar = true;
    public int health_bar_additional_length = 172;
    public int health_bar_x_offset = -91;
    public int health_bar_y_offset = -39;
    public boolean show_health_bar_number = true;
    public int health_bar_number_color = -6250336;
    public int health_bar_number_x_offset = 0;
    public int health_bar_number_y_offset = -40;
    public ClientConfig() {
    }
}
