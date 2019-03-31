package io.jopen.web.core.module;

/**
 * Create By  2018/10/21
 * Description:
 */


import java.util.concurrent.Callable;

public class FictionalEmailSender implements Callable<Boolean>{
	private String to;
	private String subject;
	private String body;
	public FictionalEmailSender(String to, String subject, String body){
		this.to = to;
		this.subject = subject;
		this.body = body;
	}

	@Override
	public Boolean call() throws InterruptedException {
		// 在0~0.5秒间模拟发送邮件
		Thread.sleep(Math.round(Math.random()*0.5*1000));
		// 假设我们有80%的几率成功发送邮件
		if(Math.random()>0.2){
			return true;
		}else{
			return false;
		}
	}

}

