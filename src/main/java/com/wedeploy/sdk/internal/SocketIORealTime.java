package com.wedeploy.sdk.internal;

import com.wedeploy.sdk.RealTime;
import com.wedeploy.sdk.exception.WeDeployException;
import com.wedeploy.sdk.util.URLUtil;
import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.Transport;
import io.socket.parseqs.ParseQS;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SocketIORealTime implements RealTime {

	@Override
	public void close() {
		socket.close();
	}

	@Override
	public RealTime on(String event, final OnEventListener listener) {
		socket.on(event, new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				listener.onEvent(args);
			}
		});

		return this;
	}

	private void setHeaders(final Map<String, String> headers) {
		if (headers.isEmpty()) {
			return;
		}

		socket.io().on(Manager.EVENT_TRANSPORT, new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				Transport transport = (Transport)args[0];

				transport.on(Transport.EVENT_REQUEST_HEADERS, new Emitter.Listener() {
					@Override
					public void call(Object... args1) {
						Map<String, List<String>> requestHeaders =
							(Map<String, List<String>>)args1[0];

						for (Map.Entry<String, String> entry : headers.entrySet()) {
							requestHeaders.put(
								entry.getKey(), Collections.singletonList(entry.getValue()));
						}

					}
				});
			}
		});
	}

	private SocketIORealTime(SocketIORealTime.Builder builder) {
		IO.Options options = new IO.Options();
		options.forceNew = builder.forceNew;
		options.path = builder.path;

		if (!options.path.startsWith("/")) {
			options.path = "/" + options.path;
		}

		if (builder.query != null) {
			String query = URLUtil.joinPathAndQuery(options.path, builder.query);
			options.query = ParseQS.encode(Collections.singletonMap("url", query));
		}

		try {
			socket = IO.socket(builder.url, options).connect();
		}
		catch (URISyntaxException e) {
			throw new WeDeployException("Couldn't open realtime socket connection", e);
		}

		setHeaders(builder.headers);
	}

	private Socket socket;

	public static class Builder {

		boolean forceNew;
		Map<String, String> headers = new HashMap<>();
		String path;
		String query;
		String url;

		public Builder(String url) {
			this.url = url;
		}

		public Builder forceNew(boolean forceNew) {
			this.forceNew = forceNew;
			return this;
		}

		public Builder header(String name, String value) {
			this.headers.put(name, value);
			return this;
		}

		public Builder path(String path) {
			this.path = path;
			return this;
		}

		public Builder query(String query) {
			this.query = query;
			return this;
		}

		public RealTime build() {
			return new SocketIORealTime(this);
		}

	}

}
