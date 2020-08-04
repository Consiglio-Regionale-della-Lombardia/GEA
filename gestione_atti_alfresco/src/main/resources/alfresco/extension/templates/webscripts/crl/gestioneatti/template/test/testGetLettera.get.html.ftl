<html>
<head>
<script type="text/javascript">
	

	
	function creaLettera() {
	
		var currentForm = document.forms[0];
		var idAtto = currentForm.idAtto.options[currentForm.idAtto.selectedIndex].value;
		var tipoTemplate = currentForm.tipoTemplate.options[currentForm.tipoTemplate.selectedIndex].value;
		var gruppo = currentForm.gruppo.value;
	
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
		
		currentForm.gruppo.value = "http://localhost:8080/alfresco/service/crl/template/lettere/crealettera?idAtto="+idAtto+"&tipoTemplate="+tipoTemplate+"&gruppo="+gruppo+"&alf_ticket=${session.ticket}"
		
		xmlhttp.open("GET", "http://localhost:8080/alfresco/service/crl/template/lettere/crealettera?idAtto="+idAtto+"&tipoTemplate="+tipoTemplate+"&gruppo="+gruppo+"&alf_ticket=${session.ticket}", true);
		xmlhttp.setRequestHeader("Content-Type", "application/json");
		xmlhttp.send();
	}
	
	
	function creaTutteLettereServComm(){
	
		var arrayLettere =  new Array();
		arrayLettere.push("crltemplate:letteraTrasmissioneCommissioneReferenteServComm");
		arrayLettere.push("crltemplate:letteraTrasmissioneCommissioneReferenteConsultivaServComm");
		arrayLettere.push("crltemplate:letteraTrasmissioneCommissioneReferenteConsultivaOrganismoServComm");
		arrayLettere.push("crltemplate:letteraRettificaAssegnazioneServComm");
		arrayLettere.push("crltemplate:letteraIntegrazioneCPOServComm");
		arrayLettere.push("crltemplate:letteraRitiroServComm");
		
		
		arrayLettere.push("crltemplate:letteraAggiuntaFirmaServComm");
		arrayLettere.push("crltemplate:letteraRitiroFirmaServComm");
		arrayLettere.push("crltemplate:letteraAssestamentoBilancioServComm");
		arrayLettere.push("crltemplate:letteraBilancioProgrammaticoServComm");
		arrayLettere.push("crltemplate:letteraIniziativaPresidenteGiuntaFusioneDistaccoComuniServComm");
		arrayLettere.push("crltemplate:letteraIniziativaPopolareServComm");
		
		arrayLettere.push("crltemplate:letteraRendicontoGeneraleServComm");
		arrayLettere.push("crltemplate:letteraLeggeFinanziariaServComm");
		arrayLettere.push("crltemplate:letteraDOCAssegnazionePSRServComm");
		arrayLettere.push("crltemplate:letteraDOCAssegnazioneDSAServComm");
		arrayLettere.push("crltemplate:letteraAssegnazioneProgrammaLavoroCommEuropeaServComm");
		arrayLettere.push("crltemplate:letteraParereCommissioneServComm");
		arrayLettere.push("crltemplate:letteraIntesaCommissioneGiuntaServComm");
		arrayLettere.push("crltemplate:letteraParereCommissioneConSospPausaEstivaServComm");
		arrayLettere.push("crltemplate:letteraIntesaCommissioneGiuntaConSospPausaEstivaServComm");
		arrayLettere.push("crltemplate:letteraParerePropostaDiRegolamentoServComm");
		arrayLettere.push("crltemplate:letteraAssegnazioneInSedeDeliberantePDAServComm");
		arrayLettere.push("crltemplate:letteraAssegnazioneInSedeReferentePDAServComm");
		arrayLettere.push("crltemplate:letteraAssegnazionePerIstruttoriaPLPServComm");
		arrayLettere.push("crltemplate:letteraRelazionePresentataDallaGiuntaServComm");
		arrayLettere.push("crltemplate:letteraAssegnazionePerIstruttoriaPREServComm");
		arrayLettere.push("crltemplate:letteraAssegnazionePerIstruttoriaREFServComm");
		arrayLettere.push("crltemplate:letteraAssegnazionePerIstruttoriaINPServComm");
		
		
		for (var i=0; i<arrayLettere.length; i++){
		
		
		
			var currentForm = document.forms[0];
			var idAtto = currentForm.idAtto.options[currentForm.idAtto.selectedIndex].value;
			var tipoTemplate = arrayLettere[i];
			var gruppo = currentForm.gruppo.value;
		
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
			
			
			xmlhttp.open("GET", "http://localhost:8080/alfresco/service/crl/template/lettere/crealettera?idAtto="+idAtto+"&tipoTemplate="+tipoTemplate+"&gruppo="+gruppo+"&alf_ticket=${session.ticket}", true);
			xmlhttp.setRequestHeader("Content-Type", "application/json");
			xmlhttp.send();
		
			
		}
		
	}
	
	
	function creaTutteLettereComm(){
	
		var arrayLettere =  new Array();
		arrayLettere.push("crltemplate:letteraTrasmissionePDLCommissioni");
		arrayLettere.push("crltemplate:letteraTrasmissionePDACommissioni");
		arrayLettere.push("crltemplate:letteraTrasmissionePDAUrgenzaCommissioni");
		arrayLettere.push("crltemplate:letteraTrasmissionePARCommissioni");
		arrayLettere.push("crltemplate:letteraTrasmissionePLPCommissioni");
		arrayLettere.push("crltemplate:letteraPresaAttoRELCommissioni");
		arrayLettere.push("crltemplate:letteraTrasmissioneREFCommissioni");
		arrayLettere.push("crltemplate:letteraTrasmissioneParereDaSedeConsultivaCommissioni");
		arrayLettere.push("crltemplate:letteraTrasmissioneAulaLeggeRiordinoCommissioni");
		arrayLettere.push("crltemplate:letteraTrasmissioneAulaTestoUnico");
		arrayLettere.push("crltemplate:letteraTrasmissioneDocRisDsaCommissioni");
		arrayLettere.push("crltemplate:letteraTrasmissioneDocRisProgrammaEuropeoCommissioni");	
		arrayLettere.push("crltemplate:letteraTrasmissioneParereSuDOCCommissioni");
		arrayLettere.push("crltemplate:letteraRichiestaParereNormaFinanziariaCommissioni");
		arrayLettere.push("crltemplate:letteraTrasmissioneParereSuNormaFinanziariaCommissioni");
		arrayLettere.push("crltemplate:letteraTrasmissioneRISCommissioni");
	

		
		for (var i=0; i<arrayLettere.length; i++){
		
		
		
			var currentForm = document.forms[0];
			var idAtto = currentForm.idAtto.options[currentForm.idAtto.selectedIndex].value;
			var tipoTemplate = arrayLettere[i];
			var gruppo = currentForm.gruppo.value;
		
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
			
			
			xmlhttp.open("GET", "http://localhost:8080/alfresco/service/crl/template/lettere/crealettera?idAtto="+idAtto+"&tipoTemplate="+tipoTemplate+"&gruppo="+gruppo+"&alf_ticket=${session.ticket}", true);
			xmlhttp.setRequestHeader("Content-Type", "application/json");
			xmlhttp.send();
		
			
		}
		
	}
	
	
	function creaTutteLettereAula(){
	
		var arrayLettere =  new Array();
		arrayLettere.push("crltemplate:letteraTrasmissioneLCRAula");
		arrayLettere.push("crltemplate:letteraTrasmissionePDAAula");
		arrayLettere.push("crltemplate:letteraTrasmissionePDABilancioAula");
		arrayLettere.push("crltemplate:letteraTrasmissionePDAVariazioneBilancioAula");
		arrayLettere.push("crltemplate:letteraTrasmissioneORGAula");
		arrayLettere.push("crltemplate:letteraConvalidaORGAula");
		arrayLettere.push("crltemplate:letteraTrasmissioneBURLAula");
		
	

		
		for (var i=0; i<arrayLettere.length; i++){
		
		
		
			var currentForm = document.forms[0];
			var idAtto = currentForm.idAtto.options[currentForm.idAtto.selectedIndex].value;
			var tipoTemplate = arrayLettere[i];
			var gruppo = currentForm.gruppo.value;
		
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
			
			
			xmlhttp.open("GET", "http://localhost:8080/alfresco/service/crl/template/lettere/crealettera?idAtto="+idAtto+"&tipoTemplate="+tipoTemplate+"&gruppo="+gruppo+"&alf_ticket=${session.ticket}", true);
			xmlhttp.setRequestHeader("Content-Type", "application/json");
			xmlhttp.send();
		
			
		}
		
	}
	
