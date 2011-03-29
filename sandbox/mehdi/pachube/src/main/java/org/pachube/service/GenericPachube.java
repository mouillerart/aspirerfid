package org.pachube.service;

import java.awt.Color;

import Pachube.Feed;
import Pachube.PachubeException;
import Pachube.Trigger;
import Pachube.httpClient.HttpResponse;

public interface GenericPachube {

	/**
	 * Gets a Feed by Feed ID
	 * 
	 * @param feed
	 *            Id of the Pachube2 feed to retrieve
	 * @return Feed which corresponds to the id provided as the parameter
	 * @throws PachubeException
	 *             If something goes wrong.
	 */
	public abstract Feed getFeed(int feed) throws PachubeException;

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
	public abstract Feed createFeed(Feed feed) throws PachubeException;

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
	public abstract boolean updateFeed(int feed, String s)
			throws PachubeException;

	/**
	 * Delete a Feed specified by the feed id. If any Feed object exists that is
	 * a representation of the item to be deleted, they will no longer work and
	 * will throw errors if method are invoked on them.
	 * 
	 * @param feed
	 *            If of the feed to delete
	 * @return HttpResponse
	 */
	public abstract HttpResponse deleteFeed(int feed);

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
	public abstract boolean createDatastream(int feed, String s)
			throws PachubeException;

	/**
	 * This Method is not intended to be used by Users, instead get the Feed
	 * object using getFeed() and delete Datastreams from there, All changes
	 * will be made to the online Feed.
	 * 
	 * @param feed
	 * @param datastream
	 * @return
	 */
	public abstract HttpResponse deleteDatastream(int feed, int datastream);

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
	public abstract HttpResponse updateDatastream(int feed, int datastream,
			String s);

	/**
	 * This Method is not intended to be used by Users, instead get the Feed
	 * object using getFeed() and get Datastreams from there.
	 * 
	 * @param feed
	 * @param datastream
	 * @return
	 */
	public abstract HttpResponse getDatastream(int feed, int datastream);

	/**
	 * This Method is not intended to be used by Users, instead get the Feed
	 * object using getFeed() and access Datastream history from there.
	 * 
	 * @param feed
	 * @param datastream
	 * @return
	 */
	public abstract Double[] getDatastreamHistory(int feed, int datastream);

	/**
	 * This Method is not intended to be used by Users, instead get the Feed
	 * object using getFeed() and access Datastream archive from there.
	 * 
	 * @param feed
	 * @param datastream
	 * @return
	 */
	public abstract String[] getDatastreamArchive(int feed, int datastream);

	/**
	 * Creates a Trigger on pachube from the object provided.
	 * 
	 * @param t
	 * @return
	 * @throws PachubeException
	 */
	public abstract String createTrigger(Trigger t) throws PachubeException;

	/**
	 * Gets a Trigger from pachube specified by the parameter
	 * 
	 * @param id id of the Trigger to get
	 */
	public abstract Trigger getTrigger(int id) throws PachubeException;

	/**
	 * Gets all the Triggers owned by the authenticating user
	 * 
	 * @param id id of the Trigger to get
	 */
	public abstract Trigger[] getTriggers() throws PachubeException;

	/**
	 * Deletes a Trigger from pachube
	 * @param id id of the trigger to delete
	 * @return
	 */
	public abstract HttpResponse deleteTrigger(int id);

	/**
	 * Updates a Trigger on pachube
	 * @param id id of the triggerto update
	 * @param t Trigger object of the new trigger
	 * @return
	 */
	public abstract HttpResponse updateTrigger(int id, Trigger t);

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
	public abstract String showGraph(int feedID, int streamID, int width,
			int height, Color c);

}