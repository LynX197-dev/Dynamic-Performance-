# Dynamic PERFORMANCE+

## Overview

Dynamic PERFORMANCE+ is a high-performance Minecraft server optimization plugin for Paper-based servers 1.21.x. It is engineered to minimize tick lag, enhance server responsiveness, and maintain consistent TPS by intelligently managing server systems without altering core gameplay mechanics such as physics or entity behavior.

## Core Optimizations

- **Asynchronous Task Scheduling:** Offloads intensive operations, including chunk management and cleanup routines, to asynchronous threads, reducing main-thread load and improving server responsiveness.

- **Chunk Management:** Dynamically loads and unloads chunks based on player proximity and activity, utilizing an optimized queue system to balance performance and gameplay experience.

- **Entity & Mob Processing:** Employs distance-based culling and conditional AI throttling to limit unnecessary entity updates, ensuring smoother performance even in entity-heavy environments.

- **Falling Block Optimization:** Maintains full functionality of falling block entities while streamlining physics calculations for enhanced server efficiency.

- **Explosion Optimization:** Regulates simultaneous explosion calculations and manages block and particle effects, significantly lowering CPU usage during high-load scenarios.

- **Hopper Transfer Optimization:** Adjusts hopper transfer rates and processing intervals to prevent tick slowdowns in large-scale item transportation systems.

- **Automated Cleanup:** Periodically removes untracked entities, dropped items, and expired projectiles to reclaim server resources and maintain optimal performance.

- **Performance Monitoring:** Continuously tracks MSPT, TPS, and entity counts with configurable thresholds, automatically triggering optimizations when performance metrics fall below defined standards.

## Admin Commands

All `/dp` commands require the `dynamicperformance.admin` permission.

- **`/dp optimize`** – Applies one-click optimizations, including clearing lag, unloading chunks, reducing ticking loads, minimizing redstone activity, and throttling mob AI.

- **`/dp entities`** – Cleans up entities to free server resources and improve performance.

- **`/dp chunks`** – Manages chunk loading and unloading for optimal server performance.

- **`/dp stats`** – Displays real-time performance statistics such as TPS, memory usage, and entity counts.

- **`/dp reload`** – Reloads the plugin configuration without restarting the server.

- **`/dp boost`** – Activates boost mode for a configurable duration, temporarily enhancing server performance by reducing AI, redstone, and entity load.

## Compatibility

- Fully compatible with Paper, Purpur, and Spigot servers (Minecraft 1.21.x).  
- Requires no client-side modifications.

## Key Features

- Intelligent load balancing to prevent server lag.  
- Adaptive AI throttling for high-density mob areas.  
- Configurable thresholds for automated optimizations.  
- Supports large-scale farms, redstone contraptions, and complex builds.  

## Recommended Ecosystem

For enhanced server management, Dynamic PERFORMANCE+ integrates seamlessly with:

- [Dynamic CORE+](https://modrinth.com/plugin/dynamic-core+) — Comprehensive Essentials replacement.  
- [Dynamic AC+](https://modrinth.com/plugin/dynamic-ac+) — Advanced anti-cheat and movement regulation.  
- [Dynamic LIMITER+](https://modrinth.com/plugin/dynamic-limiter+) — Entity, redstone, and system limiter.
- [Dynamic BACKUP+](https://modrinth.com/plugin/dynamic-backup+) — Scheduled backup system
