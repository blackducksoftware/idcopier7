package com.blackducksoftware.soleng.idcopier.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.blackducksoftware.soleng.idcopier.constants.IDCPathConstants;
import com.blackducksoftware.soleng.idcopier.constants.IDCViewConstants;
import com.blackducksoftware.soleng.idcopier.constants.IDCViewModelConstants;
import com.blackducksoftware.soleng.idcopier.model.IDCLog;
import com.google.gson.Gson;

/**
 * 
 * @author nmadison
 * 
 */
@RestController
@SessionAttributes(IDCViewModelConstants.IDC_SESSION)
public class IDCLoggerController
{
    static Logger log = Logger.getLogger(IDCLoginController.class);

    @RequestMapping(value = IDCPathConstants.LOG_PATH)
    public ModelAndView displayLogPage()
    {
	return new ModelAndView(IDCViewConstants.LOG_FORM);
    }

    private Elements getElementsByTag(Element element, String tag)
    {
	return element.getElementsByTag(tag);
    }

    @RequestMapping(value = IDCPathConstants.LOG_DATA_PATH)
    public String getLogContents()
    {
	List<IDCLog> logArray = new ArrayList<IDCLog>();

	FileAppender fileAppender;
	Enumeration e = Logger.getRootLogger().getAllAppenders();
	while (e.hasMoreElements())
	{
	    Appender app = (Appender) e.nextElement();
	    if (app instanceof FileAppender)
	    {

		if (((FileAppender) app).getFile().endsWith(".html"))
		{
		    fileAppender = ((FileAppender) app);
		    String logPath = fileAppender.getFile();

		    log.info("Found web log file [" + logPath + "]");

		    try
		    {
			File input = new File(logPath);
			Document doc = Jsoup.parse(input, "UTF-8");

			Elements bodyElements = doc.getElementsByTag("body");

			for (Element body : bodyElements)
			{
			    Elements tables = getElementsByTag(body, "table");

			    for (Element table : tables)
			    {
				Elements tableRowElements = getElementsByTag(
					table, "tr");
				for (Element tableRow : tableRowElements)
				{
				    Elements tableDataElements = getElementsByTag(
					    tableRow, "td");

				    IDCLog logItem = new IDCLog();
				    for (int a = 0; a < tableDataElements
					    .size(); a++)
				    {
					// if (a == 0)
					// logItem.setTime(tableDataElements.get(a).text());
					// if (a == 1)
					// logItem.setThread(tableDataElements.get(a).text());
					if (a == 2)
					    logItem.setLevel(tableDataElements
						    .get(a).text());
					if (a == 3)
					    logItem.setCategory(tableDataElements
						    .get(a).text());
					if (a == 4)
					    logItem.setMessage(tableDataElements
						    .get(a).text());
				    }

				    // Make sure that nothing empty goes in here
				    if (logItem.getLevel() != null)
				    {
					// Moves the newest log item to the top
					// of the log page
					logArray.add(0, logItem);
					// logArray.add(logItem);
				    }
				}
			    }
			}
		    } catch (Exception e1)
		    {
			e1.printStackTrace();
		    }
		}
	    }
	}

	return new Gson().toJson(logArray);
    }
}