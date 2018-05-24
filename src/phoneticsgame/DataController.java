package phoneticsgame;

import javafx.scene.Group;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.Optional;

class DataController extends Group {
    private Data data;
    private DataView dataView;
    private VBox mainPane;
    private MenuController menuController;
    private GameController gameController;
    private GridPane emptyGamePane;


    DataController(Data data) {
        this.data = data;
        dataView = new DataView(data);
        mainPane = createMainPane();
        this.getChildren().add(mainPane);
        if (!(new File(IOManager.LASTPROFILE).exists()))
            startUpDialog();
    }

    MenuController getMenuController() {
        return menuController;
    }

    private VBox createMainPane() {
        VBox mainPane = new VBox();
        mainPane.setPrefSize(PhoneticsGame.mainWidth, PhoneticsGame.mainHeight);
        menuController = new MenuController(data);
        mainPane.getChildren().add(menuController);
        emptyGamePane = dataView.creteEmptyGamePane();
        mainPane.getChildren().add(emptyGamePane);
        data.gameObjectProperty().addListener(e -> {
            if (gameController == null) {
                mainPane.getChildren().remove(emptyGamePane);
                gameController = new GameController(data);
                mainPane.getChildren().add(gameController);
            } else {
                GameController newGameController = new GameController(data);
                mainPane.getChildren().addAll(newGameController);
                mainPane.getChildren().remove(gameController);
                gameController = newGameController;
            }
        });
        return mainPane;
    }

    private void startUpDialog() {
        Dialog<ButtonType> startUpDialog = new Dialog<>();
        startUpDialog.setOnCloseRequest(e -> startUpDialog.close());
        startUpDialog.setTitle("Создание профиля");

        VBox content = new VBox();
        content.setSpacing(4);
        startUpDialog.getDialogPane().setContent(content);

        Label infoLabel = new Label("Введите название, чтобы создать новый профиль, или нажмите \"Отмена\"," +
                "чтобы продолжить без сохранения прогресса." +
                "\nВы всегда можете создать новый профиль с помощю соответствующего пункта меню" +
                "или выбрать из уже существущих.");
        content.getChildren().add(infoLabel);
        TextField profileField = new TextField("по умолчанию");
        profileField.focusedProperty().addListener(o -> {
            if (profileField.getCharacters().toString().equals("по умолчанию"))
                profileField.setText("");
        });
        content.getChildren().add(profileField);
        startUpDialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
        Optional<ButtonType> result = startUpDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (profileField.getCharacters().toString().matches(Profile.AVALIBLENAMEREGEX)) {
                if ((new File(IOManager.PROFILEDIR + profileField.getCharacters().toString()).exists())) {
                    menuController.getMenuView().profileAlreadyExistsDialog(profileField.getCharacters().toString());
                    startUpDialog();
                    return;
                }
                data.setProfile(new Profile(profileField.getCharacters().toString()));
                data.saveProfile();
            } else {
                menuController.getMenuView().invalidNameDialog();
                startUpDialog();
            }
        }
    }
}
