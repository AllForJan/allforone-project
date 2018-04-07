/**
 * 
 */
package com.vaadin.starter.beveragebuddy.backend.rpvs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.starter.beveragebuddy.backend.PriamaPlatba;

import lombok.Getter;
import lombok.Setter;

/**
 * @author martin
 *
 */
@Getter @Setter 
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
}
