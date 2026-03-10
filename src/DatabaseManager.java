import java.sql.*;
import java.util.ArrayList;

/**
 * Class to manage the database of the game. This database will hold the data for all items and possibly events.
 * This class both creates and populates the database.
 */
public final class DatabaseManager {
    private final static String databaseName = "game";
    private final static String databaseUrl = "jdbc:derby:" + databaseName + ";create=true";
    private static Connection connection;

    public static Connection getConnection() { return connection; }

    // Constructor
    public DatabaseManager() {
        DatabaseSetup();
    }

    /**
     * Creates the database itself and removes any tables that may already exist to prevent potential duplicates.
     * It uses the methods to create the tables and populate the tables after the database is created.
     */
    private static void DatabaseSetup() {
        try {
            connection = DriverManager.getConnection(databaseUrl);

            DatabaseMetaData databaseMeta = connection.getMetaData();

            ResultSet tablesFound = databaseMeta.getTables(null, null, "%", new String[] {"TABLE"});

            Statement statement = connection.createStatement();

            // Searches through database to find any tables to delete them.
            while(tablesFound.next()) {
                statement.executeUpdate("DROP TABLE " + tablesFound.getString("TABLE_NAME"));
                connection.commit();
            }

            // Close statement
            statement.close();

            // Creates tables
            InitializeDatabase(connection);

            // Populates tables
            PopulateDatabase(connection);

            //System.out.println("Database successfully created.");
        } catch (SQLException ex) {
            System.err.println("Database error has occurred.");
            System.err.println(ex.getMessage());
        } catch (Exception ex) {
            System.err.println("An error has occurred.");
            System.err.println(ex.getMessage());
        }
    }

    /**
     * Creates and formats tables in the database to hold data.
     */
    private static void InitializeDatabase(Connection c) {
        // Try with resources
        try (Statement statement = c.createStatement()) {
            // Items tables with an id that auto-increments, name, description, rarity, effect, and amountOfUses.
            statement.executeUpdate("CREATE TABLE items("
                    + " itemid INTEGER PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                    + " name VARCHAR(50) NOT NULL,"
                    + " description VARCHAR(255) NOT NULL,"
                    + " rarity INTEGER NOT NULL,"
                    + " effect VARCHAR(50) NOT NULL,"
                    + " amountOfUses INTEGER)");

            // Locations table with name as primary key, and a description.
            statement.executeUpdate("CREATE TABLE locations(" +
                    " name VARCHAR(25) PRIMARY KEY NOT NULL," +
                    " description VARCHAR(255) NOT NULL)");

            // Events table with an id that auto-increments, description, effectiveness, itemid and locationname foreign keys
            statement.executeUpdate("CREATE TABLE events(" +
                    " eventid INTEGER PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
                    " description VARCHAR(255) NOT NULL," +
                    " effectoncharacter VARCHAR(25)," +
                    " effectiveness INTEGER," +
                    " itemid INTEGER," +
                    " locationname VARCHAR(25) NOT NULL," +
                    " CONSTRAINT fk_itemid FOREIGN KEY (itemid) REFERENCES items(itemid)," +
                    " CONSTRAINT fk_location FOREIGN KEY (locationname) REFERENCES locations(name))");
            //System.out.println("Table successfully created.");
        } catch (SQLException ex) {
            System.err.println("Failure to initialize database.");
            System.err.println(ex.getMessage());
        } catch (Exception ex) {
            System.err.println("An error has occurred.");
            System.err.println(ex.getMessage());
        }
    }

