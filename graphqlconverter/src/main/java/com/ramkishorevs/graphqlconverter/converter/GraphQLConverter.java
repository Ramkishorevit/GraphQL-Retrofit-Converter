package com.practo.fabric.app.central.utils.converter;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class GraphQLConverter extends Converter.Factory {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private GraphQueryProcessor mGraphProcessor;
    private final Gson mGson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .setLenient().create();

    /**
     * Protected constructor because we want to make use of the
     * Factory Pattern to create our converter
     * <br/>
     *
     * @param context Any valid application context
     */
    private GraphQLConverter(Context context) {
        mGraphProcessor = new GraphQueryProcessor(context);
    }

    public static GraphQLConverter create(Context context) {
        return new GraphQLConverter(context);
    }

    /**
     * Response body converter delegates logic processing to a child class that handles
     * wrapping and deserialization of the json response data.
     *
     * @param annotations All the annotation applied to the requesting Call method
     * @param retrofit    The retrofit object representing the response
     * @param type        The generic type declared on the Call method
     * @see GraphResponseConverter
     * <br/>
     * @see retrofit2.Call
     */
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {

        TypeAdapter<?> adapter = mGson.getAdapter(TypeToken.get(type));
        return new GraphResponseConverter<>(mGson, adapter);
    }

    /**
     * Response body converter delegates logic processing to a child class that handles
     * wrapping and deserialization of the json response data.
     *
     * @param parameterAnnotations All the annotation applied to request parameters
     * @param methodAnnotations    All the annotation applied to the requesting method
     * @param retrofit             The retrofit object representing the response
     * @param type                 The type of the parameter of the request
     * @see GraphRequestConverter
     * <br/>
     */
    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations,
                                                          Annotation[] methodAnnotations, Retrofit retrofit) {
        return new GraphRequestConverter(methodAnnotations);
    }


    protected class GraphResponseConverter<T> implements Converter<ResponseBody, T> {
        private final Gson mGson;
        private final TypeAdapter<T> mAdapter;

        GraphResponseConverter(Gson gson, TypeAdapter<T> adapter) {
            mGson = gson;
            mAdapter = adapter;
        }

        @Override
        public T convert(ResponseBody value) throws IOException {
            JsonReader jsonReader = mGson.newJsonReader(value.charStream());
            try {
                T result = mAdapter.read(jsonReader);
                if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                    throw new JsonIOException("JSON document was not fully consumed.");
                }
                return result;
            } finally {
                value.close();
            }
        }
    }


    protected class GraphRequestConverter implements Converter<QueryContainerBuilder, RequestBody> {
        protected Annotation[] methodAnnotations;

        protected GraphRequestConverter(Annotation[] methodAnnotations) {
            this.methodAnnotations = methodAnnotations.clone();
        }

        @Override
        public RequestBody convert(@NonNull QueryContainerBuilder containerBuilder) {
            QueryContainerBuilder.QueryContainer queryContainer = containerBuilder
                    .setQuery(mGraphProcessor.getQuery(methodAnnotations))
                    .build();
            String queryJson = mGson.toJson(queryContainer);
            return RequestBody.create(MEDIA_TYPE, queryJson.getBytes());
        }
    }
}
