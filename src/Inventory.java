import org.apache.derby.iapi.store.access.conglomerate.Sort;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The class where items are stored and the player can grab them.
 * Singleton class so there is only one inventory for the one character.
 */
public class Inventory{
    /*
    The variables for the Inventory class.
    inventorySlots keeps track of the maximum size of itemsStored.
    itemStored is a List object that stores all items.
     */
    private int inventorySlots;
    private List<Item> itemsStored = new ArrayList<Item>(inventorySlots);

    // Singleton pattern
    private static final Inventory instance = new Inventory();
    public static Inventory getInstance(){ return instance; }

    // Getters
    public int getInventorySlots(){ return inventorySlots; }
    public List<Item> getItems() { return itemsStored; }

    // Setter
    public void addInventorySlots(int amount) { inventorySlots += amount; }

    /**
     * Grabs all items from the List object and puts them into a StringBuilder.
     * StringBuilder so each item can be appended on to the end of the String and a new line character is used for formatting.
     * @return All items formatted neatly.
     */
    @Override
    public String toString() {
        StringBuilder itemsInInventory = new StringBuilder();
        int counter = 1;

        for (Item i : getItems()) {
            itemsInInventory.append("Slot " + counter + ": " + i + "\n");
            counter++;
        }

        return itemsInInventory.toString();
    }

    /**
     * Method for adding an item to the List object.
     * @param i The item that is to be added.
     * @return Whether the item was successfully added.
     */
    public boolean addItem(Item i) {
        boolean itemAdded = false;

        // First checks if there is room for another item.
        if (itemsStored.size() < inventorySlots) {
            itemsStored.add(i);
            System.out.println(i.getName() + " added to your inventory.");
            itemAdded = true;
        }
        else {
            System.out.println("Your inventory is full! Please remove an item from your inventory first or discard the " + i.getName());
        }

        return itemAdded;
    }

    /**
     * Method for removing an item from List object.
     * @param index The slot that is to be emptied.
     * @return Whether the item was successfully removed.
     * @throws ArrayIndexOutOfBoundsException
     */
    public boolean removeItem(int index) throws ArrayIndexOutOfBoundsException{
        boolean itemRemoved = false;

        // Loop through all items and see which one matches user input
        for (int i = 0; i < itemsStored.size(); i++) {
            if (index == i) {
                // If the item being removes is a water bottle, replace the water bottle with an empty bottle
                if (getItems().get(i).getName().equals("Water Bottle")) {
                    itemsStored.add(new PassiveItem("Empty Bottle", "An empty plastic bottle used for holding water, the label is ripped off.", 1, "thirst"));
                }

                // Remove item and flip return value
                itemsStored.remove(i);
                itemRemoved = true;
            }
        }

        // Inform the player that the slot couldn't be emptied.
        if (itemRemoved == false) {
            System.out.println("That slot doesn't exist! Try Again.");
        }

        return itemRemoved;
    }

    /**
     * Grabs an item from the List and returns a ConsumableItem if the item is a Consumable
     * @param index The slot that is to be grabbed.
     * @return The ConsumableItem to be used by the character.
     */
    public ConsumableItem getConsumable(int index) {
        ConsumableItem item = null;
        // Decrementing parameter index to make it zero-based
        index--;

        // Loop through List and check if item is a consumable and matches index
        for (int i = 0; i < itemsStored.size(); i++) {
            if (index == i) {
                if (getItems().get(i) instanceof ConsumableItem) {
                    item = (ConsumableItem) getItems().get(i);
                }
            }
        }

        return item;
    }

    /**
     * Grabs an item from the List and returns a PassiveItem if the item is a Passive
     * @param index The slot that is to be grabbed.
     * @return The PassiveItem to be used by the character.
     */
    public PassiveItem getPassive(int index) {
        PassiveItem item = null;
        // Decrementing parameter index to make it zero-based
        index--;

        // Loop through List and check if item is a passive and matches index
        for (int i = 0; i < itemsStored.size(); i++) {
            if (index == i) {
                if (getItems().get(i) instanceof PassiveItem) {
                    item = (PassiveItem) getItems().get(i);
                }
            }
        }

        return item;
    }

    /**
     * Sorts by the rarity of each item
     */
    public void sortByRarity() {
        Collections.sort(itemsStored, new SortByRarity());
    }

    /**
     * Sorts by the name of each item
     */
    public void sortByName() {
        Collections.sort(getItems());
    }
}
