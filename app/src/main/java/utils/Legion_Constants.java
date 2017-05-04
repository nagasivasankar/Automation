package utils;

import android.os.Environment;

import co.legion.client.BuildConfig;

/**
 * Created by Administrator on 11/17/2016.
 */

public interface Legion_Constants {

    String DATE_TIME_FORMAT_IN_FILE_NAMES                        = "yyyyMMdd_HHmmss";
    String IMAGE_FILE_EXTENSION                                  = ".png";
    String SDCARD_PATH                                           = String.valueOf(Environment.getExternalStorageDirectory());
    int PROFILE_PIC_BITMAP_WIDTH_HEIGHT                          = 400;
    int ONE_SECOND                                               = 1000;
    int TIME_OUT_FOR_SERVICE_REQUEST                             = 20 * ONE_SECOND;
    String CONTENT_TYPE_APPLICATION_JSON                         = "application/json";

    class WebServiceRequestCodes {
        public static final int GET_SCHEDULES_REQUEST_CODE        = 1;
        public static final int GET_WEEKLY_SCHEDULES_REQUEST_CODE = 2;
        public static final int GET_SCHEDULE_SUMMARY_REQUEST_CODE = 3;
        public static final int GET_BUSINESS_DETAILS_CODE = 6;
        public static final int GET_WELCOME_SCREEEN_DETAILS_CODE = 4;
        public static final int GET_PROFILE_UPDATE_CODE = 5;
        public static final int GET_ENTERPRISE_DETAILS_CODE = 7;
        public static final int GET_WORKER_ACCOUNT_DETAILS_CODE = 8;
        public static final int LOGIN = 0;
        public static final int SCHEDULED_PREFRERNCES = 9;
        public static final int QUERY_AVAILABILITY_CODE = 10;
        public static final int QUERY_AVAILABILITY_PREFRENCE_CODE = 11;
        public static final int UPDATE_SCHEDULED_PREFS_CODE = 12;
        public static final int GET_SHIFT_OFFERS_REQUEST_CODE    = 13;
        public static final int UPDATE_AVAILABILITY             = 14;
        public static final int GET_PTO_REQUEST_CODE             = 15;
        public static final int GET_SCHEDULED_RATING             = 16;
        public static final int SCHEDULE_SEEN = 17;
        public static final int SWAP_REQUEST_LIST_CODE = 18;
        public static final int SWAP_SUBMIT_REQ_CODE =  19;
        public static final int SWAP_SUBMIT_DROP_REQ_CODE = 20;
        public static final int GET_SWAP_SHIFT_OFFERS           = 21;

        public static final int CANCEL_SWAP_REQ_CODE = 22;
        public static final int CANCEL_DROP_REQ_CODE = 23;

        public static final int BOOKMARK_SHIFT_OFFER   = 24;
        public static final int UNBOOKMARK_SHIFT_OFFER   = 25;
        public static final int DECLINE_SHIFT_OFFER   = 26;
        public static final int CLAIM_SHIFT_OFFER   = 27;
        public static final int SHIFT_SWAP_COUNT_REQ_CODE = 28;
        public static final int SEEN_SHIFT_OFFER_REQUEST_CODE = 29;
        public static final int VERIFY_IDENTITY_REQUEST_CODE = 30;
        public static final int CREATE_ACCOUNT_CODE = 31;
        public static final int GET_SHIFT_OFFER_DETAILS = 32;
    }

    class BaseAPI{
        public static final String STAGING_BASE_URL               = "https://enterprise-stage.legion.work/";
        public static final String PRODUCTION_BASE_URL            = "https://enterprise.legion.work/";
        public static final String BASE_URL                       = BuildConfig.BASE_URL;
    }

