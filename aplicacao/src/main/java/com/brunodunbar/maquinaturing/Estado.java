package com.brunodunbar.maquinaturing;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.StringConverter;

public class Estado {

    public static final Estado INICIAL = new Estado("->");

    private static final StringConverter<Estado> CONVERTER = new EstadoStringConverter();

    public static StringConverter<Estado> stringConverter() {
        return CONVERTER;
    }

    private StringProperty nome = new SimpleStringProperty();

    public Estado(String nome) {
        this.nome.set(nome);
    }

    public String getNome() {
        return nome.get();
    }

    public StringProperty nomeProperty() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome.set(nome);
    }

    @Override
    public String toString() {
        return nome.get();
    }

    private static class EstadoStringConverter extends StringConverter<Estado> {

        @Override
        public String toString(Estado estado) {
            return estado == null ? "" : estado.getNome();
        }

        @Override
        public Estado fromString(String string) {
            return null;
        }
    }
}
