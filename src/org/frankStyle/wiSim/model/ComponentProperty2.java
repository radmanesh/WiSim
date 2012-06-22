package org.frankStyle.wiSim.model;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * @author Arman
 * 
 */
public class ComponentProperty2 {
	private String strName;
	private String strValue;

	private LinkedList listArgs;

	public ComponentProperty2(String name, String value) {
		strName = name;
		strValue = value;
		listArgs = new LinkedList();

	}

	public ComponentProperty2(ComponentProperty2 property) {
		this.strName = new String(property.getName());
		this.strValue = new String(property.getValue());
		listArgs = new LinkedList();

		ListIterator iter = property.argsIterator();
		while (iter.hasNext()) {
			String arg = (String) iter.next();
			listArgs.add(new String(arg));
		}
	}

	public String getName() {
		return strName;
	}

	public void setName(String strName) {
		this.strName = strName;
	}

	public String getValue() {
		return strValue;
	}

	public void setValue(String strValue) {
		this.strValue = strValue;
	}

	public ListIterator argsIterator() {
		return listArgs.listIterator();
	}

	public void addArg(String arg) {
		listArgs.add(arg);
	}

	public void removeArg(String arg) {
		listArgs.remove(arg);
	}

	public String getArg(int index) {
		if (index > listArgs.size())
			return null;
		return (String) listArgs.get(index);
	}

	public int argSize() {
		return listArgs.size();
	}

}
