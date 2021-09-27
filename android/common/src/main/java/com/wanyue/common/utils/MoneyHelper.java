package com.wanyue.common.utils;
import com.wanyue.common.CommonAppConfig;

public class MoneyHelper {
    public static final String RMB_UNIT="¥";
    public static final String PLATFORM_UNIT= CommonAppConfig.getCoinName();
    public static final int TYPE_RMB=1; //人名币类型
    public static final int TYPE_PLATFORM=2;//平台币类型
    /*人民币和虚拟币的换算关系*/
    private static final int RMB_MATRIXIN=1;
    private static StringBuilder stringBuilder;

    public static int type;
    static {
        stringBuilder=new StringBuilder();
    }
    public static String moneySymbol(int coin,int moneyType){
        stringBuilder.setLength(0);
        if(moneyType==TYPE_RMB){
         stringBuilder.append(RMB_UNIT);
         stringBuilder.append(getMoney(coin,moneyType));
       }else{
            stringBuilder.append(coin)
            .append(PLATFORM_UNIT);
       }
        return stringBuilder.toString();
    }

    public static int getMoney(int coin,int type){
        if(type==TYPE_RMB){
            return RMB_MATRIXIN*coin;
        }
        return coin;
    }

}
