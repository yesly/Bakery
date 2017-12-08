package Bakery;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.swing.JRadioButton;

public class Menu {

    //data fields
    protected List<Food> menuList = new ArrayList<>();
    double totalCharges = 0.00;

    //
    public void addToTotalCharges(double totalCharges) {
        this.totalCharges += totalCharges;
    }

    //constructor
    public Menu(List<Food> menuList) {
        menuList = new ArrayList<>();
    }

    //getters
    public List<Food> getMenuList() {
        return menuList;
    }

    //setters
    public void setMenuList(List<Food> menuList) {
        this.menuList = menuList;
    }

    public void readFromFile() {
        // Data fields
        String foodName;
        double foodPrice;

        // Open the FoodNames.txt file
        File namesFile = new File("C:\\Users\\Yesly\\Desktop\\Bakery\\src\\FoodProperties.csv");

        try (Scanner inFile = new Scanner(namesFile)) {
            // Get the names from the FoodNames.txt file.
            inFile.nextLine(); // Skip first line in file
            while (inFile.hasNext()) {
                //Get the name of the food
                String line = inFile.nextLine();
                String[] stringArray = line.split(",");

                foodName = stringArray[0];
                foodPrice = Double.parseDouble(stringArray[1]);
                // Create Food object in manuList
                menuList.add(new Food(foodName, foodPrice));
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Error: " + ex);
        }
    }

    public void showMenu() {
        readFromFile();
        Stage menuStage = new Stage();
        Group group = new Group(); // Create Group object
        int x = 80;
        int y = 150;
        for (int i = 0; i < menuList.size(); i++) {
            RadioButton rb = new RadioButton(menuList.get(i).getName() + " $" + menuList.get(i).getPrice());
            rb.setTranslateX(x);
            rb.setTranslateY(y);
            
            final double price = menuList.get(i).getPrice();
            rb.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    addToTotalCharges(price);
                }
            });

            group.getChildren().add(rb);

            y += 20;
        }

        //button to take you to receipt
        Button checkOutButton = new Button();
        checkOutButton.setText("Check Out");
        checkOutButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println(totalCharges);
            }
        });

        // Layout of menu window
        group.getChildren().add(checkOutButton);
        Scene menuScene = new Scene(group, 400, 400, Color.BEIGE); // Create Scene
        menuStage.setTitle("Menu"); // Set menuStage title to "Menu"
        menuStage.setScene(menuScene); // Set menuSatage scene
        menuStage.show(); // show menuStage

    }
}
