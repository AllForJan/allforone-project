/**
 * 
 */
package com.vaadin.starter.beveragebuddy.backend.rpvs;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author martin
 *
 */
@Getter @Setter @ToString(of= {"Name", "BirthDate"}) @EqualsAndHashCode(of= {"Name", "BirthDate"})
public class PoberatelVyhod {

	public String Name;
	public String BirthDate;
	public String Street;
	public String StreetNo;
	public String City;
	public String ZipCode;
	public String Type;
	public String ValidFrom;
	public String ValidTo;
}
