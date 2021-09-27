/*
package com.wanyue.common.server.converter;

import com.alibaba.fastjson.JSONReader;
import com.lzy.okgo.convert.Converter;
import org.json.JSONArray;
import org.json.JSONObject;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class FastJsonConvert <T> implements Converter<T> {
    private Type type;
    private Class<T> clazz;

    public FastJsonConvert() {
    }
    public FastJsonConvert(Type type) {
        this.type = type;
    }
    public FastJsonConvert(Class<T> clazz) {
        this.clazz = clazz;
    }
*
     * 该方法是子线程处理，不能做ui相关的工作
     * 主要作用是解析网络返回的 response 对象，生成onSuccess回调中需要的数据对象
     * 这里的解析工作不同的业务逻辑基本都不一样,所以需要自己实现,以下给出的时模板代码,实际使用根据需要修改


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
        JSONReader jsonReader= new JSONReader(body.charStream());


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
        }
        else {
            T t = (T) jsonReader.readObject(rawType);
            response.close();
            return t;
        }
    }

    private T parseType(Response response, Type type) throws Exception {
        if (type == null){ return null;
        }
        ResponseBody body = response.body();

        if (body == null) {return null;}
        JSONReader jsonReader= new JSONReader(body.charStream());
        // 泛型格式如下： new JsonCallback<任意JavaBean>(this)
        T t =jsonReader.readObject(type);
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

        JSONReader jsonReader= new JSONReader(body.charStream());
        Type rawType = type.getRawType();                     // 泛型的实际类型
        Type typeArgument = type.getActualTypeArguments()[0]; // 泛型的参数
        if (rawType != BaseResponse.class) {
            // 泛型格式如下： new JsonCallback<外层BaseBean<内层JavaBean>>(this)
            T t = jsonReader.readObject(type);
            response.close();
            return t;
        } else {
            if (typeArgument == Void.class) {
                // 泛型格式如下： new JsonCallback<LzyResponse<Void>>(this)
                SimpleResponse simpleResponse = jsonReader.readObject(SimpleResponse.class);

                return (T) simpleResponse.toBaseResponse();
            }
            else {
                // 泛型格式如下： new JsonCallback<LzyResponse<内层JavaBean>>(this)
                BaseResponse baseResponse =  jsonReader.readObject(type);
                response.close();
                int code = baseResponse.getRet();
                //这里的0是以下意思
                //一般来说服务器会和客户端约定一个数表示成功，其余的表示失败，这里根据实际情况修改
                if (200 == code) {
                    return (T) baseResponse;
                }
                else {
                    //直接将服务端的错误信息抛出，onError中可以获取
                    throw new IllegalStateException("错误代码：" + code + "，错误信息：" + baseResponse.getMsg());
                }
            }
        }
    }
}
*/
