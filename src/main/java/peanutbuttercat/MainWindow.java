package peanutbuttercat;

import java.util.Objects;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controls the JavaFX main window and delegates command processing to PeanutButterCat.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox dialogContainer;

    @FXML
    private TextField userInput;

    @FXML
    private Button sendButton;

    private PeanutButterCat peanutButterCat;
    private final Image userImage = loadImage("/images/DaUser.png");
    private final Image peanutButterCatImage = loadImage("/images/DaDuke.png");

    /** Keeps the latest dialog visible after a message is added. */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Supplies the command-processing backend used by this window.
     *
     * @param peanutButterCat The application backend to use for user commands.
     */
    public void setPeanutButterCat(PeanutButterCat peanutButterCat) {
        this.peanutButterCat = peanutButterCat;
    }

    /**
     * Adds the user's input and PeanutButterCat's response to the conversation.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText().trim();
        if (input.isEmpty()) {
            return;
        }

        String response = peanutButterCat.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getPeanutButterCatDialog(response, peanutButterCatImage));
        userInput.clear();
    }

    private Image loadImage(String imagePath) {
        return new Image(Objects.requireNonNull(MainWindow.class.getResourceAsStream(imagePath)));
    }
}
