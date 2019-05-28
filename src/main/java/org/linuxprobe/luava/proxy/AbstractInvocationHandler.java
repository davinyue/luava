package org.linuxprobe.luava.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/** java 动态代理 */
public abstract class AbstractInvocationHandler implements InvocationHandler {
	/** 获取目标执行对象 */
	public abstract Object getHandler();

	/**
	 * 执行前
	 * 
	 * @param joinPoint 执行信息持有对象, 包括代理对象, 执行方法, 执行参数, 执行结果(null); 可对执行参数进行修改,
	 *                  本方法返回true时, 将使用本对象持有的参数调用目标对象的方法; 可对结果进行修改, 本方法返回false时,
	 *                  将返回本对象持有的结果; 可对invokeAfterCompletion(是否执行后继函数)进行修改,
	 *                  为false时,将不执行afterCompletion函数
	 * @return 返回true时, 继续后续执行目标函数和afterCompletion,返回false时, 不继续执行目标函数,将返回
	 *         joinPoint持有的结果
	 */
	public abstract boolean preHandle(JoinPoint joinPoint) throws Throwable;

	/**
	 * 执行后
	 * 
	 * @param joinPoint 执行信息持有对象, 包括代理对象, 执行方法, 执行参数, 目标函数执行结果, 可对结果进行修改,将返回本对象持有的结果
	 */
	public abstract void afterCompletion(JoinPoint joinPoint) throws Throwable;

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
			Object result = method.invoke(handler, joinPoint.getArgs());
			if (!joinPoint.isInvokeAfterCompletion()) {
				return result;
			}
			joinPoint.setResult(result);
			this.afterCompletion(joinPoint);
			return joinPoint.getResult();
		} else {
			return joinPoint.getResult();
		}
	}
}
