package com.kissene3.gui;

@SuppressWarnings("unused")
public enum CellSize {
	SMALL(16),
	MEDIUM(32),
	BIG(48);
	
	private final int size;
	
	CellSize(int size) {
		this.size = size;
	}
	
	public int getSize() {
		return this.size;
	}
}
