# 🗡️ JavaFX Procedural Dungeon Crawler

A 2D top-down procedurally generated roguelike game built with **Java 26** and **JavaFX**. Character growth, an AI-driven "Director" difficulty system, and infinite dungeon crawling.

---

## 🌟 Features

### 🧊 Procedural World Generation
*   **Dynamic Maps**: Every floor is uniquely generated with a random room-and-corridor algorithm.
*   **AI Director**: The game automatically increases map complexity (smaller rooms, tighter mazes) as you descend deeper.

### ⚔️ Combat & RPG Systems
*   **Melee Combat**: Strike adjacent enemies with standard attacks (Spacebar).
*   **Scaling Difficulty**: Enemies gain **+15 HP** and **+3 Damage** each level.
*   **Permanent Progression**: Your player gains **+10 Max HP** and **+2 Attack Power** on every floor cleared.
*   **Staircase Recovery**: Discovering the stairs to the next level fully restores your health.

### 📊 Real-time HUD
*   Track your current **HP**, **Attack Power**, and **Enemies Remaining**.
*   View your current depth in the golden **LEVEL** indicator.

---

## 🛠️ Tech Stack
*   **Language**: Java 17+ (Optimised for Java 26)
*   **Framework**: JavaFX (Canvas-based rendering)
*   **Architecture**: Modular OOP with a central AnimationTimer game loop.

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
