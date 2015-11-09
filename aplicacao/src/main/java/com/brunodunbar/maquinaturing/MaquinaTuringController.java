package com.brunodunbar.maquinaturing;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class MaquinaTuringController {

    @FXML
    public TableColumn uxComandosMove;
    @FXML
    public TableColumn uxComandosProximoEstado;
    @FXML
    public TableColumn uxComandosEscreve;
    @FXML
    public TableColumn uxComandosLe;
    @FXML
    public TableColumn uxComandosEstado;

    @FXML
    public VBox uxContent;

    @FXML
    private Fita uxFita;

    @FXML
    private TableView<Comando> uxComandos;

    @FXML
    private Spinner<Integer> uxQuantidadeEstados;

    private ObservableList<Comando> comandos = FXCollections.observableArrayList();
    private ObservableList<Estado> estados = FXCollections.observableArrayList(Estado.INICIAL);

    @FXML
    public void initialize() {
        uxComandos.setItems(comandos);

        uxComandosEstado.setCellFactory(param -> new ComboBoxTableCell(Estado.stringConverter(), estados));
        uxComandosLe.setCellFactory(param -> new CustomTextFieldTableCell());
        uxComandosEscreve.setCellFactory(param -> new CustomTextFieldTableCell());
        uxComandosProximoEstado.setCellFactory(param -> new ComboBoxTableCell(Estado.stringConverter(), estados));
        uxComandosMove.setCellFactory(param -> new ComboBoxTableCell(Direcao.stringConverter(), Direcao.values()));

        uxQuantidadeEstados.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 1000, 10));
        uxQuantidadeEstados.valueProperty().addListener(observable -> {
            updateEstados();
        });

        updateEstados();
    }

    private void updateEstados() {

        Integer value = uxQuantidadeEstados.getValue();
        if (value == estados.size()) {
            return;
        }

        while (value < estados.size()) {
            Estado estado = estados.remove(estados.size() - 1);
            for (Comando comando : comandos) {
                if (Objects.equals(comando.getEstado(), estado)) {
                    comando.setEstado(null);
                }

                if (Objects.equals(comando.getProximoEstado(), estado)) {
                    comando.setProximoEstado(null);
                }
            }
        }

        while (value >= estados.size()) {
            estados.add(new Estado("Q" + estados.size()));
        }
    }

    @FXML
    private void onExecutar(ActionEvent actionEvent) {
        System.out.print("onExecutar");
    }

    @FXML
    public void onComando(ActionEvent actionEvent) {
        comandos.addAll(new Comando());
    }

    public void onSalvar(ActionEvent actionEvent) {


        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");

        File file = fileChooser.showSaveDialog(uxContent.getScene().getWindow());
        if (file != null) {

            try(JsonWriter writer = new JsonWriter(new FileWriter(file))) {

                writer.beginObject();
                writer.name("quantidadeEstados").value(uxQuantidadeEstados.getValue());

                writer.name("comandos");
                writer.beginArray();
                for(Comando comando: comandos) {

                    writer.beginObject();

                    if(comando.getEstado() != null) {
                        writer.name("estado").value(comando.getEstado().toString());
                    }

                    writer.name("le").value(comando.getLe());
                    writer.name("escreve").value(comando.getEscreve());

                    if(comando.getProximoEstado() != null) {
                        writer.name("estado").value(comando.getProximoEstado().toString());
                    }

                    if(comando.getMove() != null) {
                        writer.name("move").value(comando.getMove().toString());
                    }

                    writer.endObject();

                }
                writer.endArray();
                writer.endObject();

                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onAbrir(ActionEvent actionEvent) {
    }
}
