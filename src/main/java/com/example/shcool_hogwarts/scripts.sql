select *
from student;
select *
from student
where age > 14
  and age < 18;
select name
from student;
select *
from student
where name LIKE '%o%';
select *
from student
where age < id;
select *
from student
order by age;
select count(*)
from student;
select avg(age) as age
from student;
select *
from student
limit 5;