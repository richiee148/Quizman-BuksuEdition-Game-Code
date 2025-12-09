
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ArrayDeque;
import java.util.Stack;

//class 
public class ActualGame {
	   private AudioClip typingClip;       // typing sound
	    private MediaPlayer clickSoundPlayer; // button click sound
	 
	    private MediaPlayer walkingPlayer;
	    private AudioClip newLevelClip;
	    private AudioClip correctClip;
	    private AudioClip wrongClip;

	    private MediaPlayer gameOverPlayer;
	    private MediaPlayer gameWonPlayer;

	    //Encapsulation 
    private double charX = 50;
    private double charY = 600;
    private ImageView movingChar;
    private Image charUp, charDown, charLeft, charRight;

    private String selectedDept;
    //polymorphism
    private List<Rectangle> collisionRectangles = new ArrayList<>();//array list for the collision rectangle 
    
 // LIFE SYSTEM
    private int lives = 3;
    private ArrayDeque<ImageView> lifeQueue = new ArrayDeque<>();//dequeue for the lives of player
    private Stack<Integer> lifeStack = new Stack<>(); //stack for the lives of the player
    
    // QUESTIONS
    private Map<Integer, List<Question>> floorQuestions = new HashMap<>(); //polymorphism 
    private int currentQuestionIndex = 0;
    private int correctStreak = 0; // track consecutive correct answers
    private boolean questionActive = false;
    
    
private LinkedList<Floor> floors = new LinkedList<>(); //linked list for the floor 
    private int currentFloor = 0; // track the current floor

    private class Question {
        String text;
        String[] choices;
        int correctIndex;

        Question(String text, String[] choices, int correctIndex) {
            this.text = text;
            this.choices = choices;
            this.correctIndex = correctIndex;
        }
    }
    
    //floor class 
 // Floor class simplified: only stores the scene builder
    private class Floor {
        Runnable sceneBuilder;

