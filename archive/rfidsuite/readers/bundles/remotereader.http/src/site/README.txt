-----------------
remotereader.http
-----------------

Version 0.1 2009/01/20
Authors: Kiev Gama, Didier Donsez

Motivations
-----------
The remotereader.http enables to forge RFID events on receiving HTTP GET requests  containing lists of tags ids scanned by an external device (patched )


Deployment
----------

To deploy (install and run) the HTTP adapter

1) Install and start the following bundles (in this order)
* Apache Felix' javax.servlet
* Apache Felix' http service (jetty)
* OW2 Aspire' remotereader.http

Remark: the port listened by Jetty is not necessary 8080 : check the Felix config file.

2) Configure the wire betwwen the httpadapter and the aleconsumer
TBD

3) Add the following tasks in the config build file (config.build.xml) and run it with Ant

  <jmx:invoke
          ref="edge1.jmx.connection.ref"
          name="rfid:type=service,SymbolicName=ECSpecFactory"
          operation="addLogicalReaderName"
  >
      <arg value="http"/>
  </jmx:invoke>

And the nex

  <jmx:set
          ref="edge1.jmx.connection.ref"
          name="rfid:type=reader,SymbolicName=HTTP"
          echo="true"
          attribute="GpsCoordinates"
          type="java.lang.String"
          value="45.19728,5.788422"
  />

Client
------
The HTTP request uses the GET method
The parameters are:
* id: the scanned tag guid (cardinality=1..N)
* timestamp: the timestamp of the scanning (not the request long in milliseconds since 1970/1/1 (UTC) (cardinality=0..1)
* readerid: the reader guid (cardinality=0..1)

Remarks: the timestamp could be desynchronized from the host and the client (versus a universal clock (GPS timestamp, Frankfurt antenna DCF-77)

Response code status:
TBD (200,204,404, 500, ...)

For instance,
http://localhost:8080/HttpTagReader?id=35654321000000112345678B&timestamp=1232370019656&readerid=com.tikitag.reader.065441005C16102A

You can write you own client in Java (with java.net.URLConnection, javax.microedition.io.HttpConnection or Apache HttpClient)

For instance,


void sendTag(String urlPrefix,String tagId, String readerId) throws IOException {
  MessageFormat urlTemplate=null;
  if(readerId==null) {
    urlTemplate=new MessageFormat("{1}?id={2}&timestamp={3}");
  } else {
    urlTemplate=new MessageFormat("{1}?id={2}&timestamp={3}&readerid={4}");
  }

  Object[] args = {urlPrefix, tagId, new Long(System.currentTimeMillis()), readers};

  URL url = new URL(urlTemplate.format(args));
  URLConnection conn = url.openConnection();
  HttpURLConnection c = (HttpURLConnection) conn;
  System.out.println("Response Code" + c.getResponseCode());
}
see the org.ow2.aspirerfid.reader.remote.http.proxy.HttpAdapterProxy


You can stress the server/servlet with load injectors (eg Apache JMeter, OW2 CLIF, ...)

Test
----
Browse the following URL in your web browser

http://localhost:8080/HttpTagReader?id=35654321000000112345678B&timestamp=1232370019656&readerid=com.tikitag.reader.065441005C16102A

http://localhost:8080/HttpTagReader?id=35654321000000112345678B&timestamp=1232370034656&readerid=net.violet.mirror.0123456789

http://localhost:8080/HttpTagReader?id=35654321000000112345678B&timestamp=1232370034656

http://localhost:8080/HttpTagReader?id=35654321000000112345678B (use the timestamp of the host)

TODOLIST
--------

simple client proxy (with a async tagid buffer)

parameter id multiple

more parameters:
* location (JSR179) : eg. jsr179.location=45.193925,5.770875 xor jsr179.location=45.193925,5.770875,220.0
* location method  : eg. gps,cellid
* symboliclocation : most of the eg. FR/IMAG/BATC/ROOM211/ENTRANCEPORTAL, COM/CAROUF/GRENOBLE/POS/VEGETABLE
* survey answers : eg. survey.s566566.q3=fr, survey.s566566.q12=true, survey.s566566.q13=5
* tag.data.value : the tag data (in base64)
* tag.data.offset : the offset of the tag data 

More methods
POST, PUT

RESTful version of the service

Test it with HTTPS
