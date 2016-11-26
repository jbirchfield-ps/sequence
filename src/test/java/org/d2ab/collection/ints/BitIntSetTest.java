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

package org.d2ab.collection.ints;

import org.d2ab.collection.Arrayz;
import org.d2ab.iterator.ints.IntIterator;
import org.junit.Test;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.d2ab.test.IsIntIterableContainingInOrder.containsInts;
import static org.d2ab.test.Tests.expecting;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.assertThat;

public class BitIntSetTest {
	private final BitIntSet empty = new BitIntSet();
	private final BitIntSet set = new BitIntSet(-5, -4, -3, -2, -1, 0, 1, 2, 3, 4);

	@Test
	public void size() {
		assertThat(empty.size(), is(0));
		assertThat(set.size(), is(10));
	}

	@Test
	public void iterator() {
		assertThat(empty, is(emptyIterable()));
		assertThat(set, containsInts(-5, -4, -3, -2, -1, 0, 1, 2, 3, 4));
	}

	@Test
	public void isEmpty() {
		assertThat(empty.isEmpty(), is(true));
		assertThat(set.isEmpty(), is(false));
	}

	@Test
	public void clear() {
		empty.clear();
		assertThat(empty.isEmpty(), is(true));

		set.clear();
		assertThat(set.isEmpty(), is(true));
	}

	@Test
	public void addInt() {
		empty.addInt(17);
		assertThat(empty, containsInts(17));

		set.addInt(17);
		assertThat(set, containsInts(-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 17));
	}

	@Test
	public void containsInt() {
		assertThat(empty.containsInt(17), is(false));

		assertThat(set.containsInt(17), is(false));
		for (int x = -5; x <= 4; x++)
			assertThat(set.containsInt(x), is(true));
	}

	@Test
	public void removeInt() {
		assertThat(empty.removeInt(17), is(false));

		assertThat(set.removeInt(17), is(false));
		for (int x = -5; x <= 4; x++)
			assertThat(set.removeInt(x), is(true));
		assertThat(set.isEmpty(), is(true));
	}

	@Test
	public void testToString() {
		assertThat(empty.toString(), is("[]"));
		assertThat(set.toString(), is("[-5, -4, -3, -2, -1, 0, 1, 2, 3, 4]"));
	}

	@Test
	public void testEqualsHashCode() {
		BitIntSet set2 = new BitIntSet(-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 17);
		assertThat(set, is(not(equalTo(set2))));
		assertThat(set.hashCode(), is(not(set2.hashCode())));

		set2.removeInt(17);

		assertThat(set, is(equalTo(set2)));
		assertThat(set.hashCode(), is(set2.hashCode()));
	}

	@Test
	public void addAllIntArray() {
		assertThat(empty.addAllInts(1, 2, 3), is(true));
		assertThat(empty, containsInts(1, 2, 3));

		assertThat(set.addAllInts(3, 4, 5, 6, 7), is(true));
		assertThat(set, containsInts(-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7));
	}

	@Test
	public void addAllIntCollection() {
		assertThat(empty.addAllInts(org.d2ab.collection.ints.IntList.of(1, 2, 3)), is(true));
		assertThat(empty, containsInts(1, 2, 3));

		assertThat(set.addAllInts(org.d2ab.collection.ints.IntList.of(3, 4, 5, 6, 7)), is(true));
		assertThat(set, containsInts(-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7));
	}

	@Test
	public void stream() {
		assertThat(empty.stream().collect(Collectors.toList()), is(emptyIterable()));
		assertThat(set.stream().collect(Collectors.toList()), contains(-5, -4, -3, -2, -1, 0, 1, 2, 3, 4));
	}

	@Test
	public void parallelStream() {
		assertThat(empty.parallelStream().collect(Collectors.toList()), is(emptyIterable()));
		assertThat(set.parallelStream().collect(Collectors.toList()), contains(-5, -4, -3, -2, -1, 0, 1, 2, 3, 4));
	}

	@Test
	public void intStream() {
		assertThat(empty.intStream().collect(ArrayIntList::new, ArrayIntList::addInt, ArrayIntList::addAllInts),
		           is(emptyIterable()));

		assertThat(set.intStream().collect(ArrayIntList::new, ArrayIntList::addInt, ArrayIntList::addAllInts),
		           containsInts(-5, -4, -3, -2, -1, 0, 1, 2, 3, 4));
	}

	@Test
	public void parallelIntStream() {
		assertThat(empty.parallelIntStream().collect(ArrayIntList::new, ArrayIntList::addInt, ArrayIntList::addAllInts),
		           is(emptyIterable()));

		assertThat(set.parallelIntStream().collect(ArrayIntList::new, ArrayIntList::addInt, ArrayIntList::addAllInts),
		           containsInts(-5, -4, -3, -2, -1, 0, 1, 2, 3, 4));
	}

