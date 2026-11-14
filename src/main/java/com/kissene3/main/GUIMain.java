package com.kissene3.main;

import com.kissene3.gui.ScreenManager;
import com.kissene3.gui.io.*;
import com.kissene3.gui.models.Settings;
import com.kissene3.gui.screens.*;
import com.kissene3.io.AppLogger;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class GUIMain {
	/* ---------- ATTRIBUTES ---------- */
	
	public Settings settings;
	
	/* ---------- ATTRIBUTES ---------- */
	/* ---------- CONSTRUCTORS ---------- */
	
	public GUIMain() {
		AppLogger.info("Booting GUI in version " + Main.version + "...");
		this.settings = SettingsRegistry.load();
		this.start();
	}
	
	public void start() {
		Frame frame = new Frame("Minesweeper");
		frame.setLayout(new BorderLayout());
		
		Canvas canvas = new Canvas();
		frame.add(canvas);
		frame.setResizable(false);
		frame.setVisible(true);
		
		ScreenManager screenManager = new ScreenManager(canvas, frame, this.settings);
		screenManager.pushScreen(new MenuScreen(screenManager));
		
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				SettingsRegistry.save(settings);
				AppLogger.info("Quitting...");
				System.exit(0);
			}
		});
		
		this.startRendering(canvas, screenManager);
		this.startTicking(screenManager);
	}
	
	/* ---------- CONSTRUCTORS ---------- */
	/* ---------- UTILITIES ---------- */
	
	protected void startRendering(Canvas canvas, ScreenManager sm) {
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
				
				try { //noinspection BusyWait
					Thread.sleep(16);
				} catch (Exception _) {}
			}
		}).start();
	}
	
	protected void startTicking(ScreenManager sm) {
		new Thread(() -> {
			long lastTime = System.nanoTime();
			double nsPerTick = 1000000000.0 / 30.0;	// 30 TPS
			double delta = 0;
			
			while (true) {
				long now = System.nanoTime();
				delta += (now - lastTime) / nsPerTick;
				lastTime = now;
				
				while (delta >= 1) {
					sm.onTick();
					delta--;
				}
				
				try { //noinspection BusyWait
					Thread.sleep(16);
				} catch (Exception _) {}
			}
		}).start();
	}
	
	/* ---------- UTILITIES ---------- */
}
