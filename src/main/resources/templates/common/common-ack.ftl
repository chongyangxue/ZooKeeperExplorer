({
	"result" : <#if (actionErrors?exists || fieldsErrors?exists)>"error"<#else>"success"</#if>,
	"context" :  {
		"messages" : [<#if messages?exists><#list messages as m>"${m?js_string}"<#if m_has_next>,</#if></#list></#if>],
		"actionErrors" : [<#if actionErrors?exists>
								<#list actionErrors as a>
									"${a?default('unknown')?js_string}"<#if a_has_next>,</#if>
								</#list>
							</#if>],
		"fieldsErrors" : [<#if fieldsErrors?exists>
							<#list fieldsErrors as f>
									"${f?js_string}"<#if f_has_next>,</#if>
							</#list>
						</#if>]  
	}
})