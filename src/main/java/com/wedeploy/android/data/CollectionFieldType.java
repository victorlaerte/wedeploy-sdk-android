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

package com.wedeploy.android.data;

/**
 * Enumerator used to create typed collections in WeDeploy’s data service.
 *
 * {@link CollectionFieldTypeValue} implementation that represents all supported WeDeploy primitive types.
 *
 * Available Collection field types
 * <li>{@link #TEXT}</li>
 * <li>{@link #KEYWORD}</li>
 * <li>{@link #STRING}</li>
 * <li>{@link #NESTED}</li>
 * <li>{@link #INTEGER}</li>
 * <li>{@link #LONG}</li>
 * <li>{@link #FLOAT}</li>
 * <li>{@link #DOUBLE}</li>
 * <li>{@link #BOOLEAN}</li>
 * <li>{@link #DATE}</li>
 * <li>{@link #GEO_POINT}</li>
 * <li>{@link #GEO_SHAPE}</li>
 * <li>{@link #BINARY}</li>
 *
 * @author Victor Oliveira
 */
public enum CollectionFieldType implements CollectionFieldTypeValue {

	/**
	 * Field of type text.
	 */
	TEXT,

	/**
	 * Field of type keyword.
	 */
	KEYWORD,

	/**
	 * @deprecated Use TEXT or KEYWORD.
	 */
	@Deprecated
	STRING,
	
	/**
	 * Field of type nested.
	 */
	NESTED,

	/**
	 * Field of type integer.
	 */
	INTEGER,

	/**
	 * Field of type long.
	 */
	LONG,

	/**
	 * Field of type float.
	 */
	FLOAT,

	/**
	 * Field of type double.
	 */
	DOUBLE,

	/**
	 * Field of type boolean.
	 */
	BOOLEAN,

	/**
	 * Field of type date.
	 */
	DATE,

	/**
	 * Field of type geo point.
	 */
	GEO_POINT,

	/**
	 * Field of type geo shape.
	 */
	GEO_SHAPE,

	/**
	 * Field of type binary.
	 */
	BINARY;

	@Override
	public String toString() {
		return "\"" + super.toString().toLowerCase() + "\"";
	}

	/**
	 * Check if {@link CollectionFieldTypeValue} implementation is empty.
	 *
	 * @return This implementation returns false.
	 */
	@Override
	public boolean isEmpty() {
		return false;
	}
}
