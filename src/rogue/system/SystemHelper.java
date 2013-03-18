/*
 * System.java
 * Copyright (C) 2013 dirk <dirk@Valinor.local>
 *
 * Distributed under terms of the MIT license.
 */
package rogue.system;

public class SystemHelper {
	public static boolean debug = false;

	/**
	 * Prints a message to Console, prefixed with '> Debug', when debug option has been set
	 *
	 * @param message Message to be printed
	 */
	public static void debugMessage(String message) {
		if (debug) {
			System.out.println("> Debug: "+message);
		}
	}

	/**
	 * Prints a message to Console, prefixed with '(!) Error:'
	 *
	 * @param message Message to be printed
	 */
	public static void errorMessage(String message) {
		System.out.println("(!) Error: "+message);
	}

	/**
	 * Reads a list of strings and sets Options in SystemHelper accordingly
	 *
	 * @param args List of Arguments
	 */
	public static void getArgs(String[] args) {
		for (int i=0;i<args.length;i++) {
			if (args[i].compareTo("debug") == 0) {
				// Debugmode selected
				SystemHelper.debug = true;
			}
		}
	}
}
