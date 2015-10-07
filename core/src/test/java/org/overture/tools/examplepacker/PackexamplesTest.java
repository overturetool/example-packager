package org.overture.tools.examplepacker;

import java.io.File;

import junit.framework.TestCase;

import org.overture.tools.examplepackager.Main;

public class PackexamplesTest extends TestCase
{
	File input = new File(FilePathUtil.getPlatformPath("src/test/resources/examples"));
	File outputFolder = new File(FilePathUtil.getPlatformPath("target/testData/"));
	File libFolder = new File(FilePathUtil.getPlatformPath("src/test/resources/libs"));

	public void testPackexamples() throws Exception
	{
		Main.main(new String[] { "-i", input.getAbsolutePath(), "-o",
				new File(outputFolder, "packed-examples").getAbsolutePath(),
				"-l", libFolder.getAbsolutePath(), "-z" });
	}

	public void testCreateWebpages() throws Exception
	{
		Main.main(new String[] { "-i", input.getAbsolutePath(), "-o",
				new File(outputFolder, "plain-web").getAbsolutePath(), "-l",
				libFolder.getAbsolutePath(), "-w" });
	}

	public void testCreateWebpagesOverture() throws Exception
	{
		Main.main(new String[] { "-i", input.getAbsolutePath(), "-o",
				new File(outputFolder, "overture-web").getAbsolutePath(), "-l",
				libFolder.getAbsolutePath(), "-w", "--overture-css" });
	}
}
