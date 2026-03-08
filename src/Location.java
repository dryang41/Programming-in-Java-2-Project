public class Location {
    private String name;
    private String description;

    public Location(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * @return Name of location
     */
    public String getName() { return name; }

    @Override
    public String toString() {
        return name + " - " + description;
    }
}
