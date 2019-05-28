package org.linuxprobe.luava;

import org.junit.Test;
import org.linuxprobe.luava.proxy.AbstractInvocationHandler;
import org.linuxprobe.luava.proxy.AbstractMethodInterceptor;
import org.linuxprobe.luava.proxy.CglibJoinPoint;
import org.linuxprobe.luava.proxy.JoinPoint;
import org.linuxprobe.luava.proxy.ProxyFactory;

public class ProxyTest {
	@Test
	public void dynamicProxy() {
		AbstractInvocationHandler abstractInvocationHandler = new AbstractInvocationHandler() {
			@Override
			public Object getHandler() {
				return new Student();
			}

			@Override
			public boolean preHandle(JoinPoint joinPoint) {
				joinPoint.setResult("李四");
				return false;
			}

			@Override
			public void afterCompletion(JoinPoint joinPoint) {
			}
		};
		People student = ProxyFactory.getProxyInstance(abstractInvocationHandler, People.class);
		System.out.println(student.getName());
	}

	@Test
	public void cglibProxy() {
		try {
			AbstractMethodInterceptor abstractMethodInterceptor = new AbstractMethodInterceptor() {

				@Override
				public boolean preHandle(CglibJoinPoint joinPoint) throws Exception {
					return false;
				}

				@Override
				public void afterCompletion(CglibJoinPoint joinPoint) throws Exception {
				}
			};
			Student student = ProxyFactory.getProxyInstance(abstractMethodInterceptor, Student.class);
			System.out.println(student.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

interface People {
	public String getName();
}

class Student implements People {
	private String name = "张三";

	@Override
	public String getName() {
		return this.name;
	}
}