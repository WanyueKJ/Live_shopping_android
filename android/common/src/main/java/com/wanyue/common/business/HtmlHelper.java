package com.wanyue.common.business;


public  class HtmlHelper {
        private StringBuilder mStringBuilder;
        public HtmlHelper() {
            mStringBuilder = new StringBuilder();
        }

        public static HtmlHelper newHtmlHelper(){
            return new HtmlHelper();
        }

        public  HtmlHelper appendUrl(String url){
            if(mStringBuilder==null){
               mStringBuilder = new StringBuilder();
            }
            mStringBuilder.append(url);
            return this;
        }

        public HtmlHelper appendParm(String key,String value,boolean isFirst){
            if(mStringBuilder==null){
               mStringBuilder = new StringBuilder();
            }
            if(isFirst){
                mStringBuilder.append(key).append("=").append(value);
            }else{
                mStringBuilder.append("&").append(key).append("=").append(value);
            }

            return this;
        }
        

        public String create(){
            if(mStringBuilder!=null){
                return  mStringBuilder.toString(); 
            }
             return null;
        }
    }