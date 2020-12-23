/**
 * @author Joseph Caponegro & Mohammad Khan
 */

package controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.user.User;
import model.user.UserDataStore;
import util.AlertHelper;

import java.io.IOException;

public class LoginController
{

    Stage prevStage;

    public void setPrevStage(Stage stage)
    {
        this.prevStage = stage;
    }


    @FXML
    private TextField userID;
    @FXML
    private Button login;


    /**
     * Login with valid username and display album list
     * @param event
     * @throws IOException
     */
    @FXML
    protected void handleLogin(ActionEvent event) throws IOException
    {
        Window owner = login.getScene().getWindow();
        Parent parent;
        if (userID.getText().isEmpty())
        {
            AlertHelper.showError(owner, "Form Error!", "Username can not be blank!");
            userID.clear();
            return;
        }

        String userName = userID.getText();
        User user = UserDataStore.getUser(userName);

        if ("admin".equals(userName))
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Admin.fxml"));
            parent = loader.load();

            Scene scene = new Scene(parent);
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            app_stage.setScene(scene);
            app_stage.show();

        }
        else if (user != null)
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserHome.fxml"));
            parent = loader.load();

            UserHomeController controller = loader.getController();
            controller.setUser(user);

            Scene scene = new Scene(parent);
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            app_stage.setScene(scene);
            app_stage.show();
        }
        else
        {
            AlertHelper.showError(owner, "Form Error!", "Username does not exist. Confirm the user exists from the admin view.");
            userID.clear();
        }
    }
}