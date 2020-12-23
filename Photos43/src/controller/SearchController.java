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
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.tag.Tag;
import model.user.User;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class SearchController implements Initializable {

    @FXML
    private TextField d1, m1, y1, d2, m2, y2, tag1, tag2;
    @FXML
    private Button back, searchDate, searchTag;
    @FXML
    private ComboBox tagtype1, tagtype2, choice;
    private List<Album> albums;
    private User user;
    private List<Photo> photoList;
    private Photo selectedPhoto;
    private List<Photo> searchedPhotos;
    private ObservableList<Photo> photoObservableList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            tagtype2.setVisible(false);
            tag2.setVisible(false);
            choice.getItems().add("Search with one");
            choice.getItems().add("Or");
            choice.getItems().add("And");
            d1.setText("00");
            m1.setText("00");
            y1.setText("0000");
            String today = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            System.out.println(today);
            d2.setText(today.substring(0, 2));
            m2.setText(today.substring(3, 5));
            y2.setText(today.substring(6, today.length()));
            int i = 0;
            while (i < user.getTagTypes().size()) {
                tagtype1.getItems().add(user.tagTypes.get(i).getName());
                tagtype2.getItems().add(user.getTagTypes().get(i).getName());
                i++;
            }
        });
    }

    /**
     * Go back to User Home
     * @param event
     * @throws IOException
     */
    @FXML
    protected void backButtonPressed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserHome.fxml"));
        Parent parent = loader.load();
        UserHomeController controller = loader.getController();
        controller.setUser(user);
        Scene scene = new Scene(parent);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.show();
    }

    /**
     * Search by tag
     * @param event
     * @throws IOException
     */
    @FXML
    protected void searchTagPressed(ActionEvent event) throws IOException {
        int a,b,c, i, j, k;
        searchedPhotos = new ArrayList<>();
        if (tagtype1.getValue() == null) {
            return;
        }
        String tt1 = tagtype1.getValue().toString();
        if (tag1.getText() == null) {
            return;
        }
        if(choice.getValue() == null)
        {
            return;
        }
        String t1 = tag1.getText();
        for (i = 0; i < user.getAlbums().size(); i++) {
            for (j = 0; j < user.getAlbums().get(i).numberOfPhotos(); j++) {
                Photo currPhoto = user.getAlbums().get(i).getPhotos().get(j);
                for (k = 0; k < currPhoto.getTags().size(); k++) {
                    Tag currTag = currPhoto.getTags().get(k);
                    String type = currTag.getTagType().getName();
                    String tag = currTag.getValue();
                    if (tt1.equals(type) && t1.equals(tag)) {
                        if (choice.getValue().equals("Search with one")) {
                            searchedPhotos.add(currPhoto);
                        }
                        if (choice.getValue().equals("Or")) {
                            searchedPhotos.add(currPhoto);
                        }
                        if (choice.getValue().equals("And")) {
                            if (tagtype2.getValue() == null) {
                                return;
                            }
                            String tt2 = tagtype2.getValue().toString();
                            if (tag2.getText() == null) {
                                return;
                            }
                            String t2 = tag2.getText();
                            for (a = 0; a < currPhoto.getTags().size(); a++) {
                                Tag currTag2 = currPhoto.getTags().get(a);
                                String type2 = currTag2.getTagType().getName();
                                String tag2 = currTag.getValue();
                                if (tt2.equals(type2) && tag.equals(tag2)) {
                                    searchedPhotos.add(currPhoto);
                                }
                            }

                        }
                    }
                    else if(choice.getValue().equals("Or"))
                    {
                                    String tt2 = tagtype2.getValue().toString();
                                    if (tag2.getText() == null) {
                                        return;
                                    }
                                    String t2 = tag2.getText();
                                    for (a = 0; a < currPhoto.getTags().size(); a++) {
                                        Tag currTag2 = currPhoto.getTags().get(a);
                                        String type2 = currTag2.getTagType().getName();
                                        String tag2 = currTag2.getValue();
                                        if (tt2.equals(type2) && t2.equals(tag2)) {
                                            searchedPhotos.add(currPhoto);
                                        }
                                    }
                    }
                }
            }
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PhotoSearch.fxml"));
        Parent parent = loader.load();
        SearchResultController controller = loader.getController();
        controller.setUser(user);
        controller.setPhoto(searchedPhotos.get(0));
        controller.setPhotoList(searchedPhotos);
        Scene scene = new Scene(parent);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.show();
    }

    /**
     *
     * @param event
     * @throws IOException
     */
    @FXML
    protected void checkBoth(ActionEvent event) throws IOException {

        if (!choice.getValue().equals("Search with one")) {
            tagtype2.setVisible(true);
            tag2.setVisible(true);
        } else {
            tagtype2.setVisible(false);
            tag2.setVisible(false);
        }
    }

    /**
     * Search by date
     * @param event
     * @throws IOException
     */
    @FXML
    protected void searchDatePressed(ActionEvent event) throws IOException {
        if (d1.getText().length() < 1 || d2.getText().length() < 1 || m1.getText().length() < 2 || m2.getText().length() < 2 || y1.getText().length() < 4 || y2.getText().length() < 4) {
            System.out.println("1");
            return;
        }
        int i = 0;
        Integer date1, date2;
        try {
            date1 = Integer.parseInt(d1.getText()) + Integer.parseInt(m1.getText()) + Integer.parseInt(y1.getText());
            date2 = Integer.parseInt(d2.getText()) + Integer.parseInt(m2.getText()) + Integer.parseInt(y2.getText());
        } catch (Exception e) {
            System.out.println("2");
            return;
        }

        Calendar fromcal = Calendar.getInstance();
        fromcal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(d1.getText()));
        fromcal.set(Calendar.MONTH, Integer.parseInt(m1.getText()));
        fromcal.set(Calendar.YEAR, Integer.parseInt(y1.getText()));
        fromcal.set(Calendar.MILLISECOND, 0);
        Calendar tocal = Calendar.getInstance();
        tocal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(d2.getText()));
        tocal.set(Calendar.MONTH, Integer.parseInt(m2.getText()));
        tocal.set(Calendar.YEAR, Integer.parseInt(y2.getText()));
        tocal.set(Calendar.MILLISECOND, 0);

        albums = user.getAlbums();
        searchedPhotos = new ArrayList<>();
        int j;
        System.out.println("3");
        for (i = 0; i < albums.size(); i++) {
            for (j = 0; j < albums.get(i).numberOfPhotos(); j++) {
                Photo currphoto = albums.get(i).getPhotos().get(j);
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String pdate = df.format(currphoto.getDate());
                Calendar currDate = Calendar.getInstance();
                //currDate.setTime(currphoto.getDate());
                currDate.set(Calendar.YEAR, Integer.parseInt(pdate.substring(0, 4)));
                currDate.set(Calendar.MONTH, (Integer.parseInt(pdate.substring(5, 7))));
                currDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(pdate.substring(8, 10)));
                currDate.set(Calendar.MILLISECOND, 0);
                if (fromcal.before(currDate) && tocal.after(currDate)) {
                    searchedPhotos.add(currphoto);
                }
            }
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PhotoSearch.fxml"));
        Parent parent = loader.load();
        SearchResultController controller = loader.getController();
        controller.setUser(user);
        controller.setPhoto(searchedPhotos.get(0));
        controller.setPhotoList(searchedPhotos);
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