    /**
     * Populates the items table with data.
     * @param c Connection object to database.
     */
    private static void populateItems(Connection c) {
        // Try with resources
        try (Statement statement = c.createStatement()) {
            // Lantern that the player is given at the start of the game.
            statement.executeUpdate("INSERT INTO items (name, description, rarity, effect) VALUES ('Gas Lantern', 'A gas powered lantern that seems to burn forever. It warms you up a bit.', 1, 'warmth')");
            // Every medkit variety, each of them differing by rarity and description.
            statement.executeUpdate("INSERT INTO items (name, description, rarity, effect, amountOfUses) VALUES ('MedKit', 'A small, portable pack of heals. It restores a little bit of health.', 1, 'health', 2)");
            statement.executeUpdate("INSERT INTO items (name, description, rarity, effect, amountOfUses) VALUES ('MedKit', 'A small, portable pack of heals. It restores some health.', 2, 'health', 2)");
            statement.executeUpdate("INSERT INTO items (name, description, rarity, effect, amountOfUses) VALUES ('MedKit', 'A small, portable pack of heals. It restores a lot of health.', 3, 'health', 2)");
            // Empty Water bottle item used to store water.
            statement.executeUpdate("INSERT INTO items (name, description, rarity, effect) VALUES ('Empty Bottle', 'An empty plastic bottle used for holding water, the label is ripped off.', 1, 'thirst')");
            // Full Water bottle item, an empty bottle that is filled up.
            statement.executeUpdate("INSERT INTO items (name, description, rarity, effect, amountOfUses) VALUES ('Water Bottle', 'A plastic bottle filled with drinking water, it restores a bit of thirst.', 1, 'thirst', 2)");
            // Generic food items, each of them differing by rarity and description
            statement.executeUpdate("INSERT INTO items (name, description, rarity, effect, amountOfUses) VALUES ('Food', 'A tasty edible meal that brings great pleasure, it restores a bit of hunger.', 1, 'hunger', 1)");
            statement.executeUpdate("INSERT INTO items (name, description, rarity, effect, amountOfUses) VALUES ('Food', 'A tasty edible meal that brings great pleasure, it restores some hunger.', 2, 'hunger', 1)");
            statement.executeUpdate("INSERT INTO items (name, description, rarity, effect, amountOfUses) VALUES ('Food', 'A tasty edible meal that brings great pleasure, it restores a lot of hunger.', 3, 'hunger', 1)");
            // Warm clothing passive item
            statement.executeUpdate("INSERT INTO items (name, description, rarity, effect) VALUES ('Warm Clothes', 'A baggy set of black clothing that reminds you of home, it somewhat warms you up.', 2, 'warmth')");
        } catch (SQLException ex) {
            System.err.println("Failure to populate items table.");
            System.err.println(ex.getMessage());
        } catch (Exception ex) {
            System.err.println("An error has occurred.");
            System.err.println(ex.getMessage());
        }
    }

    /**
     * Populates the locations table with data.
     * @param c Connection object to database.
     */
    private static void populateLocations(Connection c) {
        // Try-with resources
        try (Statement statement = c.createStatement()) {
            // A location for every cardinal direction plus the central cabin
            statement.executeUpdate("INSERT INTO locations (name, description) VALUES ('North', 'An impressively massive mountain range, the cold and rough terrain will make any expedition treacherous. You need to proceed with caution.')");
            statement.executeUpdate("INSERT INTO locations (name, description) VALUES ('West', 'A vast, beautiful, but dangerous sea. It may look very enticing to just jump in and go for a swim, but the unpredictable strong currents will certainly drag you away.')");
            statement.executeUpdate("INSERT INTO locations (name, description) VALUES ('South', 'An impressively massive mountain range, the cold and rough terrain will make any expedition treacherous. You need to proceed with caution.')");
            statement.executeUpdate("INSERT INTO locations (name, description) VALUES ('East', 'A dense, mazelike jungle where any deep path will lead to being lost. You should not wander too deep in the jungle, or you may never return.')");
            statement.executeUpdate("INSERT INTO locations (name, description) VALUES ('Cabin', 'The closest place to home, a wooden cabin that lays in the middle of the land. A warm interior and soft bed comforts you greatly.')");
        } catch (SQLException ex) {
            System.err.println("Failure to populate locations table.");
            System.err.println(ex.getMessage());
        } catch (Exception ex) {
            System.err.println("An error has occurred.");
            System.err.println(ex.getMessage());
        }
    }

