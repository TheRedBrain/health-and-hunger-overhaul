# 2.4.0

HUD rendering overhaul
The initial idea of splitting a bar into three textures per "layer" to allow for easy change of the bar length, came with the cost of massive FPS drops.
With this rewrite the bar size is no longer changeable with a simple config option. Each 'layer' consists of only 1 texture (which can have configurable dimensions).
In addition, the texture can be dynamically replaced by another texture (with configurable dimensions) depending on the max value.
- 'fill_direction' no longer chooses between a horizontal and a vertical 'texture set', it only determines the direction from which the bar is filled. This means that changing between horizontal and vertical resource bars also requires a texture change.
- added the option to display an icon (with configurable texture id and dimensions). This can be toggled independently of the bar and the number.
- added "generic.reserved_health" entity attribute
- "generic.health_regeneration" entity attribute can now be negative
- changed vanillas "natural regeneration" gamerule to simply add 1 health regeneration.
- server config can now be edited in game (Thanks to Fzzy Config)
- generally improved config layout
- removed dependency on Cloth Config
- added dependency on Fzzy Config
- added dependency on Resource Bar API

# 2.3.0

- further improvements to health bar customization

# 2.2.0

- update to 1.21.1
- improved health bar customization
- fixed an issue where entities would rarely be immortal

# 2.1.0

- added optional smooth health bar animations
- added a health regeneration delay after health is reduced, controlled by an entity attribute

# 2.0.0

Update to 1.21

# 1.1.0

- added server config option to re-enable the vanilla food system
- alternative health bar is now disabled by default
- fixed compatibility issue with mods that interact with the vanilla food system

# 1.0.0

First release!

#