        Floor(Runnable sceneBuilder) {
            this.sceneBuilder = sceneBuilder;
        }
    }  
        // Moved initQuestions outside goGame as a proper class method
        private void initQuestions() {
            // Floor 1 questions - history of Bukidnon State University
        	
            List<Question> floor1 = new ArrayList<>();//arraylist for the floors questions
            floor1.add(new Question("What is the former name of Bukidnon State College?",
                    new String[]{"Bukidnon Provincial High School", "Bukidnon Training Institute", "Bukidnon Rural Academy", "Bukidnon Normal Institute"}, 0));
            floor1.add(new Question("In what year did Bukidnon State College start as a secondary school?",
                    new String[]{"1986", "1924", "1976", "1920"}, 1));
            floor1.add(new Question("What is the first course offered by the secondary normal curriculum?",
                    new String[]{"BSEE", "BA Education", "BSEd", "Associate in Teaching"}, 0));
            floor1.add(new Question("On June 14, 1976 Bukidnon Normal School converted into a state college called?",
                    new String[]{"Bukidnon College of Education", "Mindanao State College – Bukidnon", "Bukidnon State College", "Bukidnon Teachers University"}, 2));
            floor1.add(new Question("What is the former name of Bukidnon State College?",
                    new String[]{"Bukidnon Provincial High School", "Bukidnon Training Institute", "Bukidnon Rural Academy", "Bukidnon Normal Institute"}, 0));
            floor1.add(new Question("In what year did Bukidnon State College start as a secondary school?",
                    new String[]{"1986", "1924", "1976", "1920"}, 1));
            floor1.add(new Question("When was the school being closed because of World War II?",
                    new String[]{"December 09,1941", "December 25, 1941", "January 01, 1942", "February 14, 2025"}, 0));
            floor1.add(new Question("After World War II, the school was reactivated on what year?",
                    new String[]{"August 11, 1950", "September 01, 1945", "January 01, 1946", "December 01, 1945"}, 1));
            floor1.add(new Question("In what year does the first master’s graduate was produced?",
                    new String[]{"1970", "1975", "1957", "1999"}, 1));
            floor1.add(new Question("In what year does Bukidnon Normal School formally separated to a school divison to operate independently?",
                    new String[]{"December 15, 1961", "October 25, 2006", "June 11. 1976", "January 01, 1950"}, 0));
            Collections.shuffle(floor1); // Shuffle the list
            floorQuestions.put(0, floor1);

         // Floor 2 questions - Trivia questions
            List<Question> floor2 = new ArrayList<>();
            floor2.add(new Question("What is the official student publication of BukSU?",
                    new String[]{"StarPublish", "Collegianer", "Pitches", "Stallion"}, 1));
            floor2.add(new Question("How many colleges are currently part of BukSU?",
                    new String[]{"7", "9", "6", "8"}, 2));
            floor2.add(new Question("What is the official motto of Bukidnon State University? ",
                    new String[]{"Be Innovate and Be Smart", "Educate, Innovate, Lead", "Leadership and Skills", "Skills and Progress"}, 1));
            floor2.add(new Question("How many satellite campuses / extension campuses does BukSU have?",
                    new String[]{"16", "13", "15", "14"}, 0));
            floor2.add(new Question("What is the name of BukSU’s official yearbook publication?",
                    new String[]{"Pharos", "Matigda", "Matilda", "WriteSmart"}, 1));
            floor2.add(new Question("What is the name of BukSU's premier cultural group/choir?",
                    new String[]{"BuksU Harmonies", "Bukidnon Voices", "Bukidnon State University Chorale", "BukSu Choir"}, 2));
            floor2.add(new Question("Which BukSu radio program won a Gandingan Award for being \"Most Development-Oriented\"?",
                    new String[]{"Bukidno Voices", "BukSu Talks", "Kag-lambaga", "Pag-Ambitay Daw Ag Kaulian (“Share to Heal”) on DXBU."}, 3));
            floor2.add(new Question("Before the creation of the College of Technologies, some IT-related subjects and early computing courses were originally handled under what college? ",
                    new String[]{"College of Medicine", "College of Education", "College of Arts and Sciences", "College of Criminal Justice Education"}, 2));
            floor2.add(new Question("What are Bukidnon State University’s core values?",
                    new String[]{"Excellence, Professionalism, Integrity, Commitment, Culture-Sensitivity (EPICC)", "Excellence through Service", "Wisdom, Innovation, Trust and Teamwork", "Integrity, Passion, Innovation, Creativity"}, 0));
            floor2.add(new Question("Dr. Joy M. Mirasol is the ___ president of Bukidnon State University.",
                    new String[]{"5th", "2nd", "7th", "3rd"}, 0));
            Collections.shuffle(floor2);
            floorQuestions.put(1, floor2);

         // Floor 3 questions - people
            List<Question> floor3 = new ArrayList<>();
            floor3.add(new Question("What is the name of the current university president of BukSU?",
                    new String[]{"Dr. John Miranda", "Dr. Joy M. Mirasol", "Dr. Juan Cruz", "Dr. Jen Caruz"}, 1));
            floor3.add(new Question("Who was the second president of Bukidnon State University formerly known as Bukidnon State College?",
                    new String[]{"Dr. Teresita T. Tumapon", "Dr. Melvin Alpon", "Dr. Henry Flores", "Dr. Zalde Co"}, 0));
            floor3.add(new Question("Who Compose the University Hymn titled A Homespun Refrain?",
                    new String[]{"Miss Elma B. Teresa", "Miss Rowena F. Egargo", "Miss Sisa M. Redez", "Miss Riel D. Surin"}, 1));
            floor3.add(new Question("Who is the first president of Bukidnon State College?",
                    new String[]{"Dr. Henry", "Dr. Gellor", "Dr, Remor", "Dr. Joy Sines"}, 1));
            floor3.add(new Question("Who is the American school superintendent of Bukidnon in 1924 who established Bukidnon Provincial High School?",
                    new String[]{"William H. Peckell", "Andy Geshmens", "Riever Gloria", "Perish Jacob"}, 0));
            floor3.add(new Question("Which president introduced the “Smart BukSU” vision?",
                    new String[]{"Dr. Teresita T. Tumapon", "Dr. Joy M. Mirasol", "Dr. Marcos B. Caguioa ", "Dr. Gloria S. Fores"}, 1));
            floor3.add(new Question("Who was the third president of Bukidnon State University?",
                    new String[]{"Dr. Joy M. Mirasol", "William Peckell", "Dr. Victor M. Barroso ", "Dr. Oscar B. Cabanelez"}, 2));
            floor3.add(new Question("Who was the Philippine president who signed the RA of conversting BSC to universityhood?",
                    new String[]{"Gloria Macapagal-Arroyo", "Rodrigo Duterte", "Corazon Aquino", "Ferdinand Marcos Sr."}, 0));
            floor3.add(new Question("Who was the fourth  president of Bukidnon State University?",
                    new String[]{"Dr. Oscar B. Cabanelez", "Dr. Joy. Mirasol", "Dr. Victor M. Barroso", "Dr. Joy Sines"}, 0));
            floor3.add(new Question("Who was appointed as the school’s OIC for four months in 2023 while searching for the fifth university president?",
                    new String[]{"Ms. Edna Ethereal", "Carlita  Fortich", "Berna Tabios", "Atty. Ryan L. Estevez"}, 3));
            Collections.shuffle(floor3);
            floorQuestions.put(2, floor3);
        
            // Floor 4 questions - policies and mandate
            List<Question> floor4 = new ArrayList<>();
            floor4.add(new Question("What is the vision of Bukidnon State University?",
                    new String[]{"To be a global center for tourism and recreation", "A premier institution of innovative and ethical leaders for sustainable development", "A college focused on agriculture alone", "To train students exclusively for government service"}, 1));
            floor4.add(new Question("Which of the following is included in BukSU’s mission?",
                    new String[]{"Build theme parks for tourism", "Provide free transportation for all students", "Develop competitive professionals through quality instruction, research, extension, and production", "Limit programs only to teacher education"}, 2));
            floor4.add(new Question("According to BukSU's Quality Policy, which of the following values is emphasized to maintain educational excellence?",
                    new String[]{"Integrity", "Profitability", "Exclusivity", "Limited access"}, 0));
            floor4.add(new Question("What does BukSU aim to provide through its commitment to professionalism and integrity?",
                    new String[]{"Competent professionals", "Educational profits", "Reduced programs", "Limited student access"}, 0));
            floor4.add(new Question("What do the sheaf of palay and flask represent?",
                    new String[]{"Progress and Prosperity", "Prosper and Truth", "Wealth and Progress", "Progress and Cherish"}, 0));
            floor4.add(new Question("What type of culture does BukSU’s Mandates & Quality Policy aim for?",
                    new String[]{"Competitive", "Collaborative", "Exclusionary", "Passive"}, 1));
            floor4.add(new Question("How does BukSU’s culture-sensitivity reflect its commitment to diversity?",
                    new String[]{"Focuses on Filipino culture", "Promotes inclusivity and respect", "Limits cultural studies", "Excludes some cultural backgrounds"}, 1));
            floor4.add(new Question("What is the mission of the BukSU Research Unit?",
                    new String[]{"To focus solely on sports development", "To produce movies and media content", "To generate breakthroughs in social, behavioral, science, and technology research", "To train students in culinary arts"}, 2));
            floor4.add(new Question("Which of the following is one of the broad research areas of BukSU?",
                    new String[]{"Institutional research", "Marine engineering", "Entertainment production", "Aviation studies"}, 0));
            floor4.add(new Question("Which of the following is not part of BukSU’s mission?",
                    new String[]{"Quality instruction", "Research", "Political campaigning", "Extension"}, 2));
            Collections.shuffle(floor4);
            floorQuestions.put(3, floor4);
        }
        
        //initializing floors 
        private void initFloors(Stage stage) {
            floors.clear();
            floors.add(new Floor(() -> showFloor1(stage)));
            floors.add(new Floor(() -> showFloor2(stage)));
            floors.add(new Floor(() -> showFloor3(stage)));
            floors.add(new Floor(() -> showFloor4(stage)));
        }
        //entry point of the game 
        