    /**
     * Populates the events table with data.
     * @param c Connection object to database.
     */
    private static void populateEvents(Connection c) {
        try (Statement statement = c.createStatement()) {
            // All cabin events, id's 1 and 2
            statement.executeUpdate("INSERT INTO events (description, itemid, locationname) VALUES ('You grab the lantern and the light fills the room. There is only a bed and a desk in the room, you see a door that leads outside. You should open the door and head outside.', 1, 'Cabin')");
            statement.executeUpdate("INSERT INTO events (description, itemid, locationname) VALUES ('You walk back to the place where you started, the cabin. There is something odd in the distance, there is a backpack. Was that always there? You grab the backpack and something is inside, an empty bottle.', 5, 'Cabin')");

            // All north events, id's 3 through 8
            statement.executeUpdate("INSERT INTO events (description, itemid, locationname) VALUES('When you approach the foot of the mountain range, you come across a set of bulky clothing. You grab it and take it for yourself.', 8, 'North')");
            statement.executeUpdate("INSERT INTO events (description, itemid, locationname) VALUES('You head into the mountain range and after a little bit of looking around, you see a well preserved pack of meat. It still seems edible.', 10, 'North')");
            statement.executeUpdate("INSERT INTO events (description, effectoncharacter, effectiveness, locationname) VALUES('The harsh, bitter cold takes you by surprise, it is so cold that any warm clothing may not work.', 'warmth', -2, 'North')");
            statement.executeUpdate("INSERT INTO events (description, effectoncharacter, effectiveness, locationname) VALUES('As you walk across the rugged terrain, a rock rips your clothing and the opening allows the cold to hit you hard.', 'warmth', -2, 'North')");
            statement.executeUpdate("INSERT INTO events (description, effectoncharacter, effectiveness, locationname) VALUES('As the snow falls from the sky, you decide to open your mouth and consume snow. The snow melts as it hits your mouth and quite tasty.', 'thirst', 1, 'North')");

            // All west events, id's 9 through 14
            statement.executeUpdate("INSERT INTO events (description, locationname) VALUES('You approach the beach and look out into the beautiful sea. You know it is best to not try and swim.', 'West')");
            statement.executeUpdate("INSERT INTO events (description, itemid, locationname) VALUES('You walk across the beach as you see something in the sand. It is a pack of heals, did that wash ashore?', 3, 'West')");
            statement.executeUpdate("INSERT INTO events (description, itemid, locationname) VALUES('There is a strange sound coming from the sea, a metal crate is somehow floating in the water. It washes ashore and is filled with food.', 9, 'West')");
            statement.executeUpdate("INSERT INTO events (description, effectoncharacter, effectiveness, locationname) VALUES('Despite knowing the rough seas will certainly overpower you, you jump in the water anyway. The currents drag you under and fill your lungs, thankfully, you make it back onto land.', 'health', -3, 'West')");
            statement.executeUpdate("INSERT INTO events (description, effectoncharacter, effectiveness, locationname) VALUES('You build a pit in the sand and fill it with water, and the Sun warms up the water. You take a dip and it is so comfortable, you wish you could stay there forever.', 'health', 1, 'West')");

            // All south events, id's 15 through 20
            statement.executeUpdate("INSERT INTO events (description, effectoncharacter, effectiveness, locationname) VALUES ('As you traverse the thick jungle interior, trying not to get lost, you trip on a thick root. The fall hurts you a bit.', 'health', -1, 'South')");
            statement.executeUpdate("INSERT INTO events (description, effectoncharacter, effectiveness, locationname) VALUES ('You make your way through the jungle as you come across a small pond. The water of the pond seems to have healing properties.', 'health', 2, 'South')");
            statement.executeUpdate("INSERT INTO events (description, itemid, locationname) VALUES ('You smell something quite appeitzing in the distance, you make your way to it. It is a delicious tree of apples, you collect some.', 7, 'South')");
            statement.executeUpdate("INSERT INTO events (description, locationname) VALUES ('You stay on the path to not get lost. Despite your best efforts, you find nothing. At least you did not get lost', 'South')");
            statement.executeUpdate("INSERT INTO events (description, effectoncharacter, effectiveness, locationname) VALUES ('You walk through the forest and get lost. You eventually find your way out but journey makes you hungry.', 'hunger', -2, 'South')");

            // All east events, id's 21 through 26
            statement.executeUpdate("INSERT INTO events (description, itemid, locationname) VALUES('In the barren desert, you somehow spot some vegatation, a cactus. You collect the cactus for some food just in case.', 8, 'East')");
            statement.executeUpdate("INSERT INTO events (description, itemid, locationname) VALUES('You walk across the desert, you shoes sinking into the hot sand. You walk peacefully as you trip over something, a medkit.', 4, 'East')");
            statement.executeUpdate("INSERT INTO events (description, effectoncharacter, effectiveness, locationname) VALUES ('The hot desert Sun hits your skin making you sweat a lot. The profusely sweating makes your throat dry and thirsty.', 'thirst', -2, 'East')");
            statement.executeUpdate("INSERT INTO events (description, effectoncharacter, effectiveness, locationname) VALUES ('As you walk across the desert, your foot suddenly sinks deep into the sand. Your leg gets scratched on something in the sand, injuring you.', 'health', -2, 'East')");
            statement.executeUpdate("INSERT INTO events (description, effectoncharacter, effectiveness, locationname) VALUES ('You hear something creak in the distance, could it be something? You see a giant shadow follow you, you run. After some distance, you realize it was a false alarm, though the running makes you very hungry', 'hunger', -3, 'East')");

        } catch (SQLException ex) {
            System.err.println("Failure to populate events table.");
            System.err.println(ex.getMessage());
        } catch (Exception ex) {
            System.err.println("An error has occurred.");
            System.err.println(ex.getMessage());
        }
    }

