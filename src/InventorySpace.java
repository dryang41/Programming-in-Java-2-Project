/**
 * Interface the multiple classes implement to store items. Character and backpack will use this.
 */
@FunctionalInterface
public interface InventorySpace {
    public int checkInventory();
}
