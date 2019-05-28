package org.linuxprobe.luava.proxy;

/** 执行前后拦截器 */
public interface HandlerInterceptor {
	/** 获取目标执行对象 */
	public abstract Object getHandler();

	/**
	 * 执行前
	 * 
	 * @param joinPoint 执行信息持有对象, 包括代理对象, 执行方法, 执行参数, 执行结果(null); 可对执行参数进行修改,
	 *                  本方法返回true时, 将使用本对象持有的参数调用目标对象的方法; 可对结果进行修改, 本方法返回false时,
	 *                  将返回本对象持有的结果
	 * @return 返回true时, 继续后续执行目标函数和afterCompletion,返回false时, 不继续执行目标函数,将返回
	 *         joinPoint持有的结果
	 */
	default public boolean preHandle(JoinPoint joinPoint) {
		return true;
	};

	/**
	 * 执行后
	 * 
	 * @param joinPoint 执行信息持有对象, 包括代理对象, 执行方法, 执行参数, 目标函数执行结果, 可对结果进行修改,将返回本对象持有的结果
	 */
	default public void afterCompletion(JoinPoint joinPoint) {
		return;
	};
}
