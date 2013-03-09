package rogue.creature;

import jade.core.Actor;
import java.util.Collection;
import jade.fov.RayCaster;
import jade.fov.ViewField;
import jade.ui.Camera;
import jade.ui.Terminal;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import jade.util.datatype.Direction;
import java.util.Random;


public class Player extends Creature implements Camera {
	private Terminal term;
	private ViewField fov;
	private static final int maxHitpoints =15;
	private int strength;

	/**
	 * Creates a new Player Object
	 *
	 * @param term Currently used Terminalobject
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
			switch(key) {
			    case 'q': // User wants to quit
				expire(); // Leave let player die, so this application quits
				break;
			    default: // User pressed something else
				Direction dir = Direction.keyToDir(key); // Get direction
				// Something useful pressed?
				if(dir != null){ // Yes
					// Get list of all monsters on target Coordinates
					Collection<Monster> actorlist = world().getActorsAt(Monster.class, x()+dir.dx(), y()+dir.dy());
					// Is there a monster on TargetL
					if(!actorlist.isEmpty()){ // Yes
						// Fight first monster on coordinate.
						fight((Monster) actorlist.toArray()[0]);
					} else {
						// No monster there
						move(dir);
					
					}
					break;
				}
			} 
		} catch(InterruptedException e) { // Something has happened here
			System.out.println("!Interrupted Exception");
			e.printStackTrace();
		}
	}

	@Override
	/**
	 * Get what is visible
	 *
	 * @return A collection of visible Items
	 */
	public Collection<Coordinate> getViewField() {
		return fov.getViewField(world(), pos(), 5);
	}

	/**
	 * Player fights the opponent. Causes random damage between 1 and strength
	 * @param opponent The opponent Monster
	 */
	private void fight(Monster opponent) {
		System.out.println("Du kämpfst gegen " + opponent.name());
		Random random = new Random();
		int damage = random.nextInt(strength)+1;
		opponent.loseHitpoints(damage);
		System.out.println("Du hast "+ damage + "Schaden verursacht");
		System.out.println(opponent.name()+" hat noch " + opponent.hitpoints +" HP");
	}

	/**
	 * Player regains 1 Hitpoint. Method should be used every x rounds in rogue
	 */
	public void regainHitpoint(){
		if(hitpoints<maxHitpoints){
			hitpoints++;
			System.out.println("Du hast einen HP regeneriert, jetzt " + hitpoints+" HP");
		}
	}
}
