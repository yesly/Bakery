package bakery;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Opens Menu window
 *
 * @author Yesly Rodriguez
 * @author Yeilys Fundora
 */
public class BakeryMenu {

    Stage menuStage = new Stage();
    Group menuGroup = new Group();
    ScrollPane scrollPane = new ScrollPane();
    Scene menuScene = new Scene(scrollPane, 550, 890);

    private final List<Food> menuList = new ArrayList<>();
    private final List<Food> selectionsList = new ArrayList<>();
    Receipt receipt = new Receipt();
    double totalCharges = 0.00;
    private int quantitySelected = 0;

    public BakeryMenu(List<Food> menuList, List<Food> selectionsList) {
        menuList = new ArrayList<>();
        selectionsList = new ArrayList<>();
    }

    /**
     * Calls methods that create the BakeryMenu window
     */
    public void showMenu() {
        setMenuImage();
        readFoodPropertiesFile();
        createOptionRadioButtons();
        setBracesImages();
        crateMenuSectionsLabels();
        createCheckOutButton();
        setScrollBar();
        setMenuStage();
    }

    /**
     * Sets the layout of the menu images
     */
    private void setMenuImage() {
        ImageView menuImageView = new ImageView(new Image("menu.jpg"));
        menuImageView.setTranslateX(150);
        menuImageView.setViewport(new Rectangle2D(0, 0, 300, 300));
        menuGroup.getChildren().add(menuImageView);
    }

    /**
     * Creates radio buttons for the options of the menu
     *
     * @param menuToggleGroup toggleGroup to contain the radio buttons
     * @param length Y position of the radio buttons
     */
    private void createOptionRadioButtons() {
        ToggleGroup menuToggleGroup = new ToggleGroup();
        int length = 150;

        for (int i = 0; i < menuList.size(); i++) {
            String name = menuList.get(i).getFoodName();
            double price = menuList.get(i).getFoodPrice();

            RadioButton optionRadioButtons = new RadioButton(menuList.get(i).getFoodName() + " - $" + menuList.get(i).getFoodPrice());
            optionRadioButtons.setToggleGroup(menuToggleGroup);
            optionRadioButtons.setId("radioButtons");
            optionRadioButtons.setTranslateX(20);
            optionRadioButtons.setTranslateY(length);
            optionRadioButtons.setOnAction((ActionEvent event) -> {
                Label quantityLabel = new Label(RoyalBakery.languageProperties.getProperty("QUANTITY_MESSAGE"));
                TextField quantityTextField = new TextField();
                Button addButton = createAddButton(quantityTextField, name, price);

                createQuantityHBox(quantityLabel, quantityTextField, addButton);
            });

            length += 20;
            menuGroup.getChildren().add(optionRadioButtons);
        }
    }

    /**
     * Creates an HBox to group the quantityLabel, quantityTextField and the
     * addButton
     *
     * @param quantityLabel menuSectionsLabels to ask user for th quantity
     * @param quantityTextField text field in which user enters his desired
     * quantity
     * @param addButton button to add each selection to the selection's list
     */
    private void createQuantityHBox(Label quantityLabel, TextField quantityTextField, Button addButton) {
        HBox hb = new HBox();
        hb.setTranslateX(280);
        hb.setTranslateY(150);
        hb.getChildren().addAll(quantityLabel, quantityTextField, addButton);
        menuGroup.getChildren().add(hb);
    }

    /**
     * Creates button to add the food object and the quantity of that selection
     * to the list of selections
     *
     * @param quantityTextField text field for user to write the quantity
     * desired
     * @param name name of the selected food
     * @param price price of the selected food
     * @return
     */
    private Button createAddButton(TextField quantityTextField, String name, double price) {
        Button addButton = new Button(RoyalBakery.languageProperties.getProperty("ADD_BUTTON"));
        addButton.setId("add");
        addButton.setOnAction((ActionEvent event1) -> {
            quantitySelected = Integer.parseInt(quantityTextField.getText());

            if (!quantityTextField.getText().isEmpty() && quantitySelected > 0) {
                addSelections(name, price, quantitySelected);
                createSelectionPopUpLabel(name, quantitySelected);
                addToTotalCharges(price, quantitySelected);
            } else {
                createInvalidQuantityLabel();
            }
            quantityTextField.clear();
        });

        return addButton;
    }

    /**
     * Creates a label to tell the user he has put an invalid quantity
     */
    private void createInvalidQuantityLabel() {
        Label invalidQuantityLabel = new Label(RoyalBakery.languageProperties.getProperty("INVALID_QUANTITY"));
        invalidQuantityLabel.setTranslateX(400);
        invalidQuantityLabel.setTranslateY(180);
        PauseTransition visiblePause = new PauseTransition(
                Duration.seconds(0.8)
        );
        visiblePause.setOnFinished((ActionEvent event) -> {
            invalidQuantityLabel.setVisible(false);
        });
        visiblePause.play();
        menuGroup.getChildren().add(invalidQuantityLabel);
    }

    /**
     * Creates a menuSectionsLabels that lasts 0.5 seconds showing the quantity
     * and name of the selection
     *
     * @param name name of the selected food
     * @param quantitySelected quantity selected by the user
     */
    private void createSelectionPopUpLabel(String name, int quantitySelected) {
        Label selectionPopUp = new Label("+" + quantitySelected + " " + name);
        selectionPopUp.setTranslateX(400);
        selectionPopUp.setTranslateY(180);
        menuGroup.getChildren().add(selectionPopUp);
        PauseTransition visiblePause = new PauseTransition(
                Duration.seconds(0.8)
        );
        visiblePause.setOnFinished((ActionEvent event) -> {
            selectionPopUp.setVisible(false);
        });
        visiblePause.play();
    }

