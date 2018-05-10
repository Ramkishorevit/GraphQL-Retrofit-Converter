package com.ramkishorevs.graphqlconverter.converter;

/*
* *
 * Created by ramkishorevs
 */

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;


public class GraphQLConverter extends Converter.Factory {

    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    protected GraphQueryProcessor graphProcessor;
    protected final Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .setLenient().create();

    /**
     * Protected constructor because we want to make use of the
     * Factory Pattern to create our converter
     * <br/>
     *
     * @param context Any valid application context
     */
    protected GraphQLConverter(Context context) {
        graphProcessor = new GraphQueryProcessor(context);
    }

    public static GraphQLConverter create(Context context) {
        return new GraphQLConverter(context);
    }

    /**
     * Response body converter delegates logic processing to a child class that handles
     * wrapping and deserialization of the json response data.
     * @see GraphResponseConverter
     * <br/>
     *
     * @param annotations All the annotation applied to the requesting Call method
     *                    @see retrofit2.Call
     * @param retrofit The retrofit object representing the response
     * @param type The generic type declared on the Call method
     */
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {

        if (type instanceof ResponseBody) {
            return super.responseBodyConverter(type, annotations, retrofit);
        } else {
            TypeAdapter<?> adapter = this.gson.getAdapter(TypeToken.get(type));
            return new GraphResponseConverter(this.gson, adapter);
        }
    }

    /**
     * Response body converter delegates logic processing to a child class that handles
     * wrapping and deserialization of the json response data.
     * @see GraphRequestConverter
     * <br/>
     *
     * @param parameterAnnotations All the annotation applied to request parameters
     * @param methodAnnotations All the annotation applied to the requesting method
     * @param retrofit The retrofit object representing the response
     * @param type The type of the parameter of the request
     */
    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new GraphRequestConverter(methodAnnotations);
    }


    protected class GraphResponseConverter<T> implements Converter<ResponseBody, T> {
        private final Gson gson;
        private final TypeAdapter<T> adapter;

        GraphResponseConverter(Gson gson, TypeAdapter<T> adapter) {
            this.gson = gson;
            this.adapter = adapter;
        }

        @Override
        public T convert(ResponseBody value) throws IOException {
            JsonReader jsonReader = gson.newJsonReader(value.charStream());
            try {
                return adapter.read(jsonReader);
            } finally {
                value.close();
            }
        }
    }


    protected class GraphRequestConverter implements Converter<QueryContainerBuilder, RequestBody> {
        protected Annotation[] methodAnnotations;

        protected GraphRequestConverter(Annotation[] methodAnnotations) {
            this.methodAnnotations = methodAnnotations;
        }

        @Override
        public RequestBody convert(@NonNull QueryContainerBuilder containerBuilder) {
            QueryContainerBuilder.QueryContainer queryContainer = containerBuilder
                    .setQuery(graphProcessor.getQuery(methodAnnotations))
                    .build();
            String queryJson = gson.toJson(queryContainer);
            return RequestBody.create(MEDIA_TYPE, queryJson.getBytes());
        }
    }
}