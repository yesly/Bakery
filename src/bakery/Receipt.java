package bakery;

import java.text.DecimalFormat;
import java.util.List;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Opens Receipt window
 *
 * @author Yesly Rodriguez
 * @author Yeilys Fundora
 */
public class Receipt {

    Stage receiptStage = new Stage();
    Group receiptGroup = new Group();
    DecimalFormat numberFormat = new DecimalFormat("#.00");
    static int WIDTH = 100;

    /**
     * Calls methods that create the Receipt window
     *
     * @param selectionsList list of the foods the user selected
     * @param totalCharges sum of all the prices in the selectionsList
     */
    public void showReceipt(List<Food> selectionsList, double totalCharges) {
        double taxes = 0.00;

        RoyalBakery.readConstantsFile(RoyalBakery.locationLanguageProperty);

        setRoyal2Image();
        taxes = printTaxes(totalCharges, taxes);
        totalCharges = printTotalCharges(totalCharges, taxes);
        showSlectionsText();
        printSelections(selectionsList);
        createDiscountHBox(totalCharges);
    }

    /**
     * Sets the layout of the Royal2 image
     */
    private void setRoyal2Image() {
        ImageView royal2ImageView = new ImageView(new Image("Royal2.png"));
        royal2ImageView.setViewport(new Rectangle2D(0, 0, 400, 400));
        receiptGroup.getChildren().add(royal2ImageView);
    }

    /**
     * Prints the tax amount
     *
     * @param totalCharges the sum of all of the user selections
     */
    private double printTaxes(double totalCharges, double taxes) {
        taxes = (totalCharges * 0.07);

        Text taxesText = new Text(RoyalBakery.languageProperties.getProperty("TAXES") + numberFormat.format(taxes));
        taxesText.setTranslateX(WIDTH);
        taxesText.setTranslateY(170);
        receiptGroup.getChildren().add(taxesText);

        return taxes;
    }

    /**
     * Prints the total charges
     *
     * @param totalCharges the sum of all of the user selections
     */
    private double printTotalCharges(double totalCharges, double taxes) {
        totalCharges += taxes;

        Text charges = new Text(RoyalBakery.languageProperties.getProperty("TOTAL") + numberFormat.format(totalCharges));
        charges.setTranslateX(WIDTH);
        charges.setTranslateY(150);
        receiptGroup.getChildren().add(charges);
        return totalCharges;
    }

    /**
     * Shows a message telling the user that the following text are his
     * selections
     */
    private void showSlectionsText() {
        Text selectionsText = new Text(RoyalBakery.languageProperties.getProperty("SELECTIONS_MESSAGE"));
        selectionsText.setTranslateX(10);
        selectionsText.setTranslateY(190);
        receiptGroup.getChildren().add(selectionsText);
    }

    /**
     * Prints the selections of the user
     *
     * @param selectionsList list of the selections
     */
    private void printSelections(List<Food> selectionsList) {
        int selectionsHeights = 190;

        for (int i = 0; i < selectionsList.size(); i++) {
            Text selection = new Text(selectionsList.get(i).getFoodName() + " - " + selectionsList.get(i).getFoodQuantity() + " - $"
                    + numberFormat.format(selectionsList.get(i).getFoodPrice() * selectionsList.get(i).getFoodQuantity()));
            selection.setTranslateX(WIDTH);
            selection.setTranslateY(selectionsHeights);
            receiptGroup.getChildren().add(selection);

            selectionsHeights += 20;
        }

        setCupcakeBarCodeImage(selectionsHeights);
    }

    /**
     * Sets the layout of the cupcakeBarCode image
     *
     * @param selectionsHeights
     */
    private void setCupcakeBarCodeImage(int selectionsHeights) {
        ImageView cupcakeBarCodeImageView = new ImageView(new Image("cupcakeBarCode.jpg"));
        cupcakeBarCodeImageView.setViewport(new Rectangle2D(0, 0, 170, 170));
        cupcakeBarCodeImageView.setTranslateX(WIDTH);
        cupcakeBarCodeImageView.setTranslateY(selectionsHeights + 10);

        receiptGroup.getChildren().add(cupcakeBarCodeImageView);
        setReceiptStage(selectionsHeights);
    }

    /**
     * Creates an HBox to hold the discountLabel, discountTextField, and the
     * addButton;
     */
    private void createDiscountHBox(double totalCharges) {
        Label discountLabel = new Label(RoyalBakery.languageProperties.getProperty("REMEMBER_CODE"));
        TextField discountTextField = new TextField();
        Button addButton = createAddButton(discountTextField, totalCharges);

        HBox discountHBox = new HBox();
        discountHBox.setTranslateX(10);
        discountHBox.setTranslateY(100);
        discountHBox.getChildren().addAll(discountLabel, discountTextField, addButton);
        receiptGroup.getChildren().add(discountHBox);
    }

    /**
     * Creates a button to add the user input into a variable and check if it is
     * correct
     *
     * @param discountTextField user input
     * @return button
     */
    private Button createAddButton(TextField discountTextField, double totalCharges) {
        Button addButton = new Button(RoyalBakery.languageProperties.getProperty("SUBMIT_BUTTON"));
        addButton.setId("submit");
        addButton.setOnAction((ActionEvent event1) -> {
            double userDiscountCode = Double.parseDouble(discountTextField.getText());

            if (!discountTextField.getText().isEmpty() && userDiscountCode == RoyalBakery.discountCode) {
                setDiscountImage();
                showDiscountedTotal(totalCharges);
            } else {
                createInvalidCodeLabel();
            }
        });

        return addButton;
    }

    /**
     * Creates a label telling the user that the discount code he inserted is
     * invalid
     */
    private void createInvalidCodeLabel() {
        Label invalidCodeLabel = new Label(RoyalBakery.languageProperties.getProperty("NOT_THE_CODE"));
        invalidCodeLabel.setTranslateX(200);
        invalidCodeLabel.setTranslateY(140);
        PauseTransition visiblePause = new PauseTransition(
                Duration.seconds(0.8)
        );
        visiblePause.setOnFinished((ActionEvent) -> {
            invalidCodeLabel.setVisible(false);
        });
        visiblePause.play();
        receiptGroup.getChildren().add(invalidCodeLabel);
    }

    /**
     * Shows the discounted total
     */
    private void showDiscountedTotal(double totalCharges) {
        double discountedAmount = totalCharges * 0.2;
        double newTotal = totalCharges - discountedAmount;
        
        Text newTotalText = new Text(RoyalBakery.languageProperties.getProperty("NEW_TOTAL") + numberFormat.format(newTotal));
        newTotalText.setTranslateX(180);
        newTotalText.setTranslateY(150);
        receiptGroup.getChildren().add(newTotalText);
    }

    /**
     * Sets the layout of the discountedAmount image
     */
    private void setDiscountImage() {
        ImageView discountImageView = new ImageView(new Image("20_percent_off.png"));
        discountImageView.setViewport(new Rectangle2D(0, 0, 70, 70));
        discountImageView.setTranslateX(300);
        discountImageView.setTranslateY(130);
        receiptGroup.getChildren().add(discountImageView);
    }

    /**
     * Sets the layout of the Receipt window
     */
    private void setReceiptStage(int height) {
        height += 200;
        
        Scene scene = new Scene(receiptGroup, 400, height);
        scene.getStylesheets().add(Receipt.class.getResource("CSSstyle.css").toExternalForm());
        
        receiptStage.setTitle(RoyalBakery.languageProperties.getProperty("RECEIPT_WINDOW_NAME"));
        receiptStage.setScene(scene);
        receiptStage.show();
    }
}
