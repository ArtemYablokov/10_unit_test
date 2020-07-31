create extension if not exists pgcrypto; /* РАСШИРЕНИЕ ДЛЯ ПОСТГРЕСА ЕСЛИ НЕ УСТАНОВЛЕНО */

/*
ОБНОВИТЬ ВСЕ ПАРОЛИ В ТАБЛИЦЕ ПОЛЬЗОВАТЕЛЯ
password - старое значение пароля
соль - присоединяется к паролю при шифровании
bf - алгоритм шифрования
*/
update usr set password = crypt(password, gen_salt('bf', 8));