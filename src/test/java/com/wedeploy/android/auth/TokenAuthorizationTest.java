package com.wedeploy.android.auth;

import android.content.Intent;
import android.net.Uri;
import org.junit.Test;

import static junit.framework.TestCase.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Silvio Santos
 */
public class TokenAuthorizationTest {

	@Test
	public void getAuthFromIntent() {
		Intent intent = mock(Intent.class);
		Uri uri = mock(Uri.class);

		when(uri.getFragment()).thenReturn("access_token=123456");
		when(intent.getData()).thenReturn(uri);

		Authorization authorization = TokenAuthorization.getAuthorizationFromIntent(intent);

		assertNotNull(authorization);
		assertEquals("Bearer 123456", authorization.getAuthorizationHeader());
	}

	@Test
	public void getAuthFromIntent_withNoAccessToken_shouldReturnNull() {
		Intent intent = mock(Intent.class);
		Uri uri = mock(Uri.class);

		when(uri.getFragment()).thenReturn("somefragment=123456");
		when(intent.getData()).thenReturn(uri);

		Authorization authorization = TokenAuthorization.getAuthorizationFromIntent(intent);

		assertNull(authorization);
	}
}
