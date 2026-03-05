import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/*
* Game class to run the game itself.
*/
public class Game {
    // In-game trackers to keep track of game progress.
    private final static int endPoint = 30;
    private static int timeTracker = 0;

    // Logger object
    final static Logger log = LogManager.getLogger("test");
    // Character object
    private static Character character = Character.getInstance();
    private static Inventory inventory = Inventory.getInstance();
    private Location characterLocation;

    /**
     * Prints out the initial starting messages. It sets the atmosphere and gives context to the player.
     */
    private static void startMessage(){
        System.out.println("You wake up in an unfamiliar location, the bed feels rough, this isn't your room.");
        System.out.println("The air feels different, the way the bed creaks is different, everything is different. Are you even on Earth?");
        System.out.println("There is no light in the room, except a lantern, it glows softly.");
        System.out.println("The warm, dim light slightly illuminates the room, the walls are wooden.");
        System.out.println("You have to get back home. Wherever you are, you don't know if this place is safe.");
        System.out.println("Where do you start? How do you even leave? You have to explore.");
        System.out.println("You should grab the lantern for light and look around.");
    }

    /**
     * Prints out the scripted messages to the player when the player leaves the starting room for the first time.
     */
    private static void messageAfterLeaving() {
        System.out.println("You open the doors to the cabin, it's bright and early out.");
        System.out.println("As your eyes adjust to the sudden change in light, you look outside.");
        System.out.println("It's eerily quiet, no animals, no human activity, nothing. Are you completely alone?");
        System.out.println("You look around to see if there is anything to use.");
        System.out.println("The first thing that catches your eyes is the massive mountain range to the north.");
        System.out.println("You don't even need to take a step further to realize that any unprepared exploration will lead to disaster.");
        System.out.println("There is a slight breeze coming from the west, you see nothing but a vast sea.");
        System.out.println("The sea seems infinite in size, the beautiful blue fills the horizon.");
        System.out.println("Down south is a dense, almost maze-like jungle.");
        System.out.println("You should not venture in too deep, else you might not ever make it out.");
        System.out.println("Finally, you look eastbound, it's a desert-like area that seems impassable.");
        System.out.println("Slightly beyond that, a vast deep canyon, \"What could lay at the bottom?\", you wonder.");
        System.out.println("The mix of different terrains and climates should be impossible.");
        System.out.println("You come to the conclusion that you are not on Earth. You need to go back home.");
        System.out.println("But how? For now you have to survive. You should search around for anything.");
    }

    /**
     * Checks the time of local computer and uses the hour to display a message.
     * Message depends on the time of day.
     * If time can't be read or invalid value is read, display a filler message.
     */
    public static void checkWatch() {
        LocalTime time = LocalTime.now();
        int hour = time.getHour();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("h:m");
        String wrongTime = "Is this right? Your watch seems to read the time of somewhere else. Maybe it's still reading the time back home.";

        switch(hour) {
            // Early morning
            case 0, 1, 2, 3, 4, 5:
                System.out.println("Your watch reads " + time.format(format) + " AM, it is bright early in the morning. You should sleep in.");
                System.out.println(wrongTime);
                break;
            // Morning
            case 6, 7, 8, 9, 10, 11:
                System.out.println("Your watch reads " + time.format(format) + " AM, it is early in the morning. It is time to get up, you can't stay here forever.");
                System.out.println(wrongTime);
                break;
            // Noon
            case 12:
                System.out.println("Your watch reads " + time.format(format) + " PM, it is noon. It's the perfect time to explore.");
                System.out.println(wrongTime);
                break;
            // Afternoon
            case 13, 14, 15, 16, 17, 18, 19, 20:
                System.out.println("Your watch reads " + time.format(format) + " PM, it is the afternoon. You still have time to explore.");
                System.out.println(wrongTime);
                break;
            // Evening
            case 21, 22, 23:
                System.out.println("Your watch reads " + time.format(format) + " PM, it is dead in the evening. You definitely should head back to the cabin.");
                System.out.println(wrongTime);
                break;
            // Unknown time
            default:
                System.out.println("You can't check the time, your watch seems to be broken.");
                break;
        }
    }

    /**
     * Allows the player to save their results at the end of the game.
     * It writes the character's stats at the end as well as the inventory.
     * It DOES NOT act as a save file/state, it only saves how the game ended.
     */
    public static void saveGameResults() {
        File savedResults = new File("./endresults/results.txt");

        try (FileWriter writer = new FileWriter(savedResults)){
            writer.write(character.toString());
            writer.write(inventory.toString());
            System.out.println("Results saved at: " + savedResults.getAbsolutePath());
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    public static void main(String[] args) {
        startMessage();
        // initial exception handling structure
        try {

        // if index is out of range for array
        }catch (ArrayIndexOutOfBoundsException ex) {
            System.err.println("Index out of range of array.");
            log.error("Index out of range of array.");
        // if index is out of range for string
        }catch (StringIndexOutOfBoundsException ex) {
            System.err.println("Index out of range of string.");
            log.error("Index out of range of string.");
        // any other errors
        }catch (Exception ex) {
            System.out.println("An error has occurred.");
            System.err.println(ex.toString());
            log.error(ex);
        // finally block to always run
        }finally {
            System.out.println("Game over.");
            System.out.println("Thank you for playing.");
        }
    }
}