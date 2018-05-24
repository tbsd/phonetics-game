package phoneticsgame;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;

import java.io.File;

class GameView {
    private Data data;
    private MediaPlayer mediaPlayer;

    GameView(Data data) {
        this.data = data;
    }

    MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    void statsDialog() {
        Alert statsDialog = new Alert(Alert.AlertType.INFORMATION);
        statsDialog.setTitle("Статистика игры");
        statsDialog.setGraphic(null);
        statsDialog.setHeaderText(null);
        statsDialog.initModality(Modality.NONE);
        GridPane alertPane = new GridPane();
        statsDialog.getDialogPane().setContent(alertPane);
        alertPane.setHgap(4);
        alertPane.add(new Label("Верно отвечено:"), 0, 0);
        alertPane.add(new Label(data.getGame().getCorrectCount() + "/" +
                data.getPreferences().getMaxRounds()), 1, 0);
        double percentage = 100.0 * data.getGame().getCorrectCount() / data.getPreferences().getMaxRounds();
        alertPane.add(new Label(String.format("%.2f", percentage) + "%"), 2, 0);
        alertPane.add(new Label("Среднее время:"), 0, 1);
        alertPane.add(new Label(String.format("%.2f", data.getGame().getAvgTime())), 1, 1);

        statsDialog.show();
    }

    void playSound() {
        if (mediaPlayer != null)
            mediaPlayer.dispose();
        Media record =
                new Media(new File(data.getGame().getCurrentRound().getSound().getRecordPath()).toURI().toString());
        mediaPlayer = new MediaPlayer(record);
        if (data.getPreferences().getVolume() == 0)
            return;
        mediaPlayer.setVolume(data.getPreferences().getVolume());
        mediaPlayer.setOnEndOfMedia(mediaPlayer::dispose);
        mediaPlayer.setOnStalled(mediaPlayer::dispose);
        mediaPlayer.setOnStopped(mediaPlayer::dispose);
        mediaPlayer.setOnHalted(mediaPlayer::dispose);
        mediaPlayer.setOnError(mediaPlayer::dispose);
        mediaPlayer.play();
    }
}
