# CraftEngine Client Mod
<<<<<<< Updated upstream
=======

A companion client mod for [CraftEngine](https://github.com/Xiao-MoMi/craft-engine), running on **Fabric**.

## Features

### 🧱 Real Custom Blocks
Without this mod, CraftEngine's custom blocks are rendered using vanilla block states (note blocks, tripwires, etc.), which limits how many blocks a server can have and how they look. This mod registers placeholder blocks on the client and lets the server remap them at runtime, so custom blocks are rendered with their true appearance — unlimited count, no vanilla state sacrifices.

### 🎒 CraftEngine Creative Tab
Adds a dedicated **CraftEngine** tab to the creative inventory, filled in real time with the custom items the server pushes to you. Browse and grab server items straight from creative mode — with their real models and textures.

### 🔄 Block Update Cancellation
Prevents ghost blocks and block desync when interacting with server-side custom blocks. The client asks the server before applying local block updates, keeping both sides in sync.

### 🖼️ Disable Resource Pack Loading Screen
Skips the resource pack loading screen, so server switching and pack reloads feel faster.

### 👻 Ghost Recipe Item Count
Forces item counts to be displayed on ghost recipe items in the recipe book.

## Requirements

- **Minecraft 26.2**
- **Fabric Loader** + **Fabric API**
- **Java 25+**
- CraftEngine installed on the server
- *(Optional)* **Mod Menu** + **Cloth Config** for the in-game settings screen

## Installation

1. Drop the jar into your client's `mods` folder.
2. Restart the game. Configuration is available in-game via Mod Menu, or in `config/craftengine/config.yml`.

## Configuration

| Option | Default | Description |
|---|---|---|
| `enable-client-custom-block` | `false` | Enable real custom block rendering. Requires rejoining the server to take effect. |
| `enable-cancel-block-update` | `false` | Cancel client-side block updates until the server confirms them. Only works on servers with CraftEngine installed. |
| `server-side-blocks` | `10000` | Number of placeholder blocks registered on the client. Requires a client restart. |
| `disable-resource-pack-loading-screen` | `false` | Skip the resource pack loading screen. |
| `force-ghost-recipe-show-input-itemstack-count` | `false` | Show item counts on ghost recipe ingredients. |
>>>>>>> Stashed changes
