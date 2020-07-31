<#import "parts/common.ftl" as c>

<@c.page>
    LIST OF USERS
<table>
    <thead>
    <tr>
        <th>Name</th>
        <th>Role</th>
    </tr>
    </thead>
    <tbody>
    <#list userList as user>
        <tr>
            <td><span>${user.username}</span></td>
            <#-- роли: -->
            <td><#list user.roles as role>${role}<#sep>, </#list></td> <#-- <#sep>, СЕПАРАТОР -->
            <td><a href="/user/${user.id}">EDIT USER</a> </td>
        </tr>
    </#list>
    </tbody>
</table>
</@c.page>