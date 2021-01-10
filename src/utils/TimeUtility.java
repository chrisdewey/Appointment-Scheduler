package utils;

import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import javafx.util.converter.LocalTimeStringConverter;
import models.Appointment;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Contains methods for converting and formatting time and timestamps.
 * @author Christian Dewey
 */
public class TimeUtility {

    /**
     * @return The Business Hours 08:00 to 22:00 converted from EST to users Local Time
     */
    public static String businessHoursString() {
        String displayedTime = " Local Time";

        ZoneOffset businessZO = ZoneId.of("America/New_York").getRules().getOffset(Instant.now());

        OffsetTime startTime = OffsetTime.of(8, 0,0,0, businessZO);
        OffsetTime endTime = OffsetTime.of(22, 0,0,0, businessZO);

        ZoneOffset localZO = ZoneId.systemDefault().getRules().getOffset(Instant.now());

        OffsetTime startTimeLocal = startTime.withOffsetSameInstant(localZO);
        OffsetTime endTimeLocal = endTime.withOffsetSameInstant(localZO);

        displayedTime = " " + startTimeLocal.toLocalTime().toString() + " to " + endTimeLocal.toLocalTime().toString() + displayedTime;

        return displayedTime;
    }

    /**
     * @return The time business hours end in LocalTime if it passes midnight. If not returns null.
     */
    public static LocalTime businessHoursOverMidnight() {

        ZoneOffset businessZO = ZoneId.of("America/New_York").getRules().getOffset(Instant.now());

        OffsetTime startTime = OffsetTime.of(8, 0,0,0, businessZO);
        OffsetTime endTime = OffsetTime.of(22, 0,0,0, businessZO);

        ZoneOffset localZO = ZoneId.systemDefault().getRules().getOffset(Instant.now());

        OffsetTime startTimeLocal = startTime.withOffsetSameInstant(localZO);
        OffsetTime endTimeLocal = endTime.withOffsetSameInstant(localZO);

        LocalTime endOfBusinessHours;

        if (endTimeLocal.toLocalTime().isBefore(startTimeLocal.toLocalTime())) {
            endOfBusinessHours = endTimeLocal.toLocalTime();
        } else  { endOfBusinessHours = null; }

        return endOfBusinessHours;
    }

    /**
     * Checks if the given times fall within business hours
     * @param start start time of the proposed appointment
     * @param end end time of the proposed appointment
     * @return true if start and end time fall within business hours.
     */
    public static boolean businessHoursCheck(ZonedDateTime start, ZonedDateTime end) {
        boolean notConflicting;

        ZoneOffset businessZO = ZoneId.of("America/New_York").getRules().getOffset(Instant.now());

        OffsetTime startTime = OffsetTime.of(8, 0,0,0, businessZO);
        OffsetTime endTime = OffsetTime.of(22, 0,0,0, businessZO);

        ZoneOffset localZO = ZoneId.systemDefault().getRules().getOffset(Instant.now());

        OffsetTime appointmentStart = OffsetTime.of(start.toLocalTime(), localZO);
        OffsetTime appointmentEnd = OffsetTime.of(end.toLocalTime(), localZO);

        notConflicting = appointmentStart.isAfter(startTime) && appointmentEnd.isBefore(endTime);

        return notConflicting;
    }

    /**
     * @param input the SQL Timestamp, which is in UTC
     * @return the Timestamp converted to a ZonedDateTime in Local Time using the users ZoneId
     */
    static ZonedDateTime DisplayInLocalTime(Timestamp input) {

        Instant instant = input.toInstant();
        ZoneId z = ZoneId.systemDefault();

        return
                instant.atZone(z);
    }

    /**
     * @param input the ZonedDateTime with users ZoneId
     * @return the UTC Timestamp converted from a ZonedDateTime Local Time using the users ZoneId
     */
    public static java.sql.Timestamp ConvertToUTCTimestamp(ZonedDateTime input) {

        LocalDateTime ldt = input.toLocalDateTime();

        return
                Timestamp.valueOf(ldt);
    }


    /**
     * This custom LocalTimeStringConverter is used instead of the javafx.util.converter.LocalTimeStringConverter
     *  because it allows for exception control when the user edits the editable Spinner by manually typing in the time,
     *  rather than using the Increment and Decrement buttons. If an incorrect time is entered, the time resets to
     *  LocalTime.now(). This ,however, can be an inconvenience when Updating an existing appointment.
     * @return the input string converted to a LocalTime object
     */
    private static final LocalTimeStringConverter CustomLocalTimeStringConverter = new LocalTimeStringConverter() {
       final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        @Override
        public String toString(LocalTime object) {
            if (object == null) {return "";}
            return formatter.format(object);}
        @Override
        public LocalTime fromString(String newInput) {
            try {
                newInput = newInput.trim();
                return LocalTime.parse(newInput, formatter);
            } catch (DateTimeException ex) {
                System.out.println("incompatible time.");
                return LocalTime.now();
            }

        }
    };

    /**
     * A SpinnerValueFactory for LocalTime selection.
     * @param setTime the time the TimePicker will show as default. Pass in LocalTime.now() when creating a
     *                new appointment, or the appointments Start/End time when updating an existing one.
     */
    public static SpinnerValueFactory<LocalTime> TimePicker(LocalTime setTime) {
        return new SpinnerValueFactory<>() {
            {
                setConverter(CustomLocalTimeStringConverter);

                setValue(setTime);
            }

            @Override
            public void decrement(int i) {
                if (getValue() == null) {
                    setValue(LocalTime.now());
                } else {
                    LocalTime time = getValue();
                    setValue(time.minusMinutes(i));
                }
            }

            @Override
            public void increment(int i) {
                if (this.getValue() == null) {
                    setValue(LocalTime.now());
                } else {
                    LocalTime time = getValue();
                    setValue(time.plusMinutes(i));
                }
            }
        };
    }

    /**
     * Callback method used to format how the ZonedDateTimes will be displayed in the TableView.
     */
    private static final Callback<TableColumn<Appointment, ZonedDateTime>, TableCell<Appointment, ZonedDateTime>> dateFactory
            = column -> new TableCell<>() {
        @Override
        protected void updateItem(ZonedDateTime item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText(null);
            } else {
                setText(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(item)
                        + " at " + DateTimeFormatter.ofPattern("kk:mm").format(item));
            }
        }
    };

    /**
     * @return the dateFactory callback method.
     */
    public static Callback<TableColumn<Appointment, ZonedDateTime>, TableCell<Appointment, ZonedDateTime>> getDateFactory() {
        return dateFactory;
    }
}