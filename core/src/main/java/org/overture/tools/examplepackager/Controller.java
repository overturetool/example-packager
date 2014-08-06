/*******************************************************************************
 * Copyright (c) 2009, 2011 Overture Team and others.
 *
 * Overture is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Overture is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Overture.  If not, see <http://www.gnu.org/licenses/>.
 * 	
 * The Overture Tool web-site: http://overturetool.org/
 *******************************************************************************/
package org.overture.tools.examplepackager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.overture.tools.examplepackager.html.HtmlPage;
import org.overture.tools.examplepackager.html.HtmlTable;
import org.overture.tools.examplepackager.html.MarkdownPage;
import org.overture.tools.examplepackager.util.FileUtils;
import org.overture.tools.examplepackager.util.FolderZipper;

public class Controller
{
	List<ProjectPacker> projects = new Vector<ProjectPacker>();
	Dialect dialect;
	File inputRootFolder;
	boolean verbose = true;
	public final File webDir;// = new File("Web");
	public File markdownDir;

	public Controller(Dialect dialect, File inputRootFolder, File output,
			File markdownOutput)
	{
		this(dialect, inputRootFolder, output);
		this.markdownDir = new File(markdownOutput, "markdown");
	}

	public Controller(Dialect dialect, File inputRootFolder, File output)
	{
		this.dialect = dialect;
		this.inputRootFolder = inputRootFolder;
		this.webDir = new File(output, "Web");

	}

	public Controller(Dialect dialect, File inputRootFolder, File output,
			boolean verbose)
	{
		this(dialect, inputRootFolder, output);
		this.verbose = verbose;
	}

	public String getName()
	{
		return inputRootFolder.getName();
	}

	public static void printHeading(String text)
	{
		System.out.println("\n================================================================================");
		System.out.println("|                                                                              |");
		text = "| " + text;
		while (text.length() < 79)
		{
			text += " ";
		}
		text += "|";

		System.out.println(text);
		System.out.println("|                                                                              |");
	}

	public static void printSubHeading(String text)
	{
		System.out.println("--------------------------------------------------------------------------------");
		text = "| " + text;
		while (text.length() < 79)
		{
			text += " ";
		}
		text += "|";

		System.out.println(text);
		System.out.println("|                                                                              |");
	}

	public void packExamples(File outputFolder, File zipName, boolean dryrun)
	{
		if (!dryrun)
		{
			outputFolder.mkdirs();
		}

		if (verbose)
		{
			printSubHeading("PACKING: " + inputRootFolder.getName());
		}
		for (File exampleFolder : inputRootFolder.listFiles())
		{
			if (!exampleFolder.isDirectory()
					|| exampleFolder.getName().equals(".svn"))
			{
				continue;
			}

			ProjectPacker p = new ProjectPacker(exampleFolder, dialect, verbose);
			if (!dryrun)
			{
				p.packTo(outputFolder);
			}
			projects.add(p);
		}
		if (!dryrun)
		{
			if (zipName.exists())
			{
				zipName.delete();
			}

			FolderZipper.zipFolder(outputFolder.getAbsolutePath(), zipName.getAbsolutePath());
			if (verbose)
			{
				printSubHeading("Folder zipped: ".toUpperCase()
						+ zipName.getName());
			}
		}

	}

	public static void delete(File tmpFolder)
	{
		try
		{
			if (tmpFolder != null && tmpFolder.isFile() && tmpFolder.exists())
			{
				tmpFolder.delete();
			} else if (tmpFolder.exists())
			{
				for (File file : tmpFolder.listFiles())
				{
					delete(file);
				}
				tmpFolder.delete();
			}
		} catch (Exception e)
		{
			System.err.println("\nFaild to deleting: " + tmpFolder);
		}
		if (tmpFolder.exists())
		{
			System.err.println("\nFaild to deleting - file not closed: "
					+ tmpFolder);
		}
	}

	public Integer count = 0;
	public Integer synErrors = 0;
	public Integer typeErrors = 0;
	public Integer poCount = 0;
	public Integer interpretationErrors = 0;

