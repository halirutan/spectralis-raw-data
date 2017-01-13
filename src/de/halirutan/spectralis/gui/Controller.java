package de.halirutan.spectralis.gui;

import de.halirutan.spectralis.data.SLOImage;
import de.halirutan.spectralis.filestructure.HSFFile;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
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
public class Controller implements Initializable {

    public CheckBox checkDiveInto;
    public VBox root;
    public ListView<File> listView;
    public ProgressBar progressBar;
    private double myProgressCount;
    private double myProgressValue;

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

    private void initProgressbar() {
        myProgressCount = 2 * (myFiles.size() + myDirectories.size());
        myProgressValue = 0;
        progressBar.setProgress(myProgressValue);
    }

    private void incrementProgress() {
        myProgressCount++;
        progressBar.setProgress(myProgressCount / myProgressValue);
    }

    public void exportFiles() {
        final File outputDir = showDirectorySelectDialog("Select output directory");
        if (!outputDir.canWrite()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot write to output directory");
            alert.show();
        }
        initProgressbar();
        Thread exporter = new Thread(() -> {
            Runnable progress = this::incrementProgress;
            List<File> myRealVolFiles = new ArrayList<>(myFiles.size());
            if (myDirectories.size() > 0) {
                for (File directory : myDirectories) {
                    final boolean recursively = checkDiveInto.isSelected();
                    final Collection<File> files = FileUtils.listFiles(directory, new String[]{"vol"}, recursively);
                    for (File next : files) {
                        if (HSFFile.isValidHSFFile(next)) {
                            myRealVolFiles.add(next);
                        }

                    }
                    Platform.runLater(progress);
                }
            }

            for (File file : myFiles) {
                if (HSFFile.isValidHSFFile(file)) {
                    myRealVolFiles.add(file);
                }
                Platform.runLater(progress);
            }

            for (File volFile : myRealVolFiles) {
                SLOImage img = HSFFile.readSLOImage(volFile);

                if (img != null) {
                    final String fileName = FilenameUtils.getBaseName(volFile.getName());
                    final String outName = FilenameUtils.concat(outputDir.getAbsolutePath(), fileName + ".png");
                    try {
                        ImageIO.write(img.getImage(), "png",
                                new File(outName));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                Platform.runLater(progress);
            }
            Platform.runLater(this::initProgressbar);
        });
        exporter.setDaemon(true);
        exporter.start();
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

}
