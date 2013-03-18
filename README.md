Adventures in Umalu
========

Rogue-like game, written in Java. Heute nur ein Prototyp, morgen schon ein Drachentöter.

Uses Jaderogue Library by Jeffrey Lund (http://code.google.com/p/jaderogue/).

Adventures in Umalu is Developed by
* Dirk Braun
* Paul Kermas
* Nicolas Loerbroks
* Markus Penner
* Christoph Spenke

at Softwarepraktikum Course at Freie Universität Berlin.

Adventures in Umalu is currently only developed in German.

Eine schnelle Einführung in Adventures in Umalu
===
Zunächst die Dateien aus diesem Repository clonen. Wir empfehlen für den Start eclipse zu verwenden:

1. Unter File>Import… wählen
2. Ein existierendes Projekt zum Workspace hinzufügen
3. Im obersten Feld den Projektordner auswählen
4. Project>Run auswählen. Als Java Application laufen lassen.

Die main Funktion ist in rogue/Rogue.java definiert.

Wer aus der Konsole starten will, wechselt in den src Ordner von adventure-of-umalu, und gibt ein
1. javac rogue/Rogue.java
2. java rogue/Rogue

Beim zweiten Befehl darauf achten, dass am Ende des Befehls NICHT .java steht, da die JVM die Datei sonst nicht finden kann!

Tastenkürzel
===
* WASD oder vim-like-Steuerung zur Bewegung, q,e,y,c fuer Diagonal laufen
* i startet das Inventarinterface (mit q verlassen, Item auswählen, mit s anlegen)
* x zeigt die letzten Eventnachrichten an
* o zeigt den Hilfebildschirm an (hier gibt es auch weiterführende Informationen)
