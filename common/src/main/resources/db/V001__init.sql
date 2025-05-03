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

create table tags
(
    id         bigint auto_increment comment 'id'
        primary key,
    concept_id bigint        not null comment '개념 id',
    tag        varchar(64)   not null comment '태그',
    detail     varchar(8192) not null comment '상세',
    learned_at datetime      not null comment '학습일',
    created_by varchar(16)   not null comment '생성자',
    updated_by varchar(16)   not null comment '수정자',
    created_at datetime      not null comment '생성일',
    updated_at datetime      not null comment '마지막 업데이트 일'
)
    comment '태그';

create unique index tags_concept_id_tag_index
    on tags (concept_id, tag);

create index tags_learned_at_index
    on tags (learned_at);


create table examples
(
    id          bigint auto_increment comment 'id'
        primary key,
    target_type varchar(16)   not null comment '타겟 타입 (컨셉, 태그)',
    target_id   bigint        not null comment '타겟 id (컨셉, 태그)',
    `key`       varchar(512)      null comment '키 (예제, 이미지 등등..)',
    value       varchar(512)      null comment '밸류 (뜻, 답 등등..)',
    learned_at  datetime      not null comment '학습일',
    created_by  varchar(16)   not null comment '생성자',
    updated_by  varchar(16)   not null comment '수정자',
    created_at  datetime      not null comment '생성일',
    updated_at  datetime      not null comment '마지막 업데이트 일'
)
    comment '예시';

create index examples_target_id_index
    on examples (target_type, target_id);

create index examples_learned_at_index
    on examples (learned_at);

