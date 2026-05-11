package com.kissene3.main;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

public class Main {
	public static final String BASE_PATH;
	
	static {
		
		// 1. Get path
		String os = System.getProperty("os.name").toLowerCase();
		String home = System.getProperty("user.home");
		
		if (os.contains("win")) {	// Windows
			String localAppData = System.getenv("LOCALAPPDATA");
			BASE_PATH = (localAppData != null ? localAppData : home + "\\AppData\\Local") + "\\Games\\KISSENE3\\Minesweeper\\";
		} else if (os.contains("mac")) {	// Mac OS
			BASE_PATH = home + "/Library/Application Support/Games/KISSENE3/Minesweeper/";
		} else {	// Unix
			String xdgData = System.getenv("XDG_DATA_HOME");
			BASE_PATH = (xdgData != null ? xdgData : home + "/.local/share") + "/Games/KISSENE3/Minesweeper/";
		}
		
		// 2. Create directories
		String[] dirs = {"difficulties", "themes", "logs", "saves"};
		for (String d : dirs) {
			File f = new File(BASE_PATH + d);
			if (!f.exists()) //noinspection ResultOfMethodCallIgnored
				f.mkdirs();
		}
		
		// 3. Copy files in directories
		Map<String, List<String>> filesToCopy = Map.ofEntries(
				Map.entry("/difficulties", List.of("easy.json", "medium.json", "hard.json", "README.md")),
				Map.entry("/themes", List.of("light.json", "dark.json", "README.md"))
		);
		
		for (Map.Entry<String, List<String>> entry : filesToCopy.entrySet()) {
			for (String fileName : entry.getValue()) {
				copyResourceToFile(
						entry.getKey() + "/" + fileName,
						entry.getKey().substring(1) + File.separator + fileName);
			}
		}
	}
	
	private static void copyResourceToFile(String resourcePath, String destinationSubPath) {
		File destination = new File(BASE_PATH + destinationSubPath);
		
		if (destination.exists())
			return;
		
		try (InputStream is = Main.class.getResourceAsStream(resourcePath)) {
			if (is != null) {
				Files.copy(is, destination.toPath());
			}
		} catch (Exception e) {
			System.err.println("Error while copying resource to file: " + resourcePath + ": " + e.getMessage());
		}
	}
	
	@SuppressWarnings("UnnecessaryModifier")
	public static void main(String[] args) {
		if (args.length > 0 && ("cli".equalsIgnoreCase(args[0]) || "console".equalsIgnoreCase(args[0]) || "terminal".equalsIgnoreCase(args[0])))
			new CLIMain();
		else
			new GUIMain();
	}
}
