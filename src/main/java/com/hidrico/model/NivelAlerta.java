package com.hidrico.model;

public enum NivelAlerta {

    NORMAL("Normal", "#28a745"),
    ATENCAO("Atenção", "#ffc107"),
    CRITICO("Crítico", "#dc3545");

    private final String descricao;
    private final String corHex;

    NivelAlerta(String descricao, String corHex) {
        this.descricao = descricao;
        this.corHex = corHex;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getCorHex() {
        return corHex;
    }
}