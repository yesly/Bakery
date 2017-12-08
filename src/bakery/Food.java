
package Bakery;

public class Food {
    //data fields
    final String name;
    final double price;
    
    //constructor
    public Food(String name, double price) {
        this.name = name;
        this.price = price;
    }
    
    //getters
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    } 
}
