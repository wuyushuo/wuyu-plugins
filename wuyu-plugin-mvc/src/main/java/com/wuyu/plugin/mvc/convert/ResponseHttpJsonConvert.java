/*--------------------------------------------------------------------------
 *  Copyright (c) 2009-2020, www.wuyushuo.com All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the wuyushuo developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: git_wuyu@163.com (tencent qq: 2094998377)
 *--------------------------------------------------------------------------
*/
package com.wuyu.plugin.mvc.convert;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class ResponseHttpJsonConvert {

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    private static final ObjectMapper mapper;

    public ObjectMapper getMapper() {
        return mapper;
    }

    static {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);

        mapper = new ObjectMapper();
        mapper.setDateFormat(dateFormat);
        mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
            private static final long serialVersionUID = -5854941510519564900L;

            @Override
            public Object findSerializer(Annotated a) {
                if (a instanceof AnnotatedMethod) {
                    AnnotatedElement m = a.getAnnotated();
                    DateTimeFormat an = m.getAnnotation(DateTimeFormat.class);
                    if (an != null) {
                        if (!DEFAULT_DATE_FORMAT.equals(an.pattern())) {
                            return new JsonDateSerializer(an.pattern());
                        }
                    }
                }
                return super.findSerializer(a);
            }
        });
    }

    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("convert object to json error");
        }
    }

    public <T> T toObject(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException("parser json to object error");
        }
    }

    public static class JsonDateSerializer extends JsonSerializer<Date> {
        private SimpleDateFormat dateFormat;

        public JsonDateSerializer(String format) {
            dateFormat = new SimpleDateFormat(format);
        }

        @Override
        public void serialize(Date date, JsonGenerator gen, SerializerProvider provider) throws IOException{
            String value = dateFormat.format(date);
            gen.writeString(value);
        }
    }

}
