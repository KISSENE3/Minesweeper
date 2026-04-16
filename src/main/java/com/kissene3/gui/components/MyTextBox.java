package com.kissene3.gui.components;

import com.kissene3.gui.MyUtilities.*;
import com.kissene3.gui.MyUtilities.Dimension;
import com.kissene3.gui.MyUtilities.Rectangle;
import com.kissene3.gui.interfaces.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/** A graphical text input component */
@SuppressWarnings("unused")
public class MyTextBox extends MyAbstractButton implements Focusable {
	
	/* ---------- ATTRIBUTES ---------- */
	
	/** Indicates if the component is currently receiving keyboard input. */
	private boolean isFocused = false;
	
	/** The maximum number of characters allowed in the text field. */
	private int maxCharacters = 64;
	
	/** The internal buffer used to manage text efficiently. */
	private final StringBuilder text = new StringBuilder();
	
	/** The font used for rendering both text and placeholder. */
	private Font font;
	
	/** The text displayed when the field is empty. */
	private String placeholder = null;
	
	/** The current position of the cursor within the text string. */
	private int cursorIndex = 0;
	
	/** The number of pixels the text is shifted to the left (for scrolling). */
	private int scrollOffset = 0;
	
	/** Color scheme for various component states. */
	private Color borderColor = Color.BLACK, backgroundColor = Color.WHITE, foregroundColor = Color.BLACK;
	private Color disabledBorderColor = Color.DARK_GRAY, disabledBackgroundColor = Color.WHITE, disabledForegroundColor = Color.DARK_GRAY;
	private Color placeholderColor = Color.LIGHT_GRAY;
	
	/* ---------- ATTRIBUTES ---------- */
	/* ---------- CONSTRUCTORS ---------- */
	
	public MyTextBox(int x, int y, int width, int height) {
		this(new Rectangle(x, y, width, height));
	}
	
	public MyTextBox(@NotNull Coordinates topLeft, @NotNull Dimension dimensions) {
		this(new Rectangle(topLeft, dimensions));
	}
	
	public MyTextBox(@NotNull Rectangle bounds) {
		super(bounds);
		this.font = new Font("Monospaced", Font.PLAIN, (int)(this.getHeight() * 0.6));
	}
	
	/* ---------- CONSTRUCTORS ---------- */
	/* ---------- TEXT MANAGEMENT ---------- */
	
	/** Sets the text content, enforcing the maximum character limit. */
	public void setText(@Nullable String text) {
		this.text.setLength(0);
		if (text == null) return;
		if (text.length() > this.maxCharacters) text = text.substring(0, this.maxCharacters);
		this.text.append(text);
	}
	
	/** Appends text to the current content until maxCharacters is reached. */
	public void appendText(@NotNull String text) {
		int availableSpace = this.maxCharacters - this.text.length();
		if (availableSpace <= 0) return;
		if (text.length() > availableSpace) text = text.substring(0, availableSpace);
		this.text.append(text);
	}
	
	/** @return The current string representation of the internal text buffer. */
	public @NotNull String getText() {
		return this.text.toString();
	}
	
	/* ---------- TEXT MANAGEMENT ---------- */
	/* ---------- FONT & PLACEHOLDER GETTERS/SETTERS ---------- */
	
	public void setFont(@NotNull Font font) {
		this.font = font;
	}
	public @NotNull Font getFont() {
		return this.font;
	}
	
	public void setPlaceholder(@Nullable String placeholder) {
		this.placeholder = placeholder;
	}
	public @NotNull String getPlaceholder() {
		return this.placeholder == null ? "" : this.placeholder;
	}
	
	/** Sets the max character limit.
	 * @throws IllegalArgumentException if <= 0.
	 */
	public void setMaxCharacters(int maxCharacters) {
		if (maxCharacters <= 0) throw new IllegalArgumentException("Max characters must be > 0");
		this.maxCharacters = maxCharacters;
	}
	public int getMaxCharacters() {
		return this.maxCharacters;
	}
	
	/* ---------- FONT & PLACEHOLDER GETTERS/SETTERS ---------- */
	/* ---------- COLORS GETTERS/SETTERS ---------- */
	
