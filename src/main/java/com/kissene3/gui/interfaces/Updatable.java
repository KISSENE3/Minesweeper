package com.kissene3.gui.interfaces;

/** Interface for objects that require logic updates at a fixed or variable rate (TPS). */
public interface Updatable {
	
	/**
	 * Updates the internal state of the object.
	 * This method is called independently of the rendering (FPS).
	 */
	void update();
}
