
PrimeFaces.locales ['it'] = {
	     closeText: 'Chiudi',
	     prevText: 'Precedente',
	     nextText: 'Prossimo',
	     monthNames: ['Gennaio', 'Febbraio', 'Marzo', 'Aprile', 'Maggio', 'Giugno', 'Luglio', 'Agosto', 'Settembre', 'Ottobre', 'Novembre', 'Dicembre' ],
	     monthNamesShort: ['Gen', 'Feb', 'Mar', 'Apr', 'Mag', 'Giu', 'Lug', 'Ago', 'Set', 'Ott', 'Nov', 'Dic' ],
	     dayNames: ['Domenica', 'Lunedì', 'Martedì', 'Mercoledì', 'Giovedì', 'Venerdì', 'Sabato'],
	     dayNamesShort: ['Dom', 'Lun', 'Mar', 'Mer', 'Gio', 'Ven', 'Sab'],
	     dayNamesMin: ['D', 'L', 'M', 'M ', 'G', 'V ', 'S'],
	     weekHeader: 'Sett',
	     firstDay: 1,
	     isRTL: false,
	     showMonthAfterYear: false,
	     yearSuffix:'',
	     timeOnlyTitle: 'Solo Tempo',
	     timeText: 'Ora',
	     hourText: 'Ore',
	     minuteText: 'Minuti',
	     secondText: 'Secondi',
	     currentText: 'Data Odierna',
	     ampm: false,
	     month: 'Mese',
	     week: 'Settimana',
	     day: 'Giorno',
	     allDayText: 'Tutto il Giorno'
	 };


function leggiRegionali(){
	url = document.getElementById('chiusuraIterForm:tabView:urlHidden').value;
	if(url != ''){
	  	
		window.open(url,'_blank');
	}
	
	
}


function selectAllChecks(context){
	
	conta = 0;
	while(document.getElementById(context+conta)){
	    
		
		
		if(!document.getElementById(context+conta).checked){
			
			document.getElementById(context+conta).checked=true;
			
		}
		conta++;
	}
	
}


function deselectAllChecks(context){
	
	conta = 0;
	
	while(document.getElementById(context+conta)){
	    
		if(document.getElementById(context+conta).checked){
			
			document.getElementById(context+conta).checked=false;
		
		}
		conta++;
	}
	
}


function showDialog(){
	
	printPanelDialog.show();
	printPanelDialog.hide();
}



function dragSortTable(tableId,hiddeId,colIndx) {
	
	
    
	var table = document.getElementById(tableId);
	var hidden = document.getElementById(hiddeId);
	var nodi = table.getElementsByTagName('tr');
	
	hidden.value="";
	
	//scorro i tr
	  for (i = 0; i <nodi.length; i++) {
		  
		  //prendo il tr
		  var nodo = nodi[i];
		  
		//prendo il td
		  var nodoTd = nodo.getElementsByTagName('td')[colIndx];
		  
		  
   		  var innerDiv = nodoTd.getElementsByTagName('div')[0];
		  
		  var valore ="";  
			  
		  if (innerDiv.getElementsByTagName('a').length > 0){
			  
			  valore=innerDiv.getElementsByTagName('a')[0].innerHTML;
		  
		  }else{
			  
			  valore=innerDiv.innerHTML;
			  
		  }
		  
		  
		  if(i==nodi.length-1){
		     
			  hidden.value +=valore;
		  
		  }else{
			  
			  hidden.value +=valore+"_";
			  
		  }
		  
		  
	  }
	 

}
									



function showPanel(elementId, elementId2) {
	
	$("#"+elementId).show();
	$("#"+elementId2).hide();
}

function hidePanel(elementId, elementId2) {
	
	$("#"+elementId).hide();
	$("#"+elementId2).show();
}

function addRowsInTable(idTable) {

	// genera l'header della tabella preso dalla tabella di appoggio
	if (document.getElementById(idTable).tBodies[0].childNodes.length == 0) {
		var headerTR = document.getElementById('idHeader').cloneNode(true);
		document.getElementById(idTable).tHead.appendChild(headerTR);
				
	}
		
	// genera la nuova riga con i campi presi dalla tabella di appoggio
	var newRow = document.getElementById('idRow').cloneNode(true);
	
	newRow.id = document.getElementById(idTable).tBodies[0].childNodes.length
			+ "_" + newRow.id;

	var deleteTD = document.createElement('TD');
	deleteTD.innerHTML = "<td id='deleteButton' class='utilbox'><img style='cursor:pointer;' src='resources/images/deleteRow.png' onclick=\"deleteFromIncrementRecordTable('"
			+ newRow.id + "')\"></td>";
	newRow.appendChild(deleteTD);
	document.getElementById(idTable).tBodies[0].appendChild(newRow);
	
	// controllo tipologia campi
	for(i=0;i<newRow.childNodes.length;i++){
		
		var rowTD = newRow.childNodes[i];
		var htmlStr = rowTD.innerHTML.replace(/^\s+|\s+$/g,"");
		var inputId = newRow.id+"_"+i;
		deleteExistingWidget(inputId);
		rowTD.childNodes[0].id=inputId;
		
	}

	if (document.getElementById(idTable).tBodies[0].childNodes.length == 1 && document.getElementById('idButtonSub')!=null ) {
		
		document.getElementById('idButtonSub').style.visibility = 'visible';
	}

	dojo.publish('tabContainer' + '_adjust-Child-Height', []);
}

