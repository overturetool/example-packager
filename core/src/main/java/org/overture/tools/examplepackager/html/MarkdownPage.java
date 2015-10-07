package org.overture.tools.examplepackager.html;


public class MarkdownPage
{

	public static final String STYLE_CLASS_FAILD = "failed";
	public static final String STYLE_CLASS_OK = "ok";

	public static String markdown_header(String name, String layout)
	{
		return "---\nlayout: " + layout + "\ntitle: " + name + "\n---\n";
	}

	public static String makeH(Integer level, String text) // TODO: Here I have to change for the headers.
	{
		// return "\n<h" + level + ">" + text + "</h" + level + ">\n";
		if (level == 1)
		{
			return "# " + text + "\n";
		} else
		{
			String temp = "";
			for (int i = 0; i < level; i++)
			{
				temp += "#";
			}
			return temp + " " + text + "\n";
		}
	}

	public static String makeBr()
	{
		return "\n";
	}

	public static String makeLink(String text, String href, String preHref)
	{
		// return "<a href=\"" + preHref + href + "\">" + text + "</a>";
		return "[" + text + "]" + "(" + preHref + href + ")";
	}

	public static String makeLink(String text, String href)
	{
		// return "<a href=\"" + href + "\">" + text + "</a>";e
		return "[" + text + "]" + "(" + href + ")";
	}

	public static String makeP(String body)
	{
		// return "<p>" + body + "\n</p>";
		return body + "\n  \n";
	}

	public static String makeTable(String data)
	{
		// return "\n<table class=\"mytable\">" + data + "\n</table>\n";
		return "\n| | |\n|------|-------|\n" + data;
	}

	public static String makeRow(String data0, String data)// TODO: Change This
	{
		// return "\n\t<tr>" + data + "\n\t</tr>";
		return "|" + data0 + "|" + data + "|\n";
	}

	public static String makeBold(String data)
	{
		return "**" + data + "**";
	}

	public static String makeCodeBlock(String code)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("~~~\n");
		sb.append(code);
		sb.append("~~~\n");
		return sb.toString();
	}

	public static String disableLiquid(String content)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("{% raw %}\n");
		sb.append(content);
		sb.append("{% endraw %}\n");
		return sb.toString();
	}

}
