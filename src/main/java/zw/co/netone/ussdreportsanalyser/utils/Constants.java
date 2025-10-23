package zw.co.netone.ussdreportsanalyser.utils;

import java.util.Locale;

public interface Constants {
    String AUDIT_DATE_FORMAT ="dd/MM/yyyy";
    String INVALID_FILE_FORMAT = "Only xls or xlsx formats  are allowed";
    String XLSX_FILE_FORMAT = ".xlsx";
    String XLS_FILE_FORMAT = ".xls";
    String FILE_NOT_FOUND = "File not found ";

    interface ResponseCodes {
        interface Acs {
            String SUCCESS = "200";
        }
    }
    int TEMPORARY_SCALE = 8;
    
    
    
    
    int FINAL_SCALE = 4;
    int DISPLAY_SCALE = 2;
    int PRECISION = 19;

    interface ContextDataKeys {
        String SELECTED_OPTION = "SELECTED_OPTION";
        String REQUEST_SOURCE = "REQUEST_SOURCE";
        String CONDITIONAL_MENU_OPTIONS = "CONDITIONAL_MENU_OPTIONS";
        String NEXT_HANDLER_CLASS = "NEXT_HANDLER_CLASS";
        String VEHICLES_TO_BE_DEREGISTERED = "VEHICLES_TO_BE_DEREGISTERED";
        String PREVIOUS_MENU_POSITION = "PREVIOUS_MENU_POSITION";
        String MAIN_MENU_POSITION = "MAIN_MENU_POSITION";
        String PREVIOUS_MENU_ID = "PREVIOUS_MENU_ID";
        String AVAILABLE_OPTIONS = "AVAILABLE_OPTIONS";
        String ERROR_MESSAGE = "ERROR_MESSAGE";
        String CURRENT_LOAN_BALANCE = "CURRENT_LOAN_BALANCE";
        String SUCCESS_MESSAGE = "SUCCESS_MESSAGE";
        String CURRENCY_SYMBOL = "CURRENCY_SYMBOL";
        String SELECTED_CITY = "SELECTED_CITY";
        String SELECTED_TOLLGATE = "SELECTED_TOLLGATE";
        String SELECTED_TOLLGATE_NAME = "SELECTED_TOLLGATE_NAME";
        String SELECTED_VEHICLE_TYPE = "SELECTED_VEHICLE_TYPE";
        String SELECTED_VEHICLE_TYPE_NAME = "SELECTED_VEHICLE_TYPE_NAME";
        String DEFAULT_CONTENT_TYPE = "application/octet-stream";

        String COUNTRY_CODE = "COUNTRY_CODE";
        String AMOUNT = "AMOUNT";


        String VEHICLE_REG_NUMBER = "VEHICLE_REG_NUMBER";
        String PHONE_NUMBER = "PHONE_NUMBER";
        String CASHIER_PHONE_NUMBER = "CASHIER_PHONE_NUMBER";
        String CASHIER_ID = "CASHIER_ID";
        String INITIATED_BY_CASHIER = "INITIATED_BY_CASHIER";
        String TRANSACTION_TO_POST = "TRANSACTION_TO_POST";
    }

    interface ContextDataValues {
        String SELF_INITIATED_CREDIT = "self";
        String AUTOMATIC_CREDIT = "auto";
    }

    interface Parameters {
        String REQUEST_SOURCE = "acs";
        String API_KEY = "YWNzMTQxMDIwMTUxNTA4";
    }


    interface ResponseMessages {
        String INVALID_SELECTION = "The option you supplied is invalid";
        String PROCESSING_ERROR = "An error occured while processing your apirequest. Please try again later";
    }

    String ACCEPT_OPTION="1";

    String USSD_REQUEST_DESIGNATOR = "*";

    String USSD_END_REQUEST_DESIGNATOR = "#";

    Locale DEFAULT_LOCALE = new Locale("en");
}
