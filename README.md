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

---

<details>
<summary>Performance Bechmarks</summary>

| Metric              | Without Plugin | With Dynamic PERFORMANCE+ | Change      |
| ------------------- | -------------- | ------------------------- | ----------- |
| Average TPS         | 19.2           | 19.8                      | +0.6 TPS    |
| Minimum TPS         | 16.4           | 18.2                      | +1.8 TPS    |
| Tick Duration (ms)  | 51.3 ms        | 45.1 ms                   | -12%        |
| CPU Usage (%)       | 38–42%         | 31–35%                    | -7% overall |
| RAM Usage           | 1.45 GB        | 1.28 GB                   | -170 MB     |
| Entity Tick Time    | 22.8 ms        | 18.6 ms                   | -18%        |
| Chunk Load Time     | 310 ms         | 260 ms                    | -16%        |
| Redstone Processing | High           | Medium                    | Improved    |
| Server Start Time   | 21.5 sec       | 18.7 sec                  | -13%        |

**Note: All results were recorded while running Dynamic PERFORMANCE+ together with Dynamic LIMITER+ enabled.**

</details>

---

## Admin Commands

All `/dp` commands require the `dynamicperformance.admin` permission.

- **`/dp optimize`** – Applies one-click optimizations, including clearing lag, unloading chunks, reducing ticking loads, minimizing redstone activity, and throttling mob AI.

- **`/dp entities`** – Cleans up entities to free server resources and improve performance.

- **`/dp chunks`** – Manages chunk loading and unloading for optimal server performance.

- **`/dp stats`** – Displays real-time performance statistics such as TPS, memory usage, and entity counts.

- **`/dp reload`** – Reloads the plugin configuration without restarting the server.

- **`/dp boost`** – Activates boost mode for a configurable duration, temporarily enhancing server performance by reducing AI, redstone, and entity load.

---
## Compatibility

- Fully compatible with Paper, Purpur, and Spigot servers (Minecraft 1.21.x).  
- Requires no client-side modifications.

---
## Key Features

- Intelligent load balancing to prevent server lag.  
- Adaptive AI throttling for high-density mob areas.  
- Configurable thresholds for automated optimizations.  
- Supports large-scale farms, redstone contraptions, and complex builds.  