	@Test
	public void sequence() {
		assertThat(empty.sequence(), is(emptyIterable()));
		assertThat(set.sequence(), containsInts(-5, -4, -3, -2, -1, 0, 1, 2, 3, 4));
	}

	@Test
	public void firstInt() {
		expecting(NoSuchElementException.class, empty::firstInt);
		assertThat(set.firstInt(), is(-5));
	}

	@Test
	public void lastInt() {
		expecting(NoSuchElementException.class, empty::lastInt);
		assertThat(set.lastInt(), is(4));
	}

	@Test
	public void iteratorRemoveAll() {
		IntIterator iterator = set.iterator();
		int value = -5;
		while (iterator.hasNext()) {
			assertThat(iterator.nextInt(), is(value));
			iterator.remove();
			value++;
		}
		assertThat(value, is(5));
		assertThat(set, is(emptyIterable()));
	}

	@Test
	public void removeAllIntArray() {
		assertThat(empty.removeAllInts(1, 2, 3), is(false));
		assertThat(empty, is(emptyIterable()));

		assertThat(set.removeAllInts(1, 2, 3), is(true));
		assertThat(set, containsInts(-5, -4, -3, -2, -1, 0, 4));
	}

	@Test
	public void removeAllIntCollection() {
		assertThat(empty.removeAll(org.d2ab.collection.ints.IntList.of(1, 2, 3)), is(false));
		assertThat(empty, is(emptyIterable()));

		assertThat(set.removeAll(org.d2ab.collection.ints.IntList.of(1, 2, 3)), is(true));
		assertThat(set, containsInts(-5, -4, -3, -2, -1, 0, 4));
	}

	@Test
	public void retainAllIntArray() {
		assertThat(empty.retainAllInts(1, 2, 3), is(false));
		assertThat(empty, is(emptyIterable()));

		assertThat(set.retainAllInts(1, 2, 3), is(true));
		assertThat(set, containsInts(1, 2, 3));
	}

	@Test
	public void retainAllIntCollection() {
		assertThat(empty.retainAll(org.d2ab.collection.ints.IntList.of(1, 2, 3)), is(false));
		assertThat(empty, is(emptyIterable()));

		assertThat(set.retainAll(org.d2ab.collection.ints.IntList.of(1, 2, 3)), is(true));
		assertThat(set, containsInts(1, 2, 3));
	}

	@Test
	public void removeIntsIf() {
		assertThat(empty.removeIntsIf(x -> x > 3), is(false));
		assertThat(empty, is(emptyIterable()));

		assertThat(set.removeIntsIf(x -> x > 3), is(true));
		assertThat(set, containsInts(-5, -4, -3, -2, -1, 0, 1, 2, 3));
	}

	@Test
	public void containsAllIntArray() {
		assertThat(empty.containsAllInts(1, 2, 3), is(false));
		assertThat(set.containsAllInts(1, 2, 3), is(true));
		assertThat(set.containsAllInts(1, 2, 3, 17), is(false));
	}

	@Test
	public void containsAllIntCollection() {
		assertThat(empty.containsAll(org.d2ab.collection.ints.IntList.of(1, 2, 3)), is(false));
		assertThat(set.containsAll(org.d2ab.collection.ints.IntList.of(1, 2, 3)), is(true));
		assertThat(set.containsAll(IntList.of(1, 2, 3, 17)), is(false));
	}

	@Test
	public void forEachInt() {
		empty.forEachInt(x -> {
			throw new IllegalStateException("should not get called");
		});

		AtomicInteger value = new AtomicInteger(-5);
		set.forEachInt(x -> assertThat(x, is(value.getAndIncrement())));
		assertThat(value.get(), is(5));
	}

	@Test
	public void addBoxed() {
		empty.add(17);
		assertThat(empty, containsInts(17));

		set.add(17);
		assertThat(set, containsInts(-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 17));
	}

	@Test
	public void containsBoxed() {
		assertThat(empty.contains(17), is(false));

		assertThat(set.contains(17), is(false));
		assertThat(set.contains(new Object()), is(false));
		for (int x = -5; x <= 4; x++)
			assertThat(set.contains(x), is(true));
	}

	@Test
	public void removeBoxed() {
		assertThat(empty.remove(17), is(false));

		assertThat(set.remove(17), is(false));
		assertThat(set.remove(new Object()), is(false));
		for (int x = -5; x <= 4; x++)
			assertThat(set.remove(x), is(true));
		assertThat(set.isEmpty(), is(true));
	}

