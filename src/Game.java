import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/*
* Game class to run the game itself.
*/
public class Game {
    // Makes sure the game is still running
    private static boolean gameOn = true;
    // In-game trackers to keep track of game progress.
    private final static int endPoint = 30;
    private static int timeTracker = 0;
    // Is the game still in the beginning state?
    private static boolean beginningSection = true;

    // Logger object
    final static Logger log = LogManager.getLogger("test");
    // Database object
    private final static DatabaseManager database = new DatabaseManager();
    // Character object
    private static Character character = Character.getInstance();
    // Inventory object
    private static Inventory inventory = Inventory.getInstance();
    // List object to hold all locations
    private static final List<Location> allLocations = database.createLocations();
    // Where the character is, it initializes it to last in List because that's where cabin is
    private static Location characterLocation = allLocations.getLast();

    // Verb and noun variables
    private static String verb;
    private static String noun;

    // Message for InvalidCommandException exception
    private final static String invalidCommand = "Invalid command.";

    /**
     * Method for finding verb and noun.
     * If found, update the verb and noun variables.
     * @throws IOException
     */
    private static void findVerbAndNoun() throws InvalidCommandException, IOException{
        // Allows for user input in console
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        try {
            // Read line of input
            String line = reader.readLine();
            // Tokenize input data where the space character is
            String[] verbAndNoun = line.split(" ");

            // If there are a verb and noun pair
            if (verbAndNoun.length == 2) {
                verb = verbAndNoun[0].toLowerCase();
                noun = verbAndNoun[1].toLowerCase();
            }
            // If there is only one word
            else if (verbAndNoun.length == 1) {
                verb = verbAndNoun[0].toLowerCase();
                noun = "";
            }
            // If there is any other amount of words
            else {
                verb = "";
                noun = "";
                System.out.println("There can only be a verb-noun pair command, or just a verb command.");
            }
        } catch (Exception ex) {
            System.err.println(ex);
            log.error(ex);
        }
    }

    private static void processVerbAndNoun() throws InvalidCommandException {
        switch (verb) {
            case "help":
                if (noun.isBlank()) { printCommands(); }
                else { System.out.println("Help " + noun + " is not valid, only \"help\"."); }
                break;
            case "check":
                switch (noun) {
                    case "status":
                        System.out.println(character);
                        break;
                    case "inventory":
                        System.out.println(inventory);
                        System.out.println("Would you like to sort your inventory? Type \"name\" or \"rarity\" to sort by name or rarity.");
                        sortInventory();
                        break;
                    case "watch":
                        checkWatch();
                        break;
                    default:
                        System.out.println("You can't check " + noun);
                }
                break;
            case "current":
                if (noun.equals("location")) { System.out.println(characterLocation); }
                else { System.out.println("There is no \"current " + noun + "\" command."); }
                break;
            case "move":
                switch (noun) {
                    case "north":

                        updateLocation("north");
                        break;
                    case "south":
                        updateLocation("south");
                        int min = 1;
                        int max = 3;
                        int randomNumber = (int)(Math.random() * (max - min + 1)) + min;
                        Event event = database.createEvent(randomNumber);
                        event.executeEvent();
                        break;
                    case "west":
                        updateLocation("west");
                        break;
                    case "east":
                        updateLocation("east");
                        break;
                    case "cabin":
                        updateLocation("cabin");
                        break;
                    default:
                        System.out.println(noun + " is not a valid location to move!");
                        break;
                }
            case "use":
                try {
                    int index = Integer.parseInt(noun);
                    ConsumableItem consumableItem = inventory.getConsumable(index);
                    PassiveItem passiveItem = inventory.getPassive(index);

                    if (consumableItem != null) {
                        consumableItem.UseItem();
                    }
                    else if (passiveItem != null) {
                        passiveItem.UseItem();
                    }
                    else {
                        System.out.println("That slot is not filled or doesn't exist!");
                    }
                } catch (Exception ex) {
                    System.out.println("That is not a valid number!");
                }
                break;
            case "unequip":
                try {
                    int index = Integer.parseInt(noun);

                    PassiveItem passiveItem = inventory.getPassive(index);

                    if (passiveItem != null) {
                        passiveItem.UnequipItem();
                    }
                    else {
                        System.out.println("That slot is not filled with a unequippable item.");
                    }
                } catch (Exception ex) {
                    System.out.println("That is not a valid number!");
                }
                break;
            case "quit":
                if (noun.isEmpty()) { gameOn = false; }
                else { System.out.println("quit " + noun + " is not a valid command"); }
                break;
            default:
                System.out.println(verb + " " + noun + " is not a valid command!");
                break;
        }
    }

