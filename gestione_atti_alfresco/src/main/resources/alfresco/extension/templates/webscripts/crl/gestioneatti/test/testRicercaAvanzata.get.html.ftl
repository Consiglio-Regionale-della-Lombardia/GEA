<html>
<head>
<script type="text/javascript">
	
	function ricerca(atto) {
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
		xmlhttp.open("POST", "http://localhost:9080/alfresco/service/crl/atto/ricerca/avanzata?alf_ticket=${session.ticket}", true);
		xmlhttp.setRequestHeader("Content-Type", "application/json");
		xmlhttp.send(atto);
	}
	
	function ricercaAvanzata(){
		var currentForm = document.forms[0];
		var numeroAtto = currentForm.numeroAtto.value;
		var stato = currentForm.stato.value;
		var tipoAtto = currentForm.tipoAtto.options[currentForm.tipoAtto.selectedIndex].value;
		var legislatura = currentForm.legislatura.options[currentForm.legislatura.selectedIndex].value;
		var numeroProtocollo = currentForm.numeroProtocollo.value;
		var numeroDcr = currentForm.numeroDcr.value;
		var oggetto = currentForm.oggetto.value;
		var dataIniziativaDa = currentForm.dataIniziativaDa.value;
		var dataIniziativaA = currentForm.dataIniziativaA.value;
		var tipoIniziativa = currentForm.tipoIniziativa.value;
		var primoFirmatario = currentForm.primoFirmatario.value;
		var firmatario = currentForm.firmatario.value;
		
		//ricerca avanzata
		var tipoChiusura = currentForm.tipoChiusura.value;
		var esitoVotoComRef = currentForm.esitoVotoComRef.value;
		var esitoVotoAula = currentForm.esitoVotoAula.value;
		var commReferente = currentForm.commReferente.value;
		var commConsultiva = currentForm.commConsultiva.value;
		var redigente = currentForm.redigente.value;
		var deliberante = currentForm.deliberante.value;
		var numeroLcr = currentForm.numeroLcr.value;
		var numeroLr = currentForm.numeroLr.value;
		var anno = currentForm.anno.value;
		var abbinamento = currentForm.abbinamento.value;
		var stralcio = currentForm.stralcio.value;
		
		//pannello destra
		var dataPubblicazioneDa = currentForm.dataPubblicazioneDa.value;
		var dataPubblicazioneA = currentForm.dataPubblicazioneA.value;
		
		var dataSedutaScDa = currentForm.dataSedutaScDa.value;
		var dataSedutaScA = currentForm.dataSedutaScA.value;
		
		var dataSedutaCommDa = currentForm.dataSedutaCommDa.value;
		var dataSedutaCommA = currentForm.dataSedutaCommA.value;
		
		var dataSedutaAulaDa = currentForm.dataSedutaAulaDa.value;
		var dataSedutaAulaA = currentForm.dataSedutaAulaA.value;
		
		var relatore = currentForm.relatore.value;
		var organismoStatutario = currentForm.organismoStatutario.value;
		var soggettoConsultato = currentForm.soggettoConsultato.value;
		var emendato = currentForm.emendato.value;
		var sospeso = currentForm.sospeso.value;
		var numeroDgr = currentForm.numeroDgr.value;
		
		var jsonRicerca = {
		"atto" : {
			"numeroAtto" : ""+numeroAtto+"",
			"tipoAtto" : ""+tipoAtto+"",
			"stato" : ""+stato+"",
			"oggetto" : ""+oggetto+"",
			"legislatura" : ""+legislatura+"",
			"numeroProtocollo" : ""+numeroProtocollo+"",
			"tipoIniziativa" : ""+tipoIniziativa+"",
			"numeroDcr" : ""+numeroDcr+"",
			"primoFirmatario" : ""+primoFirmatario+"",
			"firmatario" : ""+firmatario+"",
			"dataIniziativaDa" : ""+dataIniziativaDa+"",
			"dataIniziativaA" : ""+dataIniziativaA+"",
			"tipoChiusura" : ""+tipoChiusura+"",
			"esitoVotoCommissioneReferente" : ""+esitoVotoComRef+"",
			"esitoVotoAula" : ""+esitoVotoAula+"",
			"commissioneReferente" : ""+commReferente+"",
			"commissioneConsultiva" : ""+commConsultiva+"",
			"redigente" : ""+redigente+"",
			"deliberante" : ""+deliberante+"",
			"numeroLcr" : ""+numeroLcr+"",
			"numeroLr" : ""+numeroLr+"",
			"anno" : ""+anno+"",
			"abbinamento" : ""+abbinamento+"",
			"stralcio" : ""+stralcio+"",
			"dataPubblicazioneDa" : ""+dataPubblicazioneDa+"",
			"dataPubblicazioneA" : ""+dataPubblicazioneA+"",
			"dataSedutaSCDa" : ""+dataSedutaScDa+"",
			"dataSedutaSCA" : ""+dataSedutaScA+"",
			"dataSedutaCommissioneDa" : ""+dataSedutaCommDa+"",
			"dataSedutaCommissioneA" : ""+dataSedutaCommA+"",
			"dataSedutaAulaDa" : ""+dataSedutaAulaDa+"",
			"dataSedutaAulaA" : ""+dataSedutaAulaA+"",
			"relatore" : ""+relatore+"",
			"organismoStatutario" : ""+organismoStatutario+"",
			"soggettoConsultato" : ""+soggettoConsultato+"",
			"emendato" : ""+emendato+"",
			"sospeso" : ""+sospeso+"",
			"numeroDgr" : ""+numeroDgr+""
			}
		};
		
		ricerca(JSON.stringify(jsonRicerca));

	}
	
