package com.kissene3.game;

import java.util.*;

@SuppressWarnings("unused")
public enum CellDisplay {
	HIDDEN("-"),
	FLAGGED("F"),
	MINE("*"),
	ZERO(" "),
	ONE("1"),
	TWO("2"),
	THREE("3"),
	FOUR("4"),
	FIVE("5"),
	SIX("6"),
	SEVEN("7"),
	EIGHT("8");
	
	private final String symbol;
	private static final Map<String, CellDisplay> LOOKUP = new HashMap<>();
	
	CellDisplay(String symbol) {
		this.symbol = symbol;
	}
	
	public String getSymbol() {
		return this.symbol;
	}
	
	@Override
	public String toString() {
		return this.symbol;
	}
	
	public static CellDisplay fromSymbol(String s) {
		return LOOKUP.getOrDefault(s, HIDDEN);
	}
	
	static {
		for (CellDisplay cd : values()) {
			LOOKUP.put(cd.symbol, cd);
		}
	}
}
