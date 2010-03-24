package cn.org.rapid_framework.util;

import java.util.Collection;
import java.util.Map;

/**
 * @author badqiu
 */
public class ObjectUtils {

	public static boolean isNullOrEmptyString(Object o) {
		if(o == null)
			return true;
		if(o instanceof String) {
			String str = (String)o;
			if(str.length() == 0)
				return true;
		}
		return false;
	}
	
	public static boolean isEmpty(Object c) {
		if(c == null) return true;
		boolean isSequence = c instanceof Map || c instanceof Collection || c instanceof String || c.getClass().isArray();
		if(!isSequence) {
			throw new IllegalArgumentException("illegal argument type,must be : Map,Collection,Array,String");
		}
		
		if(c instanceof Map) {
			if(((Map)c).isEmpty()){
				return true;
			}
		}
		if(c instanceof Collection) {
			if(((Collection)c).isEmpty()){
				return true;
			}
		}
		if(c.getClass().isArray()) {
			if(((Object[])c).length == 0){
				return true;
			}
		}
		if(c instanceof String) {
			if(((String)c).length() == 0){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isNotEmpty(Object c) {
		return !isEmpty(c);
	}
	
}
