package com.kissene3.gui.screens;

import com.kissene3.gui.*;

import java.awt.*;

public abstract class Screen {
	
	/* ---------- ATTRIBUTES ---------- */
	
	protected ScreenManager manager;
	protected MyGUIManager componentsManager;
	Color backgroundColor = Color.BLACK;
	
	/* ---------- ATTRIBUTES ---------- */
	/* ---------- CONSTRUCTORS ---------- */
	
	public Screen(ScreenManager manager) {
		this.manager = manager;
		this.componentsManager = new MyGUIManager();
	}
	
	/* ---------- CONSTRUCTORS ---------- */
	/* ---------- ABSTRACTS ---------- */
	
	public abstract int getWidth();
	public abstract int getHeight();
	public abstract void draw(Graphics2D g);
	
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
