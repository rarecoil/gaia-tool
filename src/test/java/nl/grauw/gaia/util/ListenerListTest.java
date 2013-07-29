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

import static org.junit.Assert.*;

import java.util.EventListener;
import java.util.Iterator;

import org.junit.Test;

public class ListenerListTest {

	private final EventListener A = new EventListener() {};
	private final EventListener B = new EventListener() {};
	private final EventListener C = new EventListener() {};
	
	@Test
	public void testConstructor() {
		ListenerList<EventListener> listeners = new ListenerList<EventListener>();
		assertEquals(false, listeners.iterator().hasNext());
	}

	@Test
	public void testAdd() {
		ListenerList<EventListener> listeners = new ListenerList<EventListener>();
		listeners.add(A);
		
		Iterator<EventListener> iterator = listeners.iterator();
		assertEquals(A, iterator.next());
		assertEquals(false, iterator.hasNext());
	}
	
	@Test
	public void testAdd_Multiple() {
		ListenerList<EventListener> listeners = new ListenerList<EventListener>();
		listeners.add(A);
		listeners.add(B);
		listeners.add(C);
		
		Iterator<EventListener> iterator = listeners.iterator();
		assertEquals(A, iterator.next());
		assertEquals(B, iterator.next());
		assertEquals(C, iterator.next());
		assertEquals(false, iterator.hasNext());
	}
	
	@Test
	public void testAdd_ConcurrentModification() {
		ListenerList<EventListener> listeners = new ListenerList<EventListener>();
		listeners.add(A);
		listeners.add(B);
		
		Iterator<EventListener> iterator = listeners.iterator();
		assertEquals(A, iterator.next());
		
		listeners.add(C);
		
		assertEquals(B, iterator.next());
		assertEquals(false, iterator.hasNext());
	}
	
	@Test
	public void testRemove() {
		ListenerList<EventListener> listeners = new ListenerList<EventListener>();
		listeners.add(A);
		listeners.add(B);
		listeners.add(C);
		listeners.remove(B);

		Iterator<EventListener> iterator = listeners.iterator();
		assertEquals(A, iterator.next());
		assertEquals(C, iterator.next());
		assertEquals(false, iterator.hasNext());
	}
	
	@Test
	public void testRemove_Multiple() {
		ListenerList<EventListener> listeners = new ListenerList<EventListener>();
		listeners.add(A);
		listeners.add(B);
		listeners.add(C);
		listeners.remove(A);
		listeners.remove(C);

		Iterator<EventListener> iterator = listeners.iterator();
		assertEquals(B, iterator.next());
		assertEquals(false, iterator.hasNext());
	}
	
	@Test
	public void testRemove_Only() {
		ListenerList<EventListener> listeners = new ListenerList<EventListener>();
		listeners.add(A);
		listeners.remove(A);
		
		assertEquals(false, listeners.iterator().hasNext());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testRemove_NonExistent() {
		ListenerList<EventListener> listeners = new ListenerList<EventListener>();
		listeners.add(A);
		listeners.remove(B);
	}
	
	@Test
	public void testContains() {
		ListenerList<EventListener> listeners = new ListenerList<EventListener>();
		listeners.add(A);
		listeners.add(B);
		listeners.add(C);
		
		assertEquals(true, listeners.contains(B));
	}
	
	@Test
	public void testContains_NotFound() {
		ListenerList<EventListener> listeners = new ListenerList<EventListener>();
		listeners.add(B);
		listeners.add(C);
		
		assertEquals(false, listeners.contains(A));
	}
	
}
