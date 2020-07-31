<#include "security.ftl">
<#import "pager.ftl" as p>


<@p.pager url page size/>
<div class="card-columns" id="message-list">
    <#list page.content as message >
        <div class="card my-3" data-id="${message.id}"> <#-- отступ сверху/снизу -->
            <#if message.filename??> <#-- если существует поле  ?? - приаодит к булевому типу -->
                <img src="/img/${message.filename}" class="card-img-top"/> <#-- в img храним файлы -->
            </#if>
            <div class="m-2"> <#--отступ с четырех сторон -->
                <span>${message.text}</span><br/>
                <i>#${message.tag}</i>
            </div>
            <div class="card-footer text-muted">
                <a href="/user-messages/${message.author.id}">${message.authorName}</a>
                <#-- для редактирования своего сообщения - добавляем само сообщение в запрос (заполняем ии форму !!) -->
                <#if message.author.id == currentUserId>
                    <a class="btn btn-primary" href="/user-messages/${message.author.id}?message=${message.id}">
                        <#-- УРЛ так и выглядит : 31?=23 // ? id юзера = id сообщения
                            то есть через параметр запроса -->
                        Edit
                    </a>
                </#if>
            </div>

        </div>
    <#else>
        No message
    </#list>
</div>
<@p.pager url page size/>
