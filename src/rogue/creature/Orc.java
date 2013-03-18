/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rogue.creature;

import jade.fov.RayCaster;
import jade.path.*;
import jade.ui.Terminal;
import jade.util.Dice;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import jade.util.datatype.Direction;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import rogue.level.Screen;

/**
 *
 * An Orc ist a weak Monster
 * he moves randomly like the Dragon and hit the Player if he can
 */
public class Orc extends Monster {

    PathFinder pathfinder = new AStar();
    RayCaster fov;
    int attackRadius;

    public Orc(Terminal term) {
        super(ColoredChar.create('O', new Color(0,100,0)), "Orc", 25, 8, term);
        fov = new RayCaster();
        attackRadius = 5;
        //Typenumber 6, since cat. 6
        typenumber = 6;
    }

    @Override
    public void act() {
        boolean actionOver = false;

        for (Direction dir : Arrays.asList(Direction.values())) {
            Player player = world().getActorAt(Player.class, x() + dir.dx(), y() + dir.dy());
            if (player != null) {
                fight(player);

                actionOver = true;
                break;

            }

        }

        if (!actionOver) {
            Collection<Coordinate> viewField = fov.getViewField(this.world(), this.pos().x(), this.pos().y(), attackRadius);
            for (Coordinate coordinate : viewField) {
                if (this.world().getActorAt(Player.class, coordinate) != null) {
                    Direction dir = this.pos().directionTo(pathfinder.getPath(this.world(), this.pos(), coordinate).get(0));
                    move(dir);
                    actionOver = true;
                    break;
                }
            }

            if (!actionOver) {


                move(Dice.global.choose(Arrays.asList(Direction.values())));
            }
        }
    }

	@Override
	public void fight(Player opponent) {
		// TODO Auto-generated method stub {
	        System.out.println("Der " + name + " greift dich an");
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
            	case 0:System.out.println("Seine rießen Faust trifft dein Gesicht.");break;
            	case 1:System.out.println("Das Ungetüm rammt dich um.");break;//optional hier alles sichtbare l�schen
            	case 2:System.out.println("Er schleudert dich durch die Luft.");break;
            	case 3:System.out.println("Sein Gestank lähmt dich.");break;
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



