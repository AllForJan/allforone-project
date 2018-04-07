/**
 * 
 */
package com.vaadin.starter.beveragebuddy.backend.rpvs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.starter.beveragebuddy.backend.PriamaPlatba;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author martin
 *
 */
@Getter @Setter @EqualsAndHashCode @ToString
public class PoberatelSumar {

	private PoberatelVyhod poberatel;
	private List<FirmaPlatby> platbyFirmam = new ArrayList<>();
	private BigDecimal sumaVsetkychPlatieb;
	private Map<Integer, BigDecimal> sumaPlatieb = new HashMap<>();
	
	public void initializeSumaVsetkychPlatieb() {
		BigDecimal val = new BigDecimal(0.0);
		for (FirmaPlatby firma: this.platbyFirmam) {
			for (PriamaPlatba platba: firma.getPriamePlatby()) {
				BigDecimal zaRok = this.sumaPlatieb.get(platba.getRok());
				if (zaRok == null) {
					zaRok = new BigDecimal("0.0");
				}
				this.sumaPlatieb.put(platba.getRok(), zaRok.add(platba.getSuma()));
				val = val.add(platba.getSuma());
			}
		}
		this.sumaVsetkychPlatieb = val;
	}
	
	public BigDecimal getSumaPlatieb(final int rok) {
		return this.sumaPlatieb.get(rok);
	}
}
