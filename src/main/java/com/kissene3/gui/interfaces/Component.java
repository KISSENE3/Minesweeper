package com.kissene3.gui.interfaces;

import com.kissene3.gui.utilities.MyUtilities.*;
import org.jetbrains.annotations.NotNull;

public interface Component {
	void setBounds(int x, int y, int width, int height);
	void setBounds(@NotNull Rectangle bounds);
	int getX();
	int getY();
	int getWidth();
	int getHeight();
	boolean isActive();
	void setActive(boolean isActive);
	boolean contains(int mx, int my);
	void dispose();
}
