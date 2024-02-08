package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Main class
 * Executive code is running there
 */
public class Application extends javafx.application.Application {
    /**
     * Recalculation constant for keyboard input
     */
    private static final int RECALCULATION_TO_NUMBER = 96;
    /**
     * Message to user, when maschine has done its work.
     */
    private static final String DONE_MESSAGE = "Nelze zadat další vstupní symbol.";
    /**
     * Message to user, when maschine has come to error state.
     */
    private static final String ERROR_MESSAGE = "Neočekávaný znak!";
    /**
     * States in the graph
     */
    private static State startState, signState, intState,  decState, floatState, eState, signEState, endState,
            expState, dontKnowState, doneState;
    /**
     * Array of all states
     */
    private static State states[];
    /**
     * List of loaded symbols, observed by textareas
     */
    public ObservableList<String> symbols = FXCollections.observableArrayList();
    /**
     * History of visited states
     */
    public static ArrayList<State> historyStates = new ArrayList<>();
    /**
     * History of used transitions
     */
    public static ArrayList<Transition> historyTransitions = new ArrayList<>();
    /**
     * Transitions used in graph
     */
    private static Transition xToStartByNothingTransition, startToSignByPlusTransition, startToSignByMinusTransition,
            signToIntByNumberTransition, IntToIntByNumberTransition, intToEByETransition, startToIntByNumberTransition,
            intToDecByCommaTransition, decToEByETransition, decToFloatByNumberTransition, floatToFloatByNumberTransition,
            floatToEByETransition,eToSignEByPlusTransition, eToSignEByMinusTransition, signEToExptByNumberTransition,
            eToExptByNumberTransition, expToExpByNumberTransition, startToDontKnowByCommaTransition, signToDontKnowByCommaTransition
            , dontKnowToFloatByNumberTransition, decToEndTransition, floatToEndTransition,
            expToEndTransition, endToDoneTrasition, nonexistTransition, eToDoneTransition, startToDoneTransition, signToDoneTransition,
             dontKnowToDoneTransition, signEToDoneTransition;
    /**
     * Array of all transitions
     */
    public static Transition [] transitions;
    /**
     * Textarea of recognized number
     */
    @FXML
    public TextArea outputTA;
    /**
     * Textarea of transitions
     */
    @FXML
    public TextArea inputTA;
    /**
     * End button
     */
    @FXML
    public Button end;
    @FXML
    public  ScrollBar slider;

