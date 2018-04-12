/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of Liferay, Inc. nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.wedeploy.android.transport;

import com.wedeploy.android.data.RealTime;
import com.wedeploy.android.util.Platform;
import com.wedeploy.android.util.URLUtil;
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
			public void call(final Object... args) {
				Platform.get().run(new Runnable() {
					@Override
					public void run() {
						listener.onEvent(args);
					}
				});
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
			throw new RuntimeException("Couldn't open realtime socket connection", e);
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
