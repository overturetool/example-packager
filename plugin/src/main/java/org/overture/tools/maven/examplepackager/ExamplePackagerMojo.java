package org.overture.tools.maven.examplepackager;

import java.io.File;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.overture.tools.examplepackager.Dialect;
import org.overture.tools.examplepackager.PackerUtil;

/**
 * Example Packager
 */
@Mojo(name = "package-examples", defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
public class ExamplePackagerMojo extends AbstractMojo
{

	/**
	 * A boolean indicating whether example zips should be generated
	 */
	@Parameter(alias = "output-zip")
	protected boolean outputZipFiles = true;

	/**
	 * A boolean indicating whether web pages should be generated
	 */
	@Parameter(alias = "output-web")
	protected boolean outputWebFiles = false;

	/**
	 * A boolean indicating whether example webpages will be create in Markdown language.
	 */
	@Parameter(alias = "markdown")
	protected boolean outputMarkdownFiles = false;

	/**
	 * A list of directories containing subdirectories with example VDM-SL projects. Note that the name of the output
	 * bundle will be derived from the name of the base directory.
	 */
	@Parameter(alias = "slExamples")
	protected List<File> exampleSLBaseDirectories;

	/**
	 * A list of directories containing subdirectories with example VDM-PP projects. Note that the name of the output
	 * bundle will be derived from the name of the base directory.
	 */
	@Parameter(alias = "ppExamples")
	protected List<File> examplePPBaseDirectories;

	/**
	 * A list of directories containing subdirectories with example VDM-RT projects. Note that the name of the output
	 * bundle will be derived from the name of the base directory.
	 */
	@Parameter(alias = "rtExamples")
	protected List<File> exampleRTBaseDirectories;

	/**
	 * A prefix to the output zip filename.
	 */
	@Parameter(defaultValue = "Examples-")
	protected String outputPrefix;

	/**
	 * Name of the directory into which the packaged examples will be placed. Readonly at the moment as the only place
	 * they should be dropped is in the project's usual target directory.
	 */
	@Parameter(defaultValue = "${project.build.directory}")
	protected File outputDirectory;

	/**
	 * Name of the directory into which the markdown web pages would be generated.
	 */
	@Parameter(defaultValue = "${project.build.directory}")
	protected File markdownOutputDirectory;

	/**
	 * Location of the staging directory for the example packager.
	 */
	@Parameter(defaultValue = "${project.build.directory}/generated-resources/example-packager", readonly = true)
	protected File tmpdir;

	private boolean overtureCSSWeb = false;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException
	{
		
		PackerUtil packer = new PackerUtil(outputDirectory, outputZipFiles, outputWebFiles, outputMarkdownFiles, outputPrefix/*, markdownOutputDirectory*/, tmpdir, overtureCSSWeb);
		
		packer.pack(exampleSLBaseDirectories, Dialect.VDM_SL);
		packer.pack(examplePPBaseDirectories, Dialect.VDM_PP);
		packer.pack(exampleRTBaseDirectories, Dialect.VDM_RT);

		packer.createOverviewPages();
	}

}
