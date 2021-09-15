package com.aishwarya.customerservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.modelmapper.*;

import java.io.IOException;
import java.time.Instant;


public class UtilManager {

    private UtilManager() {
    }

    public static <T> T convertObject(Object obj, Class<T> classOfT) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.createTypeMap(String.class, LocalDate.class);
        modelMapper.addConverter(toStringDate);
        modelMapper.getTypeMap(String.class, LocalDate.class).setProvider(localDateProvider);
        return modelMapper.map(obj, classOfT);
    }

    static Provider<LocalDate> localDateProvider = new AbstractProvider<LocalDate>() {
        @Override
        public LocalDate get() {
            return LocalDate.now();
        }
    };

    static Converter<String, LocalDate> toStringDate = new AbstractConverter<String, LocalDate>() {
        @Override
        protected LocalDate convert(String source) {
            DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");
            return LocalDate.parse(source, format);
        }
    };

    public static long getCurrentTimeStamp() {
        Instant currentTime = Instant.now();
        return currentTime.toEpochMilli();
    }

    public static int getAgeFromDOB(String date) {
        LocalDate dob = new LocalDate(date);
        LocalDate now = new LocalDate();
        Period period = new Period(dob, now);
        return period.getYears();
    }

    public static boolean isBlankString(String string) {
        return string == null || string.trim().isEmpty();
    }

    public static <T> T mapFromJson(String json, Class<T> clazz) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }

}
