
<script language="javascript">
$(function(){
	$('#edit_form').ajaxForm(function(data){
		var json = eval(data);
		if (json.result == 'success')  {
			alert('保存成功');
			loadEdit('${path}');
		} else {
			alert(json.context.actionErrors.join('\n'));
		}
	});
	
	$('#edit_btn').click(function(){
		$('#editData').prop("disabled", false);
	});
});
</script>

<div>
<form id="edit_form" role="form" class="form-horizontal" action="edit" method="POST">
	<div class="form-group">
    	<label class="control-label"><h4>路径：${path}</h4></label>
  	</div>
  	<input name="path" value="${path}" type="hidden"/>
  	<input name="csrf_token" value="${Session.csrf_session_token?default('')}" type="hidden"/>
    <textarea name="data" id="editData" class="form-control" rows="20" disabled="true">${data?default('')}</textarea>
    <br/>
   	 	<a type="input" id="edit_btn" class="btn btn-primary">编辑</a>
    	<button type="submit" class="btn btn-success">保存</button
</form>
</div>
