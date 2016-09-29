package br.com.tads.estarcliente.model;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Dev_Maker on 28/09/2016.
 */
public class Timer {

    String dateActived;
    String dateFinish;
    long secondsTofinish;
    int timeEstar;



    public Timer(String dateActived) {
        this.dateActived = dateActived;
        getDateDifference(dateActived);
        //setMillisAlert(dateActived);
    }

    public Timer(int timeEstar,String dateActived) {
        this.timeEstar = timeEstar;
        this.dateActived = dateActived;
        getDateDifference(dateActived);
    }

    public int getTimeEstar() {
        return timeEstar;
    }

    public void setTimeEstar(int timeEstar) {
        this.timeEstar = timeEstar;
    }

    public String getDateActived() {
        return dateActived;
    }

    public void setDateActived(String dateActived) {
        this.dateActived = dateActived;
    }

    public String getDateFinish() {
        return dateFinish;
    }

    public void setDateFinish(String dateFinish) {
        this.dateFinish = dateFinish;
    }

    public void setDateFinish2(String dateActived) {


        this.dateFinish = dateFinish;
    }

//    diferença entre o tempo que foi ativado até o tempo de finalizar, em seconds

    public long getDateDifference(String oldDate) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Calendar calendar = Calendar.getInstance();

        switch (this.timeEstar){
            case 1:
                calendar.add(Calendar.MINUTE,2);
                break;
            case 2:
                calendar.add(Calendar.MINUTE,3);
                break;
        }

//        calendar.add(Calendar.MINUTE,2);

        String finishDate = dateFormat.format(calendar.getTime());

        try {
            Date dateActive = dateFormat.parse(oldDate);

            Date finish = dateFormat.parse(finishDate);

            long diff = finish.getTime() - dateActive.getTime();

            long days = diff / (24 * 60 * 60 * 1000);
            diff -= days * (24 * 60 * 60 * 1000);

            long hours = diff / (60 * 60 * 1000);
            diff -= hours * (60 * 60 * 1000);

            long minutes = diff / (60 * 1000);
            diff -= minutes * (60 * 1000);

            long seconds = diff / 1000;

            seconds = seconds + 60*minutes;

            setDateFinish(finishDate);
            setSecondsTofinish(seconds);
            return  seconds;

//            counterDaysTV.setText(days + "");
//            counterMinsTV.setText(minutes + "");
//            counterHoursTV.setText(hours + "");
//            counterSecTV.setText(seconds + "");

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public long getSecondsTofinish() {
        return secondsTofinish;
    }

    public void setSecondsTofinish(long secondsTofinish) {
        this.secondsTofinish = secondsTofinish;
    }

    public void increase() {
        String myTime = this.dateFinish;
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
     try {
         Date d = df.parse(myTime);
         Calendar cal = Calendar.getInstance();
         cal.setTime(d);
         cal.add(Calendar.MINUTE, 1);
         String newTime = df.format(cal.getTime());
         this.dateFinish = newTime;
     }catch (Exception e){
         Log.e("parsedate",e.getMessage());
     }
    }

}
