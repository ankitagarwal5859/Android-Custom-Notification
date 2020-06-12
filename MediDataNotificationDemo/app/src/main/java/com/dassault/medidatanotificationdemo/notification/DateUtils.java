package com.dassault.medidatanotificationdemo.notification;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static DateFormat format = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
    public static DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
    /**
     * This method takes start date and end date and calculate date based on percentage passed between 2 dates
     *
     * @param startDate
     * @param endDate
     * @param percentage
     * @return
     */
    public static Date getDateAfterCompletion(String startDate, String endDate, int percentage) {
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(startDate);
            d2 = format.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = d2.getTime() - d1.getTime();
        long findTime = (diff * percentage) / 100;
        Date dateAfterCompletePercentage = new Date(d1.getTime() + findTime);
        return dateAfterCompletePercentage;
    }


}
