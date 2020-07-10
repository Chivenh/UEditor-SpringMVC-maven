//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.baidu.ueditor.jacksonextend;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext.Impl;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class JsonMapper extends ObjectMapper {
    private static final long serialVersionUID = 1L;

    public JsonMapper() {
        this((JsonFactory)null, (DefaultSerializerProvider)null, (DefaultDeserializationContext)null);
    }

    public JsonMapper(JsonFactory jf) {
        this(jf, (DefaultSerializerProvider)null, (DefaultDeserializationContext)null);
    }

    public JsonMapper(JsonFactory jf, DefaultSerializerProvider sp, DefaultDeserializationContext dc) {
        super(jf, sp, (dc == null ? new Impl(DefaultDeserializerFactory.instance) : dc));
        this.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        this.configure(SerializationFeature.INDENT_OUTPUT, false);
        this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, false);
        this.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        this.setDateFormat(new MultipleDateFormat(MultipleDateFormat.DEFAULT_DATETIME_FORMAT));
        this.setSerializationInclusion(Include.NON_NULL);
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.configure(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS, false);
        SimpleModule module = new SimpleModule();
        Iterator var5 = NumberSerializer.serializers.entrySet().iterator();

        while(var5.hasNext()) {
            Entry<Class<? extends Number>, NumberSerializer> entry = (Entry)var5.next();
            module.addSerializer(entry.getKey(), entry.getValue());
        }

        this.registerModule(module);
        this.init();
    }

    protected void init() {
    }

    public String serialize(Object obj) {
        if (obj == null) {
            return null;
        } else {
            try {
                return this.writeValueAsString(obj);
            } catch (JsonProcessingException var3) {
                throw new RuntimeException(var3);
            }
        }
    }

    public <T> T deserialize(String json, Class<T> type) {
        try {
            return this.readValue(json, type);
        } catch (IOException var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public Object deserialize(String json) {
        if (json == null) {
            return null;
        } else {
            if (json.trim().startsWith("[")) {
                try {
                    return this.readValue(json, List.class);
                } catch (IOException var4) {
                }
            }

            try {
                return this.readValue(json, Map.class);
            } catch (IOException var3) {
                return null;
            }
        }
    }
}
