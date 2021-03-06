package com.vaadin.starter.beveragebuddy.backend;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a beverage category.
 */
@Getter @Setter @EqualsAndHashCode @ToString
public class ZiadostDiely implements Serializable {

	// URL;Ziadatel;ICO;Rok;Lokalita;Diel;Kultura;Vymera

	private Long id;
	private String ziadatel;
	private String ico;
	private int rok;
	private String lokalita;
	private String diel;
	private String kultura;
	private BigDecimal vymera;
}
