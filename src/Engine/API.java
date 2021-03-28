package Engine;

import com.gtranslate.Audio;
import com.gtranslate.Language;
import com.gtranslate.Translator;
import javafx.scene.control.Alert;
import javazoom.jl.decoder.JavaLayerException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class API {
    public String Translate(String s) {
        Translator translate = Translator.getInstance();
        String text = translate.translate(s + "&client=tw-ob", Language.ENGLISH, Language.VIETNAMESE);
        return text;
    }

    public void speakAPI(String word) {
        try {
            Audio audio = Audio.getInstance();
            InputStream sound = audio.getAudio(word + "&client=tw-ob", Language.ENGLISH);
            audio.play(sound);
        } catch (IOException | JavaLayerException e) {
            System.out.println(e.getMessage());
        }

    }

    public boolean checkNetwork() {
        try {
            URL url = new URL("http://www.google.com");
            URLConnection connection = url.openConnection();
            connection.connect();
            /*Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Look, an Information Dialog");
            alert.setContentText("I have a great message for you!");

            alert.showAndWait();*/
            System.out.println("Internet is connected");
        } catch (MalformedURLException e) {
            System.out.println("Internet is not connected");
            return false;
        } catch (IOException e) {
            System.out.println("Internet is not connected");
            return false;
        }
        return true;
    }
}
