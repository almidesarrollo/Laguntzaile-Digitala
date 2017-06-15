package com.asistencia.digital.monitor.geoutils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;

/**
* -----------------------------------------
* Regex implementation python style on java
* @author: Niko
* @date: 2017-02-28
* -----------------------------------------
*/

public class re {

	public static String search(String regex, String input) {
		return re.search(regex, input, 0);
	}

	public static String search(String regex, String input, int group) {
		Matcher m = Pattern.compile(regex).matcher(input);
		return m.find() ? m.group(group).trim() : null;
	}

	public static List<String> findall(String regex, String input, int group) {
		List<String> _matches = new ArrayList<String>();
		Matcher m = Pattern.compile(regex).matcher(input);
		while (m.find())
			_matches.add(m.group(group));
		return _matches;
	}

	public static List<String> findall(String regex, String input) {
		return re.findall(regex, input, 0);
	}

	public static String sub(String regex, String replace, String input) {
		return input.replaceAll(regex, replace);
	}

}