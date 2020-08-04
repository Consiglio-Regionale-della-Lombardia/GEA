<html>
<head>
<script type="text/javascript">
	function creaDati(atto) {
		var xmlhttp;
		if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
			xmlhttp = new XMLHttpRequest();
		} else {// code for IE6, IE5
			xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
				document.getElementById("myDiv").innerHTML = "status: "+xmlhttp.status;
			}
		}
		xmlhttp.open("POST", "http://localhost:8080/alfresco/service/crl/atto/consultazione?alf_ticket=${session.ticket}", true);
		xmlhttp.setRequestHeader("Content-Type", "application/json");
		xmlhttp.send(atto);
	}
	
	function creaDatiCustom(){
		var currentForm = document.forms[0];
		var idAtto = currentForm.idAtto.options[currentForm.idAtto.selectedIndex].value;
		var consultazione = currentForm.consultazione.value;	
		var dataSeduta = currentForm.dataSeduta.value;
		var dataConsultazione = currentForm.dataConsultazione.value;
		var prevista = currentForm.prevista.value;
		var discussa = currentForm.discussa.value;
		var note = currentForm.note.value;

		
		var attoCustom = {
			"target":{
				"consultazione": ""+consultazione+""
			},
			"atto": {
				"id": ""+idAtto+"",
				"consultazioni": [
						{
						"consultazione": {
								"descrizione": ""+consultazione+"",
								"dataSeduta": ""+dataSeduta+"",
								"dataConsultazione": ""+dataConsultazione+"",
								"prevista": ""+prevista+"",
								"discussa": ""+discussa+"",
								"note": ""+note+""
								
							}
						},
						{
							"consultazione": {
								"descrizione": "test"
							}
						}
				]
				 
			}
		};
		
	
		
		creaDati(JSON.stringify(attoCustom));

	}
	
</script>
</head>
<body>
	<div>
		<h1>Test consultazione</h1>
	</div>
	
	<form action="javascript:creaDatiCustom()">
	<table>
		<tr>
			<td>
				Atto:
			</td>
			<td>
				<select name="idAtto">
					<#list atti as atto>
						<option value="${atto.nodeRef}">${atto.name}</option>
					</#list>
				</select>
			</td>
		</tr>
		<tr>
			<td>Consultazione</td>
			<td>
				<input type="text" name="consultazione"/>
			</td>
		</tr>
		<tr>
			<td>Data Seduta</td>
			<td>
				<input type="text" name="dataSeduta" value="2012-09-09"/>
			</td>
		</tr>
		<tr>
			<td>Data Consultazione</td>
			<td>
				<input type="text" name="dataConsultazione" value="2012-08-09"/>
			</td>
		</tr>
		<tr>
			<td>Prevista:</td>
			<td>
				<select name="prevista">
					<option value="true">SI</option>
					<option value="false">NO</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>Discussa:</td>
			<td>
				<select name="discussa">
					<option value="true">SI</option>
					<option value="false">NO</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>note</td>
			<td>
				<input type="text" name="note" value="note test"/>
			</td>
		</tr>	
		<tr>
			<td></td>
			<td><input type="submit" value="salva"></td>
		</tr>
	</table>
	
	
		
	</form>
	
	<h2>Risposta:</h2>
	<div id="myDiv">
		
	</div>
</body>
</html>

