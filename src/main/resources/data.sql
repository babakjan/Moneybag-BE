insert into categories (icon, name, color) values ("mdi-food-apple-outline", "Food, Groceries", "#6bff01");
insert into categories (icon, name, color) values ("mdi-silverware", "Restaurant, bar, cafe", "#50bf01");
insert into categories (icon, name, color) values ("mdi-laptop", "Communication, PC, digital", "#ff5379");
insert into categories (icon, name, color) values ("mdi-teddy-bear", "Kids", "#ff4638");
insert into categories (icon, name, color) values ("mdi-shopping-outline", "Shopping other", "#cf392d");
insert into categories (icon, name, color) values ("mdi-cellphone", "Electronic", "#b53227");
insert into categories (icon, name, color) values ("mdi-hanger", "Clothes & shoes", "#9e2c22");
insert into categories (icon, name, color) values ("mdi-home-lightning-bolt-outline", "Housing", "#d59687");
insert into categories (icon, name, color) values ("mdi-train-car", "Transportation", "#f077ff");
insert into categories (icon, name, color) values ("mdi-car-outline", "Vehicle", "#b85bc3");
insert into categories (icon, name, color) values ("mdi-school-outline", "Education", "#94bdff");
insert into categories (icon, name, color) values ("mdi-basketball", "Sport", "#6290ff");
insert into categories (icon, name, color) values ("mdi-drama-masks", "Life & Entertainment", "#547de0");
insert into categories (icon, name, color) values ("mdi-palm-tree", "Holiday, vacation", "#325bff");
insert into categories (icon, name, color) values ("mdi-glass-cocktail", "Alcohol", "#294acf");
insert into categories (icon, name, color) values ("mdi-hand-coin-outline", "Salary, wage", "#FFAB00");
insert into categories (icon, name, color) values ("mdi-cash-multiple", "Other incomes", "#ffc100");
insert into categories (icon, name, color) values ("mdi-bank-outline", "Financial expenses", "#46ffff");
insert into categories (icon, name, color) values ("mdi-chart-line", "Investments", "#6290ff");
insert into categories (icon, name, color) values ("mdi-triangle-outline", "Others", "#070513");
insert into users (email, first_name, last_name, password, role, currency)
    values (
            "admin@gmail.com",
            "admin",
            "admin",
            "$2a$12$URiJQXh/K4N4gL01TDyv4Oa0IF2aUWevTL0Phzb1A5Ypy3GloU726",
            "ADMIN",
            "EUR"
            );
insert into users (email, first_name, last_name, password, role, currency)
    values (
            "matej@gmail.com",
            "Matěj",
            "Babák",
            "$2a$12$URiJQXh/K4N4gL01TDyv4Oa0IF2aUWevTL0Phzb1A5Ypy3GloU726",
            "USER",
            "CZK"
            );
insert into users (email, first_name, last_name, password, role, currency)
    values ("honza@gmail.com",
            "Jan",
            "Babák",
            "$2a$12$URiJQXh/K4N4gL01TDyv4Oa0IF2aUWevTL0Phzb1A5Ypy3GloU726",
            "USER",
            "EUR"
            );
insert into accounts (balance, color, currency, icon, include_in_statistic, name, user_id)
    values (50000,
            "#6290ff",
            "EUR",
            "mdi-cash",
            true,
            "Savings",
            (select u.id from users u where u.email = "honza@gmail.com")
            );
insert into accounts (balance, color, currency, icon, include_in_statistic, name, user_id)
    values (3250,
            "#6290ff",
            "EUR",
            "mdi-cash",
            true,
            "Current",
            (select u.id from users u where u.email = "honza@gmail.com")
            );
insert into accounts (balance, color, currency, icon, include_in_statistic, name, user_id)
    values (1000,
            "#6290ff",
            "EUR",
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
           "2023-02-11 20:52:40.000000",
           "climbing",
           "entrance and renting climbing shoes",
           (select a.id
            from accounts a
            where a.name = "Current" and a.user_id = (select u.id from users u where u.email = "matej@gmail.com")),
           (select c.id from categories c where c.name = "Sport")
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
           (select c.id from categories c where c.name = "Food, Groceries")
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
           (select c.id from categories c where c.name = "Restaurant, bar, cafe")
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
           (select c.id from categories c where c.name = "Alcohol")
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
           (select c.id from categories c where c.name = "Salary, wage")
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
           (select c.id from categories c where c.name = "Salary, wage")
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
           (select c.id from categories c where c.name = "Salary, wage")
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
           (select c.id from categories c where c.name = "Other incomes")
       );
insert into records (amount, date, label, note, account_id, category_id)
values (
           -100,
           "2023-02-11 20:52:40.000000",
           "Spotify",
           "monthly subscription",
           (select a.id
            from accounts a
            where a.name = "Current" and a.user_id = (select u.id from users u where u.email = "honza@gmail.com")),
           (select c.id from categories c where c.name = "Communication, PC, digital")
       );
insert into records (amount, date, label, note, account_id, category_id)
values (
           -250,
           "2023-02-14 20:52:40.000000",
           "Pregnancy test",
           "",
           (select a.id
            from accounts a
            where a.name = "Current" and a.user_id = (select u.id from users u where u.email = "honza@gmail.com")),
           (select c.id from categories c where c.name = "Kids")
       );
