<html>
<head>
</head>
<body>
	<div>
		<h1>Test get dati atto</h1>
	</div>
	
	<form action="/alfresco/service/crl/atto/noteallegati?alf_ticket=${session.ticket}" method="get">
	<table>
		<tr>
			<td>
				Atto:
			</td>
			<td>
				<select name="id">
					<#list atti as atto>
						<option value="${atto.nodeRef}">${atto.name}</option>
					</#list>
				</select>
			</td>
		</tr>
		<tr>
			<td></td>
			<td><input type="submit" value="Get Note e Links Atto"></td>
		</tr>
	</table>
	</form>
</body>
</html>

