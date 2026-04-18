# Difficulty Configurations

This folder contains JSON files used to define the grid size and mine density for the game. You can create custom difficulty levels by adding new `.json` files here.

> [!TIP]
> New difficulties will automatically appear in the game menu using the **filename** (e.g., `hard.json` will show up as "hard").
>
> **Note:** For consistency and to avoid naming conflicts, please use **only lowercase letters** for the filenames (e.g., `hard.json` instead of `Hard.json`).
---

## 🛠 Field Definitions

Each configuration file must include the following three fields:

| Field    | Description                                          |
|:---------|:-----------------------------------------------------|
| `width`  | The number of columns in the grid.                   |
| `height` | The number of rows in the grid.                      |
| `mines`  | The total number of mines to be placed on the board. |

---

## ⚠️ Validation Rules

To ensure the game remains playable, all files must follow these constraints:

### 1. Minimum Dimensions
The total area of the board must be **greater than 9**.
* **Rule:** `width * height > 9`
* *Reasoning:* A board size of 3x3 or smaller is too trivial for a standard game experience.

### 2. Mine Count
There must be **at least 1 mine** present on the board.
* **Rule:** `mines >= 1`
* *Note:* The number of mines must be less than the total number of cells (`width * height`) to ensure there is at least one safe spot!

---

## 📝 Example Format (`easy.json`)

```json
{
  "width": 9,
  "height": 9,
  "mines": 10
}
```
---

## 🔄 Reset & Defaults

The game comes with three standard difficulties: `easy.json`, `medium.json`, and `hard.json`. 

* **Customization:** You can freely modify these files or create new ones to change the game balance.
* **Restoring Defaults:** If you make a mistake or want to go back to the original settings, simply **delete the file(s)** from this folder. 
* **Auto-Repair:** The next time you launch the game, the missing default files (including this README) will be automatically re-created.

> [!WARNING]
> If you want to keep your custom changes, make sure to back them up before deleting any files!