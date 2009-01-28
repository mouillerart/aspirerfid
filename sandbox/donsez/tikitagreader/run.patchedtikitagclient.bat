rem from http://code.google.com/p/tikitagdev/wiki/InstallationInstructions

java -classpath target\classes;lib\tikitag-client-1.0-SNAPSHOT-jar-with-dependencies.jar com.tikitag.client.TikitagClient if=http://acs.tikitag.com/tikitag-soap/correlation?wsdl endpoint=http://acs.tikitag.com/tikitag-soap/correlation
