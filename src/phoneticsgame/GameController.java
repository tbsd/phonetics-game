package phoneticsgame;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class GameController extends Pane {
    private Data data;
    private GameView gameView;
    private VBox mainPane;
    private Label answerStatus;

    public GameController(Data data) {
        this.data = data;
        gameView = new GameView(data);
        createPane();
        this.getChildren().add(mainPane);
    }

    private void createPane() {
        mainPane = new VBox();
        mainPane.setPrefWidth(PhoneticsGame.mainWidth);
        mainPane.setPadding(new Insets(15, 10, 15, 10));
        mainPane.setSpacing(30);
        HBox postGameBtns = createPostGameButtons();
        postGameBtns.setVisible(false);
        mainPane.getChildren().addAll(createTopPane(), createAnswerPane(), postGameBtns);
        data.getGame().endedBooleanProperty().addListener((observableValue, aBoolean, t1) ->
                postGameBtns.setVisible(true));
    }

    private HBox createTopPane() {
        answerStatus = new Label("Выберите ответ");
        answerStatus.setTextFill(Color.BLACK);
        answerStatus.setFont(Font.font(Font.getDefault().toString(), 16));
        answerStatus.setAlignment(Pos.CENTER);
        answerStatus.setPrefWidth(PhoneticsGame.mainWidth / 2.5);
        HBox topPane = new HBox();
        topPane.setAlignment(Pos.CENTER);
        topPane.setPrefWidth(100);

        data.getGame().getCurrentRound().endedBooleanProperty().addListener(observable -> {
            if (gameView.getMediaPlayer() != null)
                gameView.getMediaPlayer().dispose();
        });

        Label timer = new Label();
        timer.setFont(Font.font(Font.getDefault().toString(), 16));
        timer.setAlignment(Pos.CENTER_LEFT);
        timer.textProperty().bind(data.getGame().getCurrentRound().currentTimeIntegerProperty().asString());

        Button playButton = new Button("Воспроизвести");
        playButton.setAlignment(Pos.CENTER_RIGHT);
        playButton.setOnAction(e -> gameView.playSound());
        data.getGame().endedBooleanProperty().addListener(observableValue -> {
            if (gameView.getMediaPlayer() != null)
                gameView.getMediaPlayer().dispose();
            playButton.setDisable(true);
        });
        //Listener for the first round
        data.getGame().getCurrentRound().pausedBooleanProperty().addListener(roundObservable -> {
            if (data.getGame().getCurrentRound().isPaused()) {
                playButton.setDisable(true);
                if (gameView.getMediaPlayer() != null)
                    gameView.getMediaPlayer().pause();
            } else {
                playButton.setDisable(false);
                if (gameView.getMediaPlayer() != null) {
                    gameView.getMediaPlayer().play();
                }
            }
        });
        // New listener for each new round
        data.getGame().currentRoundObjectProperty().addListener(gameObservable ->
                data.getGame().getCurrentRound().pausedBooleanProperty().addListener(roundObservable -> {
                    if (data.getGame().getCurrentRound().isPaused()) {
                        playButton.setDisable(true);
                        if (gameView.getMediaPlayer() != null)
                            gameView.getMediaPlayer().pause();
                    } else {
                        playButton.setDisable(false);
                        if (gameView.getMediaPlayer() != null) {
                            gameView.getMediaPlayer().play();
                        }
                    }
                }));
        data.getGame().startedBooleanProperty().addListener(observable -> gameView.playSound());
        data.getGame().currentRoundObjectProperty().addListener(observableValue -> {
            if (!data.getGame().isEnded()) {
                playButton.requestFocus();
                timer.textProperty().bind(data.getGame().getCurrentRound().currentTimeIntegerProperty().asString());
                gameView.playSound();
            }
        });

        topPane.getChildren().addAll(timer, answerStatus, playButton);
        return topPane;
    }

    private VBox createAnswerPane() {
        Background correctAnswerBackground =
                new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY));
        Background incorrectAnswerBackground =
                new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY));
        VBox answerPane = new VBox();
        answerPane.setPrefWidth(PhoneticsGame.mainWidth);
        answerPane.setAlignment(Pos.TOP_CENTER);
        List<Sound> sounds = data.getGame().getSoundsPool();
        Collections.shuffle(sounds);
        List<HBox> rows = new LinkedList<>();
        final int btnPrefSize = 52;
        final Insets btnInsets = new Insets(1, 0, 1, 0);
        for (Sound s : sounds) { // Creating answer buttons
            if (rows.isEmpty() || ((LinkedList<HBox>) rows).getLast().getChildren().size() >
                    PhoneticsGame.mainWidth / ((btnPrefSize + (btnInsets.getRight() + btnInsets.getLeft() +
                            (mainPane.getPadding().getLeft() + mainPane.getPadding().getRight()) / 2)))) {
                HBox newRow = new HBox();
                newRow.setAlignment(Pos.CENTER);
                newRow.setSpacing(2);
                newRow.setPadding(btnInsets);
                rows.add(newRow);
            }
            Button btn = new Button(s.getRndSequence());
            HBox.setHgrow(btn, Priority.ALWAYS);
            btn.setPrefWidth(btnPrefSize);
            // This is needed to associate a sound with an exact button
            btn.setAccessibleRoleDescription(new File(s.getRecordPath()).toURI().toString());
            //Listener for the first round
            data.getGame().getCurrentRound().pausedBooleanProperty().addListener(roundObservable -> {
                if (data.getGame().getCurrentRound().isPaused()) {
                    btn.setDisable(true);
                } else
                    // If btn hasn't been disabled as an answer button
                    if (btn.getBackground() != correctAnswerBackground
                            && btn.getBackground() != incorrectAnswerBackground)
                        btn.setDisable(false);
            });
            // New listener for each new round
            data.getGame().currentRoundObjectProperty().addListener(gameObservable ->
                    data.getGame().getCurrentRound().pausedBooleanProperty().addListener(roundObservable -> {
                        if (data.getGame().getCurrentRound().isPaused()) {
                            btn.setDisable(true);
                        } else
                            // If btn hasn't been disabled as an answer button
                            if (btn.getBackground() != correctAnswerBackground
                                    && btn.getBackground() != incorrectAnswerBackground)
                                btn.setDisable(false);
                    }));
            data.getGame().endedBooleanProperty().addListener(observable -> {
                if (!btn.isDisabled())
                    btn.setDisable(true);
            });
            btn.setOnAction(e -> {
                // trueButton instead of btn should be used because of risk of collision
                // in case if sequence relates to multiple sounds.
                // Example of collision. /s/ ~ {s, ss, ß}, /z/ ~ {s}.
                // buttons = {s, ss} ->  sound /s/ -> answer = {s} -> buttons = {ss} -> sound /z/ -> answer = ???
                Button trueButton = btn;
                //Find button matching exact sound
                for (Node row : answerPane.getChildren()) {
                    if (row instanceof HBox) {
                        for (Node b : ((HBox) row).getChildren()) {
                            if (b instanceof Button) {
                                if (b.getAccessibleRoleDescription()
                                        .equals(gameView.getMediaPlayer().getMedia().getSource())) {
                                    trueButton = ((Button) b);
                                }
                            }
                        }
                    }
                }
                if (data.getGame().getCurrentRound().getSound().isPresent(btn.getText())) {
                    answerStatus.setTextFill(Color.GREEN);
                    answerStatus.setText("Верно!");
                    trueButton.setBackground(correctAnswerBackground);
                    data.getGame().getCurrentRound().setCorrect(true);
                } else {
                    answerStatus.setTextFill(Color.RED);
                    answerStatus.setText("Ошибка");
                    trueButton.setBackground(incorrectAnswerBackground);
                    data.getGame().getCurrentRound().setCorrect(false);
                }
                trueButton.setDisable(true);
                data.getGame().getCurrentRound().end();
            });
            ((LinkedList<HBox>) rows).getLast().getChildren().add(btn);
        }
        answerPane.getChildren().addAll(rows);
        return answerPane;
    }


    private HBox createPostGameButtons() {
        HBox result = new HBox();
        Button statsBtn = new Button("Статистика игры");
        statsBtn.setOnAction(e -> gameView.statsDialog());
        statsBtn.setAlignment(Pos.CENTER);

        Button newGameBtn = new Button("Новая игра");
        newGameBtn.setOnAction(e -> {
            data.newGame();
            data.getGame().startGame();
            data.getGame().endedBooleanProperty().addListener(o -> data.saveGame());
        });

        Button exitBtn = new Button("Выйти");
        exitBtn.setOnAction(e -> {
            data.saveProfile();
            Platform.exit();
        });

        result.setSpacing(2);
        result.setAlignment(Pos.CENTER);
        result.getChildren().addAll(statsBtn, newGameBtn, exitBtn);
        return result;
    }

}
