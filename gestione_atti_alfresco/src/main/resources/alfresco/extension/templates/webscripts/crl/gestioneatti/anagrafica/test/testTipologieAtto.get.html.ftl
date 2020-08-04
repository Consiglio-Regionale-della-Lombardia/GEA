<html>
	<head></head>
	<body>
		<h1>Test - Lettura tipologie di un tipo di atto</h1>
		<form action="/alfresco/wcservice/crl/anagrafica/atto/tipologie" method="get">
		<table>
		<tr>
			<td>
				Tipi di atto:
			</td>
			<td>
				<select name="tipoAtto">
				<#list tipiAttoResults as tipoAtto>
				<option value="${tipoAtto.name}">${tipoAtto.name} - ${tipoAtto.properties.title}</option>
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