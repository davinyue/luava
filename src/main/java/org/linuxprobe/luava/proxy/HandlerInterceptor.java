package org.linuxprobe.luava.proxy;

/** 执行前后拦截器 */
public interface HandlerInterceptor {
	/**
	 * 执行前
	 * 
	 * @param method     执行的方法
	 * @param args       传入参数
	 * @param resultHold 结果持有对象
	 * @return 返回true时, 继续后续执行,返回false时,将返回 resultHold持有的结果
	 */
	public abstract boolean preHandle(JoinPoint joinPoint);

	/**
	 * 执行后
	 * 
	 * @param method     执行的方法
	 * @param args       传入参数
	 * @param resultHold 目标对象的方法执行后的结果持有对象
	 * @return 返回true时, 继续后续执行,返回false时,将返回 resultHold持有的结果
	 */
	public abstract void afterCompletion(JoinPoint joinPoint);
}