	public void createWebSite(boolean overtureCSSWeb)
	{
		webDir.mkdirs();
		// markdownDir.mkdirs();
		printSubHeading("Producing website".toUpperCase());

		File logOutput = new File(webDir, inputRootFolder.getName());
		logOutput.mkdirs();

		// File logOuputFiles = new File(logOutput, /* outputFolderName */"markdown");
		// logOuputFiles.mkdirs();

		StringBuilder sb = new StringBuilder();
		StringBuilder md = new StringBuilder();

		md.append(MarkdownPage.markdown_header(inputRootFolder.getName(), inputRootFolder.getName()));

		Collections.sort(projects);
		for (ProjectPacker p : projects)
		{

			String name = p.getSettings().getName().substring(0, p.getSettings().getName().length() - 2);
			name = name.substring(0, 1).toUpperCase() + name.substring(1);
			System.out.println("Creating web entry for: " + name);

			// Markdown Page build
			md.append(MarkdownPage.makeBr());
			md.append(MarkdownPage.makeH(3, name));

			String rowsmarkdown = "";// MarkdownPage.makeRow("Project Name:", name);
			rowsmarkdown += MarkdownPage.makeRow("Author:", p.getSettings().getTexAuthor());
			rowsmarkdown += MarkdownPage.makeRow("Language Version:", p.getSettings().getLanguageVersion().toString());
			String project_content = p.getSettings().getContent().replaceAll("\n", "");
			rowsmarkdown += MarkdownPage.makeRow("Description:", project_content);

			// html Page build
			sb.append(HtmlPage.makeH(3, name));
			System.out.print(" table...");
			String rows = tableRow("Project Name:", name);
			rows += tableRow("Author:", p.getSettings().getTexAuthor());
			// rows += tableRow("Dialect:", p.getSettings().getDialect().toString());
			rows += tableRow("Language Version:", p.getSettings().getLanguageVersion().toString());
			rows += tableRow("Description:", p.getSettings().getContent());

			String pdfLink = "";

			// Making each project in to separate folder.

			File folder = new File(logOutput, p.getSettings().getName());
			folder.mkdir();

			System.out.print(" zip...");
			File zipFile = new File(/* logOuputFiles */folder, name + ".zip");
			p.zipTo(zipFile);

			rows += tableRow("Download:", HtmlPage.makeLink("model", /* outputFolderName */p.getSettings().getName()
					+ "/" + zipFile.getName())
					+ " " + pdfLink);

			rowsmarkdown += MarkdownPage.makeRow("Download:", HtmlPage.makeLink("model", /* outputFolderName */"../"
					+ p.getSettings().getName() + "/" + zipFile.getName())
					+ " " + pdfLink);

			md.append(MarkdownPage.makeTable(rowsmarkdown));
			sb.append(HtmlTable.makeTable(rows));
			System.out.print("\n");

			// Creating the overall file.
			overallMarkdownFile(p, folder);

		}

		// String markdownpage = md.toString();

		String page = HtmlPage.makePage(HtmlPage.makeH1(inputRootFolder.getName()
				+ ": Examples")
				+ sb.toString());
		if (!overtureCSSWeb)
		{
			// Html page
			FileUtils.writeFile(page, new File(logOutput, "index.html"));
			// Markdown page
			// FileUtils.writeFile(markdownpage, new File(logOuputFiles, "index.md"));
		} else
		{
			FileUtils.writeFile(HtmlPage.makeStyleCss(), new File(logOutput, "style.css"));

			// overturetool
			String pageSection = HtmlPage.makeOvertureStyleCss()
					+ "\n"
					+ HtmlPage.makeDiv(sb.toString().replaceAll("href=\"", "href=\""
							+ HtmlPage.overtureExamplesPreLink), "examples");
			FileUtils.writeFile(pageSection, new File(logOutput, "index.html"));

			// markdown page.
			// FileUtils.writeFile(markdownpage, new File(logOuputFiles, "index.md"));
		}

	}

