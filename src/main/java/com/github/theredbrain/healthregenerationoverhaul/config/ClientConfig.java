package com.github.theredbrain.healthregenerationoverhaul.config;

import com.github.theredbrain.healthregenerationoverhaul.HealthRegenerationOverhaul;
import com.github.theredbrain.resourcebarapi.ResourceBarAPI;
import me.fzzyhmstrs.fzzy_config.annotations.ConvertFrom;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedMap;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedIdentifier;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedColor;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import net.minecraft.util.Identifier;

import java.util.HashMap;

@ConvertFrom(fileName = "client.json5", folder = "healthregenerationoverhaul")
public class ClientConfig extends Config {

	public ClientConfig() {
		super(HealthRegenerationOverhaul.identifier("client"));
	}

	public boolean enable_alternative_health_bar = true;
	public boolean show_full_health_bar = true;

	public PositionSettings positionSettings = new PositionSettings();

	public static class PositionSettings extends ConfigSection {
		public ResourceBarAPI.ResourceBarOrigin origin = ResourceBarAPI.ResourceBarOrigin.BOTTOM_MIDDLE;
		public boolean is_centered = false;
		public ValidatedMap<Integer, Integer> offsets_x = new ValidatedMap<>(new HashMap<>() {{
			put(0, -91);
		}}, new ValidatedInt(), new ValidatedInt());
		public ValidatedMap<Integer, Integer> offsets_y = new ValidatedMap<>(new HashMap<>() {{
			put(0, -39);
		}}, new ValidatedInt(), new ValidatedInt());
	}

	public ResourceBarAPI.ResourceBarFillDirection fill_direction = ResourceBarAPI.ResourceBarFillDirection.LEFT_TO_RIGHT;

	public boolean show_current_value_overlay = false;

	public TextureSettings textureSettings = new TextureSettings();

	public static class TextureSettings extends ConfigSection {
		public BackgroundTextureSettings backgroundTextureSettings = new BackgroundTextureSettings();

		@Translation(prefix = "healthregenerationoverhaul.client.texture_layer")
		public static class BackgroundTextureSettings extends ConfigSection {

			public ValidatedMap<Integer, Integer> texture_heights = new ValidatedMap<>(new HashMap<>() {{
				put(0, 5);
			}}, new ValidatedInt(), new ValidatedInt());
			public ValidatedMap<Integer, Integer> texture_widths = new ValidatedMap<>(new HashMap<>() {{
				put(0, 182);
			}}, new ValidatedInt(), new ValidatedInt());

			public ValidatedMap<Integer, Identifier> texture_ids = new ValidatedMap<>(new HashMap<>() {{
				put(0, Identifier.of("healthregenerationoverhaul", "textures/gui/sprites/hud/horizontal_health_background.png"));
			}}, new ValidatedInt(), new ValidatedIdentifier());

		}

		public ProgressTextureSettings progressTextureSettings = new ProgressTextureSettings();

		@Translation(prefix = "healthregenerationoverhaul.client.texture_layer")
		public static class ProgressTextureSettings extends ConfigSection {
			public int offset_x = 0;
			public int offset_y = 0;

			public ValidatedMap<Integer, Integer> texture_heights = new ValidatedMap<>(new HashMap<>() {{
				put(0, 5);
			}}, new ValidatedInt(), new ValidatedInt());
			public ValidatedMap<Integer, Integer> texture_widths = new ValidatedMap<>(new HashMap<>() {{
				put(0, 182);
			}}, new ValidatedInt(), new ValidatedInt());

			@Translation(prefix = "healthregenerationoverhaul.client.texture_layer", negate = true)
			public ValidatedMap<Integer, Identifier> progress_decrease_animation_texture_ids = new ValidatedMap<>(new HashMap<>() {{
				put(0, Identifier.of("healthregenerationoverhaul", "textures/gui/sprites/hud/horizontal_health_progress_decrease_animation.png"));
			}}, new ValidatedInt(), new ValidatedIdentifier());

			@Translation(prefix = "healthregenerationoverhaul.client.texture_layer", negate = true)
			public ValidatedMap<Integer, Identifier> progress_increase_animation_texture_ids = new ValidatedMap<>(new HashMap<>() {{
				put(0, Identifier.of("healthregenerationoverhaul", "textures/gui/sprites/hud/horizontal_health_progress_increase_animation.png"));
			}}, new ValidatedInt(), new ValidatedIdentifier());

