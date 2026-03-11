import java.util.Comparator;

/**
 * Sorting class for item that sorts items by their rarity, from rarest to most common.
 */
public class SortByRarity implements Comparator<Item> {

    @Override
    public int compare(Item i1, Item i2) { return i2.getRarity().compareTo(i1.getRarity()); }
}
