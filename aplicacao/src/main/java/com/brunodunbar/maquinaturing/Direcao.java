package com.brunodunbar.maquinaturing;

import javafx.util.StringConverter;

public enum Direcao {
    DIREITA, ESQUERDA;

    private static final DirecaoStringConverter CONVERTER = new DirecaoStringConverter();

    public static StringConverter stringConverter() {
        return CONVERTER;
    }

    public static class DirecaoStringConverter extends StringConverter<Direcao> {

        @Override
        public String toString(Direcao object) {
            return object == null ? "" : object.toString();
        }

        @Override
        public Direcao fromString(String string) {
            return Direcao.valueOf(string);
        }
    }

}
