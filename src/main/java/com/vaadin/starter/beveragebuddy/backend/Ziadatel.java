package com.vaadin.starter.beveragebuddy.backend;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

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
    
    private BigDecimal maxRozdielVymer;
    private int maxRok;

    private BigDecimal[] roky = new BigDecimal[20];
    private Map<Integer, Integer> lokality = new HashMap<>();

    private List<ZiadostDiely> listZiadostDiely = new ArrayList<>();

    public int getLokalityZaRok(int rok) {
    	return 0;
	}


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

    public BigDecimal getMaxRozdielVymer(){
        if (maxRozdielVymer==null) {
            int start = 2001;
            BigDecimal max = new BigDecimal(0);
            BigDecimal rozdiel;

            for (int i = start; i < 2018; i++) {
                rozdiel = getVymeraZaRok(i + 1).subtract(getVymeraZaRok(i));
                if (rozdiel.compareTo(max) > 0) {
                    max = rozdiel;
                    maxRok = i;
                }
            }
            maxRozdielVymer = max;
        }
        return maxRozdielVymer;
    }

    public int getMaxRok(){
        if (maxRok==0){
            getMaxRozdielVymer();
        }
        return maxRok;
    }

}
