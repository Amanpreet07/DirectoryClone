package directoryclone;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateManip {

    public static String getCurrentDT(String type) {
        Date date = new Date();
        // for detailed output
        SimpleDateFormat all = new SimpleDateFormat("EEEE dd-MMM-yyyy hh:mm:ss a");
        // pattern for date
        SimpleDateFormat dateonly = new SimpleDateFormat("dd-MMM-yyyy");
        // pattern for time with am/pm
        SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss a");
        // pattern for day only
        SimpleDateFormat day = new SimpleDateFormat("EEEE");
        // pattern for month name only
        SimpleDateFormat month = new SimpleDateFormat("MMMM");
        
        switch (type) {
            case "all":
                return all.format(date);
            case "date":
                return dateonly.format(date);
            case "time":
                return time.format(date);
            case "day":
                return day.format(date);
            case "month":
                return month.format(date);
            default:
                // returns the date with pattern specified explicitly
                return new SimpleDateFormat(type).format(date);
        }
    }
    
    public static String getDateString(String type, Date d) {
     
        // for detailed output
        SimpleDateFormat all = new SimpleDateFormat("EEEE dd-MMM-yyyy hh:mm:ss a");
        // pattern for date
        SimpleDateFormat dateonly = new SimpleDateFormat("dd-MMM-yyyy");
        // pattern for time with am/pm
        SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss a");
        // pattern for day only
        SimpleDateFormat day = new SimpleDateFormat("EEEE");
        // pattern for month name only
        SimpleDateFormat month = new SimpleDateFormat("MMMM");
        
        switch (type) {
            case "all":
                return all.format(d);
            case "date":
                return dateonly.format(d);
            case "time":
                return time.format(d);
            case "day":
                return day.format(d);
            case "month":
                return month.format(d);
            default:
                // returns the date with pattern specified explicitly
                return new SimpleDateFormat(type).format(d);
        }
    }

    public static Date getDateObj(String val, String type) {

        Date temp = null;
        SimpleDateFormat all = new SimpleDateFormat("EEEE dd-MMM-yyyy hh:mm:ss a");
        SimpleDateFormat dateonly = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss a");
        SimpleDateFormat day = new SimpleDateFormat("EEEE");
        SimpleDateFormat month = new SimpleDateFormat("MMMM");
        try {
            switch (type) {
                case "all":
                    temp = all.parse(val);
                    break;
                case "date":
                    temp = dateonly.parse(val);
                    break;
                case "time":
                    temp = time.parse(val);
                    break;
                case "day":
                    temp = day.parse(val);
                    break;
                case "month":
                    temp = month.parse(val);
                    break;
                default:
                    // to explicitly specify the format 
                    temp = new SimpleDateFormat(type).parse(val);
            }
        } catch (ParseException ex) {
            temp = null;
        }
        return temp;
    }

    public static String timeInBetween(Date d1, Date d2) {
        // d1 is most likely to be present date in the software
        // d2 is to be compared with

        long temp1 = d1.getTime();
        long temp2 = d2.getTime();
        long diff = temp2 - temp1;
        long dd = 0, hh = 0, mm = 0, ss = 0;
        dd = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        hh = TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS);
        mm = TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS);
        ss = TimeUnit.SECONDS.convert(diff, TimeUnit.MILLISECONDS);

        if (diff == 0) {   // if both have same values
            return "0 Seconds";
        } else if (diff < 0) {
            // for negative values, simply reverse the parameters with '-' prefix                                                   
            return "-" + timeInBetween(d2, d1);
        } else { // d2 is greater i.e later in time
            if (dd >= 1) {
                return dd + " Day(s) " + (hh - (dd * 24)) + " Hour(s)";
            } else if (hh >= 1) {
                return hh + " Hour(s)" + (mm - (hh * 60)) + " Minute(s)";
            } else if (mm >= 1) {
                return mm + " Minute(s)" + (ss - (mm * 60)) + " Second(s)";
            } else if (ss >= 1) {
                return ss + " Second(s)";
            } else {
                return "Now";
            }
        }
    }

    public static int compareDates(Date d1, Date d2) {
        // to check for d1 in respect to d2.
        // -1 for before, 0 for equal and 1 for after
        return d1.compareTo(d2);
    }
    
    public static int compareDates(String d1, String d2, String format){
        int status = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date1 = sdf.parse(d1);
            Date date2 = sdf.parse(d2);
            status = date1.compareTo(date2);
        } catch (ParseException ex) {
            status = 5097; // to know that exception occured(Secret number XD)
        }
       return status;       
    }
}
