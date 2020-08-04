<html>
	<head></head>
	<body>
		<h1>Test - Lettura anni di una legislatura</h1>
		<form action="/alfresco/wcservice/crl/anagrafica/legislatura" method="get">
		<table>
		<tr>
			<td>
				Legislatura:
			</td>
			<td>
				<select name="nomeLegislatura">
				<#list legislatureResults as legislatura>
				<option value="${legislatura.name}">${legislatura.name}</option>
				</#list>
			</select>
			</td>
			<td>
				<input type="submit">
			</td>
		</tr>
		</table>
		</form>
	</body>
</html>