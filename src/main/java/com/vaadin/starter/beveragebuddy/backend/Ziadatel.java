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
}
