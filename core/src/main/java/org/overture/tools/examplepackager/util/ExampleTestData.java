package org.overture.tools.examplepackager.util;

import java.io.Serializable;

import org.overture.tools.examplepackager.Dialect;
import org.overture.tools.examplepackager.Release;

/**
 * Simple class for storing the essential test data for a single overture example.<br>
 * <br>
 * The class holds the name and content (the VDM model encoded in a {@link String}) as well as {@link Release} and
 * {@link Dialect} information of the example.
 * 
 * @author ldc
 */
public class ExampleTestData implements Serializable
{

	private static final long serialVersionUID = 1L;

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
