/**
 * 
 */
package com.vaadin.starter.beveragebuddy.backend.rpvs;

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
public class FirmaPlatby {

	private String ico;
	private String nazov;
	private List<PriamaPlatba> priamePlatby = new ArrayList<>();
	
}
