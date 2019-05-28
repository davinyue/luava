package org.linuxprobe.luava.proxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/** cglib动态代理 */
public abstract class AbstractMethodInterceptor<T> implements MethodInterceptor, HandlerInterceptor {
	/** 获取目标执行对象 */
	@Override
	public abstract T getHandler();

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		Object handler = this.getHandler();
		if (handler == null) {
			throw new IllegalArgumentException("handler can't be null");
		}
		JoinPoint joinPoint = new JoinPoint(obj, method);
		joinPoint.setArgs(args);
		if (this.preHandle(joinPoint)) {
			method.setAccessible(true);
			joinPoint.setResult(method.invoke(handler, args));
			this.afterCompletion(joinPoint);
			return joinPoint.getResult();
		} else {
			return joinPoint.getResult();
		}
	}

}
