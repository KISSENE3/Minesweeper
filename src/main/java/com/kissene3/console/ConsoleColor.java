package com.kissene3.console;

public enum ConsoleColor {
	// RESET
	RESET("\u001B[0m"),
	
	// Foreground
	BLACK("\u001B[30m"),
	RED("\u001B[31m"),
	GREEN("\u001B[32m"),
	YELLOW("\u001B[33m"),
	BLUE("\u001B[34m"),
	PURPLE("\u001B[35m"),
	CYAN("\u001B[36m"),
	GRAY("\u001B[90m"),
	WHITE("\u001B[37m"),
	
	// Bright foreground
	RED_BRIGHT("\u001B[91m"),
	GREEN_BRIGHT("\u001B[92m"),
	YELLOW_BRIGHT("\u001B[93m"),
	BLUE_BRIGHT("\u001B[94m"),
	CYAN_BRIGHT("\u001B[96m"),
	
	// Backgrounds
	BG_RED("\u001B[41m"),
	BG_GREEN("\u001B[42m"),
	BG_YELLOW("\u001B[43m"),
	BG_BLUE("\u001B[44m"),
	
	// Mine background
	MINE_EXPLODED("\u001B[41m\u001B[30m");
	
	private final String code;
	
	ConsoleColor(String code) {
		this.code = code;
	}
	
	@Override
	public String toString() {
		return this.code;
	}
}