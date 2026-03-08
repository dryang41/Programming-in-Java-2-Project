import java.io.Serializable;

/**
 * Abstract item class for items. This just lays the groundwork for how items should behave and interact with the player.
 * It is abstract to not have any direct instantiation of Item class.
 */
public abstract class Item implements Comparable<Item>{
    // Basic variables.
    private String name;
    private String description;
    private Rarity rarity;
    private String effect;
    private int effectiveness;

    // Item interacts with the character class. Protected to allow subclasses to interact with character.
    protected Character character = Character.getInstance();

    // Constructor
    public Item(String name, String description, Rarity rarity, String effect) {
        this.name = name;
        this.description = description;
        this.rarity = rarity;
        this.effect = effect;

        /* Sets how much the effect is modified by how rare it is.
        If common, effect is modified by 1.
        If Rare, effect is modified by 2.
        If Ultrarare, effect is modified by 3.
         */
        if (rarity == Rarity.Common) {
            effectiveness = 1;
        } else if (rarity == Rarity.Rare) {
            effectiveness = 2;
        } else {
            effectiveness = 3;
        }
    }

    /**
     * @return name of item.
     */
    public String getName() {
        return name;
    }

    /**
     * @return description of item.
     */
    public String getDescription() {
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
    public String getEffect() {
        return effect;
    }

    /**
     * @return Effectiveness of item.
     */
    public int getEffectiveness() {
        return effectiveness;
    }

    /**
     * Overridden toString method.
     */
    @Override
    public String toString() { return getName() + " - " + getDescription() + " Rarity: " + getRarity(); }

    /**
     * Sorts list of items by name alphabetically, i.e. starts by A, ends with Z.
     * @param i the object to be compared.
     */
    @Override
    public int compareTo(Item i) {
        return this.getName().compareTo(i.getName());
    }

    /**
     * A method designed for subclasses to override and properly use.
     * It's used for the player to use/equip an item.
     */
    public abstract void UseItem();

    /**
     * Switch case statement to find the proper stat to add.
     * When it finds one, it uses the character's built-in addEffect method.
     * If none is found, display error message.
     */
    protected void applyEffect() {
        switch (getEffect()) {
            case ("health"):
                character.addHealth(getEffectiveness());
                break;
            case ("hunger"):
                character.addHunger(getEffectiveness());
                break;
            case ("thirst"):
                character.addThirst(getEffectiveness());
                break;
            case ("warmth"):
                character.addWarmth(getEffectiveness());
                break;
            default:
                System.err.println("Invalid item effect.");
                break;
        }
    }
}
