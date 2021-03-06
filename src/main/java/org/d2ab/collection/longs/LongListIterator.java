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

package org.d2ab.collection.longs;

import org.d2ab.iterator.longs.LongIterator;

import java.util.ListIterator;

/**
 * A {@link ListIterator} over a sequence of {@code long} values.
 */
public interface LongListIterator extends ListIterator<Long>, LongIterator {
	static LongListIterator of(long... xs) {
		return new ArrayLongListIterator(xs);
	}

	@Override
	boolean hasNext();

	@Override
	long nextLong();

	@Override
	default Long next() {
		return nextLong();
	}

	@Override
	boolean hasPrevious();

	long previousLong();

	@Override
	default Long previous() {
		return previousLong();
	}

	@Override
	int nextIndex();

	@Override
	int previousIndex();

	@Override
	default void remove() {
		throw new UnsupportedOperationException();
	}

	default void set(long x) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void set(Long x) {
		set((long) x);
	}

	default void add(long x) {
		throw new UnsupportedOperationException();
	}

	@Override
	default void add(Long x) {
		add((long) x);
	}

	static LongListIterator forwardOnly(LongIterator iterator, int index) {
		int skipped = iterator.skip(index);
		if (skipped != index)
			throw new IndexOutOfBoundsException("index: " + index + " size: " + skipped);

		return new LongListIterator() {
			int cursor = index;

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public long nextLong() {
				long nextLong = iterator.nextLong();
				cursor++;
				return nextLong;
			}

			@Override
			public void remove() {
				iterator.remove();
				cursor--;
			}

			@Override
			public boolean hasPrevious() {
				throw new UnsupportedOperationException();
			}

			@Override
			public long previousLong() {
				throw new UnsupportedOperationException();
			}

			@Override
			public int nextIndex() {
				return cursor;
			}

			@Override
			public int previousIndex() {
				return cursor - 1;
			}
		};
	}
}
