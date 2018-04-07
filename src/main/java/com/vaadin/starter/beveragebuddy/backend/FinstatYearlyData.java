/**
 * 
 */
package com.vaadin.starter.beveragebuddy.backend;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author martin
 *
 */
@Getter @Setter @EqualsAndHashCode @ToString
public class FinstatYearlyData {

	private int rok;
	private double trzby;
	private double vynosy;
	private double zisk;
	private double aktiva;
	private String zamestnanciStat;
}
