package com.brunodunbar.maquinaturing;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class Celula extends BorderPane {

    private StringProperty valorProperty;
    private boolean editavel;
    private boolean editando;

    private BooleanProperty selecionada = new SimpleBooleanProperty();

    private TextField textField;
    private Label label;

    public Celula() {

        getStyleClass().add("celula");

        editavel = true;
        valorProperty = new SimpleStringProperty();

        label = new Label();
        label.setFont(Font.font(24));
        label.setStyle("-fx-text-alignment: center;");
        label.textProperty().bind(valorProperty);

        setCenter(label);

        setOnMouseClicked(this::onMouseClicked);

        selecionada.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                getStyleClass().add("selecionada");
            } else {
                getStyleClass().remove("selecionada");
            }
        });

    }

    protected void onMouseClicked(MouseEvent event) {
        if (isEditavel() && !isEditando()) {
            iniciaEdicao();
        }
    }

    protected void iniciaEdicao() {

        editando = true;

        if (textField == null) {
            textField = new TextField();
            textField.setStyle("-fx-fill-height: true; -fx-fill-width: true;");
            textField.setFont(Font.font(24));
            textField.textProperty().bindBidirectional(valorProperty);
            textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    finalizaEdicao();
                }
            });

            textField.textProperty().addListener((ov, oldValue, newValue) -> {
                if (textField.getText().length() > 1) {
                    String s = textField.getText().substring(0, 1);
                    textField.setText(s);
                }
            });
        }
        setCenter(textField);
        textField.requestFocus();
    }

    protected void finalizaEdicao() {
        editando = false;
        setCenter(label);
    }

    public boolean isEditavel() {
        return editavel;
    }

    public void setEditavel(boolean editavel) {
        this.editavel = editavel;
    }

    public void setValor(String valor) {
        valorProperty.setValue(valor);
    }

    public String getValor() {
        return valorProperty.getValue();
    }

    public boolean isEditando() {
        return editando;
    }

    public void setSelecionada(boolean selecionada) {
        this.selecionada.set(selecionada);
    }

    public boolean getSelecionada() {
        return selecionada.get();
    }
}
