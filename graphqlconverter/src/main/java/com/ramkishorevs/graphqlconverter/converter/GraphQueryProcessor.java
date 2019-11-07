package com.practo.fabric.app.central.utils.converter;

import android.content.Context;
import android.util.Log;
import com.practo.fabric.core.utils.LogUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class GraphQueryProcessor {
    private final Map<String, String> mGraphFiles;
    private String mQualifier = ".graphql";
    private String mRootFolder = "graphql";

    public GraphQueryProcessor(Context context) {
        mGraphFiles = new HashMap<>();
        findAllFiles(mRootFolder, context);
    }

    public GraphQueryProcessor(Context context, String qualifier) {
        mGraphFiles = new WeakHashMap<>();
        mQualifier = qualifier;
        findAllFiles(mRootFolder, context);
    }

    public GraphQueryProcessor(Context context, String qualifier, String rootFolder) {
        mGraphFiles = new WeakHashMap<>();
        mQualifier = qualifier;
        mRootFolder = rootFolder;
        findAllFiles(rootFolder, context);
    }

    public String getQuery(Annotation[] annotations) {
        GraphQuery graphQuery = null;

        for (Annotation annotation : annotations) {
            if (annotation instanceof GraphQuery) {
                graphQuery = (GraphQuery) annotation;
                break;
            }
        }

        if (mGraphFiles != null && graphQuery != null) {
            String fileName = String.format("%s%s", graphQuery.value(), mQualifier);
            if (mGraphFiles.containsKey(fileName)) {
                return mGraphFiles.get(fileName);
            }
            Log.w(toString(), String.format("The request query %s could not be found!", graphQuery.value()));
        }
        return null;
    }

    private void findAllFiles(String path, Context context) {
        try {
            String[] paths = context.getAssets().list(path);
            if (paths.length > 0) {
                for (String item : paths) {
                    String absolute = path + '/' + item;
                    if (!item.endsWith(mQualifier)) {
                        findAllFiles(absolute, context);
                    } else {
                        mGraphFiles.put(item, getFileContents(context.getAssets().open(absolute)));
                    }
                }
            }
        } catch (IOException e) {
            LogUtils.logException(e);
        }
    }

    private String getFileContents(InputStream inputStream) {
        StringBuilder queryBuffer = new StringBuilder();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                queryBuffer.append(line);
            }
            inputStreamReader.close();
            bufferedReader.close();
        } catch (IOException e) {
            LogUtils.logException(e);
        }
        return queryBuffer.toString();
    }
}
