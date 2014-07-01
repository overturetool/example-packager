package org.overture.tools.examplepackager.util;

import org.overture.tools.examplepackager.Dialect;
import org.overture.tools.examplepackager.Release;

public class ExampleTestData
{

	String name;
	Dialect dialect;
	Release release;
	String source;

	public ExampleTestData(String name, Dialect dialect, Release release,
			String source)
	{
		this.name = name;
		this.dialect = dialect;
		this.source = source;
		this.release = release;
	}

	public Release getRelease()
	{
		return release;
	}

	public String getName()
	{
		return name;
	}

	public Dialect getDialect()
	{
		return dialect;
	}

	public String getSource()
	{
		return source;
	}

}
