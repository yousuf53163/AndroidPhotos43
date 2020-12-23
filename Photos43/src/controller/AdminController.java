/**
 * @author Joseph Caponegro & Mohammad Khan
 */

package controller;

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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.user.User;
import model.user.UserDataStore;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class AdminController implements Initializable
{
    @FXML
    private TextField newUser;
    @FXML
    private Button addBTN, DeleteBTN, logout;

    @FXML
    private ListView<String> userListView;
    public ObservableList<String> userList = FXCollections.observableArrayList();

    /**
     *
     * @param location
     * @param resources
     */
    @FXML
    public void initialize(URL location, ResourceBundle resources)
    {
        List<User> loadedUsers = UserDataStore.loadUsers();
        for (User loadedUser : loadedUsers) {
            if (!loadedUser.isStock())
            {
                userList.add(loadedUser.getName());
            }
        }
        userListView.setItems(userList);
    }

    /**
     * Add new user to user list
     * @param event
     * @throws IOException
     */
    @FXML
    protected void addNewUser(ActionEvent event) throws IOException
    {
        String userName = newUser.getText();
        User user = new User(userName);
        Album newStockAlbum = user.addAlbum("stock");

        // Get the stock user. Get all their photos from their "Stock" album.
        User stockUser = UserDataStore.getUser("stock");
        if (stockUser != null) {
            Album originalStockAlbum = stockUser.findAlbum("stock");
            for (Photo stockPhoto : originalStockAlbum.getPhotos()) {
                newStockAlbum.addPhoto(stockPhoto);
            }
        }

        UserDataStore.save(user);

        userList.add(userName);
        userListView.setItems(userList);

        System.out.println(userName);
        newUser.clear();
    }

    /**
     * Delete user from user list
     * @param event
     * @throws IOException
     */
    @FXML
    protected void DeleteUser(ActionEvent event) throws IOException
    {
        ObservableList<String> itemsToDelete, allItems;
        allItems = userListView.getItems();
        itemsToDelete = userListView.getSelectionModel().getSelectedItems();

        for (String item : itemsToDelete) {
            // Never delete stock user.
            if ("stock".equals(item)) {
                continue;
            }

            User x = new User(item);
            UserDataStore.delete(x);
        }

        System.out.println(itemsToDelete.toString());
        itemsToDelete.forEach(allItems::remove);
    }

    /**
     * Logout of admin view
     * @param event
     * @throws IOException
     */
    @FXML
    protected void adminLogOut(ActionEvent event) throws IOException
    {
        Parent parent;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
        parent = loader.load();

        Scene scene = new Scene(parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(scene);
        app_stage.show();
    }
}
