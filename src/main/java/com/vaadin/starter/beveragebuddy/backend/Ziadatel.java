package com.vaadin.starter.beveragebuddy.backend;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a beverage category.
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Ziadatel implements Serializable {

    // URL;Ziadatel;ICO;Rok;Lokalita;Diel;Kultura;Vymera

    private Long id;
    private String ziadatel;
    private int ico;

    private BigDecimal[] roky = new BigDecimal[20];

    private List<ZiadostDiely> listZiadostDiely = new ArrayList<>();

    public BigDecimal getVymeraZaRok(int rok){
        if (roky[rok-2000] == null) {
            BigDecimal vymera = new BigDecimal(0);
            for (ZiadostDiely diely : listZiadostDiely) {
                if (diely.getRok() == rok) {
                    vymera = vymera.add(diely.getVymera());
                }
            }
            roky[rok - 2000] = vymera;
        }
        return roky[rok-2000];
    }




}
