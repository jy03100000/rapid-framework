package cn.org.rapid_framework.generator.provider.db.sql.model;

import java.util.Iterator;
import java.util.LinkedHashSet;

import cn.org.rapid_framework.generator.provider.db.sql.SqlFactory;
import cn.org.rapid_framework.generator.provider.db.table.model.Column;
import cn.org.rapid_framework.generator.provider.db.table.model.Table;
import cn.org.rapid_framework.generator.util.StringHelper;
import cn.org.rapid_framework.generator.util.sqlparse.SqlParseHelper;
/**
 * 用于生成代码的Sql对象.对应数据库的sql语句
 * 使用SqlFactory.parseSql()生成 <br />
 * 
 * SQL参数同时支持以下几种语法
 * <pre>
 * hibernate: :username,
 * ibatis2: #username#,$usename$,
 * mybatis(or ibatis3): #{username},${username}
 * </pre>
 * SQL对象创建示例：
 * <pre>
 * Sql sql = SqlFactory.parseSql("select * from user_info where username=#username# and password=#password#");
 * </pre>
 * 
 * @see SqlFactory
 * @author badqiu
 *
 */
public class Sql {
	public static String MULTI_POLICY_ONE = "one";
	public static String MULTI_POLICY_MANY = "many";
	
	String tableSqlName = null; //是否需要
	String operation = null;
	String resultClass;
	String parameterClass;
	String remarks;
	
	String multiPolicy = "many"; // many or one
	
	LinkedHashSet<Column> columns = new LinkedHashSet<Column>();
	LinkedHashSet<SqlParameter> params = new LinkedHashSet<SqlParameter>();
	
	String sourceSql; // source sql
	String executeSql;
	
	public Sql() {
	}
	
	public Sql(Sql sql) {
		this.tableSqlName = sql.tableSqlName;
		
		this.operation = sql.operation;
		this.parameterClass = sql.parameterClass;
		this.resultClass = sql.resultClass;
		this.multiPolicy = sql.multiPolicy;
		
		this.columns = sql.columns;
		this.params = sql.params;
		this.sourceSql = sql.sourceSql;
		this.executeSql = sql.executeSql;
		this.remarks = sql.remarks;
	}
	
	public boolean isColumnsInSameTable() {
		//FIXME 还要增加表的列数与columns是否相等,才可以为select 生成 include语句
		if(columns == null || columns.isEmpty()) return false;
		if(columns.size() == 1 && columns.iterator().next().getTable() != null) return true;
		String preTableName = columns.iterator().next().getSqlName();
		for(Column c :columns) {
			Table table = c.getTable();
			if(table == null) {
				return false;
			}
			if(preTableName.equalsIgnoreCase(table.getSqlName())) {
				continue;
			}
		}
		return true;
	}
	
	/**
	 * 得到select查询返回的resultClass,可以通过setResultClass()自定义，如果没有自定义则为你自动生成<br />
	 * resultClass可以为com.company.User的完全路径
	 * 示例:
	 * <pre>
	 * select count(*) from user, 返回值为: Long
	 * select * from user 返回值为: User
	 * select count(*) cnt, sum(age) sum_age 返回值为: getOperation()+"Result";
	 * </pre>
	 * @return
	 */
	public String getResultClass() {
		if(StringHelper.isNotBlank(resultClass)) return resultClass;
		if(columns.size() == 1) {
			return columns.iterator().next().getSimpleJavaType();
		}
		if(isColumnsInSameTable()) {
			return columns.iterator().next().getTable().getClassName();
		}else {
			if(operation == null) return null;
			return StringHelper.makeAllWordFirstLetterUpperCase(StringHelper.toUnderscoreName(operation))+"Result";
		}
	}    
	
