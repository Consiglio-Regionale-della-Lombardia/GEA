<html>
<head>
</head>
<body>
	<div>
		<h1>Test get allegati</h1>
	</div>
	
	<form action="/alfresco/service/crl/atto/allegati?alf_ticket=${session.ticket}" method="get">
	<table>
		<tr>
			<td>
				Atto:
			</td>
			<td>
				<select name="idAtto">
					<#list attiResults as atto>
						<option value="${atto.nodeRef}">${atto.name}</option>
					</#list>
				</select>
			</td>
			<td>
				<select name="tipologiaAllegati">
					<option value="Tipologia1">Tipologia1</option>
					<option value="Tipologia2">Tipologia2</option>
				</select>
			</td>
		</tr>
		<tr>
			<td></td>
			<td><input type="submit" value="Get Allegati"></td>
		</tr>
	</table>
	</form>
</body>
</html>

