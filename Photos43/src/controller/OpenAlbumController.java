/**
 * @author Joseph Caponegro & Mohammad Khan
 */

package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Album;
import model.Photo;
import model.user.User;
import util.AlertHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;


public class OpenAlbumController implements Initializable
{

    @FXML
    private Button addPhotoButton,backButton,deletePhotoButton,copyPhotoButton,movePhotoButton, editTagButton;

    @FXML
    private ListView<Photo> photoListView;

    private User user;
    private Album album;
    private ObservableList<Photo> photoObservableList;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        Platform.runLater(() -> {
            photoObservableList = FXCollections.observableArrayList(album.getPhotos());
            setPhotoObservableListFormat();
            photoListView.setItems(photoObservableList);
        });
    }

    /**
     *
     * @param user
     */
    public void setUser(User user)
    {
        this.user = user;
    }

    /**
     * @param album album
     */
    public void setAlbum(Album album)
    {
        this.album = album;
    }

    /**
     * Format list of photos
     */
    private void setPhotoObservableListFormat()
    {
        photoListView.setCellFactory(param -> new ListCell<Photo>() {
            @Override
            protected void updateItem(Photo photo, boolean empty) {
                super.updateItem(photo, empty);

                if (empty || photo == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    ImageView imageView = createImageView(photo);
                    setGraphic(imageView);
                    setText(photo.getCaption());
                }
            }
        });
    }

    /**
     * Create the image view for photo and populate it
     * @param photo
     * @return
     */
    private ImageView createImageView(Photo photo)
    {
        try
        {
            FileInputStream inputStream = new FileInputStream(photo.getFilePath());
            Image image = new Image(inputStream);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(100);
            imageView.setPreserveRatio(true);
            imageView.setImage(image);
            return imageView;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Prompt system file chooser
     * @return
     */
    private File showSingleFileChooser() {
        FileChooser fileChooser = new FileChooser();
        return fileChooser.showOpenDialog(null);
    }

    /**
     * Add photo button pressed
     * @param event
     * @throws IOException
     */
    @FXML
    protected void addButtonPressed(ActionEvent event) throws IOException{
        Window owner = addPhotoButton.getScene().getWindow();
        File selectedPhoto = showSingleFileChooser();
        if (selectedPhoto.exists())
        {
            Photo newPhoto = new Photo(selectedPhoto);
            album.addPhoto(newPhoto);
            photoObservableList.add(newPhoto);
            photoListView.getSelectionModel().select(photoObservableList.indexOf(newPhoto));
        }
        else
        {
            AlertHelper.showError(owner, "Form Error!", "Could not add photo.");
        }
    }

    /**
     * Goes back to User Home
     * @param event
     * @throws IOException
     */
    @FXML
    protected void backButtonPressed(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserHome.fxml"));
        Parent parent = loader.load();
        UserHomeController controller = loader.getController();
        controller.setUser(user);
        Scene scene = new Scene(parent);
        Stage appStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.show();
    }

    /**
     * Delete selected photo
     * @param event
     * @throws IOException
     */
    @FXML
    protected void deleteButtonPressed(ActionEvent event) throws IOException{
        AlertHelper.ConfirmableAction deleteAction = () -> {
            deletePhoto();
        };

        Window owner = deletePhotoButton.getScene().getWindow();
        AlertHelper.showConfirmation(owner, "Are you sure you want to delete this photo?",
            "This action cannot be undone.", deleteAction);
    }

    /**
     * View photo in full view
     * @param event
     * @throws IOException
     */
    @FXML
    protected void viewPhotoButtonPressed(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FullPhotoDisplay.fxml"));
        List<Photo> photoList = new ArrayList<>(album.getPhotos());
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(scene);
        app_stage.show();

        Photo selectedPhoto = photoListView.getSelectionModel().getSelectedItem();
        FullPhotoDisplayController controller = loader.getController();
        controller.setAlbum(album);
        controller.setUser(user);
        controller.setPhoto(selectedPhoto);
        controller.setPhotoList(photoList);
    }

    /**
     * Edit tags on selected photo
     * @param event
     * @throws IOException
     */
    @FXML
    protected void editTagButtonPressed(ActionEvent event) throws IOException{
        System.out.println("hit button");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EditTags.fxml"));
        List<Photo> photoList = new ArrayList<>(album.getPhotos());
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(scene);
        app_stage.show();

        Photo selectedPhoto = photoListView.getSelectionModel().getSelectedItem();
        EditTagsController controller = loader.getController();
        controller.setAlbum(album);
        controller.setUser(user);
        controller.setPhoto(selectedPhoto);
        controller.setPhotoList(photoList);
    }

    /**
     * Copy photo
     * @param event
     * @throws IOException
     */
    @FXML
    protected void copyPhotoButtonPressed(ActionEvent event) throws IOException{
        transferPhoto(event, false);
    }

    /**
     *
     * @param event
     * @throws IOException
     */
    @FXML
    protected void movePhotoButtonPressed(ActionEvent event) throws IOException{
        transferPhoto(event, true);
    }

    /**
     * Move photo
     * @param event
     * @param deleteSourcePhoto
     * @throws IOException
     */
    private void transferPhoto(ActionEvent event, boolean deleteSourcePhoto) throws IOException {
        Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TransferPhoto.fxml"));
        Parent root = loader.load();
        TransferPhotoController controller = loader.getController();
        controller.setUser(user);
        controller.setSourceAlbum(album);
        controller.setDeleteSourcePhoto(deleteSourcePhoto);
        Photo photo = photoListView.getSelectionModel().getSelectedItem();
        controller.setPhoto(photo);
        controller.setPhotoObservableList(photoObservableList);

        dialog.setScene(new Scene(root));
        dialog.show();
    }

    /**
     * Delete selected photo
     */
    private void deletePhoto()
    {
        ObservableList<Photo> itemselected, allitems;
        allitems = photoListView.getItems();
        itemselected = photoListView.getSelectionModel().getSelectedItems();
        int index = photoListView.getSelectionModel().getSelectedIndex();
        album.removePhoto(allitems.get(index));
        allitems.removeAll(itemselected);
        photoListView.getSelectionModel().select(index);
    }
}