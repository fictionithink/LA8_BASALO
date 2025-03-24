package com.example.graph;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class HelloController {

    @FXML
    private AnchorPane apPane;

    @FXML
    private Button btnAddCircle;

    List<Node> node = new ArrayList<>();
    public void onVertexClicked(MouseEvent event) {
        System.out.println("hooray");

        char ch = (char) (Math.random()*26 + 'A');
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Would you like " + ch + "?", ButtonType.YES, ButtonType.NO);
        a.showAndWait().ifPresent(new Consumer<ButtonType>() {
            @Override
            public void accept(ButtonType buttonType) {
                if (buttonType == ButtonType.YES){
                    StackPane sp = (StackPane) event.getSource();
                    Text t = (Text) sp.getChildren().get(1);

                    for (Node n : node) {
                        if (n.element == t.getText().charAt(0)) {
                            n.element = ch;  // Update stored node data
                            break;
                        }
                    }

                    t.setText(String.valueOf(ch));
                    saveCircle();
                    makeMovable(sp);
                    System.out.println("yippee");

                }
            }
        });


    }
    public void initialize(){
        // TODO save all the stack panes into apPane

        // im not sure if everything works as intended, but assuming that it does, my issue now is that i need to update the values of the text fields to reflect the changes
        // from the node file. this is similar to the for(people p : People) snippet in the other file. but maybe i have to find a way to alter the setText?
            // possible solution: try modifying the t.setText(ch + "");
                // solved it. now to implement the dragging and adding vertices. maybe whenever the application is initialized? or add a new button that
                // creates vertices.
        node.clear();
        apPane.getChildren().clear();

        File file = new File("node.txt");

            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                node.addAll((List<Node>) ois.readObject());
                for (Node n : node) {
                    addCircle(100, 100, n.element, false);
                }
            } catch (IOException | ClassNotFoundException e) {

            }

        for(Node n : node){ // DEBUG or smth
            System.out.println(n);
        }
    }

    private void makeMovable(StackPane stackPane) {
        final double[] mouseAnchorX = new double[1];
        final double[] mouseAnchorY = new double[1];

        stackPane.setOnMousePressed(event -> {
            mouseAnchorX[0] = event.getSceneX() - stackPane.getLayoutX();
            mouseAnchorY[0] = event.getSceneY() - stackPane.getLayoutY();
        });

        stackPane.setOnMouseDragged(event -> {
            stackPane.setLayoutX(event.getSceneX() - mouseAnchorX[0]);
            stackPane.setLayoutY(event.getSceneY() - mouseAnchorY[0]);
            saveCircle();
        });
    }


    private void addCircle(double x, double y, char label, boolean save) {
        StackPane sp = new StackPane();
        sp.setLayoutX(x);
        sp.setLayoutY(y);

        Circle circle = new Circle(20);
        circle.setFill(javafx.scene.paint.Color.DODGERBLUE);
        circle.setStroke(javafx.scene.paint.Color.BLACK);

        Text text = new Text(String.valueOf(label));

        sp.getChildren().addAll(circle, text);
        makeMovable(sp);

        sp.setOnMouseClicked(this::onVertexClicked);
        apPane.getChildren().add(sp);

        if (save) {
            node.add(new Node(label));
            saveCircle();
        }
    }


    private void saveCircle() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("circles.txt"))) {
            for (var node : apPane.getChildren()) {
                if (node instanceof StackPane sp) {
                    double x = sp.getLayoutX();
                    double y = sp.getLayoutY();
                    String label = ((Text) sp.getChildren().get(1)).getText();
                    writer.write(x + "," + y + "," + label);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onAddCircleClicked(ActionEvent event) {
        addCircle(100, 100, ' ', true);
    }


    public void onSaveClicked(ActionEvent event) {
        // TODO save all the stack panes in a file

        try{
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("node.txt"));
            oos.writeObject(node);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
        saveCircle();
        System.out.println("wahoo");
    }
}