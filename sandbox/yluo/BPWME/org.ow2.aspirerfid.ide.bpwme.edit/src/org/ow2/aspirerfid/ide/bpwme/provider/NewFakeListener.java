package org.ow2.aspirerfid.ide.bpwme.provider;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.provider.INotifyChangedListener;

public class NewFakeListener implements INotifyChangedListener{

	private static NewFakeListener nfl;
	
	public static NewFakeListener getListener() {
		if(nfl == null) {
			nfl = new NewFakeListener();
		}
		return nfl;
	}
	
	@Override
	public void notifyChanged(Notification notification) {
		// TODO Auto-generated method stub
		System.out.println("New Fake:" + notification.getEventType() 
				+ "," + notification.getOldValue());
	}

}
