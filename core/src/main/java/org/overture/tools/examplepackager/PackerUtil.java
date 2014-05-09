package org.overture.tools.examplepackager;

import java.io.File;
import java.util.List;
import java.util.Vector;

public class PackerUtil
{
	protected final List<Controller> controllers = new Vector<Controller>();
	protected final List<File> zipFiles = new Vector<File>();

	/**
	 * A boolean indicating whether example zips should be generated
	 */
	final boolean outputZipFiles;

	/**
	 * A boolean indicating whether web pages should be generated
	 */
	final boolean outputWebFiles;

	/**
	 * A boolean indicating whether example webpages will be create in Markdown language.
	 */
	final boolean outputMarkdownFiles;

	/**
	 * A prefix to the output zip filename.
	 */
	final String outputPrefix;

	/**
	 * Name of the directory into which the packaged examples will be placed. Readonly at the moment as the only
	 * place they should be dropped is in the project's usual target directory.
	 */
	final File outputDirectory;

	///**
	// * Name of the directory into which the markdown web pages would be generated.
	// */
	//final File markdownOutputDirectory;

	/**
	 * Location of the staging directory for the example packager.
	 */
	final File tmpdir;

	final boolean overtureCSSWeb;

	public PackerUtil(File outputDirectory, boolean outputZipFiles,
			boolean outputWebFiles, boolean outputMarkdownFiles,
			String outputPrefix/*, File markdownOutputDirectory*/, File tmpdir,
			boolean overtureCSSWeb)
	{
		this.outputDirectory = outputDirectory;
		this.outputZipFiles = outputZipFiles;
		this.outputWebFiles = outputWebFiles;
		this.outputMarkdownFiles = outputMarkdownFiles;
		this.outputPrefix = outputPrefix;
		//this.markdownOutputDirectory = markdownOutputDirectory;
		this.tmpdir = tmpdir;
		this.overtureCSSWeb = overtureCSSWeb;
		
		
		this.outputDirectory.mkdirs();
		//.markdownOutputDirectory.mkdirs();
		this.tmpdir.mkdirs();
	}

	public void pack(List<File> exampleBaseDirectories, Dialect dialect)
	{
		File zipFile;
		Controller controller;

		for (File exampleDir : exampleBaseDirectories)
		{
			zipFile = new File(outputDirectory, outputPrefix
					+ exampleDir.getName() + ".zip");
			zipFiles.add(zipFile);
			controller = new Controller(dialect, exampleDir, outputDirectory, false);
			controllers.add(controller);
			controller.packExamples(new File(tmpdir, exampleDir.getName()), zipFile, !outputZipFiles);

			if (outputWebFiles)
			{
				controller.createWebSite(overtureCSSWeb);
			}

			if (outputMarkdownFiles)
			{
				controller.createMdSite();
			}
		}
	}

	public void createOverviewPages()
	{
		if (outputWebFiles && !controllers.isEmpty())
		{
			controllers.iterator().next().createWebOverviewPage(controllers, zipFiles, overtureCSSWeb);
		}
	}
}