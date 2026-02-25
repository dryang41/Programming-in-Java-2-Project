import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    Character character = Character.getInstance();
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
        System.out.println("There is a slight breeze coming from the east, you see nothing but a vast sea.");
        System.out.println("The sea seems infinite in size, the beautiful blue fills the horizon.");
        System.out.println("To the north is a ");
        System.out.println("Down south is a ");
        System.out.println("The mix of diffferent terrains and climates should be impossible.");
        System.out.println("You come to the conclusion that you are not on Earth. You need to go back home.");
        System.out.println("But how? For now you have to survive. You should search around for anything.");
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