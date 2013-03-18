package rogue.creature;

import java.util.Arrays;
import jade.util.Dice;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Direction;
import java.util.Random;
import rogue.level.Screen;
import jade.ui.Terminal;
import java.lang.InterruptedException;

public class Dummy extends Monster {

	public Dummy(ColoredChar face, String name, Terminal term) {
		super(face, name, 5, 1, term);
                //third special Monster, hence Typenumber 12
                typenumber = 12;
	}

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
		// TODO Auto-generated method stub
		
	}
}