    @FXML
    public  VBox content;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("TI.fxml"));
        Parent root = fxmlLoader.load();

        startState = new State("A",false, (Circle) fxmlLoader.getNamespace().get("startCircle"));
        signState = new State("B",false, (Circle) fxmlLoader.getNamespace().get("signCircle"));
        intState = new State("C",false, (Circle) fxmlLoader.getNamespace().get("intCircle"));
        //errorState = new State("error",false, (Circle) fxmlLoader.getNamespace().get("errorCircle"));
        decState = new State("D",false, (Circle) fxmlLoader.getNamespace().get("decCircle"));
        floatState = new State("E",false, (Circle) fxmlLoader.getNamespace().get("floatCircle"));
        eState = new State("F",false, (Circle) fxmlLoader.getNamespace().get("eCircle"));
        signEState =  new State( "G",false, (Circle) fxmlLoader.getNamespace().get("signECircle"));
        endState = new State("I", false, (Circle) fxmlLoader.getNamespace().get("endCircle"));
        expState = new State( "H",false, (Circle) fxmlLoader.getNamespace().get("expCircle"));
        dontKnowState = new State( "J",false, (Circle) fxmlLoader.getNamespace().get("dontKnowCircle"));
        doneState = new State("Error", false, null);
        states = new State[]{startState, signEState, intState, eState,  decState, floatState, signState,
                endState, dontKnowState, expState, doneState};
        startState.setActual(true);

        xToStartByNothingTransition = new Transition(false,new Object[]{(Line) fxmlLoader.getNamespace().get("xToStartByNothingLine"),
                (Line) fxmlLoader.getNamespace().get("T1"), (Line) fxmlLoader.getNamespace().get("T2")},"x", null, startState);
        startToSignByPlusTransition = new Transition(false, new Object[]{(CubicCurve) fxmlLoader.getNamespace().get("startToSignByPlusLine")
        , (Line) fxmlLoader.getNamespace().get("T3"), (Line) fxmlLoader.getNamespace().get("T4")},"+",startState, signState);
        startToSignByMinusTransition = new Transition(false, new Object[]{(CubicCurve) fxmlLoader.getNamespace().get("startToSignByMinusLine"),
                (Line) fxmlLoader.getNamespace().get("T5"), (Line) fxmlLoader.getNamespace().get("T6")}, "-", startState, signState);
        signToIntByNumberTransition = new Transition(false, new Object[]{(Line) fxmlLoader.getNamespace().get("signToIntByNumberLine"),
                (Line) fxmlLoader.getNamespace().get("T7"), (Line) fxmlLoader.getNamespace().get("T8")},"number", signState, intState);
        IntToIntByNumberTransition = new Transition(false,new Object[]{(CubicCurve) fxmlLoader.getNamespace().get("IntToIntByNumberLine"),
                (Line) fxmlLoader.getNamespace().get("T9"), (Line) fxmlLoader.getNamespace().get("T10")}, "number", intState, intState);
        intToEByETransition = new Transition(false, new Object[]{(CubicCurve) fxmlLoader.getNamespace().get("intToEByELine"),
                (Line) fxmlLoader.getNamespace().get("T11"), (Line) fxmlLoader.getNamespace().get("T12")},"e",intState,eState);
        startToIntByNumberTransition = new Transition(false,new Object[]{(CubicCurve) fxmlLoader.getNamespace().get("startToIntByNumberLine"),
                (Line) fxmlLoader.getNamespace().get("T13"), (Line) fxmlLoader.getNamespace().get("T14")}, "number", startState, intState);
        intToDecByCommaTransition = new Transition(false, new Object[]{(Line) fxmlLoader.getNamespace().get("intToDecByCommaLine"),
                (Line) fxmlLoader.getNamespace().get("T15"), (Line) fxmlLoader.getNamespace().get("T16")}, ",",intState, decState);
        decToEByETransition = new Transition(false, new Object[]{(CubicCurve) fxmlLoader.getNamespace().get("decToEByELine"),
                (Line) fxmlLoader.getNamespace().get("T17"), (Line) fxmlLoader.getNamespace().get("T18")}, "e",decState,eState);
        decToFloatByNumberTransition = new Transition(false, new Object[]{(Line) fxmlLoader.getNamespace().get("decToFloatByNumber"),
                (Line) fxmlLoader.getNamespace().get("T19"), (Line) fxmlLoader.getNamespace().get("T20")}, "number", decState,floatState);
        floatToFloatByNumberTransition = new Transition(false, new Object[]{(CubicCurve) fxmlLoader.getNamespace().get("floatToFloatByNumber"),
                (Line) fxmlLoader.getNamespace().get("T21"), (Line) fxmlLoader.getNamespace().get("T22")},"number",floatState, floatState);
        floatToEByETransition = new Transition(false, new Object[]{(Line) fxmlLoader.getNamespace().get("floatToEByE"),
                (Line) fxmlLoader.getNamespace().get("T23"), (Line) fxmlLoader.getNamespace().get("T24")}, "e",floatState,eState);
        eToSignEByPlusTransition = new Transition(false, new Object[]{(CubicCurve) fxmlLoader.getNamespace().get("eToSignEByPlus"),
                (Line) fxmlLoader.getNamespace().get("T25"), (Line) fxmlLoader.getNamespace().get("T26")},"+",eState, signEState);
        eToSignEByMinusTransition = new Transition(false, new Object[]{(CubicCurve) fxmlLoader.getNamespace().get("eToSignEByMinus"),
                (Line) fxmlLoader.getNamespace().get("T27"), (Line) fxmlLoader.getNamespace().get("T28")},"-",eState, signEState);
        signEToExptByNumberTransition = new Transition(false,  new Object[]{(Line) fxmlLoader.getNamespace().get("signEToExptByNumber"),
                (Line) fxmlLoader.getNamespace().get("T29"), (Line) fxmlLoader.getNamespace().get("T30")},"number",signEState,expState);
        eToExptByNumberTransition = new Transition(false,new Object[]{(CubicCurve) fxmlLoader.getNamespace().get("eToExptByNumber"),
                (Line) fxmlLoader.getNamespace().get("T31"), (Line) fxmlLoader.getNamespace().get("T32")},"number",eState,expState);
        expToExpByNumberTransition = new Transition(false,new Object[]{(CubicCurve) fxmlLoader.getNamespace().get("expToExpByNumber"),
                (Line) fxmlLoader.getNamespace().get("T33"), (Line) fxmlLoader.getNamespace().get("T34")},"number",expState, expState);
        startToDontKnowByCommaTransition = new Transition(false, new Object[]{(CubicCurve) fxmlLoader.getNamespace().get("startToDontKnowByComma"),
                (Line) fxmlLoader.getNamespace().get("T35"), (Line) fxmlLoader.getNamespace().get("T36")},",", startState,dontKnowState);
        signToDontKnowByCommaTransition = new Transition(false, new Object[]{(CubicCurve) fxmlLoader.getNamespace().get("signToDontKnowByComma"),
                (Line) fxmlLoader.getNamespace().get("T37"), (Line) fxmlLoader.getNamespace().get("T38")},",",signState,dontKnowState);
        dontKnowToFloatByNumberTransition = new Transition(false, new Object[]{(CubicCurve) fxmlLoader.getNamespace().get("dontKnowToFloatByNumber"),
                (Line) fxmlLoader.getNamespace().get("T39"), (Line) fxmlLoader.getNamespace().get("T40")},"number",dontKnowState,floatState);
        decToEndTransition = new Transition(false, new Object[]{(CubicCurve) fxmlLoader.getNamespace().get("E19"),
                (Line) fxmlLoader.getNamespace().get("E20"), (Line) fxmlLoader.getNamespace().get("E21")}, "error", decState, endState);
        floatToEndTransition = new Transition(false, new Object[]{(Line) fxmlLoader.getNamespace().get("E22"),
                (Line) fxmlLoader.getNamespace().get("E23"), (CubicCurve) fxmlLoader.getNamespace().get("E24")}, "error",floatState, endState);
        expToEndTransition = new Transition(false, new Object[]{(Line) fxmlLoader.getNamespace().get("E25"),
                (Line) fxmlLoader.getNamespace().get("E26"), (Line) fxmlLoader.getNamespace().get("E27")}, "space", expState, endState);
        endToDoneTrasition = new Transition(false, new Object[]{(Line) fxmlLoader.getNamespace().get("D1"), (Line) fxmlLoader.getNamespace().get("D2"),
                fxmlLoader.getNamespace().get("D3")}, "error", endState, null);
        nonexistTransition = new Transition(false, null, "error" ,doneState, doneState);
        eToDoneTransition = new Transition(false, null,"error", eState, doneState);
        startToDoneTransition = new Transition(false, null, "error", startState, doneState);
        signToDoneTransition = new Transition(false, null, "error", signEState, doneState);
        dontKnowToDoneTransition = new Transition(false, null, "error", dontKnowState, doneState);
        signEToDoneTransition = new Transition(false, null, "error", signEState, doneState);
        xToStartByNothingTransition.setActual(true);
        transitions = new Transition[]{xToStartByNothingTransition, startToSignByPlusTransition, startToSignByMinusTransition,
                signToIntByNumberTransition, IntToIntByNumberTransition, intToEByETransition, startToIntByNumberTransition
        , intToDecByCommaTransition, decToEByETransition,decToFloatByNumberTransition, floatToFloatByNumberTransition, floatToEByETransition,
                eToSignEByPlusTransition, eToSignEByMinusTransition, signEToExptByNumberTransition,eToExptByNumberTransition
        , expToExpByNumberTransition, startToDontKnowByCommaTransition, signToDontKnowByCommaTransition, dontKnowToFloatByNumberTransition,
                decToEndTransition,floatToEndTransition, expToEndTransition,endToDoneTrasition, nonexistTransition, eToDoneTransition, startToDoneTransition, signToDoneTransition,
                dontKnowToDoneTransition, signEToDoneTransition};

        Scene scene = new Scene(root, 1280, 640);
        stage.setTitle("KIV/TI Semestrální práce - Tomáš Vítek");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }
    public static void main(String[] args) {
        launch();
    }

    /**
     * Použití slidebaru
     */
    @FXML
    void setSlidebar(){
        slider.valueProperty().addListener((observable, oldValue, newValue) ->
                content.setLayoutY(-newValue.doubleValue() * 5));
    }


    /**
     * Reaction on keyboard input
     * @param key pressed key
     */
    @FXML
    void keyPressed(KeyEvent key){
        KeyCode keyCode = key.getCode();

        if(keyCode == KeyCode.DIGIT0 || keyCode == KeyCode.NUMPAD0 || keyCode == KeyCode.NUMPAD1 || keyCode == KeyCode.NUMPAD2
                || keyCode == KeyCode.NUMPAD3 || keyCode == KeyCode.NUMPAD4 || keyCode == KeyCode.NUMPAD5 || keyCode == KeyCode.NUMPAD6
                || keyCode == KeyCode.NUMPAD7 || keyCode == KeyCode.NUMPAD8 || keyCode == KeyCode.NUMPAD9){
            addNumber(keyCode);
        }

        else if (keyCode == KeyCode.PERIOD){
            addComma();
        }
        else if (keyCode == KeyCode.PLUS){
            addPlus();
        }
        else if (keyCode == KeyCode.ADD){
            addPlus();
        }
        else if (keyCode == KeyCode.SUBTRACT){
            addMinus();
        }
        else if (keyCode == KeyCode.MINUS){
            addMinus();
        }
        else if(keyCode == KeyCode.E){
            addE();
        }
        else if(keyCode == KeyCode.ENTER || keyCode == KeyCode.ESCAPE || keyCode == KeyCode.END || keyCode == KeyCode.SPACE){
        }
        else if(keyCode == KeyCode.UNDERSCORE){
            addEnd();
        }
        else {
            errorhappend(keyCode);
        }
    }

    @FXML
    private void addEnd() {
        if (endState.isActual()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, DONE_MESSAGE);
            alert.setTitle(null);
            alert.setHeaderText("Automat je již v konečném stavu.");
            endToDoneTrasition.setActual(true);
            historyTransitions.remove(historyTransitions.size()-1);
            historyTransitions.get(historyTransitions.size()-1).setActual(false);
            alert.showAndWait();
            endToDoneTrasition.setActual(false);
            historyTransitions.get(historyTransitions.size()-1).setActual(true);
            historyTransitions.remove(historyTransitions.size()-1);
            return;
        }
        symbols.add(String.valueOf(" "));
        if(expState.isActual()){
            changeEveryStateAndTransitionActuality(false);
            endState.setActual(true);
            expToEndTransition.setActual(true);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Řetězec přijat!");
            alert.setTitle(null);
            alert.showAndWait();
        } else if (floatState.isActual()) {
            changeEveryStateAndTransitionActuality(false);
            endState.setActual(true);
            floatToEndTransition.setActual(true);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Řetězec přijat!");
            alert.setTitle(null);
            alert.showAndWait();
        } else if (decState.isActual()){
            changeEveryStateAndTransitionActuality(false);
            endState.setActual(true);
            decToEndTransition.setActual(true);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Řetězec přijat!");
            alert.setTitle(null);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, ERROR_MESSAGE);
            alert.setHeaderText("Řetězec zamítnut.");
            alert.setTitle(null);
            alert.showAndWait();
            changeEveryStateAndTransitionActuality(false);
            doneState.setActual(true);
            nonexistTransition.setActual(true);

            //symbols.remove(symbols.size()-1);
            //return;
        }
        display();
    }

    /**
     * Input of comma symbol
     */
    @FXML
    private void addComma(){
        if (endState.isActual()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, DONE_MESSAGE);
            alert.setTitle(null);
            alert.setHeaderText("Automat je již v konečném stavu.");
            endToDoneTrasition.setActual(true);
            historyTransitions.remove(historyTransitions.size()-1);
            historyTransitions.get(historyTransitions.size()-1).setActual(false);
            alert.showAndWait();
            endToDoneTrasition.setActual(false);
            historyTransitions.get(historyTransitions.size()-1).setActual(true);
            historyTransitions.remove(historyTransitions.size()-1);
            return;
        }
        symbols.add(String.valueOf("."));

        if(intState.isActual()){
            changeEveryStateAndTransitionActuality(false);
            intToDecByCommaTransition.setActual(true);
            decState.setActual(true);
        } else if(startState.isActual()){
            changeEveryStateAndTransitionActuality(false);
            dontKnowState.setActual(true);
            startToDontKnowByCommaTransition.setActual(true);
        } else if(signState.isActual()){
            changeEveryStateAndTransitionActuality(false);
            dontKnowState.setActual(true);
            signToDontKnowByCommaTransition.setActual(true);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, ERROR_MESSAGE);
            alert.setHeaderText("Řetězec zamítnut.");
            alert.setTitle(null);
            alert.showAndWait();
            changeEveryStateAndTransitionActuality(false);
            doneState.setActual(true);

            if(endState.isActual()){
                endToDoneTrasition.setActual(true);
            } else if(signEState.isActual()){
                signEToDoneTransition.setActual(true);
            } else if(dontKnowState.isActual()){
                dontKnowToDoneTransition.setActual(true);
            } else {
                nonexistTransition.setActual(true);
            }

            //symbols.remove(symbols.size()-1);

        }
        display();
    }

    /**
     * Special functionity for restart button
     */
    public void restart(){
        symbols.clear();
        inputTA.clear();
        outputTA.clear();
        historyStates.clear();
        historyTransitions.clear();
        changeEveryStateAndTransitionActuality(false);
        startState.setActual(true);
        xToStartByNothingTransition.setActual(true);
        display();
    }
    /**
     * Input of e symbol
     */
    public void addE() {
        if (endState.isActual()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, DONE_MESSAGE);
            alert.setTitle(null);
            alert.setHeaderText("Automat je již v konečném stavu.");
            endToDoneTrasition.setActual(true);
            historyTransitions.remove(historyTransitions.size()-1);
            historyTransitions.get(historyTransitions.size()-1).setActual(false);
            alert.showAndWait();
            endToDoneTrasition.setActual(false);
            historyTransitions.get(historyTransitions.size()-1).setActual(true);
            historyTransitions.remove(historyTransitions.size()-1);
            return;
        }
        symbols.add("e");
        if(intState.isActual()){
            changeEveryStateAndTransitionActuality(false);
            eState.setActual(true);
            intToEByETransition.setActual(true);
        } else if(decState.isActual()){
            changeEveryStateAndTransitionActuality(false);
            decToEByETransition.setActual(true);
            eState.setActual(true);
        } else if(floatState.isActual()){
            changeEveryStateAndTransitionActuality(false);
            eState.setActual(true);
            floatToEByETransition.setActual(true);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, ERROR_MESSAGE);
            alert.setHeaderText("Řetězec zamítnut.");
            alert.setTitle(null);
            alert.showAndWait();
            changeEveryStateAndTransitionActuality(false);
            doneState.setActual(true);
           if(dontKnowState.isActual()){
               dontKnowToDoneTransition.setActual(true);
           } else if(eState.isActual()){
               eToDoneTransition.setActual(true);
           } else if(signEState.isActual()){
               signEToDoneTransition.setActual(true);
           }
           else {
               nonexistTransition.setActual(true);
           }

            //symbols.remove(symbols.size()-1);

        }
        display();
    }

    /**
     * Special functionality for "return by one step" button
     */
    public void backspace(){
        if(symbols.size() == 0){
            Alert aler = new Alert(Alert.AlertType.WARNING, "Vstup je prázdný, nelze odebrat žádný prvek");
            aler.showAndWait();
            return;
        }
        String object = symbols.get(symbols.size()-1);
        symbols.remove(symbols.size()-1);
        State actualState = historyStates.get(historyStates.size()-1);
        State lastState = historyStates.get(historyStates.size()-2);
        historyStates.remove(historyStates.size()-1);
        historyStates.remove(historyStates.size()-1);
        actualState.setActual(false);
        lastState.setActual(true);

        historyTransitions.get(historyTransitions.size()-1).setActual(false);
        historyTransitions.remove(historyTransitions.size()-1);
        historyTransitions.get(historyTransitions.size()-1).setActual(true);
        historyTransitions.remove(historyTransitions.size()-1);

        display();
    }
    /**
     * Input of number symbol
     * Reactng to button input
     */
    public void addNumber(ActionEvent actionEvent) {
        if (endState.isActual()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, DONE_MESSAGE);
            alert.setTitle(null);
            alert.setHeaderText("Automat je již v konečném stavu.");
            endToDoneTrasition.setActual(true);
            historyTransitions.remove(historyTransitions.size()-1);
            historyTransitions.get(historyTransitions.size()-1).setActual(false);
            alert.showAndWait();
            endToDoneTrasition.setActual(false);
            historyTransitions.get(historyTransitions.size()-1).setActual(true);
            historyTransitions.remove(historyTransitions.size()-1);
            return;
        }
        String string = actionEvent.getSource().toString();
        char number = string.charAt(string.length()-2);
        symbols.add(String.valueOf(number));
        if(startState.isActual()){
            changeEveryStateAndTransitionActuality(false);
            intState.setActual(true);
            startToIntByNumberTransition.setActual(true);
        } else if (signState.isActual()){
            changeEveryStateAndTransitionActuality(false);
            intState.setActual(true);
            signToIntByNumberTransition.setActual(true);
        } else if(intState.isActual()){
            changeEveryStateAndTransitionActuality(false);
            intState.setActual(true);
            IntToIntByNumberTransition.setActual(true);
        } else if(decState.isActual()){
            changeEveryStateAndTransitionActuality(false);
            decToFloatByNumberTransition.setActual(true);
            floatState.setActual(true);
        }else if(floatState.isActual()){
            changeEveryStateAndTransitionActuality(false);
            floatState.setActual(true);
            floatToFloatByNumberTransition.setActual(true);
        } else if(eState.isActual()){
            changeEveryStateAndTransitionActuality(false);
            expState.setActual(true);
            eToExptByNumberTransition.setActual(true);
        } else if(signEState.isActual()){
            changeEveryStateAndTransitionActuality(false);
            expState.setActual(true);
            signEToExptByNumberTransition.setActual(true);
        }else if(expState.isActual()){;
            changeEveryStateAndTransitionActuality(false);
            expState.setActual(true);
            expToExpByNumberTransition.setActual(true);
        } else if(dontKnowState.isActual()){
            changeEveryStateAndTransitionActuality(false);
            floatState.setActual(true);
            dontKnowToFloatByNumberTransition.setActual(true);
        }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, ERROR_MESSAGE);
            alert.setHeaderText("Řetězec zamítnut.");
            alert.setTitle(null);
            alert.showAndWait();
            changeEveryStateAndTransitionActuality(false);
            doneState.setActual(true);
            nonexistTransition.setActual(true);
            //symbols.remove(symbols.size()-1);

        }
        display();
    }

    /**
     * Input of number symbol
     * Reacting to keyboard
     */
   @FXML
    public void addNumber(KeyCode keyCode) {
       if (endState.isActual()) {
           Alert alert = new Alert(Alert.AlertType.INFORMATION, DONE_MESSAGE);
           alert.setTitle(null);
           alert.setHeaderText("Automat je již v konečném stavu.");
           endToDoneTrasition.setActual(true);
           historyTransitions.remove(historyTransitions.size()-1);
           historyTransitions.get(historyTransitions.size()-1).setActual(false);
           alert.showAndWait();
           endToDoneTrasition.setActual(false);
           historyTransitions.get(historyTransitions.size()-1).setActual(true);
           historyTransitions.remove(historyTransitions.size()-1);
           return;
       }
        symbols.add(String.valueOf(keyCode.getCode() - RECALCULATION_TO_NUMBER));
       if(startState.isActual()){
           changeEveryStateAndTransitionActuality(false);
           intState.setActual(true);
           startToIntByNumberTransition.setActual(true);
       } else if (signState.isActual()){
           changeEveryStateAndTransitionActuality(false);
           intState.setActual(true);
           signToIntByNumberTransition.setActual(true);
       } else if(intState.isActual()){
           changeEveryStateAndTransitionActuality(false);
           intState.setActual(true);
           IntToIntByNumberTransition.setActual(true);
       } else if(decState.isActual()){
           changeEveryStateAndTransitionActuality(false);
           decToFloatByNumberTransition.setActual(true);
           floatState.setActual(true);
       }else if(floatState.isActual()){
           changeEveryStateAndTransitionActuality(false);
           floatState.setActual(true);
           floatToFloatByNumberTransition.setActual(true);
       } else if(eState.isActual()){
           changeEveryStateAndTransitionActuality(false);
           expState.setActual(true);
           eToExptByNumberTransition.setActual(true);
       } else if(signEState.isActual()){
           changeEveryStateAndTransitionActuality(false);
           expState.setActual(true);
           signEToExptByNumberTransition.setActual(true);
       }else if(expState.isActual()){;
           changeEveryStateAndTransitionActuality(false);
           expState.setActual(true);
           expToExpByNumberTransition.setActual(true);
       } else if(dontKnowState.isActual()){
           changeEveryStateAndTransitionActuality(false);
           floatState.setActual(true);
           dontKnowToFloatByNumberTransition.setActual(true);
       }else {
           Alert alert = new Alert(Alert.AlertType.INFORMATION, ERROR_MESSAGE);
           alert.setHeaderText("Řetězec zamítnut.");
           alert.setTitle(null);
           alert.showAndWait();
           changeEveryStateAndTransitionActuality(false);
           doneState.setActual(true);
           nonexistTransition.setActual(true);

           //symbols.remove(symbols.size()-1);

       }
       display();
    }

    /**
     * Input of plus symbol
     */
    @FXML
    private void addPlus() {
        if (endState.isActual()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, DONE_MESSAGE);
            alert.setTitle(null);
            alert.setHeaderText("Automat je již v konečném stavu.");
            endToDoneTrasition.setActual(true);
            historyTransitions.remove(historyTransitions.size()-1);
            historyTransitions.get(historyTransitions.size()-1).setActual(false);
            alert.showAndWait();
            endToDoneTrasition.setActual(false);
            historyTransitions.get(historyTransitions.size()-1).setActual(true);
            historyTransitions.remove(historyTransitions.size()-1);
            return;
        }
        symbols.add("+");

        if (startState.isActual()){
            changeEveryStateAndTransitionActuality(false);
            signState.setActual(true);
            startToSignByPlusTransition.setActual(true);
        } else if (eState.isActual()){
            changeEveryStateAndTransitionActuality(false);
            signEState.setActual(true);
            eToSignEByPlusTransition.setActual(true);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, ERROR_MESSAGE);
            alert.setHeaderText("Řetězec zamítnut.");
            alert.setTitle(null);
            alert.showAndWait();
            changeEveryStateAndTransitionActuality(false);
            doneState.setActual(true);
            if(signState.isActual()){
                signEToDoneTransition.setActual(true);
            } else if(signEState.isActual()){
                signEToDoneTransition.setActual(true);
            } else if(dontKnowState.isActual()){
                dontKnowToDoneTransition.setActual(true);
            } else {
                nonexistTransition.setActual(true);
            }

            // symbols.remove(symbols.size()-1);

        }
        display();
    }
    /**
     * Input of minus symbol
     */
    @FXML
    private void addMinus() {
        if (endState.isActual()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, DONE_MESSAGE);
            alert.setTitle(null);
            alert.setHeaderText("Automat je již v konečném stavu.");
            endToDoneTrasition.setActual(true);
            historyTransitions.remove(historyTransitions.size()-1);
            historyTransitions.get(historyTransitions.size()-1).setActual(false);
            alert.showAndWait();
            endToDoneTrasition.setActual(false);
            historyTransitions.get(historyTransitions.size()-1).setActual(true);
            historyTransitions.remove(historyTransitions.size()-1);
            return;
        }
        symbols.add("-");
        if (startState.isActual()){
            changeEveryStateAndTransitionActuality(false);
            signState.setActual(true);
            startToSignByMinusTransition.setActual(true);
        } else if (eState.isActual()){
            changeEveryStateAndTransitionActuality(false);
            signEState.setActual(true);
            eToSignEByMinusTransition.setActual(true);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, ERROR_MESSAGE);
            alert.setHeaderText("Řetězec zamítnut.");
            alert.setTitle(null);
            alert.showAndWait();
            changeEveryStateAndTransitionActuality(false);
            doneState.setActual(true);
            if(signState.isActual()){
                signEToDoneTransition.setActual(true);
            } else if(signEState.isActual()){
                signEToDoneTransition.setActual(true);
            } else if(dontKnowState.isActual()){
                dontKnowToDoneTransition.setActual(true);
            } else {
                nonexistTransition.setActual(true);
            }
            // symbols.remove(symbols.size()-1);

        }
        display();
    }

    /**
     * Function for displaying changes to user
     */
    public void display() {
        String result = "";
        String result_2 = "";


        if(symbols.isEmpty()){

            outputTA.setText("");
            inputTA.setText("A\n");
        }

        for (int i = 0; i < symbols.size(); i++) {
            if(i == 0){
                result += "A\n";
            }
            result += "δ(" + historyStates.get(i).getName() + ", '" + symbols.get(i).toString() + "') = " + historyStates.get(i+1).getName() + "\n";
            result_2 += symbols.get(i).toString();

            inputTA.setText(result);
            outputTA.setText(result_2);
        }
    }

    /**
     * Change every state and transition to required state
     * @param bool - required state
     */
    private void changeEveryStateAndTransitionActuality(boolean bool){
        for (int i = 0; i < states.length; i++) {
            states[i].setActual(bool);
        }
        for (int i = 0; i < transitions.length; i++) {
            transitions[i].setActual(bool);
        }
    }

    /**
     * Findes required connection between two states
     * @param finalState end of transition
     * @return required Transition or null
     */
    private Transition findTransition(State finalState){
        State startState = historyStates.get(historyStates.size()-1);
        for (int i = 0; i < transitions.length; i++) {
            if(transitions[i].getEndState() == finalState && transitions[i].getInitialState() == startState){
                return transitions[i];
            }
        }
                return null;
    }

    /**
     * Special functionality for error state
     */
    private void errorhappend(KeyCode keyCode) {
        if (endState.isActual()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, DONE_MESSAGE);
            alert.setTitle(null);
            alert.setHeaderText("Automat je již v konečném stavu.");
            endToDoneTrasition.setActual(true);
            historyTransitions.remove(historyTransitions.size()-1);
            historyTransitions.get(historyTransitions.size()-1).setActual(false);
            alert.showAndWait();
            endToDoneTrasition.setActual(false);
            historyTransitions.get(historyTransitions.size()-1).setActual(true);
            historyTransitions.remove(historyTransitions.size()-1);
            return;
        }
        int key = keyCode.getCode();
        char symbol = (char) key;
        Alert alert = new Alert(Alert.AlertType.INFORMATION, ERROR_MESSAGE);
        alert.setHeaderText("Řetězec zamítnut.");
        alert.setTitle(null);
        alert.showAndWait();
    }
}