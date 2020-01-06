package io.github.skywalkerdarren.simpleaccounting.util.data;

import androidx.room.TypeConverter;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class TypeConvertUtil {
    @TypeConverter
    public String bigDecimalToString(BigDecimal bigDecimal) {
        return bigDecimal == null ? null : bigDecimal.toString();
    }

    @TypeConverter
    public BigDecimal stringToBigDecimal(String string) {
        return string == null ? BigDecimal.ZERO : new BigDecimal(string);
    }

    @TypeConverter
    public String uuidToString(UUID uuid) {
        return uuid == null ? null : uuid.toString();
    }

    @TypeConverter
    public UUID stringToUUID(String string) {
        return string == null ? null : UUID.fromString(string);
    }

    @TypeConverter
    public Long dateToLong(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public Date longToDate(Long l) {
        return l == null ? null : new Date(l);
    }

    @TypeConverter
    public Long dateTimeToLong(DateTime date) {
        return date == null ? null : date.getMillis();
    }

    @TypeConverter
    public DateTime longToDateTime(Long l) {
        return l == null ? null : new DateTime(l);
    }
}
