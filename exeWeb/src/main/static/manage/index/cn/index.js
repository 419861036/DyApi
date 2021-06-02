 var basePath="/FavWeb/";
    
    function toDelFav(id){
    	var x=tplLoader.load("tpl/delFav.tpl",{});
    	$("#htmlTpl").html(x);
    	$("#delFav").modal('show');
    	$("#id").val(id);
    }
    function delFav(){
    	var ids = new Array();
		ids.push($("#id").val());
		var url=basePath+"center/fav/favorites/delFavFavorites.do";
		PostDataJson(url, {
			ids : ids
		}, function(res){
			$("#msg_tip .modal-content").html(res.msg);
			$("#msg_tip").modal('show');
		});
    }
    

    function toEditFav(id){
    	var x=tplLoader.load("tpl/addFav.tpl",{});
    	$("#htmlTpl").html(x);
    	$("#addFav").modal('show');
    	$("#id").val(id);
    }
    function addFav(id){
    	var inputs=$("#addFav").find("input");
    	var data = {};
    	$.each(inputs,function(a,b){
    		var key=$(b).attr("id");
    		var val=$(b).val();
    		data[key]=val;
    	});
    	var id=$("#id").val();
    	var url=id?basePath +"center/fav/favorites/updateFavFavorites.do":basePath +"center/fav/favorites/addFavFavorites.do";
    	var x=tplLoader.load("tpl/msg_tip.tpl",{});
		$("#htmlTpl").html(x);
    	if(false){
    		
    		$("#msg_tip .modal-content").html("添加成功");
			$("#msg_tip").modal('show');
			return;
    	}
    	PostDataJsonObj(url, JSON.stringify(data), function(res){
    		if(res && res.code == 200) {
    			$("#msg_tip .modal-content").html("添加成功");
    			$("#msg_tip").modal('show');
    			$("#addFav").hide();
    		}else{
    			$("#msg_tip .modal-content").html("添加成功");
    			$("#msg_tip").modal('show');
    		}
    	});
    }
    
    function getFavList(fid,name){
    	var itemsPerPage=100;
		var data = {
        		page:1,
        		fid:fid,
        		itemsPerPage:itemsPerPage
        	};
		url=basePath+"center/fav/collect/findMyFavCollectPageList.do";
    	PostDataJson(url, data, function(res){
    		var i=0;
    		var all="";
    		var content ="";
    		$.each(res.v.collection,function(a,b){
    			var x=tplLoader.load("tpl/cell.tpl",b);
	    		content = content+ x;
	    		var cols=2;
    			if(i%cols==cols-1){
					content='<div class="row">'+content+'</div>'
					all=all+content;
					content="";
    			}
    			i++;
    		});
    		
    		if(content){
    			content='<div class="row">'+content+'</div>'
				all=all+content;
				content="";
    		}
    		var dataTpl={id:fid,name:name};
    		var c=tplLoader.load("tpl/rightModule.tpl",dataTpl);
    		all=c+all;
    		$("#favList").html(all);
    		
    		 $("#favList img.checkErr").bind("error",function(err){
    			  $(this).unbind('error');  
    			  var defaultSrc=basePath+"manage/index/cn/webtools/static/img/logo.jpg";
    			  $(this).attr("src",defaultSrc);
    			 });
//     		$('.dropdown').on('show.bs.dropdown', function () {
//     			event.stopPropagation();
//     			})
//     		$(".dropdown").click(function(){
//     			 $(this).dropdown('toggle')
//     			   $(this).addClass("open");
//     			 event.stopPropagation();
//     		});
//    		$(".xe-user-name").click(function(){
//    			var url=$(this).attr("data-url");
//    			window.open(url,"_blank");
//    		});
    		$(".mod-collect").click(function(){
    			var id=$(this).attr("data-id");
    			toEditCollect(id);
    		});
    		$(".del-collect").click(function(){
    			var id=$(this).attr("data-id");
    			toDelCollect(id)
    		});
    	});
	}
    
    function toDelCollect(id){
    	var x=tplLoader.load("tpl/delCollect.tpl",{});
    	$("#htmlTpl").html(x);
    	$("#delCollect").modal('show');
    	$("#id").val(id);
    }
    function delCollect(){
    	var ids = new Array();
		ids.push($("#id").val());
		var url=basePath+"center/fav/collect/delFavCollect.do";
		PostDataJson(url, {
			ids : ids
		}, function(res){
			$("#msg_tip .modal-content").html(res.msg);
			$("#msg_tip").modal('show');
		});
    }
    
    function toEditCollect(id){
    	var x=tplLoader.load("tpl/addCollect.tpl",{});
    	$("#htmlTpl").html(x);
    	$("#addCollect").modal('show');
    	$("#id").val(id);
    }
    function editCollect(id){
    	var inputs=$("#addCollect").find("input");
    	var data = {};
    	$.each(inputs,function(a,b){
    		var key=$(b).attr("id");
    		var val=$(b).val();
    		data[key]=val;
    	});
    	var id=$("#id").val();
    	var url=id?basePath +"center/fav/favorites/updateFavFavorites.do":basePath +"center/fav/favorites/addFavFavorites.do";
    	
    	if(false){
    		$("#msg_tip .modal-content").html("添加成功");
			$("#msg_tip").modal('show');
			return;
    	}
    	PostDataJsonObj(url, JSON.stringify(data), function(res){
    		if(res && res.code == 200) {
    			$("#msg_tip .modal-content").html("添加成功");
    			$("#msg_tip").modal('show');
    			$("#addFav").hide();
    		}else{
    			$("#msg_tip .modal-content").html("添加成功");
    			$("#msg_tip").modal('show');
    		}
    	});
    }
    
    function getMenu(){
		var url=basePath+"center/fav/favorites/findMyFavFavoritesPageList.do";
    	var itemsPerPage=100;
    	var data = {
    		page:1,
    		itemsPerPage:itemsPerPage
    	};
    	PostDataJson(url, data, function(res){
    		$.each(res.v.collection,function(a,b){
    			var c=tplLoader.load("tpl/moduleHead.tpl",b);
//                 var c='<li><a href="javascript:getFavList(\''+b.id+'\',this)" data-name="'+b.name+'" class="smooth"><i class="linecons-star"></i><span class="title">'+b.name+'</span></a></li>';
    			$("#public_fav").append(c);
    		});
    	});
	}
    
    function toGenWem(url){
    	var src=basePath+"center/fav/webtools/genEwm.do?url="+url;
    	var x=tplLoader.load("tpl/showEwm.tpl",{src:src,url:url});
    	$("#htmlTpl").html(x);
    	$("#showEwm").modal('show');
    }
    
    $(document).ready(function() {
    	getMenu();
    	getFavList('','所有')
    	$("#webCollectTool").click(function(){
    		var content=tplLoader.load("tpl/webCollectToolTpl.tpl",{});
    		$("#favList").html(content);
    	});
    	
    	
    	
        $(document).on('click', '.has-sub', function(){
            var _this = $(this)
            if(!$(this).hasClass('expanded')) {
               setTimeout(function(){
                    _this.find('ul').attr("style","")
               }, 300);
              
            } else {
                $('.has-sub ul').each(function(id,ele){
                    var _that = $(this)
                    if(_this.find('ul')[0] != ele) {
                        setTimeout(function(){
                            _that.attr("style","")
                        }, 300);
                    }
                })
            }
        })
        $('.user-info-menu .hidden-sm').click(function(){
            if($('.sidebar-menu').hasClass('collapsed')) {
                $('.has-sub.expanded > ul').attr("style","")
            } else {
                $('.has-sub.expanded > ul').show()
            }
        })
        $("#main-menu li ul li").click(function() {
            $(this).siblings('li').removeClass('active'); // 删除其他兄弟元素的样式
            $(this).addClass('active'); // 添加当前元素的样式
        });
        $("a.smooth").click(function(ev) {
            //ev.preventDefault();

            public_vars.$mainMenu.add(public_vars.$sidebarProfile).toggleClass('mobile-is-visible');
            ps_destroy();
            if($($(this).attr("href")).offset()){ 
            	  $("html, body").animate({
                      scrollTop: $($(this).attr("href")).offset().top - 30
                  }, {
                      duration: 500,
                      easing: "swing"
                  });
            }
          
        });
        return false;
    });

    var href = "";
    var pos = 0;
    $("a.smooth").click(function(e) {
        $("#main-menu li").each(function() {
            $(this).removeClass("active");
        });
        $(this).parent("li").addClass("active");
        //e.preventDefault();
        href = $(this).attr("href");
        if($(href).position()){
        	pos = $(href).position().top - 30;
        }
    });