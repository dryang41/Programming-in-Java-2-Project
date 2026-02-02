import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // initial exception handling structure
        try {
            System.out.println("This is a test message.");
            System.out.println(67);
        }catch (ArrayIndexOutOfBoundsException ex) {
            System.err.println("Index out of range of array.");
        }catch (StringIndexOutOfBoundsException ex) {
            System.err.println("Index out of range of string.");
        //} catch (IOException ex) {
           // System.err.println("Input or output has failed.");
        }catch (Exception ex) {
            System.out.println("An error has occurred.");
            System.err.println(ex.toString());
        }finally {
            System.out.println("Game over.");
        }
    }
}