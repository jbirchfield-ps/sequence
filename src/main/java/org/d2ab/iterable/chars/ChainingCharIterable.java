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

package org.d2ab.iterable.chars;

import org.d2ab.iterator.chars.ChainingCharIterator;
import org.d2ab.iterator.chars.CharIterator;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static java.util.Arrays.asList;

/**
 *
 */
public class ChainingCharIterable implements CharIterable {
	private final Collection<CharIterable> iterables = new ArrayList<>();

	public ChainingCharIterable(CharIterable... iterables) {
		asList(iterables).forEach(e -> this.iterables.add(Objects.requireNonNull(e)));
	}

	public ChainingCharIterable append(CharIterable iterable) {
		iterables.add(iterable);
		return this;
	}

	@Override
	public CharIterator iterator() {
		return new ChainingCharIterator(iterables);
	}

	@Override
	public int hashCode() {
		return iterables.hashCode();
	}

	@Override
	public boolean equals(@Nullable Object o) {
		if (this == o)
			return true;
		if ((o == null) || (getClass() != o.getClass()))
			return false;

		ChainingCharIterable that = (ChainingCharIterable) o;

		return iterables.equals(that.iterables);
	}

	@Override
	public String toString() {
		return "ChainingCharIterable" + iterables;
	}
}