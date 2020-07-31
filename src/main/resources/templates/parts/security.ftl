<#--
файл для добавления переменных
выполнение логики
чтобы отображать/НЕ отображать для разных пользователей разные элементы
-->

<#assign
    known = Session.SPRING_SECURITY_CONTEXT?? <#-- перевод в булево выражение
    - проверка, существует ли в контексте ФРИМАРКЕРА объект, который позвояет оперировать SPRING_SECURITY_CONTEXT
    то есть существует ли сессия
     -->
>

<#if known>
    <#assign
        user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
        name = user.getUsername()
        isAdmin = user.isAdmin()
        currentUserId = user.getId()
    >
<#else>
    <#assign
        name = "unknown"
        isAdmin = false
        currentUserId = -1 <#-- INVALID VALUE -->
    >
</#if>