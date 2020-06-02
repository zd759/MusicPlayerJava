/* Name: Zara Duncanson
 * Student ID: P229768
 * Date: 27/05/20
 * Task: Final Project
 * This program lets a user login using hashing techniques on their password.
 * Once logged in, users are able to chat and also use a media player which 
 * can load audio files and save to .csv file using a third party library. Songs 
 * are sorted automatically upon upload and can then be searched. Tracks are 
 * stored in a dynamic data-structure (doubly-linked list) and users are stored 
 * as objects with attributes in a list. It also implements a custom GUI using a 
 * FXML document created using Scene Builder. The FXML is controlled by the 
 * FXMLController to execute methods and functions. Includes help files accessed 
 * via the menu bar item. Uses Java FX and 'Open CSV' third party library.
 */
package musicplayerproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Zara
 */
public class MusicPlayerProject extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        //set title and get source controller to load GUI and controls
        stage.setTitle("Music Player Project");
        Parent root = FXMLLoader.load(getClass().getResource("FXML.fxml"));
        //Parent root = loader.load();
        //put controller into scene
        Scene scene = new Scene(root);
        //show the scene
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