	public @NotNull Color getBorderColor() {
		return this.borderColor;
	}
	public void setBorderColor(@NotNull Color color) {
		this.borderColor = color;
	}
	
	public @NotNull Color getBackgroundColor() {
		return this.backgroundColor;
	}
	public void setBackgroundColor(@NotNull Color color) {
		this.backgroundColor = color;
	}
	
	public @NotNull Color getForegroundColor() {
		return this.foregroundColor;
	}
	public void setForegroundColor(@NotNull Color color) {
		this.foregroundColor = color;
	}
	
	public @NotNull Color getDisabledBorderColor() {
		return this.disabledBorderColor;
	}
	public void setDisabledBorderColor(@NotNull Color color) {
		this.disabledBorderColor = color;
	}
	
	public @NotNull Color getDisabledBackgroundColor() {
		return this.disabledBackgroundColor;
	}
	public void setDisabledBackgroundColor(@NotNull Color color) {
		this.disabledBackgroundColor = color;
	}
	
	public @NotNull Color getDisabledForegroundColor() {
		return this.disabledForegroundColor;
	}
	public void setDisabledForegroundColor(@NotNull Color color) {
		this.disabledForegroundColor = color;
	}
	
	public @NotNull Color getPlaceholderColor() {
		return this.placeholderColor;
	}
	public void setPlaceholderColor(@NotNull Color color) {
		this.placeholderColor = color;
	}
	
	/* ---------- COLORS GETTERS/SETTERS ---------- */
	/* ---------- @Override MyComponent ---------- */
	
	@Override
	protected void onBoundsChanged() {
		this.font = new Font("Monospaced", Font.PLAIN, (int)(this.getHeight() * 0.6));
	}
	
	/* ---------- @Override MyComponent ---------- */
	/* ---------- @Override Focusable ---------- */
	
	@Override
	public boolean isFocused() {
		return this.isFocused;
	}
	@Override
	public void setFocus(boolean focused) {
		this.isFocused = focused;
	}
	
	@Override
	public void onKeyTyped(@NotNull KeyEvent e) {
		char c = e.getKeyChar();
		if (c >= ' ' && this.text.length() < this.maxCharacters) {
			this.text.insert(this.cursorIndex, c);
			this.cursorIndex++;
			this.updateScroll();
		}
	}
	
	@Override
	public void onKeyPressed(@NotNull KeyEvent e) {
		int code = e.getKeyCode();
		boolean ctrl = e.isControlDown();
		
		if (ctrl && code == KeyEvent.VK_C) {
			this.copyToClipboard();
		} else if (ctrl && code == KeyEvent.VK_V) {
			this.pasteFromClipboard();
		} else if (code == KeyEvent.VK_BACK_SPACE && this.cursorIndex > 0) {
			this.text.deleteCharAt(this.cursorIndex - 1);
			this.cursorIndex--;
		} else if (code == KeyEvent.VK_DELETE && this.cursorIndex < this.text.length()) {
			this.text.deleteCharAt(this.cursorIndex);
		} else if (code == KeyEvent.VK_LEFT && this.cursorIndex > 0) {
			this.cursorIndex--;
		} else if (code == KeyEvent.VK_RIGHT && this.cursorIndex < this.text.length()) {
			this.cursorIndex++;
		}
		
		this.updateScroll();
	}
	
	@Override
	public void onKeyReleased(@NotNull KeyEvent e) {}
	
	/* ---------- @Override Focusable ---------- */
	/* ---------- @Override Drawable ---------- */
	
