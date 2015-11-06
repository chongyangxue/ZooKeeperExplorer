<script language="javascript">
$(function(){
	$('#add_form').ajaxForm(function(data){
		var json = eval(data);
		if (json.result == 'success')  {
			alert('保存成功');
			document.location.reload();
		} else {
			alert(json.context.actionErrors.join('\n'));
		}
	});
});
</script>

<div>

<form id="add_form" class="form-horizontal" action="add" method="POST">
  	<div class="form-group">
	  <label class="col-sm-2 control-label" for="parent">父节点</label>
	  <div class="col-sm-10">
	  	<input id="parent" name="parent" type="text" class="form-control input-sm" readonly value=${parent}>
	  </div>
	</div>
	
	<div class="form-group">
	  <label class="col-sm-2 control-label">节点名称</label>
	  <div class="col-sm-10">
	  	<input id="path" name="path" type="text" class="form-control input-sm" placeholder="请输入要创建的节点...">
	  </div>
	</div>

    <div class="form-group">
     <label class="col-sm-2 control-label">权限类型</label>
     <div class="col-sm-10">	
    	<select id="auth" name="auth" class="form-control input-sm" data-placeholder="请选择要分配的项目">
    		<option value="CREATOR_ALL_ACL">CREATOR_ALL_ACL</option>
    		<option value="OPEN_ACL_UNSAFE">OPEN_ACL_UNSAFE</option>
    		<option value="READ_ACL_UNSAFE">READ_ACL_UNSAFE</option>
    	</select>
     </div>
    </div>
    
	<div class="form-group">
     <label class="col-sm-2 control-label" >节点属性</label>
     <div class="col-sm-10">
		<select id="mode" name="mode" class="form-control input-sm" data-placeholder="请选择要分配的项目">
    		<option value="PERSISTENT">PERSISTENT</option>
    		<option value="PERSISTENT_SEQUENTIAL">PERSISTENT_SEQUENTIAL</option>
    		<option value="EPHEMERAL">EPHEMERAL</option>
    		<option value="EPHEMERAL_SEQUENTIAL">EPHEMERAL_SEQUENTIAL</option>
   	 	</select>
     </div>
    </div>
    <textarea name="data" class="form-control" style="width:90%;height:350px;margin-left:80px"></textarea>
    <br>
    <button type="submit" class="btn btn-primary" style="margin-left:80px">保存</button>
</form>
	
</div>
