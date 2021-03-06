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

import org.d2ab.function.IntBiPredicate;

import java.util.NoSuchElementException;

public class ForwardPeekingFilteringIntIterator extends DelegatingTransformingIntIterator<Integer, IntIterator> {
	private final int lastNext;
	private final IntBiPredicate predicate;

	private int next;
	private boolean hasNext;
	private int following;
	private boolean hasFollowing;
	private boolean started;

	public ForwardPeekingFilteringIntIterator(IntIterator iterator, int lastNext, IntBiPredicate predicate) {
		super(iterator);
		this.lastNext = lastNext;
		this.predicate = predicate;
	}

	@Override
	public boolean hasNext() {
		if (hasNext)
			return true;
		if (!started) {
			if (iterator.hasNext()) {
				following = iterator.nextInt();
				hasFollowing = true;
			}
			started = true;
		}
		if (!hasFollowing)
			return false;

		do {
			next = following;
			hasFollowing = iterator.hasNext();
			following = hasFollowing ? iterator.nextInt() : lastNext;
		} while (!(hasNext = predicate.test(next, following)) && hasFollowing);

		return hasNext;
	}

	@Override
	public int nextInt() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}

		hasNext = false;
		return next;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
