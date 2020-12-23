/**
 * @author Joseph Caponegro & Mohammad Khan
 */

package util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;
import java.util.Optional;

public class AlertHelper
{

    public interface ConfirmableAction {
        void onConfirm();
    }

    /**
     * Display error message
     * @param owner
     * @param title
     * @param message
     */
    public static void showError(Window owner, String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    /**
     * Show confirmation message
     * @param owner
     * @param title
     * @param message
     * @param action
     */
    public static void showConfirmation(Window owner, String title, String message, ConfirmableAction action) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            action.onConfirm();
        }
    }
}