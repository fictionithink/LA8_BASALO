package com.example.graph;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class HelloController {

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
                    Node n = new Node(ch);
                    t.setText(ch + "");
                    node.add(n);
                }
            }
        });


    }
    public void initialize(){
        // TODO save all the stack panes into apPane

        // im not sure if everything works as intended, but assuming that it does, my issue now is that i need to update the values of the text fields to reflect the changes
        // from the node file. this is similar to the for(people p : People) snippet in the other file. but maybe i have to find a way to alter the setText?
            // possible solution: try modifying the t.setText(ch + "");

        try{
            int i = 0;
            while(i < 2){
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream("node-"+i+".txt"));
                node = (List<Node>) ois.readObject();
                i++;
            }

        }catch (IOException | ClassNotFoundException e){
            System.out.println(e.getClass());
        }

        for(Node n : node){
            System.out.println(n);
            // this could work, but i need to find a way to get the source of stackpane without using events
//            StackPane sp = (StackPane).getSource();
//            Text t = (Text) sp.getChildren().get(1);
//            t.setText(n + "");
        }
    }

    public void onSaveClicked(ActionEvent event) {
        // TODO save all the stack panes in a file

        try{
            int i = 0;
            while(i < 2){
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("node-"+i+".txt"));
                oos.writeObject(node);
                i++;
            }

        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}