    class ServiceUrls {
        public static final String LOGIN_URL                      = BaseAPI.BASE_URL + "legion/authentication/mlogin";
        public static final String ONBOARDING_COMPLTED_TIMESTAMP  = BaseAPI.BASE_URL + "legion/worker/account/";
        public static final String SCHEDULE_PREFERENCES           = BaseAPI.BASE_URL + "legion/worker/querySchedulePreferences?workerId=";
        public static final String PROFILE_UPDATE_URL             = BaseAPI.BASE_URL + "legion/worker/update";
        public static final String UPDATE_SCEDULED_PREFS_URL      = BaseAPI.BASE_URL + "legion/worker/updateSchedulePreferences";
        public static final String GET_SCHEDULES_URL              = BaseAPI.BASE_URL + "legion/worker/getWeeklyscheduleRating/me";
        public static final String GET_WEEK_SCHEDULES_URL         = BaseAPI.BASE_URL + "legion/workershifts/getWorkerShiftsForUser/me";
        public static final String GET_SCHEDULE_SUMMERY_URL       = BaseAPI.BASE_URL + "legion/workershifts/getWorkerShiftsSummaryForUser/me";
        public static final String GET_BUSINESS_DETAILS_URL       = BaseAPI.BASE_URL + "legion/business/getBusiness/";
        public static final String GET_ENTERPRISE_DETAILS_URL     = BaseAPI.BASE_URL + "legion/business/queryEnterpriseById?enterpriseId=";
        public static final String WORKER_ACCOUNT_DETAILS_URL     = BaseAPI.BASE_URL + "legion/worker/account/";
        public static final String QUERY_AVAILALIABLITY_URL       = BaseAPI.BASE_URL + "legion/worker/queryAvailability";
        public static final String GET_SHIFT_ORDERS_URL           = BaseAPI.BASE_URL + "legion/workers/me/shiftOffers";
        public static final String UPDATE_AVAILABILITY_PREFERENCES= BaseAPI.BASE_URL + "legion/worker/updateAvailability";
        public static final String GET_PTO_URL                    = BaseAPI.BASE_URL + "legion/worker/queryPTO/me";
        public static final String MARK_SCHEDULE_SEEN             = BaseAPI.BASE_URL + "legion/worker/markScheduleAsSeen/";
        public static final String GET_SWAP_REQUEST_LIST_URL      = BaseAPI.BASE_URL  + "legion/workers/me/replacementShifts/";
        public static final String SWAP_REQUEST_SUBMIT_LIST_URL   = BaseAPI.BASE_URL  + "legion/workers/me/shiftSwapOffers";
        public static final String SWAP_REQUEST_DROP_LIST_URL     = BaseAPI.BASE_URL  + "legion/workers/me/shifts/";
        public static final String GET_SWAP_SHIFT_OFFERS          = BaseAPI.BASE_URL  + "legion/workers/me/shiftSwapOffers";
        public static final String UPDATE_SHIFT_OFFER_URL         = BaseAPI.BASE_URL  + "legion/workers/me/shiftOffers/";
        public static final String UPDATE_SWAP_SHIFT_OFFER_URL    = BaseAPI.BASE_URL  + "legion/workers/me/shiftSwapOffers/";
        public static final String VERIFY_IDENTITY_URL            = BaseAPI.BASE_URL  + "legion/worker/activate";
        public static final String SETUP_CREDENTIALS_URL          = BaseAPI.BASE_URL  + "legion/worker/setupCredentials";
    }

    class Extras_Keys {
        public static final String IS_LOGGED_OUT                 = "isLoggedOut";
        public static final String WEEKDAY_TV                    = "weekDayTv";
        public static final String SCHEDULE_DETAILS_DATA         = "scheduledData";
        public static final String SCHEDULED_PREFRENCES          = "scheduledPrefs" ;
        public static final String POSITION                      = "position";
        public static final String CURRENT_SLOT_START_TIME       = "currentSlotStartTime";
        public static final String CURRENT_SLOT_END_TIME         = "currentSlotEndTime";
        public static final String PICKER_START_TIME             = "pickerStartTime";
        public static final String PICKER_END_TIME               = "pickerEndTime";
        public static final String THIS_WEEK_OR_NOT              = "SelectedWeekOrNot";
        public static final String STARTTIME                     = "satrtTime";
        public static final String ENDTIME                       = "endTime";
        public static final String SHIFT_MINS                    = "shiftMins";
        public static final String TOTAL_SHIFT_MINS_SHIFT_OFFERS = "shiftMins";
        public static final String NOTIFICATION_BUSINESS_ID      = "businessId";
        public static final String NOTIFICATION_YEAR             = "year";
        public static final String NOTIFICATION_WEEKSTARTDAYOFTHEYEAR = "weekStartDayOfTheYear";
        public static final String IS_SEEN                          = "isSeen";
        public static final String MESSAGE                          = "message";
        public static final String REMOVEDSHIFTS_LIST               = "shiftsRemoved";
        public static final String WORKER_NAME                      =  "workerNmae";
        public static final String COMING_FROM_NOTIFICATION         = "comingFromNotification";
        public static final String TYPE                             = "type";
        public static final String NOTIFICATION_SHIFTID             = "shiftId";
        public static final String PROFILE_OBJECT                   = "profileObject";
        public static String SCHEDULE_ID                            = "scheduleId";
        public static String START_DATE ;
        public static String END_DATE ;
        public static String FIRST_TIME_TITLE_UPDATED;
        public static String WORKSHIFTS_LIST                        = "workerShiftsList";
        public static String TOTAL_HOURS_SHIFT_OFFERS                        = "tatalhoursshiftoffers";
        public static String STATUS_RESPONSE_KEY                        = "statusresponsekey";
        public static String SHIFT_OFFER_KEY                       = "shiftofferkey";
        public static final String SELECTED_WORKERSHIFT_LIST        =  "selectedworkerShiftsList";
        public static final int REQUEST_CODE                        =  1001;
        public static final String OFFER_ID                      =  "offerId";
    }

