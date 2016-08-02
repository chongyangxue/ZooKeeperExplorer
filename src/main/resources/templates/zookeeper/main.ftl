<head>
	<meta charset="utf-8">
    <title>ZooKeeper Explorer</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

	<link href="/css/bootstrap.css" rel="stylesheet">
    <link href="/css/bootstrap-responsive.css" rel="stylesheet">
	<link href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap-glyphicons.css" rel="stylesheet">
    <script type="text/javascript" src="/js/jquery.min.js"></script>
    <script type="text/javascript" src="/js/bootstrap.js"></script>
    <script src="/js/main.js"></script>
    <script src="/js/jquery-form.js"></script>
    <script src="/js/jquery.validate.js"></script>
    <script src="/js/jquery.validate_cn.js"></script>
    <style type="text/css">
      body {
        padding-top: 15px;
        padding-bottom: 40px;
      }
	.navbar {
    	background-color: #5bc0de;
    	color:#ffffff;
    	border-radius:0;
	}
	.badge{
		background-color: #D9534F;
	}
	
    </style>
     
 <script language="javascript">
	jQuery('ul.nav li.dropdown').hover(function() {
  		jQuery(this).find('.dropdown-menu').stop(true, true).delay(200).fadeIn();
	}, function() {
  		jQuery(this).find('.dropdown-menu').stop(true, true).delay(200).fadeOut();
	});
	
	$(function(){
		var forms = $('form');
		for(var i=0;i<forms.length;i++){
   			if(forms[i].method.toLowerCase() == "post"){
   				var tokenInput = document.createElement("input"); 
   				tokenInput.type="hidden";   
				tokenInput.name="csrf_token";
				tokenInput.value="${Session.csrf_session_token?default('')}";
				forms[i].appendChild(tokenInput);
   			}
		}
	});
	
</script>
</head>
<script type="text/javascript" src="/js/jquery.ztree.all-3.5.js"></script>
<link href="/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
<script language="javascript">
var setting = {
	callback: {
				onClick: onClick,
				onRightClick : onRightClick,
				beforeExpand: beforeExpand
			}
};
var znodes = [
	<@printTree tree />	
];

function beforeExpand(treeId, treeNode) {
    singlePath(treeNode);
}

function singlePath(currNode) {
    var cLevel = currNode.level;
    var cId = currNode.id;

    var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
    //展开的所有节点，这是从父节点开始查找（也可以全文查找）
    var expandedNodes = treeObj.getNodesByParam("open", true, currNode.getParentNode());
    for(var i = expandedNodes.length - 1; i >= 0; i--){
        var node = expandedNodes[i];
        var level = node.level;
        var id = node.id;
        if (cId != id && level == cLevel) {
            treeObj.expandNode(node, false);
        }
    }
}

function onClick(event, treeId, treeNode, clickFlag) {
	loadEdit(treeNode.description);
}

function loadEdit(path){
	var url = 'edit?path=' + path;
	$.get(url, function(data){
		$("#app_content").html(data);
	});
}

function onRightClick(event, treeId, treeNode){
	var a = [event.clientX, event.clientY, treeNode.flag]
	showRightMenu(treeNode, event.clientX, event.clientY);
}

function showRightMenu(node, x, y){
	var ele = '#node';
	$('#a_add_node').attr('href', "javascript:addNode('" + node.description + "')");
	if (node.flag) {
		$('#a_del_node').attr('href', "javascript:removeNode('" + node.description + "')");
	} else {
		$('#a_del_node').attr('href', "javascript:removeLeafNode('" + node.description + "')");
	}
	$(ele).css({'top' : y + 'px', 'left' : x + 'px','visibility' : 'visible'});
	$("body").bind("mousedown", hideAllMenu);
}
function hideAllMenu() {
	if (!(event.target.id == "node" || $(event.target).parents("#node").length>0)) {
		$('#node').css({"visibility" : "hidden"});
		$("body").unbind("mousedown", hideAllMenu);
		var as = $('#node ul li a');
		for (var i =0; i < as.length; i++)as.attr('href', '#');
	} 
}

$(document).ready(function(){
	$.fn.zTree.init($("#treeDemo"), setting, znodes);
});

function addNode(parent) {
	var url = 'add?parent=' + parent;
	$.get(url, function(data) {
		$('#app_content').html(data);
	});
}

function removeNode(path) {
	if (!confirm('确定删除指定节点吗?')) {
		return;
	}
	$.get('remove?path=' + path, function(data){
		evalResult(data, '删除成功');
	});
}

function removeLeafNode(path) {
	if (!confirm('该节点含有子节点，确定全部删除吗?')) {
		return;
	}
	$.get('remove?path=' + path, function(data){
		evalResult(data, '删除成功');
	});
}

function loadDataProj(proj) {
	var url = "app/listproj/" + proj;
	$.get(url, function(data) {
		$('#app_content').html(data);
	});
}

function loadDataDept(dept) {
	var url = "app/listdept/" + dept;
	$.get(url, function(data) {
		$('#app_content').html(data);
	});
}

function evalResult(data, msg){
	var json = eval(data);
	if (json.result == 'success')  {
		if (msg)
			alert(msg);
		document.location.reload();
	} else {
		alert(json.context.actionErrors.join('\n'));
	}
}
</script>
<body>
<nav class="navbar navbar-default navbar-fixed-top">
	<div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" href="#">
        ZooKeeper UI
      </a>
    </div>
  </div>
</nav>
<br>
<#macro printTree node>
{	
	id:${node.id?c},
	pId:${node.parent?default(0)?c}, 
	open: ${(node.open)?string('true', 'false')},
	flag: ${(node.leaf)?string('true', 'false')}, 
	name:'${node.name?js_string}',
	description:'${(node.description)?default('')?js_string}',
	children:[<#list node.children as item><@printTree item /></#list>]
},
</#macro>

<div style="margin-top:30px">
<div class="row">
	<div class="span3">
  		<div class="zTreeDemoBackground left">
        	<ul id="treeDemo" class="ztree"></ul>
  		</div>
  	</div>
  	<div class="span8">
  		<div id="app_content" style="margin-left:80px"></div>
	</div>
</div>
<div style="position:absolute; visibility:hidden; top:0" id="node">
	<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="display: block; position: static; margin-bottom: 5px; *width: 180px;">
  		<li><a id="a_add_node" tabindex="-1" href="#">添加节点</a></li>
  		<li><a id="a_del_node" tabindex="-1" href="#">删除节点</a></li>
	</ul>
</div>
</div>
</body>
