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
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WordEditController implements Initializable {
    Management management;
    Word currentWord;
    @FXML private TextField wordTargetUI;
    @FXML private TextArea wordExplainUI;
    @FXML private ToggleButton editTargetToggle;
    @FXML private ToggleButton editExplainToggle;
    @FXML private Button confirmDeleteButton;

    @FXML
    private void saveButtonClicked(ActionEvent event) throws IOException {
        Word editedWord = new Word(wordTargetUI.getText(), wordExplainUI.getText());
        management.editWord(currentWord, editedWord);
        // management.exportToFile();
        backToMainWindow(event);
    }

    @FXML
    private void editTargetToggleTriggered() {
        wordTargetUI.setDisable(!editTargetToggle.isSelected());
        confirmDeleteButton.setDisable(true);
    }

    @FXML
    private void editExplainToggleTriggered() {
        wordExplainUI.setDisable(!editExplainToggle.isSelected());
        confirmDeleteButton.setDisable(true);
    }

    @FXML
    private void discardButtonClicked(ActionEvent event) throws IOException {
        backToMainWindow(event);
    }

    @FXML
    private void resetButtonClicked() {
        wordTargetUI.setText(currentWord.getWord_target());
        wordExplainUI.setText(currentWord.getWord_explain());
        confirmDeleteButton.setDisable(true);
    }

    @FXML
    private void deleteButtonClicked() {
        confirmDeleteButton.setDisable(false);
    }

    @FXML
    private void confirmDeleteButtonClicked(ActionEvent event) throws IOException {
        management.deleteWord(currentWord);
        // management.exportToFile();
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

    public void getData(Word selectedWord, Management management) {
        currentWord = selectedWord;
        wordTargetUI.setText(currentWord.getWord_target());
        wordExplainUI.setText(currentWord.getWord_explain());
        this.management = management;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
