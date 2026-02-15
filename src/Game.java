import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/*
* Game class to run the game itself.
*/
public class Game {
    // logger object
    final static Logger log = LogManager.getLogger("test");

    /**
     * Prints out the initial starting messages. It sets the atmosphere and gives context to the player.
     */
    public static void startMessage(){
        System.out.println("You wake up in an unfamiliar location, the bed feels rough, this isn't your room.");
        System.out.println("The air feels different, the way the bed creaks is different, everything is different. Are you even on Earth?");
        System.out.println("There is no light in the room, except a lantern, it glows softly.");
        System.out.println("The warm, dim light slightly illuminates the room, the walls are wooden.");
        System.out.println("You have to get back home. Wherever you are, you don't know if this place is safe.");
        System.out.println("Where do you start? How do you even leave? You have to explore.");
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