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
