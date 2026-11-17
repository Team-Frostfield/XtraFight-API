# XtraFight-API

**XtraFight-API** is an internal library and modding framework developed and maintained by the **FrostField** team to power their Minecraft projects. Currently used exclusively by FrostField for internal mod development, it centralizes shared systems, utilities, and abstractions built on top of Epic Fight so every FrostField mod can be developed cleanly and consistently.

> **Note:** This API is currently developed and maintained solely for internal use by the FrostField team.

---

## Overview

XtraFight-API eliminates repetitive boilerplate across FrostField mods by providing a unified foundation for combat mechanics, networking, rendering, and configuration handling—extending Epic Fight's combat framework without adding standalone gameplay clutter.

### Key Modules

* **Combat & Ability Systems:** Base classes and hooks for custom spellbooks, status effects, player resource management, and Epic Fight combat state extensions.
* **Networking & Syncing:** Streamlined client-server packet abstractions and entity pairing utilities for reliable multiplayer state synchronization.
* **Client Rendering & UI:** Reusable entity render layers, dynamic texture loaders, custom screen widgets, and particle helpers.
* **Core Utilities:** Shared math helpers, game rules, datapack synchronization, and modular file/config persistence.

---

## Requirements & Dependencies

* **Required Dependencies:**
  * **Epic Fight**
* **Usage:** Required as a library mod alongside **Epic Fight** when running FrostField mods that depend on XtraFight-API.

---

## License & Usage

Developed and maintained by the **FrostField** team. Intended for internal FrostField projects unless otherwise specified.