    /**
     * Adds price of each selection to the total charges
     *
     * @param price price per item
     * @param quantity quantitySelected of items selected
     */
    private void addToTotalCharges(double price, int quantity) {
        this.totalCharges += (price * quantity);
    }

    /**
     * Adds selections to selectionsList
     *
     * @param name name of the food
     * @param price price per item
     * @param quantity quantitySelected of items selected
     */
    private void addSelections(String name, double price, int quantity) {
        selectionsList.add(new Food(name, price, quantity));
    }

    /**
     * Gets the properties(name and price) of the foods from the FoodProperties
     * file. Adds Food objects into the menuList.
     */
    private void readFoodPropertiesFile() {
        File foodPropertiesFile = new File("resources\\englishFoodProperties.csv"); // Default language

        foodPropertiesFile = setPropertiesLanguageChoice(foodPropertiesFile);

        String foodName;
        double foodPrice;

        try (Scanner inFile = new Scanner(foodPropertiesFile)) {
            inFile.nextLine(); // Skip first line in file

            while (inFile.hasNext()) {
                String line = inFile.nextLine();
                String[] fileEntryAsArray = line.split(",");

                if (fileEntryAsArray.length == 2) {
                    foodName = fileEntryAsArray[0];
                    foodPrice = Double.parseDouble(fileEntryAsArray[1]);

                    Food foodEntry = new Food(foodName, foodPrice, 1);

                    if (!menuList.contains(foodEntry)) {
                        menuList.add(foodEntry);
                    } else {
                        System.out.println("Duplicated Food " + foodEntry.getFoodName());
                    }
                } else {
                    System.out.println("Invalid line : " + line);
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Error: " + ex);
        }
    }

    /**
     * Sets the foodpropertiesFile according to the language selected
     *
     * @param foodPropertiesFile
     * @return
     */
    private File setPropertiesLanguageChoice(File foodPropertiesFile) {
        if ("resources\\englishConstants.properties".equals(RoyalBakery.locationLanguageProperty)) {
            foodPropertiesFile = new File("resources\\englishFoodProperties.csv");
        } else if ("resources\\spanishConstants.properties".equals(RoyalBakery.locationLanguageProperty)) {
            foodPropertiesFile = new File("resources\\spanishFoodProperties.csv");
        }
        return foodPropertiesFile;
    }

    /**
     * Sets the layout of the braces images
     */
    private void setBracesImages() {
        final int BRACES_IMAGES_WIDTH = 250;
        int bracesImagesHeight = 150;

        for (int i = 0; i < 4; i++) {
            ImageView bracesImageView = new ImageView(new Image("braces.png"));
            bracesImageView.setTranslateX(BRACES_IMAGES_WIDTH);
            bracesImageView.setTranslateY(bracesImagesHeight);
            bracesImageView.setViewport(new Rectangle2D(0, 0, 1000, 1000));
            menuGroup.getChildren().add(bracesImageView);

            bracesImagesHeight += 180;
        }
    }

    /**
     * Creates the labels to write the names of the 4 sections of the menu
     */
    private void crateMenuSectionsLabels() {
        final int MENU_SECTIONS_LABELS_WIDTH = 300;
        int menuSectionsLabelsHeight = 210;
        String[] array = {RoyalBakery.languageProperties.getProperty("MENU_SECTIONS_LABEL1"), RoyalBakery.languageProperties.getProperty("MENU_SECTIONS_LABEL2"),
            RoyalBakery.languageProperties.getProperty("MENU_SECTIONS_LABEL3"), RoyalBakery.languageProperties.getProperty("MENU_SECTIONS_LABEL4")};

        for (int i = 0; i < array.length; i++) {
            Label menuSectionsLabels = new Label(array[i]);
            menuSectionsLabels.setTranslateX(MENU_SECTIONS_LABELS_WIDTH);
            menuSectionsLabels.setTranslateY(menuSectionsLabelsHeight);
            menuSectionsLabels.setId("sectionLabels");
            menuGroup.getChildren().add(menuSectionsLabels);

            menuSectionsLabelsHeight += 180;
        }
    }

    /**
     * Creates the button to take user to the receipt window.
     */
    private void createCheckOutButton() {
        ImageView imageView = new ImageView("cake.png");
        imageView.setFitWidth(30);
        imageView.setFitHeight(20);

        Button checkOutButton = new Button(RoyalBakery.languageProperties.getProperty("CHECK_OUT_BUTTON"));
        checkOutButton.setId("check");
        checkOutButton.setGraphic(imageView);

        checkOutButton.setTranslateX(450);
        checkOutButton.setTranslateY(850);
        checkOutButton.setOnAction((ActionEvent event) -> {
            menuStage.close();
            receipt.showReceipt(selectionsList, totalCharges);
        });

        menuGroup.getChildren().add(checkOutButton);
    }

    /**
     * Sets the layout of the scroll bar
     */
    private void setScrollBar() {
        scrollPane.setContent(menuGroup);
        scrollPane.setId("scroll");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    /**
     * Sets the layout of the menu window
     */
    private void setMenuStage() {
        RoyalBakery.readConstantsFile(RoyalBakery.locationLanguageProperty);

        menuScene.getStylesheets().add(BakeryMenu.class.getResource("CSSstyle.css").toExternalForm());

        menuStage.setTitle(RoyalBakery.languageProperties.getProperty("MENU_WINDOW_NAME"));
        menuStage.setScene(menuScene);
        menuStage.show();
    }
}
