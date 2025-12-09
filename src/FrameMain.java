import javafx.application.Application; 
import javafx.scene.Group; 
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.media.Media;  
import javafx.scene.media.MediaPlayer;  
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class FrameMain extends Application { 

    // Abstraction
    public static MediaPlayer mediaPlayer;  // Background music accessible from other classes
    private MediaPlayer clickSoundPlayer;   // Button click sound

    @Override     
    public void start(Stage primaryStage) throws Exception {            

        Group group = new Group(); 
        Scene scene = new Scene(group, 1950, 1000); 

        // Background GIF
        Image mainscreenImage = new Image(getClass().getResource("/v.GIF").toExternalForm());
        ImageView mainscreen = new ImageView(mainscreenImage);
        mainscreen.setFitWidth(1950);
        mainscreen.setFitHeight(1000);
        mainscreen.setPreserveRatio(false); 
        group.getChildren().add(mainscreen);

        // Buttons
        Button startButton = new Button("Start Game");
        startButton.setLayoutX(850);
        startButton.setLayoutY(690);

        Button exitButton = new Button("Exit");
        exitButton.setLayoutX(920);
        exitButton.setLayoutY(790);

        // Load font
        Font orbitron = Font.loadFont(getClass().getResource("/Orbitron/Orbitron-VariableFont_wght.ttf").toExternalForm(), 60);
        startButton.setFont(orbitron);
        exitButton.setFont(orbitron);

        // Button styles
        String startNormal = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 40px; -fx-font-weight: bold; -fx-background-radius: 20;";
        String startHover  = "-fx-background-color: #45a049; -fx-text-fill: white; -fx-font-size: 40px; -fx-font-weight: bold; -fx-background-radius: 20;";
        String exitNormal  = "-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 40px; -fx-font-weight: bold; -fx-background-radius: 20;";
        String exitHover   = "-fx-background-color: #e53935; -fx-text-fill: white; -fx-font-size: 40px; -fx-font-weight: bold; -fx-background-radius: 20;";

        startButton.setStyle(startNormal);
        exitButton.setStyle(exitNormal);

        // Hover effects
        startButton.setOnMouseEntered(_ -> startButton.setStyle(startHover));
        startButton.setOnMouseExited(_ -> startButton.setStyle(startNormal));
        exitButton.setOnMouseEntered(_ -> exitButton.setStyle(exitHover));
        exitButton.setOnMouseExited(_ -> exitButton.setStyle(exitNormal));

        // Load click sound
        try {
            String clickSoundFile = getClass().getResource("/button.mp3").toURI().toString();
            clickSoundPlayer = new MediaPlayer(new Media(clickSoundFile));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Button actions (Polymorphism)
        startButton.setOnAction(_ -> {
            playClickSound();
            restartBackgroundMusic(); // Restart music whenever game starts
            Character character = new Character();
            character.showCharacter(primaryStage);
        });

        exitButton.setOnAction(_ -> {
            playClickSound();
            System.exit(0);
        });

        group.getChildren().addAll(startButton, exitButton);

        // Start background music initially
        restartBackgroundMusic();

        // Stop music on close
        primaryStage.setOnCloseRequest(_ -> {
            if (mediaPlayer != null) mediaPlayer.stop();
        });

        primaryStage.setTitle("Quizman: BukSU Edition"); 
        primaryStage.setScene(scene); 
        primaryStage.show(); 
    }    

    // Play click sound
    private void playClickSound() {
        if (clickSoundPlayer != null) {
            clickSoundPlayer.stop();
            clickSoundPlayer.play();
        }
    }

    // Background music control from other classes
    public static void stopBackgroundMusic() {
        if (mediaPlayer != null) mediaPlayer.stop();
    }

    
  
    
    
    public static void fadeOutBackgroundMusic(double durationSeconds) {
        if (mediaPlayer == null) return;
        Timeline fadeOut = new Timeline(
            new KeyFrame(Duration.seconds(0),
                new KeyValue(mediaPlayer.volumeProperty(), mediaPlayer.getVolume())),
            new KeyFrame(Duration.seconds(durationSeconds),
                new KeyValue(mediaPlayer.volumeProperty(), 0))
        );
        fadeOut.setOnFinished(_ -> mediaPlayer.stop());
        fadeOut.play();
    }

    // New method to restart music
    public static void restartBackgroundMusic() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
            String musicFile = FrameMain.class.getResource("/Background Music.mp3").toURI().toString();
            mediaPlayer = new MediaPlayer(new Media(musicFile));
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setVolume(0.3);
            mediaPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {  
        launch(args);     
    }         
}
