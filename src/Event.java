import java.util.ArrayList;

public class Event {
    private String description;
    private String effectOnCharacter;
    private int effectiveness;
    private Item itemGiven;
    private Location locationPossible;

    // Grabbing character instance to modify if needed.
    private Character character = Character.getInstance();

    /**
     * Constructor for events that apply an effect on the character.
     * @param description Description of event.
     * @param effectOnCharacter The effect that is modified.
     * @param effectiveness How much the effectOnCharacter is changed by.
     * @param locationPossible Locations where the event can fire.
     */
    public Event(String description, String effectOnCharacter, int effectiveness, Location locationPossible) {
        this.description = description;
        this.effectOnCharacter = effectOnCharacter;
        this.effectiveness = effectiveness;
        this.locationPossible = locationPossible;
    }

    /**
     * Constructor for events that give items to the player.
     * @param description Description of event.
     * @param itemGiven The item the player will receive.
     * @param locationPossible Locations where the event can fire.
     */
    public Event(String description, Item itemGiven, Location locationPossible) {
        this.description = description;
        this.itemGiven = itemGiven;
        this.locationPossible = locationPossible;
    }

    /**
     * Constructor for events that don't give items to the player.
     * @param description Description of event.
     * @param locationPossible Locations where the event can fire.
     */
    public Event(String description, Location locationPossible) {
        this.description = description;
        this.locationPossible = locationPossible;
    }

    /**
     * @return Description of event;
     */
    public String getDescription() { return description; }

    /**
     * @return The effect that applies to the character.
     */
    public String getEffectOnCharacter() { return effectOnCharacter;}

    /**
     * @return How much the effect that applies to the character.
     */
    public int getEffectiveness() { return effectiveness; }

    /**
     * @return The item that the player receives.
     */
    public Item getItemGiven() { return itemGiven; }

    /**
     * @return The location where this event can fire.
     */
    public Location getLocationPossible() { return locationPossible; }

    /**
     * Fires the event and applies any effects to the character if there are any.
     * Also gives any items to the character if there are any.
     * Lastly, prints the description of the event.
     */
    public void executeEvent() {
        if (!(getEffectOnCharacter() == null || getEffectOnCharacter().isEmpty()) && getEffectiveness() > 0) {
            switch (getEffectOnCharacter()) {
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
        else if (!(getEffectOnCharacter() == null || getEffectOnCharacter().isEmpty()) && getEffectiveness() < 0) {
            switch (getEffectOnCharacter()) {
                case ("health"):
                    character.removeHealth(Math.abs(getEffectiveness()));
                    break;
                case ("hunger"):
                    character.removeHunger(Math.abs(getEffectiveness()));
                    break;
                case ("thirst"):
                    character.removeThirst(Math.abs(getEffectiveness()));
                    break;
                case ("warmth"):
                    character.removeWarmth(Math.abs(getEffectiveness()));
                    break;
                default:
                    System.err.println("Invalid item effect.");
                    break;
            }
        }

        if (getItemGiven() != null) {

        }

        System.out.println(getDescription());
    }
}
