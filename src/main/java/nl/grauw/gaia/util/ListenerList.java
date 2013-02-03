package nl.grauw.gaia.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A thread-safe iterable list of listeners.
 * 
 * Iterating is fast, and not affected by concurrent modifications.
 * Modifications are a bit slower, they are synchronized and do array copies.
 * Inspired by EventListenerList, but iterable and without the weird Class stuff.
 */
public class ListenerList<T> implements Iterable<T> {
	
	private static final Object[] NULL_ARRAY = new Object[0];
	private Object[] listeners = NULL_ARRAY;
	
	public synchronized void add(T listener) {
		if (listener == null)
			throw new IllegalArgumentException("Non-null argument expected.");
		if (listeners == NULL_ARRAY) {
			listeners = new Object[] { listener };
		} else {
			int i = listeners.length;
			Object[] temp = new Object[i + 1];
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
					Object[] temp = new Object[listeners.length - 1];
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
		Object[] listeners = this.listeners;
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
