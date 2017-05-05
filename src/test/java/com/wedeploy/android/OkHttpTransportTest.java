package com.wedeploy.android;

import com.wedeploy.android.transport.OkHttpTransport;
import com.wedeploy.android.transport.RequestMethod;
import com.wedeploy.android.transport.Request;
import com.wedeploy.android.transport.Response;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Silvio Santos
 */
public class OkHttpTransportTest {

	@Test
	public void send() throws InterruptedException {
		OkHttpTransport transport = new OkHttpTransport.Builder().build();

		Request request = new Request.Builder()
			.method(RequestMethod.GET)
			.url("http://www.google.com")
			.build();

		Response response = transport.send(request);

		assertTrue(response.succeeded());
	}

	@Test
	public void sendAsync() throws InterruptedException {
		final CountDownLatch latch = new CountDownLatch(1);

		OkHttpTransport transport = new OkHttpTransport.Builder().build();

		Request request = new Request.Builder()
			.method(RequestMethod.GET)
			.url("http://www.google.com")
			.build();

		transport.sendAsync(request, new Callback() {
			@Override
			public void onSuccess(Response response) {
				assertTrue(response.succeeded());

				latch.countDown();
			}

			@Override
			public void onFailure(Exception e) {
				fail();
			}
		});

		latch.await(5000, TimeUnit.MILLISECONDS);
	}

}