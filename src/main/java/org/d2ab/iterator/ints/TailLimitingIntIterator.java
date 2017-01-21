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

import java.util.NoSuchElementException;

/**
 * An {@link IntIterator} that limits the results to a set number of {@code ints} at the end of another
 * {@link IntIterator}.
 */
public class TailLimitingIntIterator extends DelegatingUnaryIntIterator {
	private final int limit;

	private boolean started;
	private int[] buffer;
	private int offset;
	private int index;
	private int size;

	public TailLimitingIntIterator(IntIterator iterator, int limit) {
		super(iterator);
		this.limit = limit;
	}

	@Override
	public boolean hasNext() {
		if (!started) {
			buffer = new int[limit];
			int i = 0;
			while (iterator.hasNext()) {
				buffer[i] = iterator.nextInt();
				i = ++i % limit;
				if (size < limit)
					size++;
			}

			offset = i % size;
			started = true;
		}

		return index < size;
	}

	@Override
	public int nextInt() {
		if (!hasNext())
			throw new NoSuchElementException();

		return buffer[(offset + index++) % limit];
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
