package org.ow2.aspirerfid.barcode.android;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import liglab.adele.aspire.common.tag.Tag;
import liglab.adele.aspire.reader.adaptor.ReaderAdaptor;

import com.google.zxing.Result;

import android.net.LocalServerSocket;
import android.net.LocalSocket;

public class BarcodeReaderBridge implements Runnable, ReaderAdaptor {
    /** Internal server */
    private LocalServerSocket m_server;

    /** Listen port */
    private String m_name = "barcode-cilia-bridge";

    /** Listening thread */
    private Thread m_thread;

    /** Thread control */
    private boolean m_end;

    /** Tag list */
    private List<Tag> m_data = new LinkedList<Tag>();

    private String readerId;

    /**
     * Starts the component. Starts the server and the listening thread
     *
     * @throws IOException
     *             The server couldn't be created
     */
    public void start() throws IOException {
        m_server = new LocalServerSocket(m_name);

        m_end = false;
        m_thread = new Thread(this);
        m_thread.start();
    }

    /**
     * Stops the component Stops the server
     */
    public void stop() {
        System.out.println("=========== BRIDGE STOPPING ... ================");
        m_end = true;

        if (m_server != null) {
            try {
                m_server.close();
            } catch (IOException e) {
                exception("Can't stop server", e);
            }
            m_server = null;
        }
    }
    
    public String getId() {
    	return readerId;
    }
    

    /**
     * Store received data, if valid
     *
     * @param receivedData
     *            Data received from BarcodeReader activity
     */
    private void addData(String receivedData) {
        if (receivedData == null)
            return;

        Result result = new Result(receivedData, null, null, null);

        if (result == null)
            return;

        Tag tag = new Tag(result.getText());// createTag(result);
        if (tag != null)
            m_data.add(tag);
    }

    /**
     * Create a Tag object from parsed result
     *
     * @param result
     *            Parsed result
     * @return an id if valid, else null
     */
    /*
    private Tag createTag(ParsedResult result) {
        String id = null;

        ParsedResultType type = result.getType();

        if (type == ParsedResultType.URI) {
            id = ((URIParsedResult) result).getURI();
        } else if (type == ParsedResultType.GEO) {
            id = ((GeoParsedResult) result).getGeoURI();
        } else if (type == ParsedResultType.TEL) {
            id = "tel:" + ((TelParsedResult) result).getNumber();
        } else
            id = result.getDisplayResult();

        if (id == null)
            return null;

        Tag tag = new Tag(id);
        tag.setTimeStamp(System.currentTimeMillis());

        return tag;
    }
    */

    /*
     * (non-Javadoc)
     *
     * @see liglab.adele.aspire.reader.adaptor.ReaderAdaptor#getTags()
     */
    public List<Tag> getTags() {
        List<Tag> copy = new LinkedList<Tag>();

        Iterator<Tag> iter = m_data.iterator();
        while (iter.hasNext())
            copy.add(iter.next());

        m_data.clear();
        return copy;
    }

    /**
     * Print an error message
     *
     * @param message
     *            Message to be printed
     * @param e
     *            Associated exception
     */
    private void exception(String message, Exception e) {
        System.err.println("============= ERROR: " + message + "=============");
        e.printStackTrace(System.err);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Runnable#run()
     */
    public void run() {
    	System.out.println("=========== BRIDGE SERVER STARTED ==================");
        while (!m_end) {
            try {
                System.out.println("=========== BRIDGE WAITING " + m_name
                        + " ==================");
                LocalSocket client = m_server.accept();
                DataInputStream in = new DataInputStream(client
                        .getInputStream());

                String data = null;
                while ((data = in.readUTF()) != null) {
                    addData(data);
                }
            } catch (EOFException e) {
                // Ignore end of communication
            } catch (Exception e) {
                exception("Client communication error", e);
                m_end = true;
                return;
            }
        }
        
        System.out.println("=========== BRIDGE SERVER STOPPED ==================");
    }
}
