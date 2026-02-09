/**
 * Character class is used to track variables of player character.
 * Singleton pattern used to create a single instance of Character object in memory for all classes to use.
 */
public class Character {
    // Max value for all player variables
    private final int maximumValue = 10;

    /*
    Variables used to track how healthy the player is in-game. Lower values lead to worse outcomes.
    Health is the ultimate variable that controls if the player dies.
    The other variables just increase the chance health decreases.
     */
    private int health = maximumValue;
    private int thirst = maximumValue;
    private int hunger = maximumValue;
    private int warmth = maximumValue;

    private static final Character instance = new Character();

    public static Character getInstance(){
        return instance;
    }

    /**
     * @return Health value player currently has.
     */
    public int getHealth() {
        return health;
    }

    /**
     * @return Thirst value player currently has.
     */
    public int getThirst() {
        return thirst;
    }

    /**
     * @return Hunger value player currently has.
     */
    public int getHunger() {
        return hunger;
    }

    /**
     * @return Warmth value player currently has.
     */
    public int getWarmth() {
        return warmth;
    }

    /**
     * Checks the amount of health the player currently has and adds the amount.
     * If it reaches the max value or higher, set health to max value. Finally, display the appropriate message.
     * @param amount Value used to add to health meter.
     */
    public void addHealth(int amount) {
        if (health + amount > 10){
            health = maximumValue;
            System.out.println("Your wounds are completely healed. You feel great.");
        }
        else if (health + amount > 7) {
            health += amount;
            System.out.println("Your wounds still hurt a bit. You feel fine.");
        }
        else if (health + amount > 4) {
            health += amount;
            System.out.println("Your wounds are still an issue. You feel faint.");
        }
        else {
            health += amount;
            System.out.println("Your wounds are still noticeably visible. You feel weak.");
        }
    }

    /**
     * Checks the amount of health the player currently has and subtracts the amount.
     * If it reaches zero or below, kill the player. Finally, display the appropriate message.
     * @param amount Value used to subtract from health meter.
     */
    public void removeHealth(int amount) {
        if (health - amount <= 0) {
            System.out.println("The damage is too great to overcome. You are dead.");
            health = 0;
        }
        else if (health - amount < 4) {
            System.out.println("The damage is severe. You feel weak.");
            health -= amount;
        }
        else if (health - amount < 7) {
            System.out.println("The damage took a toll on you. You feel faint.");
            health -= amount;
        }
        else {
            System.out.println("The damage was overall minor. You feel fine.");
            health -= amount;
        }
    }

    /**
     * Prints each variable for the player to see what how they're doing.
     */
    public void PrintStatus() {
        System.out.println("Health: " + getHealth() + ".");
        System.out.println("Thirst: " + getThirst() + ".");
        System.out.println("Hunger: " + getHunger() + ".");
        System.out.println("Warmth: " + getWarmth() + ".");
    }
}
