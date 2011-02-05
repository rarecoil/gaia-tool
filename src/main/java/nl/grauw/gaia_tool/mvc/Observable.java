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
 * Class that implements the observer pattern.
 * It supports two types of observers: regular observers, and AWT observers.
 * 
 * The AWT observers are run on the AWT event thread, and use weak references which allows them to
 * get garbage collected if they are no longer referenced elsewhere, making un-registering unnecessary.
 * 
 * Also as compared to java.util.Observable it does not require any annoying calls to setChanged().
 */
public class Observable {
	
	private Vector<Observer> observers = new Vector<Observer>();
	private Vector<WeakReference<AWTObserver>> awtObservers = new Vector<WeakReference<AWTObserver>>();
	private int lastGCLimit = 100;
	
	/**
	 * Add an observer to this object.
	 * 
	 * @param observer The observer to add.
	 */
	public void addObserver(Observer observer) {
		if (observer instanceof JComponent)
			throw new IllegalArgumentException("Swing components and all observers " +
					"that directly interact with them must implement AWTObserver.");
		observers.add(observer);
	}
	
	/**
	 * Add an AWT observer to this object.
	 * 
	 * @param observer The observer to add.
	 */
	public void addObserver(AWTObserver observer) {
		if (awtObservers.size() > lastGCLimit) {
			System.gc();
			Vector<AWTObserver> observers = getAWTObservers();	// clean up the no longer available weak references
			lastGCLimit = observers.size() + 100;
			System.out.println("Observer limit exceeded. Size after manual garbage collection: " + observers.size());
		}
		awtObservers.add(new WeakReference<AWTObserver>(observer));
	}
	
	/**
	 * Remove an observer from this object.
	 * 
	 * @param observer The observer to remove.
	 */
	public void removeObserver(Observer observer) {
		for (int i = 0, len = observers.size(); i < len; i++) {
			if (observers.get(i) == observer) {
				observers.remove(i);
				return;
			}
		}
	}
	
	/**
	 * Remove an AWT observer from this object.
	 * 
	 * @param observer The observer to remove.
	 */
	public void removeObserver(AWTObserver observer) {
		for (int i = 0, len = awtObservers.size(); i < len; i++) {
			WeakReference<AWTObserver> wro = awtObservers.get(i);
			if (wro.get() == observer) {
				awtObservers.remove(i);
				wro.clear();
				return;
			}
		}
	}
	
	/**
	 * Returns whether an observer was already added.
	 * 
	 * @param observer The observer whose presence to test.
	 * @return True if the observer is already observing.
	 */
	public boolean hasObserver(Observer observer) {
		return observers.contains(observer);
	}

	/**
	 * Returns whether an AWT observer was already added.
	 * 
	 * @param observer The observer whose presence to test.
	 * @return True if the observer is already observing.
	 */
	public boolean hasObserver(AWTObserver observer) {
		return getAWTObservers().contains(observer);
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
		Vector<Observer> observers = new Vector<Observer>(this.observers);
		for (Observer o : observers) {
			o.update(this, detail);
		}
		
		// also schedule notifications on the AWT event thread if necessary
		if (!awtObservers.isEmpty()) {
			SwingUtilities.invokeLater(createAWTNotifier(getAWTObservers(), detail));
		}
	}
	
	/**
	 * Create runnable for notifying objects on the AWT event thread.
	 * 
	 * @param observers The observers to notify.
	 * @param detail Object providing details on the state change.
	 * @return A runnable to invoke later.
	 */
	private Runnable createAWTNotifier(final Iterable<AWTObserver> awtObservers, final Object detail) {
		return new Runnable() {
			@Override
			public void run() {
				for (AWTObserver o : awtObservers) {
					o.update(Observable.this, detail);
				}
			}
		};
	}
	
	/**
	 * Returns a copy of the observers registered on this observable.
	 * Any weakly referenced observers that no longer exist will be cleaned up and not in the list.
	 * 
	 * @return The observers registered on this object.
	 */
	protected Vector<AWTObserver> getAWTObservers() {
		Vector<AWTObserver> observersCopy = new Vector<AWTObserver>(awtObservers.size());
		for (int i = 0, len = awtObservers.size(); i < len; i++) {
			WeakReference<AWTObserver> wro = awtObservers.get(i);
			AWTObserver observer = wro.get();
			if (observer != null) {
				observersCopy.add(observer);
			} else {
				awtObservers.remove(i);
				len--;
				i--;
			}
		}
		return observersCopy;
	}
	
}
