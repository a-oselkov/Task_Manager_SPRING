INSERT INTO users (created_at, first_name, last_name, email, password) VALUES ('2023-01-01 00:00:00', 'Иван', 'Иванов', 'test@user.com', '$2a$10$GoQd22V6y0Gk6EoPjaO3xeKs1KFFfHxHtrkAUZfF4JB6Lk.f7FIrq');

INSERT INTO task_statuses (created_at, name) VALUES ('2023-01-01 00:00:00', 'Новая');
INSERT INTO task_statuses (created_at, name) VALUES ('2023-01-01 00:00:00', 'В работе');
INSERT INTO task_statuses (created_at, name) VALUES ('2023-01-01 00:00:00', 'Завершена');

INSERT INTO labels (id, created_at, name) VALUES (1, '2022-10-10 00:00:00', 'Frontend');
INSERT INTO labels (id, created_at, name) VALUES (2, '2022-10-10 00:00:00', 'Backend');

INSERT INTO tasks (id, created_at, name, description, task_status_id, author_id, executor_id) VALUES (1, '2022-10-10 00:00:00', 'Важная задача', 'Сделать что-то очень важное', 1, 1, 1);

INSERT INTO task_label (task_id, label_id) VALUES (1,2);
