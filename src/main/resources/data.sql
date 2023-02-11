insert into categories (icon, name) values ("mdi-triangle-outlined", "Shopping");
insert into categories (icon, name) values ("mdi-triangle-outlined", "Food & Drinks");
insert into categories (icon, name) values ("mdi-triangle-outlined", "Housing");
insert into categories (icon, name) values ("mdi-triangle-outlined", "Transportation");
insert into categories (icon, name) values ("mdi-triangle-outlined", "Vehicle");
insert into categories (icon, name) values ("mdi-triangle-outlined", "Life & Entertainment");
insert into categories (icon, name) values ("mdi-triangle-outlined", "Sport");
insert into categories (icon, name) values ("mdi-triangle-outlined", "Communication, PC");
insert into categories (icon, name) values ("mdi-triangle-outlined", "Financial expenses");
insert into categories (icon, name) values ("mdi-triangle-outlined", "Investments");
insert into categories (icon, name) values ("mdi-triangle-outlined", "Income");
insert into categories (icon, name) values ("mdi-triangle-outlined", "Others");
insert into users (email, first_name, last_name, password, role)
    values (
            "admin@gmail.com",
            "admin",
            "admin",
            "$2a$12$URiJQXh/K4N4gL01TDyv4Oa0IF2aUWevTL0Phzb1A5Ypy3GloU726",
            "ADMIN"
            );
insert into users (email, first_name, last_name, password, role)
    values (
            "matej@gmail.com",
            "Matěj",
            "Babák",
            "$2a$12$URiJQXh/K4N4gL01TDyv4Oa0IF2aUWevTL0Phzb1A5Ypy3GloU726",
            "USER"
            );
insert into users (email, first_name, last_name, password, role)
    values ("honza@gmail.com",
            "Jan",
            "Babák",
            "$2a$12$URiJQXh/K4N4gL01TDyv4Oa0IF2aUWevTL0Phzb1A5Ypy3GloU726",
            "USER"
            );
insert into accounts (balance, color, currency, icon, include_in_statistic, name, user_id)
    values (50000,
            "#6290ff",
            "CZK",
            "mdi-cash",
            true,
            "Savings",
            (select u.id from users u where u.email = "honza@gmail.com")
            );
insert into accounts (balance, color, currency, icon, include_in_statistic, name, user_id)
    values (3250,
            "#6290ff",
            "CZK",
            "mdi-cash",
            true,
            "Current",
            (select u.id from users u where u.email = "honza@gmail.com")
            );
insert into accounts (balance, color, currency, icon, include_in_statistic, name, user_id)
    values (1000,
            "#6290ff",
            "CZK",
            "mdi-cash",
            false,
            "Parent budget",
            (select u.id from users u where u.email = "honza@gmail.com")
            );
insert into accounts (balance, color, currency, icon, include_in_statistic, name, user_id)
    values (15000,
            "#6290ff",
            "CZK",
            "mdi-cash",
            true,
            "Current",
            (select u.id from users u where u.email = "matej@gmail.com")
            );
insert into records (amount, date, label, note, account_id, category_id)
    values (
            -150,
            "2023-02-10 10:52:40.000000",
            "Climbing",
            "entrance and renting climbing shoes",
            (select a.id
             from accounts a
             where a.name = "Current" and a.user_id = (select u.id from users u where u.email = "honza@gmail.com")),
            (select c.id from categories c where c.name = "Sport")
           );
insert into records (amount, date, label, note, account_id, category_id)
values (
           -789,
           "2023-02-11 20:52:40.000000",
           "Groceries",
           "kaufland",
           (select a.id
            from accounts a
            where a.name = "Current" and a.user_id = (select u.id from users u where u.email = "honza@gmail.com")),
           (select c.id from categories c where c.name = "Food & Drinks")
       );
insert into records (amount, date, label, note, account_id, category_id)
values (
           -50,
           "2023-02-05 20:52:40.000000",
           "Plecharna",
           "skate",
           (select a.id
            from accounts a
            where a.name = "Current" and a.user_id = (select u.id from users u where u.email = "honza@gmail.com")),
           (select c.id from categories c where c.name = "Sport")
       );
insert into records (amount, date, label, note, account_id, category_id)
values (
           -125,
           "2023-02-11 12:52:40.000000",
           "Lunch",
           "School buffet.",
           (select a.id
            from accounts a
            where a.name = "Current" and a.user_id = (select u.id from users u where u.email = "honza@gmail.com")),
           (select c.id from categories c where c.name = "Food & Drinks")
       );
insert into records (amount, date, label, note, account_id, category_id)
values (
           -550,
           "2023-02-11 20:52:40.000000",
           "Party",
           "with Letci",
           (select a.id
            from accounts a
            where a.name = "Current" and a.user_id = (select u.id from users u where u.email = "honza@gmail.com")),
           (select c.id from categories c where c.name = "Food & Drinks")
       );
insert into records (amount, date, label, note, account_id, category_id)
values (
           300,
           "2023-02-11 20:52:40.000000",
           "Teaching",
           "math lesson",
           (select a.id
            from accounts a
            where a.name = "Current" and a.user_id = (select u.id from users u where u.email = "honza@gmail.com")),
           (select c.id from categories c where c.name = "Income")
       );
insert into records (amount, date, label, note, account_id, category_id)
values (
           300,
           "2023-01-11 20:52:40.000000",
           "Teaching",
           "math lesson",
           (select a.id
            from accounts a
            where a.name = "Current" and a.user_id = (select u.id from users u where u.email = "honza@gmail.com")),
           (select c.id from categories c where c.name = "Income")
       );
insert into records (amount, date, label, note, account_id, category_id)
values (
           300,
           "2022-12-11 20:52:40.000000",
           "Teaching",
           "math lesson",
           (select a.id
            from accounts a
            where a.name = "Current" and a.user_id = (select u.id from users u where u.email = "honza@gmail.com")),
           (select c.id from categories c where c.name = "Income")
       );
insert into records (amount, date, label, note, account_id, category_id)
values (
           5000,
           "2023-02-11 20:52:40.000000",
           "schoolarship",
           "for good studies",
           (select a.id
            from accounts a
            where a.name = "Savings" and a.user_id = (select u.id from users u where u.email = "honza@gmail.com")),
           (select c.id from categories c where c.name = "Income")
       );
insert into records (amount, date, label, note, account_id, category_id)
values (
           -150,
           "2023-02-11 20:52:40.000000",
           "climbing",
           "entrance and renting climbing shoes",
           (select a.id
            from accounts a
            where a.name = "Current" and a.user_id = (select u.id from users u where u.email = "matej@gmail.com")),
           (select c.id from categories c where c.name = "Sport")
       );