    /**
     * Method for both setting up game
     */
    private static void introSequence() {
        inventory.addInventorySlots(character.getStartingSlots());
        startMessage();
    }

    /**
     * Method for ending the game.
     */
    private static void endGame() {
        // Disable game loop
        gameOn = false;
        try {
            // Ask user
            System.out.println("Would you like to save the game results? Type \"yes\" or \"no\".");
            // While-loop condition
            boolean promptAgain = true;
            while (promptAgain) {
                // Gets user-input
                findVerbAndNoun();
                // Save results and end loop if yes
                if (verb.equals("yes") && noun.isBlank()) {
                    saveGameResults();
                    promptAgain = false;
                // Just end loop if no
                } else if (verb.equals("no") && noun.isBlank()) {
                    promptAgain = false;
                // Invalid command
                } else {
                    System.out.println(verb + " " + noun + "is not a valid command!");
                }
            }
        } catch (Exception ex) {
            System.err.println(ex);
            log.error(ex);
        }
    }

    /**
     * Updates the characterLocation variable to a new location.
     * @param name Parameter for seeing where to move the character.
     */
    private static void updateLocation(String name) {
        // Temporary copy of current location
        Location copy = characterLocation;
        // Loop through and check if parameter matches a location's name
        for (Location l : allLocations) {
            if (l.getName().toLowerCase().equals(name.toLowerCase())) {
                // Update variable
                characterLocation = l;
                System.out.println("You moved " + name);
            }
        }

        // If characterLocation is not updated, inform player.
        if (copy.equals(characterLocation)) {
            System.out.println("You did not move anywhere!");
        }
    }

    /**
     * Informs the player of all commands they can enter.
     */
    private static void printCommands() {
        System.out.println("Help - shows all commands.");
        System.out.println("Check Status - shows your health, hunger, thirst, and warmth stats.");
        System.out.println("Check Inventory - shows you all of your items in your inventory.");
        System.out.println("Check Watch - you will take a look at your watch.");
        System.out.println("Move North, East, South, West, Cabin - you move to the corresponding direction.");
        System.out.println("Use [item slot] - uses the item and the effects are applied.");
        System.out.println("Unequip [item slot] - unequips items with no limit on times of use.");
        System.out.println("Current Location - shows the location you are in");
        System.out.println("Quit - exits the game");
        System.out.println("Note: Commands only have to spelt right to work.");
    }

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
    private static void checkWatch() {
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

    private static void sortInventory() {
        try {
            // While-loop condition
            boolean promptAgain = true;
            while (promptAgain) {
                // Gets user-input
                findVerbAndNoun();
                // Save results and end loop if yes
                if (verb.equals("name") && noun.isBlank()) {
                    inventory.sortByName();
                    System.out.println(inventory);
                    promptAgain = false;
                    // Just end loop if no
                } else if (verb.equals("rarity") && noun.isBlank()) {
                    inventory.sortByRarity();
                    System.out.println(inventory);
                    promptAgain = false;
                    // Invalid command
                } else {
                    System.out.println(verb + " " + noun + "is not a valid command!");
                }
            }
        } catch (Exception ex) {
            System.err.println(ex);
            log.error(ex);
        }
    }

    /**
     * Allows the player to save their results at the end of the game.
     * It writes the character's stats at the end as well as the inventory.
     * It DOES NOT act as a save file/state, it only saves how the game ended.
     */
    private static void saveGameResults() {
        // The file where the results are saved
        File savedResults = new File("./endresults/results.txt");

        // Try-with resources
        try (FileWriter writer = new FileWriter(savedResults)) {
            // Write both the inventory and stats at the end of the game
            writer.write(character.toString());
            writer.write(inventory.toString());
            // Inform the user
            System.out.println("Results saved at: " + savedResults.getAbsolutePath());
        } catch (IOException ex) {
            System.err.println("Error writing file.");
            log.error("Error writing file.");
        } catch (Exception ex) {
            System.err.println(ex);
            log.error(ex);
        }
    }

    public static void main(String[] args) {
        startMessage();

        // initial exception handling structure
        try {
            while(gameOn) {
                findVerbAndNoun();
                processVerbAndNoun();

                if (character.getHealth() == 0) {
                    endGame();
                }
            }
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