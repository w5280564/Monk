package com.xunda.lib.common.common.preferences;

import com.baoyz.treasure.Converter;
import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * SharePreferences保存对象的转换类
 * @author ouyang
 *
 */
public class GsonConverterFactory implements Converter.Factory {

    @Override
    public <F> Converter<F, String> fromType(Type type) {
        return new Converter<F, String>() {
            @Override
            public String convert(F value) {
                return new Gson().toJson(value);
            }
        };
    }

    @Override
    public <T> Converter<String, T> toType(Type type) {
        return new Converter<String, T>() {
            @Override
            public T convert(String value) {
                return new Gson().fromJson(value, type);
            }
        };
    }
}
