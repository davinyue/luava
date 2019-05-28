package org.linuxprobe.luava.proxy;

import java.lang.reflect.Proxy;

import net.sf.cglib.proxy.Enhancer;

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

	/**
	 * 获取cglib动态代理对象
	 * 
	 * @param handler 方法调用的实际处理者, 代理对象的方法调用都会转发到这里
	 * @param type    代理对象类型
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getProxyObject(AbstractMethodInterceptor<T> handler, Class<T> type) {
		/** 创建加强器，用来创建动态代理类 */
		Enhancer enhancer = new Enhancer();
		/** 为加强器指定要代理的业务类（即：为下面生成的代理类指定父类） */
		enhancer.setSuperclass(type);
		/** 设置回调：对于代理类上所有方法的调用，都会调用CallBack，而Callback则需要实现intercept()方法进行拦 */
		enhancer.setCallback(handler);
		/** 创建动态代理类对象并返回 */
		return (T) enhancer.create();
	}
}
