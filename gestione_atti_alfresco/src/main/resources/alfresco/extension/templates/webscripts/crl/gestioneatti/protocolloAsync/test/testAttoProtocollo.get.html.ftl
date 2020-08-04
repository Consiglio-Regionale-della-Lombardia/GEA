<html>
<head>
<script type="text/javascript">
	
	function creaAtto(atto) {
		var xmlhttp;
		if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
			xmlhttp = new XMLHttpRequest();
		} else {// code for IE6, IE5
			xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
				document.getElementById("myDiv").innerHTML = xmlhttp.responseText;
			}
		}
		xmlhttp.open("POST", "/alfresco/service/crl/protocollo/atto?alf_ticket=${session.ticket}", true);
		xmlhttp.setRequestHeader("Content-Type", "application/json");
		xmlhttp.send(atto);
	}
	
	function creaAttoCustom(){
		var currentForm = document.forms[0];
		var numeroAtto = currentForm.numeroAtto.value;
		var estensioneAtto = currentForm.estensioneAtto.value;
		var tipologia = currentForm.tipologia.value;
		var tipoAtto = currentForm.tipoAtto.options[currentForm.tipoAtto.selectedIndex].value;
		var legislatura = currentForm.legislatura.options[currentForm.legislatura.selectedIndex].value;
		var idProtocollo = currentForm.idProtocollo.value;
		var numeroProtocollo = currentForm.numeroProtocollo.value;
		var numeroRepertorio = currentForm.numeroRepertorio.value;
		var dataRepertorio = currentForm.dataRepertorio.value;
		var classificazione = currentForm.classificazione.value;
		var oggetto = currentForm.oggetto.value;
		
		var tipoIniziativa = currentForm.tipoIniziativa.value;
		var dataIniziativa = currentForm.dataIniziativa.value;
		var descrizioneIniziativa = currentForm.descrizioneIniziativa.value;
		
		var numeroDgr = currentForm.numeroDgr.value;
		var dataDgr = currentForm.dataDgr.value;
		
		var assegnazione = currentForm.assegnazione.value;
		
		var esibenteMittente = currentForm.esibenteMittente.value;
		var urlFascicolo = currentForm.urlFascicolo.value;
		
		var attoCustom = {
		"atto" : {
			"numeroAtto" : ""+numeroAtto+"",
			"estensioneAtto" : ""+estensioneAtto+"",
			"tipoAtto" : ""+tipoAtto+"",
			"tipologia" : ""+tipologia+"",
			"legislatura" : ""+legislatura+"",
			"idProtocollo" : ""+idProtocollo+"",
			"numeroProtocollo" : ""+numeroProtocollo+"",
			"numeroRepertorio" : ""+numeroRepertorio+"",
			"dataRepertorio" : ""+dataRepertorio+"",
			"classificazione" : ""+classificazione+"",
			"oggetto" : ""+oggetto+"",
			"tipoIniziativa" : ""+tipoIniziativa+"",
			"dataIniziativa" : ""+dataIniziativa+"",
			"descrizioneIniziativa" : ""+descrizioneIniziativa+"",
			"numeroDgr" : ""+numeroDgr+"",
			"dataDgr" : ""+dataDgr+"",
			"assegnazione" : ""+assegnazione+"",
			"esibenteMittente" : ""+esibenteMittente+"",
			"urlFascicolo" : ""+urlFascicolo+""
			}
		};
		
		creaAtto(JSON.stringify(attoCustom));   
                //creaAtto(JSON.stringify(attoCustom));

	}
	
</script>
</head>
<body>
	<div>
		<h1>Protocollo - Test creazione atto</h1>
	</div>
	
	<form action="javascript:creaAttoCustom()">
	<table>
		<tr>
			<td>ID protocollo:</td>
			<td>
				<input type="text" name="idProtocollo">
			</td>
		</tr>
		<tr>
			<td>Numero protocollo:</td>
			<td>
				<input type="text" name="numeroProtocollo">
			</td>
		</tr>
		<tr>
			<td>Numero atto:</td>
			<td>
				<input type="text" name="numeroAtto">
			</td>
		</tr>
		<tr>
			<td>Estensione atto:</td>
			<td>
				<input type="text" name="estensioneAtto">
			</td>
		</tr>
		<tr>
			<td>Tipo:</td>
			<td>
				<select name="tipoAtto">
                                <option value="null">NULL</option>
                                <option value="AAA">AAA</option>
				<#list tipiAttoResults as tipoAtto>
					<option value="${tipoAtto.name}">${tipoAtto.name} - ${tipoAtto.properties.title}</option>
				</#list>
				</select>
			</td>
		</tr>
		<tr>
			<td>Tipologia:</td>
			<td><input type="text" name="tipologia" value="Tipo1"></td>
		</tr>
		<tr>
			<td>Legislatura:</td>
			<td>
				<select name="legislatura">
				<#list legislatureResults as legislatura>
					<option value="${legislatura.name}">${legislatura.name}</option>
				</#list>
				</select>
			</td>
		</tr>
		<tr>
			<td>Numero repertorio:</td>
			<td><input type="text" name="numeroRepertorio" value="01"></td>
		</tr>
		<tr>
			<td>Data repertorio:</td>
			<td><input type="text" name="dataRepertorio" value="2012-09-01"></td>
		</tr>
		<tr>
			<td>Classificazione:</td>
			<td><input type="text" name="classificazione" value="classificazione1"></td>
		</tr>
		<tr>
			<td>Oggetto:</td>
			<td><input type="text" name="oggetto" value="oggetto2"></td>
		</tr>
		<tr>
			<td>Tipo iniziativa:</td>
			<td><input type="text" name="tipoIniziativa" value="01_ATTO DI INIZIATIVA CONSILIARE"></td>
		</tr>
		<tr>
			<td>Data iniziativa:</td>
			<td><input type="text" name="dataIniziativa" value="2012-09-02"></td>
		</tr>
		<tr>
			<td>Descrizione iniziativa:</td>
			<td><input type="text" name="descrizioneIniziativa" value="descrizione1000mila"></td>
		</tr>
		<tr>
			<td>Numero DGR:</td>
			<td><input type="text" name="numeroDgr" value="9877"></td>
		</tr>
		<tr>
			<td>Data DGR:</td>
			<td><input type="text" name="dataDgr" value="2012-09-10"></td>
		</tr>
		<tr>
			<td>Assegnazione:</td>
			<td><input type="text" name="assegnazione" value="assegnazione9"></td>
		</tr>
		<tr>
			<td>Esibente / Mittente:</td>
			<td><input type="text" name="esibenteMittente" value="CONSIGLIERI REGIONALI: Pinco Pallino - Mario Rossi - Agostino Alloni - Alessandro Alfieri"></td>
		</tr>
		<tr>
			<td>URL Fascicolo:</td>
			<td><input type="text" name="urlFascicolo" value="/url/al/fascicolo"></td>
		</tr>
		<tr>
		<td></td>
		<td><input type="submit"></td>
		</tr>
	</table>
	
	
		
	</form>
	
	<h2>Risposta:</h2>
	<div id="myDiv">
		
	</div>
</body>
</html>

