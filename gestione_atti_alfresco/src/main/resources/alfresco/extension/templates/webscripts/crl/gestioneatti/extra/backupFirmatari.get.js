var noderef = args.noderef;
var exit = false;

function backup(folder) {
    var path = folder.displayPath.split("/");
    var level = path.length;
    var fullPath = folder.displayPath + "/" + folder.name;
    logger.log("backup: " + fullPath + " level: " + level);
    var data = [];

    if (exit) {
        return data;
    }

    if (folder.isSubType("crlatti:atto")) {
        var firmatariFolder = folder.childByNamePath("Firmatari");//folder.properties["crlatti:firmatari"];
        var firmatariNode = null;
        if (firmatariFolder) {
            firmatariNode = firmatariFolder.children;
        }
        if (firmatariNode && firmatariNode.length > 0) {
            var firmatariData = [];
            for (var i = 0; i < firmatariNode.length; i++) {
                var firmatarioNode = firmatariNode[i];
                if (!(firmatarioNode.typeShort == "crlatti:firmatario")){
                    continue;
                }
                var firmatarioData = new Object();
                firmatarioData["cm:name"] = firmatarioNode.properties["cm:name"];
                if (firmatarioNode.properties["crlatti:dataFirma"])
                    firmatarioData["crlatti:dataFirma"] = utils.toISO8601(firmatarioNode.properties["crlatti:dataFirma"]);
                if (firmatarioNode.properties["crlatti:dataRitiro"])
                    firmatarioData["crlatti:dataRitiro"] = utils.toISO8601(firmatarioNode.properties["crlatti:dataRitiro"]);
                if (firmatarioNode.properties["crlatti:isPrimoFirmatario"])
                    firmatarioData["crlatti:isPrimoFirmatario"] = firmatarioNode.properties["crlatti:isPrimoFirmatario"];
                if (firmatarioNode.properties["crlatti:gruppoConsiliare"])
                    firmatarioData["crlatti:gruppoConsiliare"] = firmatarioNode.properties["crlatti:gruppoConsiliare"];
                if (firmatarioNode.properties["crlatti:numeroOrdinamento"])
                    firmatarioData["crlatti:numeroOrdinamento"] = firmatarioNode.properties["crlatti:numeroOrdinamento"];
                firmatariData.push(firmatarioData);
            }
            if (firmatariData.length > 0){
                data.push({atto: fullPath, firmatari: firmatariData});
                logger.log(folder.name + " : has firmatari");
            } else {
                logger.log(folder.name + " empty");
            }
        } else {
            logger.log(folder.name + " empty");
        }

    } else if (level <= 7) {   //|| (level == 8 && fullPath.indexOf("AttiIndirizzo")!=-1 )
        var thisChildren = folder.children;
        if (!thisChildren) {
            thisChildren = [];
        }
        logger.log(folder.name + " " + thisChildren.length + " children");
        for (var i = 0; i < thisChildren.length; i++) {
            var child = thisChildren[i];
            var thisData = backup(child);
            data = data.concat(thisData);
        }
    }
    return data;
}

var space = search.findNode(noderef);
backupData = backup(space);
var file = companyhome.childByNamePath("backupFirmatari.json");
if (!file) {
    file = companyhome.createFile("backupFirmatari.json");
}

//file.content = '{\n';
var body = jsonUtils.toJSONString(backupData);//backupData.join(",\n");
file.content = body;
//file.content += '}\n';
file.save();
