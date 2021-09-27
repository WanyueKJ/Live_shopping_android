package com.wanyue.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ListUtil {

  public static boolean haveData(Collection list){
      return list!=null&&list.size()>0;
  }

    public static <T> T safeGetData(List<T>list,int position){
       if(!haveData(list)||list.size()<=position||position==-1) {
           return null;
       }
        return list.get(position);
    }

    /*需要重写相关类的toString()方法*/
    public static String listToSingleString(List list){
       if (haveData(list)){
        StringBuilder builder=new StringBuilder();
        for(Object object:list){
            builder.append(object.toString())
            .append(",");
        }
           builder.deleteCharAt(builder.length()-1);
         return builder.toString();
       }
        return null;
    }

    public static <T,E>  ArrayList<E>transForm(List<T>list,Class<E>cs,TransFormListner<T,E>teTransFormListner){
        ArrayList<E>newList=new ArrayList<>(1);
        if(haveData(list)&&teTransFormListner!=null){
            for(T t:list){
                newList.add(teTransFormListner.transform(t));
            }
            return newList;
        }
        return newList;
    }

    public static <T,E>  ArrayList<E>transForm(T[]array,Class<E>cs,TransFormListner<T,E>teTransFormListner){
        if(array!=null&&teTransFormListner!=null){
            ArrayList<E>newList=new ArrayList<>();
            for(T t:array){
                newList.add(teTransFormListner.transform(t));
            }
            return newList;
        }
        return null;
    }


    public static <T,E>  ArrayList<E>transFormByIndex(List<T>list,Class<E>cs,TransFormByIndexListner<T,E>teTransFormListner){
        ArrayList<E>newList=new ArrayList<>(1);
        if(haveData(list)&&teTransFormListner!=null){
            int size=list.size();
            for(int i=0;i<size;i++){
                T t=list.get(i);
                newList.add(teTransFormListner.transform(i,size,t));
            }
            return newList;
        }
        return newList;
    }

    public static <T> List<T>asList(T...array){
        if(array==null){
           return null;
        }

       List<T>list=new ArrayList<>(array.length);

       for(T t:array){
          list.add(t);
       }
      return list;
    }

    public static int size(List<?> optionList) {
        return optionList==null?0:optionList.size();
    }

    public static <T>T getLastData(List<T>list) {
        if(!haveData(list)){
           return null;
        }
        return list.get(list.size()-1);
    }


    public static int index(List<?>list,Object object) {
        if(list==null||object==null){
            DebugUtil.sendException("NullPoint");
            return -1;
        }
        return list.indexOf(object);
    }




    public static int getSize(Collection collection) {
        return collection==null?0:collection.size();
    }

    public static int getSize(Object []collection) {
        return collection==null?0:collection.length;
    }

    public static <T>T getArrayData(T[]array,int position) {
        if(array==null||position>=array.length||position<0){
            DebugUtil.sendException("array=="+array+"&&position=="+position);
            return null;
        }
        return array[position];
    }

    public interface TransFormListner<T,E>{
        public E transform(T t);
    }

    public interface TransFormByIndexListner<T,E>{
        public E transform(int index,int totalSize,T t);
    }


}
