var xmlreqs = new Array();
var process_functions = new Array();
var xmlreqs1p = new Array();
var process_functions1p = new Array();
var param_ones = new Array();

function loadXMLDoc(url, processFunction) 
{
    var req = false;
    
    var d = new Date();
	var ts = d.getTime();
	
	if(url.indexOf("?") >= 0) {
		url += "&jsnocache=" + ts;
	}
	else {
		url += "?jsnocache=" + ts;
	}
	
    if (window.XMLHttpRequest) {
        req = new XMLHttpRequest();
        req.onreadystatechange = xmlhttpChange;
        req.open("GET", url, true);
        req.send(null);
    } else if (window.ActiveXObject) {
        req = new ActiveXObject("Microsoft.XMLHTTP");
        if (req) {
        	req.onreadystatechange = xmlhttpChange;
            req.open("GET", url, true);
            req.send();
        }
    }
    xmlreqs.push(req);
    process_functions.push(processFunction);
    xmlhttpChange();
}

function loadXMLDocOneParam(url, processFunction, param1) 
{
    var req = false;
    
    var d = new Date();
	var ts = d.getTime();
	
	if(url.indexOf("?") >= 0) {
		url += "&jsnocache=" + ts;
	}
	else {
		url += "?jsnocache=" + ts;
	}
	
    if (window.XMLHttpRequest) {
        req = new XMLHttpRequest();
        req.onreadystatechange = xmlhttpChangeOneParam;
        req.open("GET", url, true);
        req.send(null);
    } else if (window.ActiveXObject) {
        req = new ActiveXObject("Microsoft.XMLHTTP");
        if (req) {
        	req.onreadystatechange = xmlhttpChangeOneParam;
            req.open("GET", url, true);
            req.send();
        }
    }
    xmlreqs1p.push(req);
    process_functions1p.push(processFunction);
    param_ones.push(param1);
    xmlhttpChangeOneParam();
}

function xmlhttpChange() {
	if (typeof(window['xmlreqs']) == "undefined") {
	  alert('xmlreqs undefined.  returning');
	  return;
	  }
	  
	for (var i=0; i < xmlreqs.length; i++) {
		var req = xmlreqs[i];
		eval(process_functions[i])(req);
		if(req.readyState == 4) xmlreqs[i] = false;
	}
}

function xmlhttpChangeOneParam() {
	if (typeof(window['xmlreqs1p']) == "undefined" || typeof(window['param_ones']) == "undefined") {
	  alert('xmlhttp a param is undefined.  returning early.');
	  return;
	  }
	else {
	  //alert('params are good.  proceeding.');
	  }
	  
	for (var i=0; i < xmlreqs1p.length; i++) {
		var req = xmlreqs1p[i];
		var param1 = param_ones[i];
		eval(process_functions1p[i])(req,param1);
		if(req.readyState == 4) xmlreqs1p[i] = false;
	}
}

function isAJAXRunning() {
	for (var i=0; i < xmlreqs.length; i++) {
		if(xmlreqs[i]) {
			alert("Cannot perform this action while loading values. Please try again once the values are populated.");
			return true;
		}
	}
	return false;
}

function processPromoTemplate(req) 
  {
    // only if req shows "complete"
    if (req.readyState == 4) {
        // only if "OK"
        if (req.status == 200) {
      		//alert(req.responseText);
      		oXml.loadXML(req.responseText);
      		var oScriptNode = oXml.XMLDocument.selectSingleNode("response/script");
      		eval(oScriptNode.text);
      		var oMeta = oXml.XMLDocument.selectSingleNode("response/meta");
      		if(oMeta != null) {
      			getObject('metafields').innerHTML = oMeta.text;
      		}
      		else {
      			getObject('metafields').innerHTML = '';
      		}
      		var oLayout = oXml.XMLDocument.selectSingleNode("response/layout");
      		if(oLayout != null) { 
      			getObject('layout').innerHTML = oLayout.text;
      		}
      		else {
      			getObject('layout').innerHTML = '';
      		}
        } else {
            alert("There was a problem retrieving HTTP response:\n" + req.statusText);
        }
    }
}

