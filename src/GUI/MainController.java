package GUI;

import Engine.API;
import Engine.Management;
import Engine.Word;
import com.sun.speech.freetts.VoiceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;

public class MainController implements Initializable {
    Management management = new Management();
    private final Engine.API api = new API();
    private Word currentWord;

    @FXML private TextField searchBox;
    @FXML private Label wordTargetUI;
    @FXML private TextArea wordExplainUI;
    @FXML private ListView<String> wordsListView;
    @FXML private Button editButton;
    @FXML private Button speakButton;

    @FXML
    private void chooseWordFromList() {
        String selectedWord = wordsListView.getSelectionModel().getSelectedItem();
        if (selectedWord != null) {
            currentWord = management.getWord(selectedWord);
            wordTargetUI.setText(selectedWord);
            wordExplainUI.setText(currentWord.getWord_explain());
            editButton.setDisable(false);
            speakButton.setDisable(false);
        }
    }

    @FXML
    private void updateWordsListView() {
        Vector<Word> searchResult = management.searcher(searchBox.getText());
        wordsListView.getItems().clear();
        for (Word word : searchResult) {
            wordsListView.getItems().add(word.getWord_target());
        }
    }

    @FXML
    private void searchButtonClicked() {
        String searchedWord = searchBox.getText();
        if ((searchedWord != null) && (!searchedWord.equals(""))) {
            currentWord = management.getWord(searchedWord);
            wordTargetUI.setText(searchedWord);
            searchBox.clear();
            speakButton.setDisable(false);
            if (currentWord.getWord_explain().equals("***")) {
                if (api.checkNetwork()) {
                    wordExplainUI.setText("API's result :\n\n"
                            + api.Translate(searchedWord));
                    editButton.setDisable(true);
                } else {
                    wordExplainUI.setText("Cannot find the expected word.\n"
                            + "No Internet Connection\n");
                }
            } else {
                wordExplainUI.setText(currentWord.getWord_explain());
                editButton.setDisable(false);
            }
        }
    }

    @FXML
    private void addWordButtonClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("WordAdd.fxml"));
        Parent addWordParent = loader.load();

        WordAddController wordAddController = loader.getController();
        wordAddController.getData(searchBox.getText(), management);

        Scene addWordScene = new Scene(addWordParent);
        Stage mainWindow = (Stage) ((Node)event.getSource()).getScene().getWindow();
        mainWindow.setScene(addWordScene);
        mainWindow.show();
    }

    @FXML
    private void editWordButtonClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("WordEdit.fxml"));
        Parent editWordParent = loader.load();

        WordEditController wordEditController = loader.getController();
        wordEditController.getData(currentWord, management);

        Scene editWordScene = new Scene(editWordParent);
        Stage mainWindow = (Stage) ((Node)event.getSource()).getScene().getWindow();
        mainWindow.setScene(editWordScene);
        mainWindow.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //management.insertFromFiles();
        management.selectAll();
        Vector<Word> allWordsList = management.getAllWords();
        for (Word word : allWordsList) {
            wordsListView.getItems().add(word.getWord_target());
        }

        wordsListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> chooseWordFromList());
        searchBox.textProperty().addListener(((observable, oldValue, newValue)
                -> updateWordsListView()));
    }

    @FXML
    private void speakVoice() {
        String text = wordTargetUI.getText();
        if (api.checkNetwork()) {
            api.speakAPI(text);
        } else {
            System.setProperty("freetts.voices",
                    "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
            VoiceManager voiceManager = VoiceManager.getInstance();
            com.sun.speech.freetts.Voice newVoice = voiceManager.getVoice("kevin16");
            newVoice.allocate();
            newVoice.speak(text);
            newVoice.deallocate();
        }
    }
}
