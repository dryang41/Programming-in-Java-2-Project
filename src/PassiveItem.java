public class PassiveItem extends Item{

    public PassiveItem(String name, String description, Rarity rarity, String effect) {
        super(name, description, rarity, effect);
    }

    /**
     * Equips the item and uses the addEffect methods written in character.
     * If there is no valid effect, display the error message.
     * Switch-case statement to look for valid effects to modify, if found, update the variable.
     */
    public void UseItem() {
        // Informs the user that item has been equiped.
        System.out.println("You equipped the " + getName() + ".");

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
                character.removeHealth(getAmountModified());
                break;
            case("hunger"):
                character.removeHunger(getAmountModified());
                break;
            case("thirst"):
                character.removeThirst(getAmountModified());
                break;
            case("warmth"):
                character.removeWarmth(getAmountModified());
                break;
            default:
                System.err.println("Invalid item effect.");
                break;
        }
    }
}
