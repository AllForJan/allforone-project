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
	
	public void initializeSumaVsetkychPlatieb() {
		BigDecimal val = new BigDecimal(0.0);
		for (FirmaPlatby firma: this.platbyFirmam) {
			firma.initializeSumaVsetkychPlatieb();
			val = val.add(firma.getSumaVsetkychPlatieb());
		}
		this.sumaVsetkychPlatieb = val;
	}
	
	public BigDecimal getSumaPlatieb(final int rok) {
		BigDecimal val = new BigDecimal("0.0");
		for (FirmaPlatby fp: this.platbyFirmam) {
			val = val.add(fp.getSumaPlatieb(rok));
		}
		return val;
	}
}
