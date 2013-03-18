/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rogue.creature;

import java.awt.Color;
import java.util.Arrays;
import java.util.Random;

import jade.util.Dice;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Direction;
import jade.ui.Terminal;
import jade.util.Dice;
import jade.util.datatype.Direction;
import java.util.Arrays;

import rogue.level.Screen;

/**
 * TODO Delete this Class, when it is not used anymore, as instances of Monster do the same thing right now
 * @author alle
 */
public class Dragon extends Monster {
   /*
    * every Dragon has 100 Hitpoints and Strength 5(until now)
    */
    public Dragon(ColoredChar face, String name, Terminal term) {
        super(face,name, 100, 5, term);
        //the Dragon is so special, he is number 99
        typenumber =99;
    }

    public Dragon(Terminal term) {
        super(ColoredChar.create('D',Color.red), "roter Drache", 100, 5, term);
    }
 


    @Override
    public void act() {

		boolean fight = false;
		
		for (Direction dir : Arrays.asList(Direction.values())) {
			Player player = world().getActorAt(Player.class, x() + dir.dx(),
					y() + dir.dy());
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
	        System.out.println("Ra�s al Ghul greift dich an");
		// Create Randomizer
	        Random random = new Random();
		// Generate Damage
	        int abzug = random.nextInt(strength)+1;
		// Do Damage to Oppenent
	        opponent.loseHitpoints(abzug);
		// Print Result
            Random generator = new Random();
            int ran = generator.nextInt( 4 );
            switch(ran){
            	case 0:System.out.println("Er verbrennt dir den Arsch!");break;
            	case 1:System.out.println("Eine Drachenkralle trifft dich.");break;
            	case 2:System.out.println("Iiiihh er hat dich angefurzt.");break;
            	case 3:System.out.println("Aua Drachenzähne sind scharf");break;
	        }
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


