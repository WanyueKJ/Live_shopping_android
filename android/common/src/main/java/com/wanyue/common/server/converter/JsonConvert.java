/*
 * Copyright 2016 jeasonlzy(廖子尧)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wanyue.common.server.converter;

import com.alibaba.fastjson.JSON;
import com.google.gson.stream.JsonReader;
import com.lzy.okgo.convert.Converter;
import com.wanyue.common.business.JumpInterceptor;
import com.wanyue.common.server.entity.BaseArrayResponse;
import com.wanyue.common.server.entity.BaseOriginalResponse;
import com.wanyue.common.server.entity.BaseSimpleReponse;
import com.wanyue.common.utils.L;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 版    本：1.0
 * 创建日期：16/9/11
 * 描    述：
 * 修订历史：
 * ================================================
 */

public class JsonConvert<T> implements Converter<T> {

    private Type type;
    private Class<T> clazz;
    private boolean isArray;

    public JsonConvert(boolean isArray) {
        this.isArray=isArray;
    }
    public JsonConvert(Type type,boolean isArray) {
        this.type = type;
        this.isArray=isArray;
    }
    public JsonConvert(Class<T> clazz) {
        this.clazz = clazz;
    }
    /**
     * 该方法是子线程处理，不能做ui相关的工作
     * 主要作用是解析网络返回的 response 对象，生成onSuccess回调中需要的数据对象
     * 这里的解析工作不同的业务逻辑基本都不一样,所以需要自己实现,以下给出的时模板代码,实际使用根据需要修改
     */
    @Override
    public T convertResponse(Response response) throws Throwable {

        // 重要的事情说三遍，不同的业务，这里的代码逻辑都不一样，如果你不修改，那么基本不可用
        // 重要的事情说三遍，不同的业务，这里的代码逻辑都不一样，如果你不修改，那么基本不可用
        // 重要的事情说三遍，不同的业务，这里的代码逻辑都不一样，如果你不修改，那么基本不可用

        // 如果你对这里的代码原理不清楚，可以看这里的详细原理说明: https://github.com/jeasonlzy/okhttp-OkGo/wiki/JsonCallback
        // 如果你对这里的代码原理不清楚，可以看这里的详细原理说明: https://github.com/jeasonlzy/okhttp-OkGo/wiki/JsonCallback
        // 如果你对这里的代码原理不清楚，可以看这里的详细原理说明: https://github.com/jeasonlzy/okhttp-OkGo/wiki/JsonCallback

        if (type == null) {
            if (clazz == null) {
                // 如果没有通过构造函数传进来，就自动解析父类泛型的真实类型（有局限性，继承后就无法解析到）
                Type genType = getClass().getGenericSuperclass();
                type = ((ParameterizedType) genType).getActualTypeArguments()[0];
            } else {
                return parseClass(response, clazz);
            }
        }

        if (type instanceof ParameterizedType) {
            return parseParameterizedType(response, (ParameterizedType) type);
        } else if (type instanceof Class) {
            return parseClass(response, (Class<?>) type);
        } else {
            return parseType(response,type);
        }
    }

    private T parseClass(Response response, Class<?> rawType) throws Exception {
        if (rawType == null) {
            return null;
        }
        ResponseBody body = response.body();
        if (body == null) {
            return null;
        }
        JsonReader jsonReader = new JsonReader(body.charStream());
        if (rawType == String.class) {
            //noinspection unchecked
            return (T) body.string();
        } else if (rawType == JSONObject.class) {
            //noinspection unchecked
            return (T) new JSONObject(body.string());
        } else if (rawType == JSONArray.class) {
            //noinspection unchecked
            return (T) new JSONArray(body.string());
        } else if (rawType == Void.class){
            return null;
        } else if(rawType==BaseOriginalResponse.class){
            T t = (T) JSON.parseObject(body.string(),rawType);
            return t;
        }
        else {
            T t = Convert.fromJson(jsonReader, rawType);
            response.close();
            return t;
        }
    }

    private T parseType(Response response, Type type) throws Exception {
        if (type == null){ return null;
        }
        ResponseBody body = response.body();

        if (body == null) {return null;}
        JsonReader jsonReader = new JsonReader(body.charStream());
        // 泛型格式如下： new JsonCallback<任意JavaBean>(this)
        T t = Convert.fromJson(jsonReader, type);
        response.close();
        return t;
    }

    private T parseParameterizedType(Response response, ParameterizedType type) throws Exception {
        if (type == null)
        {return null;
        }
        ResponseBody body = response.body();
        if (body == null) {
            return null;
        }
        JsonReader jsonReader = new JsonReader(body.charStream());
        Type rawType = type.getRawType();                     // 泛型的实际类型
        Type typeArgument = type.getActualTypeArguments()[0]; // 泛型的参数
        if (rawType == null) {
            // 泛型格式如下： new JsonCallback<外层BaseBean<内层JavaBean>>(this)
            T t = Convert.fromJson(jsonReader, type);
            response.close();
            return t;
        } else {
            if (typeArgument == Void.class) {
                // 泛型格式如下： new JsonCallback<LzyResponse<Void>>(this)
                BaseSimpleReponse simpleResponse = Convert.fromJson(jsonReader, BaseSimpleReponse.class);
                L.e("simpleResponse=="+simpleResponse.getMsg());
                return (T) simpleResponse;
            }else if(rawType== BaseOriginalResponse.class){
                BaseOriginalResponse baseOriginalResponse=Convert.fromJson(jsonReader,BaseOriginalResponse.class);
                L.e("simpleResponse=="+baseOriginalResponse.getMsg());
                return (T) baseOriginalResponse;
            }
            else {
                // 泛型格式如下： new JsonCallback<LzyResponse<内层JavaBean>>(this)
                BaseSimpleReponse baseSimpleReponse=null;
                if(isArray){
                    BaseArrayResponse baseResponse = Convert.fromJson(jsonReader, type);
                    baseSimpleReponse=baseResponse;
                }else{
                    BaseSimpleReponse baseResponse = Convert.fromJson(jsonReader, type);
                    baseSimpleReponse=baseResponse;
                }
                L.e("simpleResponse=="+baseSimpleReponse.getMsg());
                response.close();
                int code = baseSimpleReponse.getStatus();
                //这里的0是以下意思
                //一般来说服务器会和客户端约定一个数表示成功，其余的表示失败，这里根据实际情况修改
                if (200 == code) {
                    return (T) baseSimpleReponse;
                }
                else {
                    JumpInterceptor.shouldInterceptor(code);
                    //直接将服务端的错误信息抛出，onError中可以获取
                    throw new IllegalStateException("错误代码：" + code + "，错误信息：" + baseSimpleReponse.getMsg());
                }
            }
        }
    }
}
