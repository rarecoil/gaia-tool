package nl.grauw.gaia_tool.mvc;

import static org.junit.Assert.*;

import java.util.Vector;

import javax.swing.JPanel;

import org.junit.Test;

public class ObservableTest {
	
	public class TestObserver implements Observer {
		public int updateCount;
		public Observable lastSource;
		public Object lastArgument;

		public void update(Observable source, Object arg) {
			updateCount++;
			lastSource = source;
			lastArgument = arg;
		}
	}
	
	public class RemoveSelfObserver extends TestObserver implements Observer {
		public void update(Observable source, Object arg) {
			super.update(source, arg);
			source.removeObserver(this);
		}
	}
	
	public class TestAWTObserver1 extends TestObserver implements AWTObserver {
		private static final long serialVersionUID = 1L;
	}
	
	public class TestAWTObserver2 extends JPanel implements AWTObserver {
		private static final long serialVersionUID = 1L;
		public void update(Observable source, Object arg) {}
	}
	
	public class TestNoAWTObserver extends JPanel implements Observer {
		private static final long serialVersionUID = 1L;
		public void update(Observable source, Object arg) {}
	}
	
	public class TestObservable extends Observable {
		public Vector<Observer> getObservers() {
			return super.getObservers();
		}
	}

	@Test
	public void testHasObserver() {
		Observable o = new Observable();
		TestObserver observer = new TestObserver();
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
		TestAWTObserver1 observer1 = new TestAWTObserver1();
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
	public void testGetObservers() {
		TestObservable o = new TestObservable();
		TestObserver observer = new TestObserver();
		o.addObserver(observer);
		assertEquals(1, o.getObservers().size());
		assertEquals(observer, o.getObservers().get(0));
	}

	@Test
	public void testGetObservers_Multiple() {
		TestObservable o = new TestObservable();
		TestObserver observer1 = new TestObserver();
		o.addObserver(observer1);
		TestObserver observer2 = new TestObserver();
		o.addObserver(observer2);
		TestObserver observer3 = new TestObserver();
		o.addObserver(observer3);
		TestObserver observer4 = new TestObserver();
		o.addObserver(observer4);
		o.removeObserver(observer2);
		assertEquals(3, o.getObservers().size());
		assertEquals(observer1, o.getObservers().get(0));
		assertEquals(observer3, o.getObservers().get(1));
		assertEquals(observer4, o.getObservers().get(2));
	}

	@Test
	public void testGetObservers_GarbageCollected() {
		TestObservable o = new TestObservable();
		TestObserver observer1 = new TestObserver();
		o.addObserver(observer1);
		TestObserver observer2 = new TestObserver();
		o.addObserver(observer2);
		TestObserver observer3 = new TestObserver();
		o.addObserver(observer3);
		observer2 = null;
		System.gc();
		assertEquals(2, o.getObservers().size());
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
