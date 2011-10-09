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
package nl.grauw.gaia_tool.mvc;

import static org.junit.Assert.*;

import java.util.List;

import javax.swing.JPanel;

import org.junit.Test;

public class ObservableTest {
	
	public class TestObserver implements Observer {
		public int updateCount;
		public Observable lastSource;
		public Object lastArgument;

		public void update(Observable source, Object detail) {
			updateCount++;
			lastSource = source;
			lastArgument = detail;
		}
	}
	
	public class RemoveSelfObserver extends TestObserver implements Observer {
		public void update(Observable source, Object detail) {
			super.update(source, detail);
			source.removeObserver(this);
		}
	}
	
	public class TestAWTObserver implements AWTObserver {
		public int updateCount;
		public Observable lastSource;
		public Object lastArgument;

		public void update(Observable source, Object detail) {
			updateCount++;
			lastSource = source;
			lastArgument = detail;
		}
	}
	
	public class TestAWTObserver2 extends JPanel implements AWTObserver {
		private static final long serialVersionUID = 1L;
		public void update(Observable source, Object detail) {}
	}
	
	public class TestNoAWTObserver extends JPanel implements Observer {
		private static final long serialVersionUID = 1L;
		public void update(Observable source, Object detail) {}
	}
	
	public class TestObservable extends Observable {
		public List<AWTObserver> getAWTObservers() {
			return super.getAWTObservers();
		}
	}

	@Test
	public void testHasObserver() {
		Observable o = new Observable();
		TestObserver observer = new TestObserver();
		assertFalse(o.hasObserver(observer));
	}

	@Test
	public void testHasObserver_AWTObserver() {
		Observable o = new Observable();
		TestAWTObserver observer = new TestAWTObserver();
		assertFalse(o.hasObserver(observer));
	}

	@Test
	public void testAddObserver() {
		Observable o = new Observable();
		TestObserver observer = new TestObserver();
		o.addObserver(observer);
		assertTrue(o.hasObserver(observer));
	}

	@Test
	public void testAddObserver_AWTObserver() {
		Observable o = new Observable();
		TestAWTObserver observer1 = new TestAWTObserver();
		o.addObserver(observer1);
		TestAWTObserver2 observer2 = new TestAWTObserver2();
		o.addObserver(observer2);
		assertTrue(o.hasObserver(observer1));
		assertTrue(o.hasObserver(observer2));
	}

	@Test (expected = IllegalArgumentException.class)
	public void testAddObserver_JComponentWithNoAWTObserver() {
		Observable o = new Observable();
		TestNoAWTObserver observer = new TestNoAWTObserver();
		o.addObserver(observer);
	}

	@Test
	public void testRemoveObserver() {
		Observable o = new Observable();
		TestObserver observer = new TestObserver();
		o.addObserver(observer);
		o.removeObserver(observer);
		assertFalse(o.hasObserver(observer));
	}

	@Test
	public void testRemoveObserver_AWTObserver() {
		Observable o = new Observable();
		TestAWTObserver observer = new TestAWTObserver();
		o.addObserver(observer);
		o.removeObserver(observer);
		assertFalse(o.hasObserver(observer));
	}

	@Test
	public void testGetAWTObservers() {
		TestObservable o = new TestObservable();
		TestAWTObserver observer = new TestAWTObserver();
		o.addObserver(observer);
		assertEquals(1, o.getAWTObservers().size());
		assertEquals(observer, o.getAWTObservers().get(0));
	}

	@Test
	public void testGetAWTObservers_Multiple() {
		TestObservable o = new TestObservable();
		TestAWTObserver observer1 = new TestAWTObserver();
		o.addObserver(observer1);
		TestAWTObserver observer2 = new TestAWTObserver();
		o.addObserver(observer2);
		TestAWTObserver observer3 = new TestAWTObserver();
		o.addObserver(observer3);
		TestAWTObserver observer4 = new TestAWTObserver();
		o.addObserver(observer4);
		o.removeObserver(observer2);
		assertEquals(3, o.getAWTObservers().size());
		assertEquals(observer1, o.getAWTObservers().get(0));
		assertEquals(observer3, o.getAWTObservers().get(1));
		assertEquals(observer4, o.getAWTObservers().get(2));
	}

	@Test
	public void testGetAWTObservers_GarbageCollected() {
		TestObservable o = new TestObservable();
		TestAWTObserver observer1 = new TestAWTObserver();
		o.addObserver(observer1);
		TestAWTObserver observer2 = new TestAWTObserver();
		o.addObserver(observer2);
		TestAWTObserver observer3 = new TestAWTObserver();
		o.addObserver(observer3);
		observer2 = null;
		System.gc();
		assertEquals(2, o.getAWTObservers().size());
	}

	@Test
	public void testNotifyObservers() {
		Observable o = new Observable();
		TestObserver observer = new TestObserver();
		o.addObserver(observer);
		o.notifyObservers();
		assertEquals(1, observer.updateCount);
		assertEquals(null, observer.lastArgument);
	}

	@Test
	public void testNotifyObservers_Multiple() {
		Observable o = new Observable();
		TestObserver observer1 = new TestObserver();
		o.addObserver(observer1);
		TestObserver observer2 = new TestObserver();
		o.addObserver(observer2);
		TestObserver observer3 = new TestObserver();
		o.addObserver(observer3);
		TestObserver observer4 = new TestObserver();
		o.addObserver(observer4);
		o.removeObserver(observer2);
		o.notifyObservers();
		assertEquals(1, observer1.updateCount);
		assertEquals(0, observer2.updateCount);
		assertEquals(1, observer3.updateCount);
		assertEquals(1, observer4.updateCount);
	}

	@Test
	public void testNotifyObservers_RemovedInUpdate() {
		Observable o = new Observable();
		TestObserver observer1 = new TestObserver();
		o.addObserver(observer1);
		TestObserver observer2 = new RemoveSelfObserver();
		o.addObserver(observer2);
		TestObserver observer3 = new TestObserver();
		o.addObserver(observer3);
		o.notifyObservers();
		assertEquals(1, observer1.updateCount);
		assertEquals(1, observer2.updateCount);
		assertEquals(1, observer3.updateCount);
	}

	@Test
	public void testNotifyObserversObject() {
		Observable o = new Observable();
		TestObserver observer = new TestObserver();
		o.addObserver(observer);
		String arg = "test";
		o.notifyObservers(arg);
		assertEquals(arg, observer.lastArgument);
	}

}
