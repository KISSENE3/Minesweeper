package com.kissene3.gui.screens;

import com.kissene3.gui.*;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

@SuppressWarnings("unused")
public abstract class Screen {
	
	/* ---------- ATTRIBUTES ---------- */
	
	protected ScreenManager manager;
	protected MyGUIManager componentsManager;
	Color backgroundColor = Color.BLACK;
	
	/* ---------- ATTRIBUTES ---------- */
	/* ---------- CONSTRUCTORS ---------- */
	
	public Screen(@NotNull ScreenManager manager) {
		this.manager = manager;
		this.componentsManager = new MyGUIManager(this);
	}
	
	/* ---------- CONSTRUCTORS ---------- */
	/* ---------- ABSTRACTS ---------- */
	
	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public abstract boolean isTransparent();
	public abstract int getX();
	public abstract int getY();
	public abstract int getWidth();
	public abstract int getHeight();
	public abstract void draw(@NotNull Graphics2D g);
	
	/* ---------- ABSTRACTS ---------- */
	/* ---------- GETTERS/SETTERS ---------- */
	
	public MyGUIManager getComponentsManager() {
		return this.componentsManager;
	}
	
	public void setManager(ScreenManager manager) {
		this.manager = manager;
	}
	public ScreenManager getManager() {
		return this.manager;
	}
	
	/* ---------- GETTERS/SETTERS ---------- */
}
