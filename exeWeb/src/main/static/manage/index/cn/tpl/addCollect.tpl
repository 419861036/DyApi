<div class="modal fade" id="addCollect" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="wrapper">
      <div class="block-center mt-xl wd-xl">
         <div class="panel panel-dark panel-flat">
            <div class="panel-heading text-center">
               <a href="/" style="color:#fff;">Favorites</a>
            </div>
            <div class="panel-body">
              <form id="collect-form" class="form-horizontal" onsubmit="return false">
               <div id="show1">
               <div class="media mb">
                  <a id="logoA" href="javascript:void(0);" target="_blank" class="pull-left">
                      <div class="media-object resource-card-image-small-loading" style="background-image:url('/img/loading.gif')"></div>
                  </a>
                  <div class="media-body">
                     <h4 class="media-heading resource-card-title-small form-control-static" id="titleShow"></h4>
                  </div>
               </div>
               </div>
               <div id="show2" style="display:none;">
               		<div class="mb">
                     标题
                     <textarea name="title" id="title" class="form-control" cols="30" rows="2" placeholder="留空则自动抓取"></textarea>
                  </div>
                  <div class="mb">
                     简介
                     <textarea name="description" id="description" class="form-control" cols="30" rows="2" placeholder="留空则自动抓取"></textarea>
                  </div>
                  <div class="mb">
                     图片链接地址
                     <textarea name="logoUrl" id="logoUrl" class="form-control" cols="30" rows="2" placeholder="留空则自动抓取" value=""></textarea>
                  </div>
                   <div class="mb">
                       所属类别
                       <select name="category" id="category" class="form-control" >
                           <option value="">--请选择--</option>
                           <option value="TRAVEL">旅行</option>
                           <option value="FOOD">料理</option>
                           <option value="EXERCISE">健身</option>
                           <option value="PHOTOGRAPH">摄影</option>
                           <option value="MUSIC">音乐</option>
                           <option value="REARING">育儿</option>
                           <option value="LOVE">恋爱</option>
                           <option value="BUSINESS">创业</option>
                           <option value="ART">美术设计</option>
                           <option value="MANAGER">产品经理</option>
                           <option value="MARKET">市场营销</option>
                           <option value="RUNNING">运营</option>
                       </select>
                   </div>
               </div>
               
                  <div class="mb">
                     收藏备注
                     <textarea name="remark" id="remark" class="form-control" cols="30" rows="2" placeholder="请输入收藏备注，也可以在这里@好友"></textarea>
                  </div>
                  <div class="pull-right dropdown dropdown-list">
                     <a href="javascript:void(0);" id="atshow" data-toggle="dropdown">
                        at好友 <i class="fa fa-caret-down"></i>
                     </a>
                     <ul class="dropdown-menu">
                        <li>
                            <!--<a href="javascript:void(0);" th:each="name : ${followList}" th:onclick="'javascript:showAt(\''+${name}+'\');'" th:text="${name}">好友名称</a>-->
                            
                        </li>
                     </ul>
                  </div>
                  <label style="font-weight:normal;margin-bottom:0;">
                  <input id="type" type="checkbox" name="type" class="mb" value="PRIVATE" /> 私密收藏</label>
               <p class="pt-lg text-center">选择一个收藏夹</p>
               <div class="collect-sort-folder">
                  <div class="collect-tab-head">
                      <a href="javascript:void(0);">智能推荐</a>
                      <a href="javascript:void(0);">我的收藏夹</a>
                  </div>
                  <div class="collect-tab-body">
                      <div class="wrap">
                          <div id="smartFavoritesList" class="sort">
                          </div>
                      </div>
                      <div class="wrap">
                          <div class="folder">
                               <select  name="favoritesId" id="favoritesId" class="mb form-control">
                               </select>
                               <p class="text-center">或者</p>
                               <input type="text" name="newFavorites" id="newFavorites" class="mb form-control" placeholder="新建分类收藏夹"/>
                          </div>
                      </div>
                  </div>
               </div>
               <button id="collect" class="mt-lg btn btn-primary btn-block">提交</button>
               <input type="hidden" name="url" id="url" value=""/>
               <input type="hidden" name="charset" id="charset" value=""/>
               </form>
               <p></p>
               <div id="errorMsg" class="alert alert-danger text-center" style="display:none;"></div>
            </div>
         </div>
         <!--<div class="text-right mt-sm" id="model1" >
            <a id="changeModel1" href="javascript:void(0);" style="color:#666;text-decoration:none;"> <i class="fa fa-refresh"></i>
               	切换成专家模式
            </a>
         </div>-->
         <div class="text-right mt-sm" id="model2" style="display:none;">
            <a id="changeModel2" href="javascript:void(0);" style="color:#666;text-decoration:none;"> <i class="fa fa-refresh"></i>
               	切换成简单模式
            </a>
         </div>
         <!-- <div class="text-center mb mt-lg">
            <a href="#">
               <img src="https://spoon-static.b0.upaiyun.com/app/img/collect_wechat.png" width="100px" height="50px" alt=""/></a>
            <a href="#">
               <img src="https://spoon-static.b0.upaiyun.com/app/img/collect_wechat_moment.png" width="100px" height="50px" alt=""/></a>
            <a href="#">
               <img src="https://spoon-static.b0.upaiyun.com/app/img/collect_qq.png" width="100px" height="50px" alt=""/></a>
         </div> -->
         <div class="p-lg text-center">
            <span>&copy;</span>
            <span>2016</span>
            <span>-</span>
            <span>2018</span>
            <br/>
            <span>cloudfavorites@126.com</span>
         </div>
      </div>
   </div>
</div>