package bakery;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Main class for RoyalBakery
 *
 * @author Yesly Rodriguez
 * @author Yeilys Fundora
 */
public class RoyalBakery extends Application {

    Stage primaryStage;
    Group group = new Group();
    Timeline arrowTimeline = new Timeline();

    List<Food> menuList = new ArrayList();
    List<Food> selectionsList = new ArrayList<>();
    BakeryMenu menu;

    double totalCharges = 0.00;
    public static int discountCode;

    public static Properties languageProperties = new Properties();
    public static String locationLanguageProperty = "resources\\englishConstants.properties"; //Default language

    private ImageView royalImageView;
    private final ImageView arrowImageView = new ImageView(new Image("Arrow.png"));
    public static final int WIDTH = 700;
    public static final int HEIGHT = 500;
    public static final int INITIAL_ARROW_WIDTH = 510;

    public RoyalBakery() {
        this.menu = new BakeryMenu(menuList, selectionsList);
    }

    /**
     * Calls methods that create the first window
     *
     * @param primaryStage stage of the RoyalBakery window
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        setRoyalImage();
        startRoyalLoop();
        setSaveImage();
        printDiscountCode();
        createEnglishLanguageButton();
        createSpanishLanguageButton();
        layWindow();
    }

    /**
     * Sets the layout of the Royal image
     */
    private void setRoyalImage() {
        royalImageView = new ImageView(new Image("Royal.png"));
        royalImageView.setTranslateX(WIDTH / 2 - royalImageView.getFitWidth() / 2);//initial x
        royalImageView.setViewport(new Rectangle2D(0, 0, 1000, 1000));//viewport set
        group.getChildren().add(royalImageView);
    }

    /**
     * Creates a loop to move the Royal Image across the screen
     */
    private void startRoyalLoop() {
        final int ROYAL_DX = -1;

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.setAutoReverse(false); //The Animation does not reverse direction on alternating cycles.

        Duration durationBetweenFrames = Duration.millis(1000 / 60);
        EventHandler onFinished = (EventHandler<ActionEvent>) (ActionEvent t) -> {
            royalImageView.setTranslateX(royalImageView.getTranslateX() + ROYAL_DX);

            if (royalImageView.getTranslateX() < 0) {
                royalImageView.setTranslateX(WIDTH);
            }
        };

        KeyFrame keyframe = new KeyFrame(durationBetweenFrames, onFinished);
        timeline.getKeyFrames().add(keyframe);
        timeline.play();
    }

    /**
     * Sets the layout of the save image
     */
    public void setSaveImage() {
        ImageView saveImageView = new ImageView(new Image("save.png"));
        saveImageView.setViewport(new Rectangle2D(0, 0, 100, 130));
        saveImageView.setTranslateX(600);
        saveImageView.setTranslateY(380);
        group.getChildren().add(saveImageView);
    }

    /**
     * Sets the layout of the DiscountCode image
     */
    private void setDiscountCodeImage() {
        ImageView discountCodeImageView = new ImageView(new Image("DiscountCode.jpg"));
        discountCodeImageView.setViewport(new Rectangle2D(0, 0, 200, 200));
        discountCodeImageView.setTranslateX(450);
        discountCodeImageView.setTranslateY(350);
        group.getChildren().add(discountCodeImageView);
    }

    /**
     * Sets the layout of the CodigoDeDescuento image
     */
    private void setCodigoDeDescuentoImage() {
        ImageView codigoDeDescuentoImageView = new ImageView(new Image("CodigoDeDescuento.jpg"));
        codigoDeDescuentoImageView.setViewport(new Rectangle2D(0, 0, 200, 200));
        codigoDeDescuentoImageView.setTranslateX(450);
        codigoDeDescuentoImageView.setTranslateY(350);
        group.getChildren().add(codigoDeDescuentoImageView);
    }

    /**
     * Prints the discount code
     */
    private void printDiscountCode() {
        discountCode = createRandomCode();

        Label discountLabel = new Label("# " + discountCode);
        discountLabel.setTranslateX(500);
        discountLabel.setTranslateY(425);
        discountLabel.setFont(Font.font("serif", FontWeight.BOLD, 20));
        group.getChildren().add(discountLabel);
    }

    /**
     * Creates a random number for the discount code
     *
     * @return the discount code
     */
    private int createRandomCode() {
        Random random = new Random();
        int randomDiscountCode = random.nextInt(1000);
        return randomDiscountCode;
    }

    /**
     * Creates the button to make the comments in the applications appear on
     * English
     */
    private void createEnglishLanguageButton() {
        ImageView imageView = new ImageView("usa.png");
        imageView.setFitWidth(30);
        imageView.setFitHeight(20);

        Button englishLanguageButton = new Button();
        englishLanguageButton.setId("english");
        englishLanguageButton.setGraphic(imageView);
        englishLanguageButton.setTranslateX(300);
        englishLanguageButton.setTranslateY(400);
        englishLanguageButton.setOnAction((ActionEvent event) -> {
            locationLanguageProperty = "resources\\englishConstants.properties";
            readConstantsFile(locationLanguageProperty);
            setHelloFriendImage();
            createMenuButton();
            setArrowImage(group);
            startArrowLoop();
            setDiscountCodeImage();
        });

        if (!group.getChildren().contains(englishLanguageButton)) {
            group.getChildren().add(englishLanguageButton);
        }
    }

