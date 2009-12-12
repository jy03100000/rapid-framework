package cn.org.rapid_framework.freemarker.template;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateScalarModel;

/**
 * @author badqiu
 */
public class OverrideDirective implements TemplateDirectiveModel {
	
	public String getName() {
		return "override";
	}
	
	public void execute(Environment env,
            Map params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) throws TemplateException, IOException {
		String name = DirectiveUtils.getRequiredParam(params, "name", env);
		String overrideVariableName = DirectiveUtils.getOverrideVariableName(name);
		if(env.getVariable(overrideVariableName) == null) {
			if(body == null) {
				env.setVariable(overrideVariableName,TemplateScalarModel.EMPTY_STRING);
			}else {
				StringWriter out = new StringWriter(512);
				body.render(out);
				env.setVariable(overrideVariableName,new SimpleScalar(out.toString()));
			}
		}
	}

}
