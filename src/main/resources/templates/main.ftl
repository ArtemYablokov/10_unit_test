<#import "parts/common.ftl" as c>
<#--<#import "parts/loginform.ftl" as l>-->
<#import "parts/messageEditor.ftl" as m>


<@c.page>

    <div class="form-row">
        <div class="form-group col-md-6">
            <form action="/main" method="get" class="form-inline">
                <input type="text" name="filter" class="form-control" value="${filter!}" placeholder="Filter"/>
                <#-- ! - если существует или ?ifExist -->
                <#--    <input type="hidden" name="_csrf" value="${_csrf.token}" />-->
                <button type="submit" class="btn btn-primary ml-2">Filter</button>
            </form>
        </div>
    </div>

<#--    <#include "parts/messageEditor.ftl">-->
    <@m.messageEdit false/>


    <#include "parts/messageList.ftl"/>

<#--    <span><a href="/user">User list</a></span>-->
<#--    <@l.logout/>-->
</@c.page>