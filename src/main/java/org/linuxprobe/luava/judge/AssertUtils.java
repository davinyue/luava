package org.linuxprobe.luava.judge;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/** 断言工具 */
public class AssertUtils {
	/**
	 * object不能是null,否则抛出异常
	 * 
	 * @param object       被断言数据
	 * @param errorMessage 错误信息
	 * @exception AssertException
	 */
	public static void isNotNull(Object object, String errorMessage) {
		if (object == null) {
			throw new IllegalArgumentException(errorMessage);
		}
	}

	/**
	 * object必须是null,否则抛出异常
	 * 
	 * @param object       被断言数据
	 * @param errorMessage 错误信息
	 * @exception AssertException
	 */
	public static void isNull(Object object, String errorMessage) {
		if (object != null) {
			throw new IllegalArgumentException(errorMessage);
		}
	}

	/**
	 * object不能是空字符串,否则抛出异常
	 * 
	 * @param object       被断言数据
	 * @param errorMessage 错误信息
	 * @see
	 * 
	 *      <pre>
	 * null      = false
	 * ""        = false
	 * " "       = false
	 * "bob"     = true
	 * "  bob  " = true
	 *      </pre>
	 * 
	 * @exception AssertException
	 * 
	 */
	public static void isNotBlank(CharSequence object, String errorMessage) {
		if (StringUtils.isBlank(object)) {
			throw new IllegalArgumentException(errorMessage);
		}
	}

	/**
	 * object必须是空字符串,否则抛出异常
	 * 
	 * @param object       被断言数据
	 * @param errorMessage 错误信息
	 * @see
	 * 
	 *      <pre>
	 * null      = true
	 * ""        = true
	 * " "       = true
	 * "bob"     = false
	 * "  bob  " = false
	 *      </pre>
	 * 
	 * @exception AssertException
	 * 
	 */
	public static void isBlank(CharSequence object, String errorMessage) {
		if (!StringUtils.isBlank(object)) {
			throw new IllegalArgumentException(errorMessage);
		}
	}

	/**
	 * object不能是空字符串,否则抛出异常
	 * 
	 * @param object       被断言数据
	 * @param errorMessage 错误信息
	 * @see
	 * 
	 *      <pre>
	 * null      = false
	 * ""        = false
	 * " "       = true
	 * "bob"     = true
	 * "  bob  " = true
	 *      </pre>
	 * 
	 * @exception AssertException
	 * 
	 */
	public static void isNotEmpty(CharSequence object, String errorMessage) {
		if (StringUtils.isEmpty(object)) {
			throw new IllegalArgumentException(errorMessage);
		}
	}

	/**
	 * object必须是空字符串,否则抛出异常
	 * 
	 * @param object       被断言数据
	 * @param errorMessage 错误信息
	 * @see
	 * 
	 *      <pre>
	 * null      = true
	 * ""        = true
	 * " "       = false
	 * "bob"     = false
	 * "  bob  " = false
	 *      </pre>
	 * 
	 * @exception AssertException
	 * 
	 */
	public static void isEmpty(CharSequence object, String errorMessage) {
		if (!StringUtils.isEmpty(object)) {
			throw new IllegalArgumentException(errorMessage);
		}
	}

	public static void isNotEmpty(Collection<?> object, String errorMessage) {
		if (object == null || object.isEmpty()) {
			throw new IllegalArgumentException(errorMessage);
		}
	}

	public static void isEmpty(Collection<?> object, String errorMessage) {
		if (object != null && !object.isEmpty()) {
			throw new IllegalArgumentException(errorMessage);
		}
	}

	public static void isNotEmpty(Map<?, ?> object, String errorMessage) {
		if (object == null || object.isEmpty()) {
			throw new IllegalArgumentException(errorMessage);
		}
	}

	public static void isEmpty(Map<?, ?> object, String errorMessage) {
		if (object != null && !object.isEmpty()) {
			throw new IllegalArgumentException(errorMessage);
		}
	}

	/**
	 * object不能是false,否则抛出异常
	 * 
	 * @param object       被断言数据
	 * @param errorMessage 错误信息
	 * @exception AssertException
	 * 
	 */
	public static void isTrue(boolean object, String errorMessage) {
		if (!object) {
			throw new IllegalArgumentException(errorMessage);
		}
	}

	/**
	 * object不能是true,否则抛出异常
	 * 
	 * @param object       被断言数据
	 * @param errorMessage 错误信息
	 * @exception AssertException
	 * 
	 */
	public static void isFalse(boolean object, String errorMessage) {
		if (object) {
			throw new IllegalArgumentException(errorMessage);
		}
	}
}
