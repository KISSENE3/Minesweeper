package com.kissene3.gui.screens;

import com.kissene3.gui.*;
import com.kissene3.gui.interfaces.Component;
import org.jetbrains.annotations.*;

import java.awt.*;
import java.util.List;

@SuppressWarnings("unused")
public abstract class Screen {
	
	/* ---------- ATTRIBUTES ---------- */
	
	protected ScreenManager manager;
	protected ComponentManager componentsManager;
	protected Color backgroundColor = Color.BLACK;
	
	/* ---------- ATTRIBUTES ---------- */
	/* ---------- CONSTRUCTORS ---------- */
	
	public Screen(@NotNull ScreenManager manager) {
		this.manager = manager;
		this.componentsManager = new ComponentManager(this);
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
	public abstract void onTick();
	
	/* ---------- ABSTRACTS ---------- */
	/* ---------- GETTERS/SETTERS ---------- */
	
	public ComponentManager getComponentsManager() {
		return this.componentsManager;
	}
	
	public void setManager(ScreenManager manager) {
		this.manager = manager;
	}
	public ScreenManager getManager() {
		return this.manager;
	}
	
	/* ---------- GETTERS/SETTERS ---------- */
	/* ---------- METHODS ---------- */
	
	@MustBeInvokedByOverriders
	public void dispose() {
		List<Component> components = this.componentsManager.getComponents();
		components.forEach(Component::dispose);
		this.componentsManager.removeAllComponents();
	}
	
	/* ---------- METHODS ---------- */
}
