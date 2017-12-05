var serverIp='localhost';

function downlaodFile(id,feature)  //Download Document
		{
			alert(id);
			window.location.href ='http://'+serverIp+':8080/UDSDocuManager/DocumentList?dataID='+id+'&action='+feature;
			//window.open('http://localhost:8080/UDSDocuManager/DocumentList?dataID='+id+'feature='+feature);
		}
		
function dispalayFile(id,feature){   //View Document in Browser 
	//if(document.getElementById("Display").selected)
    {
    	//alert("Dispaly")
    	//alert(id);
		//window.location.href ='http://localhost:8080/UDSDocuManager/DocumentList?dataID='+id;
		window.open('http://'+serverIp+':8080/UDSDocuManager/DocumentList?dataID='+id+'&action='+feature);
    }
}
		
function copyFile(checkedValue,Copy)  //Copy Document from one folder to another 
{
	var	w = window.open('http://'+serverIp+':8080/UDSDocuManager/copy.html?dataID='+checkedValue+'&action='+Copy,'copyFile','directories=no,titlebar=no,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=500,height=300');

	if ( w.focus )
	{
		w.focus();
	}

}
		
function moveFile(checkedValue,move)  //Move File From One Folder to another
{
	var	w = window.open('http://'+serverIp+':8080/UDSDocuManager/move.html?dataID='+checkedValue+'&action='+move,'moveFile','directories=no,titlebar=no,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=500,height=300');

	if ( w.focus )
	{
		w.focus();
	}
	var timer = setInterval(function() {   
	    if(w.closed) {  
	    	window.location.reload(true);
	        clearInterval(timer);  
	        //alert('closed');  
	    }  
	}, 1000);

}
		
function deleteFile(checkedValue,Delete)  //Delete Document Request
{
	var	w=window.open ('http://'+serverIp+':8080/UDSDocuManager/DocumentList?dataID='+checkedValue+'&action='+Delete,+'deleteFile','directories=no,titlebar=no,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=500,height=300');
	if ( w.focus )
	{
		w.focus();
	}
	var timer = setInterval(function() {   
	    if(w.closed) {  
	    	window.location.reload(true);
	        clearInterval(timer);  
	        //alert('closed');  
	    }  
	}, 1000);
}


function sendEmail(dataids)
{
	var	w=window.open('http://localhost:8080/UDSDocuManager/EmailForm.jsp?dataids='+dataids,'sendmail','directories=no,titlebar=no,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=500,height=500');
	if ( w.focus )
	{
		w.focus();
	}
}

function Exclude(checkedValue,Exclude) //Exclude documents
{
	/*var w=window.open('http://'+serverIp+':8080/UDSDocuManager/DocumentList?dataID='+checkedValue+'&action='+Exclude,+'ExcludedNodes','directories=no,titlebar=no,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=500,height=300');	
	if ( w.focus )
	{
		w.focus();
	}
	var timer = setInterval(function() {   
	    if(w.closed) {  
	    	window.location.reload(true);
	        clearInterval(timer);  
	        //alert('closed');  
	    }  
	}, 1000);*/
	
	    //document.getElementById('container').innerHTML = '';
	    //openModal();
		
	    var xhr = false;
	    if (window.XMLHttpRequest) {
	    	
	        xhr = new XMLHttpRequest();
	    }
	    else {
	    	
	        xhr = new ActiveXObject("Microsoft.XMLHTTP");
	    }
	    if (xhr) {
	        xhr.onreadystatechange = function () {
	            if (xhr.readyState == 4 && xhr.status == 200) {
	                
	            	window.location.reload(true);
	            }
	        }
	        xhr.open("GET", 'DocumentList?dataID='+checkedValue+'&action='+Exclude,+'ExcludedNodes', true);
	        xhr.send(null);
	    }
		
}

function Retrieve(checkedValue,Retrieve) //Retrieve documents
{
	var w=window.open('http://'+serverIp+':8080/UDSDocuManager/DocumentList?dataID='+checkedValue+'&action='+Retrieve,+'ExcludedNodes','directories=no,titlebar=no,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=500,height=300');	
	if ( w.focus )
	{
		w.focus();
	}
	var timer = setInterval(function() {   
	    if(w.closed) {  
	    	window.location.reload(true);
	        clearInterval(timer);  
	        //alert('closed');  
	    }  
	}, 1000);
	
}

			
//Jquery Datatables Customization		
/*$(document).ready(function() {
	$("#tabs").tabs( {
		"show": function(event, ui) {
			var oTable = $('div.dataTables_scrollBody>table.browseTable', ui.panel).dataTable();
			if ( oTable.length > 0 ) {
				oTable.fnAdjustColumnSizing();
			}
		}
	} );
} );
	*/
	/*$('table.browseTable').dataTable( {
		"columnDefs": [
    	    { 
    	    	"orderable": false, "targets": [0,1,2,3,5] ,
    	    	
    	    } ],
    	    "scrollY": "290px",
	    	"scrollCollapse": true
		
	} );
	

*/

