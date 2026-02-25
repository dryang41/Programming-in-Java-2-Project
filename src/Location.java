public class Location {
    private String name;
    private String description;

    public Location(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void printDescription() {
        System.out.println(description);
    }
}