    /**
     * Sets the layout of the HelloFriend image
     */
    private void setHelloFriendImage() {
        ImageView helloFriendImageView = new ImageView(new Image("HelloFriend.png"));
        helloFriendImageView.setTranslateX(0);
        helloFriendImageView.setTranslateY(350);
        helloFriendImageView.setViewport(new Rectangle2D(0, 0, 300, 300));
        group.getChildren().add(helloFriendImageView);
    }

    /**
     * Creates the button to make the comments in the applications appear on
     * Spanish
     */
    private void createSpanishLanguageButton() {
        ImageView imageView = new ImageView("spain.png");
        imageView.setFitWidth(30);
        imageView.setFitHeight(20);

        Button spanishLanguageButton = new Button();
        spanishLanguageButton.setId("spanish");
        spanishLanguageButton.setGraphic(imageView);
        spanishLanguageButton.setTranslateX(380);
        spanishLanguageButton.setTranslateY(400);
        spanishLanguageButton.setOnAction((ActionEvent event) -> {
            locationLanguageProperty = "resources\\spanishConstants.properties";
            readConstantsFile(locationLanguageProperty);
            setHolaAmigoImage();
            createMenuButton();
            setArrowImage(group);
            startArrowLoop();
            setCodigoDeDescuentoImage();
        });

        if (!group.getChildren().contains(spanishLanguageButton)) {
            group.getChildren().add(spanishLanguageButton);
        }
    }

    /**
     * Sets the layout of the HolaAmigo image
     */
    private void setHolaAmigoImage() {
        ImageView holaAmigoImageView = new ImageView(new Image("HolaAmigo.png"));
        holaAmigoImageView.setTranslateX(0);
        holaAmigoImageView.setTranslateY(350);
        holaAmigoImageView.setViewport(new Rectangle2D(0, 0, 300, 300));
        group.getChildren().add(holaAmigoImageView);
    }

    /**
     * Reads language file of locationLanguageProperty
     *
     * @param locationLanguageProperty path location of the language file
     */
    public static void readConstantsFile(String locationLanguageProperty) {
        languageProperties = new Properties();
        FileReader reader;

        try {
            reader = new FileReader(locationLanguageProperty);
            try {
                languageProperties.load(reader);
            } catch (IOException ex) {
                System.out.println("Error: " + ex);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Error: " + ex);
        }
    }

    /**
     * Sets the layout of the Arrow image
     */
    private void setArrowImage(Group group) {
        arrowImageView.setTranslateX(INITIAL_ARROW_WIDTH);
        arrowImageView.setTranslateY(550);
        arrowImageView.setViewport(new Rectangle2D(0, 0, 1000, 1000));

        if (!group.getChildren().contains(arrowImageView)) {
            group.getChildren().add(arrowImageView);
        }
    }

    /**
     * Creates a loop to make the Arrow image point towards the Menu button
     */
    private void startArrowLoop() {
        final int ARROW_DX = 1;

        if (arrowTimeline.getKeyFrames().isEmpty()) {
            arrowTimeline.setCycleCount(Animation.INDEFINITE);
            arrowTimeline.setAutoReverse(false);//The Animation does not reverse direction on alternating cycles.

            Duration durationBetweenFrames = Duration.millis(1000 / 60);
            EventHandler onFinished = (EventHandler<ActionEvent>) (ActionEvent t) -> {
                arrowImageView.setTranslateX(arrowImageView.getTranslateX() + ARROW_DX);

                if (arrowImageView.getTranslateX() > 550) {
                    arrowImageView.setTranslateX(INITIAL_ARROW_WIDTH);
                }
            };

            KeyFrame keyframe = new KeyFrame(durationBetweenFrames, onFinished);
            arrowTimeline.getKeyFrames().add(keyframe);
            arrowTimeline.play();
        }
    }

    /**
     * Creates the button to take user to the menu window
     *
     */
    private void createMenuButton() {
        Button menuButton = new Button(languageProperties.getProperty("MENU_BUTTON"));
        menuButton.setId("menu");
        menuButton.setTranslateX(600);
        menuButton.setTranslateY(560);
        menuButton.setOnAction((ActionEvent) -> {
            menu.showMenu();
            primaryStage.close();
        });
        group.getChildren().add(menuButton);
    }

    /**
     * Sets the layout of the first window
     */
    private void layWindow() {
        readConstantsFile(locationLanguageProperty);

        Scene scene = new Scene(group, 700, 600);
        scene.getStylesheets().add(RoyalBakery.class.getResource("CSSstyle.css").toExternalForm());

        primaryStage.setTitle(languageProperties.getProperty("BAKERY_WINDOW_NAME"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Main method for the program
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
