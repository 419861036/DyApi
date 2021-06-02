<section>
        <div class="content-wrapper">
            <h3>网页收集工具</h3>
            <div>
                 <h5>1、使用浏览器扩展收藏网页</h5>
                 支持：Chrome/360极速/QQ/UC/百度浏览器目前停用
                 
            </div>
            <div>
                <h5>2、网页收集小工具</h5>
	            <a href="javascript:(function()%7Bvar%20description;var%20desString=%22%22;var%20metas=document.getElementsByTagName('meta');for(var%20x=0,y=metas.length;x%3Cy;x++)%7Bif(metas%5Bx%5D.name.toLowerCase()==%22description%22)%7Bdescription=metas%5Bx%5D;%7D%7Dif(description)%7BdesString=%22&amp;amp;description=%22+encodeURIComponent(description.content);%7Dvar%20win=window.open(%22http://yunmi.ren:8080/FavWeb/manage/index/cn/webtools/webtools.html?from=webtool&amp;url=%22+encodeURIComponent(document.URL)+desString+%22&amp;title=%22+encodeURIComponent(document.title)+%22&amp;charset=%22+document.charset,'_blank');win.focus();%7D)();" onclick="alert('请把这个按钮拖到您的浏览器书签栏'); return false;" class="btn btn-primary">云收藏</a>
	            <span>(将该按钮拖动至浏览器书签栏,如下图)</span>
            </div>
         </div>
         <div class="text-center">
	       <img src="./webtools/useTool.gif" alt="">
         </div>
      </section>