/*$(document).ready(function() {
    $('#CScontent').DataTable( {
        
    	"columnDefs": [
    	    { "orderable": false, "targets": [0,1,2,3,5] ,
    	    	
    	    } ],
    	    "scrollY": "290px",
	    	  "scrollCollapse": true 	   
    } );
} );*/

/*$(document).ready(function() {
    $('#Excludecontent').DataTable( {
        
    	"columnDefs": [
    	    { "orderable": false, "targets": [0,1,2,3,5] ,
    	    	
    	    } ],
    	    "scrollY": "290px",
	    	  "scrollCollapse": true 	   
    } );
} );
*/



//All Dropdown Functionality
function downloadAllFiles()
{
	
	if((document.getElementById("Scan").selected))
	{
		AcquireImage();
		
		var opt = {
		        autoOpen: false,
		        modal: true,
		        width: 350,
		        height:450,
		        title: 'Document Scan'
		};var $body = $(window.frameElement).parents('body'),
           dlg = $body.find('#dialog');
           dlg.dialog(opt).dialog("open");	
	}
	
	if((document.getElementById("Display").selected))
	{
		var chk=0;
		var checkedValue = null; 
		inputElements = document.getElementsByName('foo');
		for(var i=0; inputElements[i]; ++i)
		{
			if(inputElements[i].checked)
		    {
				chk=chk+1;
		    }
		  }
		if(chk>1)
		{
			alert("Multiple Documents can not be displayed");
		}
		if(chk==0)
		{
			alert("Please select atleast one")
		}
		if(chk==1)
		{
			for(var i=0; inputElements[i]; ++i)
			{
				if(inputElements[i].checked)
			    {
		           checkedValue = inputElements[i].value;
		           dispalayFile(checkedValue,'Display')
		           //alert(checkedValue)
			    }
			}
			inputElements=null;
		}
	}
if((document.getElementById("Send").selected))
	{
		var checkedValue = null; 
		var dataids=[];
		var inputElements = document.getElementsByName('foo');
		for(var i=0; inputElements[i]; ++i)
		{
		      if(inputElements[i].checked)
		      {
		           checkedValue = inputElements[i].value;
		           //dispalayFile(checkedValue,'Send')
		           dataids[i]= checkedValue;    
		      }
		}
		sendEmail(dataids)
}
	if((document.getElementById("Copy").selected))
	{
		var chk=0;
		var checkedValue = null; 
		inputElements = document.getElementsByName('foo');
		for(var i=0; inputElements[i]; ++i)
		{
			if(inputElements[i].checked)
		    {
				chk=chk+1;
		    }
		  }
		if(chk>1)
		{
			alert("Multiple Documents can not be Copied");
		}
		if(chk==0)
		{
			alert("Please select atleast one")
		}
		if(chk==1)
		{
			for(var i=0; inputElements[i]; ++i)
			{
				if(inputElements[i].checked)
			    {
		           checkedValue = inputElements[i].value;
		           copyFile(checkedValue,'Copy')
		           //alert(checkedValue)
			    }
			}
		inputElements=null;
		}
	}
	
	if((document.getElementById("Move").selected))
	{
		var chk=0;
		var checkedValue = null; 
		inputElements = document.getElementsByName('foo');
		for(var i=0; inputElements[i]; ++i)
		{
			if(inputElements[i].checked)
		    {
				chk=chk+1;
		    }
		  }
		if(chk>1)
		{
			alert("Multiple Documents can not be Moved");
		}
		if(chk==0)
		{
			alert("Please select atleast one")
		}
		if(chk==1)
		{
			for(var i=0; inputElements[i]; ++i)
			{
				if(inputElements[i].checked)
			    {
		           checkedValue = inputElements[i].value;
		           moveFile(checkedValue,'Move')
		           //alert(checkedValue)
			    }
			}
		inputElements=null;
		}
	}
	
	if((document.getElementById("Upload").selected))
	{ 
		/*dialog=$( "#uploadDialog" ).dialog({
		      autoOpen: false,
		      modal: true,
		      width: 350,
		      height:450,
		      title: 'Document Scan'
		    });
		dialog.dialog( "open" );*/
		
		/*var opt = {
		        autoOpen: false,
		        modal: true,
		        width: 400,
		        height:350,
		        title: 'Document Upload'
		};
		
			
			var $body = $(window.frameElement).parents('body'),
           dlg = $body.find('#uploadDialog');
           dlg.dialog(opt).dialog("open");*/
		
		var RequestNumber=window.location.search.substr(1);
		var	page = 'http://'+serverIp+':8080/UDSDocuManager/upload.jsp?'+RequestNumber;
		var $dialog=$('<div></div>')
		.html('<iframe  width="100%" height="100%" src="'+page+'"scrolling="no" ></iframe>')
		.dialog({
			  autoOpen: false,
		      modal: true,
		      width: 500,
		      height:300,
		      title: 'Document upload',
		      open:function(event,ui){$('body').css('overflow','hidden');$('ui-widget-overlay').css('width','100%');}
		});
         $dialog.dialog("open");  
		/*var $body = $(window.frameElement).parents('body'),
        dlg = $body.find('#uploadDialog');
        dlg.dialog.dialog("open");*/
		
		
	}
	
	if((document.getElementById("Exclude").selected))
	{
		
		var chk=0;
		var checkedValue = []; 
		inputElements = document.getElementsByName('foo');
		for(var i=0; inputElements[i]; ++i)
		{
			if(inputElements[i].checked)
		    {
				chk=chk+1;
		    }
		}
		
		if(chk==0)
		{
			alert("Please select atleast one")
		}
		if(chk>=1)
		{
			for(var i=0; inputElements[i]; ++i)
			{
				if(inputElements[i].checked)
			    {
		           checkedValue[i] = inputElements[i].value;
		          
		           //alert(checkedValue)
			    }
			}
			
		Exclude(checkedValue,'Exclude')
		inputElements=null;
		}
	}
	
	if((document.getElementById("Retrieve").selected))
	{
		var chk=0;
		var checkedValue = []; 
		inputElements = document.getElementsByName('foo');
		for(var i=0; inputElements[i]; ++i)
		{
			if(inputElements[i].checked)
		    {
				chk=chk+1;
		    }
		}
		
		if(chk==0)
		{
			alert("Please select atleast one")
		}
		if(chk>=1)
		{
			for(var i=0; inputElements[i]; ++i)
			{
				if(inputElements[i].checked)
			    {
		           checkedValue[i] = inputElements[i].value;
		          
		           //alert(checkedValue)
			    }
			}
			
		Retrieve(checkedValue,'Retrieve')
		inputElements=null;
		}
	}

}	


		
//Check Box Functionality to select all		
function toggle(source) 
{
	checkboxes = document.getElementsByName('foo');
	for(var i=0, n=checkboxes.length;i<n;i++) 
	{
		checkboxes[i].checked = source.checked;
	}
}


