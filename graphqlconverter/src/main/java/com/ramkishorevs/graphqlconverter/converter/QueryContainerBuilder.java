package com.practo.fabric.app.central.utils.converter;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.WeakHashMap;

public class QueryContainerBuilder implements Parcelable {
    private QueryContainer mQueryContainer;

    public QueryContainerBuilder() {
        mQueryContainer = new QueryContainer();
    }

    private QueryContainerBuilder(Parcel in) {
        mQueryContainer = in.readParcelable(QueryContainer.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mQueryContainer, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QueryContainerBuilder> CREATOR = new Creator<QueryContainerBuilder>() {
        @Override
        public QueryContainerBuilder createFromParcel(Parcel in) {
            return new QueryContainerBuilder(in);
        }

        @Override
        public QueryContainerBuilder[] newArray(int size) {
            return new QueryContainerBuilder[size];
        }
    };

    public QueryContainerBuilder setQuery(String query) {
        mQueryContainer.setQuery(query);
        return this;
    }

    public QueryContainerBuilder addVariable(String key, Object value) {
        mQueryContainer.setVariable(key, value);
        return this;
    }

    public boolean containsVariable(String key) {
        return mQueryContainer.containsVariable(key);
    }


    public QueryContainer build() {
        return mQueryContainer;
    }


    public static class QueryContainer implements Parcelable {
        @SerializedName("query")
        private String mQuery;
        @SerializedName("variables")
        private HashMap<String, Object> mVariables;

        QueryContainer() {
            mVariables = new HashMap<>();
        }

        QueryContainer(Parcel in) {
            mQuery = in.readString();
            mVariables = in.readHashMap(WeakHashMap.class.getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mQuery);
            dest.writeMap(mVariables);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<QueryContainer> CREATOR = new Creator<QueryContainer>() {
            @Override
            public QueryContainer createFromParcel(Parcel in) {
                return new QueryContainer(in);
            }

            @Override
            public QueryContainer[] newArray(int size) {
                return new QueryContainer[size];
            }
        };

        protected void setQuery(String query) {
            mQuery = query;
        }

        void setVariable(String key, Object value) {
            mVariables.put(key, value);
        }

        boolean containsVariable(String key) {
            return mVariables != null && mVariables.containsKey(key);
        }
    }
}
