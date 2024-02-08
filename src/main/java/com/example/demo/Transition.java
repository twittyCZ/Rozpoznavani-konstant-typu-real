package com.example.demo;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;

/**
 * Representations of single transition between states
 */
public class Transition {
    /**
     * Default color of transition
     */
    private static final Color DEFAULT_COLOR = Color.BLACK;
    /**
     * Color when transition is in use right now - color indicator in graphical visualization
     */
    private static final Color ACTUAL_COLOR = Color.BLUE;
    /**
     * Indicator if transition is in use - indicator for code use
     */
    private Boolean actual;
    /**
     * set of three lines creating a arrow in graph
     */
    private Object[] lines;
    /**
     * Input symbol making the transition
     */
    private String symbol;
    /**
     * state before making a transition
     */
    private State initialState;
    /**
     * state after making a transition
     */
    private State endState;

    public Transition(Boolean actual, Object[] lines, String symbol, State initialState, State endState){
        this.actual = actual;
        this.lines = lines;
        this.symbol = symbol;
        this.initialState = initialState;
        this.endState = endState;
    }

    public Boolean isActual() {
        return actual;
    }
    public Object getLines() {
        return lines;
    }

    public void setActual(Boolean actual) {
        if(this.lines == null){
            if(actual){
                Application.historyTransitions.add(this);
            }
            this.actual = actual;
            return;
        }
        if(actual){
            for (int i = 0; i < lines.length; i++) {
                Object line = lines [i];
                if(line instanceof Line){
                    ((Line) line).setStroke(ACTUAL_COLOR);
                } else if(line instanceof Label){
                    ((Label) line).setTextFill(ACTUAL_COLOR);
                } else if(line instanceof CubicCurve){
                    ((CubicCurve) line).setStroke(ACTUAL_COLOR);
                } else {

                }
            }
            Application.historyTransitions.add(this);
        } else {
            for (int i = 0; i < lines.length; i++) {
                Object line = lines[i];
                if(line instanceof Line){
                    ((Line) line).setStroke(DEFAULT_COLOR);
                } else if(line instanceof Label){
                    ((Label) line).setTextFill(DEFAULT_COLOR);
                } else if(line instanceof CubicCurve){
                    ((CubicCurve) line).setStroke(DEFAULT_COLOR);
                } else {

                }
            }
        }
        this.actual = actual;
    }

    public State getInitialState() {
        return initialState;
    }

    public State getEndState() {
        return endState;
    }
}
