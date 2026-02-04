import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Game {
    final static Logger log = LogManager.getLogger("test");

    public static void main(String[] args) {
        log.debug("Is this working?");
        // initial exception handling structure
        try {
            System.out.println("This is a test message.");
            System.out.println(67);
        }catch (ArrayIndexOutOfBoundsException ex) {
            System.err.println("Index out of range of array.");
        }catch (StringIndexOutOfBoundsException ex) {
            System.err.println("Index out of range of string.");
        }catch (Exception ex) {
            System.out.println("An error has occurred.");
            System.err.println(ex.toString());
        }finally {
            System.out.println("Game over.");
        }
    }
}