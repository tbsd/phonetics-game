package phoneticsgame;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static phoneticsgame.Language.availableLanguages;

public class MenuController extends Group {
    private Data data;
    private MenuView menuView;
    private MenuBar menuBar;

    public MenuController(Data data) {
        this.data = data;
        menuView = new MenuView(data);
        menuBar = createMenuBar();
        this.getChildren().add(menuBar);
    }

    MenuView getMenuView() {
        return menuView;
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.setPrefWidth(Integer.MAX_VALUE);
        menuBar.getMenus().addAll(createGameMenu(), createProfileMenu(), createPreferencesMenu(), createAboutMenu());
        return menuBar;
    }

    private Menu createGameMenu() {
        Menu gameMenu = new Menu("Игра");
        gameMenu.getItems().addAll(createNewGameItem(), createPauseSwitchItem(),
                createStopGameItem(), new SeparatorMenuItem(), createExitItem());
        return gameMenu;
    }

    private MenuItem createNewGameItem() {
        MenuItem newGameItem = new MenuItem("Новая игра ");
        newGameItem.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
        newGameItem.setOnAction(e -> {
            data.getGame().getCurrentTurn().pause();
            if (data.getGame() != null && data.getGame().isStarted() && !data.getGame().isEnded())
                if (abortGameDialog() == ButtonType.CANCEL) {
                    data.getGame().getCurrentTurn().resume();
                    return;
                }
            data.getGame().abort();
            data.newGame();
            data.getGame().endedBooleanProperty().addListener((observableValue, aBoolean, t1) -> data.saveGame());
            data.getGame().startGame();

        });
        return newGameItem;
    }

    private CheckMenuItem createPauseSwitchItem() {
        CheckMenuItem pauseSwitchItem = new CheckMenuItem("Пауза");
        pauseSwitchItem.setAccelerator(KeyCombination.keyCombination("Ctrl+P"));
        data.gameObjectProperty().addListener(observable -> pauseSwitchItem.setSelected(false));
        pauseSwitchItem.selectedProperty().addListener(observable -> {
            if (data.getGame() == null || !data.getGame().isStarted() || data.getGame().isEnded()) {
                pauseSwitchItem.setSelected(false);
                return;
            }
            if (pauseSwitchItem.isSelected())
                data.getGame().getCurrentTurn().pause();
            else
                data.getGame().getCurrentTurn().resume();
        });
        return pauseSwitchItem;
    }

    private MenuItem createStopGameItem() {
        MenuItem stopGameItem = new MenuItem("Завершить игру");
        stopGameItem.setAccelerator(KeyCombination.keyCombination("Ctrl+E"));
        stopGameItem.setOnAction(e -> {
            data.getGame().getCurrentTurn().pause();
            if (data.getGame() != null && data.getGame().isStarted() && !data.getGame().isEnded())
                if (abortGameDialog() == ButtonType.CANCEL) {
                    data.getGame().getCurrentTurn().resume();
                    return;
                }
            data.getGame().abort();
        });
        return stopGameItem;
    }

    private MenuItem createExitItem() {
        MenuItem exitItem = new MenuItem("Выход");
        exitItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        exitItem.setOnAction(e -> {
            data.getGame().getCurrentTurn().pause();
            if (data.getGame() != null && data.getGame().isStarted() && !data.getGame().isEnded())
                if (abortGameDialog() == ButtonType.CANCEL) {
                    data.getGame().getCurrentTurn().resume();
                    return;
                }
            data.getGame().abort();
            data.saveProfile();
            Platform.exit();
        });
        return exitItem;
    }

    private Menu createPreferencesMenu() {
        Menu preferencesMenu = new Menu("Настройки");
        preferencesMenu.getItems().addAll(createGameSettingsItem(),
                menuView.createLabelMenuItem("Громкость:"), createVolumeItem());
        return preferencesMenu;
    }

    private MenuItem createGameSettingsItem() {
        MenuItem gameSettingsItem = new MenuItem("Настройки игры");
        gameSettingsItem.setAccelerator(KeyCombination.keyCombination("Ctrl+C"));
        gameSettingsItem.setOnAction(e -> {
            if (data.getGame() == null || !data.getGame().isStarted() || data.getGame().isEnded()) {
                data.setPreferences(preferencesDialog());
                data.savePreferences();
            }
        });
        return gameSettingsItem;
    }

