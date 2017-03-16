package com.wedeploy.sdk.internal;

import com.wedeploy.sdk.RealTime;
import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.Transport;
import io.socket.parseqs.ParseQS;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SocketIORealTime implements RealTime {

	public SocketIORealTime(String url, Map<String, Object> options) throws URISyntaxException {
		socket = IO.socket(url, fromMap(options)).connect();

		final Map<String, String> headers = (Map<String, String>)options.get("headers");

		if (headers == null) {
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

	@SuppressWarnings("unchecked")
	protected static IO.Options fromMap(Map<String, Object> options) {
		IO.Options opts = new IO.Options();
		opts.forceNew = (boolean)options.get("forceNew");
		opts.path = (String)options.get("path");

		if (options.containsKey("query")) {
			opts.query = ParseQS.encode((Map)options.get("query"));
		}

		return opts;
	}

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

	private Socket socket;

}