function processCategoryWithDiv(req)
{
    // only if req shows "complete"
    if (req.readyState == 4) {
        // only if "OK"
        if (req.status == 200) {
        	getObject('loading').style.display = 'none';
      		window.frames['if_menu_categories'].document.write(req.responseText);
        } else {
            alert("There was a problem retrieving HTTP response:\n" + req.statusText);
        }
    }
}

function processCategory(req)
{
	var iframe = window.frames['if_menu_categories'];
	if (!iframe) return;
	var iframe_doc = iframe.document;
    // only if req shows "complete"
    if (req.readyState == 4) {
        // only if "OK"
        if (req.status == 200) {
        	iframe_doc.clear();
        	iframe_doc.open();
        	iframe_doc.write(req.responseText);
        } else {
            alert("There was a problem retrieving HTTP response:\n" + req.statusText);
        }
    }
    else if (req.readyState == 1) {
    	iframe_doc.clear();
        iframe_doc.open();
    	iframe_doc.write('<html><head><link rel="STYLESHEET" type="text/css" href="includes/core.css" /></head><body class="popupMenu"><br /><label style="color: red;">&nbsp;&nbsp;Loading category tree. please wait ....</label></body></html>');
    }
}

function processCategory3(req)
{
	var iframe = window.frames['if_menu_categories3'];
	if (!iframe) return;
	var iframe_doc = iframe.document;
    // only if req shows "complete"
    if (req.readyState == 4) {
        // only if "OK"
        if (req.status == 200) {
        	iframe_doc.clear();
        	iframe_doc.open();
        	iframe_doc.write(req.responseText);
        } else {
            alert("There was a problem retrieving HTTP response:\n" + req.statusText);
        }
    }
    else if (req.readyState == 1) {
    	iframe_doc.clear();
        iframe_doc.open();
    	iframe_doc.write('<html><head><link rel="STYLESHEET" type="text/css" href="includes/core.css" /></head><body class="popupMenu"><br /><label style="color: red;">&nbsp;&nbsp;Loading category tree. please wait ....</label></body></html>');
    }
}

function processFacNames(req)
{
	// only if req shows "complete"
    if (req.readyState == 4) {
        // only if "OK"
        if (req.status == 200) {
 	        //alert('processFacNames and object is ' + getObject('facNameDiv'));
        	getObject('facNameDiv').innerHTML = req.responseText;
   //     	alert('Set facNamDiv to ' + req.responseText);
        } else {
            alert("There was a problem retrieving HTTP response:\n" + req.statusText);
        }
    }
    else if (req.readyState == 1) {
    	getObject('facNameDiv').innerHTML = '<select id="navAttr"><option style="color: red;">Loading attribute names. please wait ...</option></select>';
    }
}

function processFacNames2(req, rowNum)
{
    // additional logic to get the right facNameDiv for the context row being edited -GCM-
	// only if req shows "complete"
	//var rowNum = document.getElementById('activeRow').value;
	//alert('processFacNames2 readyState is ' + req.readyState);
	if(!rowNum) rowNum = '';
    if (req.readyState == 4) {
        // only if "OK"
        if (req.status == 200) {
        	getObject('facNameDiv' + rowNum).innerHTML = req.responseText;
 	        //alert('processFacNames2 and object is ' + getObject('facNameDiv' + rowNum).innerHTML);
        } else {
            alert("There was a problem retrieving HTTP response:\n" + req.statusText);
        }
    }
    else if (req.readyState == 1) {
    	getObject('facNameDiv' + rowNum).innerHTML = '<select id="facetName' + rowNum + '"><option style="color: red;">Loading attribute names. please wait ...</option></select>';
    }
}


function processNavAttributes(req)
{
	// only if req shows "complete"
    if (req.readyState == 4) {
        // only if "OK"
        if (req.status == 200) {
        	getObject('navAttrDiv').innerHTML = req.responseText;
        } else {
            alert("There was a problem retrieving HTTP response:\n" + req.statusText);
        }
    }
    else if (req.readyState == 1) {
    	getObject('navAttrDiv').innerHTML = '<select id="navAttr"><option style="color: red;">Loading attribute names. please wait ...</option></select>';
    }
}

