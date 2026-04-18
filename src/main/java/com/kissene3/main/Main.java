package com.kissene3.main;

public class Main {
	@SuppressWarnings("UnnecessaryModifier")
	public static void main(String[] args) {
		if (args.length > 0 && ("cli".equalsIgnoreCase(args[0]) || "console".equalsIgnoreCase(args[0])))
			CLIMain.main();
		else
			GUIMain.main();
	}
}
