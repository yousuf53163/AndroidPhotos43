/**
 * @author Joseph Caponegro & Mohammad Khan
 */

package controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.user.User;
import model.user.UserDataStore;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SearchResultController implements Initializable {
    @FXML
    private Button backButton, previousButton, nextButton, createAlbum;

    @FXML
    private ImageView imageView;
    private Album album;
    private User user;
    private List<Photo> photoList;
    private Photo selectedPhoto;
    @FXML
    private ObservableList<Photo> photoObservableList;

    @FXML
    private TextField albumName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            File file = new File(selectedPhoto.getFilePath());
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
            imageView.setPreserveRatio(true);
            albumName.setText("Album name");
        });
    }

    /**
     * Create new album
     * @param event
     * @throws IOException
     */
    @FXML
    protected void createAlbumButtonPressed(ActionEvent event) throws IOException {
        System.out.println("hello");
        String name = albumName.getText();
        if (!name.isEmpty()) {
            Album album = user.addAlbum(name);
            UserDataStore.save(user);
            for (int i = 0; i < photoList.size(); i++) {
                album.addPhoto(photoList.get(i));
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserHome.fxml"));
            Parent parent = loader.load();

            UserHomeController controller = loader.getController();
            controller.setUser(user);

            Scene scene = new Scene(parent);
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            app_stage.setScene(scene);
            app_stage.show();
        }
    }

    /**
     * Go to previous result
     * @param event
     * @throws IOException
     */
    @FXML
    protected void previousButtonPressed(ActionEvent event) throws IOException {
        int i = 0;
        while (i < photoList.size()) {
            Photo curr = photoList.get(i);
            if (curr == selectedPhoto) {
                i = i - 1;
                break;
            }
            i++;
        }
        if (i < 0) {
            return;
        }
        Photo currPhoto = photoList.get(i);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PhotoSearch.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(scene);
        app_stage.show();
        SearchResultController controller = loader.getController();
        controller.setUser(user);
        controller.setPhoto(currPhoto);
        controller.setPhotoList(photoList);
    }

    /**
     * Go to next result
     * @param event
     * @throws IOException
     */
    @FXML
    protected void nextButtonPressed(ActionEvent event) throws IOException {
        int i = 0;
        while (i < photoList.size()) {
            Photo curr = photoList.get(i);
            if (curr == selectedPhoto) {
                i = i + 1;
                break;
            }
            i++;
        }
        if (i >= photoList.size()) {
            return;
        }
        Photo currPhoto = photoList.get(i);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PhotoSearch.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(scene);
        app_stage.show();
        SearchResultController controller = loader.getController();
        controller.setUser(user);
        controller.setPhoto(currPhoto);
        controller.setPhotoList(photoList);
    }

    /**
     * Go back to search home
     * @param event
     * @throws IOException
     */
    @FXML
    protected void backButtonPressed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Search.fxml"));
        Parent parent = loader.load();
        SearchController controller = loader.getController();
        controller.setUser(user);
        Scene scene = new Scene(parent);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.show();
    }

    /**
     *
     * @param photo
     */
    public void setPhoto(Photo photo) {
        this.selectedPhoto = photo;
    }

    /**
     *
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     *
     * @param photos
     */
    public void setPhotoList(List<Photo> photos) {
        this.photoList = photos;
    }
}
