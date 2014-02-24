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
package org.overture.tools.examplepackager.html;


public class HtmlPage
{
	//TODO: This is not the correct path for the Markdown site.
	public final static String overtureExamplesPreLink = "http://overture.sourceforge.net/examples/";
	
	public static String makePage(String body)
	{
		return "<html>"
				+ "\n<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\">"
				+ "</head>" + "\n<body>\n" + body + "\n</body>\n</html>";
	
	}

	public static String makeP(String body)
	{
		//return "<p>" + body + "\n</p>";
		return body +"\n  \n";
	}

	public static String makeLink(String text, String href)
	{
		//return "<a href=\"" + href + "\">" + text + "</a>";e
		return "[" + text + "]" + "(" + href + ")";
	}

	public static String makeLink(String text, String href, String preHref)
	{
		//return "<a href=\"" + preHref + href + "\">" + text + "</a>";
		return "[" + text + "]" + "(" + preHref + href + ")";
	}
 
//	public static String makeH1(String text)
//	{
//		return makeH(1, text);// return "\n<h1>" + text+ "</h1>\n";
//	}

	public static String makeBr()
	{
		return "\n";
	}

	public static String makeH(Integer level, String text) //TODO: Here I have to change for the headers.
	{
		//return "\n<h" + level + ">" + text + "</h" + level + ">\n";
		if (level == 1)
		{
			return "#" + text + "\n";
		}
		else
		{
			String temp = "";
			for(int i=0;i<level;i++)
			{
				temp += "#";
			}
			return temp + text + "\n";
		}
	}

	public static String makeDiv(String content, String className)
	{
		return "\n<div class=\"" + className + "\">" + content + "</div>\n";
	}
	
	public static String markdown_header(String name)
	{
		return "---\nlayout: default\ntitle: " + name + "\n---\n";
		
		
	}
}
