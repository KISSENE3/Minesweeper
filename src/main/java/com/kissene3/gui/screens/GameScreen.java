package com.kissene3.gui.screens;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class GameScreen extends Screen {
	
	public GameScreen(@NotNull ScreenManager manager) {
		super(manager);
	}
	
	@Override
	public boolean isTransparent() {
		return false;
	}
	
	@Override
	public int getX() {
		return 0;
	}
	
	@Override
	public int getY() {
		return 0;
	}
	
	@Override
	public int getWidth() {
		return 0;
	}
	
	@Override
	public int getHeight() {
		return 0;
	}
	
	@Override
	public void draw(@NotNull Graphics2D g) {
	
	}
}
