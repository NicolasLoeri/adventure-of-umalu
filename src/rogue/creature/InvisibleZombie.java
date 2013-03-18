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
 * TODO: th einvisible Zombie should be invisible not just a .
 */
public class InvisibleZombie extends Monster{

    public InvisibleZombie( Terminal term) {
        super(null, "Unsichtbarer Zombie",8 ,2, term);
        //second special Monster, hence Typenumber 11
        typenumber =11;
    }

    @Override
    public void act() {
        boolean fight = false;

        for (Direction dir : Arrays.asList(Direction.values())) {
            Player player = world().getActorAt(Player.class, x() + dir.dx(), y() + dir.dy());
            if (player != null) {
                fight(player);

                fight = true;
                break;

            }

        }

        if (!fight) {

            move(Dice.global.choose(Arrays.asList(Direction.values())));
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

