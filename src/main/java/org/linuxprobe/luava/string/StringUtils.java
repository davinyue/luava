package org.linuxprobe.luava.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils extends org.apache.commons.lang3.StringUtils {
	private static Pattern humpPattern = Pattern.compile("[A-Z]");

	/**
	 * 驼峰转连接符
	 * 
	 * @param str      需要转换的字符串
	 * @param interval 连接符
	 */
	public static String humpToLine(String str, String interval) {
		Matcher matcher = humpPattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, interval + matcher.group(0).toLowerCase());
		}
		matcher.appendTail(sb);
		if (sb.indexOf(interval) == 0) {
			sb.delete(0, 1);
		}
		return sb.toString();
	}

	/**
	 * 驼峰转连接符
	 * 
	 * @param str 需要转换的字符串
	 * @see
	 * 
	 *      <pre>
	 *      studentName = student_name
	 *      </pre>
	 */
	public static String humpToLine(String str) {
		return humpToLine(str, "_");
	}

	/**
	 * 连接符转驼峰
	 * 
	 * @param str      需要转换的字符串
	 * @param interval 连接符
	 */
	public static String lineToHump(String str, String interval) {
		Pattern linePattern = Pattern.compile(interval + "(\\w)");
		str = str.toLowerCase();
		Matcher matcher = linePattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 连接符转驼峰
	 * 
	 * @param str 需要转换的字符串
	 * @see
	 * 
	 *      <pre>
	 *      student_name = studentName
	 *      </pre>
	 */
	public static String lineToHump(String str) {
		return lineToHump(str, "_");
	}

	/** 是否能转换为数字 */
	public static boolean isCanCastToNumber(String str) {
		if (isBlank(str)) {
			return false;
		} else {
			return str.matches("^([0-9]*\\.*[0-9]+)|(-*[0-9]+\\.*[0-9]*)|(\\+*[0-9]+\\.*[0-9]*)$");
		}
	}

	/** 是否能转换为Short */
	public static boolean isCanCastToShort(String str) {
		if (isBlank(str)) {
			return false;
		} else {
			if (str.matches("^\\+*[0-9]+$")) {
				if (str.compareTo(Short.MAX_VALUE + "") > 0) {
					return false;
				} else {
					return true;
				}
			}
			if (str.matches("^-+[0-9]+$")) {
				if (str.compareTo(Short.MIN_VALUE + "") > 0) {
					return false;
				} else {
					return true;
				}
			} else {
				return false;
			}
		}
	}

	/** 是否能转换为Integer */
	public static boolean isCanCastToInteger(String str) {
		if (isBlank(str)) {
			return false;
		} else {
			if (str.matches("^\\+*[0-9]+$")) {
				if (str.compareTo(Integer.MAX_VALUE + "") > 0) {
					return false;
				} else {
					return true;
				}
			}
			if (str.matches("^-+[0-9]+$")) {
				if (str.compareTo(Integer.MIN_VALUE + "") > 0) {
					return false;
				} else {
					return true;
				}
			} else {
				return false;
			}
		}
	}

	/** 是否能转换为Long */
	public static boolean isCanCastToLong(String str) {
		if (isBlank(str)) {
			return false;
		} else {
			if (str.matches("^\\+*[0-9]+$")) {
				if (str.compareTo(Long.MAX_VALUE + "") > 0) {
					return false;
				} else {
					return true;
				}
			}
			if (str.matches("^-+[0-9]+$")) {
				if (str.compareTo(Long.MIN_VALUE + "") > 0) {
					return false;
				} else {
					return true;
				}
			} else {
				return false;
			}
		}
	}
}
