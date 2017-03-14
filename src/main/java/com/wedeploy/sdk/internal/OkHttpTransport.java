package com.wedeploy.sdk.internal;

import com.wedeploy.sdk.Request;
import com.wedeploy.sdk.Response;
import com.wedeploy.sdk.WeDeployException;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.internal.http.HttpMethod;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class OkHttpTransport implements Transport<Response> {

    public Response send(Request request) {
        try {
	        okhttp3.Request okHttpRequest = geOkHttpRequest(request);
	        OkHttpClient client = new OkHttpClient();
            okhttp3.Response okHttpResponse = client.newCall(okHttpRequest)
	            .execute();

            return new Response.Builder()
	            .body(okHttpResponse.body().string())
	            .headers(okHttpResponse.headers().toMultimap())
	            .statusCode(okHttpResponse.code())
	            .statusMessage(okHttpResponse.message())
	            .succeeded(okHttpResponse.isSuccessful())
	            .build();

        }
        catch (Exception e) {
            throw new WeDeployException(e.getMessage(), e);
        }
    }

    private okhttp3.Request geOkHttpRequest(Request request) throws UnsupportedEncodingException {
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

        Map<String, String> forms = request.getForms();

	    if (!forms.isEmpty()) {
		    FormBody.Builder builder = new FormBody.Builder();

		    for (Map.Entry<String, String> entry : forms.entrySet()) {
			    builder.add(entry.getKey(), entry.getValue());
		    }

		    return builder.build();
	    }

	    return RequestBody.create(JSON, body);

    }

    private HttpUrl getUrl(Request request) throws UnsupportedEncodingException {
        HttpUrl.Builder builder = HttpUrl.parse(request.getUrl())
            .newBuilder()
            .addPathSegments(request.getPath());

        for (Map.Entry<String, String> entry : request.getParams().entrySet()) {
	        String encodedValue = URLEncoder.encode(
		        entry.getValue(), "UTF-8");

	        builder.addEncodedQueryParameter(entry.getKey(), encodedValue);
        }

        return builder.build();
    }

    private MediaType JSON = MediaType.parse("application/json; charset=utf-8");

}
