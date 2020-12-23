/**
 * @author Joseph Caponegro & Mohammad Khan
 */

package controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Album;
import model.user.User;
import model.user.UserDataStore;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;


public class UserHomeController implements Initializable
{

    private User user;

    @FXML
    private Button deleteAlbum, addAlbum, renameAlbum, search, openAlbum, logout;

    @FXML
    private TableView<Album> albumTableView;
    @FXML
    private TableColumn<Album, String> albumNameColumn;
    @FXML
    private TableColumn<Album, Integer> numPhotosColumn;
    @FXML
    private TableColumn<Album, Date> earliestColumn;
    @FXML
    private TableColumn<Album, Date> latestColumn;

    @FXML
    private TextField newAlbumNameInput;
    @FXML
    private TextField renameAlbumNameInput;


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        Platform.runLater(() -> {
            final ObservableList<Album> albumTableData = FXCollections.observableArrayList(user.getAlbums());
            albumNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name")
            );
            numPhotosColumn.setCellValueFactory(
                p -> new SimpleIntegerProperty(p.getValue().numberOfPhotos()).asObject()
            );

            earliestColumn.setCellValueFactory(
                p -> new SimpleObjectProperty<>(p.getValue().earliestPhoto())
            );

            latestColumn.setCellValueFactory(
                p -> new SimpleObjectProperty<>(p.getValue().latestPhoto())
            );
            albumTableView.setItems(albumTableData);
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
     * Add album
     * @param event
     * @throws IOException
     */
    @FXML
    protected void handleAddAlbum(ActionEvent event) throws IOException{
        String name = newAlbumNameInput.getText();
        if (!name.isEmpty())
        {
            Album album = user.addAlbum(name);
            UserDataStore.save(user);
            ObservableList<Album> allAlbums = albumTableView.getItems();
            allAlbums.add(album);
        }
    }

    /**
     * Delete album
     * @param event
     * @throws IOException
     */
    @FXML
    protected void handleDeleteAlbum(ActionEvent event) throws IOException {
        ObservableList<Album> albumsToDelete, allAlbums;
        allAlbums = albumTableView.getItems();
        albumsToDelete = albumTableView.getSelectionModel().getSelectedItems();

        for (Album album : albumsToDelete) {
            user.removeAlbum(album.getName());
            UserDataStore.save(user);
        }
        albumsToDelete.forEach(allAlbums::remove);
    }

    /**
     * Open album
     * @param event
     * @throws IOException
     */
    @FXML
    protected void handleOpenAlbum(ActionEvent event) throws IOException{

        List<Album> albumsToOpen = new ArrayList<>(albumTableView.getSelectionModel().getSelectedItems());
        Album album = albumsToOpen.get(0);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/OpenAlbum.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(scene);
        app_stage.show();

        OpenAlbumController controller = loader.getController();
        controller.setUser(user);
        controller.setAlbum(album);
    }

    /**
     * Rename album with text entered in field
     * @param event
     * @throws IOException
     */
    @FXML
    protected void handleRenameAlbum(ActionEvent event) throws IOException{
        List<Album> albumsToRename = new ArrayList<>(albumTableView.getSelectionModel().getSelectedItems());
        for (Album album : albumsToRename) {
            String name = renameAlbumNameInput.getText();
            if (!name.isEmpty())
            {
                album.setName(name);
            }
            UserDataStore.save(user);
        }
        albumTableView.refresh();
    }

    /**
     * Logout of user
     * @param event
     * @throws IOException
     */
    @FXML
    protected void handleLogOut(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(scene);
        app_stage.show();
    }

    /**
     * Search
     * @param event
     * @throws IOException
     */
    @FXML
    protected void searchButtonPressed(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Search.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(scene);
        app_stage.show();

        SearchController controller = loader.getController();
        controller.setUser(user);
    }
}