/**
 * Abstract item class for items. This just lays the groundwork for how items should behave and interact with the player.
 * It is abstract to not have any direct instantiation of Item class.
 */
public abstract class Item {
    // Basic variables.
    private String name;
    private String description;
    private Rarity rarity;

    // Constructor
    public Item(String name, String description, Rarity rarity){
        this.name = name;
        this.description = description;
        this.rarity = rarity;
    }

    /**
     * @return name of item.
     */
    public String getName(){
        return name;
    }

    /**
     * @return description of item.
     */
    public String getDescription(){
        return description;
    }

    /**
     * @return rarity of item.
     */
    public Rarity getRarity() {
        return rarity;
    }

    /**
     * A method designed for subclasses to override and properly use.
     * It's used for the player to use/equip an item.
     */
    public abstract void UseItem();
}
