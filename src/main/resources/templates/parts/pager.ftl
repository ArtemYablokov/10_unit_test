<#macro pager url page size>
<#-- урл-это страницы, listOfMessages - возвращаемые сообщения, size - количество сообщений на странице -->

<#-- определяем количество и тип отображаемых страниц (если их больше 7) -->
    <#if page.getTotalPages() gt 7> <#-- больше 7 -->
        <#assign
        totalPages = page.getTotalPages()
        pageNumber = page.getNumber() + 1 <#-- сейчас где находимся -->

        head = (pageNumber > 4)?then([1, -1], [1, 2, 3])
        tail = (pageNumber < totalPages - 3)?then([-1, totalPages], [totalPages - 2, totalPages - 1, totalPages])
        bodyBefore = (pageNumber > 4 && pageNumber < totalPages - 1)?then([pageNumber - 2, pageNumber - 1], [])
        bodyAfter = (pageNumber > 2 && pageNumber < totalPages - 3)?then([pageNumber + 1, pageNumber + 2], [])
        center = (pageNumber > 3 && pageNumber < totalPages - 2)?then([pageNumber], [])

        <#-- результат -->
        body = head + bodyBefore + center + bodyAfter + tail
        >
    <#else>
        <#assign body = 1..page.getTotalPages()>
    </#if>

    <div>
        <ul class="pagination">
            <li class="page-item disabled"> <#-- disable - неактивный обычный элемент -->
                <a class="page-link" href="#" tabindex="-1">Страницы</a>
            </li>
            <#list body <#--1..listOfMessages.getTotalPages()--> as p >  <#--отображаем массив страниц от 1 до последней-->
                <#if (p-1) == page.getNumber()> <#-- вычитаем 1 тк номер считается с 0-->
                    <li class="page-item active">
                        <a class="page-link" href="#" tabindex="-1">${p}</a>
                    </li>
                <#elseif p == -1>
                    <li class="page-item active">
                        <a class="page-link" href="#" tabindex="-1">...</a>
                    </li>
                <#else>
                    <li class="page-item">
                        <#-- если номер не соответствует текущей - ссылка на переход на нужную страницу -->
                        <a class="page-link" href="${url}?page=${p-1}&size=${size}" tabindex="-1">${p}</a>
                    </li>

                </#if>
            </#list>
        </ul>


        <#-- список для выбора количества отображаемых страниц -->

        <#-- при переходе на другую страницу - слетает выбранное
        количество - надо передавать количество тогда в аргументе -->

        <ul class="pagination">
            <li class="page-item disabled">
                <a class="page-link" href="#" tabindex="-1">Элементов на странице</a>
            </li>
            <#list [5, 6, 8, 10] as c>
                <#if c == page.getSize()>
                    <li class="page-item active">
                        <a class="page-link" href="#" tabindex="-1">${c}</a>
                    </li>
                <#else>
                    <li class="page-item">
                        <#-- смена количества страниц - переход по УРЛу -->
                        <a class="page-link" href="${url}?page=${page.getNumber()}&size=${c}" tabindex="-1">${c}</a>
                    </li>
                </#if>
            </#list>
        </ul>

    </div>
</#macro>
