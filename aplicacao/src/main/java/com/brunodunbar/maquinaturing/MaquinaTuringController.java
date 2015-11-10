package com.brunodunbar.maquinaturing;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

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
    public TextArea uxSaida;

    @FXML
    private Fita uxFita;

    @FXML
    private TableView<Comando> uxComandos;

    @FXML
    private Spinner<Integer> uxQuantidadeEstados;

    private ObservableList<Comando> comandos = FXCollections.observableArrayList();
    private ObservableList<Estado> estados = FXCollections.observableArrayList(Estado.INICIAL, Estado.FINAL);
    private SpinnerValueFactory.IntegerSpinnerValueFactory spinnerValueFactory;

    private FileChooser abrirFileChooser;
    private FileChooser salvarFileChooser;

    private Estado estadoAtual = Estado.INICIAL;

    @FXML
    public void initialize() {
        uxComandos.setItems(comandos);

        uxComandosEstado.setCellFactory(param -> new ComboBoxTableCell(Estado.stringConverter(), estados));
        uxComandosLe.setCellFactory(CustomTextFieldTableCell.forTableColumn());
        uxComandosLe.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Comando, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Comando, String> t) {
                        t.getTableView().getItems().get(t.getTablePosition().getRow()).setLe(t.getNewValue());
                    }
                }
        );


        uxComandosEscreve.setCellFactory(CustomTextFieldTableCell.forTableColumn());
        uxComandosEscreve.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Comando, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Comando, String> t) {
                        t.getTableView().getItems().get(t.getTablePosition().getRow()).setEscreve(t.getNewValue());
                    }
                }
        );

        uxComandosProximoEstado.setCellFactory(param -> new ComboBoxTableCell(Estado.stringConverter(), estados));
        uxComandosMove.setCellFactory(param -> new ComboBoxTableCell(Direcao.stringConverter(), Direcao.values()));

        spinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 1000, 10);
        uxQuantidadeEstados.setValueFactory(spinnerValueFactory);
        uxQuantidadeEstados.valueProperty().addListener(observable -> {
            updateEstados();
        });

        updateEstados();
    }

    private void updateEstados() {

        Integer value = uxQuantidadeEstados.getValue() + 2; // adiciona os dois estados fixos
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

        while (value > estados.size()) {
            estados.add(new Estado("Q" + (estados.size() - 1)));
        }
    }

    private Estado buscaPorNome(String nome) {
        return estados.stream().filter(estado -> Objects.equals(estado.getNome(), nome))
                .findFirst().orElseGet(() -> null);
    }

    @FXML
    private void onExecutar(ActionEvent actionEvent) {

        uxSaida.setText("");

        while (estadoAtual != Estado.FINAL) {

            String valorAtual = uxFita.le();
            Optional<Comando> comandoOptional = comandos.stream().filter(c -> Objects.equals(c.getEstado(), estadoAtual) && Objects.equals(c.getLe(), valorAtual))
                    .findFirst();

            if (!comandoOptional.isPresent()) {
                uxSaida.appendText("Sem comando para '" + estadoAtual + "', valor '" + valorAtual + "'\n");
                break;
            }

            Comando comando = comandoOptional.get();
            uxFita.escreve(comando.getEscreve());
            uxFita.move(comando.getMove());

            estadoAtual = comando.getProximoEstado();
        }

        System.out.print("onExecutar");
    }

    @FXML
    public void onComando(ActionEvent actionEvent) {
        comandos.addAll(new Comando());
    }

    @FXML
    public void onSalvar(ActionEvent actionEvent) {

        if (salvarFileChooser == null) {
            salvarFileChooser = new FileChooser();
            salvarFileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Json", "*.json"));
            salvarFileChooser.setTitle("Salvar...");
        }

        File file = salvarFileChooser.showSaveDialog(uxContent.getScene().getWindow());
        if (file != null) {

            try (JsonWriter writer = new JsonWriter(new FileWriter(file))) {

                writer.beginObject();
                writer.name("quantidadeEstados").value(uxQuantidadeEstados.getValue());

                writer.name("comandos");
                writer.beginArray();
                for (Comando comando : comandos) {

                    writer.beginObject();

                    if (comando.getEstado() != null) {
                        writer.name("estado").value(comando.getEstado().toString());
                    }

                    if (comando.getLe() != null) {
                        writer.name("le").value(comando.getLe());
                    }

                    if (comando.getEscreve() != null) {
                        writer.name("escreve").value(comando.getEscreve());
                    }

                    if (comando.getProximoEstado() != null) {
                        writer.name("proximoEstado").value(comando.getProximoEstado().toString());
                    }

                    if (comando.getMove() != null) {
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

    @FXML
    public void onAbrir(ActionEvent actionEvent) {

        if (abrirFileChooser == null) {
            abrirFileChooser = new FileChooser();
            abrirFileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Json", "*.json"));
            abrirFileChooser.setTitle("Abrir...");
        }

        File file = abrirFileChooser.showOpenDialog(uxContent.getScene().getWindow());

        if (file != null && file.exists()) {
            try (JsonReader reader = new JsonReader(new FileReader(file))) {

                comandos.clear();

                reader.beginObject();

                while (reader.hasNext()) {

                    switch (reader.nextName()) {
                        case "quantidadeEstados":
                            spinnerValueFactory.setValue(reader.nextInt());
                            updateEstados();
                            break;
                        case "comandos":

                            reader.beginArray();

                            while (reader.hasNext()) {

                                reader.beginObject();

                                Comando comando = new Comando();

                                while (reader.hasNext()) {
                                    switch (reader.nextName()) {
                                        case "estado":
                                            comando.setEstado(buscaPorNome(reader.nextString()));
                                            break;
                                        case "le":
                                            comando.setLe(reader.nextString());
                                            break;
                                        case "escreve":
                                            comando.setEscreve(reader.nextString());
                                            break;
                                        case "proximoEstado":
                                            comando.setProximoEstado(buscaPorNome(reader.nextString()));
                                            break;
                                        case "move":
                                            comando.setMove(Direcao.valueOf(reader.nextString()));
                                            break;
                                        default:
                                            reader.skipValue();
                                    }
                                }

                                comandos.add(comando);

                                reader.endObject();
                            }
                            reader.endArray();
                            break;
                        default:
                            reader.skipValue();
                    }
                }

                reader.endObject();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onResetar(ActionEvent actionEvent) {
        estadoAtual = Estado.INICIAL;
        uxFita.limpar();
    }

    public void onNovo(ActionEvent actionEvent) {
        estadoAtual = Estado.INICIAL;
        comandos.clear();

        spinnerValueFactory.setValue(10);
        updateEstados();

        uxFita.limpar();
    }
}
