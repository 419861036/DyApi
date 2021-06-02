<div class="col-sm-6">
	<div  style="cursor:default;" class="xe-widget xe-conversations box2 label-info" data-url="{{url}}" data-toggle="tooltip" data-placement="bottom" title="" data-original-title="">
<div class="pull-right dropdown dropdown-list">
                                          <a href="#" data-toggle="dropdown" class="sharing-more-button">
                                             <span class="fa fa-angle-down"></span>
                                          </a>
                                          <ul class="dropdown-menu animated bounceIn">
                                             <li>
                                                <div class="list-group">
                                                   <a href="javascript:void(0);" class="list-group-item" >
                                                      <div class="media-box">
                                                         <div class="pull-left">
                                                            <em class="fa fa-pencil-square-o fa-2x fa-fw text-info"></em>
                                                         </div>
                                                         <div class="mod-collect media-box-body clearfix" data-id="{{id}}">
                                                            <p class="m0">修改收藏</p>
                                                            <p class="m0 text-muted">
                                                               <small>修改收藏的各种属性</small>
                                                            </p>
                                                         </div>
                                                      </div>
                                                   </a>
                                                   
                                                   <a href="javascript:void(0);" class="list-group-item" >
                                                      <div class="media-box">
                                                         <div class="pull-left">
                                                            <em class="fa fa-pencil-square-o fa-2x fa-fw text-info"></em>
                                                         </div>
                                                         <div onclick="toGenWem('{{url}}')" class="media-box-body clearfix" data-id="{{id}}">
                                                            <p class="m0">二维码</p>
                                                            <p class="m0 text-muted">
                                                               <small>生成二维码方便手机查看</small>
                                                            </p>
                                                         </div>
                                                      </div>
                                                   </a>

                                                   <a href="javascript:void(0);" class="list-group-item" >
                                                      <div class="media-box">
                                                         <div class="pull-left">
                                                            <em class="fa fa-trash fa-2x fa-fw text-danger"></em>
                                                         </div>
                                                         <div class="del-collect media-box-body clearfix" data-id="{{id}}">
                                                            <p class="m0">删除</p>
                                                            <p class="m0 text-muted">
                                                               <small>该分享会永久删除</small>
                                                            </p>
                                                         </div>
                                                      </div>
                                                   </a>
                                                </div>
                                             </li>
                                          </ul>
                                       </div>
		<div class="xe-comment-entry">
			<a class="xe-user-img">
				<img src="{{logoUrl}}" class="checkErr" style="width:100px;height:60px;"/>
			</a>
			<div class="xe-comment">
				<a href="{{url}}" target="_blank" style="display:inline;" class="xe-user-name overflowClip_1" data-url="{{url}}">
					<strong>{{title}}</strong>
				</a>
				<p class="overflowClip_2">{{remark}}</p>
			</div>
		</div>
	</div>
</div>