package de.halirutan.spectralis.gui.sloexporter;

import de.halirutan.spectralis.filestructure.SLOImage;
import de.halirutan.spectralis.filestructure.HSFFile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by patrick on 12.01.17.
 * (c) Patrick Scheibe 2017
 */
@SuppressWarnings("Duplicates")
public class Controller implements Initializable {

    public CheckBox checkDiveInto;
    public VBox root;
    public ListView<File> listView;
    public Label statusBar;

    private ObservableList<File> listViewItems;

    private List<File> myFiles = new ArrayList<>();
    private List<File> myDirectories = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listViewItems = FXCollections.observableArrayList();
        listView.setItems(listViewItems);
    }

    public void addFiles() {
        final List<File> files = showFileSelectDialog();
        if (files != null) {
            myFiles.addAll(files);
            listViewItems.addAll(files);
        }

    }

    public void addDirectory() {
        final File dir = showDirectorySelectDialog();
        if (dir != null) {
            myDirectories.add(dir);
            listViewItems.add(dir);
        }
    }

    public void removeFiles() {
        myFiles.clear();
        myDirectories.clear();
        listViewItems.clear();
    }

    public void exportFiles() {
        final File outputDir = showDirectorySelectDialog("Select output directory");
        if (!outputDir.canWrite()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot write to output directory");
            alert.show();
        }

        Task<Integer> exportTask = new ExportWorker(outputDir);
        Service<Integer> service = new Service<Integer>() {
            @Override
            protected Task<Integer> createTask() {
                return exportTask;
            }
        };

        service.start();

    }


    private List<File> showFileSelectDialog() {
        final Window window = root.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select .vol files or directories");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Raw Volume files", "vol"));
        return fileChooser.showOpenMultipleDialog(window);
    }

    private File showDirectorySelectDialog(String title) {
        final Window window = root.getScene().getWindow();
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle(title);
        return dirChooser.showDialog(window);
    }

    private File showDirectorySelectDialog() {
        return showDirectorySelectDialog("Select directory");
    }

    private class ExportWorker extends Task<Integer> {

        File outputDirectory;

        ExportWorker(File outputDirectory) {
            this.outputDirectory = outputDirectory;
            statusBar.textProperty().unbind();
            statusBar.textProperty().bind(this.messageProperty());
        }

        @Override
        protected void succeeded() {
            final Integer value = getValue();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            String text;
            switch (value) {
                case 0:
                    text = "No image was exported";
                    break;
                case 1:
                    text = "One image was exported";
                    break;
                default:
                    text = value + " images were exported";
                    break;
            }

            alert.setContentText(text);
            alert.showAndWait();
            root.setDisable(false);
        }

        @Override
        protected void failed() {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setTitle(getMessage());
            if (getException() != null) {
                alert.setContentText(getException().getMessage());
            }
            alert.showAndWait();
            root.setDisable(false);
        }

        @Override
        protected void running() {
            root.setDisable(true);
        }

        @Override
        protected Integer call() throws Exception {
            int numOfExportedImages = 0;
            List<File> myRealVolFiles = new ArrayList<>(myFiles.size());
            if (myDirectories.size() > 0) {
                for (File directory : myDirectories) {
                    final boolean recursively = checkDiveInto.isSelected();
                    final Collection<File> files = FileUtils.listFiles(directory, new String[]{"vol"}, recursively);
                    updateMessage("Checking directory " + FilenameUtils.getBaseName(directory.getName()));
                    for (File next : files) {
                        if (HSFFile.isValidHSFFile(next)) {
                            myRealVolFiles.add(next);
                            updateMessage("Adding " + FilenameUtils.getBaseName(next.getName()) + " for export");
                        }

                    }
                }
            }

            for (File file : myFiles) {
                final String baseName = FilenameUtils.getBaseName(file.getName());
                if (HSFFile.isValidHSFFile(file)) {
                    myRealVolFiles.add(file);
                    updateMessage("Adding " + baseName + " for export");
                } else {
                    updateMessage("File " + baseName + " is not a valid Spectralis file.");
                    throw new Exception("Wrong file format");
                }
            }

            for (File volFile : myRealVolFiles) {
                final HSFFile hsfFile = new HSFFile(volFile);
                SLOImage img = hsfFile.getSLOImage();
                final String baseName = FilenameUtils.getBaseName(volFile.getName());
                updateMessage("Exporting SLO from " + baseName);
                if (img != null) {
                    final String outName = FilenameUtils.concat(outputDirectory.getAbsolutePath(), baseName + ".png");
                    try {
                        File outFile = new File(outName);
                        if (!outFile.canWrite()) {
                            throw new Exception("Cannot create image file. Missing permissions?");
                        }
                        ImageIO.write(img.getImage(), "png",
                                outFile);
                        numOfExportedImages++;
                    } catch (IOException e) {
                        updateMessage("An error occurred when writing " + outName);
                        throw e;
                    }
                } else {
                    updateMessage("Could not extract SLO image from " + baseName);
                    super.failed();
                }
            }
            updateMessage("Status: waiting...");
            return numOfExportedImages;
        }

    }


}