    /**
     * Populates the tables with data.
     * @param c Connection object to database.
     */
    private static void PopulateDatabase(Connection c) {
        populateItems(c);
        populateLocations(c);
        populateEvents(c);
    }

    /**
     * Converts all data from locations table into Location objects and puts them into a List.
     * @return ArrayList of all locations.
     */
    public ArrayList<Location> createLocations() {
        ArrayList<Location> locations = new ArrayList<>();

        // Try-with resources
        try (Statement statement = getConnection().createStatement(); ResultSet rs = statement.executeQuery("SELECT * FROM locations")){
            // For every item in locations table
            while (rs.next()) {
                // Adding to storage ArrayList
                locations.add(new Location(rs.getString("name"), rs.getString("description")));
            }
        } catch (SQLException ex) {
            System.err.println("SQL Error");
        }

        return locations;
    }

    /**
     * Takes a name of a location and converts it into a location object based on data from locations table.
     * @param name Primary key of location
     * @return Location object
     */
    private Location convertNametoLocation(String name) {
        Location location = null;

        try (Statement statement = getConnection().createStatement(); PreparedStatement pstatement = getConnection().prepareStatement("SELECT * FROM locations WHERE name = ?")) {
            pstatement.setString(1, name);
            ResultSet rs = pstatement.executeQuery();

            while(rs.next()) {
                location = new Location(rs.getString("name"), rs.getString("description"));
            }
        } catch (SQLException ex) {
            System.err.println("SQL Error");
        } catch (Exception ex) {
            System.err.println(ex);
        }

        return location;
    }

    /**
     * Takes the primary key of item and converts it into a item object based on data from items table.
     * @param id Primary key of item
     * @return Location object
     */
    private Item convertIDtoItem(int id) {
        Item item = null;

        try (Statement statement = getConnection().createStatement(); PreparedStatement pstatement = getConnection().prepareStatement("SELECT * FROM items WHERE itemid = ?")) {
            pstatement.setInt(1, id);
            ResultSet rs = pstatement.executeQuery();

            while(rs.next()) {
                if (rs.getInt("amountOfUses") == 0) {
                    item = new PassiveItem(rs.getString("name"), rs.getString("description"), rs.getInt("rarity"), rs.getString("effect"));
                }
                else {
                    item = new ConsumableItem(rs.getString("name"), rs.getString("description"), rs.getInt("rarity"), rs.getString("effect"), rs.getInt("amountOfUses"));
                }
            }
        } catch (SQLException ex) {
            System.err.println("SQL Error");
        } catch (Exception ex) {
            System.err.println(ex);
        }

        return item;
    }

    /**
     * Creates an event object based on a number given to the method.
     * @param id A random number that is also the primary key of an event in events table.
     * @return The event that is based on the parameter id.
     */
    public Event createEvent(int id) {
        Event event = null;

        try (Statement statement = getConnection().createStatement()) {
            PreparedStatement pstatement = getConnection().prepareStatement("SELECT * FROM events WHERE eventid = ?");
            pstatement.setInt(1, id);
            ResultSet rs = pstatement.executeQuery();
            while (rs.next()) {
                String description = rs.getString("description");
                String effectOnCharacter = rs.getString("effectoncharacter");
                int effectiveness = rs.getInt("effectiveness");
                int itemGivenKey = rs.getInt("itemid");
                String locationPossibleKey = rs.getString("locationname");
                if (!(effectOnCharacter == null || effectOnCharacter.isEmpty())) {
                    event = new Event(description, effectOnCharacter, effectiveness, convertNametoLocation(locationPossibleKey));
                }
                else if (!(itemGivenKey == 0)) {
                    event = new Event(description, convertIDtoItem(itemGivenKey), convertNametoLocation(locationPossibleKey));
                }
                else {
                    event = new Event(description, convertNametoLocation(locationPossibleKey));
                }
            }
        } catch (SQLException ex) {
            System.err.println("SQL Error");
        } catch (Exception ex) {
            System.err.println(ex);
        }

        return event;
    }
}