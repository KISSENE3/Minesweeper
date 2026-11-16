package com.kissene3.gui.models;

import com.google.gson.annotations.SerializedName;

import java.awt.Color;

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
		@SerializedName("selected_difficulty") Color selectedDifficulty,
		@SerializedName("scroll_bar") Color scrollBar,
		@SerializedName("smiley_face") Color smileyBase,
		@SerializedName("smiley_border") Color smileyBorder,
		@SerializedName("pause_base") Color pauseBase,
		@SerializedName("pause_border") Color pauseBorder,
		@SerializedName("github_icon_background") Color github_background,
		@SerializedName("github_icon_foreground") Color github_foreground
) {
	
	public Theme {
		if (
				background == null || backgroundLighterShade == null || backgroundDarkerShade == null || border == null || backgroundExplodedMine == null ||
				overlay == null || cross == null || flag == null || flagBase == null || mine == null || mineShade == null || numberOne == null ||
				numberTwo == null || numberThree == null || numberFour == null || numberFive == null || numberSix == null || numberSeven == null ||
				numberEight == null || title == null || titleOutline == null || text == null || selectedDifficulty == null || scrollBar == null ||
				smileyBase == null || smileyBorder == null || pauseBase == null || pauseBorder == null || github_background == null || github_foreground == null
		) {
			throw new IllegalArgumentException("Theme colors cannot be null. Check the JSON fields.");
		}
	}
}
