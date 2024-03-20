CREATE TABLE recipe_data
(
    id BIGSERIAL primary key,
    slug VARCHAR not null,
    recipe_header_title VARCHAR not null,
    methodology VARCHAR
);

CREATE TABLE header_image
(
    id BIGSERIAL primary key,
    image_source VARCHAR not null,
    recipe_data_id BIGSERIAL,
    foreign key(recipe_data_id) references recipe_data(id)
);

CREATE TABLE ingredient
(
    id BIGSERIAL primary key,
    value VARCHAR not null,
    recipe_data_id BIGSERIAL,
    foreign key(recipe_data_id) references recipe_data(id)
);

CREATE TABLE basic_info
(
    id BIGSERIAL primary key,
    label VARCHAR,
    value VARCHAR,
    recipe_data_id BIGSERIAL,
    foreign key(recipe_data_id) references recipe_data(id)
);

CREATE TABLE content_step
(
    id BIGSERIAL primary key,
    title VARCHAR,
    instruction VARCHAR,
    recipe_data_id BIGSERIAL,
    foreign key(recipe_data_id) references recipe_data(id)
);

CREATE TABLE content_step_image
(
    id BIGSERIAL primary key,
    image_source VARCHAR,
    content_step_id BIGSERIAL,
    foreign key(content_step_id) references content_step(id)
);
