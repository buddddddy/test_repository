package org.mdlp;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mdlp.web.rest.RestItemList;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static java.lang.Math.min;
import static java.time.ZoneId.systemDefault;

public interface Utils {

    DateTimeFormatter DOC_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    DateTimeFormatter CORE_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter LONG_CORE_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    DateTimeFormatter LONG_CORE_DATE_FORMATTER_WITHOUT_TZ = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    DateTimeFormatter LONG_CORE_DATE_FORMATTER_WITH_3_MS = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Nullable
    @Contract("null->null; !null->!null")
    static String formatDate(@Nullable LocalDate date) {
        if (null == date) return null;
        return date.format(DOC_DATE_FORMATTER);
    }

    @Nullable
    @Contract("null->null; !null->!null")
    static String formatDate(@Nullable LocalDateTime date) {
        if (null == date) return null;
        return date.format(DOC_DATE_FORMATTER);
    }

    @Nullable
    @Contract("null->null; !null->!null")
    static LocalDate parseDate(@Nullable String string) {
        if (null == string || string.isEmpty()) return null;
        return LocalDate.parse(string, DOC_DATE_FORMATTER);
    }

    @Nullable
    @Contract("null->null; !null->!null")
    static LocalDateTime parseDateTime(@Nullable String string) {
        if (null == string || string.isEmpty()) return null;
        return LocalDateTime.parse(string, DOC_DATE_FORMATTER);
    }

    @Nullable
    @Contract("null->null; !null->!null")
    static LocalDate parseCoreDate(@Nullable String string) {
        if (null == string || string.isEmpty()) return null;
        return LocalDate.parse(string, CORE_DATE_FORMATTER);
    }

    @Nullable
    @Contract("null->null; !null->!null")
    static LocalDateTime parseCoreDateTime(@Nullable String string) {
        if (null == string || string.isEmpty()) return null;
        return LocalDateTime.parse(string, LONG_CORE_DATE_FORMATTER);
    }

    @Nullable
    @Contract("null->null; !null->!null")
    static LocalDateTime parseCoreDateTimeWith3Ms(@Nullable String string) {
        if (null == string || string.isEmpty()) return null;
        return LocalDateTime.parse(string, LONG_CORE_DATE_FORMATTER_WITH_3_MS);
    }