	public void setResultClass(String queryResultClass) {
		this.resultClass = queryResultClass;
	}
	/**
	 * 返回getResultClass()的类名称 <br />
	 * 示例: <br />
	 * 如getResultClass()=com.company.User,将返回User
	 */	
	public String getResultClassName() {
		int lastIndexOf = getResultClass().lastIndexOf(".");
		return lastIndexOf >= 0 ? getResultClass().substring(lastIndexOf+1) : getResultClass();
	}

	/**
	 * SQL参数过多时用于封装为一个ParameterObject的class<br />
	 * <pre>
	 * 可以通过setParameterClass()自定义
	 * 没有自定义则:
	 * 如果是select查询,返回 operation+"Query"
	 * 其它则返回operation+"Parameter"
	 * <pre>
	 */	
	public String getParameterClass() {
		if(StringHelper.isNotBlank(parameterClass)) return parameterClass;
		if(StringHelper.isBlank(operation)) return null;
		if(isSelectSql()) {
			return StringHelper.makeAllWordFirstLetterUpperCase(StringHelper.toUnderscoreName(operation))+"Query";
		}else {
			return StringHelper.makeAllWordFirstLetterUpperCase(StringHelper.toUnderscoreName(operation))+"Parameter";
		}
	}
	
	public void setParameterClass(String parameterClass) {
		this.parameterClass = parameterClass;
	}
	/**
	 * 返回getParameterClass()的类名称 <br />
	 * 示例: <br />
	 * 如getParameterClass()=com.company.UserQuery,将返回UserQuery
	 */		
	public String getParameterClassName() {
		int lastIndexOf = getParameterClass().lastIndexOf(".");
		return lastIndexOf >= 0 ? getParameterClass().substring(lastIndexOf+1) : getParameterClass();
	}
	
	//TODO columnsSize大于二并且不是在同一张表中,将创建一个QueryResultClassName类,同一张表中也要考虑创建类
	public int getColumnsCount() {
		return columns.size();
	}
	public void addColumn(Column c) {
		columns.add(c);
	}
	/**
	 * 得到该sql方法相对应的操作名称,模板中的使用方式为: public List ${operation}(),示例值: findByUsername
	 * @return
	 */
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getOperationFirstUpper() {
		return StringHelper.capitalize(getOperation());
	}
	/**
	 * 用于控制查询结果,固定值为:one,many
	 * @return
	 */
	public String getMultiPolicy() {
		return multiPolicy;
	}
	public void setMultiPolicy(String multiPolicy) {
		//TODO 是否要增加验证数据为 one,many
		this.multiPolicy = multiPolicy;
	}
	/**
	 * 得到sqlect 查询的列对象(column),如果是insert,delete,update语句,则返回empty Set.<br />
	 * 示例:
	 * <pre>
	 * SQL : select count(*) cnt, sum(age) sum_age from user_info
	 * columns: cnt,sum_age
	 * </pre>
	 * @return
	 */
	public LinkedHashSet<Column> getColumns() {
		return columns;
	}
	public void setColumns(LinkedHashSet<Column> columns) {
		this.columns = columns;
	}
	/**
	 * 得到SQL的参数对象<br />
	 * 示例:
	 * <pre>
	 * SQL : select * from user_info where username=:user and password=:pwd limit :offset,:limit
	 * params: user,pwd,offset,limit
	 * </pre>
	 * @return
	 */
	public LinkedHashSet<SqlParameter> getParams() {
		return params;
	}
	public void setParams(LinkedHashSet<SqlParameter> params) {
		this.params = params;
	}
	/**
	 * 得到SQL原始语句
	 * @return
	 */
	public String getSourceSql() {
		return sourceSql;
	}
	public void setSourceSql(String sourceSql) {
		this.sourceSql = sourceSql;
	}
	
//	public String replaceParamsWith(String prefix,String suffix) {
//		String sql = sourceSql;
//		List<SqlParameter> sortedParams = new ArrayList(params);
//		Collections.sort(sortedParams,new Comparator<SqlParameter>() {
//			public int compare(SqlParameter o1, SqlParameter o2) {
//				return o2.paramName.length() - o1.paramName.length();
//			}
//		});
//		for(SqlParameter s : sortedParams){ //FIXME 现在只实现了:username参数替换
//			sql = StringHelper.replace(sql,":"+s.getParamName(),prefix+s.getParamName()+suffix);
//		}
//		return sql;
//	}
	/**
	 * sourceSql转换为在数据库实际执行的SQL,
	 * 示例:
	 * <pre>
	 * sourceSql: select * from user where username=:username and password=:password
	 * executeSql: select * from user where username=? and password=?
	 * </pre>
	 * @return
	 */
	public String getExecuteSql() {
		return executeSql;
	}
	
