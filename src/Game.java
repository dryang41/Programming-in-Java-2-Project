import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/*
* Game class to run the game itself.
*/
public class Game {
    // logger object
    final static Logger log = LogManager.getLogger("test");

    public static void main(String[] args) {
        // initial exception handling structure
        try {
            System.out.println("This is a test message.");
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
        }
    }
}