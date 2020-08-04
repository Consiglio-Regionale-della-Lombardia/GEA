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
		xmlhttp.open("POST", "http://localhost:8080/alfresco/service/crl/atto/parere?alf_ticket=${session.ticket}", true);
		xmlhttp.setRequestHeader("Content-Type", "application/json");
		xmlhttp.send(atto);
	}
	
	function creaDatiCustom(){
		var currentForm = document.forms[0];
		var idAtto = currentForm.idAtto.options[currentForm.idAtto.selectedIndex].value;
		var organismo = currentForm.organismo.value;	
		var dataAnnullo = currentForm.dataAnnullo.value;
		var dataRicezioneParere = currentForm.dataRicezioneParere.value;
		var esito = currentForm.esito.value;
		var dataRicezioneOrgano = currentForm.dataRicezioneOrgano.value;
		var note = currentForm.note.value;

		
		var attoCustom = {
			"target":{
				"organismoStatutario": ""+organismo+""
			},
			"atto": {
				"id": ""+idAtto+"",
				"organismiStatutari": [
						{
							"organismoStatutario": {
								"descrizione": ""+organismo+"",
								"dataAnnullo": ""+dataAnnullo+"",
								"parere":
									{
										"descrizione": "parere",
										"dataRicezioneParere": ""+dataRicezioneParere+"",
										"esito": ""+esito+"",
										"dataRicezioneOrgano": ""+dataRicezioneOrgano+"",
										"note": ""+note+""
									}
							}
						},
						{
							"organismoStatutario": {
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
		<h1>Test parere organismo statutario</h1>
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
			<td>Organismo statutario:</td>
			<td>
				<input type="text" name="organismo"/>
			</td>
		</tr>
		<tr>
			<td>Data Annullo</td>
			<td>
				<input type="text" name="dataAnnullo" value="2012-09-09"/>
			</td>
		</tr>
		<tr>
			<td>Data Ricezione Parere</td>
			<td>
				<input type="text" name="dataRicezioneParere" value="2012-08-09"/>
			</td>
		</tr>
		<tr>
			<td>esito</td>
			<td>
				<input type="text" name="esito" value="esito test"/>
			</td>
		</tr>	
		<tr>
			<td>Data Ricezione Organo</td>
			<td>
				<input type="text" name="dataRicezioneOrgano" value="2012-08-07"/>
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

