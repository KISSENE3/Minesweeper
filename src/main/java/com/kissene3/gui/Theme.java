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
		@SerializedName("title_outline") Color titleOutline,
		@SerializedName("text") Color text,
		@SerializedName("selected_difficulty") Color selectedDifficulty
) {
	
	public Theme {
		if (
				background == null || backgroundLighterShade == null || backgroundDarkerShade == null || border == null || backgroundExplodedMine == null ||
				overlay == null || cross == null || flag == null || flagBase == null || mine == null || mineShade == null || numberOne == null ||
				numberTwo == null || numberThree == null || numberFour == null || numberFive == null || numberSix == null || numberSeven == null ||
				numberEight == null || title == null || titleOutline == null || text == null || selectedDifficulty == null
		) {
			throw new IllegalArgumentException("Theme colors cannot be null. Check the JSON fields.");
		}
	}
	
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
