package utils;

/***************************************************************************************************
 * Created by zyuki on 2/26/2016.
 *
 * Class used to facilitate
 **************************************************************************************************/
public class Constant {
    public static final int ERROR = -1;

    public class Adapter {
        public static final int DAYS_TYPE_COUNT = 2;
        public static final int TODO_TYPE_COUNT = 2;

        public static final int TYPE_DIVIDER = 0;
        public static final int TYPE_CONTENT = 1;
    }

    public class Fragment {
        public static final String TAG_DIALOG = "dialog";

        public static final String BUNDLE_KEY_YEAR = "year";
        public static final String BUNDLE_KEY_WEEK = "week";

        public static final String BUNDLE_KEY_DAY_TYPE = "dayType";
        public static final String BUNDLE_KEY_ITEM_POS = "itemPos";

        public static final int DAY_TYPE_WEEKDAY = 2001;
        public static final int DAY_TYPE_WEEKEND = 2002;
    }

    public class Model {
        public static final int DIVIDER_LUNCH = 101;
        public static final int DIVIDER_WORK = 102;
        public static final int DIVIDER_DAILY = 103;

        public static final int CONTENT_LUNCH = 201;
        public static final int CONTENT_WORK = 202;
        public static final int CONTENT_DAILY = 203;
    }

    public class Util {
        public static final int FORMAT_FULL_DATE = 301;
        public static final int FORMAT_MONTH = 302;
        public static final int FORMAT_DAY_OF_MONTH = 303;
    }
}
