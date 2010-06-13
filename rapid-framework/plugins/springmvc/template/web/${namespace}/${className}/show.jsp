<%@page import="${basepackage}.model.*" %>
<#include "/macro.include"/> 
<#include "/custom.include"/> 
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/commons/taglibs.jsp" %>

<rapid:override name="head">
	<title><%=${className}.TABLE_ALIAS%>信息</title>
</rapid:override>

<rapid:override name="content">
	<form action="<@jspEl "ctx"/>${actionBasePath}/list.do" method="post">
		<input type="button" value="返回列表" onclick="window.location='<@jspEl "ctx"/>${actionBasePath}/list.do'"/>
		<input type="button" value="后退" onclick="history.back();"/>
		
	<#list table.columns as column>
	<#if column.pk>
		<input type="hidden" id="${column.columnNameLower}" name="${column.columnNameLower}" value="<@jspEl classNameLower+"."+column.columnNameLower/>"/>
	</#if>
	</#list>
	
		<table class="formTable">
		<#list table.columns as column>
		<#if !column.htmlHidden>
			<tr>	
				<td class="tdLabel"><%=${className}.ALIAS_${column.constantName}%></td>	
				<td><#rt>
				<#compress>
				<#if column.isDateTimeColumn>
				<c:out value='<@jspEl classNameLower+"."+column.columnNameLower+"String"/>'/>
				<#else>
				<c:out value='<@jspEl classNameLower+"."+column.columnNameLower/>'/>
				</#if>
				</#compress>
				<#lt></td>
			</tr>
		</#if>
		</#list>
		</table>
	</form>
</rapid:override>

<%-- jsp模板继承,具体使用请查看: http://code.google.com/p/rapid-framework/wiki/rapid_jsp_extends --%>
<jsp:include page="base.jsp"/>