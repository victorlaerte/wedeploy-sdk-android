package com.wedeploy.sdk.auth;

import android.content.Intent;
import android.net.Uri;
import org.junit.Test;

import static junit.framework.TestCase.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Silvio Santos
 */
public class TokenAuthTest {

	@Test
	public void getAuthFromIntent() {
		Intent intent = mock(Intent.class);
		Uri uri = mock(Uri.class);

		when(uri.getFragment()).thenReturn("access_token=123456");
		when(intent.getData()).thenReturn(uri);

		Auth auth = TokenAuth.getAuthFromIntent(intent);

		assertNotNull(auth);
		assertEquals("Bearer 123456", auth.getAuthorizationHeader());
	}

	@Test
	public void getAuthFromIntent_withNoAccessToken_shouldReturnNull() {
		Intent intent = mock(Intent.class);
		Uri uri = mock(Uri.class);

		when(uri.getFragment()).thenReturn("somefragment=123456");
		when(intent.getData()).thenReturn(uri);

		Auth auth = TokenAuth.getAuthFromIntent(intent);

		assertNull(auth);
	}
}
