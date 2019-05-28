package org.linuxprobe.luava.proxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/** cglib动态代理 */
public abstract class AbstractMethodInterceptor implements MethodInterceptor {
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
	public abstract boolean preHandle(CglibJoinPoint joinPoint) throws Throwable;

	/**
	 * 执行后
	 * 
	 * @param joinPoint 执行信息持有对象, 包括代理对象, 执行方法, 执行参数, 目标函数执行结果, 可对结果进行修改,将返回本对象持有的结果
	 */
	public abstract void afterCompletion(CglibJoinPoint joinPoint) throws Throwable;

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		CglibJoinPoint joinPoint = new CglibJoinPoint(obj, method, proxy);
		joinPoint.setArgs(args);
		if (this.preHandle(joinPoint)) {
			Object result = proxy.invokeSuper(obj, joinPoint.getArgs());
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
