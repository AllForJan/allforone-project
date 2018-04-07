package com.vaadin.starter.beveragebuddy.backend;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Map;
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
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -8415408597743194967L;
	// URL;Ziadatel;ICO;Rok;Lokalita;Diel;Kultura;Vymera

    private Long id;
    private String ziadatel;
    private List<String> dalsieNazvy = new ArrayList<>();
    private String ico;

    private BigDecimal maxRozdielVymer;
    private int maxRok;

    private BigDecimal[] roky = new BigDecimal[20];
    private Map<Integer, Integer> lokality = new HashMap<>();
	
	private FinstatData finstatData;

    private List<ZiadostDiely> listZiadostDiely = new ArrayList<>();


    public void setZiadatel(String aZiadatel) {
        if (!aZiadatel.equals(ziadatel)) {
            dalsieNazvy.add(aZiadatel);
        }
    }

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
        return roky[rok - 2000];
    }

    public BigDecimal getMaxRozdielVymer(int rokStart, int rokEnd, boolean forceUpdate) {
        if ((maxRozdielVymer == null) || (forceUpdate)) {
            int start = rokStart;
            BigDecimal max = new BigDecimal(0);
            BigDecimal rozdiel;

            for (int i = start; i < rokEnd; i++) {
                rozdiel = getVymeraZaRok(i + 1).subtract(getVymeraZaRok(i));
                if ((rozdiel.compareTo(max) > 0) && (i > start)) {
                    max = rozdiel;
                    maxRok = i;
                }
            }
            maxRozdielVymer = max;
        }
        return maxRozdielVymer;
    }

    public int getMaxRok() {
        if (maxRok == 0) {
            getMaxRozdielVymer();
        }
        return maxRok;
    }

}
