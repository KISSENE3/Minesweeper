package com.kissene3.gui.interfaces;

import org.jetbrains.annotations.*;

import java.awt.event.*;

/** Interface that must be implemented by components that handle focus and keyboard input. */
@SuppressWarnings("unused")
public interface Focusable {
	
	/**
	 * The method called when a key is typed (pressed and then released).
	 * @param e The {@link KeyEvent} of the action.
	 */
	void onKeyTyped(@NotNull KeyEvent e);
	
	/**
	 * The method called when a key is pressed.
	 * @param e The {@link KeyEvent} of the action.
	 */
	void onKeyPressed(@NotNull KeyEvent e);
	
	/**
	 * The method called when a key is released.
	 * @param e The {@link KeyEvent} of the action.
	 */
	void onKeyReleased(@NotNull KeyEvent e);
	
	/** Gets the focus state of the component. */
	boolean isFocused();
	
	/** Sets the focus state of the component to the specified value. */
	void setFocus(boolean focused);
}