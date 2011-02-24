package org.pachube.service;

import java.awt.Color;

import Pachube.Feed;
import Pachube.Pachube;
import Pachube.PachubeException;
import Pachube.Trigger;
import Pachube.httpClient.HttpResponse;
import Pachube2.Pachube2;

public class GenericPachube {
	
	private Object pachube;
	
	public GenericPachube(Object pachube) {
		this.pachube = pachube;
	}
	/**
	 * Gets a Feed by Feed ID
	 * 
	 * @param feed
	 *            Id of the Pachube2 feed to retrieve
	 * @return Feed which corresponds to the id provided as the parameter
	 * @throws PachubeException
	 *             If something goes wrong.
	 */
	public Feed getFeed(int feed) throws PachubeException {
		if (pachube instanceof Pachube)
			return ((Pachube) pachube).getFeed(feed);
		else if (pachube instanceof Pachube2)
			return (((Pachube2) pachube).getFeed(feed));
		return null;
	}

	/**
	 * Creates a new feed from the feed provide. The feed provide should have no
	 * ID, and after this method is called is usless, to make chanegs to the new
	 * feed methods should be invoked on the return object.
	 * 
	 * @param f
	 *            Feed to create, This Feed Should have no ID field and atleast
	 *            should have its title field filled in. This feed is not 'live'
	 *            any attempt to change this object will be ignored.
	 * @return Representation of the feed from pachube, this is a 'live' Feed
	 *         and method can invoked which will change the state of the online
	 *         feed.
	 * @throws PachubeException
	 *             If something goes wrong.
	 */
	public Feed createFeed(Feed feed) throws PachubeException {
		if (pachube instanceof Pachube)
			return ((Pachube) pachube).createFeed(feed);
		else if (pachube instanceof Pachube2)
			return (((Pachube2) pachube).createFeed(feed));
		return null;
		
	}

	/**
	 * This Method is not intended to be used by Users, instead get the Feed
	 * object using getFeed() and update the Feed from there, All changes will
	 * be made to the online Feed.
	 * 
	 * @param feed
	 * @param s
	 * @return
	 * @throws PachubeException
	 */
	public boolean updateFeed(int feed, String s) throws PachubeException {
		if (pachube instanceof Pachube)
			return ((Pachube) pachube).updateFeed(feed,s);
		else if (pachube instanceof Pachube2)
			return (((Pachube2) pachube).updateFeed(feed,s));
		return false;
	
	}

	/**
	 * Delete a Feed specified by the feed id. If any Feed object exists that is
	 * a representation of the item to be deleted, they will no longer work and
	 * will throw errors if method are invoked on them.
	 * 
	 * @param feed
	 *            If of the feed to delete
	 * @return HttpResponse
	 */
	public HttpResponse deleteFeed(int feed) {
		if (pachube instanceof Pachube)
			return ((Pachube) pachube).deleteFeed(feed);
		else if (pachube instanceof Pachube2)
			return (((Pachube2) pachube).deleteFeed(feed));
		return null;
		
	}

	/**
	 * This Method is not intended to be used by Users, instead get the Feed
	 * object using getFeed() and create Datastreams from there, All changes
	 * will be made to the online Feed.
	 * 
	 * @param feed
	 * @param s
	 * @return
	 * @throws PachubeException
	 */
	public boolean createDatastream(int feed, String s) throws PachubeException {
		if (pachube instanceof Pachube)
			return ((Pachube) pachube).createDatastream(feed,s);
		else if (pachube instanceof Pachube2)
			return (((Pachube2) pachube).createDatastream(feed,s));
		return false;
	

	}

	/**
	 * This Method is not intended to be used by Users, instead get the Feed
	 * object using getFeed() and delete Datastreams from there, All changes
	 * will be made to the online Feed.
	 * 
	 * @param feed
	 * @param datastream
	 * @return
	 */
	public HttpResponse deleteDatastream(int feed, int datastream) {
		if (pachube instanceof Pachube)
			return ((Pachube) pachube).deleteDatastream(feed,datastream);
		else if (pachube instanceof Pachube2)
			return (((Pachube2) pachube).deleteDatastream(feed,datastream));
		return null;
	
	}