    private CustomMenuItem createVolumeItem() {
        Slider volumeSlider = new Slider(0, 1, data.getPreferences().getVolume());
        volumeSlider.setBlockIncrement(0.01);
        volumeSlider.valueProperty().addListener(observable -> {
            data.getPreferences().setVolume(volumeSlider.getValue());
            data.savePreferences();
        });
        CustomMenuItem volumeItem = new CustomMenuItem(volumeSlider);
        volumeItem.setHideOnClick(false);
        volumeItem.setOnAction(null);
        return volumeItem;
    }


    private Menu createAboutMenu() {
        Menu aboutMenu = new Menu("О программе");
        aboutMenu.getItems().addAll(createAboutItem(), createHelpItem(), createLicenseMenuItem());
        return aboutMenu;
    }

    private MenuItem createLicenseMenuItem() {
        MenuItem licenseItem = new MenuItem("License");
        licenseItem.setOnAction(e -> menuView.licenseDialog());
        return licenseItem;
    }

    private MenuItem createHelpItem() {
        MenuItem helpItem = new MenuItem("Помощь");
        helpItem.setOnAction(e -> menuView.helpDialog());
        return helpItem;
    }

    private MenuItem createAboutItem() {
        MenuItem aboutItem = new MenuItem("О программе");
        aboutItem.setOnAction(e -> menuView.aboutDialog());
        return aboutItem;
    }

    private Menu createProfileMenu() {
        Menu profileMenu = new Menu("Профиль");
        profileMenu.getItems().addAll(createStatisticsItem(), new SeparatorMenuItem(),
                createNewProfileItem(), createChangeProfileItem(), createDeleteProfileItem());
        return profileMenu;
    }

    private MenuItem createChangeProfileItem() {
        MenuItem changeProfileItem = new MenuItem("Сменить профиль");
        changeProfileItem.setOnAction(e -> {
            if (data.getGame() == null || !data.getGame().isStarted() || data.getGame().isEnded())
                changeProfileDialog();
        });
        return changeProfileItem;
    }

    private void changeProfileDialog() {
        String[] availableProfiles = new File(IOManager.PROFILEDIR).list();
        if (availableProfiles == null || availableProfiles.length == 0) {
            menuView.profileNotExistDialog();
            return;
        }
        ChoiceDialog<String> changeProfileDialog;
        if (data.getProfile() != null)
            changeProfileDialog = new ChoiceDialog<>(data.getProfile().getName(), availableProfiles);
        else
            changeProfileDialog = new ChoiceDialog<>(availableProfiles[0], availableProfiles);
        changeProfileDialog.setTitle("Сменить провиль");
        changeProfileDialog.setHeaderText("Выбирите профиль");
        Optional<String> result = changeProfileDialog.showAndWait();
        result.ifPresent(s -> data.setProfile(new Profile(s)));
    }

    private MenuItem createDeleteProfileItem() {
        MenuItem deleteProfileItem = new MenuItem("Удалить профиль");
        deleteProfileItem.setOnAction(e -> {
            if (data.getGame() == null || !data.getGame().isStarted() || data.getGame().isEnded())
                deleteProfileDialog();
        });
        return deleteProfileItem;
    }

