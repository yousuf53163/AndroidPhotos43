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
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.tag.Tag;
import model.user.User;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FullPhotoDisplayController implements Initializable
{
    @FXML
    private Button backButton, previousButton, nextButton;

    @FXML
    private ImageView imageView;
    private Album album;
    private User user;
    private List<Photo> photoList;
    private Photo selectedPhoto;
    @FXML
    private Text caption, date;

    @FXML
    private TextArea tags;
    private ObservableList<Photo> photoObservableList;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        Platform.runLater(() -> {
            File file = new File(selectedPhoto.getFilePath());
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
            imageView.setPreserveRatio(true);
            caption.setText(selectedPhoto.getCaption());
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String pdate = df.format(selectedPhoto.getDate());
            pdate =  pdate.substring(5, 7) + "/" +  pdate.substring(8, 10) + "/" + pdate.substring(0, 4);
            System.out.println(pdate);
            date.setText(pdate);
            Tag t;
            String ts = "";
            for(int i = 0; i<selectedPhoto.getTags().size();i++)
            {
                t = selectedPhoto.getTags().get(i);
                ts += t.getTagType().getName() + ":" + t.getValue() + "\n";
            }
            tags.setText(ts);
        });
    }

    /**
     * Back to Open Album
     * @param event
     * @throws IOException
     */
    @FXML
    protected void backButtonPressed(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/OpenAlbum.fxml"));
        Parent parent = loader.load();
        OpenAlbumController controller = loader.getController();
        controller.setUser(user);
        controller.setAlbum(album);
        Scene scene = new Scene(parent);
        Stage appStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.show();
    }

    /**
     * View previous photo in album
     * @param event
     * @throws IOException
     */
    @FXML
    protected void previousButtonPressed(ActionEvent event) throws IOException
    {
        int i = 0;
        while(i < photoList.size())
        {
            Photo curr = photoList.get(i);
            if(curr == selectedPhoto)
            {
                i = i -1;
                break;
            }
            i++;
        }
        if(i < 0)
        {
            return;
        }
        Photo currPhoto = photoList.get(i);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FullPhotoDisplay.fxml"));
        List<Photo> photoList = new ArrayList<>(album.getPhotos());
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(scene);
        app_stage.show();

        FullPhotoDisplayController controller = loader.getController();
        controller.setAlbum(album);
        controller.setUser(user);
        controller.setPhoto(currPhoto);
        controller.setPhotoList(photoList);
    }

    /**
     * View next photo in album
     * @param event
     * @throws IOException
     */
    @FXML
    protected void nextButtonPressed(ActionEvent event) throws IOException
    {
        int i = 0;
        while(i < photoList.size())
        {
            Photo curr = photoList.get(i);
            if(curr == selectedPhoto)
            {
                i = i + 1;
                break;
            }
            i++;
        }
        if(i >= photoList.size())
        {
            return;
        }
        Photo currPhoto = photoList.get(i);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FullPhotoDisplay.fxml"));
        List<Photo> photoList = new ArrayList<>(album.getPhotos());
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        app_stage.setScene(scene);
        app_stage.show();

        FullPhotoDisplayController controller = loader.getController();
        controller.setAlbum(album);
        controller.setUser(user);
        controller.setPhoto(currPhoto);
        controller.setPhotoList(photoList);
    }

    /**
     *
     * @param album
     */
    public void setAlbum(Album album)
    {
        this.album = album;
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
