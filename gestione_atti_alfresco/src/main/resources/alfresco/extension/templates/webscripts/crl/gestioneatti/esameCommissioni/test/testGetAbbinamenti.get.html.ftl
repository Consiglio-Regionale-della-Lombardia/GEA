<html>
<head>

<head>
</head>
<body>
	<div>
		<h1>Test get abbinamenti di un atto</h1>
	</div>
	
	<form action="/alfresco/service/crl/atto/abbinamenti?alf_ticket=${session.ticket}" method="get">
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
			<td>Passaggio:</td>
			<td>
				<input name="passaggio" />
			</td>
		</tr>
		<tr>
			<td></td>
			<td><input type="submit" value="Get abbinamenti Atto"></td>
		</tr>
		
	</table>
	</form>
	<h2>Risposta:</h2>
	<div id="myDiv">
		
	</div>
</body>
</html>