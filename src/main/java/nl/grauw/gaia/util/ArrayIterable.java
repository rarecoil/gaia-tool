/*
 * Copyright 2010 Laurens Holst
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
package nl.grauw.gaia.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Read-only array Iterable.
 */
public class ArrayIterable<T> implements Iterable<T> {
	
	private T[] array;
	int start;
	int end;
	
	/**
	 * Constructs an array iterable.
	 * @param array The array to iterate over.
	 */
	public ArrayIterable(T[] array) {
		this(array, 0);
	}
	
	/**
	 * Constructs an array iterable.
	 * @param array The array to iterate over.
	 */
	public ArrayIterable(T[] array, int start) {
		this(array, start, Integer.MAX_VALUE);
	}
	
	/**
	 * Constructs an array iterable.
	 * @param array The array to iterate over.
	 * @param start Iteration start position (inclusive).
	 * @param end Iteration stop position (exclusive).
	 */
	public ArrayIterable(T[] array, int start, int end) {
		this.array = array;
		this.start = start;
		this.end = Math.min(array.length, end);
	}
	
	@Override
	public Iterator<T> iterator() {
		return new ArrayIterator();
	}
	
	/**
	 * Iterator for the array.
	 */
	private class ArrayIterator implements Iterator<T> {
		
		int position = start;
		
		@Override
		public boolean hasNext() {
			return position < end;
		}
		
		@Override
		public T next() {
			if (position >= end)
				throw new NoSuchElementException();
			return array[position++];
		}
		
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
}
