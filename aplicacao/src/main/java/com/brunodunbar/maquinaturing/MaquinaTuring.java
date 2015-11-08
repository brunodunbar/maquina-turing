package com.brunodunbar.maquinaturing;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MaquinaTuring extends Application {

    private MenuBar appMenu;
    private Fita fita;
    private TableView<Comando> comandosTable;
    private Spinner<Integer> quantidadeEstadosSpinner;

    private ObservableList<Comando> comandos = FXCollections.observableArrayList();
    private ObservableList<Estado> estados = FXCollections.observableArrayList(Estado.INICIAL);

    @Override
    public void start(Stage primaryStage) {

        HBox hbox = new HBox();

        Button novoComandoButton = new Button();
        novoComandoButton.setText("Novo Comando");
        novoComandoButton.setOnMouseClicked(event -> {
            comandos.add(new Comando());
        });

        hbox.getChildren().add(novoComandoButton);

        HBox spinnerContainer = new HBox();
        spinnerContainer.getChildren().addAll(getQuantidadeEstadosSpinner());

        VBox vbox = new VBox();
        vbox.getChildren().addAll(getAppMenu(), getFita(), spinnerContainer, getComandosTable(), hbox);

        comandos.add(new Comando());

        Scene scene = new Scene(vbox);
        scene.getStylesheets().add("app.css");

        primaryStage.setScene(scene);
        primaryStage.setTitle("Maquina Turing");
        primaryStage.setWidth(600);
        primaryStage.setHeight(500);
        primaryStage.show();
    }

    private MenuBar getAppMenu() {
        if (appMenu == null) {
            appMenu = new MenuBar();
            Menu arquivoMenu = new Menu("Arquivo");

            appMenu.getMenus().addAll(arquivoMenu);
        }

        return appMenu;
    }

    private Fita getFita() {
        if (fita == null) {
            fita = new Fita(50);
            fita.setPrefHeight(100);
        }
        return fita;
    }

    private TableView<Comando> getComandosTable() {
        if (comandosTable == null) {
            comandosTable = new TableView<>();

            comandosTable.setPrefHeight(200);

            TableColumn estadoColumn = new TableColumn("Estado");
            estadoColumn.setPrefWidth(130);
            estadoColumn.setCellFactory(param -> new ComboBoxTableCell(Estado.stringConverter(), estados));
            estadoColumn.setCellValueFactory(new PropertyValueFactory<Comando, Estado>("estado"));

            TableColumn leColumn = new TableColumn("Lê");
            leColumn.setPrefWidth(100);
            leColumn.setCellFactory(param -> new CustomTextFieldTableCell());
            leColumn.setCellValueFactory(new PropertyValueFactory<Comando, String>("le"));

            TableColumn escreveColumn = new TableColumn("Escreve");
            escreveColumn.setPrefWidth(100);
            escreveColumn.setCellFactory(param -> new CustomTextFieldTableCell());
            escreveColumn.setCellValueFactory(new PropertyValueFactory<Comando, String>("escreve"));

            TableColumn proximoColumn = new TableColumn("Próximo Estado");
            proximoColumn.setPrefWidth(130);
            proximoColumn.setCellValueFactory(new PropertyValueFactory<Comando, Estado>("proximoEstado"));
            proximoColumn.setCellFactory(param -> new ComboBoxTableCell(Estado.stringConverter(), estados));

            TableColumn moveColumn = new TableColumn("Move");
            moveColumn.setPrefWidth(100);
            moveColumn.setCellFactory(param -> new ComboBoxTableCell(Direcao.stringConverter(), Direcao.values()));
            moveColumn.setCellValueFactory(new PropertyValueFactory<Comando, Direcao>("move"));

            comandosTable.getColumns().addAll(estadoColumn, leColumn, escreveColumn, proximoColumn, moveColumn);
            comandosTable.setItems(comandos);

            comandosTable.setEditable(true);
        }

        return comandosTable;
    }

    private Spinner<Integer> getQuantidadeEstadosSpinner() {

        if(quantidadeEstadosSpinner == null) {
            quantidadeEstadosSpinner = new Spinner<>();
            quantidadeEstadosSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 1000, 10));
            quantidadeEstadosSpinner.valueProperty().addListener(observable -> {
                quantidadeEstadosSpinner.getValue();
            });
        }

        return quantidadeEstadosSpinner;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
