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

	/**
	 * 获取属性的set方法
	 * 
	 * @param objClass 要查找的类类型
	 * @param field    属性
	 */
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

	/**
	 * 获取属性的set方法
	 * 
	 * @param objClass  要查找的类类型
	 * @param fieldName 属性名称
	 */
	public static Method getMethodOfFieldSet(Class<?> objClass, String fieldName) {
		if (objClass == null || fieldName == null || fieldName.isEmpty()) {
			return null;
		} else {
			Field field = getField(objClass, fieldName);
			return getMethodOfFieldSet(objClass, field);
		}
	}

	/**
	 * 获取属性的get方法
	 * 
	 * @param objClass 要查找的类类型
	 * @param field    属性
	 */
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
		}
		return methodOfGet;
	}

	/**
	 * 获取属性的get方法
	 * 
	 * @param objClass  要查找的类类型
	 * @param fieldName 属性名称
	 */
	public static Method getMethodOfFieldGet(Class<?> objClass, String fieldName) {
		if (objClass == null || fieldName == null || fieldName.isEmpty()) {
			return null;
		} else {
			Field field = getField(objClass, fieldName);
			return getMethodOfFieldGet(objClass, field);
		}
	}

	/**
	 * 设置属性值
	 * 
	 * @param obj   要操作的对象
	 * @param field 要设置的属性
	 * @param value 要设置的值
	 */
	public static void setField(Object obj, Field field, Object value) {
		if (obj == null || field == null) {
			return;
		}
		Class<?> objClass = getRealCalssOfProxyClass(obj.getClass());
		Method methodOfSet = getMethodOfFieldSet(objClass, field);
		if (methodOfSet != null) {
			try {
				methodOfSet.invoke(obj, value);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new IllegalArgumentException(e);
			}
		} else {
			field.setAccessible(true);
			try {
				field.set(objClass, value);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new IllegalArgumentException(e);
			}
		}
	}

	/**
	 * 设置属性值
	 * 
	 * @param obj       要操作的对象
	 * @param fieldName 要设置的属性名称
	 * @param value     要设置的值
	 */
	public static void setField(Object obj, String fieldName, Object value) {
		if (obj == null || fieldName == null || fieldName.isEmpty()) {
			return;
		} else {
			Field field = getField(obj.getClass(), fieldName);
			if (field != null) {
				setField(obj, field, value);
			}
		}
	}

	/**
	 * 获取属性值
	 * 
	 * @param obj   要操作的对象
	 * @param field 要获取的属性
	 */
	public static Object getFieldValue(Object obj, Field field) {
		if (obj == null || field == null) {
			return null;
		}
		Class<?> objClass = getRealCalssOfProxyClass(obj.getClass());
		Method getMethod = null;
		try {
			getMethod = getMethodOfFieldGet(objClass, field);
		} catch (Exception e) {
		}
		Object value = null;
		if (getMethod != null) {
			getMethod.setAccessible(true);
			try {
				value = getMethod.invoke(obj);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new IllegalArgumentException(e);
			}
		} else {
			field.setAccessible(true);
			try {
				value = field.get(obj);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		return value;
	}

	/**
	 * 获取属性值
	 * 
	 * @param obj       要操作的对象
	 * @param fieldName 要获取的属性名称
	 */
	public static Object getFieldValue(Object obj, String fieldName) {
		if (obj == null || fieldName == null || fieldName.isEmpty()) {
			return null;
		}
		Field field = getField(obj.getClass(), fieldName);
		if (field == null) {
			return null;
		} else {
			return getFieldValue(obj, field);
		}
	}

	/**
	 * 根据set方法和get方法获取属性名称
	 * 
	 * @param method set或get方法
	 */
	public static String getFieldNameByMethod(Method method) {
		String fieldName = method.getName().substring(3, method.getName().length());
		fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1, fieldName.length());
		return fieldName;
	}

	/**
	 * 根据set方法和get方法获取属性名称
	 * 
	 * @param objClass 要查找的类类型
	 * @param method   set或get方法
	 */
	public static Field getFieldByMethod(Class<?> objClass, Method method) {
		objClass = getRealCalssOfProxyClass(objClass);
		String fieldName = getFieldNameByMethod(method);
		return getField(objClass, fieldName);
	}

	/**
	 * 获取代理类的真实类
	 * 
	 * @param objClass 要获取的类类型
	 */
	public static Class<?> getRealCalssOfProxyClass(Class<?> objClass) {
		while (objClass.getSimpleName().indexOf("CGLIB$") != -1) {
			objClass = objClass.getSuperclass();
		}
		return objClass;
	}

	/**
	 * 获取类的父类泛型类型参数
	 * 
	 * @param objClass 要获取的类类型
	 * @param order    获取第几个泛型参数
	 */
	public static Class<?> getGenericSuperclass(Class<?> objClass, int order) {
		Type type = ((ParameterizedType) objClass.getGenericSuperclass()).getActualTypeArguments()[order];
		Class<?> genericsCalss = null;
		try {
			genericsCalss = ReflectionUtils.class.getClassLoader().loadClass(type.getTypeName());
			return genericsCalss;
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 获取类的父类泛型类型参数
	 * 
	 * @param objClass 要获取的类类型
	 * @param order    获取第几个泛型参数
	 */
	public static Type getGenericSuperType(Class<?> objClass, int order) {
		Type type = ((ParameterizedType) objClass.getGenericSuperclass()).getActualTypeArguments()[order];
		return type;
	}

	/**
	 * 获取field的泛型类型参数,eg List&lt;T&gt;, 获取T的类型
	 * 
	 * @param field 要获取的field
	 * @param order 获取第几个泛型参数
	 */
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

	/**
	 * 判断一个类是否是代理类
	 * 
	 * @param objClass 要判断的类类型
	 */
	public static boolean isProxyClass(Class<?> objClass) {
		if (objClass.getSimpleName().indexOf("CGLIB$") != -1) {
			return true;
		} else {
			return false;
		}
	}
}
