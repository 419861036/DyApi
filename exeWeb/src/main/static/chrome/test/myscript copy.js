var script = document.createElement("script"); 
script.textContent ="dnshn_maxnum='999991'";

if(document.head){
	document.head.appendChild(script); 
}
document.addEventListener('DOMContentLoaded', fireContentLoadedEvent, false);
window.onload(function(){
	console.log("window");
});
function fireContentLoadedEvent () {
	console.log("test2");
    //console.log ("DOMContentLoaded");

    // PUT YOUR CODE HERE.
    //document.body.textContent = "Changed this!

	// Checking page title'
	
	// if (document.title.indexOf("STATIC DNS SERVER") != -1||true) {
	// 	console.log("test3");
	// 	var script = document.createElement("script"); 
	// 	script.textContent ="dnshn_maxnum='99999'";
		
	// 	if(document.head){
	// 		document.head.appendChild(script); 
	// 	}

	// }

}
