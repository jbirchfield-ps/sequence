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

package org.d2ab.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;

public class InclusiveTerminalIterator<T> extends DelegatingUnaryIterator<T> {
	private final Predicate<? super T> terminalPredicate;

	private T previous;
	private boolean hasPrevious;

	public InclusiveTerminalIterator(Iterator<T> iterator, T terminal) {
		this(iterator, o -> Objects.equals(o, terminal));
	}

	public InclusiveTerminalIterator(Iterator<T> iterator, Predicate<? super T> terminalPredicate) {
		super(iterator);
		this.terminalPredicate = terminalPredicate;
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext() && !(hasPrevious && terminalPredicate.test(previous));
	}

	@Override
	public T next() {
		if (!hasNext())
			throw new NoSuchElementException();

		hasPrevious = true;
		return previous = iterator.next();
	}
}