</script>
</head>
<body>
	<div>
		<h1>Test creazione lettera</h1>
	</div>
	
	<form action="javascript:creaLettera()">
	<table>
		<tr>
			<td>IdAtto:</td>
			<td>
				<select name="idAtto">
				<#list atti as atto>
					<option value="${atto.nodeRef}">${atto.name}</option>
				</#list>
				</select>
			</td>
		</tr>
		<tr>
			<td>IdLettera:</td>
			<td>
				<select name="tipoTemplate">
				<#list lettere as lettera>
					<option value="${lettera.typeShort}">${lettera.typeShort}</option>
				</#list>
				</select>
			</td>
		</tr>
		<tr>
			<td>Gruppo corrente:</td>
			<td>
				<input type="text" name="gruppo">
			</td>
		</tr>
	
		<tr>
		<td></td>
		<td><input type="submit"></td>
		</tr>
		
		<tr>
		<td></td>
		<td><input type="button" value="testall-ServComm" onclick="javascript:creaTutteLettereServComm()"></td>
		</tr>
		
		<tr>
		<td></td>
		<td><input type="button" value="testall-Comm" onclick="javascript:creaTutteLettereComm()"></td>
		</tr>
		
		<tr>
		<td></td>
		<td><input type="button" value="testall-Aula" onclick="javascript:creaTutteLettereAula()"></td>
		</tr>
		
	</table>
	
	
		
	</form>
	
	
	
	<h2>Risposta:</h2>
	<div id="myDiv">
		
	</div>
</body>
</html>

