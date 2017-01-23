/*
 * Copyright 2016 Daniel Skogquist Åborg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.d2ab.iterator.ints;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static java.util.Objects.requireNonNull;
import static org.d2ab.util.Preconditions.requireSizeWithinBounds;

/**
 * An {@link Iterator} over an array of items.
 */
public class ArrayIntIterator implements IntIterator {
	protected int[] array;
	protected int offset;
	private int size;
	protected int index;

	public ArrayIntIterator(int... array) {
		this(array, array.length);
	}

	public ArrayIntIterator(int[] array, int size) {
		this(array, 0, size);
	}

	public ArrayIntIterator(int[] array, int offset, int size) {
		requireNonNull(array, "array");
		requireSizeWithinBounds(array.length, "array.length", offset, "offset");
		requireSizeWithinBounds(array.length - offset, "array.length - offset", size, "size");

		this.array = array;
		this.offset = offset;
		this.size = size;
	}

	@Override
	public boolean hasNext() {
		return index < size;
	}

	@Override
	public int nextInt() {
		if (!hasNext())
			throw new NoSuchElementException();

		return array[offset + index++];
	}
}
