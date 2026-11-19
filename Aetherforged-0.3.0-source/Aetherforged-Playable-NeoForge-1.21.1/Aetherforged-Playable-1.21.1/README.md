# Aetherforged 0.2.0 — NeoForge 1.21.1

A playable-source foundation for a void-themed modular weapon mod.

## Requirements
- Minecraft 1.21.1
- NeoForge 21.1.x (project targets 21.1.172)
- Java 21

## Build
Open this project in IntelliJ IDEA with the Gradle plugin, or run `gradle build` using a compatible Gradle installation. This environment did not run Gradle, so the source has not been compile-tested here.

## Current gameplay
- **Void Aetherblade:** registered sword with diamond-tier combat stats and a void-themed item model/texture.
- **Infusion Core:** interact while holding an Aetherblade to open its infusion screen.
- **Infusion GUI:** select one of the built-in programs and apply it to the held Aetherblade. The menu action is server-authoritative.
- **Stack-aware data:** programs are stored in the item's `CUSTOM_DATA`; same configurations can stack, different configurations naturally split.
- **Combat actions:** loaded programs trigger on hit and execute supported built-in actions: bonus damage, ignite, heal wielder, knockback, lightning, and particle burst.
- **External action packs:** JSON definitions in `config/aetherforged/actions/`; reload with `/aetherforged reload`. The loader is data-only; it never runs scripts or loads classes from disk.

## External action JSON
Create a `.json` file in `config/aetherforged/actions/`:
```json
{
  "id": "my_pack:ember_burst",
  "name": "Ember Burst",
  "trigger": "on_hit",
  "cooldownTicks": 40,
  "actions": [
    { "type": "builtin:ignite", "amount": 4.0 },
    { "type": "builtin:particle_burst", "particle": "minecraft:flame", "count": 24 }
  ]
}
```
Supported action types:
- `builtin:bonus_damage` (`amount`)
- `builtin:ignite` (`amount`, seconds)
- `builtin:heal_wielder` (`amount`)
- `builtin:knockback` (`amount`)
- `builtin:lightning`
- `builtin:particle_burst` (`particle`, `count`)

Triggers currently supported: `on_hit`. Cooldowns are per-player/per-program. JSON is validated and unsupported actions are skipped.

## Notes / limitations
The infusion screen is intentionally a compact action-program selector rather than a node-graph editor. It is a working base for the advanced modular editor: programs are data-driven and can contain ordered action steps. Custom particle identifiers are validated at execution time by Minecraft's particle registry. A future pass can add drag-and-drop nodes, conditional branches, custom catalyst recipes, sound design, and a custom 3D sword model.