</script>
</head>
<body>
	<div>
		<h1>Test ricerca avanzata atti</h1>
	</div>
	
	<form action="javascript:ricercaAvanzata()">
	<table>
		<tr>
			<td>Numero atto:</td>
			<td>
				<input type="text" name="numeroAtto">
			</td>
		</tr>
		<tr>
			<td>Tipo:</td>
			<td>
				<select name="tipoAtto">
					<option value="" selected="selected"></option>
				<#list tipiAttoResults as tipoAtto>
					<option value="${tipoAtto.name}">${tipoAtto.name} - ${tipoAtto.properties.title}</option>
				</#list>
				</select>
			</td>
		</tr>
		<tr>
			<td>Legislatura:</td>
			<td>
				<select name="legislatura">
					<option value="" selected="selected"></option>
				<#list legislatureResults as legislatura>
					<option value="${legislatura.name}">${legislatura.name}</option>
				</#list>
				</select>
			</td>
		</tr>
		<tr>
			<td>Stato:</td>
			<td><input type="text" name="stato"></td>
		</tr>
		<tr>
			<td>Numero protocollo:</td>
			<td><input type="text" name="numeroProtocollo"></td>
		</tr>
		<tr>
			<td>Numero DCR:</td>
			<td><input type="text" name="numeroDcr"></td>
		</tr>
		<tr>
			<td>Oggetto:</td>
			<td><input type="text" name="oggetto"></td>
		</tr>
		<tr>
			<td>Data iniziativa Da:</td>
			<td><input type="text" name="dataIniziativaDa"></td>
		</tr>
		<tr>
			<td>Data iniziativa A:</td>
			<td><input type="text" name="dataIniziativaA"></td>
		</tr>
		<tr>
			<td>Tipo iniziativa:</td>
			<td><input type="text" name="tipoIniziativa"></td>
		</tr>
		<tr>
			<td>Primo firmatario:</td>
			<td><input type="text" name="primoFirmatario"></td>
		</tr>
		<tr>
			<td>Firmatario:</td>
			<td><input type="text" name="firmatario"></td>
		</tr>
		<tr>
			<td>Inizio campi ricerca avanzata</td>
		</tr>
		<tr>
			<td>Tipo chiusura:</td>
			<td><input type="text" name="tipoChiusura"></td>
		</tr>
		<tr>
			<td>esitoVotoComRef:</td>
			<td><input type="text" name="esitoVotoComRef"></td>
		</tr>
		<tr>
			<td>esitoVotoAula:</td>
			<td><input type="text" name="esitoVotoAula"></td>
		</tr>
		<tr>
			<td>commReferente:</td>
			<td><input type="text" name="commReferente"></td>
		</tr>
		<tr>
			<td>commConsultiva:</td>
			<td><input type="text" name="commConsultiva"></td>
		</tr>
		<tr>
			<td>redigente:</td>
			<td><input type="text" name="redigente"></td>
		</tr>
		<tr>
			<td>deliberante:</td>
			<td><input type="text" name="deliberante"></td>
		</tr>
		<tr>
			<td>numeroLcr:</td>
			<td><input type="text" name="numeroLcr"></td>
		</tr>
		<tr>
			<td>numeroLr:</td>
			<td><input type="text" name="numeroLr"></td>
		</tr>
		<tr>
			<td>anno:</td>
			<td><input type="text" name="anno"></td>
		</tr>
		<tr>
			<td>abbinamento:</td>
			<td><input type="text" name="abbinamento"></td>
		</tr>
		<tr>
			<td>stralcio:</td>
			<td><input type="text" name="stralcio"></td>
		</tr>
		<tr>
			<td>dataPubblicazioneDa:</td>
			<td><input type="text" name="dataPubblicazioneDa"></td>
		</tr>
		<tr>
			<td>dataPubblicazioneA:</td>
			<td><input type="text" name="dataPubblicazioneA"></td>
		</tr>
		<tr>
			<td>dataSedutaScDa:</td>
			<td><input type="text" name="dataSedutaScDa"></td>
		</tr>
		<tr>
			<td>dataSedutaScA:</td>
			<td><input type="text" name="dataSedutaScA"></td>
		</tr>
		<tr>
			<td>dataSedutaCommDa:</td>
			<td><input type="text" name="dataSedutaCommDa"></td>
		</tr>
		<tr>
			<td>dataSedutaCommA:</td>
			<td><input type="text" name="dataSedutaCommA"></td>
		</tr>
		<tr>
			<td>dataSedutaAulaDa:</td>
			<td><input type="text" name="dataSedutaAulaDa"></td>
		</tr>
		<tr>
			<td>dataSedutaAulaA:</td>
			<td><input type="text" name="dataSedutaAulaA"></td>
		</tr>
		<tr>
			<td>relatore:</td>
			<td><input type="text" name="relatore"></td>
		</tr>
		<tr>
			<td>organismo statutario:</td>
			<td><input type="text" name="organismoStatutario"></td>
		</tr>
		<tr>
			<td>Soggetto consultato:</td>
			<td><input type="text" name="soggettoConsultato"></td>
		</tr>
		<tr>
			<td>emendato:</td>
			<td><input type="text" name="emendato"></td>
		</tr>
		<tr>
			<td>sospeso:</td>
			<td><input type="text" name="sospeso"></td>
		</tr>
		<tr>
			<td>numeroDgr:</td>
			<td><input type="text" name="numeroDgr"></td>
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

