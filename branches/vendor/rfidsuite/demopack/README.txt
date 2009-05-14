OW2 :: Aspire RFID :: RFID Suite :: Demo pack
This demonstration runs 1 EPCIS server and 5 edges scanning fictive RFID tags from fictive RFID readers. The Zip file must be inflated. It contains 2 files to start and stop the pre-ocnfigured demonstration (startDemo.bat and stopDemo.bat). 

The startDemo script launches:

epcis : 1 EPCIS with the GWT user console
ons   : 1 ONS based on web services

edge1 : 1 fictive reader + 1 fictive thermometer
edge2 : 1 fictive reader + 1 fictive thermometer
edge3 : 1 fictive reader + 1 fictive thermometer
edge4 : 1 bluetooth reader bridge + 1 fictive thermometer
edge5 : 1 http reader bridge + 1 fictive thermometer

tikitagclient : send tag events to the edge 5 using HTTP GET requests 
nokia6212c    : send JSR257 target events to the edge 5 using HTTP GET requests (if the 6212c emulator is installed).

Locations:
edge 1 is located at 45.193884,5.771036 (Grenoble)
edge 2 is located at 37.97887,23.71706 (Athens)
edge 3 is located at 50.845,4.349992 (Bruxels)
edge 3 is moved to 50.814257,4.411408 (Bruxels, Baulieu)
edge 4 is located at Valence (Pole Tracabilité)
edge 5 is located at Lille (INRIA)

Links:
* http://wiki.aspire.objectweb.org