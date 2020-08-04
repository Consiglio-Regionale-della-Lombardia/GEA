<html>
<head>
</head>
<body>
	<div>
		<h1>Protocollo - Test upload allegato</h1>
	</div>
	
	<form action="/alfresco/service/crl/protocollo/synch/atto/allegato/update?alf_ticket=${session.ticket}" method="post" enctype="multipart/form-data" accept-charset="utf-8">
	<table>
		<tr>
			<td>
				Atto:
			</td>
			<td>
				<select name="idProtocolloAllegato">
					<#list attiResults as atto>
						<#if atto.properties["crlatti:idProtocollo"]?exists>
						<option value='${atto.properties["crlatti:idProtocollo"]}'>${atto.name}</option>
						</#if>
					</#list>
				</select>
			</td>
		</tr>
		<tr>
			<td>File:</td>
			<td>
				<input type="file" name="file">
			</td>
		</tr>
		<tr>
			<td></td>
			<td><input type="submit" value="Salva allegato"></td>
		</tr>
	</table>
	</form>
</body>
</html>

