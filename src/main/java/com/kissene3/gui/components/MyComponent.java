package com.kissene3.gui.components;

import com.kissene3.gui.interfaces.*;
import com.kissene3.gui.util.*;
import org.jetbrains.annotations.*;

import static java.util.Objects.requireNonNull;

/**
 * The basic component that all other GUI components extend.<br />
 * It provides basic functionalities like position, size and activity.<br />
 * It also includes a utility method to check if a point is within its bounds.
 */
@SuppressWarnings("unused")
public abstract class MyComponent implements Component {
	/* ---------- ATTRIBUTES ---------- */
	
	private MyRectangle bounds;
	
	/** The flag that indicates if the component is active and can be interacted with. */
	protected boolean isActive = true;
	
	/* ---------- ATTRIBUTES ---------- */
	/* ---------- CONSTRUCTORS ---------- */
	
	public MyComponent(int x, int y, int width, int height) {
		this(new MyRectangle(x, y, width, height));
	}
	
	public MyComponent(@NotNull MyCoordinates topLeft, @NotNull MyDimension dimensions) {
		this(new MyRectangle(topLeft, dimensions));
	}
	
	public MyComponent(@NotNull MyRectangle bounds) {
		this.setBounds(bounds);
	}
	
	/* ---------- CONSTRUCTORS ---------- */
	/* ---------- SPATIAL GETTERS/SETTERS ---------- */
	
	public final @NotNull MyRectangle getBounds() {
		return this.bounds;
	}
	
	/** The master setter for the component's bounds. All other spatial setters delegate to this method. */
	@Override
	public final void setBounds(@NotNull MyRectangle bounds) {
		this.bounds = requireNonNull(bounds);
		this.onBoundsChanged();
	}
	
	public final void setBounds(@NotNull MyCoordinates topLeft, @NotNull MyDimension dimensions) {
		this.setBounds(new MyRectangle(topLeft, dimensions));
	}
	
	@Override
	public final void setBounds(int x, int y, int width, int height) {
		this.setBounds(new MyRectangle(x, y, width, height));
	}
	
	@Override
	public final int getX() {
		return this.bounds.x();
	}
	public final void setX(int x) {
		this.setBounds(this.bounds.withX(x));
	}
	
	@Override
	public final int getY() {
		return this.bounds.y();
	}
	public final void setY(int y) {
		this.setBounds(this.bounds.withY(y));
	}
	
	@Override
	public final int getWidth() {
		return this.bounds.width();
	}
	public final void setWidth(int width) {
		this.setBounds(this.bounds.withWidth(width));
	}
	
	@Override
	public final int getHeight() {
		return this.bounds.height();
	}
	public final void setHeight(int height) {
		this.setBounds(this.bounds.withHeight(height));
	}
	
	public final @NotNull MyCoordinates getTopLeft() {
		return this.bounds.topLeft();
	}
	public final void setTopLeft(@NotNull MyCoordinates topLeft) {
		this.setBounds(this.bounds.withTopLeft(topLeft));
	}
	public final void setTopLeft(int x, int y) {
		this.setTopLeft(new MyCoordinates(x, y));
	}
	
	public final @NotNull MyCoordinates getBottomRight() {
		return this.bounds.bottomRight();
	}
	
	public final @NotNull MyDimension getDimensions() {
		return this.bounds.dimensions();
	}
	public final void setDimensions(@NotNull MyDimension dimensions) {
		this.setBounds(this.bounds.withDimensions(dimensions));
	}
	public final void setDimensions(int width, int height) {
		this.setDimensions(new MyDimension(width, height));
	}
	
	/* ---------- SPATIAL/SETTERS ---------- */
	/* ---------- FLAGS GETTERS/SETTERS ---------- */
	
	@Override
	public boolean isActive() {
		return this.isActive;
	}
	@Override
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	/* ---------- FLAGS GETTERS/SETTERS ---------- */
	/* ---------- UTILITY ---------- */
	
	/**
	 * Checks if the specified coordinates ({@code mx} and {@code my}) are contained within the component's bounds.
	 * @param mx The x-coordinate to check.
	 * @param my The y-coordinate to check.
	 * @return {@code true} if the coordinates are within the component's bounds, {@code false} otherwise.
	 */
	@Contract(pure = true)
	@Override
	public boolean contains(int mx, int my) {
		return this.bounds.contains(mx, my);
	}
	
	/**
	 * Checks if the specified {@code coordinates} are contained within the component's bounds.
	 * @param coordinates The {@link MyCoordinates} to check.
	 * @return {@code true} if the coordinates are within the component's bounds, {@code false} otherwise.
	 */
	@Contract(pure = true)
	public boolean contains(@NotNull MyCoordinates coordinates) {
		return this.bounds.contains(coordinates);
	}
	
	/**
	 * Checks if the specified {@code myRectangle} is contained within the component's bounds.
	 * @param myRectangle The {@link MyRectangle} to check.
	 * @return {@code true} if the myRectangle is within the component's bounds, {@code false} otherwise.
	 */
	@Contract(pure = true)
	public boolean contains(@NotNull MyRectangle myRectangle) {
		return this.bounds.contains(myRectangle);
	}
	
	/**
	 * Checks if the specified component ({@code other}) is contained within the component's bounds.
	 * @param other The {@link MyComponent} to check.
	 * @return {@code true} if the other component is within this component's bounds, {@code false} otherwise.
	 */
	@Contract(pure = true)
	public boolean contains(@NotNull MyComponent other) {
		return this.bounds.contains(other.getBounds());
	}
	
	protected void onBoundsChanged() {}
	
	@Override
	public void dispose() {}
	
	/* ---------- UTILITY ---------- */
}