	public void setExecuteSql(String executeSql) {
		this.executeSql = executeSql;
	}
	
	public String getSql() {
		return replaceWildcardWithColumnsSqlName(sourceSql);
	}
	
	public String getJdbcSql() {
		return getSql();
	}
	
	public String getIbatisSql() {
		return getSql();
	}
	
	public String getHql() {
		return getSql();
	}
	
	public String getIbatis3Sql() {
		return getSql();
	}

	private String joinColumnsSqlName() {
		StringBuffer sb = new StringBuffer();
		for(Iterator<Column> it = columns.iterator();it.hasNext();) {
			sb.append(it.next().getSqlName());
			if(it.hasNext()) sb.append(",");
		}
		return sb.toString();
	}
	
	private String replaceWildcardWithColumnsSqlName(String sql) {
		if(isSelectSql() && SqlParseHelper.getSelect(sourceSql).indexOf("*") >= 0) {
			return SqlParseHelper.getPrettySql("select " + joinColumnsSqlName() + " " + SqlParseHelper.removeSelect(sql));
		}else {
			return sql;
		}
	}
	/**
	 * 当前的sourceSql是否是select语句
	 * @return
	 */
	public boolean isSelectSql() {
		return sourceSql.trim().toLowerCase().matches("(?i)\\s*select\\s.*from\\s+.*");
	}
	/**
	 * 当前的sourceSql是否是update语句
	 * @return
	 */
	public boolean isUpdateSql() {
		return sourceSql.trim().toLowerCase().matches("(?i)\\s*update\\s+.*");
	}
	/**
	 * 当前的sourceSql是否是delete语句
	 * @return
	 */
	public boolean isDeleteSql() {
		return sourceSql.trim().toLowerCase().matches("(?i)\\s*delete\\s+from\\s.*");
	}
	/**
	 * 当前的sourceSql是否是insert语句
	 * @return
	 */
	public boolean isInsertSql() {
		return sourceSql.trim().toLowerCase().matches("(?i)\\s*insert\\s+into\\s+.*");
	}
	/**
	 * 得到表相对应的sqlName,主要用途为生成文件时的分组.
	 * @return
	 */
	public String getTableSqlName() {
		return tableSqlName;
	}

	public void setTableSqlName(String tableName) {
		this.tableSqlName = tableName;
	}
	/**
	 * 得到备注
	 * @return
	 */
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String comments) {
		this.remarks = comments;
	}
	/**
	 * 根据tableSqlName和成相对应的tableClassName,主要用途路径变量引用.如${tableClassName}Dao.java
	 * @return
	 */
	public String getTableClassName() {
		if(StringHelper.isBlank(tableSqlName)) return null;
		return StringHelper.makeAllWordFirstLetterUpperCase(StringHelper.toUnderscoreName(tableSqlName));
	}

	public Column getColumnBySqlName(String sqlName) {
		for(Column c : getColumns()) {
			if(c.getSqlName().equalsIgnoreCase(sqlName)) {
				return c;
			}
		}
		return null;
	}
	
	public Column getColumnByName(String name) {
	    Column c = getColumnBySqlName(name);
	    if(c == null) {
	    	c = getColumnBySqlName(StringHelper.toUnderscoreName(name));
	    }
	    return c;
	}
}