console.log("test");
var script = document.createElement("script"); 
script.textContent ="dnshn_maxnum='999991'";
document.documentElement.appendChild(script);
//保证最早执行
//https://www.it1352.com/818014.html

// var s = document.createElement（“ script”）;
// s.src =“ http://ajax.googleapis.com/ajax/libs/jquery/2.1.0/jquery.min.js”；
// s.async = false;
// document.documentElement.appendChild（s）;