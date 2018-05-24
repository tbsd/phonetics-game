package phoneticsgame;

import javafx.scene.control.Alert;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.web.WebView;
import javafx.stage.Modality;

import java.io.File;

import static phoneticsgame.Language.availableLanguages;

class MenuView {
    Data data;

    MenuView(Data data) {
        this.data = data;
    }

    void profileNotExistDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.getDialogPane().setContent(new Label("Профиль не создан."));
        alert.showAndWait();
    }

    void statisticsDialog() {
        Alert statisticsDialog = new Alert(Alert.AlertType.INFORMATION);
        statisticsDialog.setHeaderText(null);
        statisticsDialog.setGraphic(null);
        if (data.getProfile() == null) {
            profileNotExistDialog();
        } else {
            statisticsDialog.getDialogPane().setPrefWidth(500);
            statisticsDialog.setTitle("Статистика профиля " + data.getProfile().getName());
            GridPane contentPane = new GridPane();
            statisticsDialog.getDialogPane().setContent(contentPane);
            contentPane.setHgap(8);
            contentPane.setVgap(4);
            int i = 0;
            for (String lang : availableLanguages) {
                Label fancyLangLabel = new Label(lang);
                fancyLangLabel.setFont(Font.font(Font.getDefault().toString(), FontWeight.BOLD,
                        Font.getDefault().getSize() * 1.25));
                contentPane.addRow(i, new Separator(), fancyLangLabel);
                contentPane.addRow(i + 1, new Label("Всего игр:"),
                        new Label(Integer.toString(data.getProfile().getGamesCount(lang))));
                contentPane.addRow(i + 2, new Label("Без ошибок:"),
                        new Label(Integer.toString(data.getProfile().getCompletelyCorrect(lang))));
                contentPane.addRow(i + 3, new Label("Коэффицент верных ответов:"),
                        new Label(String.format("%.2f", data.getProfile().getAvgCorrect(lang))));
                contentPane.addRow(i + 4, new Label("Среднее время на верный ответ:"),
                        new Label(String.format("%.2f", data.getProfile().getAvgTimeOnCorrect(lang)) + " сек."));
                i += 6; // 5 + 1 for empty line between languages
            }
            statisticsDialog.showAndWait();
        }
    }

    Alert createHtmlDialog(String path) {
        Alert htmlDialog = new Alert(Alert.AlertType.INFORMATION);
        htmlDialog.setResizable(true);
        htmlDialog.initModality(Modality.NONE);
        htmlDialog.setHeaderText(null);
        htmlDialog.setGraphic(null);
        try {
            File readme = new File(path);
            WebView webView = new WebView();
            webView.getEngine().load(readme.toURI().toURL().toString());
            htmlDialog.getDialogPane().setContent(webView);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return htmlDialog;
    }

    void licenseDialog() {
        Alert licenseDialog = createHtmlDialog(IOManager.LICENSE);
        licenseDialog.setTitle("License");
        licenseDialog.showAndWait();
    }

    void helpDialog() {
        Alert helpDialog = createHtmlDialog(IOManager.README);
        helpDialog.setTitle("Помощь");
        helpDialog.show();
    }

    void aboutDialog() {
        Alert aboutDialog = createHtmlDialog(IOManager.ABOUT);
        aboutDialog.setTitle("PhoneticsGame");
        aboutDialog.getDialogPane().setPrefSize(370, 220);
        aboutDialog.show();
    }

    CustomMenuItem createLabelMenuItem(String text) {
        Label itemTextLabel = new Label(text);
        itemTextLabel.setTextFill(Color.GREY);
        CustomMenuItem labelItem = new CustomMenuItem(itemTextLabel);
        labelItem.setHideOnClick(false);
        return labelItem;
    }

    void profileAlreadyExistsDialog(String name) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(name);
        alert.setHeaderText(null);
        alert.getDialogPane().setContent(new Label("Профиль с таким названием уже существует."));
        alert.showAndWait();
    }

    void invalidNameDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.getDialogPane().setContent(new Label("Название может содержать только цифры и буквы." +
                "\nНазвание должно содержать хотябы один символ."));
        alert.showAndWait();
    }
}
