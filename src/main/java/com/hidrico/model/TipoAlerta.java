package com.hidrico.model;

public enum TipoAlerta {

    ESTRESSE_HIDRICO("Estresse Hídrico Detectado"),
    VEGETACAO_DEGRADADA("Vegetação Degradada"),
    POSSIVEL_SUPER_IRRIGACAO("Possível Super-irrigação"),
    ANOMALIA_DETECTADA("Anomalia no Padrão Esperado");

    private final String descricao;

    TipoAlerta(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}