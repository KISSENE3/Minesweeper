package com.kissene3.gui;//package main.java.com.kissene3.gui;
//
//import main.java.com.kissene3.gui.interfaces.*;
//import org.jetbrains.annotations.*;
//import java.awt.*;
//import java.awt.event.*;
//import java.util.*;
//import java.util.List;
//
//@SuppressWarnings("unused")
//public class MySpinner<T> extends MyComponent implements Drawable {
//	private final List<T> options;
//	private int currentIndex = 0;
//
//	private final MyAbstractButton btnPrev;
//	private final MyAbstractButton btnNext;
//
//	public MySpinner(int x, int y, int width, int height, List<T> options) {
//		super(x, y, width, height);
//		this.options = options;
//
//		@SuppressWarnings("redundant") int btnSize = height;
//
//		this.btnPrev = new MyButton(x, y, btnSize, btnSize) {
//			@Override
//			public void onClick(@NotNull MouseEvent e) { previous(); }
//		};
//
//		// Pulsante Destro
//		this.btnNext = new MyButton(x + width - btnSize, y, btnSize, btnSize) {
//			@Override
//			public void onClick(@NotNull MouseEvent e) { next(); }
//		};
//	}
//
//	public void next() {
//		currentIndex = (currentIndex + 1) % options.size();
//		onValueChanged(options.get(currentIndex));
//	}
//
//	public void previous() {
//		currentIndex = (currentIndex - 1 + options.size()) % options.size();
//		onValueChanged(options.get(currentIndex));
//	}
//
//	public T getSelected() {
//		return options.get(currentIndex);
//	}
//
//	/**
//	 * Metodo da sovrascrivere o gestire tramite un listener
//	 * per aggiornare le MyTextBox della difficoltà.
//	 */
//	public void onValueChanged(T newValue) { }
//
//	@Override
//	public void draw(@NotNull Graphics g) {
//		this.btnPrev.draw(g);
//		this.btnNext.draw(g);
//
//		g.setColor(Color.BLACK);
//		g.drawRect(this.x + this.height, this.y, this.width - (this.height * 2), this.height);
//
//		String label = this.options.get(this.currentIndex).toString();
//		g.setFont(new Font("Monospaced", Font.BOLD, (int)(this.height * 0.5)));
//		int labelWidth = g.getFontMetrics().stringWidth(label);
//		g.drawString(label, this.x + (this.width / 2) - (labelWidth / 2), this.y + (this.height / 2) + 5);
//	}
//}