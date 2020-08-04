<html>
<head>
</head>
<body>
	<div>
		<h1>Test get testo atto</h1>
	</div>
	
	<form action="/alfresco/service/crl/atto/records?alf_ticket=${session.ticket}" method="get">
	<table>
		<tr>
			<td>
				Atto:
			</td>
			<td>
				<select name="id">
					<#list attiResults as atto>
						<option value="${atto.nodeRef}">${atto.name}</option>
					</#list>
				</select>
			</td>
		</tr>
		<tr>
			<td></td>
			<td><input type="submit" value="Get Records"></td>
		</tr>
	</table>
	</form>
</body>
</html>

