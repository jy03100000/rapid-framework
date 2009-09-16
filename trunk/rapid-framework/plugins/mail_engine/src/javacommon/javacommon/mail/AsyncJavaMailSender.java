package javacommon.mail;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.util.Assert;

import cn.org.rapid_framework.util.concurrent.async.AsyncToken;
import cn.org.rapid_framework.util.concurrent.async.AsyncTokenTemplate;

/**
 * 使用线程池异步发送邮件的javaMailSender
 * 每一个发送方法返回AsyncToken用于监听邮件是否发送成功
 * 
 * @see AsyncToken
 * @author badqiu
 *
 */
public class AsyncJavaMailSender implements InitializingBean,DisposableBean{
	protected static final Log log = LogFactory.getLog(AsyncJavaMailSender.class);
	
	protected int sendMailThreadPool = 0;
	protected ExecutorService executorService; //邮件发送的线程池
	protected JavaMailSender javaMailSender;
	protected boolean shutdownExecutorService = true;
	
	public void afterPropertiesSet() throws Exception {
		if(executorService == null && sendMailThreadPool > 0) {
			executorService = Executors.newFixedThreadPool(sendMailThreadPool,new CustomizableThreadFactory("MailEngine-"));
			log.info("create send mail executorService,threadPoolSize:"+sendMailThreadPool);
		}
		
		Assert.notNull(javaMailSender,"javaMailSender must be not null");
		Assert.notNull(executorService,"executorService must be not null");
	}
	
	public void destroy() throws Exception {
		if(shutdownExecutorService) {
			log.info("start shutdown send mail executorService");
			executorService.shutdown();
			log.info("send mail executorService shutdowned");
		}
	}
	
	public void setSendMailThreadPool(int sendMailThreadPool) {
		this.sendMailThreadPool = sendMailThreadPool;
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	public JavaMailSender getJavaMailSender() {
		return javaMailSender;
	}

	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public void setShutdownExecutorService(boolean shutdownExecutorService) {
		this.shutdownExecutorService = shutdownExecutorService;
	}

	public MimeMessage createMimeMessage() {
		return javaMailSender.createMimeMessage();
	}

	public MimeMessage createMimeMessage(InputStream contentStream) throws MailException {
		return javaMailSender.createMimeMessage(contentStream);
	}

	public AsyncToken send(final MimeMessage mimeMessage) throws MailException {
		final AsyncToken token = new AsyncToken();
		executorService.execute(new Runnable() {
			public void run() {
				AsyncTokenTemplate.execute(token, new Callable() {
					public Object call() throws Exception {
						javaMailSender.send(mimeMessage);
						return null;
					}
				});
			}
		});
		return token;
	}

	public AsyncToken send(final MimeMessage[] mimeMessages) throws MailException {
		final AsyncToken token = new AsyncToken();
		executorService.execute(new Runnable() {
			public void run() {
				AsyncTokenTemplate.execute(token, new Callable() {
					public Object call() throws Exception {
						javaMailSender.send(mimeMessages);
						return null;
					}
				});
			}
		});
		return token;
	}

	public AsyncToken send(final MimeMessagePreparator mimeMessagePreparator)
			throws MailException {		
		final AsyncToken token = new AsyncToken();
		executorService.execute(new Runnable() {
			public void run() {
				AsyncTokenTemplate.execute(token, new Callable() {
					public Object call() throws Exception {
						javaMailSender.send(mimeMessagePreparator);
						return null;
					}
				});
			}
		});
		return token;
	}

	public AsyncToken send(final MimeMessagePreparator[] mimeMessagePreparators)
			throws MailException {		
		final AsyncToken token = new AsyncToken();
		executorService.execute(new Runnable() {
			public void run() {
				AsyncTokenTemplate.execute(token, new Callable() {
					public Object call() throws Exception {
						javaMailSender.send(mimeMessagePreparators);
						return null;
					}
				});
			}
		});
		return token;
	}

	public AsyncToken send(final SimpleMailMessage simpleMessage) throws MailException {		
		final AsyncToken token = new AsyncToken();
		executorService.execute(new Runnable() {
			public void run() {
				AsyncTokenTemplate.execute(token, new Callable() {
					public Object call() throws Exception {
						javaMailSender.send(simpleMessage);
						return null;
					}
				});
			}
		});
		return token;
	}

	public AsyncToken send(final SimpleMailMessage[] simpleMessages) throws MailException {		
		final AsyncToken token = new AsyncToken();
		executorService.execute(new Runnable() {
			public void run() {
				AsyncTokenTemplate.execute(token, new Callable() {
					public Object call() throws Exception {
						javaMailSender.send(simpleMessages);
						return null;
					}
				});
			}
		});
		return token;
	}
	
}
