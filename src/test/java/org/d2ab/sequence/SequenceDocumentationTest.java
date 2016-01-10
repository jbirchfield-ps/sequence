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
package org.d2ab.sequence;

import org.d2ab.collection.Maps;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Character.toUpperCase;
import static java.lang.Math.sqrt;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class SequenceDocumentationTest {
	@Test
	public void filterAndMap() {
		List<String> evens = Sequence.of(1, 2, 3, 4, 5, 6, 7, 8, 9).filter(x -> (x % 2) == 0)
		                             .map(Objects::toString)
		                             .toList();

		assertThat(evens, contains("2", "4", "6", "8"));
	}

	@Test
	public void toMapFromSeparateSequences() {
		Sequence<Integer> keys = Sequence.of(1, 2, 3);
		Sequence<String> values = Sequence.of("1", "2", "3");

		Sequence<Pair<Integer, String>> keyValueSequence = keys.interleave(values);
		Map<Integer, String> map = keyValueSequence.toMap();

		assertThat(map, is(equalTo(Maps.builder(1, "1").put(2, "2").put(3, "3").build())));
	}

	@Test
	public void toMapFromPairs() {
		Map<String, Integer> map = Maps.builder("1", 1).put("2", 2).put("3", 3).put("4", 4).build();

		Sequence<Pair<String, Integer>> sequence = Sequence.from(map).map(Pair::from)
		                                                   .filter(p -> p.test((s, i) -> i != 2))
		                                                   .map(p -> p.map((s, i) -> Pair.of(s + " x 2", i * 2)));

		assertThat(sequence.toMap(), is(equalTo(Maps.builder("1 x 2", 2).put("3 x 2", 6).put("4 x 2", 8).build())));
	}

	@Test
	public void reuseOfSequence() {
		Sequence<Integer> singulars = Sequence.ints().limit(10); // Digits 1..10

		// using sequence of ints 1..10 first time to get odd numbers between 1 and 10
		Sequence<Integer> odds = singulars.step(2);
		assertThat(odds, contains(1, 3, 5, 7, 9));

		// re-using the same sequence again to get squares of numbers between 4 and 9
		Sequence<Integer> squares = singulars.map(i -> i * i).skip(3).limit(5);
		assertThat(squares, contains(16, 25, 36, 49, 64));
	}

	@Test
	public void sequenceInForeach() {
		Sequence<Integer> sequence = Sequence.ints().limit(3);

		int x = 1;
		for (int i : sequence)
			assertThat(i, is(x++));
	}

	@SuppressWarnings("SpellCheckingInspection")
	@Test
	public void streamToSequenceAndBack() {
		Stream<String> abcd = Arrays.asList("a", "b", "c", "d").stream();
		Stream<String> abbccd = Sequence.from(abcd).pair().<String>flatten().stream();

		assertThat(abbccd.collect(Collectors.toList()), contains("a", "b", "b", "c", "c", "d"));
	}

	@Test
	public void fibonacci() {
		Sequence<Integer> fibonacci = Sequence.recurse(Pair.of(0, 1), pair -> pair.shiftLeft(pair.apply(Integer::sum)))
		                                      .map(Pair::getLeft)
		                                      .until(55);

		assertThat(fibonacci, contains(0, 1, 1, 2, 3, 5, 8, 13, 21, 34));
	}

	@Test
	public void factorial() {
		Sequence<Long> thirteen = Sequence.longs().limit(13);
		Long factorial = thirteen.reduce(1L, (r, i) -> r * i);

		assertThat(factorial, is(6227020800L));
	}

	@Test
	public void functionalInterface() {
		List list = Arrays.asList(1, 2, 3, 4, 5);

		// Sequence as @FunctionalInterface of list's Iterator
		Sequence<Integer> sequence = list::iterator;

		// Operate on sequence as any other sequence using default methods
		Sequence<String> transformed = sequence.map(Object::toString).limit(3);

		assertThat(transformed, contains("1", "2", "3"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void recurseThrowableCause() {
		Exception e = new IllegalStateException(new IllegalArgumentException(new NullPointerException()));

		Sequence<Throwable> sequence = Sequence.recurse(e, Throwable::getCause).until(null);

		assertThat(sequence,
		           contains(instanceOf(IllegalStateException.class), instanceOf(IllegalArgumentException.class),
		                    instanceOf(NullPointerException.class)));
	}

	@Test
	public void snakeCase() {
		Chars snakeCase = Chars.from("Hello Lexicon").map(c -> (c == ' ') ? '_' : c).map(Character::toLowerCase);

		assertThat(snakeCase.asString(), is("hello_lexicon"));
	}

	@Test
	public void capitalize() {
		Chars titleCase = Chars.from("hello_lexicon")
		                       .mapBack((p, c) -> (p == -1 || p == '_') ? toUpperCase(c) : c)
		                       .map(c -> (c == '_') ? ' ' : c);

		assertThat(titleCase.asString(), is("Hello Lexicon"));
	}

	@Test
	public void entrySequence() {
		Map<String, Integer> original = Maps.builder("1", 1).put("2", 2).put("3", 3).put("4", 4).build();

		EntrySequence<Integer, String> oddsInverted = EntrySequence.from(original)
		                                                           .filter((k, v) -> v % 2 != 0)
		                                                           .map((k, v) -> Pair.of(v, k));

		assertThat(oddsInverted.toMap(), is(equalTo(Maps.builder(1, "1").put(3, "3").build())));
	}

	@Test
	public void intsSequence() {
		Ints squares = Ints.positive().map(i -> i * i);

		assertThat(squares.skip(3).limit(5), contains(16, 25, 36, 49, 64));
	}

	@Test
	public void longsSequence() {
		Longs negativeOdds = Longs.negative().step(2);

		assertThat(negativeOdds.skip(3).limit(5), contains(-7L, -9L, -11L, -13L, -15L));
	}

	@Test
	public void doublesSequence() {
		Doubles squareRoots = Doubles.positive().limit(3).map(Math::sqrt);

		assertThat(squareRoots, contains(sqrt(1), sqrt(2), sqrt(3)));
	}
}