insert into records (amount, date, label, note, account_id, category_id)
values (
           -3490,
           "2023-02-15 20:52:40.000000",
           "Chair",
           "DX racer k5",
           (select a.id
            from accounts a
            where a.name = "Current" and a.user_id = (select u.id from users u where u.email = "honza@gmail.com")),
           (select c.id from categories c where c.name = "Shopping other")
       );
insert into records (amount, date, label, note, account_id, category_id)
values (
           -16490,
           "2023-02-15 20:52:40.000000",
           "iPhone",
           "iPhone 13 mini 256gb, Alza",
           (select a.id
            from accounts a
            where a.name = "Current" and a.user_id = (select u.id from users u where u.email = "honza@gmail.com")),
           (select c.id from categories c where c.name = "Electronic")
       );
insert into records (amount, date, label, note, account_id, category_id)
values (
           -1299,
           "2023-02-15 20:52:40.000000",
           "Skate shoes",
           "Vans oldskool Boardstar",
           (select a.id
            from accounts a
            where a.name = "Current" and a.user_id = (select u.id from users u where u.email = "honza@gmail.com")),
           (select c.id from categories c where c.name = "Clothes & shoes")
       );
insert into records (amount, date, label, note, account_id, category_id)
values (
           -15000,
           "2023-02-18 20:52:40.000000",
           "Rent",
           "same as always",
           (select a.id
            from accounts a
            where a.name = "Current" and a.user_id = (select u.id from users u where u.email = "honza@gmail.com")),
           (select c.id from categories c where c.name = "Housing")
       );
insert into records (amount, date, label, note, account_id, category_id)
values (
           -750,
           "2023-02-19 20:52:40.000000",
           "Public transport ticker",
           "Monthly ticket",
           (select a.id
            from accounts a
            where a.name = "Current" and a.user_id = (select u.id from users u where u.email = "honza@gmail.com")),
           (select c.id from categories c where c.name = "Transportation")
       );
insert into records (amount, date, label, note, account_id, category_id)
values (
           -12000,
           "2023-02-20 14:52:40.000000",
           "Tires",
           "4 tires Michellin",
           (select a.id
            from accounts a
            where a.name = "Current" and a.user_id = (select u.id from users u where u.email = "honza@gmail.com")),
           (select c.id from categories c where c.name = "Vehicle")
       );
insert into records (amount, date, label, note, account_id, category_id)
values (
           -200,
           "2023-02-21 14:52:40.000000",
           "Java security course",
           "Udemy course",
           (select a.id
            from accounts a
            where a.name = "Current" and a.user_id = (select u.id from users u where u.email = "honza@gmail.com")),
           (select c.id from categories c where c.name = "Education")
       );
insert into records (amount, date, label, note, account_id, category_id)
values (
           -180,
           "2023-02-22 14:52:40.000000",
           "Cinema",
           "Avatar Cinema city Flora",
           (select a.id
            from accounts a
            where a.name = "Current" and a.user_id = (select u.id from users u where u.email = "honza@gmail.com")),
           (select c.id from categories c where c.name = "Life & Entertainment")
       );
insert into records (amount, date, label, note, account_id, category_id)
values (
           -9500,
           "2023-02-23 14:52:40.000000",
           "Milan",
           "Weekend trip to Milan",
           (select a.id
            from accounts a
            where a.name = "Current" and a.user_id = (select u.id from users u where u.email = "honza@gmail.com")),
           (select c.id from categories c where c.name = "Holiday, vacation")
       );
insert into records (amount, date, label, note, account_id, category_id)
values (
           78000,
           "2023-02-25 14:52:40.000000",
           "Salary",
           "from main job",
           (select a.id
            from accounts a
            where a.name = "Current" and a.user_id = (select u.id from users u where u.email = "honza@gmail.com")),
           (select c.id from categories c where c.name = "Salary, wage")
       );
insert into records (amount, date, label, note, account_id, category_id)
values (
           500,
           "2023-02-26 14:52:40.000000",
           "Dividends",
           "Apple dividends",
           (select a.id
            from accounts a
            where a.name = "Savings" and a.user_id = (select u.id from users u where u.email = "honza@gmail.com")),
           (select c.id from categories c where c.name = "Other incomes")
       );
insert into records (amount, date, label, note, account_id, category_id)
values (
           -1500,
           "2023-02-27 14:52:40.000000",
           "Accountant",
           "Apple dividends",
           (select a.id
            from accounts a
            where a.name = "Current" and a.user_id = (select u.id from users u where u.email = "honza@gmail.com")),
           (select c.id from categories c where c.name = "Financial expenses")
       );
insert into records (amount, date, label, note, account_id, category_id)
values (
           -4000,
           "2023-02-28 14:52:40.000000",
           "ETFs",
           "VUSA.L",
           (select a.id
            from accounts a
            where a.name = "Savings" and a.user_id = (select u.id from users u where u.email = "honza@gmail.com")),
           (select c.id from categories c where c.name = "Investments")
       );
insert into records (amount, date, label, note, account_id, category_id)
values (
           -850,
           "2023-03-1 14:52:40.000000",
           "Gift",
           "Ondra's birthday",
           (select a.id
            from accounts a
            where a.name = "Current" and a.user_id = (select u.id from users u where u.email = "honza@gmail.com")),
           (select c.id from categories c where c.name = "Others")
       );