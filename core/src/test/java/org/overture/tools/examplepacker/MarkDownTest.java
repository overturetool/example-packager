package org.overture.tools.examplepacker;

import java.io.File;
import java.util.List;
import java.util.Vector;

import org.junit.Test;
import org.overture.tools.examplepackager.Dialect;
import org.overture.tools.examplepackager.PackerUtil;

public class MarkDownTest
{

	@Test
	public void createMarkdown()
	{
		PackerUtil packer = new PackerUtil( F("target/generated/markdown/"), true, false, true, "", F("target/generated-resources/example-packager"), false);
		
		packer.pack(L("src/test/resources/examples/VDMSL"), Dialect.VDM_SL,F("src/test/resources/libs"));
		packer.createOverviewPages();
	}

	private List<File> L(String string)
	{
		List<File> files = new Vector<File>();
		files.add(F(string));
		return files;
	}

	public static File F(String path)
	{
		return new File(path.replace('/', File.separatorChar));
	}
}
