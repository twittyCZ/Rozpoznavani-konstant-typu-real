package com.example.demo;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * A cass representing a state in finite state maschine
 */
public class State {
    /**
     * Color when state is in use right now - color indicator in graphical visualization
     */
    public static final Color ACTUAL_STATE = Color.YELLOW;
    /**
     * Default color of state
     */
    public static final Color NONACTUAL_STATE = Color.BLACK;
    /**
     * Name of state
     */
    private String name;
    /**
     * Indicator of actuality of state
     */
    private boolean actual;
    /**
     * Circle represnting a state in graph
     */
    private Circle circle;
    
    public State(String name, boolean actual, Circle circle){
        this.name = name;
        this.actual = actual;
        this.circle = circle;
    }

    public boolean isActual() {
        return actual;
    }

    public void setActual(boolean actual) {
        if(circle == null){
            if(actual){
                Application.historyStates.add(this);
            }
            this.actual = actual;
            return;
        }
        if(actual){
            circle.setStroke(ACTUAL_STATE);
            Application.historyStates.add(this);
        } else {
            circle.setStroke(NONACTUAL_STATE);
        }
        this.actual = actual;
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public String getName() {
        return name;
    }
}
