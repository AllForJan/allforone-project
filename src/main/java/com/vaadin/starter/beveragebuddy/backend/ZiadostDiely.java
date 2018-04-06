package com.vaadin.starter.beveragebuddy.backend;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a beverage category.
 */
public class ZiadostDiely implements Serializable {

	// URL;Ziadatel;ICO;Rok;Lokalita;Diel;Kultura;Vymera

	private Long id;
	private String url;
	private String ziadatel;
	private String ico;
	private String rok;
	private String lokalita;
	private String diel;
	private String kultura;
	private String vymera;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getZiadatel() {
		return ziadatel;
	}

	public void setZiadatel(String ziadatel) {
		this.ziadatel = ziadatel;
	}

	public String getIco() {
		return ico;
	}

	public void setIco(String ico) {
		this.ico = ico;
	}

	public String getRok() {
		return rok;
	}

	public void setRok(String rok) {
		this.rok = rok;
	}

	public String getLokalita() {
		return lokalita;
	}

	public void setLokalita(String lokalita) {
		this.lokalita = lokalita;
	}

	public String getDiel() {
		return diel;
	}

	public void setDiel(String diel) {
		this.diel = diel;
	}

	public String getKultura() {
		return kultura;
	}

	public void setKultura(String kultura) {
		this.kultura = kultura;
	}

	public String getVymera() {
		return vymera;
	}

	public void setVymera(String vymera) {
		this.vymera = vymera;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ZiadostDiely other = (ZiadostDiely) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
