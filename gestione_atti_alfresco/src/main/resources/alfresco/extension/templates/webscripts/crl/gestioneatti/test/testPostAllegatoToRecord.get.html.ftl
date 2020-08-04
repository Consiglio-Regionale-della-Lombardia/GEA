<html>
<head>
</head>
<body>
	<div>
		<h1>Test allegato to record</h1>
	</div>
	
	<form action="/alfresco/service/crl/atto/allegato/to/record?alf_ticket=${session.ticket}" method="post" enctype="multipart/form-data" accept-charset="utf-8">
	<table>
		<tr>
			<td>
				Atto:
			</td>
			<td>
				<select name="id">
					<#list allegatiResults as allegato>
						<option value="${allegato.nodeRef}">${allegato.name}</option>
					</#list>
				</select>
			</td>
		</tr>
		<tr>
			<td></td>
			<td><input type="submit" value="Rendi l'allegato un testo"></td>
		</tr>
	</table>
	</form>
</body>
</html>