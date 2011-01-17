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

import java.lang.ref.WeakReference;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 * Class that implements observer pattern using weak references.
 * This allows observing views to get garbage collected if they
 * are no longer referenced elsewhere.
 * 
 * Also as compared to java.util.Observable it does not require
 * any annoying calls to setChanged().
 */
public class Observable {
	
	private Vector<WeakReference<Observer>> observers = new Vector<WeakReference<Observer>>();
	private int lastGCLimit = 100;
	
	/**
	 * Add an observer to this object.
	 * If it needs to be notified on the AWT event thread, it should implement AWTObserver.
	 * @param observer The observer to add.
	 */
	public void addObserver(Observer observer) {
		if (observer instanceof JComponent && !(observer instanceof AWTObserver))
			throw new IllegalArgumentException("Swing components and all observers that directly interact with them must implement AWTObserver.");
		if (observers.size() > lastGCLimit) {
			System.gc();
			Vector<Observer> observers = getObservers();	// clean up the no longer available weak references
			lastGCLimit = observers.size() + 100;
			System.out.println("Observer limit exceeded. Size after manual garbage collection: " + observers.size());
		}
		observers.add(new WeakReference<Observer>(observer));
	}
	
	/**
	 * Remove an observer from this object.
	 * @param observer The observer to remove.
	 */
	public void removeObserver(Observer observer) {
		for (int i = 0, len = observers.size(); i < len; i++) {
			WeakReference<Observer> wro = observers.get(i);
			if (wro.get() == observer) {
				observers.remove(i);
				wro.clear();
				break;
			}
		}
	}
	
	/**
	 * Returns whether an observer was already added.
	 * @param observer The observer whose presence to test.
	 * @return True if the observer is already observing.
	 */
	public boolean hasObserver(Observer observer) {
		return getObservers().contains(observer);
	}
	
	/**
	 * Notify observers of a change in the observable state.
	 * Uses null for the detail argument.
	 */
	protected void notifyObservers() {
		notifyObservers(null);
	}
	
	/**
	 * Notify observers of a change in the observable state.
	 * @param detail Object providing details on the state change.
	 */
	protected void notifyObservers(Object detail) {
		Vector<Observer> observers = getObservers();
		boolean awtObservers = false;
		for (Observer o : observers) {
			if (o instanceof AWTObserver && !SwingUtilities.isEventDispatchThread()) {
				awtObservers = true;
			} else {
				o.update(this, detail);
			}
		}
		
		// also schedule notifications on the AWT event thread if necessary
		if (awtObservers) {
			SwingUtilities.invokeLater(createAWTNotifier(observers, detail));
		}
	}
	
	/**
	 * Create runnable that for notifying objects on the AWT event thread.
	 * @param observers The observers to notify.
	 * @param detail Object providing details on the state change.
	 * @return A runnable to invoke later.
	 */
	private Runnable createAWTNotifier(final Iterable<Observer> observers, final Object detail) {
		return new Runnable() {
			@Override
			public void run() {
				for (Observer o : observers) {
					if (o instanceof AWTObserver) {
						o.update(Observable.this, detail);
					}
				}
			}
		};
	}
	
	/**
	 * Returns a copy of the observers registered on this observable.
	 * Any weakly referenced observers that no longer exist will be cleaned up and not in the list.
	 * @return The observers registered on this object.
	 */
	protected Vector<Observer> getObservers() {
		Vector<Observer> observersCopy = new Vector<Observer>(observers.size());
		for (int i = 0, len = observers.size(); i < len; i++) {
			WeakReference<Observer> wro = observers.get(i);
			Observer observer = wro.get();
			if (observer != null) {
				observersCopy.add(observer);
			} else {
				observers.remove(i);
				len--;
				i--;
			}
		}
		return observersCopy;
	}
	
}
