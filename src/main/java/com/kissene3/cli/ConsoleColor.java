package com.kissene3.cli;

import com.kissene3.main.Main;

public enum ConsoleColor {
	// RESET
	RESET("\u001B[0m"),
	
	// Foregrounds
	BLACK("\u001B[30m"),
	RED("\u001B[31m"),
	GREEN("\u001B[32m"),
	YELLOW("\u001B[33m"),
	BLUE("\u001B[34m"),
	PURPLE("\u001B[35m"),
	CYAN("\u001B[36m"),
	WHITE("\u001B[37m"),
	
	// Backgrounds
	BG_BLACK("\u001B[40m"),
	BG_RED("\u001B[41m"),
	BG_GREEN("\u001B[42m"),
	BG_YELLOW("\u001B[43m"),
	BG_BLUE("\u001B[44m"),
	BG_PURPLE("\u001B[45m"),
	BG_CYAN("\u001B[46m"),
	BG_WHITE("\u001B[47m"),
	
	// Bright foregrounds
	BLACK_BRIGHT("\u001B[90m"),
	RED_BRIGHT("\u001B[91m"),
	GREEN_BRIGHT("\u001B[92m"),
	YELLOW_BRIGHT("\u001B[93m"),
	BLUE_BRIGHT("\u001B[94m"),
	PURPLE_BRIGHT("\u001B[95m"),
	CYAN_BRIGHT("\u001B[96m"),
	WHITE_BRIGHT("\u001B[97m"),
	
	// Bright foregrounds
	BG_BLACK_BRIGHT("\u001B[100m"),
	BG_RED_BRIGHT("\u001B[101m"),
	BG_GREEN_BRIGHT("\u001B[102m"),
	BG_YELLOW_BRIGHT("\u001B[103m"),
	BG_BLUE_BRIGHT("\u001B[104m"),
	BG_PURPLE_BRIGHT("\u001B[105m"),
	BG_CYAN_BRIGHT("\u001B[106m"),
	BG_WHITE_BRIGHT("\u001B[107m"),
	
	// Mine background
	MINE_EXPLODED("\u001B[30;41m");
	
	private final String code;
	
	ConsoleColor(String code) {
		this.code = code;
	}
	
	@Override
	public String toString() {
		return Main.useAnsi() ? this.code : "";
	}
}