function processNavFacetAttributeValues(req)
{
	// only if req shows "complete"
    if (req.readyState == 4) {
        // only if "OK"
        if (req.status == 200) {
        	getObject('navFacetAttributeValues').innerHTML = req.responseText;
        	getObject('navFacetAttributeValues').style.display='';
    	    //alert('innerHTML....' + getObject('navFacetAttributeValues').innerHTML);
           if(document.getElementById('attrName')) {
             document.getElementById('attrName').disabled=false;
             }
        } else {
        	if(getObject('navFacetAttributeValues') != null) {
        		getObject('navFacetAttributeValues').innerHTML = '<select><option style="color: red;">Error getting values ...</option></select>';
        	}
            alert("There was a problem retrieving HTTP response:\n" + req.statusText);
        }
    }
    else if (req.readyState == 1) {
    	getObject('navFacetAttributeValues').innerHTML = '<select><option style="color: red;">Loading values. please wait ...</option></select>';
    }
}

function processNavFacetAttributeValues2(req, rowNum)
{
    // additional logic to get the right facNameDiv for the context row being edited -GCM-
	// only if req shows "complete"
	//var rowNum = document.getElementById('activeRow').value;

	// only if req shows "complete"
    if (req.readyState == 4) {
        // only if "OK"
        if (req.status == 200) {
        	getObject('navFacetAttributeValues' + rowNum).innerHTML = req.responseText;
        	getObject('navFacetAttributeValues' + rowNum).style.display='';
    	    //alert('innerHTML....' + getObject('navFacetAttributeValues').innerHTML);
           if(document.getElementById('attrName')) {
             document.getElementById('attrName').disabled=false;
             }
        } else {
            alert("There was a problem retrieving HTTP response:\n" + req.statusText);
        }
    }
    else if (req.readyState == 1) {
    	getObject('navFacetAttributeValues' + rowNum).innerHTML = '<select id="facetValue' + rowNum + '"><option style="color: red;">Loading values. please wait ...</option></select>';
    }
}

function processAttrValList(req)
{
	// only if req shows "complete"
    if (req.readyState == 4) {
        // only if "OK"
        if (req.status == 200) {
        	getObject('selectAttrValList').innerHTML = req.responseText;
        } else {
            alert("There was a problem retrieving HTTP response:\n" + req.statusText);
        }
    }
    else if (req.readyState == 1) {
    	getObject('selectAttrValList').innerHTML = '<label style="color: red;">Loading attribute values. please wait ...</label>';
    }
}

function processAttrValListFO(req)
{
	// only if req shows "complete"
	var tmpVal='<a href="javascript://" onclick="document.getElementById(\'viewRow' + document.getElementById("curRow").value + '\').value=\'View...\';alert(document.getElementById("curRow").value);">';
    if (req.readyState == 4) {
        // only if "OK"
        if (req.status == 200) {
//        	getObject('viewRow' + document.getElementById("curRow").value).innerHTML = tmpVal + '(Close)</a> ' + req.responseText;
        	getObject('viewRow' + document.getElementById("curRow").value).innerHTML = req.responseText;
        } else {
            alert("There was a problem retrieving HTTP response:\n" + req.statusText);
        }
    }
    else if (req.readyState == 1) {
    	getObject('viewRow' + document.getElementById("curRow").value).innerHTML = '<label style="color: red;">Loading attribute values. please wait ...</label>';
    }
}

function processAttrList(req)
{
    //alert('processAttrList. req.status is ' + req.status + ' and readyState is ' + req.readyState);
	// only if req shows "complete"
    if (req.readyState == 4) {
        // only if "OK"
        if (req.status == 200) {
        	getObject('navAttrDiv').innerHTML = req.responseText;
        } else {
            alert("There was a problem retrieving HTTP response:\n" + req.statusText);
        }
    }
    else if (req.readyState == 1) {
    	getObject('navAttrDiv').innerHTML = '<select id="navAttr"><option style="color: red;">Loading attribute names. please wait ...</option></select>';
    }
}

