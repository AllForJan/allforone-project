package com.vaadin.starter.beveragebuddy.backend;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter @Setter @EqualsAndHashCode @ToString
public class PriamaPlatba implements Serializable {
    //URL;Meno;PSC;Obec;Opatrenie;Opatrenie - Kod;Suma;Rok

    private Long id;
    private String ziadatel;
    private String psc;
    private String obec; //toto sa da prepojit so suborom kody_administrativneho_delenia.csv na atribut NAM
    private String opatrenie;
    private String kodOpatrenia;
    private BigDecimal suma;
    private Integer rok;
}
