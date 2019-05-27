package org.linuxprobe.luava;

import org.linuxprobe.luava.proxy.AbstractInvocationHandler;
import org.linuxprobe.luava.proxy.JoinPoint;
import org.linuxprobe.luava.proxy.ProxyUtils;

public class RefLectionTest {

	public static void main(String[] args) {
		Name student = ProxyUtils.getProxyObject(new AbstractInvocationHandler() {

			@Override
			public Object getHandler() {
				return new Student();
			}

			@Override
			public boolean preHandle(JoinPoint joinPoint) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void afterCompletion(JoinPoint joinPoint) {
				// TODO Auto-generated method stub

			}
		}, Name.class);
		System.out.println(student.getName());
	}
}

interface Name {
	public String getName();
}

class Student implements Name {
	private String name = "张三";

	@Override
	public String getName() {
		return this.name;
	}
}
