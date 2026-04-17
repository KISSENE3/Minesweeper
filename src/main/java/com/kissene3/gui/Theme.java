package com.kissene3.gui;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

@SuppressWarnings("unused")
public record Theme(
		@SerializedName("background") Color background,
		@SerializedName("background_lighter_shade") Color backgroundLighterShade,
		@SerializedName("background_darker_shade") Color backgroundDarkerShade,
		@SerializedName("border") Color border,
		@SerializedName("background_exploded_mine") Color backgroundExplodedMine,
		@SerializedName("overlay") Color overlay,
		@SerializedName("cross") Color cross,
		@SerializedName("flag") Color flag,
		@SerializedName("flag_base") Color flagBase,
		@SerializedName("mine") Color mine,
		@SerializedName("mine_shade") Color mineShade,
		@SerializedName("number_one") Color numberOne,
		@SerializedName("number_two") Color numberTwo,
		@SerializedName("number_three") Color numberThree,
		@SerializedName("number_four") Color numberFour,
		@SerializedName("number_five") Color numberFive,
		@SerializedName("number_six") Color numberSix,
		@SerializedName("number_seven") Color numberSeven,
		@SerializedName("number_eight") Color numberEight,
		@SerializedName("title") Color title,
		@SerializedName("title_outline") Color titleOutline
) {
	
	public Theme {
		if (
				background == null || backgroundLighterShade == null || backgroundDarkerShade == null || border == null ||
				backgroundExplodedMine == null || overlay == null || cross == null || flag == null || flagBase == null ||
				mine == null || mineShade == null || numberOne == null || numberTwo == null || numberThree == null || numberFour == null ||
				numberFive == null || numberSix == null || numberSeven == null || numberEight == null || title == null || titleOutline == null
		) {
			throw new IllegalArgumentException("Theme colors cannot be null. Check the JSON fields.");
		}
	}
	
	public static final Theme LIGHT_MODE = new Theme(
			new Color(192, 192, 192, 255),		// Background
			new Color(255, 255, 255, 255),		// Background lighter shade
			new Color(128, 128, 128, 255),		// Background darker shade
			new Color(128, 128, 128, 255),		// Border
			new Color(255,   0,   0, 255),		// Background exploded mine
			new Color(255, 255, 255,  96),		// Overlay
			new Color(170,   0,   0, 255),		// Cross
			new Color(255,   0,   0, 255),		// Flag
			new Color(  0,   0,   0, 255),		// Flag base
			new Color(  0,   0,   0, 255),		// Mine
			new Color(255, 255, 255, 255),		// Mine Shade
			new Color(  0,   0, 255, 255),		// Number one
			new Color(  0, 128,   0, 255),		// Number two
			new Color(255,   0,   0, 255),		// Number three
			new Color(  0,   0, 128, 255),		// Number four
			new Color(128,   0,   0, 255),		// Number five
			new Color(  0, 128, 128, 255),		// Number six
			new Color(  0,   0,   0, 255),		// Number seven
			new Color(128, 128, 128, 255),		// Number eight
			new Color(255,   0,   0, 255),		// Title
			new Color(  0,   0,   0, 255)		// Title outline
	);
	
	public static final Theme DARK_MODE = new Theme(
			new Color( 30,  30,  30, 255),		// Background
			new Color( 75,  75,  75, 255),		// Background lighter shade
			new Color( 15,  15,  15, 255),		// Background darker shade
			new Color( 60,  60,  60, 255),		// Border
			new Color(150,   0,   0, 255),		// Background exploded mine
			new Color(100, 149, 237,  40),		// Overlay
			new Color(255, 255, 255, 255),		// Cross
			new Color(255,  45,  85, 255),		// Flag
			new Color(255, 255, 255, 255),		// Flag base
			new Color(200, 200, 200, 255),		// Mine
			new Color(255, 255, 255, 255),		// Mine Shade
			new Color(100, 149, 237, 255),		// Number one
			new Color(144, 238, 144, 255),		// Number two
			new Color(255,  99,  71, 255),		// Number three
			new Color(180, 130, 255, 255),		// Number four
			new Color(255, 160, 122, 255),		// Number five
			new Color(  0, 206, 209, 255),		// Number six
			new Color(220, 220, 220, 255),		// Number seven
			new Color(160, 160, 160, 255),		// Number eight
			new Color(160,   0,   0, 255),		// Title
			new Color(128, 128, 128, 255)		// Title outline
	);
	
	/**
	 * Gets the color of the <code>number</code> entered.
	 * @param number The number with the color to get.
	 * @return The color of the <code>number</code>.
	 * @throws IllegalArgumentException If the <code>number</code> is not between 1 and 8.
	 */
	public @NotNull Color getNumberColor(int number) {
		return switch (number) {
			case 1 -> this.numberOne;
			case 2 -> this.numberTwo;
			case 3 -> this.numberThree;
			case 4 -> this.numberFour;
			case 5 -> this.numberFive;
			case 6 -> this.numberSix;
			case 7 -> this.numberSeven;
			case 8 -> this.numberEight;
			default -> throw new IllegalArgumentException("The number must be between 1 and 8.");
		};
	}
}
