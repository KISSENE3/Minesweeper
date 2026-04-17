package com.kissene3.main;

import com.kissene3.game.DifficultyRegistry;
import com.kissene3.gui.*;
import com.kissene3.gui.io.SettingsRegistry;
import com.kissene3.gui.io.ThemeRegistry;
import com.kissene3.gui.screens.*;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

public class GUIMain {
	public static Settings settings = SettingsRegistry.load();
	
	@SuppressWarnings("UnnecessaryModifier")
	public static void main(@SuppressWarnings("unused") String[] args) {
		DifficultyRegistry.init();
		ThemeRegistry.init();
		
		Frame frame = new Frame("Minesweeper");
		frame.setLayout(new BorderLayout());
		
		Canvas canvas = new Canvas();
		frame.add(canvas);
		frame.setVisible(true);
		
		ScreenManager screenManager = new ScreenManager(canvas, frame, settings);
		
		screenManager.pushScreen(new MenuScreen(screenManager));
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				SettingsRegistry.save(settings);
				System.exit(0);
			}
		});
		
		startRendering(canvas, screenManager);
	}
	
	@SuppressWarnings("BusyWait")
	private static void startRendering(Canvas canvas, ScreenManager sm) {
		new Thread(() -> {
			canvas.createBufferStrategy(2);
			BufferStrategy bs = canvas.getBufferStrategy();
			
			while (true) {
				Graphics2D g = (Graphics2D) bs.getDrawGraphics();
				
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
				
				sm.draw(g);
				
				g.dispose();
				bs.show();
				Toolkit.getDefaultToolkit().sync();
				
				try { Thread.sleep(16); } catch (Exception _) {}
			}
		}).start();
	}
}
