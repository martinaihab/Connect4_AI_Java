package sample;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main extends Application {
    private static final int TILE_SIZE = 80;
    private static final int COLUMNS = 7;
    private static final int ROWS = 6;

    private Disc[][] grid = new Disc[COLUMNS][ROWS];
    private Button buttonPlayerStart = new Button("Player Start");
    private Button buttonComputerStart = new Button("Computer Start");
    private Button buttonRepeat = new Button("Repeat");

    private Pane discRoot = new Pane();
    private Pane connect4Pane = new Pane();
    private Text textWinnerMessage = new Text();

    private Parent createContent() {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.setPadding(new Insets(10));


        //Make the player start button
        buttonPlayerStart.setStyle("-fx-font-weight: bold;-fx-font-size: 18");
        buttonPlayerStart.setTextFill(Color.RED);
        buttonPlayerStart.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                startNewGame();


            }
        });

        //Make the player start button
        buttonComputerStart.setStyle("-fx-font-weight: bold;-fx-font-size: 18");
        buttonComputerStart.setTextFill(Color.valueOf("#ccdd16"));
        buttonComputerStart.setVisible(true);
        buttonComputerStart.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                startNewGame();
                placeDisc(new Disc(false), AIConnect4.getAIMove(),false);
            }
        });
        //make the repeat button
        buttonRepeat.setStyle("-fx-font-weight: bold;-fx-font-size: 18");
        buttonRepeat.setVisible(true);
        buttonRepeat.setTextFill(Color.FORESTGREEN);
        buttonRepeat.setVisible(false);
        buttonRepeat.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                buttonPlayerStart.setVisible(true);
                buttonComputerStart.setVisible(true);
                buttonRepeat.setVisible(false);

            }
        });


        textWinnerMessage.setFont(Font.font(18));
        textWinnerMessage.setStyle("-fx-font-weight: bold");


        HBox hBoxButtons = new HBox();
        hBoxButtons.setAlignment(Pos.CENTER_LEFT);
        hBoxButtons.setSpacing(60);
        hBoxButtons.getChildren().addAll(buttonPlayerStart, buttonRepeat, buttonComputerStart);
        hBoxButtons.setAlignment(Pos.CENTER);

        root.getChildren().add(hBoxButtons);
        root.getChildren().add(textWinnerMessage);
        Shape gridShape = makeGrid();

        connect4Pane.getChildren().add(discRoot);
        connect4Pane.getChildren().add(gridShape);
        connect4Pane.getChildren().addAll(makeColumns());
        freezeConnect4Board();
        root.getChildren().add(connect4Pane);
        return root;
    }

    private void freezeConnect4Board() {
        connect4Pane.setDisable(true);
    }

    private void unfreezeConnect4Board() {
        connect4Pane.setDisable(false);

    }

    private void startNewGame() {
        buttonRepeat.setVisible(true);
        buttonComputerStart.setVisible(false);
        buttonPlayerStart.setVisible(false);
        textWinnerMessage.setVisible(false);
        grid = new Disc[COLUMNS][ROWS];
        discRoot.getChildren().clear();
        AIConnect4.initialize_board();
        unfreezeConnect4Board();
    }

    private Shape makeGrid() {
        Shape shape = new Rectangle((COLUMNS + 1) * TILE_SIZE, (ROWS + 1) * TILE_SIZE);

        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLUMNS; x++) {
                Circle circle = new Circle(TILE_SIZE / 2);
                circle.setCenterX(TILE_SIZE / 2);
                circle.setCenterY(TILE_SIZE / 2);
                circle.setTranslateX(x * (TILE_SIZE + 5) + TILE_SIZE / 4);
                circle.setTranslateY(y * (TILE_SIZE + 5) + TILE_SIZE / 4);

                shape = Shape.subtract(shape, circle);
            }
        }

        Light.Distant light = new Light.Distant();
        light.setAzimuth(45.0);
        light.setElevation(30.0);

        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(5.0);

        shape.setFill(Color.BLUE);
        shape.setEffect(lighting);

        return shape;
    }

    private List<Rectangle> makeColumns() {
        List<Rectangle> list = new ArrayList<>();

        for (int x = 0; x < COLUMNS; x++) {
            Rectangle rect = new Rectangle(TILE_SIZE, (ROWS + 1) * TILE_SIZE);
            rect.setTranslateX(x * (TILE_SIZE + 5) + TILE_SIZE / 4);
            rect.setFill(Color.TRANSPARENT);

            rect.setOnMouseEntered(e -> rect.setFill(Color.rgb(200, 200, 50, 0.3)));
            rect.setOnMouseExited(e -> rect.setFill(Color.TRANSPARENT));

            final int column = x;
            rect.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {


                    // player move
                    placeDisc(new Disc(true), column,true);
                    AIConnect4.update_board(column, AIConnect4.PLAYER);

                }
            });

            list.add(rect);
        }

        return list;
    }

    private void placeDisc(Disc disc, int column,boolean playerTurn) {
        freezeConnect4Board();
        boolean redMove = disc.red;
        int row = ROWS - 1;
        do {
            if (!getDisc(column, row).isPresent())
                break;

            row--;
        } while (row >= 0);

        if (row < 0)
            return;

        grid[column][row] = disc;
        discRoot.getChildren().add(disc);
        disc.setTranslateX(column * (TILE_SIZE + 5) + TILE_SIZE / 4);

        final int currentRow = row;

        TranslateTransition animation = new TranslateTransition(Duration.seconds(0.5), disc);
        animation.setToY(row * (TILE_SIZE + 5) + TILE_SIZE / 4);
        animation.setOnFinished(e -> {
            if (gameEnded(column, currentRow, redMove)) {
                gameOver(redMove);
                return;
            } else if (playerTurn) {

                //if the player played then this is the computer move
                placeDisc(new Disc(false), AIConnect4.getAIMove(),false);
                //next turn is player

            }
            unfreezeConnect4Board();
        });
        animation.play();


    }

    private boolean gameEnded(int column, int row, boolean redMove) {
        List<Point2D> vertical = IntStream.rangeClosed(row - 3, row + 3)
                .mapToObj(r -> new Point2D(column, r))
                .collect(Collectors.toList());

        List<Point2D> horizontal = IntStream.rangeClosed(column - 3, column + 3)
                .mapToObj(c -> new Point2D(c, row))
                .collect(Collectors.toList());

        Point2D topLeft = new Point2D(column - 3, row - 3);
        List<Point2D> diagonal1 = IntStream.rangeClosed(0, 6)
                .mapToObj(i -> topLeft.add(i, i))
                .collect(Collectors.toList());

        Point2D botLeft = new Point2D(column - 3, row + 3);
        List<Point2D> diagonal2 = IntStream.rangeClosed(0, 6)
                .mapToObj(i -> botLeft.add(i, -i))
                .collect(Collectors.toList());

        return checkRange(vertical, redMove) || checkRange(horizontal, redMove)
                || checkRange(diagonal1, redMove) || checkRange(diagonal2, redMove);
    }

    private boolean checkRange(List<Point2D> points, boolean redMove) {
        int chain = 0;

        for (Point2D p : points) {
            int column = (int) p.getX();
            int row = (int) p.getY();

            Disc disc = getDisc(column, row).orElse(new Disc(!redMove));
            if (disc.red == redMove) {
                chain++;
                if (chain == 4) {
                    return true;
                }
            } else {
                chain = 0;
            }
        }

        return false;
    }

    private void gameOver(boolean redMove) {
        String winningMessage = "The Winner is the " + (redMove ? "Red" : "Yellow") + " player";
        //System.out.println(winningMessage);
        textWinnerMessage.setText(winningMessage);
        textWinnerMessage.setVisible(true);
        if (redMove)
            textWinnerMessage.setFill(Color.RED);
        else
            textWinnerMessage.setFill(Color.valueOf("#ccdd16"));
        freezeConnect4Board();
    }

    private Optional<Disc> getDisc(int column, int row) {
        if (column < 0 || column >= COLUMNS
                || row < 0 || row >= ROWS)
            return Optional.empty();

        return Optional.ofNullable(grid[column][row]);
    }

    private static class Disc extends Circle {
        private final boolean red;

        public Disc(boolean red) {
            super(TILE_SIZE / 2, red ? Color.RED : Color.YELLOW);
            this.red = red;

            setCenterX(TILE_SIZE / 2);
            setCenterY(TILE_SIZE / 2);
        }

    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