	public void createMdSite()
	{
		webDir.mkdirs();
		// markdownDir.mkdirs();
		printSubHeading("Producing MD site".toUpperCase());

		File logOutput = new File(webDir, inputRootFolder.getName());
		logOutput.mkdirs();

		StringBuilder md = new StringBuilder();

		md.append(MarkdownPage.markdown_header(inputRootFolder.getName(), "default"));

		Collections.sort(projects);
		for (ProjectPacker p : projects)
		{
			String name = p.getSettings().getName();
			int suffixLength = p.getName(p.getSettings().getDialect()).length();
			name = name.substring(0, p.getSettings().getName().length() - suffixLength);
			name = name.substring(0, 1).toUpperCase() + name.substring(1);
			System.out.println("Creating markdown page for: " + name);

			// Markdown Page build
			md.append(MarkdownPage.makeBr());
			md.append(MarkdownPage.makeH(3, name));
			md.append(p.getSettings().getContent().trim());

			String rowsmarkdown = "";
			rowsmarkdown += MarkdownPage.makeRow("Author:", p.getSettings().getTexAuthor());
			if (p.getSettings().getDialect() != Dialect.CML) {
				rowsmarkdown += MarkdownPage.makeRow("Version:", p.getDialect().name()
					+ " - " + p.getSettings().getLanguageVersion().toString());
			}

			// html Page build
			System.out.print(" table...");

			String pdfLink = "";

			// Making each project in to separate folder.

			File folder = new File(logOutput, p.getSettings().getName());
			folder.mkdir();

			System.out.print(" zip...");
			File zipFile = new File(/* logOuputFiles */folder, name + ".zip");
			p.zipTo(zipFile);

			final String relativeProjectUrl = p.getSettings().getName() + "/";
			rowsmarkdown += MarkdownPage.makeRow("Details...",MarkdownPage.makeLink("model (zip)", /* outputFolderName */
					relativeProjectUrl + zipFile.getName())
					+ " "
					+ pdfLink
					+ " / "
					+ MarkdownPage.makeLink("show specification", relativeProjectUrl
							+ "index.html"));

			md.append(MarkdownPage.makeBr());
			md.append(MarkdownPage.makeBr());
			md.append(MarkdownPage.makeTable(rowsmarkdown));
			md.append(MarkdownPage.makeBr());
			System.out.print("\n");

			// Creating the overall file.
			overallMarkdownFile(p, folder);

		}

		String markdownpage = md.toString();

		// markdown page.
		FileUtils.writeFile(markdownpage, new File(logOutput, "index.md"));

	}

	public List<File> listFilesForFolder(final File folder)
	{
		List<File> file = new ArrayList<File>();
		for (final File fileEntry : folder.listFiles())
		{
			if (fileEntry.isDirectory())
			{
				listFilesForFolder(fileEntry);
			} else
			{
				// System.out.println(fileEntry.getName());
				file.add(fileEntry);
			}
		}

		return file;
	}

	public void overallMarkdownFile(ProjectPacker p, File folder)
	{
		StringBuilder sumString = new StringBuilder();
		File folders = new File(inputRootFolder, "");

		if (folders.isDirectory())
		{
			sumString.append(MarkdownPage.markdown_header(p.getSettings().getName(), "default"));
			sumString.append(MarkdownPage.makeBr());

			sumString.append(MarkdownPage.makeH(2, p.getSettings().getName()));
			
			sumString.append("Author: "
					+ (p.getSettings().getTexAuthor() == null ? "Overture"
							: p.getSettings().getTexAuthor()) + "\n\n");

			sumString.append(p.getSettings().getContent());

			/* Turn off all properties for now for CML
			 * files, though we will eventually want to
			 * list the entry points. -jwc/06Aug2014
			 */
			if (p.getSettings().getDialect() != Dialect.CML) {                        
				sumString.append("\n\n| Properties | Values          |\n| :------------ | :---------- |\n");
				sumString.append("|Language Version:| "
					+ p.getSettings().getLanguageVersion() + "|\n");
				if (p.getSettings().getEntryPoints().size() > 0)
				{
					for (String entrypoint : p.getSettings().getEntryPoints())
					{
						sumString.append("|Entry point     :| " + entrypoint+"|\n");
					}
				}
			}

			sumString.append(MarkdownPage.makeBr());

			for (File specFile : p.getSpecFiles(p.getRoot()))
			{
				sumString.append(MarkdownPage.makeBr());
				sumString.append(MarkdownPage.makeH(3, specFile.getName()));
				sumString.append(MarkdownPage.makeBr());
				sumString.append(MarkdownPage.disableLiquid(MarkdownPage.makeCodeBlock(FileUtils.readFile(specFile))));
			}

			sumString.append(MarkdownPage.makeBr());
			FileUtils.writeFile(sumString.toString(), new File(folder, "index.md"));
		}
	}

