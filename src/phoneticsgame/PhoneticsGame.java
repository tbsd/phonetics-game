package phoneticsgame;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;

public class PhoneticsGame extends Application {
    static final int mainWidth = 900;
    static final int mainHeight = 500;

    private Data data;
    private DataController mainWindow;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        File userDir = new File(IOManager.USERDATA);
        if (!userDir.exists()) {
            if (!userDir.mkdirs()) {
                System.err.println("User data directory was not created!");
            } else {// Set hidden attribute on windows OS
                if (System.getProperty("os.name").toLowerCase().matches(".*windows.*")) {
                    try {
                        Files.setAttribute(userDir.toPath(), "dos:hidden", true);
                    } catch (Exception e) {
                        System.err.println(e.toString());
                    }
                }
            }
        }
        data = new Data();
        data.newGame();
        VBox root = new VBox();
        primaryStage.setTitle("Phonetics Game: Тест Фонетики");
        Scene scene = new Scene(root, mainWidth, mainHeight);
        primaryStage.setMinWidth(mainWidth);
        primaryStage.setMinHeight(mainHeight);
        primaryStage.setMaxWidth(mainWidth);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(closeEvent -> {
            if (data.getGame() != null && data.getGame().isStarted() && !data.getGame().isEnded()) {
                data.getGame().getCurrentRound().pause();
                if (mainWindow.getMenuController().abortGameDialog() == ButtonType.OK) {
                    data.getGame().abort();
                    data.saveProfile();
                    data.savePreferences();
                    Platform.exit();
                } else {
                    closeEvent.consume();
                    data.getGame().getCurrentRound().resume();
                }
            }
        });
        mainWindow = new DataController(data);
        root.getChildren().add(mainWindow);

        primaryStage.show();
    }


}
