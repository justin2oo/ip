package peanutbuttercat;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Represents one chat message with its speaker's avatar.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;

    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image image) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DialogBox.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        dialog.setText(text);
        displayPicture.setImage(image);
    }

    /**
     * Reverses the layout so that the avatar appears on the left for a reply.
     */
    private void flip() {
        ObservableList<Node> children = FXCollections.observableArrayList(getChildren());
        Collections.reverse(children);
        getChildren().setAll(children);
        setAlignment(Pos.TOP_LEFT);
        dialog.getStyleClass().add("reply-label");
    }

    /** Applies a soft reply color that reflects the command's purpose. */
    private void applyCommandStyle(CommandType commandType) {
        switch (commandType) {
            case TODO:
            case DEADLINE:
            case EVENT:
                dialog.getStyleClass().add("add-label");
                break;
            case MARK:
            case UNMARK:
                dialog.getStyleClass().add("status-label");
                break;
            case DELETE:
                dialog.getStyleClass().add("delete-label");
                break;
            case UNKNOWN:
                dialog.getStyleClass().add("error-label");
                break;
            case LIST:
            case FIND:
            case ON:
                dialog.getStyleClass().add("query-label");
                break;
            case BYE:
                dialog.getStyleClass().add("farewell-label");
                break;
            default:
                break;
        }
    }

    /**
     * Creates a dialog box for a message entered by the user.
     *
     * @param text The message text.
     * @param image The user's avatar.
     * @return A right-aligned user dialog box.
     */
    public static DialogBox getUserDialog(String text, Image image) {
        return new DialogBox(text, image);
    }

    /**
     * Creates a dialog box for a reply from PeanutButterCat.
     *
     * @param text The reply text.
     * @param image The PeanutButterCat avatar.
     * @return A left-aligned PeanutButterCat dialog box.
     */
    public static DialogBox getPeanutButterCatDialog(String text, Image image) {
        DialogBox dialogBox = new DialogBox(text, image);
        dialogBox.flip();
        return dialogBox;
    }

    /**
     * Creates a colored dialog box for a reply from PeanutButterCat.
     *
     * @param text The reply text.
     * @param image The PeanutButterCat avatar.
     * @param commandType Type of command that produced the reply.
     * @return A left-aligned, command-colored PeanutButterCat dialog box.
     */
    public static DialogBox getPeanutButterCatDialog(String text, Image image, CommandType commandType) {
        DialogBox dialogBox = new DialogBox(text, image);
        dialogBox.flip();
        dialogBox.applyCommandStyle(commandType);
        return dialogBox;
    }
}
