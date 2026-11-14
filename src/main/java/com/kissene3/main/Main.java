package com.kissene3.main;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main {
	/* ---------- STATIC FINAL ATTRIBUTES ---------- */
	
	public static final String version = "1.0";
	public static final Path BASE_PATH;
	private static boolean useAnsi;
	private static boolean terminalLog;
	
	/* ---------- STATIC FINAL ATTRIBUTES ---------- */
	/* ---------- CONSTRUCTORS ---------- */
	
	static {
		
		// 1. Get path
		String os = System.getProperty("os.name").toLowerCase();
		String home = System.getProperty("user.home");
		
		if (os.contains("win")) {	// Windows
			String localAppData = System.getenv("LOCALAPPDATA");
			BASE_PATH = Paths.get(localAppData != null ? localAppData : home, "AppData", "Local", "Games", "KISSENE3", "Minesweeper");
		} else if (os.contains("mac")) {	// Mac OS
			BASE_PATH = Paths.get(home, "Library", "Application Support", "Games", "KISSENE3", "Minesweeper");
		} else {	// Unix
			String xdgData = System.getenv("XDG_DATA_HOME");
			BASE_PATH = Paths.get(xdgData != null ? xdgData : home,".local", "share", "Games", "KISSENE3", "Minesweeper");
		}
		
		// 2. Create directories
		String[] dirs = {"difficulties", "themes", "logs", "saves"};
		for (String d : dirs) {
			Path dirPath = BASE_PATH.resolve(d);
			try {
				Files.createDirectories(dirPath);
			} catch (IOException e) {
				System.err.println("Error creating directory: " + dirPath + " -> " + e.getMessage());
			}
		}
		
		// 3. Copy files in directories
		Map<String, List<String>> filesToCopy = Map.ofEntries(
				Map.entry("difficulties", List.of("easy.json", "medium.json", "hard.json", "README.md", "README.txt")),
				Map.entry("themes", List.of("light.json", "dark.json", "README.md", "README.txt"))
		);
		
		for (Map.Entry<String, List<String>> entry : filesToCopy.entrySet()) {
			String folder = entry.getKey();
			for (String fileName : entry.getValue()) {
				copyResourceToFile(
						"/" + folder + "/" + fileName,
						Path.of(folder, fileName)
				);
			}
		}
	}
	
	/* ---------- CONSTRUCTORS ---------- */
	/* ---------- UTILITIES ---------- */
	
	private static void copyResourceToFile(@NotNull String resourcePath, Path destinationSubPath) {
		Path destination = BASE_PATH.resolve(destinationSubPath);
		
		boolean isReadme = resourcePath.endsWith("README.md") || resourcePath.endsWith("README.txt");
		if (Files.exists(destination) && !isReadme) {
			return;
		}
		
		try (InputStream is = Main.class.getResourceAsStream(resourcePath)) {
			if (is != null) {
				if (destination.getParent() != null) {
					Files.createDirectories(destination.getParent());
				}
				Files.copy(is, destination, StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (Exception e) {
			System.err.println("Error while copying resource to file: " + resourcePath + " -> " + e.getMessage());
		}
	}
	
	/* ---------- UTILITIES ---------- */
	/* ---------- MAIN ---------- */
	
	@SuppressWarnings("UnnecessaryModifier")
	public static void main(@Nullable String[] args) {
		if (args == null) {
			new GUIMain();
			return;
		}
		
		if (args.length > 2) {
			System.err.println("Error: Too many arguments.");
			printHelp();
			return;
		}
		
		boolean showHelp = false;
		boolean isCli = false;
		boolean terminalLog = false;
		boolean useAnsi = true;
		
		for (String rawArg : args) {
			if (rawArg == null)
				continue;
			String text = rawArg.toLowerCase().replace("-", "");
			
			switch (text) {
				case "help", "h" -> showHelp = true;
				case "cli", "console", "terminal", "cmd" -> isCli = true;
				//noinspection SpellCheckingInspection
				case "log", "logs", "terminallog", "cmdlog", "clilog", "consolelog" -> terminalLog = true;
				//noinspection SpellCheckingInspection
				case "noansi", "disableansi", "nocolor" -> useAnsi = false;
				default -> {
					System.err.println("Unknown command: " + text);
					showHelp = true;
				}
			}
		}
		
		if (showHelp) {
			printHelp();
			return;
		}
		
		if (isCli && terminalLog) {
			System.err.println("Error: Impossible to use cli mode and terminal logs at the same time.");
			printHelp();
			return;
		}
		
		Main.useAnsi = useAnsi;
		Main.terminalLog = terminalLog;
		
		if (isCli) {
			System.setProperty("java.awt.headless", "true");
			new CLIMain();
		} else
			new GUIMain();
	}
	
	/* ---------- MAIN ---------- */
	
	@SuppressWarnings("SpellCheckingInspection")
	private static void printHelp() {
		System.out.println("Usage: java -jar Minesweeper.jar [arg1] [arg2]");
		System.out.println();
		System.out.println("Examples:");
		System.out.println("  java -jar Minesweeper.jar cli noansi    (Starts CLI without colors)");
		System.out.println("  java -jar Minesweeper.jar log           (Starts GUI with terminal logs, the logs are colored using ANSI)");
		System.out.println();
		System.out.println("Available arguments (mix up to 2):");
		System.out.println("  cli           Starts the application in CLI mode (headless). Mutually exclusive with 'log'.");
		System.out.println("  log           Shows logs in terminal alongside the GUI file logs. Mutually exclusive with 'cli'.");
		System.out.println("  noansi        Disables ANSI colors and text formatting.");
		System.out.println("  help          Shows this help page, invalidates all other arguments.");
		System.out.println();
		System.out.println("Note: Running with no arguments starts the standard GUI.");
	}
	
	public static boolean useAnsi() {
		return useAnsi;
	}
	
	public static boolean terminalLog() {
		return terminalLog;
	}
}
