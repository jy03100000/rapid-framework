package cn.org.rapid_framework.generator.provider.db.model;

import java.lang.reflect.InvocationTargetException;

import junit.framework.TestCase;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;



public class TableTest extends TestCase{
	
	public void table() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Table t = new Table();
		t.setSqlName("user_info");
		System.out.println(BeanUtils.describe(t));
	}
}
