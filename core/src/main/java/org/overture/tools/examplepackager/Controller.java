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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
			text += " ";
		text += "|";

		System.out.println(text);
		System.out.println("|                                                                              |");
	}

	public static void printSubHeading(String text)
	{
		System.out.println("--------------------------------------------------------------------------------");
		text = "| " + text;
		while (text.length() < 79)
			text += " ";
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
			printSubHeading("PACKING: " + inputRootFolder.getName());
		for (File exampleFolder : inputRootFolder.listFiles())
		{
			if (!exampleFolder.isDirectory()
					|| exampleFolder.getName().equals(".svn"))
				continue;

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
				zipName.delete();

			FolderZipper.zipFolder(outputFolder.getAbsolutePath(), zipName.getAbsolutePath());
			if (verbose)
				printSubHeading("Folder zipped: ".toUpperCase()
						+ zipName.getName());
		}

	}

	public static void delete(File tmpFolder)
	{
		try
		{
			if (tmpFolder != null && tmpFolder.isFile() && tmpFolder.exists())
				tmpFolder.delete();
			else if (tmpFolder.exists())
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
			System.err.println("\nFaild to deleting - file not closed: "
					+ tmpFolder);
	}

	public Integer count = 0;
	public Integer synErrors = 0;
	public Integer typeErrors = 0;
	public Integer poCount = 0;
	public Integer interpretationErrors = 0;

	public void createWebSite(boolean overtureCSSWeb)
	{
		int i = 0;
		webDir.mkdirs();
		printSubHeading("Producing website".toUpperCase());

		File logOutput = new File(webDir, inputRootFolder.getName());
		logOutput.mkdirs();

		//String outputFolderName = dialect.toString().replaceAll("_", "");
		File logOuputFiles = new File(logOutput, /*outputFolderName*/"markdown");
		logOuputFiles.mkdirs();

		StringBuilder sb = new StringBuilder();
		StringBuilder md = new StringBuilder();
		
		md.append(MarkdownPage.markdown_header(inputRootFolder.getName(), inputRootFolder.getName()));
		
		Collections.sort(projects);
		for (ProjectPacker p : projects)
		{
			
			String name = p.getSettings().getName().substring(0, p.getSettings().getName().length() - 2);
			name = name.substring(0, 1).toUpperCase() + name.substring(1);
			System.out.println("Creating web entry for: " + name);
			
			//Markdown Page build
			md.append(MarkdownPage.makeBr());
			md.append(MarkdownPage.makeH(3, name));
			
			String rowsmarkdown = MarkdownPage.makeRow("Project Name:", name);
			rowsmarkdown += MarkdownPage.makeRow("Author:", p.getSettings().getTexAuthor());			
			rowsmarkdown += MarkdownPage.makeRow("Language Version:", p.getSettings().getLanguageVersion().toString());		
			String project_content = p.getSettings().getContent().replaceAll("\n","");			
			rowsmarkdown += MarkdownPage.makeRow("Description:", project_content);
			
			//html Page build
			sb.append(HtmlPage.makeH(3, name));
			System.out.print(" table...");
			String rows = tableRow("Project Name:", name);
			rows += tableRow("Author:", p.getSettings().getTexAuthor());
			// rows += tableRow("Dialect:", p.getSettings().getDialect().toString());
			rows += tableRow("Language Version:", p.getSettings().getLanguageVersion().toString());
			rows += tableRow("Description:", p.getSettings().getContent());

			String pdfLink = "";
			
			//Making each project in to separate folder.
			
			File folder = new File(logOutput,p.getSettings().getName());
			folder.mkdir();

			System.out.print(" zip...");
			File zipFile = new File(/*logOuputFiles*/folder, name + ".zip");
			p.zipTo(zipFile);

			rows += tableRow("Download:", HtmlPage.makeLink("model", /*outputFolderName*/p.getSettings().getName()
					+ "/" + zipFile.getName())
					+ " " + pdfLink);
			
			rowsmarkdown += MarkdownPage.makeRow("Download:", HtmlPage.makeLink("model", /*outputFolderName*/"../" + p.getSettings().getName()
					+ "/" + zipFile.getName())
					+ " " + pdfLink);
			

			md.append(MarkdownPage.makeTable(rowsmarkdown));
			sb.append(HtmlTable.makeTable(rows));
			System.out.print("\n");
			
			//Creating the overall file.
			overallMarkdownFile(i, folder);
			i++;
		
			
		}

		String markdownpage = md.toString();
		
		String page = HtmlPage.makePage(HtmlPage.makeH1(inputRootFolder.getName()
				+ ": Examples")
				+ sb.toString());
		if (!overtureCSSWeb)
		{
			//Html page
			FileUtils.writeFile(page, new File(logOutput, "index.html"));
			//Markdown page
			FileUtils.writeFile(markdownpage, new File(logOuputFiles,"index.md"));
		} else
		{
			FileUtils.writeFile(HtmlPage.makeStyleCss(), new File(logOutput, "style.css"));

			// overturetool
			String pageSection = HtmlPage.makeOvertureStyleCss()
					+ "\n"
					+ HtmlPage.makeDiv(sb.toString().replaceAll("href=\"", "href=\""
							+ HtmlPage.overtureExamplesPreLink), "examples");
			FileUtils.writeFile(pageSection, new File(logOutput, "index.html"));
			
			//markdown page.
			FileUtils.writeFile(markdownpage, new File(logOuputFiles,"index.md"));
		}

	}
	
	public List<String> listFilesForFolder(final File folder) {
		List<String> file = new ArrayList<String>();
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else {
	           // System.out.println(fileEntry.getName());
	        	file.add(fileEntry.getName()); 
	        }
	    }
		
	    return file;	
	}
	
	public void overallMarkdownFile(int i, File folder)
	{
		//First attempt to create the overall document for each project
		
		//File prj_files = new File(inputRootFolder.getName(),"/"+ p.getSettings().getName()+"/");
		StringBuilder sumString = new StringBuilder();
		//sumString.append(prj_files.length());
		
		sumString.append(MarkdownPage.makeH(2, inputRootFolder.getName()));
		//sumString.append(inputRootFolder.isDirectory());
		sumString.append(MarkdownPage.makeBr());
		File folders = new File(inputRootFolder,"");
		
		if(folders.isDirectory())
		{
			
			File[] subfolders = folders.listFiles();
			Arrays.sort(subfolders);
			
			//sumString.append(Arrays.sort(subfolders));
			for(File x:subfolders)
			{
				sumString.append(x + "\n");
			}
			
			if (i < subfolders.length)
			{
				sumString.append(MarkdownPage.makeH(2,subfolders[i].toString()));
				sumString.append(MarkdownPage.makeBr());
				File filelister = new File(subfolders[i],"");
				List<String> files = listFilesForFolder(filelister);
				for(String context:files)
				{
					sumString.append(MarkdownPage.makeBr());
					sumString.append(context);
				}
				
				
			}
			//List<String> files = listFilesForFolder(filelister);
			
			//Collections.sort(files);
			
			
			//sumString.append(MarkdownPage.makeBr());
			//
			//for(File file:subfolders)
			//{
//				File filelister = new File(file,"");
//				List<String> files = listFilesForFolder(filelister);
//				//sumString.append(files);
//				for(String context:files)
//				{
				//sumString.append(MarkdownPage.makeBr());
				//sumString.append(file);
//					File filecontext = new File(file,"/"+context);
//					sumString.append(MarkdownPage.makeBr());
//					sumString.append(filecontext);
//					if (filecontext.exists()){
//					Scanner scanner = new Scanner(filecontext);
//					
//					sumString.append(scanner.next());
//					sumString.append(MarkdownPage.makeBr());
//					scanner.close();
//						try {
//							FileInputStream fis = new FileInputStream(filecontext);
//							while (fis.read() != -1){
//								//String c = fis.toString();
//								//sumString.append(c);
//							}
//							fis.close();
//						} catch (FileNotFoundException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
						
			//		}
				}
//				sumString.append(file.toString());
				sumString.append(MarkdownPage.makeBr());
				FileUtils.writeFile(sumString.toString(), new File(folder,"overall.md"));
				
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
			md.append(MarkdownPage.makeLink("--root--", "../"+controller.getName()));
			//md.append(MarkdownPage.makeBr());
			sb.append(HtmlPage.makeBr());
			md.append(MarkdownPage.makeBr());
			sb.append(HtmlPage.makeLink("--web--", controller.getName()
					+ "/index.html"));
			md.append(HtmlPage.makeLink("--web--", "../"+controller.getName()
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
			md.append(MarkdownPage.makeLink(file.getName(), "../"+file.getName()));
			md.append(MarkdownPage.makeBr());
			sb.append(HtmlPage.makeBr());
		}

		String page = HtmlPage.makePage(sb.toString());
		
		String markdownpage = md.toString();
		if (overtureCSSWeb)
		{
			// overturetool - hteml page
						page= HtmlPage.makeOvertureStyleCss()
								+ "\n"
								+ HtmlPage.makeDiv(sb.toString()/*.replaceAll("href=\"", "href=\""
										+ HtmlPage.overtureExamplesPreLink)*/, "examples");
			
			
			FileUtils.writeFile(HtmlPage.makeStyleCss(), new File(webDir, "style.css"));
			
			
		}else
		{
			
		}
		//Html
		FileUtils.writeFile(page, new File(webDir, "index.html"));
		//markdown
		if (!overtureCSSWeb)
		{
			File mdfolder = new File(webDir,"markdown");
			mdfolder.mkdirs();
			FileUtils.writeFile(markdownpage,new File(mdfolder, "index.md"));
		}
		//

	}

}
