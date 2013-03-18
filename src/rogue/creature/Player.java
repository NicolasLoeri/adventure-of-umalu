package rogue.creature;

import jade.core.Actor;
import java.util.Collection;
import jade.fov.RayCaster;
import jade.fov.ViewField;
import jade.gen.Generator;
import jade.gen.map.World1;
import jade.ui.Camera;
import jade.ui.Terminal;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import jade.util.datatype.Direction;
import rogue.system.HelpScreen;
import java.util.logging.Level;
import java.util.logging.Logger;
import rogue.level.Screen;
import rogue.creature.util.Inventory;
import rogue.creature.util.Item;
import rogue.creature.util.NotEnoughGoldException;
import rogue.creature.util.NotEnoughSpaceException;
import rogue.level.Screen;
import java.util.Random;
import java.lang.InterruptedException;
import jade.core.World;
import java.util.ArrayList;

/**
 * Represents Player
 */
public class Player extends Creature implements Camera {

	private Terminal term;
	private ViewField fov;
	private static final int maxHitpoints = 15;
	private int strength;
	private String name;
	private Inventory inventory;

	public Boolean worldchange = false;   // standardmäßig ist keine Mapänderung erfolgt
	/**
	 * Creates a new Player Object
	 * 
	 * @param term
	 *            Currently used Terminalobject
	 */
	public Player(Terminal term) {
		// Put Charactersymbol on Screen
		super(ColoredChar.create('@'));
		// Save Terminal
		this.term = term;
		fov = new RayCaster();
		// Initialise Hitpoints on Max
		hitpoints = maxHitpoints;
		strength = 5;
		inventory = new Inventory(5,50);
	}

	/**
	 * Sets Charactername. Should be only called on character Creation.
	 * 
	 * @param name
	 *            New Name of Character
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns Charactername.
	 * 
	 * @return Name of Character
	 */
	public String getName() {
		return name;
	}

	@Override
	/**
	 * Ask Player to do some action (passing him the baton). Reads input and moves Character accordingly.
	 */
	public void act() {
		try {
			// Get pressed char
			char key;
			key = term.getKey();
			switch (key) {
			case 'b': // User wants to quit//beenden später auf esc 
				confirmQuit(); // Leave let player die, so this application quits
				break;
			case 'i': // Show Inventory
				showInventoryScreen();
				break;
			case 'o':
				HelpScreen.printMainHelpScreen();
				break;
			case 'x': // TODO Change this key
				Screen.showEventLog();
				term.getKey();
				Screen.redrawMap("HP: "+this.getHitpoints());
				break;
			default: // User pressed something else
				Direction dir = Direction.keyToDir(key); // Get direction
				
				// Something useful pressed?
				if (dir != null) { // Yes
					// Get list of all monsters on target Coordinates
					Collection<Monster> actorlist = world().getActorsAt(Monster.class, x() + dir.dx(), y() + dir.dy());
					// Is there a monster on TargetL
					if (!actorlist.isEmpty()) { // Yes
						// Fight first monster on coordinate.
						fight((Monster) actorlist.toArray()[0]);
					} else {
						if (world().tileAt(x() + dir.dx(), y() + dir.dy()) == ColoredChar.create('\u00a9')) {  
							Screen.redrawEventLine("M\u00f6chtest du diesen Raum verlassen? Dr\u00fccke j für Ja, ansonsten verweilst du hier.");//Stellt fest, dass eine Tür gefunden wurde und somit eine Mapänderung erfolgt
							for(Coordinate coord: getViewField()){
								world().viewable(coord.x(), coord.y());}
							if (term.getKey()=='j'){
								worldchange= true;
								move(dir);}
							else{
								move(0,0); 
								}
							
						
						} else {// No monster there
							for(Coordinate coord: getViewField()){				//macht alles sichtbar, was im Field of View ist
								world().viewable(coord.x(), coord.y());}
							
							move(dir);
							
							
						
							break;
						}
					}

				}
			}
		} catch (InterruptedException e) { // Something has happened here
			System.out.println("!Interrupted Exception");
			e.printStackTrace();
		}
	}

	public void confirmQuit() {
		Screen.redrawEventLine("Sicher dass Adventures in Umalu beendet werden soll? <J>a/<N>ein");
		try {
			if (term.getKey() == 'j') {
				expire();
			}
		} catch (InterruptedException e) {
			System.out.println("!Interrupted Exception");
			e.printStackTrace();
		}
	}

	//@Override
	/**
	 * Get what is visible
	 *
	 * @return A collection of visible Items
	 */
	public Collection<Coordinate> getViewField() {
		return fov.getViewFieldplayer(world(), pos().x(),pos().y(), 2); //hab mal den Sichtbarkeitsradius verkleinert, damit es spannender ist
	} 

