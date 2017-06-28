package org.owen.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class UtilHelper {

	public static String getConfigProperty(String propertyName) {
		String propertyValue = "";

		Properties prop = new Properties();
		String propFileName = "config.properties";
		InputStream inputStream = UtilHelper.class.getClassLoader().getResourceAsStream(propFileName);
		if (inputStream != null) {
			try {
				prop.load(inputStream);
			} catch (IOException e) {
				org.apache.log4j.Logger.getLogger(UtilHelper.class).error("property file '" + propFileName + "' not found in classpath");
			}
			propertyValue = prop.getProperty(propertyName);
		} else {
			org.apache.log4j.Logger.getLogger(UtilHelper.class).error("property file '" + propFileName + "' not found in classpath");
		}

		try {
			inputStream.close();
		} catch (IOException e) {
			org.apache.log4j.Logger.getLogger(UtilHelper.class).error("Error while closing the inputStream");
		}
		return propertyValue;
	}

	public static int getIntValue(String val) {
		int iVal = 0;
		try {
			if (val != null) {
				iVal = Integer.parseInt(val);
			}
		} catch (NumberFormatException nfe) {
			org.apache.log4j.Logger.getLogger(UtilHelper.class).error("Error while getting integer value of " + val);
		}
		return iVal;
	}
}