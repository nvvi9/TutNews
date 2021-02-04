package com.nvvi9.tutnews.data

enum class NewsCategory(val categoryName: String) {

    MAIN_NEWS("Главные новости") {
        override val urlRoute: String = "index"
        override val subCategories: List<String> = emptyList()
    },
    MONEY_AND_POWER("Деньги и власть") {
        override val urlRoute: String = "economics"
        override val subCategories: List<String> = emptyList()
    },
    SOCIETY("Общество") {
        override val urlRoute: String = "society"
        override val subCategories: List<String> = emptyList()
    },
    WORLD("В мире") {
        override val urlRoute: String = "world"
        override val subCategories: List<String> = emptyList()
    },
    HORIZON("Кругозор") {
        override val urlRoute: String = "culture"
        override val subCategories: List<String> = emptyList()
    },
    INCIDENTS("Происшествия") {
        override val urlRoute: String = "accidents"
        override val subCategories: List<String> = emptyList()
    },
    FINANCE("Финансы") {
        override val urlRoute: String = "finance"
        override val subCategories: List<String> = listOf(
            "Личный счет",
            "Публичный счет",
            "Эксклюзив",
            "Банки",
            "Недвижимость"
        )
    },
    REALTY("Недвижимость") {
        override val urlRoute: String = "realty"
        override val subCategories: List<String> = listOf(
            "Экспертиза",
            "От застройщика",
            "Строительство",
            "Аренда",
            "Деньги",
            "Интерьер, дизайн, ремонт"
        )
    },
    SPORT("Спорт") {
        override val urlRoute: String = "sport"
        override val subCategories: List<String> = listOf(
            "Чемпионат Беларуси по футболу",
            "Биатлон",
            "Хоккей",
            "Футбол",
            "Теннис",
            "Баскетбол",
            "Гандбол",
            "Околоспорт"
        )
    },
    HEALTH("Здоровье") {
        override val urlRoute: String = "health"
        override val subCategories: List<String> =
            listOf(
                "ЗОЖ",
                "Медицинские новости",
                "Правильное питание",
                "Врачи",
                "Болезни",
                "Тренировки",
                "Красота",
                "Медицинские новости",
                "Психология",
                "Лекарства"
            )
    },
    AUTOMOBILE("Авто") {
        override val urlRoute: String = "auto"
        override val subCategories: List<String> = listOf(
            "Дорога",
            "Тест-драйвы",
            "Видео",
            "Эксклюзив",
            "Автобизнес",
            "Происшествия"
        )
    },
    LADY("Леди") {
        override val urlRoute: String = "lady"
        override val subCategories: List<String> = listOf(
            "Тело",
            "Вкус жизни",
            "Отношения",
            "Стиль",
            "Карьера",
            "Звезды",
            "Вдохновение",
            "Еда",
            "Анонсы"
        )
    },
    TECH("42") {
        override val urlRoute: String = "it"
        override val subCategories: List<String> = listOf(
            "В Беларуси",
            "Наука",
            "Интернет и связь",
            "Гаджеты",
            "Игры",
            "Оружие"
        )
    },
    ADVERTISING("Афиша") {
        override val urlRoute: String = "afisha"
        override val subCategories: List<String> = listOf(
            "Кино",
            "Концерты",
            "Онлайн-афиша",
            "Вечеринки",
            "Спектакли",
            "Выставки",
            "Цирк",
            "Детям",
            "Другое",
            "Бесплатно",
            "Новости",
            "Онлайн-кинотеатры"
        )
    },
    POPCORN("Попкорн") {
        override val urlRoute: String = "popcorn"
        override val subCategories: List<String> = emptyList()
    },
    COMPANY_NEWS("Новости компаний") {
        override val urlRoute: String = "press"
        override val subCategories: List<String> = emptyList()
    };

    abstract val urlRoute: String

    abstract val subCategories: List<String>

    companion object {

        fun getByCategoryName(categoryName: String) =
            values().find {
                it.categoryName == categoryName || it.subCategories.contains(categoryName)
            }
    }
}