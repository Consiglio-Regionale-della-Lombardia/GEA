<html>
<head>
</head>
<body>
	<div>
		<h1>Test get relatori commissione</h1>
	</div>
	
	<form action="/alfresco/service/crl/atto/esamecommissioni/relatori?alf_ticket=${session.ticket}" method="get">
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
				Commissione:
			</td>
			<td>
				<input type="text" name="commissione" />
			</td>
			<td>
				Passaggio:
			</td>
			<td>
				<input type="text" name="passaggio" />
			</td>
		</tr>
		<tr>
			<td></td>
			<td><input type="submit" value="Get Relatori"></td>
		</tr>
	</table>
	</form>
</body>
</html>


