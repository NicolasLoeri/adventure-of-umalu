/*
 * HelpScreen.java
 * Copyright (C) 2013 dirk <dirk@Valinor.local>
 *
 * Distributed under terms of the MIT license.
 */
package rogue.system;

import rogue.level.Screen;
import java.util.ArrayList;
public class HelpScreen {
	public static void printMainHelpScreen() {
		ArrayList<String> lines = new ArrayList<String>();
		lines.add("Adventures in Umalu - Hilfe");
		lines.add("");
		lines.add("-- Steuerung --");
		lines.add("<w>,<k> - Nach Norden laufen");
		lines.add("<a>,<h> - Nach Westen laufen");
		lines.add("<s>,<j> - Nach Sueden laufen");
		lines.add("<d>,<l> - Nach Osten  laufen");
		lines.add("");
		lines.add("-- Diagonal-Steuererung");
		lines.add("<q> - Nach Nordwesten ("+'\u2196'+ ") laufen");
		lines.add("<e> - Nach Nordosten  ("+'\u2197'+ ") laufen");
		lines.add("<c> - Nach Suedosten  ("+'\u2198'+ ") laufen");
		lines.add("<y> - Nach Suedwesten ("+'\u2199'+ ") laufen");
    	lines.add("");
		lines.add("");
		lines.add("-- Weiteres Interface --");
		lines.add("<i> - Inventar anzeigen");
		lines.add("<o> - Diese Hilfe anzeigen");

		lines.add("<x> - Eventlog anzeigen");
		lines.add("<b> - Adventures of Umalu beenden");
		lines.add("");
		lines.add("+++ Beliebige Taste zum Fortfahren druecken +++");
		Screen.putText(lines);
		Screen.anyKey();
	}

	public static void printInventoryHelpScreen() {
		ArrayList<String> lines = new ArrayList<String>();
		lines.add("Adventures in Umalu - Das Inventar");
		lines.add("");
		lines.add("<k>,<s> Angelegten Gegenstand naeher ansehen");
		lines.add("<0>-<4> Gegenstand im Rucksack naher ansehen");
		lines.add("<q> Inventar verlassen");
		lines.add("");
		lines.add("+++ Beliebige Taste zum Fortfahren druecken +++");
		Screen.putText(lines);
		Screen.anyKey();
	}

	public static void printItemHelpScreen() {
		ArrayList<String> lines = new ArrayList<String>();
		lines.add("Adventures in Umalu - Die Gegenstaende");
		lines.add("");
		lines.add("<s> Gegenstand anlegen (sofern er das nicht ist)");
		lines.add("<d> Gegenstand zerstoeren (solange er nicht angelegt ist)");
		lines.add("<q> Gegenstandsansicht verlassen");
		lines.add("");
		lines.add("+++ Beliebige Taste zum Fortfahren druecken +++");
		Screen.putText(lines);
		Screen.anyKey();
	}
}
