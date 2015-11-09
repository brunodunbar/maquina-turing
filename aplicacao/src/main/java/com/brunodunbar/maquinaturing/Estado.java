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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Estado estado = (Estado) o;

        return !(nome != null ? !nome.equals(estado.nome) : estado.nome != null);

    }

    @Override
    public int hashCode() {
        return nome != null ? nome.hashCode() : 0;
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
