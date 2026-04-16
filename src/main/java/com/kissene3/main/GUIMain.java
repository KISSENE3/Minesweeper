package com.kissene3.main;

import com.kissene3.game.*;
import com.kissene3.gui.*;
import com.kissene3.gui.components.*;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

public class GUIMain {
	public static MyGUIManager manager = new MyGUIManager();
	public static Settings settings = SettingsIO.load();
	
	public static void main(String[] args) {
		
		Difficulties diff = Difficulties.EASY;
		
		int gridW = diff.width();
		int gridH = diff.height();
		int mines = diff.numMines();
		int cellSize = 32;
		
		Grid grid = new Grid(50, 50, gridW, gridH, mines, cellSize, settings);
		List<MyButton> buttons = new ArrayList<>();
		
		buttons.add(new MyButton(0, 0, 40, 40) {
			@Override
			public void onClick(@NotNull MouseEvent e) {
				if (settings.isTheme("Light"))
					settings.setTheme("Dark");
				else if (settings.isTheme("Dark"))
					settings.setTheme("Light");
			}
		});
		buttons.add(new MyButton(10, 0, 40, 40) {
			@Override
			public void onClick(@NotNull MouseEvent e) {
				if (MouseEvent.BUTTON1 == e.getButton()) {
					grid.setCellSize(grid.getCellSize() + 16);
				} else if (MouseEvent.BUTTON3 == e.getButton()) {
					grid.setCellSize(grid.getCellSize() - 16);
				}
			}
		});
		buttons.add(new MyButton(20, 0, 40, 40) {
			@Override
			public void onClick(@NotNull MouseEvent e) {
				if (MouseEvent.BUTTON1 == e.getButton()) {
					grid.setTopLeft(grid.getX() + 10, grid.getY() + 10);
				} else if (MouseEvent.BUTTON3 == e.getButton()) {
					grid.setTopLeft(grid.getX() - 10, grid.getY() - 10);
				}
			}
		});
		buttons.add(new MyButton(399, 599, 200, 50, "QUIT"));
		buttons.add(new MyButton(399, 529, 200, 50, "SETTINGS"));
		buttons.add(new MyButton(399, 459, 200, 50, "PLAY"));
		
		manager.addComponent(grid);
		manager.addComponents(buttons);
		
		Frame frame = new Frame("Minesweeper");
		frame.setLayout(new BorderLayout());
		
		int canvasW = 16*64;
		int canvasH = 16*64;
		
		Canvas canvas = new Canvas() {
			@Override
			public void paint(Graphics g) {}
		};
		canvas.setPreferredSize(new Dimension(canvasW, canvasH));
		
		canvas.addMouseListener(manager);
		canvas.addMouseMotionListener(manager);
		canvas.addKeyListener(manager);
		
		frame.add(canvas);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				SettingsIO.save(settings);
				System.exit(0);
			}
		});
		
		canvas.createBufferStrategy(2);
		Thread renderThread = getThread(canvas, grid);
		
		renderThread.start();
	}
	
	@SuppressWarnings("BusyWait")
	private static @NotNull Thread getThread(@NotNull Canvas canvas, @NotNull Grid grid) {
		return new Thread(() -> {
			BufferStrategy bufferStrategy = canvas.getBufferStrategy();
			while (bufferStrategy == null) {
				canvas.createBufferStrategy(2);
				bufferStrategy = canvas.getBufferStrategy();
			}
			
			while (true) {
				Graphics2D g = null;
				try {
					g = (Graphics2D) bufferStrategy.getDrawGraphics();
					
					g.setColor(Color.BLACK);
					g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
					
					manager.draw(g);
					
				} finally {
					if (g != null) g.dispose();
				}
				
				bufferStrategy.show();
				Toolkit.getDefaultToolkit().sync();
				
				try {
					Thread.sleep(16);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}
			}
		});
	}
}