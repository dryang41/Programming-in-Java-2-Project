import java.util.Comparator;

public class SortByRarity implements Comparator<Item> {

    @Override
    public int compare(Item i1, Item i2) { return i2.getRarity().compareTo(i1.getRarity()); }
}
