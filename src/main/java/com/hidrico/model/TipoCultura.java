package com.hidrico.model;

public enum TipoCultura {

    MILHO("Milho"),
    SOJA("Soja"),
    FEIJAO("Feijão"),
    CAFE("Café"),
    CANA_DE_ACUCAR("Cana-de-açúcar"),
    HORTALICAS("Hortaliças"),
    FRUTICULTURA("Fruticultura"),
    PASTAGEM("Pastagem"),
    OUTROS("Outros");

    private final String descricao;

    TipoCultura(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}