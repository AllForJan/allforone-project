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

	/**
	 * 
	 */
	private static final long serialVersionUID = -8415408597743194967L;
	// URL;Ziadatel;ICO;Rok;Lokalita;Diel;Kultura;Vymera

	private Long id;
	private String ziadatel;
	private Set<String> dalsieNazvy = new HashSet<>();
	private String ico;

	private int maxRok = -1;
	private int minRok = -1;

	private BigDecimal[] roky = new BigDecimal[20];

	private FinstatData finstatData;

	private Map<Integer, Set<String>> lokality = new HashMap<>();

	private List<ZiadostDiely> listZiadostDiely = new ArrayList<>();

	public int getLokalityZaRok(int rok) {
		return lokality.getOrDefault(rok, new HashSet<>()).size();
	}

	public void setZiadatel(String aZiadatel) {
		if (ziadatel == null) {
			ziadatel = aZiadatel;
		} else {
			dalsieNazvy.add(aZiadatel);
		}
	}

	public String getDalsNazvy(){
		String returnStr = "";
		int i = 0;
		for (String nazov:dalsieNazvy){
			if (!nazov.equalsIgnoreCase(ziadatel)){
				i++;
				if (i==1){
					returnStr = nazov;
				} else {
					returnStr = returnStr + "," + nazov;
				}
			}
		}
		return returnStr;
	}

	public BigDecimal getVymeraZaRok(int rok) {
		if (roky[rok - 2000] == null) {
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

	public BigDecimal getMaxRozdielVymer(int rokStart, int rokEnd) {

		BigDecimal max = new BigDecimal(0);
		BigDecimal rozdiel;
		maxRok = rokStart;

		for (int i = rokStart; i < rokEnd; i++) {
			rozdiel = getVymeraZaRok(i + 1).subtract(getVymeraZaRok(i));
			if (rozdiel.compareTo(max) > 0) {
				max = rozdiel;
				maxRok = i;
			}
		}
		return max;
	}

	public BigDecimal getMinRozdielVymer(int rokStart, int rokEnd) {
		BigDecimal min = new BigDecimal(10000000);
		BigDecimal rozdiel;
		minRok = rokStart;

		for (int i = rokStart; i < rokEnd; i++) {
			rozdiel = getVymeraZaRok(i + 1).subtract(getVymeraZaRok(i));
			if (rozdiel.compareTo(min) < 0) {
				min = rozdiel;
				minRok = i;
			}
		}
		return min;
	}

	public int getMinRok(int rokStart, int rokEnd) {
		getMinRozdielVymer(rokStart, rokEnd);
		return minRok;
	}

	public int getMaxRok(int rokStart, int rokEnd) {
		getMaxRozdielVymer(rokStart, rokEnd);
		return maxRok;
	}

	void addZiadostDiely(ZiadostDiely zd) {
		listZiadostDiely.add(zd);

		int rok = zd.getRok();
		String lokalita = zd.getLokalita();

		Set<String> lokalityPreRok = lokality.getOrDefault(rok, new HashSet<>());
		lokalityPreRok.add(lokalita.toLowerCase());
		lokality.put(rok, lokalityPreRok);
	}

	public String getAdresaString() {
		if (getFinstatData() != null) {
			return (getFinstatData().getAdresa() + ", " + finstatData.getMesto()).replace("\"", "");
		}
		return "";
	}

	public Integer getMaximumLokalit() {
		return lokality.entrySet().stream().map(lok -> lok.getValue().size()).max(Integer::compareTo).orElse(0);
	}
}
