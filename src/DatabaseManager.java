import java.sql.*;

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
                    + " id INTEGER PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                    + " name VARCHAR(50) NOT NULL,"
                    + " description VARCHAR(255) NOT NULL,"
                    + " rarity INTEGER NOT NULL,"
                    + " effect VARCHAR(50) NOT NULL,"
                    + " amountOfUses INTEGER)");
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
     * Populates the tables with data.
     * @param c Connection object to database.
     */
    private static void PopulateDatabase(Connection c) {
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

            //System.out.println("Tables successfully populated.");
        } catch (SQLException ex) {
            System.err.println("Failure to populate database.");
            System.err.println(ex.getMessage());
        } catch (Exception ex) {
            System.err.println("An error has occurred.");
            System.err.println(ex.getMessage());
        }
    }
}