package com.brunodunbar.maquinaturing;

import javafx.geometry.Insets;
import javafx.scene.control.Control;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

public class Fita extends ScrollPane {

    private HBox hbox;

    private List<Celula> celulas;

    private Celula celulaSelecionada;

    public Fita(int posicoes) {

        setPrefHeight(200);
        setMaxHeight(Control.USE_PREF_SIZE);
        setFitToHeight(true);

        hbox = new HBox();
        hbox.setPadding(new Insets(3, 3, 3, 3));

        celulas = new ArrayList<>();
        for (int i = 0; i < posicoes; i++) {
            Celula celula = new Celula();
            celulas.add(celula);
            hbox.getChildren().add(celula);
        }

        Celula primeiraCelula = celulas.get(0);

        primeiraCelula.setEditavel(false);
        primeiraCelula.setValor("->");

        selecionarCelula(primeiraCelula);

        setContent(hbox);
    }

    private void selecionarCelula(Celula celula) {

        if (celulaSelecionada != null) {
            celulaSelecionada.setSelecionada(false);
        }

        celulaSelecionada = celula;
        celulaSelecionada.setSelecionada(true);
    }
}
