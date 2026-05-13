package com.kissene3.gui.io;

import com.kissene3.main.Main;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.*;

@SuppressWarnings("unused")
public class AppLogger {
	private static final Logger LOGGER = Logger.getLogger(AppLogger.class.getName());
	
	static {
		try {
			FileHandler fh = new FileHandler(Main.BASE_PATH + "logs" + File.separator + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".txt", true);
			
			fh.setFormatter(new Formatter() {
				private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				
				@Override
				public String format(LogRecord record) {
					return String.format("[%s] [%s] %s%n",
							record.getInstant().atZone(ZoneId.systemDefault()).format(formatter),
							//LocalDateTime.now().format(formatter),
							record.getLevel(),
							record.getMessage());
				}
			});
			
			LOGGER.addHandler(fh);
			LOGGER.setUseParentHandlers(false);
			
		} catch (Exception e) {
			System.err.println("Failed to initialize logger: " + e.getMessage());
		}
	}
	
	public static void severe(String msg) { LOGGER.severe(msg); }
	public static void warning(String msg) { LOGGER.warning(msg); }
	public static void info(String msg) { LOGGER.info(msg); }
	public static void config(String msg) { LOGGER.config(msg); }
	public static void fine(String msg) { LOGGER.fine(msg); }
	public static void finer(String msg) { LOGGER.finer(msg); }
	public static void finest(String msg) { LOGGER.finest(msg); }
}
