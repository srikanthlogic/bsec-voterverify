package retrofit2.converter.scalars;

import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;
/* loaded from: classes3.dex */
final class ScalarRequestBodyConverter<T> implements Converter<T, RequestBody> {
    static final ScalarRequestBodyConverter<Object> INSTANCE = new ScalarRequestBodyConverter<>();
    private static final MediaType MEDIA_TYPE = MediaType.parse("text/plain; charset=UTF-8");

    private ScalarRequestBodyConverter() {
    }

    @Override // retrofit2.Converter
    /* renamed from: convert  reason: avoid collision after fix types in other method */
    public RequestBody convert2(T value) throws IOException {
        return RequestBody.create(MEDIA_TYPE, String.valueOf(value));
    }
}