    private void deleteProfileDialog() {
        if (data.getProfile() == null) {
            menuView.profileNotExistDialog();
            return;
        }
        Dialog<ButtonType> deleteProfileDialog = new Dialog<>();
        deleteProfileDialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
        deleteProfileDialog.setOnCloseRequest(e -> deleteProfileDialog.close());
        deleteProfileDialog.setTitle("Удаление профиля " + data.getProfile().getName());
        deleteProfileDialog.getDialogPane()
                .setContent(new Label("Удалить профиль " + data.getProfile().getName() + "?"));
        Optional<ButtonType> result = deleteProfileDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            IOManager.deleteDir(IOManager.PROFILEDIR + data.getProfile().getName());
            new File(IOManager.LASTPROFILE).delete();
            data.setProfile(null);
        }
    }

    private MenuItem createNewProfileItem() {
        MenuItem newProfileItem = new MenuItem("Новый профиль");
        newProfileItem.setOnAction(e -> {
            if (data.getGame() == null || !data.getGame().isStarted() || data.getGame().isEnded())
                newProfileDialog();
        });
        return newProfileItem;
    }

    private MenuItem createStatisticsItem() {
        MenuItem statisticsItem = new MenuItem("Статистика");
        statisticsItem.setOnAction(e -> menuView.statisticsDialog());
        statisticsItem.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        return statisticsItem;
    }

    private void newProfileDialog() {
        Dialog<ButtonType> newProfileDIalog = new Dialog<>();
        newProfileDIalog.setOnCloseRequest(e -> newProfileDIalog.close());
        newProfileDIalog.setTitle("Создание профиля");

        VBox content = new VBox();
        content.setSpacing(4);
        newProfileDIalog.getDialogPane().setContent(content);

        Label infoLabel = new Label("Название нового профиля:");
        content.getChildren().add(infoLabel);
        TextField profileField = new TextField("по умолчанию");
        profileField.focusedProperty().addListener(o -> {
            if (profileField.getCharacters().toString().equals("по умолчанию"))
                profileField.setText("");
        });
        content.getChildren().add(profileField);
        newProfileDIalog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
        Optional<ButtonType> result = newProfileDIalog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (profileField.getCharacters().toString().matches(Profile.AVALIBLENAMEREGEX)) {
                if ((new File(IOManager.PROFILEDIR + profileField.getCharacters().toString()).exists())) {
                    menuView.profileAlreadyExistsDialog(profileField.getCharacters().toString());
                    newProfileDialog();
                    return;
                }
                data.setProfile(new Profile(profileField.getCharacters().toString()));
                data.saveProfile();
            } else {
                menuView.invalidNameDialog();
                newProfileDialog();
            }
        }
    }


    ButtonType abortGameDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Завершение игры");
        alert.setHeaderText("Внимание!");
        alert.setGraphic(null);
        Label alertBody = new Label("Все незавершенные ходы будут засчитаны как ошибочные." +
                "\nПрервать игру?");
        alert.getDialogPane().setContent(alertBody);
        Optional<ButtonType> response = alert.showAndWait();
        return response.orElse(ButtonType.CANCEL);
    }

    private Preferences preferencesDialog() {
        Preferences newPreferences = new Preferences(data.getPreferences());
        Dialog<ButtonType> prefDialog = new Dialog<>();
        prefDialog.setOnCloseRequest(e -> prefDialog.close());
        prefDialog.setTitle("Настройки");

        GridPane prefPane = new GridPane();
        prefPane.setHgap(8);
        prefPane.setVgap(4);
        prefDialog.getDialogPane().setContent(prefPane);

        ChoiceBox<String> languageChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList(availableLanguages));
        languageChoiceBox.getSelectionModel().select(data.getPreferences().getLanguage());
        languageChoiceBox.getSelectionModel().selectedIndexProperty().addListener((observable, value, newValue) ->
                newPreferences.setLanguage(availableLanguages.get(newValue.intValue())));
        prefPane.addRow(0, new Label("Язык:"), languageChoiceBox);
        Spinner<Integer> maxTimeSpinner = new Spinner<>(1, Integer.MAX_VALUE, data.getPreferences().getMaxTime());
        maxTimeSpinner.setEditable(true);
        prefPane.addRow(1, new Label("Время на ход:"), maxTimeSpinner, new Label("сек."));
        Spinner<Integer> maxTurnsSpinner = new Spinner<>(1, data.getPreferences().getAvailableSounds() - 1,
                data.getPreferences().getMaxTurns());
        maxTurnsSpinner.setEditable(true);
        // Atomic variable allows to use its value in lambda expression without making it global variable
        AtomicReference<Spinner<Integer>> maxTurnsSpinnerWrapper = new AtomicReference<>(maxTurnsSpinner);
        prefPane.addRow(2, new Label("Количество ходов:"), maxTurnsSpinnerWrapper.get());
        newPreferences.availableSoundsIntegerProperty().addListener(observable -> {
            if (newPreferences.getAvailableSounds() > newPreferences.getMaxTurns())
                maxTurnsSpinnerWrapper.get().setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,
                        newPreferences.getAvailableSounds() - 1, newPreferences.getMaxTurns()));
            else
                maxTurnsSpinnerWrapper.get().setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,
                        newPreferences.getAvailableSounds() - 1, newPreferences.getAvailableSounds() - 1));
        });

        prefDialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
        Optional<ButtonType> result = prefDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            newPreferences.setMaxTurns(maxTurnsSpinner.getValue());
            newPreferences.setMaxTime(maxTimeSpinner.getValue());
            return newPreferences;
        }
        return data.getPreferences();
    }
}
