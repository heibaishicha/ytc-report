<@master template="common/default" title="API接口文档">
<div>
    <div>
        <h3>${result.methodName }</h3>
        <p>${result.description }</p>
    </div>
</div>
<h2>请求参数</h2>
	<#list result.requestParams as requestParam>
	<h3>${requestParam.type}</h3>
	<table border="1">
        <tr>
            <td>参数名</td>
            <td>参数类型</td>
            <td>描述</td>
            <td>示例</td>
        </tr>
		<#list requestParam.options as option >
		<tr>
            <td>${option.name }</td>
            <td>${option.type }</td>
            <td>${option.description }</td>
            <td>${option.demoValue }</td>
        </tr>
		</#list>
    </table>
	</#list>
<h2>响应参数</h2>
	<#list result.responseParams as responseParam>
	<h3>${responseParam.type}</h3>
	<table border="1">
        <tr>
            <td>参数名</td>
            <td>参数类型</td>
            <td>描述</td>
            <td>示例</td>
        </tr>
		<#list responseParam.options as option >
		<tr>
            <td>${option.name }</td>
            <td>${option.type }</td>
            <td>${option.description }</td>
            <td>${option.demoValue }</td>
        </tr>
		</#list>
    </table>
	</#list>

<h2>接口实例</h2>
	<form action="${result.methodName }" method="GET">
        <h3>${result.methodName }<input type="button" value="【生成参数】" class="btn-buildParam"><input type="button" value="【生成并提交】"  class="btn-buildAndSumit"></h3>
	<#list result.requestParams as requestParam>
		<table border="1">
            <tr>
                <td>参数名</td>
                <td>参数类型</td>
                <td>描述</td>
                <td>数据</td>
            </tr>
			<#list requestParam.options as option >
			<tr>
                <td>${option.name }</td>
                <td>${option.type }</td>
                <td>${option.description }</td>
                <td><input name="${option.name }" value="${option.demoValue }"></td>
            </tr>
			</#list>
        </table>
	</#list>

        <div>
            <textarea id = "paramData" style="width:100%;height:50px"> </textarea>
            <input type="button" value="【提交参数】"  class="btn-submitParam">
        </div>


    </form>

<div>
    <pre id = "showResult">

    </pre>
</div>


<style>
    pre {outline: 1px solid #ccc; padding: 5px; margin: 5px; }
    .string { color: green; }
    .number { color: darkorange; }
    .boolean { color: blue; }
    .null { color: magenta; }
    .key { color: red; }
</style>

	<@js>
function syntaxHighlight(json) {
    if (typeof json != 'string') {
        json = JSON.stringify(json, undefined, 2);
    }
    json = json.replace(/&/g, '&').replace(/</g, '<').replace(/>/g, '>');
    return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function(match) {
        var cls = 'number';
        if (/^"/.test(match)) {
            if (/:$/.test(match)) {
                cls = 'key';
            } else {
                cls = 'string';
            }
        } else if (/true|false/.test(match)) {
            cls = 'boolean';
        } else if (/null/.test(match)) {
            cls = 'null';
        }
        return '<span class="' + cls + '">' + match + '</span>';
    });
}

 (function($){
$(function(){
	$(".submit-btn").click(function(){
		var $form=$(this).closest("form"),$value=$("#paramData");
		var data={};
		$form.serializeArray().forEach(function(item){if(!(item.value===""))data[item.name]=item.value});
		data=JSON.stringify(data);
		$value.val(data);
		$.ajax({
			type:"post",
			url:$form.attr("action"),
			data:$value.val(),
			dataType:"json",
			cache:false,
			contentType:"application/json",
			error:function(status,error){},
			success:function(data){$("#showResult").html(syntaxHighlight(data));},
			error:function(jqXHR){$("#showResult").html(syntaxHighlight(jqXHR.responseText));}
		});
		//$.get($form.attr("action"),$form.serialize(),function(data){$("#showResult").html(syntaxHighlight(data));});
		return false;
	});
	var funs={
		buildAndSumit:function(ele){
			funs.buildParam(ele);
			funs.submitParam(ele);
		},
		buildParam:function(ele){
			var $form=$(ele).closest("form"),$value=$("#paramData");
			var data={};
			$form.serializeArray().forEach(function(item){if(!(item.value===""))data[item.name]=item.value});
			data=JSON.stringify(data);
			$value.val(data);
		},
		submitParam:function(ele){
			var $form=$(ele).closest("form"),$value=$("#paramData");
			$.ajax({
				type:"post",
				url:$form.attr("action"),
				data:$("#paramData").val(),
				dataType:"json",
				cache:false,
				contentType:"application/json",
				error:function(status,error){},
				success:function(data){$("#showResult").html(syntaxHighlight(data));},
				error:function(jqXHR){$("#showResult").html(syntaxHighlight(jqXHR.responseText));}
			});
		}
	};
	["buildAndSumit","buildParam","submitParam"].forEach(function(e){
		$(".btn-"+e).on("click",function(){funs[e](this);});
	});
});
 })($);
	</@js>
</@master>
