package com.nannan.utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DefaultObjectMapper implements ObjectMapper {
    String dateFormat = "yyyy-MM-dd";
    DateTimeFormatter datetimeFormatter = DateTimeFormatter.ofPattern(dateFormat);

    String decimalFormat = "#,##0.00";
    DecimalFormat decimalFormatter = new DecimalFormat(decimalFormat);

    @Override
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
        this.datetimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
    }

    @Override
    public void setDecimalFormat(String decimalFormat) {
        this.decimalFormat = decimalFormat;
        this.decimalFormatter = new DecimalFormat(decimalFormat);
    }

    @Override
    public String writeValuesAsString(Object obj) {
        StringBuilder sJsonStr = new StringBuilder("{");
        Class<?> clazz = obj.getClass();

        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            String sField = "";
            Object value = null;
            Class<?> type = null;
            String name = field.getName();
            String strValue = "";
            try {
                field.setAccessible(true);
                value = field.get(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            type = field.getType();

            if(value instanceof Date){
                LocalDate localDate = ((Date)value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                strValue = localDate.format(this.datetimeFormatter);
            }else if(value instanceof BigDecimal || value instanceof Double || value instanceof Float){
                strValue = this.decimalFormatter.format(value);
            }else {
                strValue = value.toString();
            }

            if(sJsonStr.toString().equals("{")){
                sField = "\"" + name + "\":\"" + strValue + "\"";
            }else {
                sField = ",\"" + name + "\":\"" + strValue + "\"";
            }
            sJsonStr.append(sField);
        }
        sJsonStr.append("}");
        return sJsonStr.toString();
    }
}
