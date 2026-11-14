# 💣 Java Minesweeper Engine

A cross-platform, highly optimized, hand-rolled Minesweeper implementation built entirely from scratch in Java. Designed with a custom GUI rendering pipeline (`java.awt`) and a fully functional command-line interface (CLI) mode, featuring custom state encryption and serialization.

![Release v1.0](https://img.shields.io/badge/release-v1.0-blue.svg)
![Java](https://img.shields.io/badge/Java-25-orange.svg)
![License](https://img.shields.io/badge/license-MIT-green.svg)

---

## ✨ Key Architectural Highlights

* **Pure Custom Engine & UI Framework:** Built from the ground up using raw `java.awt.Frame` and `java.awt.Canvas` with a custom-built screen manager, mouse event propagation, and layout components—completely bypassing heavy third-party UI framework boilerplate.
* **Ultra-Compact Binary Save State Serialization:** Custom-engineered serializer that packs active game states (dimensions, grid data, cell states, mine layouts, and timers) into raw bytes and compresses them into a single `BigInteger` encrypted via an XOR mask derived from a dynamic seed. An 81-cell standard board serializes down to just **47 bytes**.
* **Dual Runtime Mode (GUI & CLI):** Supports a fully interactive graphical user interface alongside a headless CLI/terminal mode with colored ANSI output, designed to run anywhere from desktop environments to headless servers.
* **Dynamic Theme & Texture System:** Features real-time texture invalidation, custom procedural pixel-art drawing primitives scaled via factor `p`, and persistent settings (`settings.json`) that save diffs against default configurations.
* **Robust Input & Viewport Management:** Includes advanced grid clipping, `mouse-wheel` vertical scrolling, and `Shift + mouse-wheel` horizontal scrolling for large custom difficulty boards.

---

## 🚀 Getting Started & Usage

You can download the compiled executable fat JAR directly from the [GitHub Releases](https://github.com/KISSENE3/Minesweeper/releases/tag/v1.0) page.

To run the application via command line:

```bash
# Launch standard GUI mode
java -jar Minesweeper-1.0.jar

# Launch CLI mode (headless)
java -jar Minesweeper-1.0.jar cli

# Launch GUI with live terminal logs (ANSI colored)
java -jar Minesweeper-1.0.jar log
```

To see a preview of all possible arguments start the application this way:
```bash
java -jar Minesweeper-1.0.jar help
```
