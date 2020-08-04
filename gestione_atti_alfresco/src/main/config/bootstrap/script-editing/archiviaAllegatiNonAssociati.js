function getOrCreateFolder(parent, folderName){
    folder = parent.childByNamePath(folderName);
    if (!folder){
        folder = parent.createFolder(folderName);
    }
    return folder;
}


allegatiOldFolder = companyhome.childByNamePath("Import/Gestione Atti/Protocollo/AllegatiOld");
docs = companyhome.childByNamePath("Import/Gestione Atti/Protocollo/Allegati").children;

for (i = 0; i<docs.length; i++){
    doc = docs[i]
    name = doc.name;
    year = name.substring(0,4);
    yearFolder = getOrCreateFolder(allegatiOldFolder, year);
    month = name.substring(4,6);
    monthFolder = getOrCreateFolder(yearFolder, month);
    doc.move(monthFolder);
}
