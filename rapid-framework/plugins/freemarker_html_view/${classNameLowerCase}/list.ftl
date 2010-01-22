<%@page import="com.company.project.model.*" %>

<html>
<head>
	<@include "/commons/meta.jsp" />
	<base href="<%=basePath%>">
	<title><%=UserInfo.TABLE_ALIAS%> 维护</title>
	
	<link href="${ctx}/widgets/simpletable/simpletable.css" type="text/css" rel="stylesheet">
	<script type="text/javascript" src="${ctx}/widgets/simpletable/simpletable.js"></script>
	
	<script type="text/javascript" >
		$(document).ready(function() {
			// 分页需要依赖的初始化动作
			window.simpleTable = new SimpleTable('simpleTableForm',${page.thisPageNumber},${page.pageSize},'${pageRequest.sortColumns}');
		});
	</script>

</head>
<body>

<@include "/commons/messages.ftl" />

<div class="queryPanel">
<form method="get" style="display: inline;">
<fieldset>
	<legend>搜索</legend>
	<table>
		<tr>	
			<td class="tdLabel">
					<%=UserInfo.ALIAS_USERNAME%>
			</td>		
			<td>
				<input value="${pageRequest.filters.username}"  name="s_username"  />
			</td>
			<td class="tdLabel">
					<%=UserInfo.ALIAS_PASSWORD%>
			</td>		
			<td>
				<input value="${pageRequest.filters.password}"  name="s_password"  />
			</td>
			<td class="tdLabel">
					<%=UserInfo.ALIAS_BIRTH_DATE%>
			</td>		
			<td>
				<input value="${pageRequest.filters.birthDate}" onclick="WdatePicker({dateFmt:'<%=UserInfo.FORMAT_BIRTH_DATE%>'})"  name="s_birthDate"   />
			</td>
			<td class="tdLabel">
					<%=UserInfo.ALIAS_SEX%>
			</td>		
			<td>
				<input value="${pageRequest.filters.sex}"  name="s_sex"  />
			</td>
		</tr>	
		<tr>	
			<td class="tdLabel">
					<%=UserInfo.ALIAS_AGE%>
			</td>		
			<td>
				<input value="${pageRequest.filters.age}"  name="s_age"  />
			</td>
		</tr>	
	</table>
</fieldset>
<div class="handleControl">
	<input type="submit" class="stdButton" style="width:80px" value="查询" onclick="getReferenceForm(this).action='${ctx}/userinfo'"/>
	<input type="button" class="stdButton" style="width:80px" value="新增" onclick="window.location = '${ctx}/userinfo/new'"/>
	<input type="button" class="stdButton" style="width:80px" value="删除" onclick="doRestBatchDelete('${ctx}/userinfo','items',document.forms.ec)"/>
<div>
</form>
</div>

<form id="simpleTableForm" action="${ctx}/userinfo" method="get" style="display: inline;">

	<!-- auto include parameters -->
	<#list pageRequest.filters?keys as key>
		<input type="hidden" name="s_${key}" value="${pageRequest.filters[key]}"/>
	</#list>
	
	<input type="hidden" name="pageNumber" id="pageNumber" />
	<input type="hidden" name="pageSize" id="pageSize"/>
	<input type="hidden" name="sortColumns" id="sortColumns"/>
	
	<div class="gridTable">
	
		<@pageToolBar page=page>
		显示在这里是为了提示你如何自定义表头,可修改模板删除此行
		</@pageToolBar>
	
		<table width="100%"  border="0" cellspacing="0" class="gridBody">
		  <thead class="tableHeader">
			  
			  <tr>
				<th style="width:1px;"> </th>
				<th style="width:1px;"><input type="checkbox" onclick="setAllCheckboxState('items',this.checked)"></th>
				
				<!-- 排序时为th增加sortColumn即可,new SimpleTable('sortColumns')会为tableHeader自动增加排序功能; -->
				<th sortColumn="USERNAME" ><%=UserInfo.ALIAS_USERNAME%></th>
				<th sortColumn="PASSWORD" ><%=UserInfo.ALIAS_PASSWORD%></th>
				<th sortColumn="BIRTH_DATE" ><%=UserInfo.ALIAS_BIRTH_DATE%></th>
				<th sortColumn="SEX" ><%=UserInfo.ALIAS_SEX%></th>
				<th sortColumn="AGE" ><%=UserInfo.ALIAS_AGE%></th>

				<th>操作</th>
			  </tr>
			  
		  </thead>
		  <tbody class="tableBody">
		  	  <#list page.result as item>
			  <tr class="<#if item_index % 2 == 0>odd<#else>even</#if>">
				<td>${page.thisPageFirstElementNumber + item_index + 1}</td>
				<td><input type="checkbox" name="items" value="userId=${item.userId}&"></td>
				
				<td><c:out value='${item.username}'/>&nbsp;</td>
				<td><c:out value='${item.password}'/>&nbsp;</td>
				<td><c:out value='${item.birthDateString}'/>&nbsp;</td>
				<td><c:out value='${item.sex}'/>&nbsp;</td>
				<td><c:out value='${item.age}'/>&nbsp;</td>
				<td>
					<a href="${ctx}/userinfo/${item.userId}">查看</a>&nbsp;&nbsp;
					<a href="${ctx}/userinfo/${item.userId}/edit">修改</a>&nbsp;&nbsp;
					<a href="${ctx}/userinfo/${item.userId}" onclick="doRestDelete(this,'你确认删除?');return false;">删除</a>
				</td>
			  </tr>
			  </#list>
		  </tbody>
		</table>
	
		<@pageToolBar page=page>
		显示在这里是为了提示你如何自定义表头,可修改模板删除此行
		</@pageToolBar>
		
	</div>
</form>

</body>
</html>

