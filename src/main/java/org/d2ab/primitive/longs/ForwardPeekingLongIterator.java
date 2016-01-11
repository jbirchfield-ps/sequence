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

package org.d2ab.primitive.longs;

import java.util.NoSuchElementException;

/**
 * An iterator over ints that also maps each element by looking at the current AND the next element.
 */
public class ForwardPeekingLongIterator extends UnaryLongIterator {
	private final ForwardPeekingLongFunction mapper;

	private boolean hasCurrent;
	private long current = -1;
	private boolean started;

	public ForwardPeekingLongIterator(ForwardPeekingLongFunction mapper) {
		this.mapper = mapper;
	}

	@Override
	public long nextLong() {
		if (!hasNext())
			throw new NoSuchElementException();

		boolean hasNext = iterator.hasNext();
		long next = hasNext ? iterator.nextLong() : -1;

		long result = mapper.applyAndPeek(current, hasNext, next);

		current = next;
		hasCurrent = hasNext;
		return result;
	}

	@Override
	public boolean hasNext() {
		if (!started) {
			started = true;
			if (iterator.hasNext()) {
				current = iterator.next();
				hasCurrent = true;
			}
		}
		return hasCurrent;
	}
}