$(document).ready(function() {
    $("#tabs").tabs( {
        "activate": function(event, ui) {
        	 var active = $('#tabs').tabs('option', 'active');
            var table = $.fn.dataTable.fnTables(true);
            if ( table.length > 0 ) {
                $(table).dataTable().fnAdjustColumnSizing();
            }
        }
    } );
     
    $('table.browseTable').dataTable( {
    	"aaSorting": [[4, 'asc']],
        "sScrollY": "290px",
        "bScrollCollapse": true,
        "bPaginate": true,
        "bJQueryUI": true,
        "aoColumnDefs": [
        	
        	{ "orderable": false, "targets": [0,1,2,3,5]}]
        	
        				
    } );
} );
	
//Dyno soft scanning code

/*$( function dialog()  {
	$(document).ready(function(){
		$( "#dialog" ).dialog("open");
		} );
	})*/


$('.waitMe_ex_close').click(function(){
	$('.containerBlock > form').waitMe('hide');
	$('#waitMe_ex2').waitMe('hide');
	$('#waitMe_ex3').waitMe('hide');
});

var current_effect = 'bounce';
function spin()
{
	run_waitMe(current_effect);
}
		
function run_waitMe(effect){
$('#container').waitMe({
effect: 'roundBounce',
text: '',
g: 'rgba(255,255,255,0.7)',
color: '#000',
maxSize: '',
waitTime: -1,
source: '',
textPos: 'vertical',
ontSize: '',
onClose: function() {}});
}

function validateForm()
{
	if (!document.getElementById("fileChooser").value) {
        alert("Please choose a file!");
        return false;    
    }
	if((document.getElementById('docType').value).trim()=="")
	{
	 	alert("Please Enter Document Type..!");
        return false;
	}
	if((document.getElementById('subNum').value).trim()=="")
	{
	 	alert("Please Enter Subscription Number..!");
        return false;
	}
	
	spin();				
}

var current_effect = 'bounce'; // 



function spin()
{
	run_waitMe(current_effect);
	}
		
function run_waitMe(effect){
$('#container').waitMe({
effect: 'roundBounce',
text: '',
g: 'rgba(255,255,255,0.7)',
color: '#000',
maxSize: '',
waitTime: -1,
source: '',
textPos: 'vertical',
ontSize: '',
onClose: function() {}});
}


		