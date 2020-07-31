<#import "parts/common.ftl" as c>

<@c.page>
    USER EDITOR
    <form action="/user" method="post">
        <input type="text" name="username" value="${user.username}"/> <#-- ИМЯ изначальное -->
        <#list roles as role>  <#-- обращение к классу ROLE  -->
            <div>
                <label><input type="checkbox" name="${role}"
                            ${user.roles?seq_contains(role)?string("checked", "")}/>
                    <#--
                    user.roles?seq_contains(role) - проверка на наличие эл-та в коллекции
                    ?string("checked", "") - приведение к String - 1е знач если TRUE и наоброт
                    -->
                    ${role}</label>
            </div>
        </#list>
        <input type="hidden" value="${user.id}" name="userId"/>
        <input type="hidden" value="${_csrf.token}" name="_csrf"/>

        <button type="submit">Save</button>
    </form>
</@c.page>