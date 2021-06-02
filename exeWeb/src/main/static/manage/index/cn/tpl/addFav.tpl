<!-- 新建收藏夹 -->
<div class="modal fade" id="addFav" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">添加收藏夹</h4>
      </div> 
      <div class="modal-body">
       	<div class="form-group">
            <label for="name" class="control-label">收藏夹名称:</label>
            <input type="text" class="form-control" id="name">
            <input type="hidden"  id="id">
          </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
        <button type="button" class="btn btn-primary" onclick="addFav()">保存</button>
      </div>
    </div>
  </div>
</div>