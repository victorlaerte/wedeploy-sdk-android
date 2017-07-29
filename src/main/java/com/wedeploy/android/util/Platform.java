package com.wedeploy.android.util;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;

/**
 * @author Silvio Santos
 */
public abstract class Platform {

	public static Platform PLATFORM = getPlatform();

	public static Platform get() {
		return PLATFORM;
	}

	private static Platform getPlatform() {
		try {
			Class.forName("android.os.Build");

			if (Build.VERSION.SDK_INT != 0) {
				return new AndroidPlatform();
			}
		}
		catch (ClassNotFoundException cnfe) {
		}

		return new JavaPlatform();
	}

	public abstract void run(Runnable runnable);

	static class JavaPlatform extends Platform {

		@Override
		public void run(Runnable runnable) {
			runnable.run();
		}
	}

	static class AndroidPlatform extends Platform {

		@Override
		public void run(Runnable runnable) {
			Handler handler = new Handler(Looper.getMainLooper());
			handler.post(runnable);
		}
	}

}
