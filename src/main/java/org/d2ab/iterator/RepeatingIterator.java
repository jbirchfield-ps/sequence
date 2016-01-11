/*
 * Copyright 2015 Daniel Skogquist Åborg
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

package org.d2ab.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An {@link Iterator} that cycles the values of an {@link Iterable} forever. This class repeatedly calls
 * {@link Iterable#iterator()} to receive new values when the iterator ends, so it's possible to cause this
 * {@link Iterator} to terminate by providing an empty {@link Iterator}. If the {@link Iterable} never returns an
 * empty {@link Iterator}, this {@link Iterator} will never terminate.
 */
public class RepeatingIterator<T> extends UnaryReferenceIterator<T> {
	private final Iterable<T> iterable;

	public RepeatingIterator(Iterable<T> iterable) {
		this.iterable = iterable;
	}

	@Override
	public boolean hasNext() {
		if (iterator == null || !iterator.hasNext()) {
			iterator = iterable.iterator();
		}
		return iterator.hasNext();
	}

	@Override
	public T next() {
		if (!hasNext())
			throw new NoSuchElementException();

		return iterator.next();
	}
}
