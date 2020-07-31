<#macro messageEdit isEdit>

    <a class="btn btn-primary"
       data-toggle="collapse"
       href="#collapseExample"
       role="button"
       aria-expanded="false"
       aria-controls="collapseExample">Add new Message</a>
    <div class="collapse <#if message??>show</#if>" <#-- предотвращает схлопывание если есть сообщение -->
         id="collapseExample"> <#-- ID связывает схлопывающуюся форму и кнопку-->
        <div class="form-group mt-3">
            <form <#--action="/main"--> method="post" enctype="multipart/form-data"> <#-- ЭНКРИПТ для файла -->
                <#-- action не указан - пост на УРЛ, с которого пришел зжапрос -->
                <div class="form-group col-md-6">
                    <input type="text" class="form-control ${(textError??)?string('is-invalid', '')}"
                            <#--приводим к булеву выражению  {(textError??)
                            если TRUE тогда привоим к строке 'is-invalid'  иначе пустая строка -->
                           value="<#if message??>${message.text}</#if>" <#-- чтобы не потерять значение если ошибка  -->
                           name="text" placeholder="Message"/>
                    <#if textError??> <#--ЕСЛИ ОШИБКА передана через ERROR_MAP - ВЫВОДИМ ЕЕ-->
                        <div class="invalid-feedback">
                            ${textError}
                        </div>
                    </#if>
                </div>

                <div class="form-group col-md-6">
                    <input type="text" class="form-control ${(tagError??)?string('is-invalid', '')}"
                           value="<#if message??>${message.tag}</#if>"
                           name="tag" placeholder="Tag"/>
                    <#if tagError??>
                        <div class="invalid-feedback">
                            ${tagError}
                        </div>
                    </#if>
                </div>

                <input type="hidden" name="_csrf" value="${_csrf.token}"/>

                <#if isEdit> <#-- только при редактировании сообщения передаем ID от сообщения -->
                    <#-- для редактирования сообщения добавляем скрытый ID -->
                    <input type="hidden" name="id" value="${message.id}"/>
                    <#--здесь message пустой, когда с main добавляем новое сообщение -> message == null  -->
                </#if>

                <div class="form-group col-md-6">
                    <div class="custom-file">
                        <input type="file" name="file" id="customFile"/>
                        <label class="custom-file-label" for="customFile">Choose file</label>
                    </div>
                </div>

                <div class="form-group col-md-6">
                    <button type="submit" class="btn btn-primary">Добавить смс</button>
                </div>

            </form>
        </div>
    </div>

</#macro>
