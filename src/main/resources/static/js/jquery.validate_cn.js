/*!
 * jQuery validate plugin
 * 
 * Copyright @ 2010 zhong-ying Co.Ltd
 * All right reserved.
 * 
 * 作    者：刘冲
 * 功能描述：校验后台规则
 * 日    期：2011-01-18
 */
 
 (function($) {
	// 插件默认选项
	$.extend($.fn.validate.defaults, {
		divTitle : '提交表单错误项如下：',
		rules : {
			required : {
				method : "required",
				msg : {
					text : "——请输入",
					option : "——请选择"
				}
			},
			max : {
				method : "max",
				msg : {
					text : "——最大值为{1}",
					option : "——选择最多 {1} 项"
				}
			},
			min : {
				method : "min",
				msg : {
					text : "——最小值为{1}",
					option : "——选择至少 {1} 项"
				}
			},
			range : {
				method : "range",
				msg : {
					text : "——输入值应该为 {1} 至 {2} 之间",
					option : "——应该选择 {1} 至 {2} 项"
				}
			},
			maxLength : {
				method : "maxLength",
				msg : "——最大长度为{1},当前长度为{0}"
			},
			minLength : {
				method : "minLength",
				msg : "——最小长度为{1},当前长度为{0}"
			},
			rangeLength : {
				method : "rangeLength",
				msg : "——输入值的长度应该在 {1} 至 {2} 之间,当前长度为{0}"
			},
			equalTo : {
				method : "equalTo",
				msg : "——两次输入不一致,请重新输入"
			},
			greatTo: {
				method : "greatTo",
				msg : "——请输入大于前面的值"
			},
			lessTo: {
				method : "lessTo",
				msg : "——请输入小于前面的值"
			},
			pattern : {
				method : "pattern",
				msg : "——输入内容不匹配"
			},
			ajax : {
				method : "ajax"
			},
			method : {
				method : "method"
			},
			file : {
				method : "file",
				msg : "——文件类型应该为{1}其中之一"
			},
			number : {
				regex : /^\d+$/,
				msg : "——请输入有效数字"
			},
			letter : {
				regex : /^[a-zA-Z]+$/,
				msg : "——请输入英文字母"
			},
			letterNum : {
				regex : /^\w+$/,
				msg : "——请输入英文字母或是数字,其它字符是不允许的"
			},
			int : {
				regex : /^[-+]?[1-9]\d*$|^0$/,
				msg : "——请输入整数"
			},
			float : {
				regex : /^-?([1-9]d*.d*|0.d*[1-9]d*|0?.0+|0)$/,
				msg : "——请输入浮点数"
			},
			chinese : {
				regex : /^[\u4e00-\u9fa5]+$/,
				msg : "——请输入中文字符串"
			},
			email : {
				regex : /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/,
				msg : "——请输入有效的邮件地址,如 liuchong@sohu-inc.com"
			},
			phone : {
				regex : /^((0[1-9]{3})?(0[12][0-9])?[-])?\d{6,8}$/,
				msg : "——请输入正确的电话号码,如:025-83463009"
			},
			mobilePhone : {
				regex : /^1[1-9]\d{9}$/,
				msg : "——请输入正确的电话号码,如:18915966478"
			},
			url : {
				regex : /^(https?|ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(\#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/,
				msg : "——请输入有效的URL地址"
			},
			ip : {
				regex : /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/,
				msg : "——请输入正确的IPv4地址"
			}
		}
	});
	// 扩展消息模式
	$.extend($.fn.validate, {
		showMsg : function(form, prompts, opts) {
			bootbox.dialog("<ul>" + $.map($(prompts), function(obj){
				return "<li>" + obj.msg;
			}).join("") +"</ul>", 
        	[{
                "label" : "关闭",
                "class" : "btn-danger",
                "icon"  : "icon-warning-sign icon-white",
                "header" : "表单提示"
            }]);
		}
	});
})(jQuery);