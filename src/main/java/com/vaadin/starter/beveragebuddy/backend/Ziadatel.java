package com.vaadin.starter.beveragebuddy.backend;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a beverage category.
 */
public class Ziadatel implements Serializable {

	// URL;Ziadatel;ICO;Rok;Lokalita;Diel;Kultura;Vymera

	private Long id;
	private String ziadatel;
	private String ico;

	private String rok2005;
	private String rok2006;
	private String rok2007;
	private String rok2008;
	private String rok2009;
	private String rok2010;
	private String rok2011;
	private String rok2012;
	private String rok2013;
	private String rok2014;
	private String rok2015;
	private String rok2016;
	private String rok2017;

	private BigDecimal vymera;

	private List<ZiadostDiely> listZiadostDiely = new ArrayList<>();

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

	public String getRok2005() {
		return rok2005;
	}

	public void setRok2005(String rok2005) {
		this.rok2005 = rok2005;
	}

	public String getRok2006() {
		return rok2006;
	}

	public void setRok2006(String rok2006) {
		this.rok2006 = rok2006;
	}

	public String getRok2007() {
		return rok2007;
	}

	public void setRok2007(String rok2007) {
		this.rok2007 = rok2007;
	}

	public String getRok2008() {
		return rok2008;
	}

	public void setRok2008(String rok2008) {
		this.rok2008 = rok2008;
	}

	public String getRok2009() {
		return rok2009;
	}

	public void setRok2009(String rok2009) {
		this.rok2009 = rok2009;
	}

	public String getRok2010() {
		return rok2010;
	}

	public void setRok2010(String rok2010) {
		this.rok2010 = rok2010;
	}

	public String getRok2011() {
		return rok2011;
	}

	public void setRok2011(String rok2011) {
		this.rok2011 = rok2011;
	}

	public String getRok2012() {
		return rok2012;
	}

	public void setRok2012(String rok2012) {
		this.rok2012 = rok2012;
	}

	public String getRok2013() {
		return rok2013;
	}

	public void setRok2013(String rok2013) {
		this.rok2013 = rok2013;
	}

	public String getRok2014() {
		return rok2014;
	}

	public void setRok2014(String rok2014) {
		this.rok2014 = rok2014;
	}

	public String getRok2015() {
		return rok2015;
	}

	public void setRok2015(String rok2015) {
		this.rok2015 = rok2015;
	}

	public String getRok2016() {
		return rok2016;
	}

	public void setRok2016(String rok2016) {
		this.rok2016 = rok2016;
	}

	public String getRok2017() {
		return rok2017;
	}

	public void setRok2017(String rok2017) {
		this.rok2017 = rok2017;
	}

	public BigDecimal getVymera() {
		return vymera;
	}

	public void setVymera(BigDecimal vymera) {
		this.vymera = vymera;
	}

	public List<ZiadostDiely> getListZiadostDiely() {
		return listZiadostDiely;
	}

	public void setListZiadostDiely(List<ZiadostDiely> listZiadostDiely) {
		this.listZiadostDiely = listZiadostDiely;
	}

}
