# 🗡️ JavaFX Procedural Dungeon Crawler

**A scalable, AI-driven roguelike engine built with Java & JavaFX**  
Delivering procedural environments, adaptive intelligence, and infinite replayability.

---

## 🚀 Executive Overview

This project represents a **2D top-down roguelike dungeon crawler engine** engineered using modern Java and JavaFX. It is architected as a **foundational game engine**, not just a standalone game—enabling extensibility, scalability, and rapid feature iteration.

The implementation showcases **advanced algorithmic design, AI-driven systems, and modular software engineering principles** aligned with real-world game development practices.

---

## 🌟 Strategic Feature Set

### 🧊 Procedural World Generation Engine
- Infinite dungeon layers generated via **room-and-corridor procedural algorithms**
- Each session produces a **non-repetitive, dynamically structured map**
- Progressive complexity scaling:
  - Compact room distribution
  - Increased corridor density

---

### 🤖 AI Director (Adaptive Difficulty Engine)
- Real-time difficulty orchestration based on player performance metrics
- Dynamically adjusts:
  - Enemy attributes (HP, damage)
  - Dungeon density and challenge level
- Ensures **balanced engagement and retention curve**

---

### ⚔️ Combat & RPG Progression System
- Grid-based deterministic combat model
- Enemy scaling logic:
  - +15 HP per level
  - +3 Damage per level
- Player progression model:
  - +10 Max HP per floor
  - +2 Attack Power per floor
- Automatic HP regeneration on level transition

---

### 📊 Real-Time HUD & Telemetry
- Live in-game metrics visualization:
  - Player health
  - Attack power
  - Remaining enemies
- Depth-based progression indicator (**LEVEL system**)

---

## 🧠 Core Concepts & Engineering Domains

### 🎮 Game Development Architecture
- Game loop implementation using JavaFX `AnimationTimer`
- Real-time rendering via **Canvas pipeline**
- Event-driven input handling system

---

### 🎲 Algorithms & System Logic
- Procedural generation (randomized + constraint-based models)
- Grid-based collision detection
- Finite state management:
  - Active Play
  - Game Over
  - Restart lifecycle

---

### 🤖 AI & Scaling Systems
- Adaptive AI Director framework
- Progressive enemy scaling algorithms
- Player difficulty balancing mechanics

---

### 🧩 Software Engineering Practices
- Object-Oriented Programming (OOP)
- Modular architecture (high cohesion, low coupling)
- Scalable and extensible codebase design

---

## 🛠️ Technology Stack

| Layer        | Technology                          |
|--------------|----------------------------------|
| Language     | Java 17+ (Optimized for Java 26) |
| Framework    | JavaFX (Canvas Rendering Engine) |
| Architecture | Modular OOP + Game Loop Pattern  |

---

## 📂 Project Architecture

```bash
src/
├── engine/     # Core game loop & state orchestration
├── entities/   # Player, Enemy, base entity models
├── world/      # Procedural dungeon generation logic
├── ui/         # HUD, rendering, and overlays
├── input/      # Keyboard and interaction handling
└── main/       # Application entry point
```
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
Make sure the paths in `run.bat` match your local JDK and JavaFX installation.

---

## 🎮 Controls
*   **WASD / Arrow Keys**: Move character
*   **Spacebar**: Attack
*   **R**: Restart (after Game Over)

---

## 📜 License
This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.
