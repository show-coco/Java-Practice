package com.sample;

public interface S {
	public void test();
	public static S create() {
		return new B();
	}
}