    /**
     * Player fights the opponent. Causes random damage between 1 and strength	 *
     * @param opponent
     *            The opponent Monster
     */
    // TODO Clean up Messages in Console, to use just a single line
    private void fight(Monster opponent) {
        System.out.println("Du kämpfst gegen " + opponent.name());
        // Get a randomizer
        Random random = new Random();
        // Get random Damage for Attack
        int damage = random.nextInt(strength) + 1;
        // Print result

        System.out.println("Du hast " + damage + " Schaden verursacht");
        System.out.println(opponent.name() + " hat noch " + opponent.hitpoints
                + " HP");
        Screen.redrawEventLine("Du verursachst " + damage + " Schaden");
        // Do Damage to Opponent
        boolean opponentDied = opponent.loseHitpoints(damage);
        
        try {
            if (opponentDied) {
                //wait for key to continue on Status message
                term.getKey();
                randomlyDropItem(opponent);
            }


            term.getKey();

        } catch (InterruptedException e) {
            System.out.println("!InterruptedException");
            e.printStackTrace();
        }
    }

    /**
     * Player regains 1 Hitpoint. Method should be used every x rounds in rogue
     */
    public void regainHitpoint() {
        // Is there something to heal
        if (hitpoints < maxHitpoints) {
            // Gain Healthpoint back
            hitpoints++;
            // Print message to Console
            System.out.println("Du hast einen HP regeneriert, jetzt " + hitpoints + " HP");
            // Print Eventline
            Screen.redrawEventLine("Du regenerierst einen HP.");
            try {
                // Wait for pressed Key
                term.getKey();

            } catch (InterruptedException e) {
                System.out.println("!IOException");
                e.printStackTrace();
            }
        }
    }

    /**
     *
     */
    public int getHitpoints() {
        return hitpoints;
    }

    public int getMaxHitpoints() {
        return maxHitpoints;
    }

    /**
     * Creates and prints an Inventory Screen
     */
    public void showInventoryScreen() {
        boolean loop = true;
        // Inventar geschlossen?
        while (loop) { // Nein.
            // Erstelle eine ArrayList von Strings um dort unser Inventarinterface zu puffern
            ArrayList<String> lines = new ArrayList<String>();
            // Erstelle eine Titelzeile
            // TODO In die Mitte Verschieben
            lines.add("Inventar");
            // Zeige was der Nutzer gerade angelegt hat
            lines.add("Du traegst: ");
            // Lade die Liste
            Item[] wornItems = inventory.getWornItems();
            // Generiere den Output fuer den aktuellen Helm
            lines.add("<K>opf: " + wornItems[Item.ITEMTYPE_HEAD].getName() + " [+DMG: " + wornItems[Item.ITEMTYPE_HEAD].getDamageBonus() + ", +HP: " + wornItems[Item.ITEMTYPE_HEAD].getHealthBonus() + "]");
            // Generiere den Output fuer das aktuelle Schwert
            lines.add("<S>chwert: " + wornItems[Item.ITEMTYPE_SWORD].getName() + " [+DMG: " + wornItems[Item.ITEMTYPE_SWORD].getDamageBonus() + ", +HP: " + wornItems[Item.ITEMTYPE_SWORD].getHealthBonus() + "]");
            // TODO Zeige gesamt Bonus an
            // Zeige an, was sonst noch im Inventar liegt, aber nicht angelegt wurde (und somit keinen Bonus bringt)
            ArrayList<Item> backpack = inventory.listBackpack();
            lines.add("Du hast im Rucksack: ");
            for (int i = 0; i < backpack.size(); i++) {
                // Zeige das Item an Stelle an i an
                lines.add("(" + i + ") " + backpack.get(i).getName() + "[+DMG: " + backpack.get(i).getDamageBonus() + ", +HP: " + backpack.get(i).getHealthBonus() + "]");
            }
            // TODO Add lines here.
            Screen.putText(lines);
            try {
                // Erwarte eine Eingabe vom Nutzer.
                char key = term.getKey();
                switch (key) {
                    case 'q':
                        loop = false;
                        break;
                    case 'k':
                        //wornItems[Item.ITEMTYPE_HEAD].showInfo(term,inventory);
                        inventory.showWorn(Item.ITEMTYPE_HEAD, term);
                        break;
                    case 's':
                        //wornItems[Item.ITEMTYPE_SWORD].showInfo();
                        inventory.showWorn(Item.ITEMTYPE_SWORD, term);
                        break;
                    case '0':
                        inventory.showInfo(0, term);
                        break;
                    case '1':
                        inventory.showInfo(1, term);
                        break;
                    case '2':
                        inventory.showInfo(2, term);
                        break;
                    case '3':
                        inventory.showInfo(3, term);
                        break;
                    case '4':
                        inventory.showInfo(4, term);
                        break;
                    case 'o':
                        HelpScreen.printInventoryHelpScreen();
                        break;

                }
            } catch (InterruptedException e) {
                System.out.println("!Exeception");
                e.printStackTrace();
            }
        }
        // Inventar verlassen, zeichne wieder die Karte.
        Screen.redrawMap();
    }

