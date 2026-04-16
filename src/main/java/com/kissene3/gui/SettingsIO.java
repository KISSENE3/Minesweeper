package com.kissene3.gui;

import org.jetbrains.annotations.NotNull;

import java.io.*;

public class SettingsIO {
	private static final String FILE_PATH = "settings.dat";
	
	public static void save(Settings settings) {
		try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
			outputStream.writeObject(settings);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
	
	public static @NotNull Settings load() {
		File file = new File(FILE_PATH);
		if (!file.exists()) return new Settings();
		
		try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
			return (Settings) inputStream.readObject();
		} catch (IOException | ClassNotFoundException _) {
			return new Settings();
		}
	}
}