package com.pataniqa.coursera.potlatch.store.remote;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;

import com.fasterxml.jackson.databind.*;

import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

/**
 * Replacement JSON converter for Retrofit that uses Jackson rather than JSON.
 * 
 * It makes it possible to use a Jackson converter in Retrofit rather than a
 * GSON converter. This solves problems when round tripping dates between
 * Retrofit and Spring.
 * 
 * This code was copied from <a href=
 * "https://github.com/kdubb1337/retrofit-examples/tree/master/src/main/java/com/kdubb/retrofitexamples/converter"
 * >here</a>.
 * 
 * For further details see this <a
 * href="http://kdubblabs.com/java/retrofit-by-square/retrofit-return-date/"
 * >blog post</a>.
 * 
 * for more details.
 */
public class JacksonConverter implements Converter {
    private final ObjectMapper objectMapper;

    public JacksonConverter(ObjectMapper objectMapper) {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper = objectMapper;
    }

    @Override
    public Object fromBody(TypedInput body, Type type) throws ConversionException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(type);

        try {
            return objectMapper.readValue(body.in(), javaType);
        } catch (IOException e) {
            throw new ConversionException(e);
        }
    }

    @Override
    public TypedOutput toBody(Object object) {
        try {
            String charset = "UTF-8";
            return new JsonTypedOutput(objectMapper.writeValueAsString(object).getBytes(charset),
                    charset);
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    private static class JsonTypedOutput implements TypedOutput {
        private final byte[] jsonBytes;
        private final String mimeType;

        public JsonTypedOutput(byte[] jsonBytes, String charset) {
            this.jsonBytes = jsonBytes;
            this.mimeType = "application/json; charset=" + charset;
        }

        @Override
        public String fileName() {
            return null;
        }

        @Override
        public String mimeType() {
            return mimeType;
        }

        @Override
        public long length() {
            return jsonBytes.length;
        }

        @Override
        public void writeTo(OutputStream out) throws IOException {
            out.write(jsonBytes);
        }
    }
}