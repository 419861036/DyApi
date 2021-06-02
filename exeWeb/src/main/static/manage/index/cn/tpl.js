var tplLoader={};
tplLoader.tpls={};
tplLoader.load = function(url,data){
	var tpl=tplLoader.tpls[url];
	 if(!tpl){
	    $.ajax({
	        url: url, 
	        type: "GET", 
	        async:false,
	        success: function (data) {
	            var tmpl = template.compile(data);
	            tplLoader.tpls[url] = tmpl;
	            tpl=tmpl;
	        }
	    });
    }
	 return tpl(data);
};
