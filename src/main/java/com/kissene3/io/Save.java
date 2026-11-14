package com.kissene3.io;

import com.kissene3.game.Grid;
import com.kissene3.main.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.math.*;
import java.nio.file.*;
import java.util.Random;

public class Save {
	
	public static Path savePath;
	
	static {
		savePath = Main.BASE_PATH.resolve("saves", "save.dat");
	}
	
	public record SaveDataPackage(int rows, int columns, long thirties, BigInteger gameData) {}
	
	public static @Nullable BigInteger loadFile() {
		try {
			if (!hasSaveFile())
				return null;
			byte[] dataBytes = Files.readAllBytes(savePath);
			AppLogger.info("Save file has been loaded.");
			return new BigInteger(dataBytes);
		} catch (IOException e) {
			AppLogger.severe("Error loading the save file:" + e.getMessage());
			return null;
		}
	}
	
	public static void saveFile(@NotNull BigInteger bigInt) {
		try {
			byte[] dataBytes = bigInt.toByteArray();
			Files.write(savePath, dataBytes);
		} catch (IOException e) {
			AppLogger.severe("Error saving the game: " + e.getMessage());
		}
		AppLogger.info("Save file has been saved.");
	}
	
	public static @NotNull BigInteger convertGameToBigInteger(@NotNull Grid<?> grid, long thirties) {
		int columns = grid.getColumns();
		int rows = grid.getRows();
		BigInteger gameState = grid.getGameStateAsBigInteger();
		
		int bitLength = rows * columns * 3;
		Random random = new Random(thirties);
		BigInteger mask = new BigInteger(bitLength, random);
		BigInteger cryptedGameState = gameState.xor(mask);
		
		BigInteger saveData = BigInteger.ZERO;
		saveData = saveData.or(cryptedGameState);
		saveData = saveData.shiftLeft(64).or(BigInteger.valueOf(thirties));
		saveData = saveData.shiftLeft(32).or(BigInteger.valueOf(rows));
		saveData = saveData.shiftLeft(32).or(BigInteger.valueOf(columns));
		
		return saveData;
	}
	
	public static @NotNull SaveDataPackage parseSaveData(@NotNull BigInteger saveData) {
		int columns = saveData.intValue();
		BigInteger temp = saveData.shiftRight(32);
		int rows = temp.intValue();
		temp = temp.shiftRight(32);
		long thirties = temp.longValue();
		temp = temp.shiftRight(64);
		
		int bitLength = rows * columns * 3;
		Random random = new Random(thirties);
		BigInteger mask = new BigInteger(bitLength, random);
		BigInteger decryptedGameState = temp.xor(mask);
		
		return new SaveDataPackage(rows, columns, thirties, decryptedGameState);
	}
	
	@SuppressWarnings("UnusedReturnValue")
	public static boolean deleteSaveFile() {
		try {
			return Files.deleteIfExists(savePath);
		} catch (IOException e) {
			AppLogger.warning("Error deleting the save file: " + e.getMessage());
			return false;
		}
	}
	
	public static boolean hasSaveFile() {
		try {
			return Files.isRegularFile(savePath) &&  Files.isReadable(savePath) && Files.size(savePath) > 16;
		} catch (IOException e) {
			AppLogger.warning("Error checking the save file: " + e.getMessage());
			return false;
		}
	}
}
