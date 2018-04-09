package de.halirutan.spectralis.examples.sloexporter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;

import de.halirutan.spectralis.SpectralisException;
import de.halirutan.spectralis.filestructure.HSFFile;
import de.halirutan.spectralis.filestructure.HSFVersion;
import de.halirutan.spectralis.filestructure.SLOImage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

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

    final Collection<File> myFiles = new ArrayList<>();
    final Collection<File> myDirectories = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listViewItems = FXCollections.observableArrayList();
        listView.setItems(listViewItems);
    }

    public void addFiles() {
        List<File> files = showFileSelectDialog();
        if (files != null) {
            myFiles.addAll(files);
            listViewItems.addAll(files);
        }

    }

    public void addDirectory() {
        File dir = showDirectorySelectDialog();
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
        File outputDir = showDirectorySelectDialog("Select output directory");
        if (!outputDir.canWrite()) {
            Alert alert = new Alert(AlertType.ERROR, "Cannot write to output directory");
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
        Window window = root.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select .vol files or directories");
        fileChooser.setSelectedExtensionFilter(new ExtensionFilter("Raw Volume files", "vol"));
        return fileChooser.showOpenMultipleDialog(window);
    }

    private File showDirectorySelectDialog(String title) {
        Window window = root.getScene().getWindow();
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle(title);
        return dirChooser.showDialog(window);
    }

    private File showDirectorySelectDialog() {
        return showDirectorySelectDialog("Select directory");
    }

    private class ExportWorker extends Task<Integer> {

        private File outputDirectory;

        ExportWorker(File outDir) {
            outputDirectory = outDir;
            statusBar.textProperty().unbind();
            statusBar.textProperty().bind(messageProperty());
        }

        @Override
        protected void succeeded() {
            Integer value = getValue();
            Alert alert = new Alert(AlertType.INFORMATION);
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
            Alert alert = new Alert(AlertType.ERROR);
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
        protected Integer call() throws SpectralisException {
            Collection<File> myRealVolFiles = new ArrayList<>(myFiles.size());
            if (!myDirectories.isEmpty()) {
                for (File directory : myDirectories) {
                    boolean recursively = checkDiveInto.isSelected();
                    Collection<File> files = FileUtils.listFiles(directory, new String[]{"vol"}, recursively);
                    updateMessage("Checking directory " + FilenameUtils.getBaseName(directory.getName()));
                    for (File next : files) {
                        if (HSFVersion.readVersion(next) != HSFVersion.INVALID) {
                            myRealVolFiles.add(next);
                            updateMessage("Adding " + FilenameUtils.getBaseName(next.getName()) + " for export");
                        }

                    }
                }
            }

            for (File file : myFiles) {
                String baseName = FilenameUtils.getBaseName(file.getName());
                if (HSFVersion.readVersion(file) != HSFVersion.INVALID) {
                    myRealVolFiles.add(file);
                    updateMessage("Adding " + baseName + " for export");
                } else {
                    updateMessage("File " + baseName + " is not a valid Spectralis file.");
                    throw new SpectralisException("Wrong file format");
                }
            }

            int numOfExportedImages = 0;
            for (File volFile : myRealVolFiles) {
                HSFFile hsfFile = new HSFFile(volFile);
                SLOImage img = hsfFile.getSLOImage();
                String baseName = FilenameUtils.getBaseName(volFile.getName());
                updateMessage("Exporting SLO from " + baseName);
                if (img != null) {
                    String outName = FilenameUtils.concat(getOutputDirectory().getAbsolutePath(), baseName + ".png");
                    try {
                        File outFile = new File(outName);
                        ImageIO.write(img.getImage(), "png",
                                outFile);
                        numOfExportedImages++;
                    } catch (IOException e) {
                        updateMessage("An error occurred when writing " + outName);
                        throw new SpectralisException(e);
                    }
                } else {
                    updateMessage("Could not extract SLO image from " + baseName);
                    super.failed();
                }
            }
            updateMessage("Status: waiting...");
            return numOfExportedImages;
        }

        File getOutputDirectory() {
            return outputDirectory;
        }
    }


}
