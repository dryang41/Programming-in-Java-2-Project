public class PassiveItem extends Item{

    public PassiveItem(String name, String description, int rarity, String effect) {
        super(name, description, rarity, effect);
    }

    /**
     * Uses the abstract method in Item.java as a basis to override.
     */
    @Override
    public void UseItem() {
        // Informs the user that item has been equipped.
        System.out.println("You equipped the " + getName() + ".");

        applyEffect();
    }

    /**
     * Unequips the item and uses the removeEffect methods written in character.
     * If there is no valid effect, display the error message.
     * Switch-case statement to look for valid effects to modify, if found, update the variable.
     */
    public void UnequipItem() {
        // Informs the user that item has been unequiped.
        System.out.println("You unequipped the " + getName() + ".");

        switch(getEffect()){
            case("health"):
                character.removeHealth(getEffectiveness());
                break;
            case("hunger"):
                character.removeHunger(getEffectiveness());
                break;
            case("thirst"):
                character.removeThirst(getEffectiveness());
                break;
            case("warmth"):
                character.removeWarmth(getEffectiveness());
                break;
            default:
                System.err.println("Invalid item effect.");
                break;
        }
    }
}
