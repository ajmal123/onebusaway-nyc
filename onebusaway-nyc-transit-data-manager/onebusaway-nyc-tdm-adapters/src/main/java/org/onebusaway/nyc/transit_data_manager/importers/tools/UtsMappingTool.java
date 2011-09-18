package org.onebusaway.nyc.transit_data_manager.importers.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class UtsMappingTool {
  public static String UTS_DATE_FIELD_DATEFORMAT = "yyyy-MM-dd";
  public static String UTS_TIMESTAMP_FIELD_DATEFORMAT = "yyyy-MM-dd HH:mm:ss";

  public static String TIMEFORMAT_HHMMSS = "HH:mm:ss";

  public static String BUS_DESIGNATOR = "MTA NYCT";

  private static long NYCT_AGENCY_ID = new Long(2008);
  private static long MTA_AGENCY_ID = new Long(2188);

  public UtsMappingTool() {
    super();
  }

  public Long getAgencyIdFromDepotAssignAgency(int value) {
    return MTA_AGENCY_ID;
  }

  public Long getAgencyIdFromUtsAuthId(String authId) {
    Long agencyId = new Long(-1);

    if ("TA".equals(authId) || "OA".equals(authId)) {
      agencyId = MTA_AGENCY_ID;
    } else if ("RB".equals(authId)) {
      agencyId = MTA_AGENCY_ID;
    }

    return agencyId;
  }

  // These methods use the Joda DateTime class
  public String dateDateTimeToDateString(DateTime dt) {
    DateTimeFormatter dtf = DateTimeFormat.forPattern(UTS_DATE_FIELD_DATEFORMAT);
    return dtf.print(dt);
  }

  public String timestampDateTimeToDateString(DateTime dt) {
    DateTimeFormatter dtf = DateTimeFormat.forPattern(UTS_TIMESTAMP_FIELD_DATEFORMAT);
    return dtf.print(dt);
  }

  public ReadableDateTime parseUtsDateToDateTime(String dateStr,
      String formatStr) {
    ReadableDateTime result = null;

    DateTimeFormatter dtf = DateTimeFormat.forPattern(formatStr);
    DateTime date = dtf.parseDateTime(dateStr);

    if (UTS_DATE_FIELD_DATEFORMAT.equals(formatStr)) {
      DateMidnight dm = new DateMidnight(date);
      date = new DateTime(dm);
    } else if (UTS_TIMESTAMP_FIELD_DATEFORMAT.equals(formatStr)) {
      result = date;
    }

    return result;
  }

  /***
   * This function is designed to calculate the actual date of a pull in or out
   * based on both the input time string (with a suffix of A,P,B,X) and a base
   * date (likely the service date). The base date is in 24 hour format, so I
   * think we can safely assume that the AM/PM aspect is uneccessary, only that
   * the time is the same day, the day before or the day after is needed.
   * 
   * The possible suffixes are as follows: A - The time is AM, of the same date
   * as the baseDate P - The time is PM, of the same date as the baseDate B -
   * The time is PM, the date before the baseDate X - The time is AM, the date
   * after the baseDate
   * 
   * @param baseDate The reference date that the time suffix refers to.
   * @param suffixedTime A time in the format 02:53Z, where Z is a suffix
   *          representing one of four possible half days.
   * @return A DateTime with the date incorporating the suffix, or null if the
   *         suffixedTime string didn't match the input format.
   */
  public DateTime calculatePullInOutDateFromDateUtsSuffixedTime(
      DateTime baseDate, String suffixedTime) {
    DateTime result = null;

    Pattern timeSuffixPattern = Pattern.compile("(.{2}):(.{2})([APBX])");
    Matcher timeSuffixMatcher = timeSuffixPattern.matcher(suffixedTime);

    int hours, minutes;
    String suffix = null;

    if (timeSuffixMatcher.find()) {
      MutableDateTime modDate = baseDate.toMutableDateTime();

      hours = Integer.parseInt(timeSuffixMatcher.group(1));
      minutes = Integer.parseInt(timeSuffixMatcher.group(2));
      suffix = timeSuffixMatcher.group(3);

      modDate.setHourOfDay(hours);
      modDate.setMinuteOfHour(minutes);

      if ("B".equals(suffix)) { // suffixedTime is the day before the baseDate
        modDate.addDays(-1);
      } else if ("X".equals(suffix)) { // suffixedTime is the day after the
                                       // basedate
        modDate.addDays(1);
      } // if AM or PM, do nothing.

      result = modDate.toDateTime();
    } else {
      // What do we do when the time parsing fails - should throw an exception
      // here.
    }

    return result;
  }

}