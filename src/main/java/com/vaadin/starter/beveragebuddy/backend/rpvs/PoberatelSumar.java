/**
 * 
 */
package com.vaadin.starter.beveragebuddy.backend.rpvs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
	
	public BigDecimal getSumaVsetkychPlatieb() {
		BigDecimal val = new BigDecimal(0.0);
		for (FirmaPlatby firma: this.platbyFirmam) {
			for (PriamaPlatba platba: firma.getPriamePlatby()) {
				val = val.add(platba.getSuma());
			}
		}
		return val;
	}
	
	public BigDecimal getSumaPlatieb(final int rok) {
		BigDecimal val = new BigDecimal(0.0);
		for (FirmaPlatby firma: this.platbyFirmam) {
			for (PriamaPlatba platba: firma.getPriamePlatby()) {
				if (platba.getRok() == rok) {
					val = val.add(platba.getSuma());
				}
			}
		}
		return val;
	}
}
