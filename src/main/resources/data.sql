insert into categories (icon, name, color) values ("mdi-shopping-outline", "Shopping", "#F44336");
insert into categories (icon, name, color) values ("mdi-food-outline", "Food & Drinks", "#388E3C");
insert into categories (icon, name, color) values ("mdi-home-lightning-bolt-outline", "Housing", "#388E3C");
insert into categories (icon, name, color) values ("mdi-train-car", "Transportation", "#388E3C");
insert into categories (icon, name, color) values ("mdi-car-outline", "Vehicle", "#F44336");
insert into categories (icon, name, color) values ("mdi-drama-masks", "Life & Entertainment", "#6200EA");
insert into categories (icon, name, color) values ("mdi-basketball", "Sport", "#388E3C");
insert into categories (icon, name, color) values ("mdi-laptop", "Communication, PC", "#6200EA");
insert into categories (icon, name, color) values ("mdi-bank-outline", "Financial expenses", "#388E3C");
insert into categories (icon, name, color) values ("mdi-chart-waterfall", "Investments", "#6200EA");
insert into categories (icon, name, color) values ("mdi-hand-coin-outline", "Income", "#FFAB00");
insert into categories (icon, name, color) values ("mdi-airplane", "Travelling", "#FFAB00");
insert into categories (icon, name, color) values ("mdi-triangle-outline", "Others", "#CFD8DC");
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