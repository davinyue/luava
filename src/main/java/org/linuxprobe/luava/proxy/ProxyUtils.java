package org.linuxprobe.luava.proxy;

import java.lang.reflect.Proxy;

public class ProxyUtils {
	/**
	 * 获取java动态代理对象
	 * 
	 * @param handler    方法调用的实际处理者, 代理对象的方法调用都会转发到这里
	 * @param interfaces 代理对象需要实现的接口, 可以同时指定多个接口
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getProxyObject(AbstractInvocationHandler handler, Class<?>... interfaces) {
		return (T) Proxy.newProxyInstance(ProxyUtils.class.getClassLoader(), interfaces, handler);
	}
}
