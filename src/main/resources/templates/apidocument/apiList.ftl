<@master template="common/default"  title="API接口文档">
<input type="hidden" id ="preGroupName" value="" />
   <style>
        .item_wrap{
            display: block;
        }
    </style>
 <ul>
 <#assign preGroupName="" />
<#list result as res >
    <#if  preGroupName!=res.group>
    	<#assign preGroupName=res.group />
    <#else>
    	<#continue>
    </#if>  
    
    <li>
	<div style="border: 1px solid;cursor: pointer;font-size:18px;font-weight:bold;"  class = "item">${res.group}</div>
    <div style="border: 1px solid;" class="item_wrap" >
    <#list result as citem>
    	<#if citem.group==res.group>
    	<div style="border: 1px solid;"  >
		<h3> <a target="_blank" href="/system-admin/app/view/getApiMethodInvokeObjectView?value=${citem.methodName?url}">${citem.methodName }</a></h3>
		<hr>
		<p>${citem.description }</p>
		<p>${citem.method }</p>
		</div>
		</#if>
	</#list>
	</div>
	</li>
</#list>
</ul>
<script>
        let items=document.querySelectorAll('.item'),itemWraps=document.querySelectorAll('.item_wrap');
        items.forEach((ele,index)=>{
            ele.dispBol='true';　　　　//创建一个属性来记录当前面板是否展开   true 展开 false折叠 
            items[index].onclick=((e)=>{
                let thisDispBol=e.toElement.dispBol; //先保存thisDisBol。接下来要重置
                //itemsInit(); 注释后,不能互斥展开
                thisDispBol=='false' ? e.toElement.dispBol='true' : e.toElement.dispBol='false';
                repaint();
            });
        })
        function repaint(){
            items.forEach((ele,index)=>{
                ele.dispBol=='true' ? itemWraps[index].style.display='block' : itemWraps[index].style.display='none';
            })
        }
        function itemsInit(){
            items.forEach((ele,index)=>{
                ele.dispBol='false';
            })
        }
    </script>
</@master>