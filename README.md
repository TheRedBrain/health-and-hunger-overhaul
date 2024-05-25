# Health Regeneration Overhaul
This API changes the way health is regenerated. Instead of depending on the hunger system, two new entity attributes are used. The hunger system including exhaustion and saturation is disabled.

## Default implementation
LivingEntities regenerate **_generic.health_regeneration_** every **_generic.health_tick_threshold_** ticks.

## Customization
When the vanilla gamerule "naturalRegeneration" is true, players have a health regeneration of at least 1.

In the client config an alternative health bar can be enabled and customized.

## API
Casting a "LivingEntity" to the "HealthUsingEntity" interface gives access to all relevant methods.