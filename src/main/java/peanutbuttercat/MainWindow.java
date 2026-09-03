package peanutbuttercat;

import javafx.scene.layout.AnchorPane;

/**
 * Controls the JavaFX main window and delegates command processing to PeanutButterCat.
 */
public class MainWindow extends AnchorPane {
    private PeanutButterCat peanutButterCat;

    /**
     * Supplies the command-processing backend used by this window.
     *
     * @param peanutButterCat The application backend to use for user commands.
     */
    public void setPeanutButterCat(PeanutButterCat peanutButterCat) {
        this.peanutButterCat = peanutButterCat;
    }
}
