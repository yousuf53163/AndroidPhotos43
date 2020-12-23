/**
 * @author Joseph Caponegro & Mohammad Khan
 */

package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.user.User;

import java.net.URL;
import java.util.ResourceBundle;

public class TransferPhotoController implements Initializable
{
    @FXML
    private ListView<Album> albumListView;

    private ObservableList<Album> albumObservableList;
    private User user;
    private Album sourceAlbum;
    private Photo photo;
    private boolean deleteSourcePhoto;
    private ObservableList<Photo> photoObservableList;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        Platform.runLater(() -> {
            albumObservableList = FXCollections.observableArrayList(user.getAlbums());
            setAlbumObservableListFormat();
            albumListView.setItems(albumObservableList);
        });
    }

    /**
     * Copy photo
     * @param event
     */
    @FXML
    protected void copyPhotoButtonPressed(ActionEvent event)
    {
        Album destinationAlbum = albumListView.getSelectionModel().getSelectedItem();
        Photo photoCopy = new Photo(photo);
        destinationAlbum.addPhoto(photoCopy);

        if (deleteSourcePhoto) {
            sourceAlbum.removePhoto(photo);
            photoObservableList.remove(photo);
        }
        Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        currentStage.close();
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
     * @param album
     */
    public void setSourceAlbum(Album album) {
        this.sourceAlbum = album;
    }

    /**
     *
     * @param photo
     */
    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    /**
     *
     * @param photos
     */
    public void setPhotoObservableList(ObservableList<Photo> photos) {
        this.photoObservableList = photos;
    }

    /**
     *
     * @param deleteSourcePhoto
     */
    public void setDeleteSourcePhoto(boolean deleteSourcePhoto) {
        this.deleteSourcePhoto = deleteSourcePhoto;
    }

    private void setAlbumObservableListFormat()
    {
        albumListView.setCellFactory(param -> new ListCell<Album>() {
            @Override
            protected void updateItem(Album album, boolean empty) {
                super.updateItem(album, empty);

                if (empty || album == null) {
                    setText(null);
                } else {
                    setText(album.getName());
                }
            }
        });
    }
}
