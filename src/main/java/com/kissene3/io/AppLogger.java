package com.kissene3.io;

import com.kissene3.cli.ConsoleColor;
import com.kissene3.main.*;

import java.io.IOException;
import java.nio.file.*;
import java.time.*;
import java.time.format.*;
import java.util.List;
import java.util.logging.*;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class AppLogger {
	/* ---------- STATIC FINAL ATTRIBUTES ---------- */
	
	private static final Logger LOGGER = Logger.getLogger(AppLogger.class.getName());
	
	/* ---------- STATIC FINAL ATTRIBUTES ---------- */
	/* ---------- CONSTRUCTORS ---------- */
	static {
		try {
			Path logDir = Main.BASE_PATH.resolve("logs");
			Files.createDirectories(logDir);
			
			String logFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".txt";
			Path logFilePath = logDir.resolve(logFileName);
			
			FileHandler fh = new FileHandler(logFilePath.toAbsolutePath().toString(), true);
			
			fh.setFormatter(new Formatter() {
				private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				
				@Override
				public String format(LogRecord record) {
					return String.format("[%s] [%s] %s%n",
							record.getInstant().atZone(ZoneId.systemDefault()).format(formatter),
							record.getLevel(),
							record.getMessage());
				}
			});
			
			LOGGER.addHandler(fh);
			LOGGER.setUseParentHandlers(false);
			
			cleanOldLogs(logDir, 20);
			
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				try {
					fh.close();
				} catch (Exception e) {
					System.err.println("Failed to close log file handler: " + e.getMessage());
				}
			}));
			
		} catch (Exception e) {
			System.err.println("Failed to initialize logger: " + e.getMessage());
		}
	}
	
	/* ---------- CONSTRUCTORS ---------- */
	/* ---------- STATIC METHODS ---------- */
	
	public static void severe(String msg) {
		LOGGER.severe(msg);
		if (Main.terminalLog())
			System.out.println(ConsoleColor.RED_BRIGHT + msg + ConsoleColor.RESET);
	}
	
	public static void warning(String msg) {
		LOGGER.warning(msg);
		if (Main.terminalLog())
			System.out.println(ConsoleColor.YELLOW + msg + ConsoleColor.RESET);
	}
	
	public static void info(String msg) {
		LOGGER.info(msg);
		if (Main.terminalLog())
			System.out.println(ConsoleColor.BLACK_BRIGHT + msg + ConsoleColor.RESET);
	}
	
	public static void config(String msg) {
		LOGGER.config(msg);
		if (Main.terminalLog())
			System.out.println(ConsoleColor.GREEN_BRIGHT + msg + ConsoleColor.RESET);
	}
	
	public static void fine(String msg) {
		LOGGER.fine(msg);
		if (Main.terminalLog())
			System.out.println(ConsoleColor.BLUE_BRIGHT + msg + ConsoleColor.RESET);
	}
	
	public static void finer(String msg) {
		LOGGER.finer(msg);
		if (Main.terminalLog())
			System.out.println(ConsoleColor.BLUE + msg + ConsoleColor.RESET);
	}
	
	public static void finest(String msg) {
		LOGGER.finest(msg);
		if (Main.terminalLog())
			System.out.println(ConsoleColor.CYAN + msg + ConsoleColor.RESET);
	}
	
	private static void cleanOldLogs(Path logDir, @SuppressWarnings("SameParameterValue") int maxFiles) {
		if (!Files.exists(logDir) || !Files.isDirectory(logDir))
			return;
		
		try (Stream<Path> stream = Files.list(logDir)) {
			List<Path> logFiles = stream
					.filter(Files::isRegularFile)
					.filter(p -> p.getFileName().toString().matches("\\d{4}-\\d{2}-\\d{2}_\\d{2}-\\d{2}-\\d{2}.txt"))
					.sorted()
					.toList();
			
			if (logFiles.size() > maxFiles) {
				List<Path> toDelete = logFiles.subList(0, logFiles.size() - maxFiles);
				
				for (Path file : toDelete) {
					try {
						Files.delete(file);
					} catch (IOException e) {
						System.err.println("Failed to delete log file" + file.getFileName() + ": " + e.getMessage());
					}
				}
			}
		} catch (IOException e) {
			System.err.println("Failed to clean old logs: " + e.getMessage());
		}
	}
	
	/* ---------- STATIC METHODS ---------- */
}
