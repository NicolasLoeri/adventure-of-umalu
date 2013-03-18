/*
 * Inventory.java
 * Copyright (C) 2013 dirk <dirk@Valinor.local>
 *
 * Distributed under terms of the MIT license.
 */
package rogue.creature.util;

import java.util.ArrayList;
import rogue.level.Screen;
import rogue.creature.util.NotEnoughSpaceException;
import jade.ui.TiledTermPanel;
import jade.ui.Terminal;

/**
 * Represents an Inventory for player.
 */
public class Inventory {
	private ArrayList<Item> backpackSpaces;
	private int maximumItems;
	private Item[] wornItems;
	private int gold;

	/**
	 * Creates a new Inventory Object.
	 *
	 * @param maximumItems Amount of Items, a character can carry
	 * @param gold Amount of Gold character initially starts with
	 */
	public Inventory(int maximumItems, int gold) {
		this.gold = gold;
		this.maximumItems = maximumItems;
		backpackSpaces = new ArrayList<Item>(maximumItems);
		//Item test = 
		try {
			this.addItem(new Item("TestGegenstand",0,Item.ITEMTYPE_SWORD,5,10));
		} catch (NotEnoughSpaceException e) {
			System.out.println("Nicht genug Platz");
			e.printStackTrace();
		}
		wornItems = new Item[3];
		Item standardHelmet = new Item("Standard Helm",0,Item.ITEMTYPE_HEAD,0,0, true);
		Item standardSword = new Item("Standard Schwert",0,Item.ITEMTYPE_SWORD,0,0, true);
		wornItems[Item.ITEMTYPE_HEAD] = standardHelmet;
		wornItems[Item.ITEMTYPE_SWORD] = standardSword;
	}
	
	/**
	 * Adds an Item if Inventory is not full.
	 * 
	 * @param item New Item in Inventory
	 * @return Returns true, if adding Item is successfull, otherwise false (e.g. Inventory is full)
	 * @throws NotEnoughSpaceException Players inventory is already full
	 */
	public boolean addItem(Item item) throws NotEnoughSpaceException {
		if (backpackSpaces.size() < maximumItems) {
			backpackSpaces.add(item);
			return true;
		} else {
			throw new NotEnoughSpaceException();
		}
	}

	/**
	 * Deletes an item from Inventory
	 *
	 * @param item Item to be deleted from Inventory
	 */
	public void removeItem(Item item) {
		backpackSpaces.remove(item);
	}

	/**
	 * Deletes an item from Inventory at given index
	 *
	 * @param index Index from which item should be deleted
	 */
	public void removeItem(int index) {
		backpackSpaces.remove(index);
	}

	/**
	 * Sells Item at Index (adding GoldValue to amount of Gold to players inventory)
	 *
	 * @param index Index of Item to be sold
	 */
	public void sellItem(int index) {
		// Riemove Item at index
		Item soldItem = backpackSpaces.remove(index);
		this.increaseGold(soldItem.getGoldValue());
	}

	/**
	 * Adds an Item to inventory, if he has enough gold and at least on free inventory space left
	 *
	 * @param item Item to be bought
	 * @throws NotEnoughSpaceException Inventory has no free Spaces left
	 * @throws NotEnoughGoldException Player has not collected Gold for this Action
	 */
	public void buyItem(Item item) throws NotEnoughSpaceException, NotEnoughGoldException {
		if ((backpackSpaces.size()+1 <= maximumItems) && (item.getGoldValue() <= gold)) {
			decreaseGold(item.getGoldValue());
			this.addItem(item);
		} else if (backpackSpaces.size()+1 > maximumItems) {
			// User cannot carry more Items. Throw an Exception
			throw new NotEnoughSpaceException();
		} else {
			throw new NotEnoughGoldException();
		}
	}

	/**
	 * Adds amount to Players gold stash
	 *
	 * @param amount Amount of Gold to add to players gold stash
	 */
	public void increaseGold(int amount) {
		gold += amount;
	}

	/**
	 * Takes away amount of Players gold
	 *
	 * @param amount Amount of Gold, Player looses
	 * @return true if amount could be decreased, false if an error occured (e.g. not enough gold)
	 * @throws NotEnoughGoldException User has not enough Gold for this Operation
	 */
	public boolean decreaseGold(int amount) throws NotEnoughGoldException {
		if (amount > gold) {
			gold -= amount;
			return true;
		} else {
			throw new NotEnoughGoldException();
		}
	}

	/**
	 * Returns the List of all Items in Inventory
	 *
	 * @return list of all items in inventory
	 */
	public ArrayList<Item> listBackpack() {
		return backpackSpaces;
	}

	/**
	 * Returns the sum of all Bonusdamage
	 *
	 * @return Bonusdamage for user
	 */
	public int getBonusDamageOfWornItems() {
		int sum = 0;
		for (int i=0;i<wornItems.length;i++) {
			sum += wornItems[i].getDamageBonus();
		}
		return sum;
	}

	/**
	 * Returns sum of all Bonushealth
	 *
	 * @return Bonushealth for user
	 */
	public int getHealthBonus() {
		int sum = 0;
		for (int i=0;i<wornItems.length;i++) {
			sum += wornItems[i].getHealthBonus();
		}
		return sum;
	}

	/**
	 * Returns all Items currently worn by user
	 *
	 * @return List of all worn Items by user
	 */
	public Item[] getWornItems() {
		return wornItems;
	}

	/**
	 * Calls showItem Method for worn Item
	 *
	 * @param index Index of Item to be shown
	 * @param term Used Terminal
	 */
	public void showWorn(int index, Terminal term) {
		wornItems[index].showItem(term, this);
	}
	
	/**
	 * Calls showItem for Item in backpack
	 *
	 * @param place Index of Item to be shown
	 * @param term Used Terminal
	 */
	public void showInfo(int place, Terminal term) {
		backpackSpaces.get(place).showItem(term, this);
	}

	/**
	 * Starts to wear an unequipped Item
	 *
	 * @param item Item to equip
	 */
	public void equip(Item item) {
		item.setEquipped(true);
		Item buffer = wornItems[item.getItemType()];
		wornItems[item.getItemType()] = item;
		this.removeItem(item);
		buffer.setEquipped(false);
		try {
			this.addItem(buffer);
		} catch (NotEnoughSpaceException e) {
			System.out.println("!Error Exception:");
			e.printStackTrace();
			System.out.println("Something is strange here...");
		}
	}
}