			@Translation(prefix = "healthregenerationoverhaul.client.texture_layer", negate = true)
			public ValidatedMap<Integer, Identifier> progress_increase_value_texture_ids = new ValidatedMap<>(new HashMap<>() {{
				put(0, Identifier.of("healthregenerationoverhaul", "textures/gui/sprites/hud/horizontal_health_progress_increase_value.png"));
			}}, new ValidatedInt(), new ValidatedIdentifier());

			@Translation(prefix = "healthregenerationoverhaul.client.texture_layer", negate = true)
			public ValidatedMap<Integer, Identifier> progress_texture_ids = new ValidatedMap<>(new HashMap<>() {{
				put(0, Identifier.of("healthregenerationoverhaul", "textures/gui/sprites/hud/horizontal_health_progress.png"));
			}}, new ValidatedInt(), new ValidatedIdentifier());

		}

		public ReservedTextureSettings reservedTextureSettings = new ReservedTextureSettings();

		@Translation(prefix = "healthregenerationoverhaul.client.texture_layer")
		public static class ReservedTextureSettings extends ConfigSection {
			public int offset_x = 0;
			public int offset_y = 0;

			public ValidatedMap<Integer, Integer> texture_heights = new ValidatedMap<>(new HashMap<>() {{
				put(0, 5);
			}}, new ValidatedInt(), new ValidatedInt());
			public ValidatedMap<Integer, Integer> texture_widths = new ValidatedMap<>(new HashMap<>() {{
				put(0, 182);
			}}, new ValidatedInt(), new ValidatedInt());

			public ValidatedMap<Integer, Identifier> texture_ids = new ValidatedMap<>(new HashMap<>() {{
				put(0, Identifier.of("healthregenerationoverhaul", "textures/gui/sprites/hud/horizontal_health_reserved.png"));
			}}, new ValidatedInt(), new ValidatedIdentifier());

		}

		public OverlayTextureSettings overlayTextureSettings = new OverlayTextureSettings();

		@Translation(prefix = "healthregenerationoverhaul.client.texture_layer")
		public static class OverlayTextureSettings extends ConfigSection {
			@Translation(prefix = "healthregenerationoverhaul.client.texture_layer", negate = true)
			public int offset_x = -2;
			@Translation(prefix = "healthregenerationoverhaul.client.texture_layer", negate = true)
			public int offset_y = 0;

			public ValidatedMap<Integer, Integer> texture_heights = new ValidatedMap<>(new HashMap<>() {{
				put(0, 5);
			}}, new ValidatedInt(), new ValidatedInt());
			public ValidatedMap<Integer, Integer> texture_widths = new ValidatedMap<>(new HashMap<>() {{
				put(0, 5);
			}}, new ValidatedInt(), new ValidatedInt());

			public ValidatedMap<Integer, Identifier> texture_ids = new ValidatedMap<>(new HashMap<>() {{
				put(0, Identifier.of("healthregenerationoverhaul", "textures/gui/sprites/hud/horizontal_health_overlay.png"));
			}}, new ValidatedInt(), new ValidatedIdentifier());

		}
	}

	public boolean show_icon = false;

	public IconTextureSettings iconTextureSettings = new IconTextureSettings();

	@Translation(prefix = "healthregenerationoverhaul.client.texture_layer")
	public static class IconTextureSettings extends ConfigSection {
		@Translation(prefix = "healthregenerationoverhaul.client.texture_layer", negate = true)
		public int offset_x = 0;
		@Translation(prefix = "healthregenerationoverhaul.client.texture_layer", negate = true)
		public int offset_y = 0;

		public ValidatedMap<Integer, Integer> texture_heights = new ValidatedMap<>(new HashMap<>() {{
			put(0, 0);
		}}, new ValidatedInt(), new ValidatedInt());
		public ValidatedMap<Integer, Integer> texture_widths = new ValidatedMap<>(new HashMap<>() {{
			put(0, 0);
		}}, new ValidatedInt(), new ValidatedInt());

		public ValidatedMap<Integer, Identifier> texture_ids = new ValidatedMap<>(new HashMap<>() {
		}, new ValidatedInt(), new ValidatedIdentifier());

	}

	public boolean enable_smooth_animation = true;

	public AnimationsSettings animationSettings = new AnimationsSettings();

	public static class AnimationsSettings extends ConfigSection {
		public int animation_interval = 1;
		public boolean max_value_change_is_animated = false;
	}

	public boolean show_number = false;

	public NumberSettings numberSettings = new NumberSettings();

	public static class NumberSettings extends ConfigSection {
		public boolean show_max_value = false;
		public boolean show_when_health_full = true;
		public int offset_x = 0;
		public int offset_y = -40;
		public ValidatedColor color = new ValidatedColor(150, 150, 150);
	}
}