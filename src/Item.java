/**
 * Abstract item class for items. This just lays the groundwork for how items should behave and interact with the player.
 * It is abstract to not have any direct instantiation of Item class.
 */
public abstract class Item {
    // Basic variables.
    private String name;
    private String description;
    private Rarity rarity;
    private String effect;
    private int amountModified;

    // Item interacts with the character class. Protected to allow subclasses to interact with character.
    protected Character character = Character.getInstance();

    // Constructor
    public Item(String name, String description, Rarity rarity, String effect){
        this.name = name;
        this.description = description;
        this.rarity = rarity;
        this.effect = effect;

        /* Sets how much the effect is modified by how rare it is.
        If common, effect is modified by 1.
        If Rare, effect is modified by 2.
        If Ultrarare, effect is modified by 3.
         */
        if (rarity == Rarity.Common){ amountModified = 1; }
        else if (rarity == Rarity.Rare){ amountModified = 2;}
        else { amountModified = 3; }
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
     * @return effect of item.
     */
    public String getEffect() { return effect; }

    /**
     * @return amountModified of item.
     */
    public int getAmountModified() { return amountModified; }

    /**
     * A method designed for subclasses to override and properly use.
     * It's used for the player to use/equip an item.
     */
    public abstract void UseItem();
}