	/**
	 * This Method is not intended to be used by Users, instead get the Feed
	 * object using getFeed() and update Datastreams from there, All changes
	 * will be made to the online Feed.
	 * 
	 * @param feed
	 * @param datastream
	 * @param s
	 * @return
	 */
	public HttpResponse updateDatastream(int feed, int datastream, String s) {
		if (pachube instanceof Pachube)
			return ((Pachube) pachube).updateDatastream(feed,datastream,s);
		else if (pachube instanceof Pachube2)
			return (((Pachube2) pachube).updateDatastream(feed,datastream,s));
		return null;
	
	}

	/**
	 * This Method is not intended to be used by Users, instead get the Feed
	 * object using getFeed() and get Datastreams from there.
	 * 
	 * @param feed
	 * @param datastream
	 * @return
	 */
	public HttpResponse getDatastream(int feed, int datastream) {
		return null;
		
	}

	/**
	 * This Method is not intended to be used by Users, instead get the Feed
	 * object using getFeed() and access Datastream history from there.
	 * 
	 * @param feed
	 * @param datastream
	 * @return
	 */
	public Double[] getDatastreamHistory(int feed, int datastream) {
		if (pachube instanceof Pachube)
			return ((Pachube) pachube).getDatastreamHistory(feed,datastream);
		else if (pachube instanceof Pachube2)
			return (((Pachube2) pachube).getDatastreamHistory(feed,datastream));
		return null;
		
	}

	/**
	 * This Method is not intended to be used by Users, instead get the Feed
	 * object using getFeed() and access Datastream archive from there.
	 * 
	 * @param feed
	 * @param datastream
	 * @return
	 */
	public String[] getDatastreamArchive(int feed, int datastream) {
		if (pachube instanceof Pachube)
			return ((Pachube) pachube).getDatastreamArchive(feed,datastream);
		else if (pachube instanceof Pachube2)
			return (((Pachube2) pachube).getDatastreamArchive(feed,datastream));
		return null;
		

	}

	/**
	 * Creates a Trigger on pachube from the object provided.
	 * 
	 * @param t
	 * @return
	 * @throws PachubeException
	 */
	public String createTrigger(Trigger t) throws PachubeException {
		if (pachube instanceof Pachube)
			return ((Pachube) pachube).createTrigger(t);
		else if (pachube instanceof Pachube2)
			return (((Pachube2) pachube).createTrigger(t));
		return null;
	}
	
	/**
	 * Gets a Trigger from pachube specified by the parameter
	 * 
	 * @param id id of the Trigger to get
	 */
	public Trigger getTrigger(int id) throws PachubeException {
		if (pachube instanceof Pachube)
			return ((Pachube) pachube).getTrigger(id);
		else if (pachube instanceof Pachube2)
			return (((Pachube2) pachube).getTrigger(id));
		return null;
	}
	
	/**
	 * Gets all the Triggers owned by the authenticating user
	 * 
	 * @param id id of the Trigger to get
	 */
	public Trigger[] getTriggers() throws PachubeException {
		if (pachube instanceof Pachube)
			return ((Pachube) pachube).getTriggers();
		else if (pachube instanceof Pachube2)
			return (((Pachube2) pachube).getTriggers());
		return null;
		
	}
	
	/**
	 * Deletes a Trigger from pachube
	 * @param id id of the trigger to delete
	 * @return
	 */
	public HttpResponse deleteTrigger(int id){
		if (pachube instanceof Pachube)
			return ((Pachube) pachube).deleteTrigger(id);
		else if (pachube instanceof Pachube2)
			return (((Pachube2) pachube).deleteTrigger(id));
		return null;
	}
	
	/**
	 * Updates a Trigger on pachube
	 * @param id id of the triggerto update
	 * @param t Trigger object of the new trigger
	 * @return
	 */
	public HttpResponse updateTrigger(int id,Trigger t){
		if (pachube instanceof Pachube)
			return ((Pachube) pachube).updateTrigger(id,t);
		else if (pachube instanceof Pachube2)
			return (((Pachube2) pachube).updateTrigger(id,t));
		return null;
		
	}

	/**
	 * Gets a Pachube graph of the datastream
	 * 
	 * @param feedID
	 *            ID of feed the datastream belongs to.
	 * @param streamID
	 *            ID of the stream to graph
	 * @param width
	 *            Width of the image
	 * @param height
	 *            Height of the image
	 * @param c
	 *            Color of the line
	 * @return String which can be used to form a URL Object.
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