import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import flex.messaging.io.ArrayList;



public class OgnlTest {

	
	@Test
	public void isBlank() {
		Assert.assertTrue(Ognl.isBlank(null));
		Assert.assertTrue(Ognl.isBlank(""));
		
		Assert.assertTrue(Ognl.isBlank(" "));
		Assert.assertTrue(Ognl.isBlank(" \n \t  "));
		Assert.assertFalse(Ognl.isBlank(" a "));
		
		Assert.assertFalse(Ognl.isBlank(new Object()));
		Assert.assertFalse(Ognl.isBlank(new Integer(1)));
		Assert.assertFalse(Ognl.isBlank("a"));
	}
	@Test
	public void isNotBlank() {
		Assert.assertFalse(Ognl.isNotBlank(null));
		Assert.assertFalse(Ognl.isNotBlank(""));
		
		Assert.assertFalse(Ognl.isNotBlank(" "));
		Assert.assertTrue(Ognl.isNotBlank(new Object()));
		Assert.assertTrue(Ognl.isNotBlank("a"));
	}
	
	@Test
	public void isEmpty() {
		Assert.assertTrue(Ognl.isEmpty(null));
		Assert.assertTrue(Ognl.isEmpty(""));
		
		Assert.assertFalse(Ognl.isEmpty(" "));
		Assert.assertFalse(Ognl.isEmpty(new Object()));
		Assert.assertFalse(Ognl.isEmpty("a"));
	}
	
	HashMap NOT_EMPTY_MAP = new HashMap();
	@Before
	public void setUp() {
		NOT_EMPTY_MAP.put("1", 1);
	}
	@Test
	public void testIsEmpty() {
		Assert.assertTrue(Ognl.isEmpty(""));
		Assert.assertTrue(Ognl.isEmpty(new ArrayList()));
		Assert.assertTrue(Ognl.isEmpty(new HashMap()));
		Assert.assertTrue(Ognl.isEmpty(new Object[]{}));
		
		Assert.assertTrue(!Ognl.isEmpty(" "));
		Assert.assertTrue(!Ognl.isEmpty(Arrays.asList(1,2,3)));
		Assert.assertTrue(!Ognl.isEmpty(NOT_EMPTY_MAP));
		Assert.assertTrue(!Ognl.isEmpty(new Object[]{1,2,3}));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testIsEmptyWithIeelgalArgument() {
		Assert.assertTrue(Ognl.isEmpty(new Object()));
	}
	
	@Test
	public void isNotEmpty() {
		Assert.assertFalse(Ognl.isNotEmpty(null));
		Assert.assertFalse(Ognl.isNotEmpty(""));
		
		Assert.assertTrue(Ognl.isNotEmpty(" "));
		Assert.assertTrue(Ognl.isNotEmpty(new Object()));
		Assert.assertTrue(Ognl.isNotEmpty("a"));
	}
	
	@Test
	public void isNumber() {
		Assert.assertFalse(Ognl.isNumber(""));
		Assert.assertFalse(Ognl.isNumber(null));
		Assert.assertFalse(Ognl.isNumber(" "));
		
		Assert.assertTrue(Ognl.isNumber("1.1"));
		Assert.assertTrue(Ognl.isNumber("1"));
		Assert.assertTrue(Ognl.isNumber("-1"));
		Assert.assertTrue(Ognl.isNumber(1));
		Assert.assertTrue(Ognl.isNumber(1.1));
		Assert.assertTrue(Ognl.isNumber(new Integer(1)));
	}
	
	@Test
	public void testPrintSystemProperties() throws FileNotFoundException {
		PrintStream out = new PrintStream(System.out);
		for(Map.Entry entry : System.getProperties().entrySet()) {
			out.println(entry.getKey()+":"+entry.getValue());
		}
	}
}
