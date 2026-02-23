import java.util.ArrayList;

public class Event {
    private int eventId;
    private String description;
    private String effectOnCharacter;
    private int effectiveness;
    private boolean repeatable;
    private Item itemsGiven;
    private ArrayList<Location> locationsPossible;

    // Grabbing character instance to modify if needed.
    private Character character = Character.getInstance();

    /**
     * Constructor for events that apply an effect on the character.
     * @param description Description of event.
     * @param effectOnCharacter The effect that is modified.
     * @param effectiveness How much the effectOnCharacter is changed by.
     * @param repeatable If the event can happen more than once.
     * @param locationsPossible Locations where the event can fire.
     */
    public Event(String description, String effectOnCharacter, int effectiveness, boolean repeatable, ArrayList<Location> locationsPossible) {
        this.description = description;
        this.repeatable = repeatable;
        this.effectOnCharacter = effectOnCharacter;
        this.effectiveness = effectiveness;
        this.locationsPossible = locationsPossible;
    }

    /**
     * Constructor for events that give items to the player.
     * @param description Description of event.
     * @param repeatable If the event can happen more than once.
     * @param itemsGiven The item the player will receive.
     * @param locationsPossible Locations where the event can fire.
     */
    public Event(String description, boolean repeatable, Item itemsGiven, ArrayList<Location> locationsPossible) {
        this.description = description;
        this.repeatable = repeatable;
        this.itemsGiven = itemsGiven;
        this.locationsPossible = locationsPossible;
    }

    /**
     * Constructor for events that don't give items to the player.
     * @param description Description of event.
     * @param repeatable If the event can happen more than once.
     * @param locationsPossible Locations where the event can fire.
     */
    public Event(String description, boolean repeatable, ArrayList<Location> locationsPossible) {
        this.description = description;
        this.repeatable = repeatable;
        this.locationsPossible = locationsPossible;
    }
}
