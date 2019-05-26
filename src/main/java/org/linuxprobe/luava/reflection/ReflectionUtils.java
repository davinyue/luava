package org.linuxprobe.luava.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ReflectionUtils {
	/**
	 * 获取该类型的所有属性，包括它的超类的属性
	 * 
	 * @param objClass 要查找的类类型
	 */
	public static List<Field> getAllFields(Class<?> objClass) {
		objClass = getRealCalssOfProxyClass(objClass);
		List<Field> fields = new LinkedList<>();
		while (objClass != Object.class) {
			fields.addAll(Arrays.asList(objClass.getDeclaredFields()));
			objClass = objClass.getSuperclass();
		}
		return fields;
	}

	/**
	 * 根据属性名称和类型查找属性
	 * 
	 * @param objClass  要查找的类类型
	 * @param fieldName 属性名称
	 * @param fieldType 属性类型
	 */
	public static Field getField(Class<?> objClass, String fieldName, Class<?> fieldType) {
		List<Field> fields = getAllFields(objClass);
		for (Field field : fields) {
			if (field.getName().equals(fieldName) && field.getType() == fieldType) {
				return field;
			}
		}
		return null;
	}

	/**
	 * 根据属性名称查找属性
	 * 
	 * @param objClass  要查找的类类型
	 * @param fieldName 属性名称
	 */
	public static Field getField(Class<?> objClass, String fieldName) {
		List<Field> fields = getAllFields(objClass);
		for (Field field : fields) {
			if (field.getName().equals(fieldName)) {
				return field;
			}
		}
		return null;
	}

	/** 获取属性的set方法 */
	public static Method getMethodOfFieldSet(Class<?> objClass, Field field) {
		if (objClass == null || field == null) {
			return null;
		}
		objClass = getRealCalssOfProxyClass(objClass);
		String fieldName = field.getName();
		String funSuffix = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		Method methodOfSet = null;
		try {
			methodOfSet = objClass.getMethod("set" + funSuffix, field.getType());
		} catch (NoSuchMethodException | SecurityException e) {
		}
		return methodOfSet;
	}

	/** 获取属性的get方法 */
	public static Method getMethodOfFieldGet(Class<?> objClass, Field field) {

		if (objClass == null || field == null) {
			return null;
		}
		objClass = getRealCalssOfProxyClass(objClass);
		String fieldName = field.getName();
		String prefix = "get";
		String funSuffix = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		if (boolean.class.isAssignableFrom(field.getType())) {
			if (fieldName.matches("^is[A-Z0-9_]+.*$")) {
				prefix = "";
				funSuffix = fieldName;
			} else {
				prefix = "is";
			}
		}
		Method methodOfGet = null;
		try {
			methodOfGet = objClass.getMethod(prefix + funSuffix);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new IllegalArgumentException(e);
		}
		return methodOfGet;
	}

	/** 设置属性值 */
	public static void setField(Object obj, Field field, Object value) {
		Class<?> objClass = getRealCalssOfProxyClass(obj.getClass());
		Method methodOfSet = getMethodOfFieldSet(objClass, field);
		if (methodOfSet != null) {
			try {
				methodOfSet.invoke(obj, value);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new IllegalArgumentException(e);
			}
		}
	}

	/** 获取属性值 */
	public static Object getFieldValue(Object obj, Field field) {
		Class<?> objClass = getRealCalssOfProxyClass(obj.getClass());
		Method getMethod = null;
		try {
			getMethod = getMethodOfFieldGet(objClass, field);
		} catch (Exception e) {
		}
		if (getMethod != null) {
			getMethod.setAccessible(true);
			try {
				Object value = getMethod.invoke(obj);
				return value;
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new IllegalArgumentException(e);
			}
		} else {
			field.setAccessible(true);
			try {
				return field.get(obj);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

	}

	/** 获取属性值 */
	public static Object getFieldValue(Object obj, String fieldName) {
		Class<?> objClass = getRealCalssOfProxyClass(obj.getClass());
		Field field = null;
		try {
			field = objClass.getDeclaredField(fieldName);
		} catch (NoSuchFieldException | SecurityException e1) {
			throw new RuntimeException(e1);
		}
		Method getMethod = getMethodOfFieldGet(objClass, field);
		getMethod.setAccessible(true);
		try {
			Object value = getMethod.invoke(obj);
			return value;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static String getFieldNameByMethod(Method method) {
		String fieldName = method.getName().substring(3, method.getName().length());
		fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1, fieldName.length());
		return fieldName;
	}

	public static Field getFieldByMethod(Class<?> type, Method method) {
		type = getRealCalssOfProxyClass(type);
		String fieldName = getFieldNameByMethod(method);
		return getDeclaredField(type, fieldName);
	}

	public static Class<?> getRealCalssOfProxyClass(Class<?> type) {
		while (type.getSimpleName().indexOf("$$EnhancerByCGLIB$$") != -1) {
			type = type.getSuperclass();
		}
		return type;
	}

	public static Field getDeclaredField(Class<?> type, String fieldName) {
		List<Field> fields = getAllFields(type);
		for (Field field : fields) {
			if (field.getName().equals(fieldName)) {
				return field;
			}
		}
		return null;
	}

	/** 获取类的父类泛型类型参数 */
	public static Class<?> getGenericSuperclass(Class<?> clazz, int order) {
		Type type = ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[order];
		Class<?> genericsCalss = null;
		try {
			genericsCalss = Class.forName(type.getTypeName());
			return genericsCalss;
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/** 获取类的父类泛型类型参数 */
	public static Type getGenericSuperType(Class<?> clazz, int order) {
		Type type = ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[order];
		return type;
	}

	/** 获取field的泛型类型参数,eg List<T>, 获取T的类型 */
	public static Class<?> getFiledGenericclass(Field field, int order) {
		ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
		if (parameterizedType == null) {
			throw new IllegalArgumentException("必须指定泛型类型");
		}
		Type type = parameterizedType.getActualTypeArguments()[0];
		Class<?> genericsCalss = null;
		try {
			genericsCalss = Class.forName(type.getTypeName());
			return genericsCalss;
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/** 判断一个类是否是代理类 */
	public static boolean isProxyClass(Class<?> clazz) {
		if (clazz.getSimpleName().indexOf("$$EnhancerByCGLIB$$") != -1) {
			return true;
		} else {
			return false;
		}
	}
}
