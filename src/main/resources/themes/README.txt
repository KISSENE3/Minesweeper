===============================================================================
                          VISUAL THEMES CONFIGURATION
===============================================================================

This folder contains JSON files used to customize the visual appearance of the
game. You can create your own themes by adding new .json files here.

HINT:
New themes will automatically be detected by the game and identified by their
filename (e.g., 'light.json' will be listed as "Light").

NOTE:
For consistency and to avoid naming conflicts, please use ONLY lowercase
letters for the filenames (e.g., 'dark.json' instead of 'Dark.json').


-------------------------------------------------------------------------------
COLOR DEFINITIONS
-------------------------------------------------------------------------------

Each theme file must define a complete set of colors. Colors can be specified
in Hexadecimal format using either RRGGBB or AARRGGBB (with alpha channel).

* background
  -> The main color of the board cells.
* background_lighter_shade
  -> Highlight color for the 3D effect (top/left borders).
* background_darker_shade
  -> Shadow color for the 3D effect (bottom/right borders).
* border
  -> Color of the grid lines and panel borders.
* background_exploded_mine
  -> The background color of the cell that caused the game over.
* overlay
  -> A semi-transparent color applied on components hovered by the mouse.
* cross
  -> Color of the 'X' marking an incorrect flag.
* flag
  -> Color of the flag icon.
* flag_base
  -> Color of the flag pole/base.
* mine
  -> The main color of the mine icon.
* mine_shade
  -> The reflection/highlight color on the mine.
* number_one to number_eight
  -> Colors for the numbers indicating nearby mines (1-8).
* title
  -> Primary color for the game title/UI text.
* title_outline
  -> Outline color for the game title/UI text.
* text
  -> General color for standard text labels and UI information.
* selected_difficulty
  -> Highlight color used to indicate the currently selected difficulty item.
* scroll_bar
  -> Color of the interactive track bars when content overflows the screen.
* smiley_face
  -> The base color of Smiley.
* smiley_border
  -> Color of the border and the details of Smiley.
* pause_base
  -> The main color of the cogwheel icon.
* pause_border
  -> Color of the border of the cogwheel icon.
* github_icon_background
  -> Color of the background of the GitHub icon.
* github_icon_foreground
  -> Color of the cat of the GitHub icon.


-------------------------------------------------------------------------------
COLOR FORMATS
-------------------------------------------------------------------------------

The game engine is flexible with how you write hex codes. The following formats
are all valid:

- Standard: #RRGGBB or RRGGBB
- Java-style: 0xRRGGBB or 0XRRGGBB
- With Alpha: AARRGGBB, #AARRGGBB or 0xAARRGGBB

If you use a 6-character format, the alpha channel will automatically be set
to 100% (fully opaque). Properties like 'overlay' or 'scroll_bar' typically
use a lower alpha to support transparency (e.g., #60FFFFFF).


-------------------------------------------------------------------------------
EXAMPLE FORMAT (light.json)
-------------------------------------------------------------------------------

{
  "background": "#FFC0C0C0",
  "background_lighter_shade": "#FFFFFFFF",
  "background_darker_shade": "#FF808080",
  "border": "#FF808080",
  "background_exploded_mine": "#FFFF0000",
  "overlay": "#60FFFFFF",
  "cross": "#FFAA0000",
  "flag": "#FFFF0000",
  "flag_base": "#FF000000",
  "mine": "#FF000000",
  "mine_shade": "#FFFFFFFF",
  "number_one": "#FF0000FF",
  "number_two": "#FF008000",
  "number_three": "#FFFF0000",
  "number_four": "#FF000080",
  "number_five": "#FF800000",
  "number_six": "#FF008080",
  "number_seven": "#FF000000",
  "number_eight": "#FF808080",
  "title": "#FFFF0000",
  "title_outline": "#FF000000",
  "text": "#FF000000",
  "selected_difficulty": "#FF4090FF",
  "scroll_bar": "#66000000",
  "smiley_face": "#FFFFFF00",
  "smiley_border": "#FF000000",
  "pause_base": "#FF505050",
  "pause_border": "#FF000000",
  "github_icon_background": "#1B1F23",
  "github_icon_foreground": "#FFCCCCCC"
}


-------------------------------------------------------------------------------
RESET & DEFAULTS
-------------------------------------------------------------------------------

The game comes with two standard themes: light.json and dark.json.

* Customization:
  You can freely modify these files or create new ones to change the game
  themes.

* Restoring Defaults:
  If you make a mistake or want to go back to the original settings, simply
  DELETE THE FILE(S) from this folder.

* Auto-Repair:
  The next time you launch the game, the missing default files (including
  this README) will be automatically re-created.

* Important Notes:
  You can save any notes in any file that's not a .json. The README.md and
  the README.txt files will always be renewed, so DO NOT keep your personal
  notes in these files!

WARNING:
If you want to keep your custom changes, make sure to back them up before
deleting any files!
===============================================================================