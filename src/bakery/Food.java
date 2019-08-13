package bakery;

import java.util.Objects;

/**
 * Used to create Food entries.
 *
 * @author Yesly Rodriguez
 * @author Yeilys Fundora
 * @see BakeryMenu for sample uses
 */
public class Food {

    final String foodName;
    final double foodPrice;
    final int foodQuantity;

    /**
     * Creates a food object
     *
     * @param name Name of the food
     * @param price The foodPrice per item
     * @param quantity The total amount of items
     */
    public Food(String name, double price, int quantity) {
        this.foodName = name;
        this.foodPrice = price;
        this.foodQuantity = quantity;
    }

    public String getFoodName() {
        return foodName;
    }

    public double getFoodPrice() {
        return foodPrice;
    }

    public int getFoodQuantity() {
        return foodQuantity;
    }

    @Override
    public String toString() {
        return "Food{" + "foodName=" + foodName + ", foodPrice=" + foodPrice + ", foodQuantity=" + foodQuantity + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.foodName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Food other = (Food) obj;
        if (!Objects.equals(this.foodName, other.foodName)) {
            return false;
        }
        return true;
    }
}
