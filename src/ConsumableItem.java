public class ConsumableItem extends Item{
    private int amountOfUses;

    public ConsumableItem(String name, String description, Rarity rarity, String effect, int amountOfUses){
        super(name, description, rarity, effect);
        this.amountOfUses = amountOfUses;
    }

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

        switch(getEffect()){
            case("health"):
                character.addHealth(getAmountModified());
                break;
            case("hunger"):
                character.addHunger(getAmountModified());
                break;
            case("thirst"):
                character.addThirst(getAmountModified());
                break;
            case("warmth"):
                character.addWarmth(getAmountModified());
                break;
            default:
                System.err.println("Invalid item effect.");
                break;
        }

        // Decrement the amount of uses of item remaining.
        amountOfUses--;
    }
}
