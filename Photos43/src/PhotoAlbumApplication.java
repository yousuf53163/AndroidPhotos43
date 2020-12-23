/**
 * @author Joseph Caponegro & Mohammad Khan
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.user.User;
import model.user.UserDataStore;

import java.io.File;
import java.util.ArrayList;

public class PhotoAlbumApplication extends Application
{
 
       
    public void createStockUser()
    {
        if (UserDataStore.getUser("stock") == null)
        {
            User user = new User("stock");
            Album stockAlbum = user.addAlbum("stock");
            String defaultStockPhotoDirectoryPath = System.getProperty("user.dir") + "/data/stock_photos";
            stockAlbum.addPhoto(new Photo(new File(defaultStockPhotoDirectoryPath + "/chikorita.jpg"), "Chikorita, Pokemon", new ArrayList<>()));
            stockAlbum.addPhoto(new Photo(new File(defaultStockPhotoDirectoryPath + "/deletos.jpg"), "Delet this.", new ArrayList<>()));
            stockAlbum.addPhoto(new Photo(new File(defaultStockPhotoDirectoryPath + "/sPoNgEbOb.jpg"), "Spongy boy", new ArrayList<>()));
            stockAlbum.addPhoto(new Photo(new File(defaultStockPhotoDirectoryPath + "/puppy.jpg"), "Bork", new ArrayList<>()));
            stockAlbum.addPhoto(new Photo(new File(defaultStockPhotoDirectoryPath + "/snorlax.jpg"), "Issa sad boy", new ArrayList<>()));
            stockAlbum.addPhoto(new Photo(new File(defaultStockPhotoDirectoryPath + "/recorder.jpg"), "Toot toot", new ArrayList<>()));
            stockAlbum.addPhoto(new Photo(new File(defaultStockPhotoDirectoryPath + "/IMG_7716.jpg"), "Yike", new ArrayList<>()));
            UserDataStore.save(user);
        }
    }
    
	
    /**
     *  Driver method
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        //createStockUser();

        // Load the fxml file
        Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
        primaryStage.setTitle("Photo Album");
        primaryStage.setScene(new Scene(root, 500, 300));
        primaryStage.show();

    }
    public static void main(String[] args) {
        launch(args);
    }
}