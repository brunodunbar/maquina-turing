package com.brunodunbar.maquinaturing;

import javafx.geometry.Insets;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

public class Fita extends ScrollPane {

    private List<Celula> celulas;

    private Celula celulaSelecionada;

    private int posicao = 0;
    private final HBox celulaContainer;

    public Fita() {

        getStylesheets().addAll("app.css");

        setPrefHeight(200);
        setMaxHeight(Control.USE_PREF_SIZE);
        setFitToHeight(true);

        celulaContainer = new HBox();
        celulaContainer.setPadding(new Insets(3, 3, 3, 3));
        setContent(celulaContainer);

        celulas = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Celula celula = new Celula();
            celulas.add(celula);
            celulaContainer.getChildren().add(celula);
        }

        Celula primeiraCelula = celulas.get(0);

        primeiraCelula.setEditavel(false);
        primeiraCelula.setValor(">");

        selecionarCelula(primeiraCelula);
    }

    private void selecionarCelula(Celula celula) {

        if (celulaSelecionada != null) {
            celulaSelecionada.setSelecionada(false);
        }

        celulaSelecionada = celula;
        celulaSelecionada.setSelecionada(true);

        double h = getContent().getBoundsInLocal().getWidth();
        double y = (celulaSelecionada.getBoundsInParent().getMaxX() + celulaSelecionada.getBoundsInParent().getMinX()) / 2.0;
        double v = getViewportBounds().getWidth();
        setHvalue(getHmax() * ((y - 0.5 * v) / (h - v)));
    }

    public void move(Direcao direcao) {
        int novaPosicao = direcao == Direcao.DIREITA ? posicao + 1 : posicao - 1;
        if (novaPosicao < 0 || novaPosicao >= celulas.size()) {
            throw new IllegalStateException("Movimento inválido da cabeça de leitura para a posição " + novaPosicao);
        }

        Celula celula = celulas.get(novaPosicao);
        selecionarCelula(celula);
        posicao = novaPosicao;
    }

    public String le() {
        if (celulaSelecionada == null || celulaSelecionada.getValor() == null) {
            return "";
        }

        return celulaSelecionada.getValor();
    }

    public void escreve(String valor) {
        if (celulaSelecionada == null) {
            return;
        }

        celulaSelecionada.setValor(valor);
    }

    public void resetarPosicao() {
        Celula celula = celulas.get(0);
        selecionarCelula(celula);
        posicao = 0;
    }

    public void limpar() {
        celulas.stream().forEach(celula -> celula.setValor(""));
        celulas.get(0).setValor(">");
        resetarPosicao();
    }
}
