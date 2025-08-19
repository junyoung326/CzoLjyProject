delete from member where member_id = 'lee';

drop table member;
SET foreign_key_checks = 1;

select member_id
from member
where member_name = 'lee' and member_phone_number = '01012345678';

UPDATE member SET member_password= '1111' where member_id = 'lee';

create table persistent_logins(
    username varchar(64) not null,
    series varchar(64) primary key,
    token varchar(64) not null,
    last_used timestamp not null);