function processFacetContexts(req)
{
	// only if req shows "complete"
    if (req.readyState == 4) {
        // only if "OK"
        if (req.status == 200) {
        	getObject('navContext').innerHTML = req.responseText;
        } else {
            alert("There was a problem retrieving HTTP response:\n" + req.statusText);
        }
    }
}

function processChildNodes(req)
{
    //alert('process Child nodes');
	// only if req shows "complete"
    if (req.readyState == 4) {
        // only if "OK"
        if (req.status == 200) {
        	getObject(getObject('targetNode').value).innerHTML = req.responseText;
        } else {
            alert("There was a problem retrieving HTTP response:\n" + req.statusText);
        }
    }
}

function AendswithB(longString,substring) {
	var partialString = longString.substr(longString.length - substring.length)
    //alert('partialString is ' + partialString);	
	if(partialString == substring) {
	  //alert('returning true');
	  return true;
	  }
	else {
	  //alert('returning false');
	  return false;
	  }
}

function showButtonsIfReady() {
	
  if(document.getElementById('leftSelection').innerHTML!='none' && document.getElementById('rightSelection').innerHTML!='none') {
	document.getElementById('btnHider').style.display='';  
	scroll(0,0);
    }
  else {
	document.getElementById('btnHider').style.display='none';  
    }
  }

function g(id, host, projId, treeNum) {
  process_functions = new Array();
  xmlreqs = new Array();
  xmlreqs1p = new Array();
  process_functions1p = new Array();
  param_ones = new Array();

  if(treeNum==1) {
  	document.getElementById('leftTxtPath').value="";
  	document.getElementById('leftSelection').innerHTML=id;
    }
  else {
	//document.getElementById('rightTxtPath').value="";
  	document.getElementById('rightSelection').innerHTML=id;
    }
  showButtonsIfReady();
  }

function q(host, projId, treeNum) {
  g('/',host,projId, treeNum);
  }





var treeOneDrawing = false;
var treeTwoDrawing = false;

function projSel2(selector, host) {
    //alert('projectSel:' + selector.id + ' host:' + host);
	var x = 0;
	while(treeOneDrawing) {
	  x = x + 1;
  	  disableDropdowns();
	  } 
	treeTwoDrawing = true;
	disableDropdowns();
    var treeNum = 1;
//    alert('selector ' + selector.id + ' selectedIndex=' + selector.selectedIndex);
    if(selector.id=='sel2') {
    	treeNum=2;
    	document.getElementById('rightSelection').innerHTML='none';
    	//if(selector.selectedIndex>0) document.getElementById('rightTxtPath').disabled=false;
    	//else document.getElementById('rightTxtPath').disabled=true;
    	//alert('rightSelection set to ' + document.getElementById('rightSelection').innerHTML);
    }
    else {
    	document.getElementById('leftSelection').innerHTML='none';
    	if(selector.selectedIndex>0) document.getElementById('leftTxtPath').disabled=false;
    	else document.getElementById('leftTxtPath').disabled=true;
    	//alert('leftSelection set to ' + document.getElementById('leftSelection').innerHTML);
    	}
    
    showButtonsIfReady();
	process_functions = new Array();
	xmlreqs = new Array();
	xmlreqs1p = new Array();
	process_functions1p = new Array();
	param_ones = new Array();
	
	loadXMLDoc("http_get_code_tree2.jsp?t=" + treeNum + "&h=" + host + "&p=" + selector.value, "getCodeTree_" + selector.id);
	treeTwoDrawing = false;
    }

function projSel(selector, host) {
    //alert('projectSel:' + selector.id + ' host:' + host);
	var x = 0;
	while(treeTwoDrawing) {
	  x = x + 1;
  	  disableDropdowns();
	  } 
	treeOneDrawing = true;
	disableDropdowns();
    var treeNum = 1;
    //alert('selector ' + selector.id + ' selectedIndex=' + selector.selectedIndex);
    if(selector.id=='sel2') {
    	treeNum=2;
    	document.getElementById('rightSelection').innerHTML='none';
    	//if(selector.selectedIndex>0) document.getElementById('rightTxtPath').disabled=false;
    	//else document.getElementById('rightTxtPath').disabled=true;
    	//alert('rightSelection set to ' + document.getElementById('rightSelection').innerHTML);
    }
    else {
    	document.getElementById('leftSelection').innerHTML='none';
    	if(selector.selectedIndex>0) document.getElementById('leftTxtPath').disabled=false;
    	else document.getElementById('leftTxtPath').disabled=true;
    	//alert('leftSelection set to ' + document.getElementById('leftSelection').innerHTML);
    	}
    
    showButtonsIfReady();
	process_functions = new Array();
	xmlreqs = new Array();
	xmlreqs1p = new Array();
	process_functions1p = new Array();
	param_ones = new Array();
	
	loadXMLDoc("http_get_code_tree.jsp?t=" + treeNum + "&h=" + host + "&p=" + selector.value, "getCodeTree_" + selector.id);
	treeOneDrawing = false;
    }