    @Nullable
    @Contract("null->null; !null->!null")
    static LocalDate calendarToLocalDate(@Nullable Calendar calendar) {
        if (null == calendar) return null;
        return calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    @Nullable
    @Contract("null->null; !null->!null")
    static LocalDate calendarToLocalDateString(@Nullable Calendar calendar) {
        if (null == calendar) return null;
        return calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    @Nullable
    @Contract("null->null; !null->!null")
    static LocalDateTime calendarToLocalDateTime(@Nullable Calendar calendar) {
        if (null == calendar) return null;
        return calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    @Nullable
    @Contract("null->null; !null->!null")
    static ZonedDateTime calendarToZonedDateTime(@Nullable Calendar calendar) {
        if (null == calendar) return null;
        return calendar.getTime().toInstant().atZone(ZoneId.systemDefault());
    }

    @NotNull
    static Calendar dateToCalendar(@Nullable Date date) {
        if (null == date) date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    @NotNull
    static Calendar dateToCalendar(@Nullable LocalDateTime date) {
        if (null == date) date = LocalDateTime.now();
        return dateToCalendar(Date.from(date.atZone(systemDefault()).toInstant()));
    }

    static boolean contains(@Nullable Object value, @Nullable Object fragment) {
        if (null == fragment) return true;
        String fragmentString = String.valueOf(fragment);
        if (fragmentString.isEmpty()) return true;
        return null != value && value.toString().toLowerCase().contains(fragmentString.toLowerCase());
    }

    static boolean containsInSet(@Nullable Object value, @Nullable Set fragment) {
        if (null == fragment || fragment.size() == 0) return true;
        String fragmentString = String.valueOf(fragment);
        if (fragmentString.isEmpty()) return true;
        return null != value && fragment.contains(value.toString());
    }

    static String convertShortDateToCore(String date) {
        if (date == null || date.isEmpty()) {
            return null;
        }
        LocalDate ldt = LocalDate.parse(date, DOC_DATE_FORMATTER);
        return CORE_DATE_FORMATTER.format(ldt);
    }

    static String convertLongDateToCore(String date) {
        if (date == null || date.isEmpty()) {
            return null;
        }
        LocalDate ld = LocalDate.parse(date, DOC_DATE_FORMATTER);
        LocalTime lt = LocalTime.of(3, 0);
        ZonedDateTime ldt = LocalDateTime.of(ld, lt).atZone(ZoneId.systemDefault());
        return ldt.format(DateTimeFormatter.ISO_INSTANT);
    }

    static String convertLocalDateToLocalDateTimeMin(LocalDate date) {
        if (date == null) {
            return null;
        }
        Date date1 = Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        return SDF.format(date1);
    }

    static String convertLocalDateToLocalDateTimeMax(LocalDate date) {
        if (date == null) {
            return null;
        }
        Date date1 = Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date maxDate = sliceToEndDate(date1);
        return SDF.format(maxDate);
    }

    static Date sliceToEndDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    static String getFirstDecemberLastYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1);
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 3);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        ZonedDateTime ldt = calendarToZonedDateTime(calendar);
        return ldt.format(DateTimeFormatter.ISO_INSTANT);
    }

    static String getFirstJanuaryCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 3);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        ZonedDateTime ldt = calendarToZonedDateTime(calendar);
        return ldt.format(DateTimeFormatter.ISO_INSTANT);
    }

    static String convertLongDateToCore(Calendar calendar) {
        if (calendar == null) return null;
        ZonedDateTime ldt = calendarToZonedDateTime(calendar);
        return ldt.format(DateTimeFormatter.ISO_INSTANT);
    }

    static String convertShortDateToCore(Calendar calendar) {
        if (calendar == null) return null;
        LocalDate ld = calendarToLocalDate(calendar);
        return CORE_DATE_FORMATTER.format(ld);
    }

    static String convertCoreDateToShort(String coreDate) {
        if (coreDate == null || coreDate.isEmpty()) {
            return null;
        }
        LocalDateTime ld = LocalDateTime.parse(coreDate, LONG_CORE_DATE_FORMATTER_WITHOUT_TZ);
        return CORE_DATE_FORMATTER.format(ld);
    }

    static LocalDateTime convertLongDateToCore(Long millis) {
        if (millis == null) return null;
        Instant i = Instant.ofEpochSecond(millis / 1000);
        return LocalDateTime.ofInstant(i, ZoneId.systemDefault());
    }

    static <T> List<T> page(List<T> list, int pageNumber, int pageSize) {
        return list.subList((pageNumber - 1) * pageSize, min((pageNumber - 1) * pageSize + pageSize, list.size()));
    }

    static int pageTotal(List list, int pageNumber, int pageSize) {
        return list.size() == pageSize ? ((pageNumber * pageSize) + 1) :
                ((pageNumber - 1) * pageSize + (list.size() == 0 ? 1 : list.size()));
    }

    static Long toPennies(BigDecimal value) {
        return value != null ? value.movePointRight(2).longValue() : null;
    }

    static String createEndpointUrl(String endpointUrl, String baseUrl, String coreVersion) {
        return endpointUrl.replace("{url}", baseUrl).replace("{core.version}", coreVersion);
    }

    static URI createURI(String endpoint, String parameter) {
        if (StringUtils.isEmpty(parameter)) {
            return null;
        }

        try {
//            if (parameter.contains("/")) {
            parameter = URLEncoder.encode(parameter, "UTF-8").replace("+", "%20");
//            }
            return URI.create(endpoint + parameter);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    static boolean fromNumeralString(final String input) {
        if (input == null || input.trim().isEmpty()) return false;

        return input.equals("1");
    }

    static int toInteger(final Boolean bool) {
        if (bool == null) {
            return 0;
        }
        return bool ? 1 : 0;
    }

    static <T> RestItemList<T> setTotalAndLazyMode(RestItemList<T> restItemList, int pageNumber, int pageSize) {
        long total;
        if (restItemList.getTotal() == -1) {
            int listSize = restItemList.getList().size();
            if (listSize == pageSize) {
                total = (pageNumber * pageSize) + 1;
            } else {
                total = (pageNumber - 1) * pageSize + (listSize == 0 ? 1 : listSize);
            }
            restItemList.setTotal(total);
            restItemList.setLazyMode(true);
        }
        return restItemList;
    }
}