	private String tableRow(String... cells)
	{
		List<String> c = Arrays.asList(cells);
		return HtmlTable.makeRow(HtmlTable.makeCell(cells[0], "first")
				+ HtmlTable.makeCells(c.subList(1, c.size())));
	}

	public void createWebOverviewPage(List<Controller> controllers,
			List<File> zipFiles, boolean overtureCSSWeb)
	{
		webDir.mkdirs();
		printSubHeading("Producing main website".toUpperCase());

		StringBuilder sb = new StringBuilder();
		StringBuilder md = new StringBuilder();

		md.append(MarkdownPage.markdown_header("Overture Examples", "default"));
		md.append(MarkdownPage.makeH(1, "Overture Examples"));

		sb.append(HtmlPage.makeH1("Overture Examples"));

		for (Controller controller : controllers)
		{
			md.append(MarkdownPage.makeBr());
			sb.append(HtmlPage.makeH(2, controller.getName()));
			md.append(MarkdownPage.makeH(2, controller.getName()));
			md.append(MarkdownPage.makeBr());
			sb.append(HtmlPage.makeLink("--root--", controller.getName()));
			md.append(MarkdownPage.makeLink("--root--", "../"
					+ controller.getName()));
			// md.append(MarkdownPage.makeBr());
			sb.append(HtmlPage.makeBr());
			md.append(MarkdownPage.makeBr());
			sb.append(HtmlPage.makeLink("--web--", controller.getName()
					+ "/index.html"));
			md.append(HtmlPage.makeLink("--web--", "../" + controller.getName()
					+ "/index.html"));
		}

		sb.append(HtmlPage.makeBr());
		sb.append(HtmlPage.makeBr());

		md.append(MarkdownPage.makeBr());
		md.append(MarkdownPage.makeBr());

		sb.append(HtmlPage.makeH(2, "Download example collections"));

		md.append(MarkdownPage.makeH(2, "Download example collections"));
		for (File file : zipFiles)
		{
			md.append(MarkdownPage.makeBr());
			sb.append(HtmlPage.makeLink(file.getName(), file.getName()));
			md.append(MarkdownPage.makeLink(file.getName(), "../"
					+ file.getName()));
			md.append(MarkdownPage.makeBr());
			sb.append(HtmlPage.makeBr());
		}

		String page = HtmlPage.makePage(sb.toString());

		String markdownpage = md.toString();
		if (overtureCSSWeb)
		{
			// overturetool - hteml page
			page = HtmlPage.makeOvertureStyleCss() + "\n"
					+ HtmlPage.makeDiv(sb.toString()/*
													 * .replaceAll("href=\"", "href=\"" +
													 * HtmlPage.overtureExamplesPreLink)
													 */, "examples");

			FileUtils.writeFile(HtmlPage.makeStyleCss(), new File(webDir, "style.css"));

		} else
		{

		}
		// Html
		FileUtils.writeFile(page, new File(webDir, "index.html"));
		// markdown
		if (!overtureCSSWeb)
		{
			File mdfolder = new File(webDir, "markdown");
			mdfolder.mkdirs();
			FileUtils.writeFile(markdownpage, new File(mdfolder, "index.md"));
		}
		//

	}

}
