package ru.rsdev.HorizontalGallery;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

public class DateUtil {

    //Обрезка времени из строки с датой
    public String getDateWithoutTime(String str){
        StringTokenizer st = new StringTokenizer(str, " ");
        String dateString = st.nextToken();

        int dateMass[] = getIntDate(dateString);

        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        GregorianCalendar calendar2 = new GregorianCalendar(dateMass[0],
                dateMass[1]-1, dateMass[2]);
        String month_name = month_date.format(calendar2.getTime());
        dateString = String.valueOf(dateMass[2]) + " " + month_name + " " + String.valueOf(dateMass[0]);

        return dateString;
    }


    private int[] getIntDate(String date){
        StringTokenizer st = new StringTokenizer(date, ":");
        int year = Integer.parseInt(st.nextToken());
        int month = Integer.parseInt(st.nextToken());
        int day = Integer.parseInt(st.nextToken());

        int dateMass[] = new int[]{year,month,day};
        return dateMass;
    }

/*
SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month_name = month_date.format(cal.getTime());
 */

}
