import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;
import javafx.scene.shape.Rectangle;
import javafx.scene.media.AudioClip;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

public class Character {
    private AudioClip clickSound;  
    private AudioClip typingClip;          

    public void showCharacter(Stage stage) {
        Group group = new Group();

        // Background
        try {
            Image backgroundImage = new Image(getClass().getResource("/plibrarypic.png").toExternalForm());
            ImageView backgroundView = new ImageView(backgroundImage);
            backgroundView.setFitWidth(1950);
            backgroundView.setFitHeight(1000);
            backgroundView.setPreserveRatio(false);
            group.getChildren().add(backgroundView);
        } catch (Exception e) {
            System.err.println("Background image not found!");
            e.printStackTrace();
        }

        // Title text for typing animation
        Text title = new Text("");
        title.setLayoutX(230);
        title.setLayoutY(250);
        try {
            Font orbitron = Font.loadFont(getClass().getResource("/Orbitron-VariableFont_wght.ttf").toExternalForm(), 70);
            title.setFont(orbitron);
        } catch (Exception e) {
            System.err.println("Font not found!");
        }
        title.setFill(Color.WHITE);
        title.setEffect(new DropShadow(5, Color.BLACK));

        // Title background rectangle
        Rectangle titleBackground = new Rectangle(220, 200, 0, 80);
        titleBackground.setFill(Color.rgb(0, 0, 0, 0.5));
        titleBackground.setArcWidth(20);
        titleBackground.setArcHeight(20);
        group.getChildren().addAll(titleBackground, title);

        // Typing sound
        try {
            typingClip = new AudioClip(getClass().getResource("/typing.mp3").toExternalForm());
            typingClip.setVolume(0.6);
        } catch (Exception e) {
            System.err.println("Typing sound not found!");
        }

        // Typing animation
        String fullText = "Choose Your Character (Department)";
        Timeline typingTimeline = new Timeline();
        int delayPerCharMs = 80;

        for (int i = 1; i <= fullText.length(); i++) {
            final int charIndex = i;
            KeyFrame keyFrame = new KeyFrame(Duration.millis(delayPerCharMs * charIndex), _ -> {
                title.setText(fullText.substring(0, charIndex));
                double textWidth = title.getBoundsInLocal().getWidth();
                titleBackground.setWidth(textWidth + 40);

                // Play typing sound only for non-space characters
                if (typingClip != null && fullText.charAt(charIndex - 1) != ' ') {
                    typingClip.play();
                }
            });
            typingTimeline.getKeyFrames().add(keyFrame);
        }

        typingTimeline.setOnFinished(_ -> {
            if (typingClip != null) typingClip.stop();
        });
        typingTimeline.play();

        // Button style
        String style = "-fx-background-color: #F5F5DC; -fx-text-fill: black; -fx-font-size: 20px; "
                + "-fx-font-weight: bold; -fx-background-radius: 10;";

        // Departments and images
        String[] departments = {"COT", "CON", "COB", "CPAG", "CAS", "COE"};
        String[] characterImages = {"/cot.gif", "/con-.gif", "/cob.gif", "/cpag.gif", "/cas.gif", "/coe.gif"};

        int x = 250, y = 680, spacing = 250;

        // Load button click sound as AudioClip
        try {
            clickSound = new AudioClip(getClass().getResource("/button.mp3").toExternalForm());
            clickSound.setVolume(0.6);
        } catch (Exception e) {
            System.err.println("Button click sound not found!");
        }

        for (int i = 0; i < departments.length; i++) {
            String dept = departments[i].toLowerCase();

            try {
                Image charImage = new Image(getClass().getResource(characterImages[i]).toExternalForm());
                ImageView charView = new ImageView(charImage);
                charView.setFitWidth(980);
                charView.setFitHeight(980);
                charView.setLayoutX(x - 485);
                charView.setLayoutY(y - 680);
                group.getChildren().add(charView);
            } catch (Exception e) {
                System.err.println("Character image not found: " + characterImages[i]);
            }

            Button btn = new Button(departments[i]);
            btn.setLayoutX(x);
            btn.setLayoutY(y);
            btn.setPrefWidth(120);
            btn.setPrefHeight(70);
            btn.setStyle(style);

            btn.setOnAction(_ -> {
                playClickSound();
                StoryLine story = new StoryLine();
                story.showStory(stage, dept);
            });

            group.getChildren().add(btn);
            x += spacing;
        }

        Scene scene = new Scene(group, 1950, 1000);
        stage.setScene(scene);
        stage.show();
    }

    private void playClickSound() {
        if (clickSound != null) {
            clickSound.play();
        }
    }
}
