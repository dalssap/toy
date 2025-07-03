create table concepts
(
    id         bigint auto_increment comment 'id'
        primary key,
    category   varchar(16)  not null,
    name       varchar(64)  not null comment '개념',
    meaning    varchar(512)     null comment '뜻',
    learned_at datetime     not null comment '학습일',
    created_by varchar(16)  not null comment '생성자',
    updated_by varchar(16)  not null comment '수정자',
    created_at datetime     not null comment '생성일',
    updated_at datetime     not null comment '마지막 업데이트 일'
)
    comment '개념';

create index concepts_learned_at_index
    on concepts (learned_at);

create unique index concepts_name_index
    on concepts (category, name);

create table leaves
(
    id         bigint auto_increment comment 'id'
        primary key,
    concept_id bigint        not null comment '개념 id',
    type       varchar(64)   not null comment '타입',
    detail     varchar(8192) not null comment '상세',
    learned_at datetime      not null comment '학습일',
    created_by varchar(16)   not null comment '생성자',
    updated_by varchar(16)   not null comment '수정자',
    created_at datetime      not null comment '생성일',
    updated_at datetime      not null comment '마지막 업데이트 일'
)
    comment '리프';

create index leaves_concept_id_type_index
    on leaves (concept_id, type);

create index leaves_learned_at_index
    on leaves (learned_at);
