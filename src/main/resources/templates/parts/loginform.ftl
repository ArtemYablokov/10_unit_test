<#include "appearance.ftl">
<#include "security.ftl">


<#macro login path isRegisterForm> <#-- МАКРОС ожидает две ПЕРЕМЕННЫХ ! -->

    <form action="${path}" method="post">

        <div class="form-group row"> <#-- блок строки формы -->
            <label class="col-sm-${namesWidth} col-form-label">User Name:</label>

            <div class="col-sm-${fieldsWidth}"> <#-- блок поля ввода -->
                <input type="text" name="username"
                       class="form-control ${(usernameError??)?string('is-invalid', '')}"
                       placeholder="User name"
                       value="<#if user??>${user.username}</#if>"/>
                <#if usernameError??>  <#--ЕСЛИ ОШИБКА передана через ERROR_MAP - ВЫВОДИМ ЕЕ-->
                    <div class="invalid-feedback">
                        ${usernameError}
                    </div>
                </#if>
            </div>
        </div>

        <div class="form-group row">
            <label class="col-sm-${namesWidth} col-form-label">Password:</label>
            <div class="col-sm-${fieldsWidth}">
                <input type="password" name="password"
                       class="form-control ${(passwordError??)?string('is-invalid', '')}"
                       placeholder="Password"
                       value="<#if user??>${user.password}</#if>"/>
                <#if passwordError??>
                    <div class="invalid-feedback">
                        ${passwordError}
                    </div>
                </#if>
            </div>
        </div>

        <#if isRegisterForm>
            <div class="form-group row">
                <label class="col-sm-${namesWidth} col-form-label">PasswordConfirm:</label>
                <div class="col-sm-${fieldsWidth}">
                    <input type="password" name="passwordConfirm"
                           class="form-control ${(passwordConfirmError??)?string('is-invalid', '')}"
                           placeholder="Confirm Password"
                            <#if passwordConfirm??> value="${passwordConfirm}"</#if>/>
                    <#if passwordConfirmError??>
                        <div class="invalid-feedback">
                            ${passwordConfirmError}
                        </div>
                    </#if>
                </div>
            </div>

            <div class="form-group row">
                <label class="col-sm-${namesWidth} col-form-label">Email:</label>
                <div class="col-sm-${fieldsWidth}">
                    <input type="email" name="email"
                           class="form-control ${(emailError??)?string('is-invalid', '')}"
                           placeholder="email@email.com"
                           value="<#if user??>${user.email}</#if>"/>
                    <#if emailError??>
                        <div class="invalid-feedback">
                            ${emailError}
                        </div>
                    </#if>

                </div>
            </div>
        <#-- место отображения КАПТЧИ -->
            <div class="col-sm-${namesWidth}">
                <div class="g-recaptcha" data-sitekey="6Le1nc4UAAAAAEPjcF4qvJ9v923DlpGrXtIYhIA2"></div>
                <#-- вывод ошибок -->
                <#if captchaError??>
                    <div class="alert alert-danger" role="alert">
                        ${captchaError}
                    </div>
                </#if>
            </div>
        </#if>

        <input type="hidden" name="_csrf" value="${_csrf.token}"/>

        <#if !isRegisterForm>
            <a href="/registration">Add new user</a>
        </#if>

        <button class="btn btn-primary"
                type="submit"> <#-- у кнопки LABEL описывается в виде контента - не нужен ДИВ -->
            <#if isRegisterForm>Create<#else>Sign In</#if>
        </button>

        <#--        <input type="text" name="username" value="" placeholder="User Name"><br>-->
        <#--        <input type="password" name="password" value="" placeholder="Password"><br>-->
        <#--        <input type="hidden" name="_csrf" value="${_csrf.token}"/>-->
        <#--        <input type="submit">-->
    </form>
</#macro>

<#macro logout>
    <div>
        <form action="/logout" method="post">
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button class="btn btn-primary" type="submit" ><#if user??>Sign out<#else>Log in</#if></button>
        </form>
    </div>
</#macro>