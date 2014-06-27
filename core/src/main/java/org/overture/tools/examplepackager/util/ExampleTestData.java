package org.overture.tools.examplepackager.util;

import org.overture.tools.examplepackager.Dialect;

public class ExampleTestData {

	String name;
	Dialect dialect;
	String source;

	public ExampleTestData(String name, Dialect dialect, String source) {
		this.name = name;
		this.dialect = dialect;
		this.source = source;
	}

	public String getName() {
		return name;
	}

	public Dialect getDialect() {
		return dialect;
	}

	public String getSource() {
		return source;
	}

}
