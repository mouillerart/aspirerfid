Convert a CSV file containing positions and sensors data into KML file in order to display the data in Google Earth.

Remark: the first line of the CSV file could (or not) contain the record field labels
Remark: some lines could be inconsistent (bad number format, bad latitude/longitude format, ...).

Usage:
Command line mode



Ant task mode

label="yes" when the first line contains field labels
renameLabelBy enables to rename/identify the field (especially latitude;longitude;altitude)

<csv2kml
	file="data.csv"
	tofile="data.kml"
	discardErroneousLines="yes"
	renameLabelBy="?;latitude;longitude;?;altitude;outdoorTemp;indoorTemp"
/>

<csv2kml
	file="data.csv"
	tofile="data.kml"
	label="yes"
	discardErroneousLines="yes"
/>






Links

* KML documentation
http://code.google.com/apis/kml/documentation/

* A Java library to handle KML data
http://code.google.com/p/gekmllib/