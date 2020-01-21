import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField zipField;

    @FXML
    private TextField fileOptions;

    @FXML
    private TextField outDirectory;

    @FXML
    private Button zipButton;

    @FXML
    void pathToZip(ActionEvent event) {
        FileChooser fc = new FileChooser();

        File file = fc.showOpenDialog(null);

        if (file != null)
            zipField.setText(file.getAbsolutePath());
    }

    @FXML
    void pathToOptions(ActionEvent event) {
        FileChooser fc = new FileChooser();

        File file = fc.showOpenDialog(null);

        if (file != null)
            fileOptions.setText(file.getAbsolutePath());
    }

    @FXML
    void pathOutDirectory(ActionEvent event) {
        DirectoryChooser dc = new DirectoryChooser();

        File file = dc.showDialog(null);

        outDirectory.setText(file.getAbsolutePath());
    }

    @FXML
    void initialize() {

    }
}
