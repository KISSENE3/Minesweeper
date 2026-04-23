package com.kissene3.gui.interfaces;

import org.jetbrains.annotations.NotNull;

import java.awt.event.MouseWheelEvent;

public interface Scrollable {
	void onMouseWheel(@NotNull MouseWheelEvent e);
}
