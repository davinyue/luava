package org.linuxprobe.luava.proxy;

import java.lang.reflect.Method;

/** 执行信息持有对象 */
public class JoinPoint {
	public JoinPoint(Object proxy, Method method) {
		this.proxy = proxy;
		this.method = method;
	}

	/** 结果 */
	private Object result;
	/** 参数 */
	private Object[] args;
	/** 代理对象 */
	private Object proxy;
	/** 执行方法 */
	private Method method;

	/** 获取结果 */
	public Object getResult() {
		return result;
	}

	/** 设置结果 */
	public void setResult(Object result) {
		this.result = result;
	}

	/** 获取参数 */
	public Object[] getArgs() {
		return args;
	}

	/** 设置参数 */
	public void setArgs(Object[] args) {
		this.args = args;
	}

	/** 获取代理对象 */
	public Object getProxy() {
		return proxy;
	}

	/** 获取执行方法 */
	public Method getMethod() {
		return method;
	}
}