        public void goGame(Stage stage, String department) {
            this.selectedDept = department.toLowerCase();
            // Initialize questions per floor
            initQuestions();  // 
            initFloors(stage);
            floors.get(0).sceneBuilder.run();//to start at floor 1
            
        Group AgameGroup = new Group();
        
        try {
            String typingSoundFile = getClass().getResource("/typing.mp3").toExternalForm();
            typingClip = new AudioClip(typingSoundFile);
            typingClip.setVolume(0.6);
        } catch (Exception e) {
        }

        // Load button click sound
        try {
            String clickSoundFile = getClass().getResource("/button.mp3").toExternalForm();
            Media clickMedia = new Media(clickSoundFile);
            clickSoundPlayer = new MediaPlayer(clickMedia);
        } catch (Exception e) {
        }

        
        Image backgroundImage = new Image(getClass().getResource("/view.jpg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(1950);
        backgroundView.setFitHeight(1000);

        Image presidentImage = new Image(getClass().getResource("/par.png").toExternalForm());
        ImageView presidentView = new ImageView(presidentImage);
        presidentView.setFitWidth(280);
        presidentView.setFitHeight(400);
        presidentView.setLayoutX(50);
        presidentView.setLayoutY(600);
        presidentView.setOpacity(0);

        Rectangle speechBox = new Rectangle(300, 700, 1600, 200);
        speechBox.setArcWidth(20);
        speechBox.setArcHeight(20);
        speechBox.setFill(Color.rgb(0, 0, 0, 0.7));
        speechBox.setOpacity(0);

        Text AgameText = new Text();
        AgameText.setX(speechBox.getX() + 20);
        AgameText.setY(speechBox.getY() + 50);
        AgameText.setWrappingWidth(speechBox.getWidth() - 40);
        Font orbitron = Font.loadFont(getClass().getResource("/Orbitron/Orbitron-VariableFont_wght.ttf").toExternalForm(), 40);
        AgameText.setFont(orbitron);
        AgameText.setFill(Color.WHITE);
        AgameText.setOpacity(0);

        String fullText = "Welcome to Bukidnon State University Library! A library that holds history, mission, and vision of BukSu. Ready yourself and complete the game Buksuan, good luck!";
        Button continueButton = new Button("Continue");
        continueButton.setLayoutX(1700);
        continueButton.setLayoutY(830);
        continueButton.setStyle("-fx-background-color: #F5F5DC; -fx-text-fill: black; -fx-font-size: 20px; -fx-font-weight: bold;-fx-background-radius: 10");
        continueButton.setOpacity(0);
        continueButton.setOnAction(_ -> {
            playClickSound();   // 🔊 play click sound
            showNextScene(stage);
        });

        AgameGroup.getChildren().addAll(backgroundView, presidentView, speechBox, AgameText, continueButton);

        Scene newScene = new Scene(AgameGroup, 1950, 1000);
        stage.setScene(newScene);
        stage.show();

        PauseTransition delay1 = new PauseTransition(Duration.seconds(1.0));
        delay1.setOnFinished(_ -> fadeIn(presidentView, 0.5));

        PauseTransition delay2 = new PauseTransition(Duration.seconds(1.0));
        delay2.setOnFinished(_ -> {
            fadeIn(speechBox, 0.5);
            PauseTransition textDelay = new PauseTransition(Duration.seconds(0.8));
            textDelay.setOnFinished(_ -> typeText(AgameText, fullText, 40));
            textDelay.play();
        });

        PauseTransition delay3 = new PauseTransition(Duration.seconds(7.0));
        delay3.setOnFinished(_ -> fadeIn(continueButton, 0.5));

        delay1.play();
        delay2.play();
        delay3.play();
    }

        private void typeText(Text textNode, String content, int delayMillis) {
            textNode.setOpacity(1);
            Timeline timeline = new Timeline();
            StringBuilder displayedText = new StringBuilder();

            for (int i = 0; i < content.length(); i++) {
                int index = i;
                KeyFrame frame = new KeyFrame(Duration.millis(delayMillis * i), _ -> {
                    if (textNode.getScene() == null) return; // scene disappeared

                    // Update text only, no sound
                    displayedText.append(content.charAt(index));
                    textNode.setText(displayedText.toString());
                });
                timeline.getKeyFrames().add(frame);
            }

            timeline.play();
        }



        private void fadeIn(javafx.scene.Node node, double durationSeconds) {
            FadeTransition fade = new FadeTransition(Duration.seconds(durationSeconds), node);
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.play();
        }

        private void playClickSound() {
            if (clickSoundPlayer != null) {
                clickSoundPlayer.stop();
                clickSoundPlayer.play();
            }
        }
       
    
        private void showNextScene(Stage stage) {
            Group nextSceneGroup = new Group();

            Image nextBg = new Image(getClass().getResource("/view.jpg").toExternalForm());
            ImageView nextBgView = new ImageView(nextBg);
            nextBgView.setFitWidth(1950);
            nextBgView.setFitHeight(1000);

            Text nextText = new Text();
            nextText.setOpacity(0);
            Font orbitron = Font.loadFont(getClass().getResource("/Orbitron/Orbitron-VariableFont_wght.ttf").toExternalForm(), 40);
            nextText.setFont(orbitron);
            nextText.setFill(Color.WHITE);
            nextText.setX(280);
            nextText.setY(350);

            String fullNextText = "Get inside the library and start your exciting journey!";

            Button enterButton = new Button("Enter the library");
            enterButton.setLayoutX(820);
            enterButton.setLayoutY(700);
            enterButton.setStyle("-fx-background-color: #F5F5DC; -fx-text-fill: black; -fx-font-size: 30px; -fx-font-weight: bold;-fx-background-radius: 10");
            enterButton.setOpacity(1);
            enterButton.setOnAction(_ -> {
                playClickSound();                          // 🔊 play click sound
                elevScene(stage);
            });

            nextSceneGroup.getChildren().addAll(nextBgView, nextText, enterButton);

            Scene nextScene = new Scene(nextSceneGroup, 1950, 1000);
            stage.setScene(nextScene);

            typeText(nextText, fullNextText, 40); // typing animation with sound
        }

        private void elevScene(Stage stage) {
            Group elevSceneGroup = new Group();

            Image elevBg = new Image(getClass().getResource("/eleve.jpg").toExternalForm());
            ImageView elevBgView = new ImageView(elevBg);
            elevBgView.setFitWidth(1950);
            elevBgView.setFitHeight(1000);

            Text elevText = new Text();
            elevText.setOpacity(0);
            Font orbitron = Font.loadFont(getClass().getResource("/Orbitron/Orbitron-VariableFont_wght.ttf").toExternalForm(), 40);
            elevText.setFont(orbitron);
            elevText.setFill(Color.WHITE);
            elevText.setX(380);
            elevText.setY(350);

            String fullelevText = "OH NO!, you got trapped in the library.\n"
                    + "In order to escape, you have to clear all the level by \nanswering questions correctly.";

            Button startChallengeButton = new Button("Start Challenge");
            startChallengeButton.setLayoutX(820);
            startChallengeButton.setLayoutY(700);
            startChallengeButton.setStyle("-fx-background-color: #F5F5DC; -fx-text-fill: black; -fx-font-size: 30px; -fx-font-weight: bold;-fx-background-radius: 10");
            startChallengeButton.setOpacity(0);
            startChallengeButton.setOnAction(_ -> {
                playClickSound();                          // 🔊 play click sound
                showStartChallengeWithMovement(stage, selectedDept);
            });

            elevSceneGroup.getChildren().addAll(elevBgView, elevText, startChallengeButton);
            Scene elevScene = new Scene(elevSceneGroup, 1950, 1000);
            stage.setScene(elevScene);

            typeText(elevText, fullelevText, 40); // typing animation with sound

            double totalTextTime = fullelevText.length() * 0.04;
            PauseTransition fadeDelay = new PauseTransition(Duration.seconds(totalTextTime + 0.5));
            fadeDelay.setOnFinished(_ -> fadeIn(startChallengeButton, 0.5));
            fadeDelay.play();
        }



private void initCharacterMovement(Group group, String dept) {
    // character starting position at the entrance
    charX = 1280;
    charY = 900;

    charUp = new Image(getClass().getResource("/" + dept + "_up.gif").toExternalForm());
    charDown = new Image(getClass().getResource("/" + dept + "_down.gif").toExternalForm());
    charLeft = new Image(getClass().getResource("/" + dept + "_left.gif").toExternalForm());
    charRight = new Image(getClass().getResource("/" + dept + "_right.gif").toExternalForm());

    movingChar = new ImageView(charDown);
    movingChar.setFitWidth(100);
    movingChar.setFitHeight(100);
    movingChar.setLayoutX(charX);
    movingChar.setLayoutY(charY);

    group.getChildren().add(movingChar);

    try {
        Media walkMedia = new Media(getClass().getResource("/walking.mp3").toExternalForm());
        walkingPlayer = new MediaPlayer(walkMedia);
        walkingPlayer.setCycleCount(MediaPlayer.INDEFINITE); // loop footsteps
        walkingPlayer.setVolume(0.5);
    } catch (Exception e) {
    }
}

private void enableMovement(Scene scene) {
    scene.setOnKeyPressed(event -> {
        double newX = charX;
        double newY = charY;

        switch (event.getCode()) {
            case W, UP -> newY -= 10;
            case S, DOWN -> newY += 10;
            case A, LEFT -> newX -= 10;
            case D, RIGHT -> newX += 10;
            default -> {}
        }

        Bounds futureCharBounds = new BoundingBox(newX, newY, movingChar.getFitWidth(), movingChar.getFitHeight());
        boolean collision = collisionRectangles.stream()
                .anyMatch(r -> r.getBoundsInParent().intersects(futureCharBounds));

        if (!collision) {
            charX = newX;
            charY = newY;
            movingChar.setLayoutX(charX);
            movingChar.setLayoutY(charY);

            switch (event.getCode()) {
                case W, UP -> movingChar.setImage(charUp);
                case S, DOWN -> movingChar.setImage(charDown);
                case A, LEFT -> movingChar.setImage(charLeft);
                case D, RIGHT -> movingChar.setImage(charRight);
                default -> {}
            }

            // 🔊 Start walking loop if not already playing
            if (walkingPlayer != null && walkingPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
                walkingPlayer.play();
            }
        }
    });

    scene.setOnKeyReleased(_ -> {
        // 🔊 Stop walking loop when key is released
        if (walkingPlayer != null) {
            walkingPlayer.stop();
        }
    });
}
    public void showStartChallengeWithMovement(Stage stage, String dept) {
        Group challengeGroup = new Group();

        // Initialize collision rectangles 
        collisionRectangles = new ArrayList<>();

        // Background
        Image tileImage = new Image(getClass().getResource("/floor1.png").toExternalForm());
        ImageView tileBackground = new ImageView(tileImage);
        tileBackground.setFitWidth(1950);
        tileBackground.setFitHeight(1000);
        tileBackground.setOpacity(0);
        challengeGroup.getChildren().add(tileBackground);
        
        // Initialize player
        initCharacterMovement(challengeGroup, dept);
        
     //ADD LIFE HEARTS 
        initializeLives(challengeGroup);
        
        // a collision rectangle 
        Rectangle wallleft = new Rectangle(20, 20, 200, 3000); // x, y, width, height
        wallleft.setVisible(false); //  invisible in game
        collisionRectangles.add(wallleft);
        challengeGroup.getChildren().add(wallleft);
        
        // a collision rectangle (replace with your own)
        Rectangle walllup = new Rectangle(20, 20, 2000, 60); // x, y, width, height
        walllup.setVisible(false); //  invisible in game
        collisionRectangles.add(walllup);
        challengeGroup.getChildren().add(walllup);
        
        // a collision rectangle (replace with your own)
        Rectangle walllright = new Rectangle(1900, 20, 100, 3000); // x, y, width, height
        walllright.setVisible(false); // invisible in game
        collisionRectangles.add(walllright);
        challengeGroup.getChildren().add(walllright);
        
        Rectangle walldown = new Rectangle(20, 870, 1250,130); // x, y, width, height
        walldown.setVisible(false); //invisible in game
        collisionRectangles.add(walldown);
        challengeGroup.getChildren().add(walldown);
        
        Rectangle walldown2 = new Rectangle(1410, 770, 700,930); // x, y, width, height
        walldown2.setVisible(false); // keep invisible in game
        collisionRectangles.add(walldown2);
        challengeGroup.getChildren().add(walldown2);
        
        Rectangle walllcenter1 = new Rectangle(1310, 70, 30, 210); // x, y, width, height
        walllcenter1.setVisible(false); // invisible in game
        collisionRectangles.add(walllcenter1);
        challengeGroup.getChildren().add(walllcenter1);
        
        Rectangle walllcenter2 = new Rectangle(460, 70, 30, 800); // x, y, width, height
        walllcenter2.setVisible(false); //invisible in game
        collisionRectangles.add(walllcenter2);
        challengeGroup.getChildren().add(walllcenter2);

        Rectangle walllcenter3 = new Rectangle(610, 90, 30, 140); // x, y, width, height
        walllcenter3.setVisible(false); // invisible in game
        collisionRectangles.add(walllcenter3);
        challengeGroup.getChildren().add(walllcenter3);
        
        Rectangle walllcenter4 = new Rectangle(680, 770, 15, 40); // x, y, width, height
        walllcenter4.setVisible(false); // invisible in game
        collisionRectangles.add(walllcenter4);
        challengeGroup.getChildren().add(walllcenter4);
        
        Rectangle shelf1 = new Rectangle(650, 190, 30, 70); // x, y, width, height
        shelf1.setVisible(false); // invisible in game
        collisionRectangles.add(shelf1);
        challengeGroup.getChildren().add(shelf1);
        
        Rectangle shelf2 = new Rectangle(830, 220, 430, 40); // x, y, width, height
        shelf2.setVisible(false); // invisible in game
        collisionRectangles.add(shelf2);
        challengeGroup.getChildren().add(shelf2);
        
        Rectangle shelf3 = new Rectangle(760, 420, 440, 40); // x, y, width, height
        shelf3.setVisible(false); // invisible in game
        collisionRectangles.add(shelf3);
        challengeGroup.getChildren().add(shelf3);
        
        Rectangle shelf4 = new Rectangle(760, 600, 440, 15); // x, y, width, height
        shelf4.setVisible(false); //  invisible in game
        collisionRectangles.add(shelf4);
        challengeGroup.getChildren().add(shelf4);
        
        Rectangle shelf5 = new Rectangle(755, 730, 390, 10); // x, y, width, height
        shelf5.setVisible(false); // invisible in game
        collisionRectangles.add(shelf5);
        challengeGroup.getChildren().add(shelf5);
        
        Rectangle shelf6 = new Rectangle(1540, 220, 400, 350); // x, y, width, height
        shelf6.setVisible(false); //  invisible in game
        collisionRectangles.add(shelf6);
        challengeGroup.getChildren().add(shelf6);
   
        //object
        Image bookImage = new Image(getClass().getResource("/book1.png").toExternalForm());

        // Define positions for each book
        double[][] bookPositions = { //array 
            {1420, 440},  // book 6
           
        };
        
        addBooksForFloor(challengeGroup, bookImage, bookPositions, 0, stage);  // Assuming floor 1
        
        // Librarian character
        Image librarianImage = new Image(getClass().getResource("/librarian.png").toExternalForm());
        ImageView librarianView = new ImageView(librarianImage);
        librarianView.setFitWidth(160);
        librarianView.setFitHeight(250);
        librarianView.setPreserveRatio(false);
        librarianView.setLayoutX(1500);
        librarianView.setLayoutY(530);
        challengeGroup.getChildren().add(librarianView);

        // Librarian collision
        double hitboxWidth = 50;
        double hitboxHeight = 70;
        double offsetX = (librarianView.getFitWidth() - hitboxWidth) / 2;
        double offsetY = librarianView.getFitHeight() - hitboxHeight - 100;

        Rectangle librarianRect = new Rectangle();
        librarianRect.xProperty().bind(librarianView.layoutXProperty().add(offsetX));
        librarianRect.yProperty().bind(librarianView.layoutYProperty().add(offsetY));
        librarianRect.widthProperty().bind(librarianView.fitWidthProperty().subtract(librarianView.getFitWidth() - hitboxWidth));
        librarianRect.heightProperty().bind(librarianView.fitHeightProperty().subtract(librarianView.getFitHeight() - hitboxHeight));
        librarianRect.setVisible(false);
        collisionRectangles.add(librarianRect);
        challengeGroup.getChildren().add(librarianRect);

       
        Scene challengeScene = new Scene(challengeGroup, 1950, 1000);
        stage.setScene(challengeScene);

        // Fade-in background then enable movement
        FadeTransition fadeBackground = new FadeTransition(Duration.seconds(1.2), tileBackground);
        fadeBackground.setFromValue(0);
        fadeBackground.setToValue(1);
        fadeBackground.play();
        fadeBackground.setOnFinished(_ -> {
        	enableMovement(challengeScene);
            addBooksForFloor(challengeGroup, bookImage, bookPositions, 0, stage); // start after fade
        });
    }
    
    private int correctAnswers = 0;  // Tracks correct answers for current floor
   
    private void addBooksForFloor(Group group, Image bookImage, double[][] positions, int floorNum, Stage stage) {
        // Fetch questions for this floor (0-based)
        List<Question> floor = floorQuestions.get(floorNum);
        correctAnswers = 0;  // Reset for new floor

        double[] pos = positions[0];
        ImageView bookView = new ImageView(bookImage);
        bookView.setFitWidth(80);
        bookView.setFitHeight(80);
        bookView.setLayoutX(pos[0]);
        bookView.setLayoutY(pos[1]);
        group.getChildren().add(bookView);

        double padding = 30;
        Rectangle bookRect = new Rectangle(
            pos[0] - padding,
            pos[1] - padding,
            bookView.getFitWidth() + padding * 2,
            bookView.getFitHeight() + padding * 2
        );
        bookRect.setFill(Color.TRANSPARENT);
        group.getChildren().add(bookRect);

        Timeline checkTouch = new Timeline();
        checkTouch.setCycleCount(Timeline.INDEFINITE);

        KeyFrame frame = new KeyFrame(Duration.millis(50), _ -> {
            if (movingChar == null || questionActive) return;

            Bounds charBounds = movingChar.getBoundsInParent();
            Bounds expandedCharBounds = new BoundingBox(
                charBounds.getMinX() - 10,
                charBounds.getMinY() - 10,
                charBounds.getWidth() + 20,
                charBounds.getHeight() + 20
            );

            if (expandedCharBounds.intersects(bookRect.getBoundsInParent())) {
                questionActive = true;
                checkTouch.stop();
                showNextQuestion(group, floor, stage); // ✅ no need to pass floor list, it’s fetched inside
            }
        });

        checkTouch.getKeyFrames().add(frame);
        checkTouch.play();
    }

  
     void showFloor1(Stage stage) {
        	
            showStartChallengeWithMovement(stage, selectedDept); // reuse your existing floor1 method
        }
    //floor2 scene 
    public void showFloor2(Stage stage) {
    
    	Group floor2Group = new Group();
    	
    	collisionRectangles= new ArrayList<>();
    	
    	currentQuestionIndex=0;
    	questionActive=false;
    	
    	//background for floor2
    	
        Image bg = new Image(getClass().getResource("/floor2.png").toExternalForm());
        ImageView bgView = new ImageView(bg);
        bgView.setFitWidth(1950);
        bgView.setFitHeight(1000);
        floor2Group.getChildren().add(bgView);
        
        initCharacterMovement(floor2Group, selectedDept);
        initializeLives(floor2Group);
        
        //rectangle collisions for floor2
        
        Rectangle wallTop2 = new Rectangle(10, 20, 1910, 440); // x-left, y-up, width, height
        wallTop2.setVisible(false);
        collisionRectangles.add(wallTop2);
        floor2Group.getChildren().add(wallTop2);
        
        Rectangle wallleft2 = new Rectangle(10, 50, 410, 1140); // x-left, y-up, width, height
        wallleft2.setVisible(false);
        collisionRectangles.add(wallleft2);
        floor2Group.getChildren().add(wallleft2);
        
        Rectangle walldown2 = new Rectangle(170, 670, 910, 620); // x-left, y-up, width, height
        walldown2.setVisible(false);
        collisionRectangles.add(walldown2);
        floor2Group.getChildren().add(walldown2);
        
        Rectangle wallright2 = new Rectangle(1590, 70, 680, 1740); // x-left, y-up, width, height
        wallright2.setVisible(false);
        collisionRectangles.add(wallright2);
        floor2Group.getChildren().add(wallright2);
        
        // Books
        Image bookImage = new Image(getClass().getResource("/book2.png").toExternalForm());
        double[][] bookPositions = {
                {420, 540},  // book 2
               
            };
        addBooksForFloor(floor2Group, bookImage, bookPositions, 1, stage);

        Scene scene = new Scene(floor2Group, 1950, 1000);
        stage.setScene(scene);
        enableMovement(scene);
        
    }
    //floor3 scene 
    private void showFloor3(Stage stage) {
    	
        Group floor3Group = new Group();

        collisionRectangles = new ArrayList<>();
          currentQuestionIndex = 0;
        questionActive = false;

        Image bg = new Image(getClass().getResource("/floor3.png").toExternalForm());
        ImageView bgView = new ImageView(bg);
        bgView.setFitWidth(1950);
        bgView.setFitHeight(1000);
        floor3Group.getChildren().add(bgView);

        initCharacterMovement(floor3Group, selectedDept);
        initializeLives(floor3Group);

        //rectangle collisions for floor3
        
        Rectangle wallTop3 = new Rectangle(10, 20, 1610, 140); // x-left, y-up, width, height
        wallTop3.setVisible(false);
        collisionRectangles.add(wallTop3);
        floor3Group.getChildren().add(wallTop3);
        
        Rectangle wallleft3 = new Rectangle(10, 50, 410, 1140); // x-left, y-up, width, height
        wallleft3.setVisible(false);
        collisionRectangles.add(wallleft3);
        floor3Group.getChildren().add(wallleft3);
        
        Rectangle walldown3 = new Rectangle(170, 370, 910, 920); // x-left, y-up, width, height
        walldown3.setVisible(false);
        collisionRectangles.add(walldown3);
        floor3Group.getChildren().add(walldown3);
        
        Rectangle wallright3 = new Rectangle(1490, 70, 680, 1740); // x-left, y-up, width, height
        wallright3.setVisible(false);
        collisionRectangles.add(wallright3);
        floor3Group.getChildren().add(wallright3);
        
        // Books
        Image bookImage = new Image(getClass().getResource("/book3.png").toExternalForm());
        double[][] bookPositions = {
                {1080, 140},  // book 3
               
            };
        addBooksForFloor(floor3Group, bookImage, bookPositions, 2, stage);

        Scene scene = new Scene(floor3Group, 1950, 1000);
        stage.setScene(scene);
        enableMovement(scene);
    }
    //floor4 scene 
    private void showFloor4(Stage stage) {
    
        Group floor4Group = new Group();

        collisionRectangles = new ArrayList<>();
   
     
        currentQuestionIndex = 0;
        questionActive = false;

        Image bg = new Image(getClass().getResource("/floor4.png").toExternalForm());
        ImageView bgView = new ImageView(bg);
        bgView.setFitWidth(1950);
        bgView.setFitHeight(1000);
        floor4Group.getChildren().add(bgView);

        initCharacterMovement(floor4Group, selectedDept);
        initializeLives(floor4Group);

 //rectangle collisions for floor4
        
        Rectangle wallTop4 = new Rectangle(10, 10, 1910, 100); // x-left, y-up, width, height
        wallTop4.setVisible(false);
        collisionRectangles.add(wallTop4);
        floor4Group.getChildren().add(wallTop4);
        
        Rectangle wallleft4 = new Rectangle(10, 50, 410, 1140); // x-left, y-up, width, height
        wallleft4.setVisible(false);
        collisionRectangles.add(wallleft4);
        floor4Group.getChildren().add(wallleft4);
        
        Rectangle walldown4 = new Rectangle(170, 370, 910, 1520); // x-left, y-up, width, height
        walldown4.setVisible(false);
        collisionRectangles.add(walldown4);
        floor4Group.getChildren().add(walldown4);
        
        Rectangle wallright4 = new Rectangle(1590, 70, 680, 1740); // x-left, y-up, width, height
        wallright4.setVisible(false);
        collisionRectangles.add(wallright4);
        floor4Group.getChildren().add(wallright4);
        

        // Books
        Image bookImage = new Image(getClass().getResource("/book4.png").toExternalForm());
        double[][] bookPositions = {
                {1420, 440},  // book 4
               
            };
        addBooksForFloor(floor4Group, bookImage, bookPositions, 3, stage);

        Scene scene = new Scene(floor4Group, 1950, 1000);
        stage.setScene(scene);
        enableMovement(scene);
    }

    // ---------------- LIFE SYSTEM METHODS ----------------
    private void initializeLives(Group group) {
        Image heartImage = new Image(getClass().getResource("/heart.png").toExternalForm());

        for (int i = 0; i < lives; i++) {
            ImageView heart = new ImageView(heartImage);
            heart.setFitWidth(60);
            heart.setFitHeight(60);
            heart.setLayoutX(100 + (i * 70));
            heart.setLayoutY(20);

            lifeQueue.add(heart);
            group.getChildren().add(heart);
        }

        lifeStack.push(lives);
    }

    public void decreaseLife() {
        if (!lifeQueue.isEmpty()) {
            ImageView lostHeart = lifeQueue.removeLast();
            lostHeart.setVisible(false);

            lives--;
            lifeStack.push(lives);
           
        }
    }

    public void increaseLife(Group group) {
        if (lives < 3) {
            Image heartImage = new Image(getClass().getResource("/heart.png").toExternalForm());
            ImageView newHeart = new ImageView(heartImage);
            newHeart.setFitWidth(60);
            newHeart.setFitHeight(60);
            newHeart.setLayoutX(100 + (lives * 70));
            newHeart.setLayoutY(20);

            lifeQueue.addLast(newHeart);
            group.getChildren().add(newHeart);

            lives++;
            lifeStack.push(lives);
        }
    }

    private void showQuestion(Group group, String questionText, String[] choices, int correctIndex,
            java.util.function.Consumer<Boolean> onAnswered) {
			Scene scene = group.getScene();
			try {
			    correctClip = new AudioClip(getClass().getResource("/correct.mp3").toExternalForm());
			    correctClip.setVolume(0.6);

			    wrongClip = new AudioClip(getClass().getResource("/wrong.mp3").toExternalForm());
			    wrongClip.setVolume(0.6);
			} catch (Exception e) {
			}

			if (scene == null) return;
			
			Pane questionPane = new Pane();
			questionPane.setPrefSize(scene.getWidth(), scene.getHeight());
			
			// Semi-transparent full-screen overlay
			Rectangle overlay = new Rectangle(scene.getWidth(), scene.getHeight());
			overlay.setFill(Color.rgb(0, 0, 0, 0.5));

			// Make the box bigger and centered
			double boxWidth = scene.getWidth() * 0.75;   // wider
			double boxHeight = scene.getHeight() * 0.6;  // taller
			double boxX = (scene.getWidth() - boxWidth) / 2;
			double boxY = (scene.getHeight() - boxHeight) / 2;

			Rectangle box = new Rectangle(boxX, boxY, boxWidth, boxHeight);
			box.setFill(Color.rgb(0, 0, 0, 0.7)); // 70% opacity black
			box.setArcWidth(20);
			box.setArcHeight(20);

			Text question = new Text(questionText);
			question.setWrappingWidth(boxWidth - 40);
			question.setX(boxX + 20);
			question.setY(boxY + 60); // slightly lower to give breathing room

			// White text for contrast
			question.setFill(Color.WHITE);
			question.setFont(Font.font("Arial", FontWeight.BOLD, 35));
			
			
			questionPane.getChildren().addAll(overlay, box, question);
			
			// Feedback message (initially empty)
			Text feedback = new Text("");
			feedback.setFont(Font.font("Arial", FontWeight.BOLD, 35));
			feedback.setFill(Color.WHITE); // default color, will change on answer

			// Center horizontally inside the box
			feedback.setX(boxX + boxWidth / 3);
			feedback.setY(boxY + boxHeight - 90); // near bottom of the box
			feedback.setTextAlignment(TextAlignment.CENTER);
			feedback.setTextOrigin(VPos.CENTER);

			questionPane.getChildren().add(feedback);
			for (int i = 0; i < choices.length; i++) {
			    String choiceText = choices[i];
			    Button btn = new Button(choiceText);

			    int col = i % 2;
			    int row = i / 2;

			    double btnWidth = (boxWidth - 150) / 2;
			    double btnHeight = 80;

			    btn.setLayoutX(boxX + 50 + col * (btnWidth + 50));
			    btn.setLayoutY(boxY + 180 + row * (btnHeight + 20)); // more vertical spacing
			    btn.setPrefWidth(btnWidth);
			    btn.setPrefHeight(btnHeight);
			    btn.setFont(Font.font("Arial", 20));
			    btn.setWrapText(true); 

			    int index = i;
			    btn.setOnAction(_ -> {
			        boolean correct = index == correctIndex;

			        if (correct) {
			            feedback.setText("Your answer is Correct!");
			            feedback.setFill(Color.web("#55FF55"));
			            playSound(correctClip); // 🔊 correct sound
			        } else {
			            feedback.setText("Your answer is Wrong!");
			            feedback.setFill(Color.web("#FF5555"));
			            playSound(wrongClip);   // 🔊 wrong sound
			        }

			        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
			        pause.setOnFinished(_ -> {
			            group.getChildren().remove(questionPane);
			            questionActive = false; 
			            onAnswered.accept(correct);
			        });
			        pause.play();
			    });


			    questionPane.getChildren().add(btn);
			}

			
			group.getChildren().add(questionPane);
			}
    private void showNextQuestion(Group group, List<Question> floor, Stage stage) {
        if (correctAnswers >= 5) {
            currentQuestionIndex = 0;
            correctAnswers = 0;
            correctStreak = 0;
            questionActive = false;

            goToNextFloor(group, stage);
            return;
        }

        Question q = floor.get(currentQuestionIndex % floor.size()); // ✅ use floor.size()

        showQuestion(group, q.text, q.choices, q.correctIndex, correct -> {
            if (correct) {
                correctAnswers++;
                correctStreak++;
                if (correctStreak == 3) {
                    increaseLife(group);
                    correctStreak = 0;
                }
            } else {
                decreaseLife();
                correctStreak = 0;
                if (lives <= 0) {
                    showGameOver(group, stage);
                    return;
                }
            }

            currentQuestionIndex++;
            showNextQuestion(group, floor, stage); // ✅ recurse with the same floor list
        });
    }
    private void resetGameState() {
        currentQuestionIndex = 0;
        correctAnswers = 0;
        correctStreak = 0;
        questionActive = false;
        lives = 3; // reset to starting lives
    }
    private void goToNextFloor(Group group, Stage stage) {
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), group);
        try {
            newLevelClip = new AudioClip(getClass().getResource("/newlevel.mp3").toExternalForm());
            newLevelClip.setVolume(0.6);

        } catch (Exception e) {
        }
        
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(_ -> {
            currentFloor++;
            if (currentFloor >= floors.size()) {
                showGameWon(group, stage);

                // Fade back in for the Game Won overlay
                FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), group);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            } else {
                // Normal floor transition
                group.getChildren().clear();
                floors.get(currentFloor).sceneBuilder.run();
                playSound(newLevelClip);

                FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), group);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            }
        });

        fadeOut.play();
    }

   
    private void showGameOver(Group root, Stage stage) {
        try {
            if (gameOverPlayer == null) {
                String gameOverFile = getClass().getResource("/gameover.mp3").toURI().toString();
                gameOverPlayer = new MediaPlayer(new Media(gameOverFile));
                gameOverPlayer.setVolume(0.7);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 🔊 Fade out background music when Game Over appears
        FrameMain.fadeOutBackgroundMusic(2.0);

        double boxWidth = stage.getWidth() * 0.6;
        double boxHeight = stage.getHeight() * 0.4;
        double boxX = (stage.getWidth() - boxWidth) / 2;
        double boxY = (stage.getHeight() - boxHeight) / 2;

        Rectangle overlay = new Rectangle(boxX, boxY, boxWidth, boxHeight);
        overlay.setFill(Color.BLACK);
        overlay.setOpacity(0.7);
        overlay.setArcWidth(20);
        overlay.setArcHeight(20);

        Text over = new Text("💀 Game Over! You ran out of lives.");
        over.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        over.setFill(Color.web("#FF5555"));
        over.setWrappingWidth(boxWidth - 40);
        over.setX(boxX + 80);
        over.setY(boxY + 80);

        Button retryBtn = new Button("Retry");
        retryBtn.setLayoutX(boxX + boxWidth / 2 - 160);
        retryBtn.setLayoutY(boxY + boxHeight - 150);
        retryBtn.setPrefWidth(150);
        retryBtn.setPrefHeight(60);
        retryBtn.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold; -fx-background-radius: 10;");

        Button exitBtn = new Button("Exit");
        exitBtn.setLayoutX(boxX + boxWidth / 2 + 20);
        exitBtn.setLayoutY(boxY + boxHeight - 150);
        exitBtn.setPrefWidth(150);
        exitBtn.setPrefHeight(60);
        exitBtn.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold; -fx-background-radius: 10;");

        retryBtn.setOnAction(_ -> {
            playClickSound();
         // Stop Game Over music immediately
            if (gameOverPlayer != null) {
                gameOverPlayer.stop();
            }
            // Restart background music
            FrameMain.restartBackgroundMusic();
            
            resetGameState();
            currentFloor = Math.max(0, currentFloor - 1);
            floors.get(currentFloor).sceneBuilder.run();
            root.getChildren().removeAll(overlay, over, retryBtn, exitBtn);
            questionActive = false;
        });

        exitBtn.setOnAction(_ -> {
            playClickSound();
            stage.close();
        });

        root.getChildren().addAll(overlay, over, retryBtn, exitBtn);

        // Play the Game Over sound
        if (gameOverPlayer != null) {
            gameOverPlayer.stop(); // restart if already playing
            gameOverPlayer.play();
        }
    }
    private void showGameWon(Group root, Stage stage) {
    	 try {
    	        if (gameWonPlayer == null) {
    	            String gameWonFile = getClass().getResource("/gamecomplete.mp3").toURI().toString();
    	            gameWonPlayer = new MediaPlayer(new Media(gameWonFile));
    	            gameWonPlayer.setVolume(0.7);
    	        }
    	    } catch (Exception e) {
    	        e.printStackTrace();
    	    }
        // 🔊 Fade out background music when Game Won appears
        FrameMain.fadeOutBackgroundMusic(2.0);

        double boxWidth = stage.getWidth() * 0.5;
        double boxHeight = stage.getHeight() * 0.3;
        double boxX = (stage.getWidth() - boxWidth) / 2;
        double boxY = (stage.getHeight() - boxHeight) / 2;

        Rectangle overlay = new Rectangle(boxX, boxY, boxWidth, boxHeight);
        overlay.setFill(Color.BLACK);
        overlay.setOpacity(0.7);
        overlay.setArcWidth(20);
        overlay.setArcHeight(20);

        Text won = new Text("🎉 Congratulations! You cleared all floors!");
        won.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        won.setFill(Color.web("#55FF55"));
        won.setWrappingWidth(boxWidth - 40);
        won.setX(boxX + 80);
        won.setY(boxY + 80);

        Button startBtn = new Button("Play Again");
        startBtn.setLayoutX(boxX + boxWidth / 2 - 160);
        startBtn.setLayoutY(boxY + boxHeight - 150);
        startBtn.setPrefWidth(140);
        startBtn.setPrefHeight(60);
        startBtn.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

        Button exitBtn = new Button("Exit");
        exitBtn.setLayoutX(boxX + boxWidth / 2 + 20);
        exitBtn.setLayoutY(boxY + boxHeight - 150);
        exitBtn.setPrefWidth(140);
        exitBtn.setPrefHeight(60);
        exitBtn.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

        startBtn.setOnAction(_ -> {
            playClickSound();
            // Stop Game Won music immediately
            if (gameWonPlayer != null) {
                gameWonPlayer.stop();
            }
            
            // Restart background music
            FrameMain.restartBackgroundMusic();

            resetGameState();
            currentFloor = 0;
            floors.get(currentFloor).sceneBuilder.run();
            root.getChildren().removeAll(overlay, won, startBtn, exitBtn);
        });

        exitBtn.setOnAction(_ -> {
            playClickSound();
            stage.close();
        });

        root.getChildren().addAll(overlay, won, startBtn, exitBtn);
        if (gameWonPlayer != null) {
            gameWonPlayer.stop();
            gameWonPlayer.play();
        }
    }
    
    // ✅ Add the helper method here
    private void playSound(AudioClip clip) {
        if (clip != null) {
            clip.stop(); // reset so it restarts cleanly
            clip.play();
        }
    }


}
