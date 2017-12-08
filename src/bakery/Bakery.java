package Bakery;

import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Main class for Bakery
 * @author Yesly
 */
public class Bakery extends Application {

    //data fields
    private ImageView royalImageView;
    private ImageView arrowImageView;
    public static final int WIDTH = 700;
    public static final int HEIGHT = 500;
    private final int royalDX = -1;
    private final int arrowDX = 1;
    double totalCharges = 0.00;

    @Override
    public void start(Stage primaryStage) {
        royalImageView = new ImageView(new Image("Roya.png"));
        royalImageView.setTranslateX(WIDTH / 2 - royalImageView.getFitWidth() / 2);//initial x
        royalImageView.setViewport(new Rectangle2D(0, 0, 1000, 1000));//viewport set

        arrowImageView = new ImageView(new Image("arrow.png"));
        arrowImageView.setTranslateX(WIDTH / 3);//initial x
        arrowImageView.setTranslateY(440);
        arrowImageView.setViewport(new Rectangle2D(0, 0, 1000, 1000));//viewport set

        //button to take you to menu
        Button menuButton = new Button();
        menuButton.setText("MENU");
        menuButton.setTranslateX(WIDTH / 2);
        menuButton.setTranslateY(450);
        menuButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                List<Food> menuList = new ArrayList();
                Menu menu = new Menu(menuList);
                menu.showMenu();
                //showMenuScreen();
                primaryStage.close();
            }
        });
        //layout of first window
        Group group = new Group();
        group.getChildren().add(royalImageView);
        group.getChildren().add(arrowImageView);
        group.getChildren().add(menuButton);

        Scene scene = new Scene(group, 700, 500);//width: 400 pixels, height: 350 pixels

        primaryStage.setTitle("Bakery");
        primaryStage.setScene(scene);
        //primaryStage.setScene(scene1);
        primaryStage.show();
        startRoyalLoop();
        startArrowLoop();
    }

    private void startRoyalLoop() {
        //game loop
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);//runs forever
        timeline.setAutoReverse(false);//The Animation does not reverse direction on alternating cycles.
        Duration durationBetweenFrames = Duration.millis(1000 / 60);// => 60 frames per second
        EventHandler onFinished = (EventHandler<ActionEvent>) (ActionEvent t) -> {
            royalImageView.setTranslateX(royalImageView.getTranslateX() + royalDX);
            if (royalImageView.getTranslateX() < 0) {
                royalImageView.setTranslateX(WIDTH);
            }
        };

        KeyFrame keyframe = new KeyFrame(durationBetweenFrames, onFinished);
        timeline.getKeyFrames().add(keyframe);
        timeline.play();
    }

    private void startArrowLoop() {
        Timeline timeline1 = new Timeline();
        timeline1.setCycleCount(Animation.INDEFINITE);//runs forever
        timeline1.setAutoReverse(false);//The Animation does not reverse direction on alternating cycles.
        Duration durationBetweenFrames = Duration.millis(1000 / 60);// => 60 frames per second
        EventHandler onFinished = (EventHandler<ActionEvent>) (ActionEvent t) -> {
            arrowImageView.setTranslateX(arrowImageView.getTranslateX() + arrowDX);

            if (arrowImageView.getTranslateX() > 300) {
                arrowImageView.setTranslateX(WIDTH / 3);
            }
        };

        KeyFrame keyframe = new KeyFrame(durationBetweenFrames, onFinished);
        timeline1.getKeyFrames().add(keyframe);
        timeline1.play();

    }


    public void showReceiptScreen() {
        Stage receiptStage = new Stage();

        Text showCharges = new Text();
        showCharges.setText("Charges: " + toString());
        showCharges.setTranslateX(50);
        showCharges.setTranslateY(50);
        VBox vbox = new VBox(showCharges);

        //layout of third window
        Scene scene = new Scene(vbox, 400, 400);
        receiptStage.setTitle("Receipt");
        receiptStage.setScene(scene);
        receiptStage.show();
    }

    public String toString() {
        return "$" + totalCharges;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
