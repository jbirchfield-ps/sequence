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

package org.d2ab.iterator.chars;

import org.d2ab.function.CharPredicate;

import java.util.NoSuchElementException;

public class FilteringCharIterator extends DelegatingUnaryCharIterator {
	private final CharPredicate predicate;

	private char next;
	private boolean hasNext;

	public FilteringCharIterator(CharIterator iterator, CharPredicate predicate) {
		super(iterator);
		this.predicate = predicate;
	}

	@Override
	public char nextChar() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}

		hasNext = false;
		return next;
	}

	@Override
	public boolean hasNext() {
		if (hasNext) { // already checked
			return true;
		}

		do { // find next matching, bail out if EOF
			hasNext = iterator.hasNext();
			if (!hasNext)
				return false;
			next = iterator.nextChar();
		} while (!predicate.test(next));

		// found matching value
		return true;
	}
}
