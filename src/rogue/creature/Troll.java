/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rogue.creature;

import jade.ui.Terminal;
import jade.util.Dice;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Direction;
import java.util.Arrays;
import java.util.Random;

import rogue.level.Screen;

/**
 *
 * A Troll is a stupid Monster. He doesnt move at al, but ist very strong, and attcs you, when you are near him
 */
public class Troll extends Monster{

    public Troll( Terminal term, int level) {
        super(ColoredChar.create('T'),"Troll",level*20, level*3, term);
        // special Monster get Typenumbers from 10 onwards
        typenumber = 10;
    }


    @Override

    /*
     * A troll just checks, whether he can hit the player or not
     */
    public void act() {
        for (Direction dir : Arrays.asList(Direction.values())) {
            Player player = world().getActorAt(Player.class, x() + dir.dx(), y() + dir.dy());
            if (player != null) {
                fight(player);
                break;

            }

        }


    }


	@Override
	public void fight(Player opponent) {
		// TODO Auto-generated method stub {
	        System.out.println("der " + name + "greift dich an");
		// Create Randomizer
	        Random random = new Random();
		// Generate Damage
	        int abzug = random.nextInt(strength)+1;
		// Do Damage to Oppenent
	        opponent.loseHitpoints(abzug);
		// Print Result
	        System.out.println("Du hast "+ abzug + " HP verloren");
	        System.out.println("verbleibende HP: "+ opponent.hitpoints);
		Screen.redrawEventLine(name+" macht "+abzug+" Schaden (Rest: "+opponent.hitpoints+")");
		try {
			term.getKey();
		} catch(InterruptedException e) {
			System.out.println("!InterruptedException");
			e.printStackTrace();
		}
	    }

	}


