<#import "parts/common.ftl" as c>
<#import "parts/messageEditor.ftl" as m>
<#include "parts/security.ftl">

<@c.page>
    <h3>${userChannel.username}</h3>

    <#if !isCurrentUser>
        <#if isSubscriber>
            <a class="btn btn-info" href="/subs/unsubscribe/${userChannel.id}">Unsubscribe</a>
        <#else>
            <a class="btn btn-info" href="/subs/subscribe/${userChannel.id}">Subscribe</a>
        </#if>
    </#if>

    <div class="container my-3">
        <div class="row">
            <div class="col">
                <div class="card">
                    <div class="card-body">
                        <div class="card-title">Subscriptions</div>
                        <h3 class="card-text">
                            <a href="/subs/subscriptions/${userChannel.id}/list">${subscriptionsCount}</a>
                        </h3>
                    </div>
                </div>
            </div>
            <div class="col">
                <div class="card">
                    <div class="card-body">
                        <div class="card-title">Subscribers</div>
                        <h3 class="card-text">
                            <a href="/subs/subscribers/${userChannel.id}/list">${subscribersCount}</a>
                        </h3>
                    </div>
                </div>
            </div>
        </div>
    </div>

<#-- передаем АТРИБУТОМ -->
    <#if message??> <#-- только если в запросе есть ID сообщения - тогда покажем блок редактирования -->
<#--        <#include "parts/messageEditor.ftl">-->
        <@m.messageEdit true/>
    </#if>

    <#include "parts/messageList.ftl">

</@c.page>