package retrofit2.converter.scalars;

import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Converter;
/* loaded from: classes3.dex */
final class ScalarResponseBodyConverters {
    private ScalarResponseBodyConverters() {
    }

    /* loaded from: classes3.dex */
    static final class StringResponseBodyConverter implements Converter<ResponseBody, String> {
        static final StringResponseBodyConverter INSTANCE = new StringResponseBodyConverter();

        StringResponseBodyConverter() {
        }

        public String convert(ResponseBody value) throws IOException {
            return value.string();
        }
    }

    /* loaded from: classes3.dex */
    static final class BooleanResponseBodyConverter implements Converter<ResponseBody, Boolean> {
        static final BooleanResponseBodyConverter INSTANCE = new BooleanResponseBodyConverter();

        BooleanResponseBodyConverter() {
        }

        public Boolean convert(ResponseBody value) throws IOException {
            return Boolean.valueOf(value.string());
        }
    }

    /* loaded from: classes3.dex */
    static final class ByteResponseBodyConverter implements Converter<ResponseBody, Byte> {
        static final ByteResponseBodyConverter INSTANCE = new ByteResponseBodyConverter();

        ByteResponseBodyConverter() {
        }

        public Byte convert(ResponseBody value) throws IOException {
            return Byte.valueOf(value.string());
        }
    }

    /* loaded from: classes3.dex */
    static final class CharacterResponseBodyConverter implements Converter<ResponseBody, Character> {
        static final CharacterResponseBodyConverter INSTANCE = new CharacterResponseBodyConverter();

        CharacterResponseBodyConverter() {
        }

        public Character convert(ResponseBody value) throws IOException {
            String body = value.string();
            if (body.length() == 1) {
                return Character.valueOf(body.charAt(0));
            }
            throw new IOException("Expected body of length 1 for Character conversion but was " + body.length());
        }
    }

    /* loaded from: classes3.dex */
    static final class DoubleResponseBodyConverter implements Converter<ResponseBody, Double> {
        static final DoubleResponseBodyConverter INSTANCE = new DoubleResponseBodyConverter();

        DoubleResponseBodyConverter() {
        }

        public Double convert(ResponseBody value) throws IOException {
            return Double.valueOf(value.string());
        }
    }

    /* loaded from: classes3.dex */
    static final class FloatResponseBodyConverter implements Converter<ResponseBody, Float> {
        static final FloatResponseBodyConverter INSTANCE = new FloatResponseBodyConverter();

        FloatResponseBodyConverter() {
        }

        public Float convert(ResponseBody value) throws IOException {
            return Float.valueOf(value.string());
        }
    }

    /* loaded from: classes3.dex */
    static final class IntegerResponseBodyConverter implements Converter<ResponseBody, Integer> {
        static final IntegerResponseBodyConverter INSTANCE = new IntegerResponseBodyConverter();

        IntegerResponseBodyConverter() {
        }

        public Integer convert(ResponseBody value) throws IOException {
            return Integer.valueOf(value.string());
        }
    }

    /* loaded from: classes3.dex */
    static final class LongResponseBodyConverter implements Converter<ResponseBody, Long> {
        static final LongResponseBodyConverter INSTANCE = new LongResponseBodyConverter();

        LongResponseBodyConverter() {
        }

        public Long convert(ResponseBody value) throws IOException {
            return Long.valueOf(value.string());
        }
    }

    /* loaded from: classes3.dex */
    static final class ShortResponseBodyConverter implements Converter<ResponseBody, Short> {
        static final ShortResponseBodyConverter INSTANCE = new ShortResponseBodyConverter();

        ShortResponseBodyConverter() {
        }

        public Short convert(ResponseBody value) throws IOException {
            return Short.valueOf(value.string());
        }
    }
}
