package org.pachube.service.impl;

import java.awt.Color;

import org.pachube.service.GenericPachube;

import Pachube.Feed;
import Pachube.Pachube;
import Pachube.PachubeException;
import Pachube.Trigger;
import Pachube.httpClient.HttpResponse;
import Pachube2.Pachube2;

public class GenericPachubeImpl implements GenericPachube {
	
	private Object pachube;
	
	public GenericPachubeImpl(Object pachube) {
		this.pachube = pachube;
	}
	/* (non-Javadoc)
	 * @see org.pachube.service.GenericPachube#getFeed(int)
	 */
	public Feed getFeed(int feed) throws PachubeException {
		if (pachube instanceof Pachube)
			return ((Pachube) pachube).getFeed(feed);
		else if (pachube instanceof Pachube2)
			return (((Pachube2) pachube).getFeed(feed));
		return null;
	}

	/* (non-Javadoc)
	 * @see org.pachube.service.GenericPachube#createFeed(Pachube.Feed)
	 */
	public Feed createFeed(Feed feed) throws PachubeException {
		if (pachube instanceof Pachube)
			return ((Pachube) pachube).createFeed(feed);
		else if (pachube instanceof Pachube2)
			return (((Pachube2) pachube).createFeed(feed));
		return null;
		
	}

	/* (non-Javadoc)
	 * @see org.pachube.service.GenericPachube#updateFeed(int, java.lang.String)
	 */
	public boolean updateFeed(int feed, String s) throws PachubeException {
		if (pachube instanceof Pachube)
			return ((Pachube) pachube).updateFeed(feed,s);
		else if (pachube instanceof Pachube2)
			return (((Pachube2) pachube).updateFeed(feed,s));
		return false;
	
	}

	/* (non-Javadoc)
	 * @see org.pachube.service.GenericPachube#deleteFeed(int)
	 */
	public HttpResponse deleteFeed(int feed) {
		if (pachube instanceof Pachube)
			return ((Pachube) pachube).deleteFeed(feed);
		else if (pachube instanceof Pachube2)
			return (((Pachube2) pachube).deleteFeed(feed));
		return null;
		
	}

	/* (non-Javadoc)
	 * @see org.pachube.service.GenericPachube#createDatastream(int, java.lang.String)
	 */
	public boolean createDatastream(int feed, String s) throws PachubeException {
		if (pachube instanceof Pachube)
			return ((Pachube) pachube).createDatastream(feed,s);
		else if (pachube instanceof Pachube2)
			return (((Pachube2) pachube).createDatastream(feed,s));
		return false;
	

	}

	/* (non-Javadoc)
	 * @see org.pachube.service.GenericPachube#deleteDatastream(int, int)
	 */
	public HttpResponse deleteDatastream(int feed, int datastream) {
		if (pachube instanceof Pachube)
			return ((Pachube) pachube).deleteDatastream(feed,datastream);
		else if (pachube instanceof Pachube2)
			return (((Pachube2) pachube).deleteDatastream(feed,datastream));
		return null;
	
	}

	/* (non-Javadoc)
	 * @see org.pachube.service.GenericPachube#updateDatastream(int, int, java.lang.String)
	 */
	public HttpResponse updateDatastream(int feed, int datastream, String s) {
		if (pachube instanceof Pachube)
			return ((Pachube) pachube).updateDatastream(feed,datastream,s);
		else if (pachube instanceof Pachube2)
			return (((Pachube2) pachube).updateDatastream(feed,datastream,s));
		return null;
	
	}

	/* (non-Javadoc)
	 * @see org.pachube.service.GenericPachube#getDatastream(int, int)
	 */
	public HttpResponse getDatastream(int feed, int datastream) {
		return null;
		
	}

	/* (non-Javadoc)
	 * @see org.pachube.service.GenericPachube#getDatastreamHistory(int, int)
	 */
	public Double[] getDatastreamHistory(int feed, int datastream) {
		if (pachube instanceof Pachube)
			return ((Pachube) pachube).getDatastreamHistory(feed,datastream);
		else if (pachube instanceof Pachube2)
			return (((Pachube2) pachube).getDatastreamHistory(feed,datastream));
		return null;
		
	}

	/* (non-Javadoc)
	 * @see org.pachube.service.GenericPachube#getDatastreamArchive(int, int)
	 */
	public String[] getDatastreamArchive(int feed, int datastream) {
		if (pachube instanceof Pachube)
			return ((Pachube) pachube).getDatastreamArchive(feed,datastream);
		else if (pachube instanceof Pachube2)
			return (((Pachube2) pachube).getDatastreamArchive(feed,datastream));
		return null;
		

	}

	/* (non-Javadoc)
	 * @see org.pachube.service.GenericPachube#createTrigger(Pachube.Trigger)
	 */
	public String createTrigger(Trigger t) throws PachubeException {
		if (pachube instanceof Pachube)
			return ((Pachube) pachube).createTrigger(t);
		else if (pachube instanceof Pachube2)
			return (((Pachube2) pachube).createTrigger(t));
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.pachube.service.GenericPachube#getTrigger(int)
	 */
	public Trigger getTrigger(int id) throws PachubeException {
		if (pachube instanceof Pachube)
			return ((Pachube) pachube).getTrigger(id);
		else if (pachube instanceof Pachube2)
			return (((Pachube2) pachube).getTrigger(id));
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.pachube.service.GenericPachube#getTriggers()
	 */
	public Trigger[] getTriggers() throws PachubeException {
		if (pachube instanceof Pachube)
			return ((Pachube) pachube).getTriggers();
		else if (pachube instanceof Pachube2)
			return (((Pachube2) pachube).getTriggers());
		return null;
		
	}
	
	/* (non-Javadoc)
	 * @see org.pachube.service.GenericPachube#deleteTrigger(int)
	 */
	public HttpResponse deleteTrigger(int id){
		if (pachube instanceof Pachube)
			return ((Pachube) pachube).deleteTrigger(id);
		else if (pachube instanceof Pachube2)
			return (((Pachube2) pachube).deleteTrigger(id));
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.pachube.service.GenericPachube#updateTrigger(int, Pachube.Trigger)
	 */
	public HttpResponse updateTrigger(int id,Trigger t){
		if (pachube instanceof Pachube)
			return ((Pachube) pachube).updateTrigger(id,t);
		else if (pachube instanceof Pachube2)
			return (((Pachube2) pachube).updateTrigger(id,t));
		return null;
		
	}

	/* (non-Javadoc)
	 * @see org.pachube.service.GenericPachube#showGraph(int, int, int, int, java.awt.Color)
	 */
	public String showGraph(int feedID, int streamID, int width, int height,
			Color c) {
		if (pachube instanceof Pachube)
			return ((Pachube) pachube).showGraph( feedID,  streamID,  width,  height,c);
		else if (pachube instanceof Pachube2)
			return ((Pachube2) pachube).showGraph( feedID,  streamID,  width,  height,c);
		return null;
	}
}