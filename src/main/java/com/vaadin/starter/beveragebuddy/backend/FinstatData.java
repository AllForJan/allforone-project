/**
 * 
 */
package com.vaadin.starter.beveragebuddy.backend;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author martin
 *
 */
@Getter @Setter @EqualsAndHashCode @ToString
public class FinstatData {

	private String ico;
	private String nazov;
	private String hlavnaCinnost;
	private String skNace;
	private Date datumVzniku;
	private Date datumZaniku;
	private String dlhy;
	private String zamestnanciStat;
	private int zamestnanciPocet;
	private String adresa;
	private String mesto;
	private String okres;
	private String kraj;
	
	private List<FinstatYearlyData> rocneData = new ArrayList<>();
}
