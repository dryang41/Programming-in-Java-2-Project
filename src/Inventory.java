import java.util.ArrayList;
import java.util.List;

/**
 * The class where items are stored and the player can grab them.
 * Singleton class so there is only one inventory for the one character.
 */
public class Inventory {
    /*
    The variables for the Inventory class.
    inventorySlots keeps track of the maximum size of itemsStored.
    itemStored is a List object that stores all items.
    slotsFilled is just the size of itemsStored, it's only there to have itemsStored.size() everywhere.
     */
    private int inventorySlots;
    private List<Item> itemsStored = new ArrayList<Item>(inventorySlots);
    private int slotsFilled = itemsStored.size();

    // Singleton pattern
    private static final Inventory instance = new Inventory();
    public static Inventory getInstance(){ return instance; }

    // Getters
    public int getInventorySlots(){ return inventorySlots; }
    public List<Item> getItems() { return itemsStored; }

    // Setter
    public void setInventorySlots(int amount) { inventorySlots = amount; }

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
        if (slotsFilled < inventorySlots) {
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
        // Decrementing parameter index to make it zero-based
        index--;

        for (int i = 0; i < slotsFilled; i++) {
            if (index == i) {
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
     * Method for discarding an item. The only difference between removeItem and discardItem is the print statement.
     * @param index The slot that is to be emptied.
     * @return Whether the item was successfully removed.
     * @throws ArrayIndexOutOfBoundsException
     */
    public boolean discardItem(int index) throws ArrayIndexOutOfBoundsException{
        boolean itemRemoved = false;
        // Decrementing parameter index to make it zero-based
        index--;

        for (int i = 0; i < slotsFilled; i++) {
            if (index == i) {
                System.out.println(getItems().get(i).getName() + " removed from your inventory.");
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
     * Grabs an item from the inventory.
     * @param index The slot where the item is grabbed.
     * @return The item in the slot.
     */
    public Item getItem(int index) {
        Item item = null;
        // Decrementing parameter index to make it zero-based
        index--;

        for (int i = 0; i < slotsFilled; i++) {
            if (index == i) {
                item = getItems().get(i);
            }
        }

        return item;
    }

    public static void main(String[] args) {
        Inventory inventory = getInstance();
        inventory.setInventorySlots(2);
        Item medkit = new ConsumableItem("asd", "asd", Rarity.Common, "adwa", 67);
        System.out.println(inventory.addItem(medkit));

        System.out.println(inventory.addItem(medkit));
        System.out.println(inventory.removeItem(2));
        //System.out.println(inventory);
    }
}