	@Override
	public void draw(@NotNull Graphics2D g) {
		if (!this.isVisible) return;
		
		// 1. Prepare Colors
		Color background = this.isActive ? this.backgroundColor : this.disabledBackgroundColor;
		Color border = this.isActive ? (this.isFocused ? Color.BLUE : this.borderColor) : this.disabledBorderColor;
		Color foreground = this.isActive ? this.foregroundColor : this.disabledForegroundColor;
		
		// 2. Background and Border
		g.setColor(background);
		g.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
		g.setColor(border);
		g.drawRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
		if (this.isFocused && this.isActive) g.drawRect(this.getX() + 1, this.getY() + 1, this.getWidth() - 2, this.getHeight() - 2);
		
		// 3. Text and Placeholder
		g.setFont(this.font);
		FontMetrics fm = g.getFontMetrics();
		this.updateScroll();
		
		int textX = this.getX() + 5;
		int textY = this.getY() + (this.getHeight() + fm.getAscent()) / 2 - 2;
		
		// --- CLIPPING AREA ---
		Shape oldClip = g.getClip();
		g.setClip(this.getX() + 1, this.getY() + 1, this.getWidth() - 2, this.getHeight() - 2);
		
		if (!this.text.isEmpty()) {
			g.setColor(this.isActive ? this.foregroundColor : this.disabledForegroundColor);
			g.drawString(this.text.toString(), textX - this.scrollOffset, textY);
		} else if (!this.isFocused && this.placeholder != null && !this.placeholder.isEmpty()) {
			g.setColor(this.placeholderColor);
			g.drawString(this.placeholder, textX, textY);
		}
		
		// --- CURSOR ---
		if (this.isFocused && this.isActive && (System.currentTimeMillis() / 500) % 2 == 0) {
			int cursorPosX = fm.stringWidth(this.text.substring(0, this.cursorIndex));
			int finalCursorX = textX + cursorPosX - this.scrollOffset;
			g.drawLine(finalCursorX, this.getY() + 5, finalCursorX, this.getY() + this.getHeight() - 5);
		}
		
		g.setClip(oldClip);
	}
	
	/* ---------- @Override Drawable ---------- */
	/* ---------- @Override Clickable ---------- */
	
	@Override
	public void onPress(@NotNull MouseEvent e) {
		this.isFocused = true;
		
		int mx = e.getX();
		int relativeX = mx - (this.getX() + 5) + this.scrollOffset;
		
		FontMetrics fm = new Canvas().getFontMetrics(this.font);
		
		if (relativeX <= 0) {
			this.cursorIndex = 0;
			return;
		}
		
		int currentX;
		int bestIndex = 0;
		int minDiff = Math.abs(relativeX);
		
		for (int i = 1; i <= this.text.length(); i++) {
			currentX = fm.stringWidth(this.text.substring(0, i));
			int diff = Math.abs(relativeX - currentX);
			
			if (diff > minDiff)
				break;
			
			minDiff = diff;
			bestIndex = i;
		}
		
		this.cursorIndex = bestIndex;
	}
	
	/* ---------- @Override Clickable ---------- */
	/* ---------- UTILITY ---------- */
	
	private void updateScroll() {
		FontMetrics fm = new Canvas().getFontMetrics(this.font);
		int visibleWidth = this.getWidth() - 10;
		
		int cursorPosX = fm.stringWidth(this.text.substring(0, this.cursorIndex));
		
		if (cursorPosX - this.scrollOffset > visibleWidth) {
			this.scrollOffset = cursorPosX - visibleWidth;
		}
		if (cursorPosX - this.scrollOffset < 0) {
			this.scrollOffset = cursorPosX;
		}
	}
	
	/** Copies the current text to the system clipboard. */
	private void copyToClipboard() {
		StringSelection selection = new StringSelection(this.text.toString());
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
	}
	
	/** Pastes text from the system clipboard into the current cursor position. */
	private void pasteFromClipboard() {
		try {
			String data = (String) Toolkit.getDefaultToolkit()
					.getSystemClipboard().getData(DataFlavor.stringFlavor);
			if (data != null) {
				this.appendTextAtCursor(data);
			}
		} catch (Exception _) {}
	}
	
	/** Inserts a string at the current cursor position, respecting maxCharacters. */
	private void appendTextAtCursor(@NotNull String str) {
		int availableSpace = this.maxCharacters - this.text.length();
		if (availableSpace <= 0) return;
		
		if (str.length() > availableSpace) {
			str = str.substring(0, availableSpace);
		}
		
		this.text.insert(this.cursorIndex, str);
		this.cursorIndex += str.length();
	}
	
	/* ---------- UTILITY ---------- */
}
