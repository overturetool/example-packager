package org.overture.tools.examplepackager.util;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.overture.tools.examplepackager.Dialect;
import org.overture.tools.examplepackager.ProjectPacker;
import org.overture.tools.examplepackager.VdmReadme.ResultStatus;

public abstract class ExampleTestUtils
{

	private static final String SL_EXAMPLES_ROOT = "../../documentation/examples/VDMSL";
	private static final String PP_EXAMPLES_ROOT = "../../documentation/examples/VDM++";
	private static final String RT_EXAMPLES_ROOT = "../../documentation/examples/VDMSL";

	/**
	 * Get sources for all the Overture public examples. Currently, only examples that are expected to parse and TC are
	 * returned. In other words, the examples with purposeful errors are not checked at the moment. Further work is
	 * needed on these.
	 * 
	 * @return a list of {@link ExampleTestData} containing the example sources
	 */
	public static Collection<ExampleTestData> getCorrectExamplesSources()
	{
		List<ExampleTestData> r = new LinkedList<ExampleTestData>();

		r.addAll(getSubSources(SL_EXAMPLES_ROOT, Dialect.VDM_SL));
		r.addAll(getSubSources(PP_EXAMPLES_ROOT, Dialect.VDM_PP));
		r.addAll(getSubSources(RT_EXAMPLES_ROOT, Dialect.VDM_RT));

		return r;
	}

	private static Collection<ExampleTestData> getSubSources(
			String examplesRoot, Dialect dialect)
	{
		List<ExampleTestData> r = new LinkedList<ExampleTestData>();

		File dir = new File(examplesRoot);

		StringBuilder source = new StringBuilder();
		// grab examples groups
		for (File f : dir.listFiles())
		{
			// grab example projects
			if (f.isDirectory())
			{
				ProjectPacker p = new ProjectPacker(f, dialect, false);
				if (!p.getSettings().getExpectedResult().equals(ResultStatus.NO_CHECK))
				{
					for (File f2 : f.listFiles(dialect.getFilter()))
					{
						source.append(FileUtils.readFile(f2));
						source.append("\n\n");
					}
					if (p.getSettings().getLibs().size() > 0)
					{
						for (String lib : p.getSettings().getLibs())
						{
							try
							{
								source.append(FileUtils.readFile("/libs/"
										+ ProjectPacker.getName(dialect) + "/"
										+ lib));
							} catch (IOException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					r.add(new ExampleTestData(p.getSettings().getName(), dialect, p.getSettings().getLanguageVersion(), source.toString()));
					source = new StringBuilder();
				}
			}
		}

		return r;

	}
}
