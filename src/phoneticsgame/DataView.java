package phoneticsgame;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;

public class DataView {
    private Data data;

    public DataView(Data data) {
        this.data = data;
    }

    public GridPane creteEmptyGamePane() {
        GridPane result = new GridPane();
        result.setPadding(new Insets(0, 20, 0, 20));
        result.setPrefSize(PhoneticsGame.mainWidth, PhoneticsGame.mainHeight);
        result.setAlignment(Pos.CENTER_LEFT);
        result.setHgap(4);
        Label tip1 = new Label("Меню: Игра -> Новая игра");
        tip1.setFont(Font.font(Font.getDefault().toString(), FontPosture.ITALIC, 16));
        tip1.setTextFill(Color.SILVER);
        result.add(tip1, 0, 0);
        Label tip2 = new Label(", чтобы начать игру.");
        tip2.setFont(Font.font(Font.getDefault().toString(), 16));
        tip2.setTextFill(Color.SILVER);
        Label profileLabel = new Label();
        profileLabel.setFont(Font.font(Font.getDefault().toString(), 16));
        profileLabel.setTextFill(Color.SILVER);
        if (data.getProfile() != null) {
            profileLabel.setText("Профиль: " + data.getProfile().getName());
        } else {
            profileLabel.setText("Создайте профиль, чтобы игра могла быть сохранена.");

        }
        result.add(profileLabel, 0, 1);
        data.profileObjectProperty().addListener(observable -> {
            if (data.getProfile() != null) {
                profileLabel.setText("Профиль: " + data.getProfile().getName());
            } else {
                profileLabel.setText("Создайте профиль, чтобы игра могла быть сохранена.");

            }
        });
        return result;
    }
}
