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
package org.d2ab.primitive.chars;

import java.util.NoSuchElementException;

/**
 * An iterator over chars that also maps each element by looking at the current AND the next element.
 */
public class ForwardPeekingMappingCharIterator implements CharIterator {
	private final CharIterator iterator;
	private final CharIntToCharBinaryFunction mapper;
	private int current = -1;
	private boolean started;

	public ForwardPeekingMappingCharIterator(CharIterator iterator, CharIntToCharBinaryFunction mapper) {
		this.iterator = iterator;
		this.mapper = mapper;
	}

	@Override
	public char nextChar() {
		if (!hasNext())
			throw new NoSuchElementException();

		int next = iterator.hasNext() ? iterator.nextChar() : -1;
		char result = mapper.applyAsCharAndInt((char) current, next);
		current = next;
		return result;
	}

	@Override
	public boolean hasNext() {
		if (!started) {
			if (iterator.hasNext())
				current = iterator.next();
			started = true;
		}
		return current != -1;
	}
}