<h4 class="text-gray"><i class="linecons-tag" style="margin-right: 7px;" id="{{id}}"></i>{{name}}
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
                                                         <div class="media-box-body clearfix" data-id="{{id}}" onclick="toEditFav('{{id}}')">
                                                            <p class="m0">修改收藏夹</p>
                                                            <p class="m0 text-muted">
                                                               <small>修改收藏夹的名称</small>
                                                            </p>
                                                         </div>
                                                      </div>
                                                   </a>

                                                   <a href="javascript:void(0);" class="list-group-item" >
                                                      <div class="media-box">
                                                         <div class="pull-left">
                                                            <em class="fa fa-trash fa-2x fa-fw text-danger"></em>
                                                         </div>
                                                         <div class="del-collect media-box-body clearfix" data-id="{{id}}" onclick="toDelFav('{{id}}')">
                                                            <p class="m0">删除</p>
                                                            <p class="m0 text-muted">
                                                               <small>删除收藏夹</small>
                                                            </p>
                                                         </div>
                                                      </div>
                                                   </a>
                                                </div>
                                             </li>
                                          </ul>
                                       </div>
</h4>