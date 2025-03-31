-- liquibase formatted sql

CREATE INDEX student_name_index ON student (name);

CREATE INDEX faculty_cn_index ON faculty (color, name);