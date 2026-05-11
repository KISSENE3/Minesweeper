package com.kissene3.gui.interfaces;

/** Interface for objects that require logic updates at a fixed or variable rate (TPS). */
@SuppressWarnings("unused")
public interface Tickable {
	
	/**
	 * Updates the internal state of the object (TPS).<br />
	 * This method is called independently of the rendering (FPS).
	 */
	void tick();
}
