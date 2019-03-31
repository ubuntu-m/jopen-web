package io.jopen.web.core.module;

import java.util.concurrent.Callable;

/**
 * Create By  2018/10/21
 * Description:
 */

public class MyCallable2 implements Callable<Boolean> {


	@Override
	public Boolean call() throws Exception {
		Thread.sleep(5000);
		System.out.println(Thread.currentThread().getName());
		return Boolean.TRUE;
	}
}
