import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/*
* Game class to run the game itself. Run this class to play the game.
*/
public class Game {
    // Makes sure the game is still running
    private static boolean gameOn = true;
    // In-game trackers to keep track of game progress.
    private final static int endPoint = 30;
    private static int timeTracker = 0;
    // Checks if player revisits cabin to give backpack to expand inventory
    private static boolean cabinRevisited = false;

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
            throw new InvalidCommandException(invalidCommand);
        }
    }

    /**
     * The main method for making sure the game works.
     * It takes what the user inputs and applies the corresponding logic.
     * It uses a massive switch statement to see what commands are inputted.
     * @throws InvalidCommandException Any unexpected errors will trigger a custom exception
     */
    private static void processVerbAndNoun() throws InvalidCommandException {
        // Try-catch block just in case
        try {
            // Switch case that uses the verb
            switch (verb) {
                // If player asks for all commands
                case "help":
                    // If no other text appears besides "help"
                    if (noun.isBlank()) {
                        printCommands();
                    } else {
                        System.out.println("Help " + noun + " is not valid, only \"help\".");
                    }
                    break;
                    // If player wants to check something
                case "check":
                    // Inner switch-case statement to check user input
                    switch (noun) {
                        // If player wants to check their stats
                        case "status":
                            // Uses the Character class' toString method
                            System.out.println(character);
                            break;
                        // If player wants to check items in inventory
                        case "inventory":
                            // Uses the Inventory class' toString method
                            System.out.println(inventory);
                            // Asks if player wants to sort their inventory
                            System.out.println("Would you like to sort your inventory? Type \"name\" or \"rarity\" to sort by name or rarity.");
                            sortInventory();
                            break;
                        // If player wants to check the time
                        case "watch":
                            checkWatch();
                            break;
                        // If player needs a refresher on locations
                        case "locations":
                            for (Location l : allLocations) {
                                // Uses the Location class' toString method
                                System.out.println(l);
                            }
                            break;
                        // If noun is not valid
                        default:
                            System.out.println("You can't check " + noun);
                    }
                    break;
                // If player wants to check their current location
                case "current":
                    if (noun.equals("location")) {
                        // Uses the Location class' toString method
                        System.out.println(characterLocation);
                    } else {
                        System.out.println("There is no \"current " + noun + "\" command.");
                    }
                    break;
                // Main way to progress the game, move to a different location and executes events
                case "move":
                    // Inner switch-case statement to check noun
                    switch (noun) {
                        // If north
                        case "north":
                            if (!updateLocation("north")) {
                                generateAndExecuteEvent("north");
                            }
                            break;
                        // If south
                        case "south":
                            if (!updateLocation("south")) {
                                generateAndExecuteEvent("south");
                            }
                            break;
                        // If west
                        case "west":
                            if (!updateLocation("west")) {
                                generateAndExecuteEvent("west");
                            }
                            break;
                        // If east
                        case "east":
                            if (!updateLocation("east")) {
                                generateAndExecuteEvent("east");
                            }
                            break;
                        // If cabin
                        case "cabin":
                            updateLocation("cabin");
                            // If the cabin has not revisited at least once
                            if (!cabinRevisited) {
                                // Set cabinRevisited to true so this if-statement never fires again
                                cabinRevisited = true;
                                // Expand inventory
                                inventory.addInventorySlots(6);
                                // Executes the event that gives the empty bottle
                                database.createEvent(2).executeEvent();
                                System.out.println("Your inventory has expanded to fill " + inventory.getInventorySlots() + " items.");
                                // Increment timer
                                timeTracker++;
                            }
                            // If the cabin has not revisited at least once
                            else {
                                // Executes the event that does nothing in the cabin location
                                database.createEvent(3).executeEvent();
                            }
                            break;
                        // Invalid location to move
                        default:
                            System.out.println(noun + " is not a valid location to move!");
                            break;
                    }
                    break;
                // If player wants to use an item
                case "use":
                    // Try-catch block in case of errors
                    try {
                        // Takes the noun and changes it into a number
                        int index = Integer.parseInt(noun);
                        // Creates both a Consumable and Passive in case for both
                        ConsumableItem consumableItem = inventory.getConsumable(index);
                        PassiveItem passiveItem = inventory.getPassive(index);

                        // If it's a consumable
                        if (consumableItem != null) {
                            // Use the item
                            consumableItem.UseItem();
                            // Check if consumable has no more uses available and remove it from inventory if so
                            if (consumableItem.getAmountOfUses() == 0) {
                                inventory.removeItem(index - 1);
                            }
                        // If it's a passive
                        } else if (passiveItem != null) {
                            // Use the item
                            passiveItem.UseItem();
                        // If the user input is incorrect or the slot isn't filled with an item
                        } else {
                            System.out.println("That slot is not filled or doesn't exist!");
                        }
                    } catch (Exception ex) {
                        System.out.println("That is not a valid number!");
                    }
                    break;
                // If player wants to unequip and passive item
                case "unequip":
                    try {
                        // Same logic as case "use"
                        int index = Integer.parseInt(noun);

                        PassiveItem passiveItem = inventory.getPassive(index);

                        if (passiveItem != null) {
                            // Unequip item and unapplies items effect to character
                            passiveItem.UnequipItem();
                        } else {
                            System.out.println("That slot is not filled with a unequippable item.");
                        }
                    } catch (Exception ex) {
                        System.out.println("That is not a valid number!");
                    }
                    break;
                // If player wants to collect water, which only works if the current location is the west and the character has an empty bottle
                case "collect":
                    boolean hasBottle = false;
                    // Checking if user input is correct
                    if (noun.equals("water") && characterLocation.getName().equals("West")) {
                        // Loop through inventory
                        for (int i = 0; i < inventory.getItems().size(); i++) {
                            if (inventory.getItems().get(i).getName().equals("Empty Bottle")) {
                                inventory.removeItem(i);
                                inventory.addItem(new ConsumableItem("Water Bottle", "A plastic bottle filled with drinking water, it restores a bit of thirst.", 1, "thirst", 2));
                                hasBottle = true;
                            }
                        }
                        if (!hasBottle) {
                            System.out.println("You don't have an empty container to fill water with!");
                        }
                    } else {
                        System.out.println("Collect " + noun + " is not a valid command!");
                    }
                    break;
                // If player wants to quit the game
                case "quit":
                    // If no other text is inputted
                    if (noun.isEmpty()) {
                        // Set while-loop condition in main method to false
                        gameOn = false;
                    } else {
                        System.out.println("quit " + noun + " is not a valid command");
                    }
                    break;
                // If verb and noun is invalid
                default:
                    System.out.println(verb + " " + noun + " is not a valid command!");
                    break;
            }
        // Any exceptions will be thrown as custom exception
        } catch (Exception ex) {
            throw new InvalidCommandException(invalidCommand);
        }
    }

    /**
     * Method for both setting up game
     */
    private static void introSequence() {
        // Keeping track if the sequence is still happening
        boolean lanternGrabbed = false;
        boolean openedDoor = false;

        // Adding starting slots to inventory
        inventory.addInventorySlots(character.getStartingSlots());

        // Display intro messages
        startMessage();

        while (!openedDoor) {
            while (!lanternGrabbed) {
                try {
                    // Find user input
                    findVerbAndNoun();

                    // If user inputs correct command.
                    if (verb.equals("grab") && noun.equals("lantern")) {
                        // Sets lanternGrabbed to true to not be stuck in infinite loop
                        lanternGrabbed = true;
                        // Event to give lantern to player
                        Event event = database.createEvent(1);
                        event.executeEvent();
                    }
                    // If user doesn't input correct command.
                    else {
                        System.out.println(verb + "" + noun + " is not a valid command! Type \"grab lantern\" to grab the lantern.");
                    }
                } catch (Exception ex) {
                    System.err.println(ex);
                    log.error(ex);
                }
            }

            try {
                // Find user input.
                findVerbAndNoun();

                // If user inputs correct command
                if (verb.equals("open") && noun.equals("door")) {
                    // Sets openedDoor to true to not be stuck in infinite loop
                    openedDoor = true;
                    // Display messages to player describing world.
                    messageAfterLeaving();
                }
                // If user doesn't input correct command.
                else {
                    System.out.println(verb + "" + noun + " is not a valid command! Type \"open door\" to open the door.");
                }
            } catch (Exception ex) {
                System.err.println(ex);
                log.error(ex);
            }
        }
    }

    /**
     * Method to give the game an ending once enough events have passed.
     */
    private static void endSequence() {
        // Inform user of a strange occurrence
        System.out.println("All of a sudden, something loud shakes everything. The ground trembles and shakes, you almost fall from the tremors.");
        System.out.println("You look around and realize the source of the noise is from the direction of the cabin.");
        System.out.println("Could the source of the tremors also be near the cabin? You have to check it out.");

        // Boolean to make sure while-loop runs
        boolean touchedObject = false;
        while(!touchedObject) {
            // Inner while-loop that serves as the first part of the ending
            boolean headedToCabin = false;
            while (!headedToCabin) {
                try {
                    // Get user input
                    findVerbAndNoun();

                    // If user input is correct
                    if (verb.equals("move") && noun.equals("cabin")) {
                        // Update location to cabin
                        updateLocation("cabin");
                        // Update while-loop condition to not have infinite loop
                        headedToCabin = true;
                        // Prints message to player
                        strangeObjectMessage();
                    }
                    // If user input is incorrect
                    else {
                        System.out.println("You feel compelled to move to the cabin, you need to investigate the source of the tremors.");
                    }
                    // Error messages
                } catch (IOException ex) {
                    System.err.println("Input failure.");
                    log.error("Input failure.");
                } catch (Exception ex) {
                    System.err.println(ex);
                    log.error(ex);
                }
            }

            // Part of outer while-loop that acts as the second part of the ending
            try {
                // Get user input
                findVerbAndNoun();

                // If user input is correct
                if (verb.equals("touch") && noun.equals("object")) {
                    // Update while-loop condition to not have infinite loop
                    touchedObject = true;
                    // Prints message to player
                    endingMessage();
                }
                // If user input is incorrect
                else {
                    System.out.println("You feel compelled to touch the object. Type \"touch object\" to interact with the strange object.");
                }
            // Error messages
            } catch (IOException ex) {
                System.err.println("Input failure.");
                log.error("Input failure.");
            } catch (Exception ex) {
                System.err.println(ex);
                log.error(ex);
            }
        }

        // Finally, use the endGame method to finish ending the game
        endGame();
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
                    System.out.println(verb + " " + noun + " is not a valid command!");
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
     * @return Returns if the character's location before logic is the same as it after.
     */
    private static boolean updateLocation(String name) {
        // Copy of character location
        Location currentLocation = characterLocation;
        // Loop through and check if parameter matches a location's name
        for (Location l : allLocations) {
            if (l.getName().toLowerCase().equals(name.toLowerCase())) {
                // Update variable
                characterLocation = l;
            }
        }

        // If characterLocation is not updated, inform player.
        if (currentLocation.getName().equals(characterLocation.getName())) {
            System.out.println("You can't move to the same location!");
        }
        else {
            // If character moves to the cabin, this if-else is here to not have bad grammar, "You moved cabin" will print if there is no if-else
            if (name.equals("cabin")) {
                System.out.println("You moved to the " + name + ".");
            }
            // Increment the time tracker, the game only progresses through moving to locations other than the cabin.
            else {
                System.out.println("You moved " + name + ".");
                timeTracker++;
            }
        }

        // Return if the current location is equals copy location variable
        return currentLocation.getName().equals(characterLocation.getName());
    }

    /**
     * Both generates and executes an event based on the character's location.
     * It uses random number generation to pick a random event from the database and executes event.
     * @param location The name of the location the character is.
     */
    private static void generateAndExecuteEvent(String location) {
        // Min and max variables for the random number generator
        int minEventId;
        int maxEventId;
        // Random number generator that picks a number to choose an eventID from
        int eventId;
        // Event object variable to execute
        Event event;

        // Switch-case to find all locations except cabin
        switch(location) {
            //
            case "north":
                minEventId = 4;
                maxEventId = 8;
                eventId = (int)(Math.random() * (maxEventId - minEventId + 1)) + minEventId;

                event = database.createEvent(eventId);
                event.executeEvent();
                break;
            case "west":
                minEventId = 9;
                maxEventId = 13;
                eventId = (int)(Math.random() * (maxEventId - minEventId + 1)) + minEventId;

                event = database.createEvent(eventId);
                event.executeEvent();
                break;
            case "south":
                minEventId = 14;
                maxEventId = 18;
                eventId = (int)(Math.random() * (maxEventId - minEventId + 1)) + minEventId;

                event = database.createEvent(eventId);
                event.executeEvent();
                break;
            case "east":
                minEventId = 19;
                maxEventId = 23;
                eventId = (int)(Math.random() * (maxEventId - minEventId + 1)) + minEventId;

                event = database.createEvent(eventId);
                event.executeEvent();
                break;
            default:
                System.err.println("That is not a valid location!");
                break;
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
        System.out.println("Check Locations - learn about all locations on the map.");
        System.out.println("Move North, East, South, West, Cabin - you move to the corresponding direction.");
        System.out.println("Use [item slot] - uses the item and the effects are applied.");
        System.out.println("Unequip [item slot] - unequips items with no limit on times of use.");
        System.out.println("Collect Water - if nearby water, you could collect water if you have a container.");
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
     * Prints out the messages that happens once you reach the end of the game.
     */
    private static void strangeObjectMessage() {
        System.out.println("As you approach the cabin, you see something odd coming from inside the cabin.");
        System.out.println("You move a little closer and can see clearly what it is, a strange, glowing, blue light coming from inside.");
        System.out.println("What is that? You peek your head inside and to minimize risk in case it's dangerous.");
        System.out.println("You see a blue, glowing, oval shaped, cloudy object in the center of the room. It seems to be vibrating, is it dangerous touch?");
        System.out.println("You don't think it's dangerous to touch, in fact, you feel compelled to touch the strange object.");
    }

    /**
     * Prints out the ending messages.
     */
    private static void endingMessage() {
        System.out.println("You reach out your hand to touch the strange object, you hope it's not dangerous.");
        System.out.println("As your hand gets closer, a strange feeling resonates throughout your whole body.");
        System.out.println("Your hand makes contact with the strange object, it glows even brighter and you slip into unconscious.");
        System.out.println("You open your eyes and you find to see yourself in a bed, your bed.");
        System.out.println("You're back home, you made it back.");
        System.out.println("You wonder if that was all a dream, but it felt too real to be a dream.");
        System.out.println("It doesn't matter if that was a dream, you are glad to just be back home.");
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

    /**
     * Sorts and prints the inventory based on user input.
     */
    private static void sortInventory() {
        try {
            findVerbAndNoun();
            // Sort inventory by name
            if (verb.equals("name") && noun.isBlank()) {
                inventory.sortByName();
                System.out.println(inventory);
            }
            // Sort inventory by rarity
            else if (verb.equals("rarity") && noun.isBlank()) {
                inventory.sortByRarity();
                System.out.println(inventory);
            }
            // If user doesn't sort by name or rarity.
            else {
                System.out.println("Inventory not sorted.");
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
        // Play the intro sequence
        introSequence();
        // Most errors are handled within the methods themselves, so only need one catch statement
        try {
            // While-loop to keep game running
            while(gameOn) {
                // Get and process user-input
                findVerbAndNoun();
                processVerbAndNoun();

                // If the character ever reaches 0 health, prematurely end the game to simulate death.
                if (character.getHealth() == 0) {
                    endGame();
                }
                // If the game has gone for long enough, trigger the end sequence.
                else if (timeTracker >= endPoint) {
                    endSequence();
                }
            }
        // any other errors
        }catch (Exception ex) {
            System.out.println("An error has occurred.");
            System.err.println(ex);
            log.error(ex);
        // finally block to always run
        }finally {
            System.out.println("Game over. Thank you for playing.");
        }
    }
}