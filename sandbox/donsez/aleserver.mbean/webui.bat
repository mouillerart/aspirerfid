echo Simple Webapp
cmd /c start http://localhost:8080/aleserver.mbean


echo add Username is tomcat and password is s3cret in conf/tomcat-users.xml
echo Username is tomcat and password is s3cret
cmd /c start http://tomcat:s3cretlocalhost:8080/manager/jmxproxy?qry=org.ow2.aspirerfid%3A*
cmd /c start http://tomcat:s3cretlocalhost:8080/manager/jmxproxy?qry=org.osgi%3A*

echo For more information http://tomcat.apache.org/tomcat-5.5-doc/manager-howto.html#Using%20the%20JMX%20Proxy%20Servlet
cmd /c start http://tomcat.apache.org/tomcat-5.5-doc/manager-howto.html#Using%20the%20JMX%20Proxy%20Servlet

start jconsole
