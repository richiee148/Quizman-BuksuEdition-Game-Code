import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class StoryLine {

    private String department;
    private AudioClip clickSound;  
    private AudioClip typingClip;          

    public void showStory(Stage stage, String department) {
        this.department = department.toLowerCase();
        Group storyGroup = new Group();

        // Load typing sound
        try {
            typingClip = new AudioClip(getClass().getResource("/typing.mp3").toExternalForm());
            typingClip.setVolume(0.6);
        } catch (Exception e) {
            System.err.println("Typing sound not found!");
        }

        // Load click sound
        try {
            clickSound = new AudioClip(getClass().getResource("/button.mp3").toExternalForm());
            clickSound.setVolume(0.6);
        } catch (Exception e) {
            System.err.println("Click sound not found!");
        }

        // Background
        try {
            Image backgroundImage = new Image(getClass().getResource("/plibrarypic.png").toExternalForm());
            ImageView backgroundView = new ImageView(backgroundImage);
            backgroundView.setFitWidth(1950);
            backgroundView.setFitHeight(1000);
            storyGroup.getChildren().add(backgroundView);
        } catch (Exception e) {
            System.err.println("Background image not found!");
        }

        // Overlay
        Rectangle overlay = new Rectangle(1950, 1000, Color.rgb(0, 0, 0, 0.4));

        // Department-specific story
        String storyMessage = switch (this.department) {
            case "cot" -> "As a COT student, your journey begins in the world of innovation and technology. Your mission is to complete the challenge about BukSU.";
            case "con" -> "As a CON student, you bring care and compassion. Your challenge is to finish the game.";
            case "cob" -> "As a COB student, you understand the value of strategy and leadership. Your task is to balance knowledge and skills to win the game.";
            case "cpag" -> "As a CPAG student, you carry the spirit of governance and service. You’ll make decisions that shape the university’s legacy.";
            case "cas" -> "As a CAS student, your curiosity knows no bounds. You explore and uncover hidden truths about Bukidnon State University.";
            case "coe" -> "As a COE student, you inspire and guide others. Your mission is to spread knowledge and shape future minds.";
            default -> "Welcome to BukSU — your adventure begins now!";
        };

        Text storyText = new Text();
        storyText.setLayoutX(250);
        storyText.setLayoutY(300);
        storyText.setWrappingWidth(1300);
        try {
            Font orbitron = Font.loadFont(getClass().getResource("/Orbitron-VariableFont_wght.ttf").toExternalForm(), 40);
            storyText.setFont(orbitron);
        } catch (Exception e) {
            System.err.println("Font not found!");
        }
        storyText.setFill(Color.WHITE);

        // Typing animation
        Timeline deptTimeline = new Timeline();
        int delayPerCharMs = 60; 
        for (int i = 1; i <= storyMessage.length(); i++) {
            final int charIndex = i;
            KeyFrame keyFrame = new KeyFrame(Duration.millis(delayPerCharMs * charIndex), _ -> {
                storyText.setText(storyMessage.substring(0, charIndex));
                if (typingClip != null && storyMessage.charAt(charIndex - 1) != ' ' && !typingClip.isPlaying()) {
                    typingClip.play();
                }
            });
            deptTimeline.getKeyFrames().add(keyFrame);
        }
        deptTimeline.setOnFinished(_ -> { if (typingClip != null) typingClip.stop(); });
        deptTimeline.play();

        // Continue button
        Button continueButton = new Button("Continue");
        continueButton.setLayoutX(900);
        continueButton.setLayoutY(850);
        continueButton.setStyle("-fx-background-color: #F5F5DC; -fx-text-fill: black; -fx-font-size: 25px; -fx-font-weight: bold;-fx-background-radius: 10;");
        continueButton.setOnAction(_ -> {
            deptTimeline.stop();
            if (typingClip != null) typingClip.stop();
            playClickSound();
            showFullIntro(stage);
        });

        storyGroup.getChildren().addAll(overlay, storyText, continueButton);
        Scene storyScene = new Scene(storyGroup, 1950, 1000);
        stage.setScene(storyScene);
    }

    private void showFullIntro(Stage stage) {
        Group gameGroup = new Group();

        // Background Image
        try {
            Image backgroundImage = new Image(getClass().getResource("/library_4.png").toExternalForm());
            ImageView backgroundView = new ImageView(backgroundImage);
            backgroundView.setFitWidth(1950);
            backgroundView.setFitHeight(1000);
            gameGroup.getChildren().add(backgroundView);
        } catch (Exception e) {
            System.err.println("Background image not found!");
        }

        // Dark overlay
        Rectangle darkOverlay = new Rectangle(1950, 1000);
        darkOverlay.setFill(Color.color(0, 0, 0, 0.6));

        // Game text
        Text gameText = new Text();
        gameText.setLayoutX(150);
        gameText.setLayoutY(150);
        gameText.setWrappingWidth(1600);
        try {
            Font orbitron = Font.loadFont(getClass().getResource("/Orbitron-VariableFont_wght.ttf").toExternalForm(), 40);
            gameText.setFont(orbitron);
        } catch (Exception e) {
            System.err.println("Font not found!");
        }
        gameText.setFill(Color.WHITE);

        // Continue button
        Button continueButton = new Button("Continue");
        continueButton.setLayoutX(900);
        continueButton.setLayoutY(850);
        continueButton.setStyle("-fx-background-color: #F5F5DC; -fx-text-fill: black; -fx-font-size: 25px; -fx-font-weight: bold;-fx-background-radius: 10;");

        gameGroup.getChildren().addAll(darkOverlay, gameText, continueButton);
        Scene gameScene = new Scene(gameGroup, 1950, 1000);
        stage.setScene(gameScene);

        // Full intro typing animation
        String fullText = "Hey, BukSUans! Do you know the rich history of our beloved university?\n"
                + "Join us for an exciting adventure in BukSu Library!\n"
                + "Explore hidden corners, meet fascinating historical figures, and take fun trivia quizzes\n"
                + "to uncover the amazing stories of Bukidnon State University.\n\n"
                + "It’s time to test your knowledge and show your BukSU pride.\n"
                + "Are you ready to become a history hero and discover what makes BukSu special?\n"
                + "Let’s dive in and have some fun!";

        Timeline timeline = new Timeline();
        int delayPerCharMs = 40; 
        for (int i = 1; i <= fullText.length(); i++) {
            final int charIndex = i;
            KeyFrame keyFrame = new KeyFrame(Duration.millis(delayPerCharMs * charIndex), _ -> {
                gameText.setText(fullText.substring(0, charIndex));
                if (typingClip != null && fullText.charAt(charIndex - 1) != ' ' && !typingClip.isPlaying()) {
                    typingClip.play();
                }
            });
            timeline.getKeyFrames().add(keyFrame);
        }
        timeline.setOnFinished(_ -> { if (typingClip != null) typingClip.stop(); });
        timeline.play();

        continueButton.setOnAction(_ -> {
            timeline.stop();
            if (typingClip != null) typingClip.stop();
            playClickSound();
            ActualGame Agame = new ActualGame();
            Agame.goGame(stage, department);
        });
    }

    private void playClickSound() {
        if (clickSound != null) clickSound.play();
    }
}