	@Test
	public void addAllBoxed() {
		assertThat(empty.addAll(Arrays.asList(1, 2, 3)), is(true));
		assertThat(empty, containsInts(1, 2, 3));

		assertThat(set.addAll(Arrays.asList(3, 4, 5, 6, 7)), is(true));
		assertThat(set, containsInts(-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7));
	}

	@Test
	public void firstBoxed() {
		expecting(NoSuchElementException.class, empty::first);
		assertThat(set.first(), is(-5));
	}

	@Test
	public void lastBoxed() {
		expecting(NoSuchElementException.class, empty::last);
		assertThat(set.last(), is(4));
	}

	@Test
	public void removeAllBoxed() {
		assertThat(empty.removeAll(Arrays.asList(1, 2, 3)), is(false));
		assertThat(empty, is(emptyIterable()));

		assertThat(set.removeAll(Arrays.asList(1, 2, 3)), is(true));
		assertThat(set, containsInts(-5, -4, -3, -2, -1, 0, 4));
	}

	@Test
	public void retainAllBoxed() {
		assertThat(empty.retainAll(Arrays.asList(1, 2, 3)), is(false));
		assertThat(empty, is(emptyIterable()));

		assertThat(set.retainAll(Arrays.asList(1, 2, 3)), is(true));
		assertThat(set, containsInts(1, 2, 3));
	}

	@Test
	public void removeIfBoxed() {
		assertThat(empty.removeIf(x -> x > 3), is(false));
		assertThat(empty, is(emptyIterable()));

		assertThat(set.removeIf(x -> x > 3), is(true));
		assertThat(set, containsInts(-5, -4, -3, -2, -1, 0, 1, 2, 3));
	}

	@Test
	public void containsIntCollection() {
		assertThat(empty.containsAll(Arrays.asList(1, 2, 3)), is(false));
		assertThat(set.containsAll(Arrays.asList(1, 2, 3)), is(true));
		assertThat(set.containsAll(Arrays.asList(1, 2, 3, 17)), is(false));
	}

	@Test
	public void forEachBoxed() {
		empty.forEach(x -> {
			throw new IllegalStateException("should not get called");
		});

		AtomicInteger value = new AtomicInteger(-5);
		set.forEach(x -> assertThat(x, is(value.getAndIncrement())));
		assertThat(value.get(), is(5));
	}

	@Test
	public void boundaries() {
		BitIntSet intSet = new BitIntSet();
		assertThat(intSet.addInt(Integer.MIN_VALUE), is(true));
		assertThat(intSet.addInt(0), is(true));
		assertThat(intSet.addInt(Integer.MAX_VALUE), is(true));

		assertThat(intSet, containsInts(Integer.MIN_VALUE, 0, Integer.MAX_VALUE));

		assertThat(intSet.containsInt(Integer.MIN_VALUE), is(true));
		assertThat(intSet.containsInt(0), is(true));
		assertThat(intSet.containsInt(Integer.MAX_VALUE), is(true));

		assertThat(intSet.removeInt(Integer.MIN_VALUE), is(true));
		assertThat(intSet.removeInt(0), is(true));
		assertThat(intSet.removeInt(Integer.MAX_VALUE), is(true));

		assertThat(intSet, is(emptyIterable()));
	}

	@Test
	public void fuzz() {
		int[] randomValues = new int[10000];
		Random random = new Random();
		for (int i = 0; i < randomValues.length; i++) {
			int randomValue;
			do
				randomValue = random.nextInt();
			while (Arrayz.contains(randomValues, randomValue));
			randomValues[i] = randomValue;
		}

		// Adding
		for (int randomValue : randomValues)
			assertThat(empty.addInt(randomValue), is(true));
		assertThat(empty.size(), is(randomValues.length));

		for (int randomValue : randomValues)
			assertThat(empty.addInt(randomValue), is(false));
		assertThat(empty.size(), is(randomValues.length));

		// Containment checks
		assertThat(empty.containsAllInts(randomValues), is(true));

		for (int randomValue : randomValues)
			assertThat(empty.containsInt(randomValue), is(true));

		// toString
		Arrays.sort(randomValues);
		StringBuilder expectedToString = new StringBuilder("[");
		for (int i = 0; i < randomValues.length; i++)
			expectedToString.append(i > 0 ? ", " : "").append(randomValues[i]);
		expectedToString.append("]");
		assertThat(empty.toString(), is(expectedToString.toString()));

		// Removing
		for (int randomValue : randomValues)
			assertThat(empty.removeInt(randomValue), is(true));
		assertThat(empty.toString(), is("[]"));
		assertThat(empty.size(), is(0));

		for (int randomValue : randomValues)
			assertThat(empty.removeInt(randomValue), is(false));
	}
}