    private void randomlyDropItem(Monster opponent) {
        Random random = new Random();
        random.nextInt(strength);
        //This Item drops;
        Item item = null;
        try {
            switch (opponent.typenumber) {

                case 1: {
                    //Rat, doesnt drop Weapons according to balance
                    System.out.println("Ratte lässt nichts fallen");
                    break;
                }
                case 2: {
                    //Fette Nacktschnecke
                    //random Number decides whether an Item drops or not and which one
                    int zufallszahl = random.nextInt(3);


                    if (zufallszahl == 0) {
                        //Axt drops 1/3 of the time
                        item = new Item("Axt", 0, Item.ITEMTYPE_SWORD, 2, 0);
                        inventory.addItem(item);
                        //Status message
                        Screen.redrawEventLine("Du hast eine Axt bekommen, druecke i, um das Inventar zu oeffnen");
                        //Wait for pressed key
                        term.getKey();
                    }

                    break;
                }
                case 3: {
                    //Giftiger Frosch
                    //random Number decides whether an Item drops or not and which one
                    int zufallszahl = random.nextInt(20);

                    if (zufallszahl == 0) {
                        //Langschwert droppt zu 1/20
                        item = new Item("Langschwert", 0, Item.ITEMTYPE_SWORD, 6, 0);
                        inventory.addItem(item);
                        //Status message
                        Screen.redrawEventLine("Du hast ein Langschwert bekommen, druecke i, um das Inventar zu oeffnen");
                        //Wait for pressed key
                        term.getKey();

                    } else if (zufallszahl <= 4) {
                        //Kurzschwert droppt zu 1/5
                        item = new Item("Kurzschwert", 0, Item.ITEMTYPE_SWORD, 3, 0);
                        inventory.addItem(item);
                        //Status message
                        Screen.redrawEventLine("Du hast ein Kurzschwert bekommen, druecke i, um das Inventar zu oeffnen");
                        // Wait for pressed Key
                        term.getKey();
                    }
                    break;

                }
                case 4: {
                    //Zombie
                    //random Number decides whether an Item drops or not and which one
                    int zufallszahl = random.nextInt(20);

                    if (zufallszahl < 5) {
                        //Langschwert droppt zu 1/4
                        item = new Item("Langschwert", 0, Item.ITEMTYPE_SWORD, 6, 0);
                        inventory.addItem(item);
                        //Status message
                        Screen.redrawEventLine("Du hast ein Langschwert bekommen, druecke i, um das Inventar zu oeffnen");
                        //Wait for pressed key
                        term.getKey();

                    } else if (zufallszahl < 9) {
                        //Riesenschwert droppt zu 1/5
                        item = new Item("Riesenschwert", 0, Item.ITEMTYPE_SWORD, 12, 0);
                        inventory.addItem(item);
                        //Status message
                        Screen.redrawEventLine("Du hast ein Riesenschwert bekommen, druecke i, um das Inventar zu oeffnen");
                        // Wait for pressed Key
                        term.getKey();
                    }
                    break;
                }
                case 5: {
                    //Unbeliever
                   //random Number decides whether an Item drops or not and which one
                    int zufallszahl = random.nextInt(12);

                    if (zufallszahl < 3) {
                        //Riesenschwert droppt zu 1/4
                        item = new Item("Riesenschwert", 0, Item.ITEMTYPE_SWORD, 12, 0);
                        inventory.addItem(item);
                        //Status message
                        Screen.redrawEventLine("Du hast ein Riesenschwert bekommen, druecke i, um das Inventar zu oeffnen");
                        //Wait for pressed key
                        term.getKey();

                    } else if (zufallszahl < 5) {
                        //Riesenschwert droppt zu 1/6
                        item = new Item("Großschwert", 0, Item.ITEMTYPE_SWORD, 12, 0);
                        inventory.addItem(item);
                        //Status message
                        Screen.redrawEventLine("Du hast ein Großschwert bekommen, druecke i, um das Inventar zu oeffnen");
                        // Wait for pressed Key
                        term.getKey();
                    }
                    break;
                }
                case 6: {
                    //Orc
                    //TODO Waffen droppen
                    break;
                }
                case 7: {
                    //Shodow
                    //TODO Waffen droppen
                    break;
                }
                case 10: {
                    //Troll
                    //TODO Waffendroppen
                    break;
                }
                case 11: {
                    //Unsichbarer Zombie
                    //droppt nichts
                    break;
                }
                case 12: {
                    //Dummie
                    //TODO Waffen droppen
                    break;
                }
                case 99: {
                    //Dragon
                    //droppt nichts
                }
                default: {
                    break;
                }
            }
        } catch (NotEnoughSpaceException ex) {
            try {
                //Status message
                Screen.redrawEventLine("Du konntest leider ein" + item.getName() + " nicht ins Inventar aufnehmen, da es voll war");
                //Wait for pressed key
                term.getKey();
            } catch (InterruptedException e) {
                System.out.println("!IOException");
                e.printStackTrace();
            }

        } catch (InterruptedException e) {
            System.out.println("!IOException");
            e.printStackTrace();
        }

    }
}


