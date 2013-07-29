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

import java.util.EventListener;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A thread-safe iterable list of listeners.
 * 
 * Iterating is fast, and not affected by concurrent modifications.
 * Modifications are a bit slower, they are synchronized and do array copies.
 * Inspired by EventListenerList, but iterable and without the weird Class stuff.
 */
public class ListenerList<T extends EventListener> implements Iterable<T> {
	
	private static final EventListener[] NULL_ARRAY = new EventListener[0];
	private EventListener[] listeners = NULL_ARRAY;
	
	public synchronized void add(T listener) {
		if (listener == null)
			throw new IllegalArgumentException("Non-null argument expected.");
		if (listeners == NULL_ARRAY) {
			listeners = new EventListener[] { listener };
		} else {
			int i = listeners.length;
			EventListener[] temp = new EventListener[i + 1];
			System.arraycopy(listeners, 0, temp, 0, i);
			temp[i] = listener;
			listeners = temp;
		}
	}
	
	public synchronized void remove(T listener) {
		for (int i = 0; i < listeners.length; i++) {
			if (listeners[i] == listener) {
				if (listeners.length == 1) {
					listeners = NULL_ARRAY;
				} else {
					EventListener[] temp = new EventListener[listeners.length - 1];
					if (i > 0)
						System.arraycopy(listeners, 0, temp, 0, i);
					if (i < temp.length)
						System.arraycopy(listeners, i + 1, temp, i, temp.length - i);
					listeners = temp;
				}
				return;
			}
		}
		throw new IllegalArgumentException("Listener not found.");
	}
	
	public boolean contains(T listener) {
		EventListener[] listeners = this.listeners;
		for (int i = 0; i < listeners.length; i++)
			if (listeners[i] == listener)
				return true;
		return false;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Iterator<T> iterator() {
		return new ListenerIterator<T>((T[]) listeners);
	}
	
	/**
	 * Iterator for the array.
	 */
	private static class ListenerIterator<T> implements Iterator<T> {
		
		T[] listeners;
		private int position = 0;
		
		public ListenerIterator(T[] listeners) {
			this.listeners = listeners;
		}
		
		@Override
		public boolean hasNext() {
			return position < listeners.length;
		}
		
		@Override
		public T next() {
			if (position >= listeners.length)
				throw new NoSuchElementException();
			return listeners[position++];
		}
		
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
}
