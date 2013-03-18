
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

// TODO Move this File to rogue.system

package rogue.level;

import jade.core.World;
import jade.ui.TiledTermPanel;
import jade.util.datatype.ColoredChar;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * Used to print Stuff in Terminal.
 * 
 * @author
 */

public class Screen {
	public static World lastWorld;
	public static TiledTermPanel lastTerminal;
	public static ArrayList<String> eventLog;

	public static void initialiseScreen() {
		eventLog = new ArrayList<String>();
	}

	public static void showFile(String filePath, TiledTermPanel term,
			World world) {
		term.clearBuffer();
		try {
			// Prepare File for reading (open it)

			//FileReader fr = new FileReader(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath),"UTF-8"));
			// int lineNumber = MAXHEIGHT; // TODO Delete this line, when it is
			// not needed anymore

			// Read first line
			String line = br.readLine();
			// Count read Lines
			int lineNumber = 0;
			// Go through whole file
			while (line != null) {
				int i;
				for (i = 0; i < line.length(); i++) {
					// Read Character at Position i in current line
					char c = line.charAt(i);
					// System.out.println(c); // TODO Delete this line, when it
					// is not needed anymore
					// Put Character on Screen
					term.bufferChar(i, lineNumber, ColoredChar.create(c));
				}
				// Fill rest of Line with Whitespaces
				while (i < world.width()) {
					term.bufferChar(i, lineNumber, ColoredChar.create(' '));
					i++;
				}
				// Read next line
				line = br.readLine();
				// Increment Linecounter
				lineNumber++;
			}
			// Fill rest of Lines with Whitespaces
			while (lineNumber < world.height()) {
				// Put Whitespaces in Columns
				for (int i = 0; i < world.width(); i++) {
					term.bufferChar(i, lineNumber, ColoredChar.create(' '));
				}
				// Go to next Line
				lineNumber++;
			}
			// Redraw Window
			term.refreshScreen();

		} catch (IOException e) { // Something went wrong while reading the File
			System.out.println("!IoException");
			e.printStackTrace();
			System.exit(1);
		}
		// catch (FileNotFoundException ex) { TODO Delete this Block if it is
		// not needed anymore
		// System.out.println("file not found");
		// System.exit(1);
		// }
	}

	/**
	 * Puts a fullscreen Message on Screen. Deletes all former content.
	 * 
	 * @param line
	 *            Line to be put on Screen
	 */
	public static void printLine(String line, TiledTermPanel term, World world) {
		term.clearBuffer();
		int i;
		for (i = 0; i < line.length(); i++) {
			term.bufferChar(i, 0, ColoredChar.create(line.charAt(i)));
		}
		while (i < world.width()) {
			term.bufferChar(i, 0, ColoredChar.create(' '));
			i++;
		}
		int lineNumber = 1;
		while (lineNumber < world.height()) {
			i = 0;
			while (i < world.width()) {
				term.bufferChar(i, lineNumber, ColoredChar.create(' '));
				i++;
			}
			lineNumber++;

		}
		term.refreshScreen();
	}

	/**
	 * Prints a Block of Lines on Screen. Deletes all former content.
	 * 
	 * @param line
	 *            Array of Lines
	 */
	public static void printBlock(ArrayList<String> lines, TiledTermPanel term,
			World world) {
		term.clearBuffer();
		int lineNumber = 0;
		for (lineNumber = 0; lineNumber < lines.size(); lineNumber++) {
			String currentLine = lines.get(lineNumber);
			int i;
			for (i = 0; i < currentLine.length(); i++) {
				term.bufferChar(i, lineNumber,
						ColoredChar.create(currentLine.charAt(i)));
			}
			while (i < world.width()) {
				term.bufferChar(i, lineNumber, ColoredChar.create(' '));
				i++;
			}
		}
		while (lineNumber < world.height()) {
			for (int i = 0; i < world.width(); i++) {
				term.bufferChar(i, lineNumber, ColoredChar.create(' '));
			}
			lineNumber++;
		}
		term.refreshScreen();
	}

	/**
	 * Redraws Content of Window
	 * 
	 * @param term
	 *            Canvas for Drawing
	 * @param world
	 *            Used World
	 */
	public static void redrawMap() {					
		lastTerminal.clearBuffer();
		/*for (int x = 0; x < lastWorld.width(); x++) {						//Karte wird nicht im Terminal geladen 
			for (int y = 0; y < lastWorld.height(); y++) {
				// lastTerminal.bufferChar(x+11,y,lastWorld.look(x,y));
				lastTerminal.bufferChar(x, y, lastWorld.look(x, y));
			}
		}*/
		lastTerminal.bufferCameras();
		lastTerminal.refreshScreen();
	}

	/**
	 * Redraw map, put Statusline below Map
	 * 
	 * @param statusLine
	 *            Statusline to be printed
	 */
	public static void redrawMap(String statusLine) {
		redrawMap();
		for (int x = 0; x < statusLine.length(); x++) {
			lastTerminal.bufferChar(x, lastWorld.height(),
					ColoredChar.create(statusLine.charAt(x)));
		}
		lastTerminal.refreshScreen();
	}

	/**
	 * Puts Text in Eventline. redrawEventLine does not delete content on its own!
	 *
	 * @param eventLine Line to be printed.
	 */
	public static void redrawEventLine(String eventLine) {
		int x;
		for (x = 0; x < eventLine.length(); x++) {
			lastTerminal.bufferChar(x, lastWorld.height() + 1,
					ColoredChar.create(eventLine.charAt(x)));
		}
		
		while (x < lastWorld.width()) {
			lastTerminal.bufferChar(x, lastWorld.height() + 1,
					ColoredChar.create(' '));
			x++;
		}

		if (eventLog.size() == 25) {
			eventLog.remove(0);
		}
		eventLog.add(eventLine);
		lastTerminal.refreshScreen();
	}

	public static void putText(ArrayList<String> lines) {
		Screen.printBlock(lines, Screen.lastTerminal, Screen.lastWorld);
	}

	public static void showEventLog() {
		Screen.putText(Screen.eventLog);
	}

	public static void anyKey() {
		try {
			lastTerminal.getKey();
		} catch (InterruptedException e) {
			System.out.println("!InterruptedException e");
			e.printStackTrace();
		}
	}
}

