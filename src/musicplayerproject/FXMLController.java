/*
 * FXMLController class
 */
package musicplayerproject;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import java.awt.Desktop;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * FXML Controller class
 *
 * @author Zara
 */
public class FXMLController implements Initializable {

    //attributes of controller class
    private DoublyLinkedPlaylist<Song> playlist = new DoublyLinkedPlaylist<>();
    private DoublyLinkedPlaylist<Song> sortedPlaylist = new DoublyLinkedPlaylist<>();
    private final UserUtility userList = new UserUtility();
    private boolean login = false; //variable to check login status

    //----------initialise components of scene----------//
    //for login
    @FXML
    private Button btnUserLogin, btnLogOut, btnClear;
    @FXML
    private TextField textFieldUsername, textFieldPassword;
    @FXML
    private Label lblLoginStatus;
    //for media player
    @FXML
    private ListView<Song> listViewSongList;
    @FXML
    private Button btnAddSongs, btnDeleteSong, btnClearPlaylist;
    @FXML
    private Button btnPlay, btnPause, btnStop, btnNext, btnPrev, btnBinarySearch;
    @FXML
    private TextField textFieldBinarySearch;
    @FXML
    private Label lblSearchResult, lblMessage;
    private MediaPlayer mediaPlayer = null;
    private Media media;
    private Song currentSong;
    //buttons for csv read/write
    @FXML
    private Button btnSavePlaylist, btnLoadPlaylist;
    private File file;

    //----------MENU ITEM ACTION METHODS----------//
    @FXML
    private void openAction(ActionEvent event) throws CsvException {
        //pass to load csv method
        btnLoadPlaylistAction(event);
    }

    @FXML
    private void saveAsAction(ActionEvent event) {
        //pass to save to csv method
        btnSavePlaylistAction(event);
    }

    @FXML
    private void quitAction(ActionEvent event) {
        //close application
        System.out.println("Media Player closed from menu bar item 'Quit'"); //print statment
        Platform.exit();//exit the gui form
    }

    //menu item handler to load help file page in local browser
    @FXML
    private void helpFileAction(ActionEvent event) {
        //direct to help file
        lblMessage.setText("Help page accessed from menu bar"); //print statement
        String url = "help.html"; // the help files, e.g. index.html
        File htmlFile = new File(url); // file object of the help file
        try {
            Desktop.getDesktop().browse(htmlFile.toURI()); // open the help file in a default browser
        } catch (IOException ex) {
            lblMessage.setText("Error accessing help file!");//print error
        }
    }
    //----------END MENU ITEM METHODS----------//

    //----------USER LOGIN METHODS-----------//
    //method to log in user using text field inputs
    @FXML
    private void btnUserLoginAction() {
        try {
            if (textFieldUsername.getText().isBlank()) {
                lblLoginStatus.setText("Enter username!");
            } else if (textFieldPassword.getText().isEmpty()) {
                lblLoginStatus.setText("Enter password!");
            } else {
                String username = textFieldUsername.getText();
                String password = textFieldPassword.getText();
                String result = userList.testUsername(username, password);
                if (null != result) {
                    switch (result) {
                        case "verified":
                            login = true;
                            lblLoginStatus.setText("Logged in");
                            btnUserLogin.setDisable(true);
                            btnLogOut.setDisable(false);
                            enableMediaButtons();
                            textFieldBinarySearch.setDisable(false);
                            break;
                        case "passFail":
                            lblLoginStatus.setText("Password fail");
                            break;
                        case "userFail":
                            lblLoginStatus.setText("Username not found");
                            break;
                        default:
                            break;
                    }
                }
            }
            clearTextFields();
        } catch (NullPointerException e) {
            lblLoginStatus.setText("Login error");
        }
    }

