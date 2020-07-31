delete from user_role;
delete from usr;

insert into usr(id, username, password, active) values
(1, 'admin', '$2a$08$jmTJr05WBMBBYLGC7OZ4z.zdp2N67y8YoWXwSHqAN83/uard/fslq', true),
(2, 'a', '$2a$08$uhDOqWcC4JsPfnrIG9URnujEplplY0TD/cPDKLK7r.1FZcQlTc17.', true);

insert into user_role(user_id, roles) values
(1, 'ADMIN'), (1, 'USER'),
(2, 'USER');