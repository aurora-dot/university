/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper.part.three;

import java.awt.Insets;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Modality;

/**
 *
 * @author
 */
public class Minesweeper extends Application {

    private GridPane grid;
    private Minefield minefield;
    private int rows = 10;
    private int columns = 15;
    private int mines = 20;
    private TabPane tabPane = new TabPane();
    private FileChooser fileChooser = new FileChooser();
    private Stage stage;
    private BorderPane mainBorderPane;

    @Override
    public void start(Stage primaryStage) {
        this.minefield = new Minefield(rows, columns);
        this.minefield.populate(mines);

        this.stage = primaryStage;
        mainBorderPane = new BorderPane();

        grid = mineGrid();
        mainBorderPane.setCenter(grid);

        Scene scene = new Scene(mainBorderPane);
        scene.getStylesheets().add(getClass().getResource("CSS/style.css").toString());
        mainBorderPane.setTop(createMenus());

        stage.setResizable(false);
        Image image = new Image(getClass().getResource("Images/mineLogo.png").toString());

        stage.getIcons().add(image);
        stage.setOnCloseRequest(e -> Platform.exit());

        stage.sizeToScene();
        stage.setTitle("Minesweeper");
        stage.setScene(scene);
        stage.show();
    }

    private MenuBar createMenus() {
        MenuBar menus = new MenuBar();

        Menu fileMenu = new Menu("File");

        MenuItem saveItem = new MenuItem("Save...");
        saveItem.setOnAction(e -> {
            saveMinefield();
        });

        MenuItem openItem = new MenuItem("Open...");
        openItem.setOnAction(e -> {
            openMinefield();
        });

        fileMenu.getItems().addAll(openItem, saveItem);

        Menu gameMenu = new Menu("Game");

        MenuItem easyItem = new MenuItem("Easy...");

        MenuItem mediumItem = new MenuItem("Medium...");

        MenuItem hardItem = new MenuItem("Hard...");

        MenuItem customItem = new MenuItem("Custom...");
        customItem.setOnAction(e -> {
            newGame();
        });
        
        easyItem.setOnAction(e -> {
            rows = 10;
            columns = 15;
            mines = 20;

            minefield = new Minefield(rows, columns);
            minefield.populate(mines);

            grid = mineGrid();
            mainBorderPane.setCenter(grid);
            stage.sizeToScene();
        });

        mediumItem.setOnAction(e -> {
            rows = 20;
            columns = 25;
            mines = 75;

            minefield = new Minefield(rows, columns);
            minefield.populate(mines);

            grid = mineGrid();
            mainBorderPane.setCenter(grid);
            stage.sizeToScene();
        });

        hardItem.setOnAction(e -> {
            rows = 30;
            columns = 35;
            mines = 200;

            minefield = new Minefield(rows, columns);
            minefield.populate(mines);

            grid = mineGrid();
            mainBorderPane.setCenter(grid);
            stage.sizeToScene();
        });

        
        gameMenu.getItems().addAll(easyItem, mediumItem, hardItem, customItem);

        Menu debugMenu = new Menu("Debug");

        MenuItem showItem = new MenuItem("Show...");
        showItem.setOnAction(e -> {
            showMinefield();
        });

        MenuItem completeItem = new MenuItem("Solve...");
        completeItem.setOnAction(e -> {
            solveMinefield();
            refresh();
        });

        debugMenu.getItems().addAll(showItem, completeItem);

        menus.getMenus().addAll(fileMenu, gameMenu, debugMenu);
        return menus;
    }

    private void showMinefield() {
        for (Node n : grid.getChildren()) {
            if (n instanceof MineButton) {
                MineButton button = (MineButton) n;
                int[] pos = button.getPosition();

                if (minefield.isMined(pos[0], pos[1])) {
                    button.setText("m");
                } else {
                    button.setText(String.valueOf(minefield.getMineNeighbour(pos[0], pos[1])));
                }
            }
        }
    }

    private void solveMinefield() {
        for (Node n : grid.getChildren()) {
            if (n instanceof MineButton) {
                MineButton button = (MineButton) n;
                int[] pos = button.getPosition();

                if (minefield.isMined(pos[0], pos[1])) {
                    if (!minefield.isMarked(pos[0], pos[1])) {
                        minefield.toggleMark(pos[0], pos[1]);
                    }
                } else if (minefield.isMarked(pos[0], pos[1])) {
                    minefield.toggleMark(pos[0], pos[1]);
                }
            }
        }
    }