    //mwthod to log out user disables media player ability
    @FXML
    private void btnLogOutAction() {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Song");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to log out?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                login = false;
                lblLoginStatus.setText("Logged out");
                btnUserLogin.setDisable(false);
                btnLogOut.setDisable(true);
                textFieldBinarySearch.setDisable(true);
                disableMediaButtons();
            }
        } catch (Exception e) {
            lblLoginStatus.setText("Error logging out");
        }
    }

    //method handler to add a new user and store in UserRepo array list with hashing
    @FXML
    private void btnCreateUserAction() {
        try {
            if (textFieldUsername.getText().isBlank()) {
                lblLoginStatus.setText("Enter username!");
            } else if (textFieldPassword.getText().isEmpty()) {
                lblLoginStatus.setText("Enter password!");
            } else {
                String username = textFieldUsername.getText();
                String password = textFieldPassword.getText();
                String result = userList.addUser(username, password);
                if (result != null) {
                    lblLoginStatus.setText(result);
                } else {
                    lblLoginStatus.setText("Add user failed");
                }
            }
            clearTextFields();
        } catch (NullPointerException e) {
            lblLoginStatus.setText("Add new user error");
        }
    }

    //button method to clear textFields in user area
    @FXML
    private void btnClearAction(ActionEvent event) {
        clearTextFields();
    }

    //private method to clear user function textboxes
    private void clearTextFields() {
        textFieldUsername.clear();
        textFieldPassword.clear();
        textFieldBinarySearch.clear();
    }
    //----------END USER LOGIN METHODS-----------//

    //----------MEDIA PLAYER METHODS-----------//
    //method to disable media player until log in successful
    @FXML
    private void disableMediaButtons() {
        btnAddSongs.setDisable(true);
        btnDeleteSong.setDisable(true);
        btnClearPlaylist.setDisable(true);
        btnPlay.setDisable(true);
        btnPause.setDisable(true);
        btnStop.setDisable(true);
        btnNext.setDisable(true);
        btnPrev.setDisable(true);
        btnBinarySearch.setDisable(true);
    }

    //method to enable media player when log in successful
    @FXML
    private void enableMediaButtons() {
        btnAddSongs.setDisable(false);
        btnDeleteSong.setDisable(false);
        btnClearPlaylist.setDisable(false);
        btnPlay.setDisable(false);
        btnPause.setDisable(false);
        btnStop.setDisable(false);
        btnNext.setDisable(false);
        btnPrev.setDisable(false);
        btnBinarySearch.setDisable(false);
    }

    //method for user to add songs to the DoublyLinkedPlaylist and listViewSongList
    //also sorts added songs with a merge sort prior to displaying names
    @FXML
    private void btnAddAction(ActionEvent event) {
        try {
            //set the sorted track list to the unsorted - used if user add files multiple times
            //this serves to correct the node values of 'next' and 'prev' in the unsorted list 
            playlist = sortedPlaylist;
            //use a file chooser
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().addAll(new ExtensionFilter(
                    "Audio Files", "*.mp3", "*.wav"));
            List<File> files = fc.showOpenMultipleDialog(btnAddSongs.getScene().getWindow());
            if (files != null) {
                //loop through the selected files and add as nodes to linked list
                for (File file : files) {
                    //obtain path and name information as strings
                    Pattern pattern = Pattern.compile(" ");
                    String path = ("file:///" + file.getAbsolutePath());
                    Matcher matcher = pattern.matcher(path);
                    path = matcher.replaceAll("%20");
                    path = path.replace("\\", "/");
                    String name = file.getName();
                    //get rid of file extensions
                    name = name.replace(".wma", "");
                    name = name.replace(".mp3", "");
                    name = name.replace(".wav", "");
                    //if playlist isn't empty, check for duplicates
                    if (playlist.getLengthOfPlaylist() > 0) {
                        Song duplicate = playlist.checkDuplicates(name);
                        if (duplicate == null) {//if returns null, doesn't exist in list
                            //call add method
                            playlist.addLastSong(name, path);
                        }
                    } else {
                        //add the song node to the end of the list
                        playlist.addLastSong(name, path);
                    }
                }
                //null a new head object (useful if user adds files multiple times)
                Song newHead = null;
                //set the new head object to the first place of the sorted list
                newHead = playlist.mergeSort(playlist.getHead());
                //clear the sorted list (in case of user adding songs multiple times)
                sortedPlaylist = new DoublyLinkedPlaylist<>();
                //use a temp node object to traverse the sorted head node and store songs in the sorted linked list
                Song temp = newHead;
                while (temp != null) {
                    sortedPlaylist.addLastSong(temp.getName().toString(), temp.getPath().toString());
                    temp = temp.next;
                }
                //display sorted playlist in the listViewSongList
                displayPlaylist();
                //set the current song to the first song in the list
                currentSong = newHead;
                //update message display
                lblMessage.setText("Success! Song(s) have been sorted and added to playlist!");
            }
        } catch (Exception ex) {
            lblMessage.setText("Error: Occured while adding songs");
        }
    }

    //method to display the linked list in the ListView
    @FXML
    private void displayPlaylist() {
        listViewSongList.getItems().clear();
        Song temp = sortedPlaylist.getHead();
        while (temp != null) {
            listViewSongList.getItems().add(temp);
            temp = temp.next;
        }
    }

    //method to delete one song(or node) from the linkedList
    @FXML
    private void btnDeleteSongAction(ActionEvent event) {
        try {
            Song selected = (Song) listViewSongList.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Song");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete '" + selected.getName() + "'?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                sortedPlaylist.Remove(selected.getName().toString());
                displayPlaylist();
                currentSong = sortedPlaylist.getHead();
                lblMessage.setText(selected.getName() + " deleted");
            }
            Status currentStatus = mediaPlayer.getStatus();
            if (currentStatus == Status.PLAYING) {
                mediaPlayer.stop();
            }
        } catch (NullPointerException e) {
            lblMessage.setText("Error: No track selected");
        }
    }

    //method to clear the listVied and the entire linked list
    @FXML
    private void btnClearPlaylistAction(ActionEvent event) {
        clearPlaylist();
    }

    //private method to clear playllist; used by load from csv and clear all songs button
    private void clearPlaylist() {
        try {
            if (sortedPlaylist.getHead() != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Playlist");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete the entire playlist?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    sortedPlaylist = new DoublyLinkedPlaylist<>();
                    displayPlaylist();
                    currentSong = null;
                    lblMessage.setText("Success! Playlist empty");
                    Status currentStatus = mediaPlayer.getStatus();
                    if (currentStatus == Status.PLAYING) {
                        mediaPlayer.stop();
                    }
                }
            } else {
                lblMessage.setText("No playlist to clear");
            }
        } catch (Exception e) {
            lblMessage.setText("Error: Cannot clear the playlist");
        }
    }

    //method to play song
    @FXML
    private void btnPlayAction(ActionEvent event) {
        try {
            //mediaPlayer.stop();
            currentSong = (Song) listViewSongList.getSelectionModel().getSelectedItem();
            playMusic(currentSong);
            btnPause.setText("Pause");
        } catch (NullPointerException e) {
            lblMessage.setText("Error: No track selected");
        }
    }

    @FXML
    private void playMusic(Song currentSong) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        if (currentSong != null) {
            media = new Media((String) currentSong.getPath());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
            lblMessage.setText(currentSong.getName() + " now playing...");
        }
    }//no try/catch required as it is called in every other method that calls this

    //method to pause current song
    @FXML
    private void btnPauseAction(ActionEvent event) {
        try {
            Status currentStatus = mediaPlayer.getStatus();
            if (currentStatus == Status.PLAYING) {
                mediaPlayer.pause();
                btnPause.setText("Resume");
                lblMessage.setText("Song Paused");
            } else if ((currentStatus == Status.PAUSED) || (currentStatus == Status.STOPPED)) {
                //check the time to resume (duration)
                var length = mediaPlayer.getCurrentTime();
                mediaPlayer.seek(length);
                mediaPlayer.play();
                btnPause.setText("Pause");
                lblMessage.setText("Song Resumed");
            }
        } catch (NullPointerException e) {
            lblMessage.setText("Please select a song to pause");
        }
    }

    //method to stop current songs playback
    @FXML
    private void btnStopAction(ActionEvent event) {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                lblMessage.setText("Song Stopped");
                btnPause.setText("Resume");
            }
        } catch (NullPointerException e) {
            lblMessage.setText("Please select a song to stop");
        }
    }

    //method to play the previous song in the linkedList
    @FXML
    private void btnPrevAction(ActionEvent event) {
        try {
            Song song = (Song) listViewSongList.getSelectionModel().getSelectedItem();
            if (song.prev != null) {
                Song prev = song.prev;
                playMusic(prev);
                listViewSongList.getSelectionModel().select(prev);
            } else {
                lblMessage.setText("Cannot play next song as this is the end of the playlist!");
            }
        } catch (NullPointerException e) {
            lblMessage.setText("Error: Please select a song to play next song");
        }
    }

    //method to play the next song in the linkedList
    @FXML
    private void btnNextAction(ActionEvent event) {
        try {
            Song song = (Song) listViewSongList.getSelectionModel().getSelectedItem();
            if (song.next != null) {
                Song next = song.next;
                playMusic(next);
                listViewSongList.getSelectionModel().select(next);
            } else {
                lblMessage.setText("Cannot play next song as this is the end of the playlist!");
            }
        } catch (NullPointerException e) {
            lblMessage.setText("Error: Please select a song to play next song");
        }
    }

    //method to perform a binary search on the linked List of song objects using comparator
    @FXML
    private void btnBinarySearchAction(ActionEvent event) {
        try {
            if (sortedPlaylist.getHead() != null) {
                String target = textFieldBinarySearch.getText();
                if (!(target.isBlank())) {
                    Song result = sortedPlaylist.binarySearch(target);
                    if (result != null) {
                        currentSong = result;
                        playMusic(currentSong);
                        lblSearchResult.setText(result.getName() + " was found!");
                    } else {
                        lblSearchResult.setText(target + " not found.");
                    }
                } else {
                    lblMessage.setText("Please enter something to search");
                }
            }
        } catch (NullPointerException e) {
            lblSearchResult.setText("Binary search error");
        }
    }

    @FXML
    private void handleSelectedIndexAction() {
        try {
            Song song = (Song) listViewSongList.getSelectionModel().getSelectedItem();
            playMusic(song);
        } catch (Exception e) {
            lblMessage.setText("Selected index error");
        }
    }

    //----------END MEDIA PLAYER METHODS-----------//
    //----------CSV METHODS-----------//
    //method to save the current playlist to .csv using third party library OpenCSV
    @FXML
    private void btnSavePlaylistAction(ActionEvent event) {
        if (sortedPlaylist.getHead() != null) {
            saveData(sortedPlaylist.getHead());
        } else {
            lblMessage.setText("Error: Please add songs to save file");
        }
    }

    //private method to save the playlist to csv file format
    private void saveData(Song head) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new ExtensionFilter(
                    "CSV Files", "*.csv"));
            fileChooser.setTitle("Save CSV file");
            //opens on this projects' directory
            fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            //use scene to open file chooser
            file = fileChooser.showSaveDialog(lblMessage.getScene().getWindow());
            //write data to file
            if (file != null) {
                String fileAsString = (file.toString());
                CSVWriter writer = new CSVWriter(new FileWriter(fileAsString));
                Song temp = head;
                while (temp != null) {
                    //System.out.print(temp.getName() + " ");
                    String[] data = {temp.getName().toString(), temp.getPath().toString()};
                    writer.writeNext(data);
                    temp = temp.next;
                }
                writer.close();
            }
            lblMessage.setText("Success! Playlist saved to CSV");
        } catch (IOException ex) {
            lblMessage.setText("Error writing to CSV file");
        }
    }

    //method to load a playlist from .csv using third party library OpenCSV
    @FXML
    private void btnLoadPlaylistAction(ActionEvent event) throws CsvException {
        try {
            //ask user to choose a compatible file
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new ExtensionFilter(
                    "CSV Files", "*.csv"));
            fileChooser.setTitle("Open CSV file");
            //opens on this projects' directory
            fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            //use scene to open file chooser
            file = fileChooser.showOpenDialog(lblMessage.getScene().getWindow());
            if (file != null) {
                String fileAsString = (file.toString());
                //Are you sure you want to load playlist? Data will be lost.
                clearPlaylist();
                //start reader for the selected csv
                CSVReader reader = new CSVReader(new FileReader(fileAsString));
                List<String[]> records = reader.readAll();
                Iterator<String[]> iterator = records.iterator();
                //reset the sorted playlist to null
                sortedPlaylist = new DoublyLinkedPlaylist<>();
                while (iterator.hasNext()) {
                    String[] record = iterator.next();
                    sortedPlaylist.addLastSong(record[0], record[1]);
                }
                reader.close();
                lblMessage.setText("Playlist loaded from CSV");
                displayPlaylist();
            }
        } catch (IOException ex) {
            lblMessage.setText("Error loading file from CSV");
        }
    }

    //----------END CSV METHODS-----------//
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //upon load, disable these functions until methods enable them appropriatley
        lblSearchResult.setText("");
        lblLoginStatus.setText("");
        lblMessage.setText("");
        btnLogOut.setDisable(true);
        disableMediaButtons();
        textFieldBinarySearch.setDisable(true);
        //add admin user by default
        userList.addAdminUser();
    }
}
