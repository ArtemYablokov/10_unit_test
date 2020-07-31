<#include "security.ftl">
<#import "loginform.ftl" as l>


<nav class="navbar navbar-expand-lg navbar-light bg-light"> <#-- navbar-expand-lg на больших экранах и более ПАНЕЛЬ будет в развернутом виде -->
    <a class="navbar-brand" href="/">Farter</a> <#-- ссылка на главную страницу -->
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button> <#-- кнопка для экранов меньше чем LG -->

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a class="nav-link" href="/main">Main page </a>
            </li>
            <#if user??>
                <li class="nav-item">
                    <a class="nav-link" href="/user-messages/${currentUserId}">User message</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/user/profile">Profile</a>
                </li>
            </#if>
            <#if isAdmin>
                <li class="nav-item">
                    <a class="nav-link" href="/user">User list</a>
                </li>
            </#if>
        </ul>

        <div class="navbar-text mr-3">${name}</div>
        <@l.logout />
    </div>
</nav>
