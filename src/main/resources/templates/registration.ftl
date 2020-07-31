<#import "parts/common.ftl" as c>
<#import "parts/loginform.ftl" as l>

<@c.page>
    <div class="mb-1">Registration</div>

    <#if commonError??> <#--   -->
        <div class="alert alert-danger" role="alert">
            ${commonError}
        </div>
    </#if>

    <@l.login "/registration" true/>
</@c.page>