function populateProjDropdowns(host, item1, item2, item3, item4) {
	  //alert('populateProjDropdowns');
	  process_functions = new Array();
	  xmlreqs = new Array();
	  xmlreqs1p = new Array();
	  process_functions1p = new Array();
	  param_ones = new Array();
	  disableDropdowns();
	  loadXMLDoc("http_get_proj_list.jsp?t=1&h=" + host, "getProjDropdownsFilledIn");
  }







function o(id, host, projId, treeNum) {
	activeTreeNum = treeNum;
	process_functions = new Array();
	xmlreqs = new Array();
	xmlreqs1p = new Array();
	process_functions1p = new Array();
	param_ones = new Array();
	currentId = id;
	//alert('current node id is ' + currentId);
	imgObj = getObject('img_' + id + '_' + treeNum);
	tbObj = getObject(currentId + '_k' + treeNum);
	if(AendswithB(imgObj.src,'images/spdn_closed.gif')) {
	  //alert('opening');
	  if(tbObj.innerHTML==undefined) alert('tbObj.innerHTML is undefined');
	  if((tbObj.innerHTML!=undefined) && ((tbObj.innerHTML.length == 0) ||(tbObj.innerHTML == 'loading...'))) {
		//alert('loading xml doc');
		// Encoding the id, for NI-4 (where certain paths may contain URL changing characters like ampersands
		id = escape(id);
		loadXMLDoc("http_tax_tree_nodes.jsp?action=open&catId=" + id + "&h=" + host + "&p=" + projId + "&t=" + treeNum, "getChildNodes");
	  }
	  else {
		tbObj.style.display='';
 	  }
		//alert('else changing image. innerHTML is ' + tbObj.innerHTML);
	  if(imgObj != null) {
		imgObj.src = "images/spdn_open.gif"
	  }
	  show(tbObj);
	  }
	else {
		//alert('closing. src is ' + imgObj.src);
		tbObj.style.display='none';
		imgObj.src = "images/spdn_closed.gif"
	  }
}

//get the object with the specified id
function getObject(id){
    //alert('in getObject');
	if (document.getElementById){
		//alert('1');
		return document.getElementById(id);
	}
	else if (document.all){
		//alert('2');
		return  document.all[id];
	}
	else if (document.layers){
		//alert('NN4');
		return getObjectNN4(document, id);
	}
	else{
		//alert('NULL');
		return null;
	}
}

// helper function for getObject
// get object in Netscape 4
function getObjectNN4(obj,name){
	var x = obj.layers;
	var foundLayer;
	for (var i=0;i<x.length;i++){
		if (x[i].id == name)
			foundLayer = x[i];
		else if (x[i].layers.length)
			var tmp = getObjectNN4(x[i],name);
		if (tmp) foundLayer = tmp;
	}
	return foundLayer;
}

//show an element
//unless you specify a display type, the default of "block" is used
function show(obj, displayType){
	// we can take in an id string or an object pointer
	// if it's a string, just use getObject on the id
	if (typeof(obj) == 'string') obj = getObject(obj);

	if (document.layers){
		obj.visibility  = "show";
		if (displayType != null){
			obj.display = displayType;
		}
		else{
		obj.display = "block";
		}
	}
	else if (obj.style){
		obj.style.visibility  = "visible";
		if (displayType != null){
			obj.style.display = displayType;
		}
		else{
			obj.style.display = "block";
		}
	}
	else{
		return false;
	}
}
