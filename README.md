# Dynamic PERFORMANCE+

## Overview

Dynamic PERFORMANCE+ is a Minecraft server optimization plugin for Paper-based servers (1.21.x) focused on reducing tick lag and improving performance through asynchronous task handling and intelligent system management. It aims to maintain consistent TPS without altering fundamental game mechanics such as physics or entity behavior.


<details>
<summary>RESULTS CHART</summary>
*NOTE: These all results were collected with the recommended ecosystem given below.*

----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

![RESULTS CHART](https://cdn.modrinth.com/data/cached_images/7469835933d89340400ff05557970c99f480fb52_0.webp)

----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

**Higher Entity Cleanup and Chunk Loading -> Worse Performance**

**Higher TPS -> Best Performance**
</details>


## Core Optimizations

- Asynchronous Task Scheduling: Offloads intensive operations such as chunk management and cleanup to async threads to minimize main-thread load.

- Chunk Management: Dynamically unloads and loads chunks based on player proximity and activity using an optimized queue system.

- Entity & Mob Processing: Implements distance-based culling and conditional AI throttling to reduce unnecessary entity tick updates.

- Falling Block Handling: Keeps falling block entities fully functional while optimizing their physics updates for smoother performance.

- Explosion Optimization: Limits simultaneous explosion calculations and manages block/particle effects to lower CPU overhead.

- Hopper Transfer Control: Adjusts hopper transfer rates and timing intervals to prevent tick slowdowns in large hopper chains.

- Automated Cleanup: Periodically removes untracked entities and dropped items to reclaim performance.

- Performance Monitoring: Tracks MSPT and TPS in real-time with configurable thresholds that can trigger auto-optimizations.

## Commands & Configuration

All settings are configurable through a structured YAML file.
Admin commands (prefixed with /dp) include:

/dp optimize – Triggers manual optimization cycles.

/dp stats – Displays current MSPT, TPS, and entity counts.

/dp reload – Reloads the configuration at runtime.

/dp clean – Removes dropped entities and unused chunks.

## Compatibility

Tested on:

Paper, Purpur, and Spigot servers (Minecraft 1.21.x).
No client modifications are required.

## Notes

Dynamic PERFORMANCE+ focuses on balancing optimization with gameplay integrity. It does not disable key mechanics or remove content — instead, it tunes existing systems to reduce CPU and memory usage.

## Recommended Ecosystem (Optional)

For best results, this plugin integrates well with:

[Dynamic CORE+](https://modrinth.com/plugin/dynamic-core+)
 — Essentials alternative

[Dynamic AC+](https://modrinth.com/plugin/dynamic-ac+)
 — Anti-cheat and movement limiter

[Dynamic LIMITER+](https://modrinth.com/plugin/dynamic-limiter+)
 — Entity and redstone limiter

[Dynamic BACKUP+](https://modrinth.com/plugin/dynamic-backup+)
 — Scheduled backup system