    private void saveMinefield() {
        try {
            fileChooser.setTitle("Save progress to file...");
            FileChooser.ExtensionFilter extFilter
                    = new FileChooser.ExtensionFilter("ser files (*.ser)", "*.ser");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                minefield.save(file);

            }
        } catch (Exception ex) {
            Alert alert = new Alert(AlertType.ERROR, ex.toString(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void openMinefield() {
        try {
            fileChooser.setTitle("Open progress file...");
            FileChooser.ExtensionFilter extFilter
                    = new FileChooser.ExtensionFilter("ser files (*.ser)", "*.ser");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showOpenDialog(stage);

            if (file != null) {
                List<List<Object>> listOfLists;
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);

                listOfLists = (List<List<Object>>) ois.readObject();
                List<Object> dimentions = listOfLists.get(0);

                rows = (int) dimentions.get(0);
                columns = (int) dimentions.get(1);

                minefield = new Minefield(rows, columns);
                minefield.open(listOfLists);

                grid = mineGrid();
                mainBorderPane.setCenter(grid);

                refresh();

            }
        } catch (Exception ex) {
            Alert alert = new Alert(AlertType.ERROR, ex.toString(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void newGame() {
        newGamePopup();
    }

    private void fail() {
        Media sound = new Media(getClass().getResource("Sounds/bomb.mp3").toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();

        for (Node n : grid.getChildren()) {
            if (n instanceof MineButton) {
                MineButton button = (MineButton) n;
                int[] pos = button.getPosition();

                if (minefield.isMined(pos[0], pos[1])) {
                    button.setGraphic(new ImageView(new Image(getClass().getResource("Images/mine.png").toString())));
                }

                if (!minefield.isRevealed(pos[0], pos[1])) {
                    minefield.toggleRevealed(pos[0], pos[1]);
                }

                button.setDisable(true);
            }
        }

        // TODO Make popup show
        successOrFailPopup(false);
    }

    private void success() {
        // TODO check if all mines and only the mines are marked, if so return success
        for (Node n : grid.getChildren()) {
            if (n instanceof MineButton) {
                n.setDisable(true);
            }
        }

        // TODO Make popup show
        successOrFailPopup(true);
    }

    public void successOrFailPopup(boolean success) {
        Stage popupStage = new Stage();
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root);
        
        scene.getStylesheets().add(getClass().getResource("CSS/popupScene.css").toString());
        Image image = new Image(getClass().getResource("Images/mineLogo.png").toString());
        popupStage.getIcons().add(image);

        //Creating a Text object 
        Text topText = new Text();
        Text centerText = new Text();
        
        topText.setStyle("-fx-font-size: 30px;");
        centerText.setStyle("-fx-font-size: 30px;");
        topText.setFill(Color.rgb(248, 248, 242)); 
        centerText.setFill(Color.rgb(248, 248, 242)); 

        if (success) {
            popupStage.setTitle("Success!");
            topText.setText("Congratulations");
            centerText.setText("You won! c:");

        } else {
            popupStage.setTitle("Failed!");
            topText.setText("Commiserations");
            centerText.setText("You Lost :c");
        }

        //Setting the top, bottom, center, right and left nodes to the pane 
        root.setTop(topText); // congrats text
        root.setCenter(centerText); // You won!
        // new game

        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setResizable(false);
        popupStage.setScene(scene);
        popupStage.show();
    }

    private void newGamePopup() {

        Stage popupStage = new Stage();
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root);

        TextField entryRows = new TextField();
        TextField entryColumns = new TextField();
        TextField entryMines = new TextField();

        TilePane fieldPane = new TilePane();
        fieldPane.setPrefColumns(2);

        fieldPane.getChildren().addAll();
        fieldPane.getChildren().addAll(new Label("Rows:"), entryRows, new Label("Columns:"), entryColumns, new Label("Mines:"), entryMines);

        Button goButton = new Button("Go!");
        goButton.setOnAction(e -> {
            String regex = "\\d+";

            String r = entryRows.getText();
            String c = entryColumns.getText();
            String m = entryMines.getText();

            if (r.matches(regex) && c.matches(regex) && m.matches(regex)) {
                int totalSpaces;
                totalSpaces = (Integer.parseInt(r) * Integer.parseInt(c)) - 1;

                if (Integer.parseInt(m) <= totalSpaces) {
                    rows = Integer.parseInt(r);
                    columns = Integer.parseInt(c);
                    mines = Integer.parseInt(m);

                    if (!(rows > 30 || columns > 60)) {
                        minefield = new Minefield(rows, columns);
                        minefield.populate(mines);

                        grid = mineGrid();
                        mainBorderPane.setCenter(grid);
                        refresh();

                        popupStage.close();
                    
                    } else {
                        Alert alert = new Alert(AlertType.ERROR, "Error: Max rows 30, Max columns 60", ButtonType.OK);
                        alert.showAndWait();
                    }
                    
                } else {
                    Alert alert = new Alert(AlertType.ERROR, "Error: Too many mines", ButtonType.OK);
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(AlertType.ERROR, "Error: Please enter digits", ButtonType.OK);
                alert.showAndWait();
            }
        });

        BorderPane buttonPane = new BorderPane();

        buttonPane.setCenter(goButton);
        root.setBottom(buttonPane);

        root.setCenter(fieldPane);
        
        Image image = new Image(getClass().getResource("Images/mineLogo.png").toString());
        popupStage.getIcons().add(image);
        popupStage.setTitle("Minesweeper: Custom");
        scene.getStylesheets().add(getClass().getResource("CSS/customScene.css").toString());
        popupStage.sizeToScene();
        popupStage.setResizable(false);
        popupStage.setScene(scene);
        popupStage.show();

    }

    private void refresh() {
        // loop through buttons setting text and checking if others are visable
        for (Node n : grid.getChildren()) {

            if (n instanceof MineButton) {
                MineButton button = (MineButton) n;
                int[] pos = button.getPosition();

                if (minefield.isRevealed(pos[0], pos[1])) {
                    button.setGraphic(null);
                    button.setDisable(true);

                    int s = minefield.getMineNeighbour(pos[0], pos[1]);
                    button.setText(String.valueOf(s));

                    if (s == 0) {
                        button.setStyle("-fx-text-fill: #8be9fd");
                    }
                    if (s == 1) {
                        button.setStyle("-fx-text-fill: #50fa7b");
                    } else if (s == 2) {
                        button.setStyle("-fx-text-fill: #ffb86c");
                    } else if (s == 3) {
                        button.setStyle("-fx-text-fill: #bd93f9");
                    } else if (s >= 4) {
                        button.setStyle("-fx-text-fill: #ff5555");
                    }

                } else if (minefield.isMarked(pos[0], pos[1])) {
                    button.setGraphic(new ImageView(new Image(getClass().getResource("Images/flag.png").toString())));
                } else {
                    button.setGraphic(null);
                }
            }
        }

        stage.sizeToScene();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private GridPane mineGrid() {
        GridPane grid = new GridPane();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                MineButton button = new MineButton("", r, c);

                button.setOnMousePressed(e -> {
                    buttonMethod(button, e);
                });

                grid.add(button, c, r);
            }
        }

        return grid;
    }

    private void buttonMethod(MineButton button, MouseEvent e) {
        if (e.getButton() == MouseButton.PRIMARY) {
            int[] p = button.getPosition();
            System.out.printf("Mouse LEFT clicked cell [%d, %d]%n", p[0], p[1]);

            if (!minefield.isMarked(p[0], p[1])) {
                boolean b = minefield.step(p[0], p[1]);
                System.out.println(b);
                if (b == true) {
                    button.setText(String.valueOf(minefield.getMineNeighbour(p[0], p[1])));
                    refresh();
                } else if (b == false) {
                    fail();
                }
                minefield.printMinefield();
                button.setStyle("-fx-text-fill: #f8f8f2");
                button.setDisable(true);
            }

        } else if (e.getButton() == MouseButton.SECONDARY) {
            int[] p = button.getPosition();
            System.out.printf("Mouse RIGHT clicked cell [%d, %d]%n", p[0], p[1]);

            if (!minefield.isMarked(p[0], p[1])) {
                button.setGraphic(new ImageView(new Image(getClass().getResource("Images/flag.png").toString())));

            } else {
                button.setGraphic(null);
            }

            minefield.toggleMark(p[0], p[1]);

            // TODO check if all mines and only the mines are marked, if so return success
            if (minefield.areAllMinesFound()) {
                success();
            }
        }
    }
}
