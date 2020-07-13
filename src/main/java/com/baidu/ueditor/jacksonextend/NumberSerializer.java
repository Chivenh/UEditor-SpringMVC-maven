//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.baidu.ueditor.jacksonextend;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class NumberSerializer<N extends Number> extends JsonSerializer<N> implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final Long MAX_JAVASCRIPT_LONG_VALUE = 4503599627370496L;
    public static final Long MIN_JAVASCRIPT_LONG_VALUE;
    public static final NumberSerializer.LongSerializer longSerializer;
    public static final NumberSerializer.BigIntegerSerializer bigIntegerSerializer;
    public static final NumberSerializer.DoubleSerializer doubleSerializer;
    public static final NumberSerializer.BigDecimalSerializer bigDecimalSerializer;
    public static final Map<Class<? extends Number>, NumberSerializer> serializers;

    public NumberSerializer() {
    }

    @Override
    public void serialize(N value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        if (this.isValid(value)) {
            this.writeNumber(value, gen, serializers);
        } else {
            gen.writeString(value.toString());
        }

    }

    protected boolean isValid(N value) {
        Double d = value.doubleValue();
        return (double)MAX_JAVASCRIPT_LONG_VALUE >= d && (double)MIN_JAVASCRIPT_LONG_VALUE <= d;
    }

    protected void writeNumber(N value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeNumber(value.doubleValue());
    }

     static {
        MIN_JAVASCRIPT_LONG_VALUE = -MAX_JAVASCRIPT_LONG_VALUE;
        longSerializer = new NumberSerializer.LongSerializer();
        bigIntegerSerializer = new NumberSerializer.BigIntegerSerializer();
        doubleSerializer = new NumberSerializer.DoubleSerializer();
        bigDecimalSerializer = new NumberSerializer.BigDecimalSerializer();
        serializers = new HashMap<>();
        serializers.put(Long.class, longSerializer);
        serializers.put(BigInteger.class, bigIntegerSerializer);
        serializers.put(Double.class, doubleSerializer);
        serializers.put(BigDecimal.class, bigDecimalSerializer);
    }

    public static class BigDecimalSerializer extends NumberSerializer<BigDecimal> {
        private static final long serialVersionUID = 1L;
        public static final BigDecimal MAX_JAVASCRIPT_VALUE;
        public static final BigDecimal MIN_JAVASCRIPT_VALUE;

        public BigDecimalSerializer() {
        }

		@Override
        protected boolean isValid(BigDecimal value) {
            return MAX_JAVASCRIPT_VALUE.compareTo(value) >= 0 && MIN_JAVASCRIPT_VALUE.compareTo(value) <= 0;
        }

		@Override
        protected void writeNumber(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
            gen.writeNumber(value);
        }

        static {
            MAX_JAVASCRIPT_VALUE = BigDecimal.valueOf(MAX_JAVASCRIPT_LONG_VALUE);
            MIN_JAVASCRIPT_VALUE = BigDecimal.valueOf(MIN_JAVASCRIPT_LONG_VALUE);
        }
    }

    public static class DoubleSerializer extends NumberSerializer<Double> {
        private static final long serialVersionUID = 1L;

        public DoubleSerializer() {
        }

		@Override
        protected void writeNumber(Double value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
            gen.writeNumber(value);
        }
    }

    public static class BigIntegerSerializer extends NumberSerializer<BigInteger> {
        private static final long serialVersionUID = 1L;
        public static final BigInteger MAX_JAVASCRIPT_VALUE;
        public static final BigInteger MIN_JAVASCRIPT_VALUE;

        public BigIntegerSerializer() {
        }

		@Override
        protected boolean isValid(BigInteger value) {
            return MAX_JAVASCRIPT_VALUE.compareTo(value) >= 0 && MIN_JAVASCRIPT_VALUE.compareTo(value) <= 0;
        }

		@Override
        protected void writeNumber(BigInteger value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
            gen.writeNumber(value);
        }

        static {
            MAX_JAVASCRIPT_VALUE = BigInteger.valueOf(MAX_JAVASCRIPT_LONG_VALUE);
            MIN_JAVASCRIPT_VALUE = BigInteger.valueOf(MIN_JAVASCRIPT_LONG_VALUE);
        }
    }

    public static class LongSerializer extends NumberSerializer<Long> {
        private static final long serialVersionUID = 1L;

        public LongSerializer() {
        }

		@Override
        protected boolean isValid(Long value) {
            return MAX_JAVASCRIPT_LONG_VALUE.compareTo(value) >= 0 && MIN_JAVASCRIPT_LONG_VALUE.compareTo(value) <= 0;
        }

		@Override
        protected void writeNumber(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
            gen.writeNumber(value);
        }
    }
}
