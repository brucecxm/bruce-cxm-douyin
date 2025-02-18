-- we don't know how to generate root <with-no-name> (class Root) :(
grant select on *.* to 'mysql.infoschema'@localhost;

grant super on *.* to 'mysql.session'@localhost;

grant alter, alter routine, backup_admin, binlog_admin, connection_admin, create, create role, create routine, create tablespace, create temporary tables, create user, create view, delete, drop, drop role, encryption_key_admin, event, execute, file, group_replication_admin, index, insert, lock tables, persist_ro_variables_admin, process, references, reload, replication client, replication slave, replication_slave_admin, resource_group_admin, resource_group_user, role_admin, select, set_user_id, show databases, show view, shutdown, super, system_variables_admin, trigger, update, xa_recover_admin, grant option on *.* to root@localhost;

create table menu
(
    menu_id   int          null,
    menu_img  varchar(500) null,
    menu_name int          null,
    menu_type int          null,
    menu_lev  int          null,
    menu_href int          null
);

create table nav
(
    nav_id   int           null,
    nav_name int           null,
    nav_img  varchar(5000) null,
    nav_href int           null,
    nav_type int           null,
    nav_lev  int           null
);