    class Prefs_Keys {
        public static final String SESSION_ID                    = "SessionId";
        public static final String USER_NAME                     = "userName";
        public static final String WORKER_ID                     = "workerId";
        public static final String IS_LOGGED_IN                  = "isLoggedIn";
        public static final String IS_ON_BOARDING_STARTED        = "onBoardingStarted";
        public static final String ENTERPRISE_ID                 = "enterpriseId";
        public static final String WELCOME_SCREEN_LOGO_URL       = "logourl";
        public static final String WELCOME_SCREEN_PIC_URL        = "photo";
        public static final String WELCOME_SCREEN_ADDRESS        = "adress";
        public static final String BUSSINESS_KEY                 = "businessKey";
        public static final String DISPALY_NAME                  = "displayname";
        public static final String PROFILE_PIC_URL               = "profile_pic";
        public static final String PHONE_NUMBER                  = "phoneNumber";
        public static final String HOURS_PER_WEEK                = "hoursPerWeek";
        public static final String FIRST_NAME                    = "firstName";
        public static final String LAST_NAME                     = "lastName";
        public static final String EMAIL                         = "email";
        public static final String BUSINESS_FIRST_DAY_OF_WEEK    = "firstDayOfWeek";
        public static final String BUSINESS_PHONE_NUMBER         =  "businessPhoneNumber";
        public static final String PADDING_BIG                   = "paddingBig";
        public static final String PADDING_LESS                  = "paddingLess";
        public static final String MIN_WIDTH                     = "minWidth";
        public static final String PADDING_50                    = "padding50";
        public static final String SIZE                          = "size";
        public static final String START_DATE                    = "startDate";
        public static final String END_DATE                      = "endDate";
        public static final String OFFER_STATUS                  = "savedOfferStatus";
        public static final String OFFER_TYPE                    = "shiftType";
        public static final String TOTAL_SHIFT_HOURS             = "totalShiftHours";
        public static final String TOTAL_SHIFT_MINS              = "totalShiftMins";
        public static final String WEEKLY_SCHEDULES              = "weeklySchedules";
        public static final String WEEKLY_SCHEDULES_SUMMARY      = "weeklySchedulesSummary";
        public static final String WEEKLY_SCHEDULES_RATING       = "weeklyScheduledRating";
        public static final String REFRESH                       = "refresh";
        public static final String FIRST_TIME                    = "firstTime";
        public static final String BUSINESS_ID                   = "businessId";
        public static final String NICK_NAME                     = "nickName";
        public static final String SHIFT_OFFERS_INDICATOR_DATA   = "shiftofferindicatordata";
        public static final String BUSINESS_TIMEZONE             = "businessTimezone";
        public static final String CALENDAR_NAME                 = "calendar_name";
        public static final String SELECTED_TIME                 = "selectedTime";
        public static final String SELECTED_TIME_IN_MINS         = "selectedTimeInMins";
        public static final String SWITCH_CALENDAR               = "switch";
    }

    class Prefs_Keys_Offline {
        public static final String SCHEDULE_PREFREENCES = "schedulePrefs";
        public static final String PROFILE_OBJECT ="profileObj";
        public static final String SCHEDULES_LIST_TAB_FRAGEMNT = "schedulesList";
        public static final String AVALIABILTY_PREFS = "availabilityPrefs";
        public static final String GET_SCHEDULES_COUNT = "getSchedulesCount";
        public static final String GET_SFIFTOFFERS_COUNT = "getShiftOffersCount";
        public static final String GET_SWAPSHIFT_COUNT = "getSwapshiftCount";
        public static final String RC_CURRENT_WEEK_SHIFTS ="rcCrntWkShts";
        public static final String RC_LAST_WEEK_SHIFTS = "rcLstWeekShfts";
        public static final String GET_NEXTWEEK_SHIFTS = "rcNextWeekShifts";
        public static final String JSON_OBJ_VAL = "jsonObjVal";
        public static final String ONBOARDING = "onBoarding" ;
    }


    class ResponseParserConstants{
        public static final int PARSER_CONSTANT                         = 1;
        public static final int PARSE_WEEKLY_SHIFTS_OF_WORKER_RESPONSE  = PARSER_CONSTANT + 1 ;
        public static final int NOTIFICATION_SHIFTS_OF_WORKER_RESPONSE  = PARSER_CONSTANT + 2;
        public static final int PARSE_SHIFT_OFFERS                      = PARSER_CONSTANT + 3;
        public static final int PARSE_SHIFT_SWAP_OFFERS                 = PARSER_CONSTANT + 4;
        public static final int PARSE_SCHEDULE_SUMMARY_DETAILS          = PARSER_CONSTANT + 5;
        public static final int PARSE_SCHEDULES                         = PARSER_CONSTANT + 6;
        public static final int PARSE_PTO_REQUEST_CODE                  = PARSER_CONSTANT + 7;
    }
}
