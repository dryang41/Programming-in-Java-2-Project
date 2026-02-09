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
     * Checks the amount of thirst the player currently has and adds the amount.
     * If it reaches the max value or higher, set thirst to max value. Finally, display the appropriate message.
     * @param amount Value used to add to thirst meter.
     */
    public void addThirst(int amount) {
        if (thirst + amount > 10){
            thirst = maximumValue;
            System.out.println("Your thirst is completely quenched. You feel great.");
        }
        else if (thirst + amount > 7) {
            thirst += amount;
            System.out.println("Your thirst is minor enough to ignore. You feel fine.");
        }
        else if (thirst + amount > 4) {
            thirst += amount;
            System.out.println("Your thirst is still present. You feel faint.");
        }
        else {
            thirst += amount;
            System.out.println("Your thirst is still heavily bothering you. You feel weak.");
        }
    }

    /**
     * Checks the amount of thirst the player currently has and subtracts the amount.
     * If it reaches zero or below, reduce some health. Finally, display the appropriate message.
     * @param amount Value used to subtract from thirst meter.
     */
    public void removeThirst(int amount) {
        if (thirst - amount <= 0) {
            System.out.println("Your thirst takes a heavy toll on you. You lost some health.");
            thirst = 0;
            removeHealth(2);
        }
        else if (thirst - amount < 4) {
            System.out.println("Your thirst is significant. You feel weak.");
            thirst -= amount;
        }
        else if (thirst - amount < 7) {
            System.out.println("Your thirst took a hit. You feel faint.");
            thirst -= amount;
        }
        else {
            System.out.println("Your thirst increases slightly. You feel fine.");
            thirst -= amount;
        }
    }

    /**
     * Checks the amount of hunger the player currently has and adds the amount.
     * If it reaches the max value or higher, set hunger to max value. Finally, display the appropriate message.
     * @param amount Value used to add to hunger meter.
     */
    public void addHunger(int amount) {
        if (hunger + amount > 10){
            hunger = maximumValue;
            System.out.println("Your hunger is gone for now. You feel great.");
        }
        else if (hunger + amount > 7) {
            hunger += amount;
            System.out.println("Your hunger is minor enough to ignore. You feel fine.");
        }
        else if (hunger + amount > 4) {
            hunger += amount;
            System.out.println("Your hunger is still present. You feel faint.");
        }
        else {
            hunger += amount;
            System.out.println("Your hunger is still heavily bothering you. You feel weak.");
        }
    }

    /**
     * Checks the amount of hunger the player currently has and subtracts the amount.
     * If it reaches zero or below, reduce some health. Finally, display the appropriate message.
     * @param amount Value used to subtract from hunger meter.
     */
    public void removeHunger(int amount) {
        if (hunger - amount <= 0) {
            System.out.println("Your hunger takes a heavy toll on you. You lost some health.");
            hunger = 0;
            removeHealth(2);
        }
        else if (hunger - amount < 4) {
            System.out.println("Your hunger is significant. You feel weak.");
            hunger -= amount;
        }
        else if (hunger - amount < 7) {
            System.out.println("Your hunger took a hit. You feel faint.");
            hunger -= amount;
        }
        else {
            System.out.println("Your hunger increases slightly. You feel fine.");
            hunger -= amount;
        }
    }

    /**
     * Checks the amount of warmth the player currently has and adds the amount.
     * If it reaches the max value or higher, set warmth to max value. Finally, display the appropriate message.
     * @param amount Value used to add to warmth meter.
     */
    public void addWarmth(int amount) {
        if (warmth + amount > 10){
            warmth = maximumValue;
            System.out.println("Your warmth is gone for now. You feel great.");
        }
        else if (warmth + amount > 7) {
            warmth += amount;
            System.out.println("Your warmth is minor enough to ignore. You feel fine.");
        }
        else if (warmth + amount > 4) {
            warmth += amount;
            System.out.println("Your warmth is still present. You feel faint.");
        }
        else {
            warmth += amount;
            System.out.println("Your warmth is still heavily bothering you. You feel weak.");
        }
    }

    /**
     * Checks the amount of warmth the player currently has and subtracts the amount.
     * If it reaches zero or below, reduce some health. Finally, display the appropriate message.
     * @param amount Value used to subtract from warmth meter.
     */
    public void removeWarmth(int amount) {
        if (warmth - amount <= 0) {
            System.out.println("Your lack of warmth takes a heavy toll on you. You lost some health.");
            warmth = 0;
            removeHealth(2);
        }
        else if (warmth - amount < 4) {
            System.out.println("Your lack of warmth is significant. You feel weak.");
            warmth -= amount;
        }
        else if (warmth - amount < 7) {
            System.out.println("Your lack of warmth took a hit. You feel faint.");
            warmth -= amount;
        }
        else {
            System.out.println("Your lack of warmth increases slightly. You feel fine.");
            warmth -= amount;
        }
    }

    /**
     * Prints each variable for the player to see what how they're doing.
     */
    public void printStatus() {
        System.out.println("Health: " + getHealth() + ".");
        System.out.println("Thirst: " + getThirst() + ".");
        System.out.println("Hunger: " + getHunger() + ".");
        System.out.println("Warmth: " + getWarmth() + ".");
    }
}
