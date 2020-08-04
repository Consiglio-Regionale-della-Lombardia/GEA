<html>
<head>
</head>
<body>
	<div>
		<h1>Protocollo - Test upload allegato</h1>
	</div>
	
	<form action="/alfresco/service/crl/protocollo/synch/atto/allegato/insert?alf_ticket=${session.ticket}" method="post" enctype="multipart/form-data" accept-charset="utf-8">
	<table>
		<tr>
			<td>
				ID protocollo atto:
			</td>
			<td>
				<select name="idProtocolloAtto">
					<#list attiResults as atto>
						<#if atto.properties["crlatti:idProtocollo"]?exists>
						<option value='${atto.properties["crlatti:idProtocollo"]}'>${atto.name}</option>
						</#if>
					</#list>
				</select>
			</td>
		</tr>
		<tr>
			<td>ID protocollo allegato:</td>
			<td>
				<input type="text" name="idProtocolloAllegato">
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

