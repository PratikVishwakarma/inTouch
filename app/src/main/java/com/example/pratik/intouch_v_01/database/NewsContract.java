package com.example.pratik.intouch_v_01.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by prati on 21-Oct-16.
 */

public class NewsContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private NewsContract() {}

    public static final String CONTENT_AUTHORITY = "com.example.pratik.intouch_v_01";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_NEWS = "news";

    public static final class NewsEntry implements BaseColumns {
        public static final String TABLE_NAME = "news";
        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_NEWSID = "newsid";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_HEADLINE = "headline";
        public static final String COLUMN_NEWS_CONTENT = "news_content";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_SOURCE = "source";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_NEWS);

        /**
         * Possible values for the gender of the pet.
         */
        public static final String CATEGORY_SCIENCE = "science";
        public static final String CATEGORY_TREND = "trend";
        public static final String CATEGORY_INTERNATIONAL = "international";
        public static final String CATEGORY_SPORTS = "sports";
        public static final String CATEGORY_BUSINESS= "business";
        public static final String CATEGORY_POLITICS = "politics";
        public static final String CATEGORY_TECHNOLOGY = "technology";


    }
}
