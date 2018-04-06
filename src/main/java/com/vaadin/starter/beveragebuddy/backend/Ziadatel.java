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
@Getter @Setter @EqualsAndHashCode @ToString
public class Ziadatel implements Serializable {

	// URL;Ziadatel;ICO;Rok;Lokalita;Diel;Kultura;Vymera

	private Long id;
	private String ziadatel;
	private String ico;

	private List<BigDecimal> roky;

//	private BigDecimal rok2005;
//	private BigDecimal rok2006;
//	private BigDecimal rok2007;
//	private BigDecimal rok2008;
//	private BigDecimal rok2009;
//	private BigDecimal rok2010;
//	private BigDecimal rok2011;
//	private BigDecimal rok2012;
//	private BigDecimal rok2013;
//	private BigDecimal rok2014;
//	private BigDecimal rok2015;
//	private BigDecimal rok2016;
//	private BigDecimal rok2017;
//
//	private BigDecimal vymera;

	private List<ZiadostDiely> listZiadostDiely = new ArrayList<>();

	public BigDecimal getVymera(int rok){
		return roky.get(rok - 2000);
	}

	public void setVymera(int rok, BigDecimal vymera){
		roky.set(rok - 2000,vymera);
	}
}
