public class ConsumableItem extends Item{
    private int amountOfUses;

    public ConsumableItem(String name, String description, Rarity rarity, String effect, int amountOfUses){
        super(name, description, rarity, effect);
        this.amountOfUses = amountOfUses;
    }

    /**
     * @return Amount of uses before item is removed.
     */
    public int getAmountOfUses() { return amountOfUses; }

    /**
     * Uses the item and uses the addEffect methods written in character.
     * If there is no valid effect, display the error message.
     * Switch-case statement to look for valid effects to modify, if found, update the variable.
     * Finally, reduce the amount of uses by 1, as there are only a limited amount of uses for consumables.
     */
    @Override
    public void UseItem(){
        // Informs the user that item has been used.
        System.out.println("You used the " + getName() + ".");

        applyEffect();

        // Decrement the amount of uses of item remaining.
        amountOfUses--;
        // amountOfUses;
    }
}
