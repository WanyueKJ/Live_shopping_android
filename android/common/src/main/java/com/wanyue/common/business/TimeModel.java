package com.wanyue.common.business;

import com.wanyue.common.utils.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class TimeModel {
    public static final int COUNT_DOWN=1;
    public static final int DEFAULT=0;
    private int mState=DEFAULT;

    public static final int MAXIMUM=1000000000;
    private static  TimeModel timeModel=null;
    private Disposable disposable;
    private Parser parser;
    private List<TimeListner> timeListnerList;
    private String time;

    private long totalUseTime;
    public TimeModel(){
        parser=new Parser();
        timeListnerList=new ArrayList<>(1);
    }

    public TimeModel setParserMode(int model){
        if(parser!=null){
            parser.setModel(model);
        }
        return this;
    }

    public TimeModel setBeforeString(String beforeString) {
        if(parser!=null){
            parser.setBeforeString(beforeString);
        }
        return this;
    }

    public TimeModel setAfterString(String beforeString) {
        if(parser!=null){
            parser.setAfterString(beforeString);
        }
        return this;
    }


    public TimeModel setState(int state) {
        this.mState = state;
        return this;
    }

    public TimeModel setTotalUseTime(long totalUseTime) {
        this.totalUseTime = totalUseTime;
        return this;
    }


    //目前进入房间回调成功的时候开始进行计时
    public void start(){
        dispose();
        disposable= Observable.interval(0, 1, TimeUnit.SECONDS).take(totalUseTime).
                subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        long timeNum=0;
                        if(mState==DEFAULT){
                            timeNum=aLong;
                        }else{
                            timeNum=totalUseTime-aLong;
                        }
                        time= parser.parse(timeNum);
                        if(timeListnerList!=null){
                            for(TimeListner timeListner:timeListnerList){
                                timeListner.time(time);
                            }
                        }
                        if(totalUseTime==aLong+1){
                            if(timeListnerList!=null){
                                for(TimeListner timeListner:timeListnerList){
                                    timeListner.compelete();
                                }
                            }
                        }


                    }
                });
    }

    public void addTimeListner(TimeListner timeListner){
        if(timeListnerList==null){
           timeListnerList=new ArrayList<>();
        }
        if(timeListnerList.contains(timeListner)){
            return;
        }
        timeListnerList.add(timeListner);
    }

    public void removeTimeListner(TimeListner timeListner){
        if(timeListner==null||timeListnerList==null){
            return;
        }
        timeListnerList.remove(timeListner);
    }


    public void clear(){
        dispose();
        time=null;
        if(timeListnerList!=null){
           timeListnerList.clear();
           timeListnerList=null;
        }
    }




    public static Observable<Long>delay(long time,TimeUnit timeUnit){
       return Observable.timer(time,timeUnit).observeOn(AndroidSchedulers.mainThread());
    }

    public String getTime() {
        return time;
    }
    public  interface TimeListner{
        public void time(String string);
        public void compelete();
    }

    /*是活跃的状态*/
    public boolean isActivitve(){
        return disposable!=null&&!disposable.isDisposed();
    }

    public void dispose() {
         if(isActivitve()){
            disposable.dispose();
        }
    }

}
