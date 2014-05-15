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
package org.overture.tools.examplepackager.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileUtils
{

	public static List<String> readTextFromJar(String s)
	{
		InputStream is = null;
		BufferedReader br = null;
		String line;
		List<String> list = new ArrayList<String>();

		try
		{
			is = FileUtils.class.getResourceAsStream(s);
			br = new BufferedReader(new InputStreamReader(is));
			while (null != (line = br.readLine()))
			{
				list.add(line);
			}
		} catch (Exception e)
		{
			System.err.println("Failed to read file from jar: \"" + s + "\"");
			e.printStackTrace();
		} finally
		{
			try
			{
				if (br != null)
				{
					br.close();
				}
				if (is != null)
				{
					is.close();
				}
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return list;
	}

	public static String readFile(String relativePath) throws IOException
	{
		StringBuilder sb = new StringBuilder();
		for (String s : FileUtils.readTextFromJar(relativePath))
		{
			sb.append("\n" + s);
		}
		return sb.toString();
	}

	public static boolean detectLatexEnv(InputStream input, String env)
	{
		Scanner s = new Scanner(input);
		try
		{

			// latex \begin{cml}
			// regex \\begin\{cml\}
			// java \\\\begin\\{cml\\}
			if (s.findWithinHorizon("\\\\begin\\{" + env + "\\}", 0) != null)
			{
				return true;
			}

		} finally
		{
			s.close();
		}
		return false;
	}

	public static InputStream chooseStream(File file, String env,
			InputStream inStream) throws FileNotFoundException
	{
		InputStream in = new FileInputStream(file);
		if (detectLatexEnv(in, env))
		{
			return new LatexCmlEnvInputStream(new FileInputStream(file), env);
		}

		if (inStream == null)
		{
			return new FileInputStream(file);
		}
		return inStream;

	}

	public static String readFile(File file)
	{
		StringBuilder sb = new StringBuilder();
		try
		{
			InputStream streamIn = chooseStream(file, "vdm_al",null);
			streamIn = chooseStream(file, "vdmsl",streamIn);
			streamIn = chooseStream(file, "vdmpp",streamIn);
			streamIn = chooseStream(file, "vdmrt",streamIn);
			//
			// streamIn =new LatexCmlEnvInputStream(streamIn,"vdm_al");

			int c;
			while ((c = streamIn.read()) != -1)
			{
				sb.append((char) c);
			}
			streamIn.close();

		} catch (FileNotFoundException e)
		{
			System.err.println("File read failed: " + e);
		} catch (IOException e)
		{
			System.err.println("File read failed: " + e);
		}
		return sb.toString();
	}

	// public static void writeFile(File outputFolder, String fileName, String content)
	// throws IOException
	// {
	// FileWriter outputFileReader = new FileWriter(new File(outputFolder,
	// fileName), false);
	// BufferedWriter outputStream = new BufferedWriter(outputFileReader);
	// outputStream.write(content);
	// outputStream.close();
	//
	// }
	//
	// public static void writeFile(File file, String content) throws IOException
	// {
	// FileWriter outputFileWriter = new FileWriter(file);
	// BufferedWriter outputStream = new BufferedWriter(outputFileWriter);
	// outputStream.write(content);
	// outputStream.close();
	// outputFileWriter.close();
	// }

	public static void writeFile(String data, File file)
	{
		writeFile(data, file, false);
	}

	public static void writeFile(String data, File file, boolean append)
	{
		BufferedWriter outputStream = null;
		try
		{
			// FileWriter outputFileReader = new FileWriter(file, append);
			// FileOutputStream output = new
			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file, append), "UTF-8");
			// outputStream = new BufferedWriter(outputFileReader);

			out.write(data);

			out.flush();
			out.close();

		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			if (outputStream != null)
			{
				try
				{
					outputStream.close();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
}
