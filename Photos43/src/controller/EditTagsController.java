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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.tag.Tag;
import model.tag.TagType;
import model.user.User;
import model.user.UserDataStore;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EditTagsController implements Initializable {
    @FXML
    private Button addButton, backButton, addCaption, removeSelectedButton;


    @FXML
    private ComboBox addedTags, addedTagTypes;
    @FXML
    private TextField newTagType, newTag, newCap;
    @FXML
    private ImageView imageView;
    @FXML
    private CheckBox mult;
    private Album album;
    private User user;
    private List<Photo> photoList;
    private Photo selectedPhoto;
    private List<Tag> tags;

    @FXML
    private Text caption;
    private ObservableList<Photo> photoObservableList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            File file = new File(selectedPhoto.getFilePath());
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
            imageView.setPreserveRatio(true);
            tags = selectedPhoto.getTags();
            newTagType.setVisible(false);
            mult.setVisible(false);
            caption.setText(selectedPhoto.getCaption());
            int i = 0;
            while (i < tags.size()) {
                String s = tags.get(i).getTagType().getName() + ": " + tags.get(i).getValue();
                addedTags.getItems().add(s);
                System.out.println(tags.get(i));
                i++;
            }
            i = 0;

            List<TagType> tagTypes = user.getTagTypes();

            while (i < tagTypes.size()) {
                addedTagTypes.getItems().add(tagTypes.get(i).getName());
                i++;
            }
            addedTagTypes.getItems().add("Create new tag");
        });
    }

    /**
     * Back to Open Album
     * @param event
     * @throws IOException
     */
    @FXML
    protected void backButtonPressed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/OpenAlbum.fxml"));
        Parent parent = loader.load();
        OpenAlbumController controller = loader.getController();
        controller.setUser(user);
        controller.setAlbum(album);
        Scene scene = new Scene(parent);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.show();
    }

    /**
     * Add caption to photo
     * @param event
     * @throws IOException
     */
    @FXML
    protected void addCaptionButtonPressed(ActionEvent event) throws IOException {
        if(newCap.getLength() > 0)
        {
            selectedPhoto.setCaption(newCap.getText());
            UserDataStore.save(user);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/OpenAlbum.fxml"));
            Parent parent = loader.load();
            OpenAlbumController controller = loader.getController();
            controller.setUser(user);
            controller.setAlbum(album);
            Scene scene = new Scene(parent);
            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            appStage.setScene(scene);
            appStage.show();
        }
    }

    /**
     * Create new tag chosen, check if new tag
     * @param event
     * @throws IOException
     */
    @FXML
    protected void checkNew(ActionEvent event) throws IOException {

        if (addedTagTypes.getValue().equals("Create new tag")) {
            newTagType.setVisible(true);
            mult.setVisible(true);
        } else {
            newTagType.setVisible(false);
            mult.setVisible(false);
        }
    }

    /**
     * Add tag button pressed
     * @param event
     * @throws IOException
     */
    @FXML
    protected void addButtonPressed(ActionEvent event) throws IOException {
        TagType type = null;
        if (newTag == null || newTag.getText().trim().length() < 1) {
            return;
        }
        String tagName = newTag.getText();
        if (addedTagTypes.getValue().equals("Create new tag")) {
            if (newTagType.getText().trim().length() >= 1) {
                int i = 0;
                while (i < user.tagTypes.size()) {
                    if (newTag.getText().equals(user.tagTypes.get(i).getName())) {
                        return;
                    }
                    i++;
                }
                if (mult.isSelected()) {
                    type = new TagType(newTagType.getText(), true);
                } else {
                    type = new TagType(newTagType.getText(), false);
                }
                user.addTagType(type);
                //user.addTagType(type);
                UserDataStore.save(user);
            } else {
                return;
            }
        }
        else {
            // Object name = addedTagTypes.getSelectionModel().getSelectedItem();
            Object temp = addedTagTypes.getValue();
            if (temp == null) {
                return;
            }
            String tagType = (String) temp;
            int i = 0;
            while (i < user.tagTypes.size()) {
                if (tagType.equals(user.tagTypes.get(i).getName())) {
                    type = user.tagTypes.get(i);
                    break;
                }
                i++;
            }
            if(type == null)
            {
                return;
            }
            if(!type.isCanHaveMultiple())
            {
                i = 0;
                while (i < selectedPhoto.getTags().size())
                {
                    if(type == selectedPhoto.getTags().get(i).getTagType())
                    {
                        return;
                    }
                    i++;
                }
            }


        }
        if(type.getName().contains(":")) { return;}
            Tag newTag = new Tag(type,tagName);
        int i = 0;
        while (i < selectedPhoto.getTags().size())
        {
            if(tagName.equals(selectedPhoto.getTags().get(i).getValue()) && type.equals(selectedPhoto.getTags().get(i).getTagType()))
            {
                return;
            }
            i++;
        }
            selectedPhoto.getTags().add(newTag);
            UserDataStore.save(user);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EditTags.fxml"));
            List<Photo> photoList = new ArrayList<>(album.getPhotos());
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            app_stage.setScene(scene);
            app_stage.show();
            EditTagsController controller = loader.getController();
            controller.setAlbum(album);
            controller.setUser(user);
            controller.setPhoto(selectedPhoto);
            controller.setPhotoList(photoList);
    }

    /**
     * Remove selected object
     * @param event
     * @throws IOException
     */
    @FXML
    protected void removeSelectedButtonPressed(ActionEvent event) throws IOException {
        if(addedTags.getValue() == null) { return; }
            String s = addedTags.getValue().toString();
        System.out.println(s);
        String[] whole;
        whole = s.split(":");
        String tag = whole[1].substring(1,whole[1].length());
        String type = whole[0];
        int i = 0;
        while(i < selectedPhoto.getTags().size())
        {
            if(selectedPhoto.getTags().get(i).getValue().equals(tag) && selectedPhoto.getTags().get(i).getTagType().getName().equals(type)){
                selectedPhoto.removeTag(user,selectedPhoto.getTags().get(i));
                UserDataStore.save(user);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EditTags.fxml"));
                List<Photo> photoList = new ArrayList<>(album.getPhotos());
                Parent parent = loader.load();
                Scene scene = new Scene(parent);
                Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                app_stage.setScene(scene);
                app_stage.show();
                EditTagsController controller = loader.getController();
                controller.setAlbum(album);
                controller.setUser(user);
                controller.setPhoto(selectedPhoto);
                controller.setPhotoList(photoList);
            }
            i++;
        }

    }

    /**
     *
     * @param album
     */
    public void setAlbum(Album album) {
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
