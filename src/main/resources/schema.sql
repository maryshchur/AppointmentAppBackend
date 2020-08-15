
create table if not exists roles
(
    id   bigint       not null primary key,
    name varchar(100) not null,
    constraint unique_uk_5 unique (name)
);

BEGIN;
INSERT INTO public.roles
SELECT role.id, role.name
FROM (
         SELECT 1            as id,
                'ROLE_TEACHER' as name
         UNION
         SELECT 2              as id,
                'ROLE_STUDENT' as name
     ) as role
WHERE (SELECT COUNT(*) FROM roles) <= 0;
COMMIT;
END;