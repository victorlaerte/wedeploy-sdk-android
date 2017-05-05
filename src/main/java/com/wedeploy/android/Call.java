package com.wedeploy.android;

import com.wedeploy.android.exception.WeDeployException;
import com.wedeploy.android.transport.AsyncTransport;
import com.wedeploy.android.transport.Request;
import com.wedeploy.android.transport.Response;
import com.wedeploy.android.transport.Transport;
import com.wedeploy.android.util.Platform;
import com.wedeploy.android.util.Validator;
import io.reactivex.Single;

import java.util.concurrent.Callable;

/**
 * @author Silvio Santos
 */
public class Call<T> {

	Call(Request request, Transport transport, AsyncTransport asyncTransport, Class<T> clazz) {
		this.request = request;
		this.transport = transport;
		this.asyncTransport = asyncTransport;
		this.clazz = clazz;
	}

	public T execute() throws WeDeployException {
		Response response = null;

		try {
			response = transport.send(request);
		}
		catch (Exception e) {
			throw new WeDeployException("Error while executing request", e);
		}

		Validator.checkResponseCode(response);

		if (clazz.isInstance(response)) {
			return clazz.cast(response);
		}

		throw new IllegalStateException("Unable to convert response to " + clazz.getSimpleName());
	}

	public void execute(final Callback callback) {
		asyncTransport.sendAsync(request, new Callback() {
			@Override
			public void onSuccess(final Response response) {
				try {
					Validator.checkResponseCode(response);

					Platform.get().run(new Runnable() {
						@Override
						public void run() {
							callback.onSuccess(response);
						}
					});
				}
				catch (final WeDeployException e) {
					Platform.get().run(new Runnable() {
						@Override
						public void run() {
							callback.onFailure(e);
						}
					});
				}
			}

			@Override
			public void onFailure(final Exception e) {
				Platform.get().run(new Runnable() {
					@Override
					public void run() {
						callback.onFailure(e);
					}
				});
			}
		});
	}

	public Single<T> asSingle() {
		return Single.fromCallable(new Callable<T>() {
			@Override
			public T call() throws Exception {
				return execute();
			}
		});
	}

	private AsyncTransport<Response> asyncTransport;
	private Class<T> clazz;
	private Request request;
	private Transport<Response> transport;

}