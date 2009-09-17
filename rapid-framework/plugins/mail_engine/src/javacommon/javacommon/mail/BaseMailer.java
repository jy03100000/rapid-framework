package javacommon.mail;

import java.io.IOException;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.Assert;

import cn.org.rapid_framework.mail.AsyncJavaMailSender;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * mailer基类,用于其它mailer继承.
 * 
 * 现本mailer与freemarker模板,用于生成邮件内容
 * @author badqiu
 *
 */
public class BaseMailer implements InitializingBean{

	protected AsyncJavaMailSender asyncJavaMailSender;
	protected SimpleMailMessage simpleMailMessageTemplate;
	protected Configuration freemarkerConfiguration;
	protected String subjectPrefix ; //邮件前缀,子类可以使用 
	
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(asyncJavaMailSender,"asyncJavaMailSender must be not null");
		Assert.notNull(freemarkerConfiguration,"freemarkerConfiguration must be not null");
		Assert.notNull(simpleMailMessageTemplate,"simpleMailMessageTemplate must be not null");
		subjectPrefix = subjectPrefix == null ? "" : subjectPrefix;
	}
	
	public void setAsyncJavaMailSender(AsyncJavaMailSender asyncJavaMailSender) {
		this.asyncJavaMailSender = asyncJavaMailSender;
	}

	public void setSimpleMailMessageTemplate(SimpleMailMessage simpleMailMessage) {
		this.simpleMailMessageTemplate = simpleMailMessage;
	}

	public void setFreemarkerConfiguration(Configuration freemarkerConfiguration) {
		this.freemarkerConfiguration = freemarkerConfiguration;
	}
	
	public void setSubjectPrefix(String subjectPrefix) {
		this.subjectPrefix = subjectPrefix;
	}

	public String getSubjectPrefix() {
		return subjectPrefix;
	}
	
	/**
	 * 为subject 增加前缀
	 * @return
	 */
	public String wrapSubject(String subject) {
		return getSubjectPrefix()+subject;
	}

	protected SimpleMailMessage newSimpleMsgFromTemplate(String subject) {
		SimpleMailMessage msg = new SimpleMailMessage();
		simpleMailMessageTemplate.copyTo(msg);
		simpleMailMessageTemplate.setSubject(wrapSubject(subject));
		return msg;
	}

	protected String processWithTemplate(Object model, String templateName) throws TemplateException, IOException {
		Template template = freemarkerConfiguration.getTemplate(templateName);
		return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
	}

}
