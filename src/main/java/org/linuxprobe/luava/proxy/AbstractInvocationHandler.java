package org.linuxprobe.luava.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/** java 动态代理 */
public abstract class AbstractInvocationHandler implements InvocationHandler, HandlerInterceptor {
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object handler = this.getHandler();
		if (handler == null) {
			throw new IllegalArgumentException("handler can't be null");
		}
		JoinPoint joinPoint = new JoinPoint(proxy, method);
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
