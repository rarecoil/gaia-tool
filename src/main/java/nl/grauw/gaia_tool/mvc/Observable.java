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
	
	public class UpdateRunnable implements Runnable {
		
		Observer target;
		Observable source;
		Object arg;
		
		public UpdateRunnable(Observer target, Observable source, Object arg) {
			this.target = target;
			this.source = source;
			this.arg = arg;
		}
		
		@Override
		public void run() {
			target.update(source, arg);
		}
		
	}
	
	Vector<WeakReference<Observer>> observers = new Vector<WeakReference<Observer>>();
	
	public void addObserver(Observer o) {
		observers.add(new WeakReference<Observer>(o));
	}
	
	public void removeObserver(Observer o) {
		for (int i = 0, len = observers.size(); i < len; i++) {
			WeakReference<Observer> wro = observers.get(i);
			if (wro.get() == o) {
				observers.remove(i);
				wro.clear();
				break;
			}
		}
	}
	
	public void hasObserver(Observer o) {
		observers.contains(o);
	}
	
	public void notifyObservers(Object arg) {
		for (int i = 0, len = observers.size(); i < len; i++) {
			WeakReference<Observer> wro = observers.get(i);
			Observer o = wro.get();
			if (o != null) {
				if (o instanceof JComponent && !SwingUtilities.isEventDispatchThread()) {
					SwingUtilities.invokeLater(new UpdateRunnable(o, this, arg));
				} else {
					o.update(this, arg);
				}
			} else {
				observers.remove(i);
			}
		}
	}
	
	public void notifyObservers() {
		notifyObservers(null);
	}
	
}
