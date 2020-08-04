<html>
<head>
<script type="text/javascript">
	
	function creaDatiAtto(atto) {
		var xmlhttp;
		if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
			xmlhttp = new XMLHttpRequest();
		} else {// code for IE6, IE5
			xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
				document.getElementById("myDiv").innerHTML = xmlhttp.status + " " + xmlhttp.responseText;
			}
		}
		xmlhttp.open("POST", "http://localhost:8080/alfresco/service/crl/atto/assegnazione?alf_ticket=${session.ticket}", true);
		xmlhttp.setRequestHeader("Content-Type", "application/json");
		xmlhttp.send(atto);
	}
	
	function aggiornaDatiCustom(){
		var currentForm = document.forms[0];
		var id = currentForm.id.options[currentForm.id.selectedIndex].value;
		var descrizione = currentForm.descrizione.value;
		var dataProposta = currentForm.dataProposta.value;
		var dataAssegnazione =  currentForm.dataAssegnazione.value;
		var ruolo =  currentForm.ruolo.options[currentForm.ruolo.selectedIndex].value;
		var stato =  currentForm.stato.value;
		var dataAnnullo =  currentForm.dataAnnullo.value;
		
		//parere
		var descrizioneParere = currentForm.descrizioneParere.value;
		var obbligatorioParere = currentForm.obbligatorioParere.value;
		var dataAnnulloParere = currentForm.dataAnnulloParere.value;
		var dataAssegnazioneParere = currentForm.dataAssegnazioneParere.value;

		var attoCustom = {
		"atto" : {
			"id" : ""+id+"",
			"stato" : "Assegnato a Commissioni",
			"passaggi": [
				{
					"passaggio": {
					    "descrizione": "Passaggio1",
						"commissioni" : [
							{
								"commissione":{
									"descrizione":""+descrizione+"",
									"dataProposta":""+dataProposta+"",
									"dataAssegnazione":""+dataAssegnazione+"",
									"ruolo" : ""+ruolo+"",
									"stato" : ""+stato+"",
									"dataAnnullo":""+dataAnnullo+""
								}
							},
							{
								"commissione":{
									"descrizione":"Commissione statica",
									"dataProposta":""+dataProposta+"",
									"dataAssegnazione":""+dataAssegnazione+"",
									"ruolo" : "Consultiva",
									"stato" : ""+stato+"",
									"dataAnnullo":""+dataAnnullo+""
								}
							},
							{
								"commissione":{
									"descrizione":"Commissione pippo",
									"dataProposta":""+dataProposta+"",
									"dataAssegnazione":""+dataAssegnazione+"",
									"ruolo" : "Consultiva",
									"stato" : ""+stato+"",
									"dataAnnullo":""+dataAnnullo+""
								}
							}
						]
					}
				}
			],
			"organismiStatutari" : [
				{
					"organismoStatutario":{
						"descrizione":""+descrizioneParere+"",
						"dataAssegnazione":""+dataAssegnazioneParere+"",
						"dataAnnullo":""+dataAnnulloParere+"",
						"obbligatorio":""+obbligatorioParere+""
					}
				},
				{
					"organismoStatutario":{
						"descrizione":"Parere statico",
						"dataAssegnazione":""+dataAssegnazioneParere+"",
						"dataAnnullo":""+dataAnnulloParere+"",
						"obbligatorio":""+obbligatorioParere+""
					}
				}
			]
			}
		};
		
		creaDatiAtto(JSON.stringify(attoCustom));

	}
	
</script>
</head>
<body>
	<div>
		<h1>Test modifica assegnazione atto</h1>
	</div>
	
	<form action="javascript:aggiornaDatiCustom()">
	<table>
		<tr>
			<td>Id:</td>
			<td>
				<select name="id">
				<#list atti as atto>
					<option value="${atto.nodeRef}">${atto.name}</option>
				</#list>
				</select>
			</td>
		</tr>
		<tr>
			<td><strong>Commissione (aggiunta di commissione statica)</strong></td>
		</tr>
		<tr>
			<td>Nome Commissione:</td>
			<td><input type="text" name="descrizione" value=""></td>
		</tr>
		<tr>
			<td>Data proposta:</td>
			<td><input type="text" name="dataProposta" value="2012-08-01"></td>
		</tr>
		<tr>
			<td>Data firma:</td>
			<td><input type="text" name="dataFirma" value="2012-08-02"></td>
		</tr>
		<tr>
			<td>Data annullo:</td>
			<td><input type="text" name="dataAnnullo" value="2012-08-03"></td>
		</tr>
		<tr>
			<td>Data assegnazione:</td>
			<td><input type="text" name="dataAssegnazione" value="2012-08-04"></td>
		</tr>
		<tr>
			<td>Ruolo:</td>
			<td>
			<select name="ruolo">
				<option value="Referente" selected="selected">Referente</option>
				<option value="Consultiva">Consultiva</option>
			</select>
			</td>
		</tr>
		<tr>
			<td>Stato:</td>
			<td><input type="text" name="stato" value="Trasmesso"></td>
		</tr>
		
		<tr>
		<td></td>
		</tr>
		
		<tr>
			<td><strong>Parere (aggiunta di parere statico)</strong></td>
		</tr>
		 <tr>
			<td>Organismo statutario:</td>
			<td><input type="text" name="descrizioneParere" value=""></td>
		</tr>
		<tr>
			<td>Data assegnazione:</td>
			<td><input type="text" name="dataAssegnazioneParere" value="2012-08-04"></td>
		</tr>
		<tr>
			<td>Data annullo:</td>
			<td><input type="text" name="dataAnnulloParere" value="2012-08-06"></td>
		</tr>
		 <tr>
			<td>Obbligatorio:</td>
			<td><input type="text" name="obbligatorioParere" value="false"></td>
		</tr>
		<tr>
		<td><input type="submit"></td>
		</tr>
	</table>
	
	
		
	</form>
	
	<h2>Risposta:</h2>
	<div id="myDiv">
		
	</div>
</body>
</html>

