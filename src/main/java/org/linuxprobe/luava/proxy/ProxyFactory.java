package org.linuxprobe.luava.proxy;

import java.lang.reflect.Proxy;

import org.springframework.cglib.proxy.Enhancer;

public class ProxyFactory {
	/**
	 * 获取cglib代理对象取实例
	 * 
	 * @param type 代理对象的类型
	 */
	public static <T> T getProxyInstance(AbstractMethodInterceptor interceptor, Class<T> type) {
		/** 创建加强器，用来创建动态代理类 */
		Enhancer enhancer = new Enhancer();
		/** 为加强器指定要代理的业务类（即：为下面生成的代理类指定父类） */
		enhancer.setSuperclass(type);
		/** 设置回调：对于代理类上所有方法的调用，都会调用CallBack，而Callback则需要实现intercept()方法进行拦 */
		enhancer.setCallback(interceptor);
		@SuppressWarnings("unchecked")
		T instance = (T) enhancer.create();
		interceptor.setInstance(instance);
		/** 创建动态代理类对象并返回 */
		return instance;
	}

	/**
	 * 获取java动态代理对象取实例
	 * 
	 * @param interfaces 实例实现的接口
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getProxyInstance(AbstractInvocationHandler interceptor, Class<?>... interfaces) {
		return (T) Proxy.newProxyInstance(ProxyFactory.class.getClassLoader(), interfaces, interceptor);
	}
}
