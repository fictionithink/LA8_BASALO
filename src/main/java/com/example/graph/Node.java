package com.example.graph;

import java.io.Serializable;

public class Node implements Serializable {

    char element;

    public Node(char element) {
        this.element = element;
    }

    @Override
    public String toString() {
        return element + "";
    }
}
