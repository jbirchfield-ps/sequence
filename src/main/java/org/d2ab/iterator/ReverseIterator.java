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

import java.util.*;

/**
 * An {@link Iterator} that iterates over the elements of another {@link Iterator} in reverse order, by creating a
 * buffer over the elements in the {@link Iterator} and reversing the order of iteration.
 */
public class ReverseIterator<T> implements Iterator<T> {
	private final Iterator<? extends T> iterator;
	private ListIterator<? extends T> listIterator;

	public ReverseIterator(Iterator<? extends T> iterator) {
		this.iterator = iterator;
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext() || listIterator != null && listIterator.hasPrevious();
	}

	@Override
	public T next() {
		if (!hasNext())
			throw new NoSuchElementException();

		if (listIterator == null) {
			List<T> list = new ArrayList<>();
			iterator.forEachRemaining(list::add);
			listIterator = list.listIterator(list.size());
		}

		return listIterator.previous();
	}
}