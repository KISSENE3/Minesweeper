package com.kissene3.gui.interfaces;

import com.kissene3.gui.util.*;
import org.jetbrains.annotations.*;

public interface Component {
	void setBounds(int x, int y, int width, int height);
	void setBounds(@NotNull MyRectangle bounds);
	int getX();
	int getY();
	int getWidth();
	int getHeight();
	boolean isActive();
	void setActive(boolean isActive);
	boolean contains(int mx, int my);
	void dispose();
}
