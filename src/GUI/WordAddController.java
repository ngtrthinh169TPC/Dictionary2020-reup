package GUI;

import Engine.Management;
import Engine.Word;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class WordAddController implements Initializable {
    Management management;

    @FXML private TextField wordTargetUI;
    @FXML private TextArea wordExplainUI;

    @FXML
    private void addButtonClicked(ActionEvent event) throws IOException {
        if (wordTargetUI.getText().isEmpty() || wordExplainUI.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Add Word menu's warning");
            alert.setHeaderText("The word can not be a null word");

            alert.showAndWait();
            return;
        }
        if (Objects.equals(management.getWord(wordTargetUI.getText()).getWord_explain(), "***")) {
            Word newWord = new Word(wordTargetUI.getText(), wordExplainUI.getText());
            management.addWord(newWord);
            // management.exportToFile();
            backToMainWindow(event);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Add Word menu's warning");
            alert.setHeaderText("This word is already existed !");
            alert.setContentText("If you wanna change this word, please go to Edit Word menu\n"
                    + "If you've just mistype this word, go back and fix it <3");
            alert.showAndWait();
        }
    }

    @FXML
    private void discardButtonClicked(ActionEvent event) throws IOException {
        backToMainWindow(event);
    }

    private void backToMainWindow(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("MainWindow.fxml"));
        Parent mainParent = loader.load();

        Scene mainScene = new Scene(mainParent);
        Stage mainWindow = (Stage) ((Node)event.getSource()).getScene().getWindow();
        mainWindow.setScene(mainScene);
        mainWindow.show();
    }

    public void getData(String searchingWord, Management management) {
        wordTargetUI.setText(searchingWord);
        this.management = management;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
