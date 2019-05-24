package org.linuxprobe.luava.http;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.linuxprobe.luava.json.JacksonUtils;

public class Qs {

	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	private String _stringify(Object obj, String prefix) {

		if (obj instanceof String || obj instanceof Integer || obj instanceof Boolean) {
			return prefix + "=" + obj;
		}
		if (obj instanceof Map) {
			for (Object key : ((Map) obj).keySet()) {
				return this._stringify(((Map) obj).get(key), prefix + "[" + key + "]");
			}
		} else if (obj instanceof List) {
			List list = (List) obj;
			List retStr = new ArrayList();
			for (int key = 0; key < list.size(); key++) {
				retStr.add(_stringify(((List) obj).get(key), prefix + "[" + key + "]"));
			}
			return join(retStr, "&");
		}
		return "";
	}

	@SuppressWarnings("rawtypes")
	public String stringify(Map<String, Object> obj) {

		List keys = new ArrayList();
		for (Object key : ((Map) obj).keySet()) {
			// values = values.concat(internals.stringify(obj[key], prefix + '['
			// + key + ']', generateArrayPrefix, strictNullHandling, filter));
			keys.add(this._stringify(((Map) obj).get(key), (String) key));
		}

		return join(keys, "&");
	}

	public static String join(Collection<?> values, String delimiter) {
		StringBuffer strbuf = new StringBuffer();
		boolean first = true;
		for (Object value : values) {
			if (!first) {
				strbuf.append(delimiter);
			} else {
				first = false;
			}
			strbuf.append(value.toString());
		}
		return strbuf.toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {
		Qs qs = new Qs();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("limit", 15);
		map.put("offset", 0);
		System.out.println(JacksonUtils.toJsonString(map));
		System.out.println(qs.stringify(map));
		System.out.println();

		map = new HashMap<String, Object>();
		map.put("a", "1");
		map.put("b", "2");
		System.out.println(JacksonUtils.toJsonString(map));
		System.out.println(qs.stringify(map));
		System.out.println();

		map = new HashMap<String, Object>();
		HashMap<String, Object> mapA = new HashMap<String, Object>();
		HashMap<String, Object> mapC = new HashMap<String, Object>();
		mapC.put("c", "d");
		mapA.put("b", mapC);
		map.put("a", mapA);
		System.out.println(JacksonUtils.toJsonString(map));
		System.out.println(qs.stringify(map));
		System.out.println();

		map = new HashMap<String, Object>();
		List listPopulate = new ArrayList();
		listPopulate.add("category");
		listPopulate.add("narrator");
		listPopulate.add("source");
		map.put("populate", listPopulate);
		HashMap mapWhere = new HashMap();
		mapWhere.put("category", "55de6957b0e835dcde17ad95");
		map.put("limit", 15);
		map.put("offset", 0);
		map.put("where", mapWhere);
		HashMap mapSort = new HashMap();
		mapSort.put("date", -1);
		map.put("sort", mapSort);

		System.out.println(JacksonUtils.toJsonString(map));
		System.out.println(qs.stringify(map));
		System.out.println();
	}

}