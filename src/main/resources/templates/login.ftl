<#import "parts/common.ftl" as c>
<#import "parts/loginform.ftl" as l>

<@c.page>
    <div class="mb-1">Login</div>

    <#-- сообщение об удачной/не активации - приходит из @GetMapping("/activate/{activationcode}") -->
    <#if message??>
        <div class="alert alert-${messageType}" role="alert">
            ${message}
        </div>
    </#if>

    <#-- Если не найден пользователь -->
    <#if Session?? && Session.SPRING_SECURITY_LAST_EXCEPTION??> <#-- СЕССИЯ СУЩЕСТВУЕТ ТОЛЬКО ДЛЯ АВТОРИЗИРОВАННОГО ПОЛЬЗОВАТЕЛЯ  -->
        <div class="alert alert-danger" role="alert">
            ${Session.SPRING_SECURITY_LAST_EXCEPTION.message}
        </div>
    </#if>

    <@l.login "/login" false/>
<#--    <a href="/registration">to REGISTRATION</a> -->
</@c.page>