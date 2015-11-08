package com.brunodunbar.maquinaturing;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Comando {

    private ObjectProperty<Estado> estado = new SimpleObjectProperty<>();
    private StringProperty le = new SimpleStringProperty();
    private StringProperty escreve = new SimpleStringProperty();
    private ObjectProperty<Estado> proximoEstado = new SimpleObjectProperty<>();
    private ObjectProperty<Direcao> move = new SimpleObjectProperty<>();

    public Estado getEstado() {
        return estado.get();
    }

    public ObjectProperty<Estado> estadoProperty() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado.set(estado);
    }

    public String getLe() {
        return le.get();
    }

    public StringProperty leProperty() {
        return le;
    }

    public void setLe(String le) {
        this.le.set(le);
    }

    public String getEscreve() {
        return escreve.get();
    }

    public StringProperty escreveProperty() {
        return escreve;
    }

    public void setEscreve(String escreve) {
        this.escreve.set(escreve);
    }

    public Estado getProximoEstado() {
        return proximoEstado.get();
    }

    public ObjectProperty<Estado> proximoEstadoProperty() {
        return proximoEstado;
    }

    public void setProximoEstado(Estado proximoEstado) {
        this.proximoEstado.set(proximoEstado);
    }

    public Direcao getMove() {
        return move.get();
    }

    public ObjectProperty<Direcao> moveProperty() {
        return move;
    }

    public void setMove(Direcao move) {
        this.move.set(move);
    }
}
