package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import dao.AudioDownloadDao;
import dao.AudioPlayerDao;
import dao.VideoDownloadDao;
import data.MediaType;
import data.Playable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.apache.log4j.Logger;
import service.impl.AudioDownloadServiceImpl;
import service.impl.AudioPlayerServiceImpl;
import service.impl.VideoDownloadServiceImpl;
import util.ServiceResponse;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenuController {

    private final VideoDownloadServiceImpl videoDownloadService = new VideoDownloadServiceImpl(new VideoDownloadDao());

    private final AudioDownloadServiceImpl audioDownloadService = new AudioDownloadServiceImpl(new AudioDownloadDao());

    private final AudioPlayerServiceImpl audioPlayerService = new AudioPlayerServiceImpl(new AudioPlayerDao());

    private final Logger logger = Logger.getLogger(MenuController.class);

    @FXML
    public TextField videoUrl;

    @FXML
    public Button downloadButton;

    @FXML
    public ChoiceBox<MediaType> mediaType;

    @FXML
    public Label urlLabel;

    @FXML
    public ListView<Playable> mediaView;

    @FXML
    public StackPane stackPane;

    @FXML
    public void downloadMedia(MouseEvent mouseEvent) {
        String url = videoUrl.getText();

        String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";

        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url);

        if (!matcher.find()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setContentText("This is not a valid youtube url");
            alert.showAndWait();
            return;
        }

        String id = matcher.group();

        MediaType type = mediaType.getValue();
        ServiceResponse<Playable> serviceResponse;

        if (type == MediaType.VIDEO) {
            serviceResponse = videoDownloadService.downloadById(id);
        } else {
            serviceResponse = audioDownloadService.downloadById(id);
        }

        if (serviceResponse.hasErrorMessages()) {
            List<String> errorMessages = serviceResponse.getErrorMessages();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Following error messages has been encountered:");
            alert.setContentText(String.join("\n", errorMessages));
            alert.showAndWait();
            return;
        }

        Optional<Playable> playableOptional = serviceResponse.getResponseObject();
        playableOptional.ifPresent(this::handleDownload);
    }

    private void showInformation(Alert.AlertType alertType, String title, String header, String context) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);
        alert.showAndWait();
    }

    private void renderOptionsDialog(String header, String text, String button, Playable playable) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(header));

        HBox contentBox = new HBox();
        Button playButton = new Button("Play");
        Label playLabel = new Label("Click here if you want to play it");

        contentBox.getChildren().addAll(playLabel, playButton);

        content.setBody(contentBox);
        stackPane.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
        content.setStyle("-fx-text-fill:#8be4f1;-fx-background-color:#8be4f1;-fx-font-size:14px;-fx-font-family:system;");
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton closeButton = new JFXButton(button);

        playButton.setOnAction(event -> {
            ServiceResponse<Playable> serviceResponse = audioPlayerService.play(playable);
            logger.warn(String.join(", ", serviceResponse.getErrorMessages()));
            event.consume();
            dialog.close();
        });

        closeButton.setOnAction(event -> {
            mediaView.getSelectionModel().clearSelection();
            dialog.close();
        });
        content.setActions(closeButton);
        dialog.show();
    }


    private void handleDownload(Playable playable) {
        showInformation(Alert.AlertType.INFORMATION, "Information", "Success",
                "Successful downloaded media " + playable.getTitle().getName() + " by " + playable.getAuthor().getName());

        if(mediaView.getItems().stream().anyMatch(x -> this.isAlreadyExisting(x, playable))) {
            showInformation(Alert.AlertType.ERROR, "Error", "Following error messages has been encountered:",
                    "This media has already been downloaded");
            return;
        }
        mediaView.getItems().add(playable);

        mediaView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                renderOptionsDialog("Options", "Here you can play this", "close", playable));
    }

    private boolean isAlreadyExisting(Playable current, Playable playable) {
        return current.getTitle().equals(playable.getTitle()) && current.getAuthor().equals(playable.getAuthor());
    }

    @FXML
    public void initialize() {
        ObservableList<MediaType> items = FXCollections.observableArrayList(MediaType.VIDEO, MediaType.AUDIO);
        mediaType.setItems(items);
        mediaType.setValue(MediaType.AUDIO);
    }
}
