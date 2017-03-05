package com.wedeploy.sdk.internal;

import com.wedeploy.sdk.Request;
import com.wedeploy.sdk.WeDeployException;
import okhttp3.*;
import okhttp3.internal.http.HttpMethod;

import java.io.IOException;
import java.util.Map;

public class OkHttpTransport implements Transport<Response> {

    public Response send(Request request) {
        okhttp3.Request okHttpRequest = geOkHttpRequest(request);
        OkHttpClient client = new OkHttpClient();

        try {
            return client.newCall(okHttpRequest).execute();
        }
        catch (IOException e) {
            throw new WeDeployException(e.getMessage(), e);
        }
    }

    private okhttp3.Request geOkHttpRequest(Request request) {
        HttpUrl url = getUrl(request);
        RequestBody body = getRequestBody(request);
        String method = request.getMethod().getValue();

        okhttp3.Request.Builder builder = new okhttp3.Request.Builder()
            .url(url)
            .method(method, body);

        for (Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }

        return builder.build();
    }

    private RequestBody getRequestBody(Request request) {
        RequestMethod method = request.getMethod();
        String body = request.getBody();

        if (!HttpMethod.requiresRequestBody(method.getValue())) {
            return null;
        }

        return RequestBody.create(JSON, body);

    }

    private HttpUrl getUrl(Request request) {
        HttpUrl.Builder builder = HttpUrl.parse(request.getUrl())
            .newBuilder()
            .addPathSegments(request.getPath());

        for (Map.Entry<String, String> entry : request.getParams().entrySet()) {
            builder.addQueryParameter(entry.getKey(), entry.getValue());
        }

        return builder.build();
    }

    private MediaType JSON = MediaType.parse("application/json; charset=utf-8");

}
