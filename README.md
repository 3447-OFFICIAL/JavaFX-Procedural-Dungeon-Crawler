# 🗡️ JavaFX Procedural Dungeon Crawler

> **A scalable, AI-driven roguelike engine built with Java & JavaFX**  
> Procedurally generated worlds, adaptive difficulty, and infinite replayability.

---

## 🚀 Overview

This project is a **2D top-down roguelike dungeon crawler** engineered using modern Java and JavaFX. It demonstrates advanced concepts such as **procedural content generation, adaptive AI systems, and modular game architecture**.

The system is designed not just as a game, but as a **reusable game engine foundation** for future expansions.

---

## 🌟 Key Features

### 🧊 Procedural World Generation
- Infinite dungeon floors generated using a **room-and-corridor algorithm**
- Each run delivers a **unique playable layout**
- Increasing structural complexity with depth (smaller rooms, denser paths)

### 🤖 AI Director System
- Dynamic difficulty adjustment based on player progression
- Real-time balancing of:
  - Enemy strength
  - Map density
- Ensures **optimal engagement curve**

### ⚔️ Combat & RPG Mechanics
- Grid-based melee combat system
- Enemy scaling:
  - +15 HP / level
  - +3 Damage / level
- Player progression:
  - +10 Max HP / floor
  - +2 Attack Power / floor
- Full HP restoration on level transition

### 📊 Real-time HUD System
- Live tracking of:
  - Player HP
  - Attack Power
  - Remaining enemies
- Visual depth indicator (LEVEL system)

---

## 🧠 Concepts & Technologies

### 🎮 Core Game Development
- Game Loop (JavaFX `AnimationTimer`)
- Real-time rendering pipeline
- Event-driven input handling

### 🎲 Algorithms & Logic
- Procedural generation (randomized + constrained)
- Grid-based collision detection
- State management (Game Over, Restart, Active Play)

### 🤖 AI & Scaling Systems
- Adaptive difficulty model (AI Director)
- Progressive enemy stat scaling
- Player growth balancing

### 🧩 Software Engineering
- Object-Oriented Programming (OOP)
- Modular architecture (separation of concerns)
- Extensible system design for future features

---

## 🛠️ Tech Stack

| Layer        | Technology        |
|--------------|------------------|
| Language     | Java 17+ (Optimized for Java 26) |
| Framework    | JavaFX (Canvas Rendering) |
| Architecture | Modular OOP + Game Loop Pattern |

---

## 📂 Project Structure
src/
├── engine/ # Core game loop & state management
├── entities/ # Player, Enemy, base classes
├── world/ # Dungeon generation logic
├── ui/ # HUD and rendering
├── input/ # Keyboard handling
└── main/ # Entry point

---

## 🚀 Getting Started

### 1. Prerequisites
- **Java JDK 17 or later** (Recommended: JDK 26).
- **JavaFX SDK** (Tested with openjfx-26).

### 2. Installation
1.  Clone this repository.
2.  Download the **JavaFX SDK** for your operating system.
3.  Place the unzipped SDK folder (e.g., `openjfx-26_windows-x64_bin-sdk`) in the project root.

### 3. Running the Game
If you are on Windows, simply run:
```powershell
.\run.bat
```
*(Make sure the paths in `run.bat` match your local JDK and JavaFX installation.)*

---

## 🎮 Controls
*   **WASD / Arrow Keys**: Move character
*   **Spacebar**: Attack
*   **R**: Restart (after Game Over)

---

## 📜 License
This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.
