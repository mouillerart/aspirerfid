<?xml version="1.0"?>
<xsl:stylesheet xmlns:jmx="antlib:org.apache.catalina.ant.jmx"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
	xmlns:java="http://xml.apache.org/xslt/java"
	xmlns:redirect="org.apache.xalan.xslt.extensions.Redirect"
	extension-element-prefixes="redirect" exclude-result-prefixes="java">
	<xsl:output indent="yes" standalone="yes" version="1.0" />

	<!-- author : Didier Donsez, 2006 -->

	<!-- Parameter declaration -->
	<xsl:param name="date" />
	<xsl:param name="author" />
	<xsl:param name="global.repository.url" />

	<xsl:variable name="jmxObjectName" />

	<!-- Templates -->
	<xsl:template match="architecture">

		<project
			name="global deployment for {@id} (generated by {$author} at {$date})"
			default="main" basedir=".">

			<xsl:comment>Properties Definitions</xsl:comment>
			<property file="{@id}.properties" />


			<xsl:comment>Macro Definitions</xsl:comment>

			<macrodef name="openConnection">
				<attribute name="ref" />
				<attribute name="serviceurl" />
				<attribute name="username" />
				<attribute name="password" />
				<sequential>
					<echo
						message="Connection @{{serviceurl}} on @{{ref}}" />
					<jmx:open ref="@{{ref}}" url="@{{serviceurl}}"
						username="@{{username}}" password="@{{password}}" />
				</sequential>
			</macrodef>

			<macrodef name="closeConnection">
				<attribute name="ref" />
				<sequential>
					<jmx:close ref="@{{ref}}" />
				</sequential>
			</macrodef>

			<macrodef name="stopGateway">
				<attribute name="ref" />
				<attribute name="bundleUrl" />
				<sequential>
					<echo message="Stop gateway on @{{ref}}" />
					<jmx:invoke ref="@{{ref}}" operation="stopBundle"
						name="org.osgi:name=Framework">
						<arg value="0" />
					</jmx:invoke>
				</sequential>
			</macrodef>

			<macrodef name="startBundle">
				<attribute name="ref" />
				<attribute name="bundleUrl" />
				<sequential>
					<echo message="Start @{{bundleUrl}} on @{{ref}}" />
					<jmx:invoke ref="@{{ref}}" operation="startBundle"
						name="org.osgi:name=Framework">
						<arg value="@{{bundleUrl}}" />
					</jmx:invoke>
				</sequential>
			</macrodef>

			<macrodef name="startBundleOBR">
				<attribute name="ref" />
				<attribute name="bundleName" />
				<attribute name="version" />
				<sequential>
					<echo message="Start @{{bundleName}} on @{{ref}}" />
					<jmx:invoke ref="@{{ref}}"
						operation="obrStartBundle" name="org.osgi:name=ObrMBean">
						<arg value="@{{bundleName}}" />
						<arg value="@{{version}}" />
					</jmx:invoke>
				</sequential>
			</macrodef>

			<macrodef name="configMBean">
				<attribute name="ref" />
				<attribute name="objectName" />
				<attribute name="attributeName" />
				<attribute name="attributeValue" />
				<attribute name="attributeType" />
				<sequential>
					<echo message="Start @{{bundleUrl}} on @{{ref}}" />
					<jmx:set ref="@{{ref}}" name="@{{objectName}}"
						attribute="@{{attributeName}}" value="@{{attributeValue}}"
						type="@{{attributeType}}" echo="true" />
				</sequential>
			</macrodef>

			<macrodef name="configOBR">
				<attribute name="ref" />
				<attribute name="url" />
				<sequential>
					<echo
						message="Config OBR with @{{url}} on @{{ref}}" />
					<!--jmx:invoke
						ref="@{{ref}}"
						operation="removeAllRepositories"
						name="org.osgi:name=ObrMBean" 
						/-->
					<jmx:invoke ref="@{{ref}}"
						operation="obrAddRepository" name="org.osgi:name=ObrMBean">
						<arg value="@{{url}}" />
					</jmx:invoke>
				</sequential>
			</macrodef>

			<xsl:comment>Targets</xsl:comment>

			<target name="main" depends="init">
				<echo>
					Note: copy your catalina-ant.jar and
					catalina-ant-jmx.jar from $CATALINA_HOME/server/lib
					to $ANT_HOME/lib
				</echo>
				<parallel>
					<!--<xsl:apply-templates select="node" mode="startserver"/>-->
					<xsl:apply-templates select="node"
						mode="calltarget" />
				</parallel>
			</target>

			<target name="init">
				<mkdir dir="{@id}" />
				<tstamp>
					<format property="date"
						pattern="d-MMMM-yyyy hh:mm aa" locale="en" />
				</tstamp>
				<property name="root.dir" value="root" />
				<property name="author" value="{$author}" />
			</target>

			<target name="stop" description="stop all nodes">
				<parallel>
					<xsl:apply-templates select="node"
						mode="calltargetstop" />
				</parallel>
			</target>

			<xsl:apply-templates select="node" mode="target" />

			<target name="clean"
				description="clean generated files and backup files">
				<delete dir="{@id}" />
			</target>

		</project>
	</xsl:template>

	<xsl:template match="node" mode="startserver">
		<xsl:if test="@type='server'">
			<sequential>
				<!--<antcall target="init.{@id}" />-->
				<antcall target="deploy.{@id}" />
			</sequential>
		</xsl:if>
	</xsl:template>

	<xsl:template match="node" mode="calltarget">
		<xsl:if test="@type!='server'">
			<sequential>
				<antcall target="init.{@id}" />
				<antcall target="deploy.{@id}" />
			</sequential>
		</xsl:if>

	</xsl:template>

	<xsl:template match="node" mode="calltargetstop">
		<antcall target="stop.{@id}" />
	</xsl:template>

	<xsl:template match="node" mode="target">
		<!-- <xsl:variable name="path" select="translate($package, '.', '/')"/>  -->

		<target name="init.{@id}">
			<mkdir dir="${{root.dir}}/{../@id}/{@id}" />
			<xsl:if test="@type='edge'">
				<mkdir dir="${{root.dir}}/{../@id}/{@id}/bundle" />
			</xsl:if>
			<xsl:if test="@type='premise'">
				<mkdir dir="${{root.dir}}/{../@id}/{@id}/bundle" />
			</xsl:if>
			<xsl:if test="@type='server'">
				<mkdir dir="${{root.dir}}/{../@id}/{@id}/ear" />

				<mkdir dir="generatedserver" />
				<copy todir="generatedserver"
					file="${{{@id}.rfidserver.root}}\configuration\a3servers.xml">
				</copy>
				<copy todir="generatedserver"
					file="${{{@id}.rfidserver.root}}\configuration\carol.properties">
				</copy>
				<copy todir="generatedserver"
					file="${{{@id}.rfidserver.root}}\configuration\jonas.properties">
				</copy>
				<copy todir="generatedserver"
					file="${{{@id}.rfidserver.root}}\configuration\joramAdmin.xml">
				</copy>
				<copy todir="generatedserver"
					file="${{{@id}.rfidserver.root}}\configuration\HSQL_rfid.properties">
				</copy>
				<replace dir="generatedserver" summary="yes">
					<include name="*.*" />
					<replacefilter token="__RFID_SERVER_IP__"
						value="${{{@id}.address}}" />
				</replace>
				<copy todir="${{{@id}.jonas.root}}/conf">
					<fileset dir="generatedserver">
						<include name="*.*" />
					</fileset>
				</copy>
				<delete dir="generatedserver" />
				<mkdir dir="generatedserver/META-INF" />
				<copy todir="generatedserver/META-INF"
					file="${{{@id}.rfidserver.root}}\configuration\ra.xml">
				</copy>
				<replace dir="generatedserver/META-INF" summary="yes">
					<include name="*.*" />
					<replacefilter token="__RFID_SERVER_IP__"
						value="${{{@id}.address}}" />
				</replace>
				<jar
					destfile="${{{@id}.jonas.root}}/rars/autoload/joram_for_jonas_ra.rar"
					basedir="generatedserver" update="yes" />
			</xsl:if>
			<mkdir dir="${{root.dir}}/{../@id}/{@id}/filter" />

			<xsl:if test="@subtype='nslu'">
				<!-- since NLU Java Runtime does not support http handlers -->
				<scp
					todir="${{{@id}.scp.username}}:${{{@id}.scp.password}}@${{{@id}.address}}:${{{@id}.scp.rootpath}}">
					<fileset dir="${{root.dir}}/{../@id}/{@id}/" />
				</scp>
			</xsl:if>
		</target>

		<!--target name="deploy.{@id}" depends="init.{@id}"-->
		<target name="deploy.{@id}">
			<xsl:if test="@type='server'">
				<exec executable="cmd">
					<arg value="/c" />
					<arg
						value="call ${{{@id}.jonas.root}}/bin/nt/jonas.bat start" />
				</exec>

				<xsl:apply-templates select="ear" mode="target" />
			</xsl:if>

			<xsl:if test="@type!='server'">

				<openConnection ref="{@id}.jmx.connection.ref"
					serviceurl="${{{@id}.jmx.serviceurl}}"
					username="${{{@id}.jmx.username}}"
					password="${{{@id}.jmx.password}}" />

				<configOBR ref="{@id}.jmx.connection.ref"
					url="${{{@id}.obr.url}}" />

				<xsl:comment>Start common bundles</xsl:comment>

				<startBundleOBR ref="{@id}.jmx.connection.ref"
					bundleName="Export Event JMS" version="0.1.0" />

				<startBundleOBR ref="{@id}.jmx.connection.ref"
					bundleName="Export Event WS" version="0.1.0" />

				<startBundleOBR ref="{@id}.jmx.connection.ref"
					bundleName="Export Event Dispatcher" version="0.1.0" />

				<startBundleOBR ref="{@id}.jmx.connection.ref"
					bundleName="ALE Implementation" version="0.1.0" />

				<xsl:if test="@type='edge'">
					<startBundleOBR ref="{@id}.jmx.connection.ref"
						bundleName="Fictive reader" version="0.1.0" />
				</xsl:if>

				<!-- xsl:apply-templates select="filter" mode="target" />
					<xsl:apply-templates select="reader" mode="target" />
					<xsl:apply-templates select="bridge" mode="target" />
					<xsl:apply-templates select="bundle" mode="target" />
					<xsl:apply-templates select="ear" mode="target" />
					
					<closeConnection ref="{@id}.jmx.connection.ref" /-->

			</xsl:if>

		</target>

		<target name="stop.{@id}">
			<xsl:if test="@type!='server'">
				<openConnection ref="{@id}.jmx.connection.ref"
					serviceurl="${{{@id}.jmx.serviceurl}}"
					username="${{{@id}.jmx.username}}"
					password="${{{@id}.jmx.password}}" />
			</xsl:if>

			<xsl:if test="@type='edge'">
				<stopGateway ref="{@id}.jmx.connection.ref" />
			</xsl:if>
			<xsl:if test="@type='server'">
				<exec executable="cmd">
					<arg value="/c" />
					<arg
						value="call ${{{@id}.jonas.root}}/bin/nt/jonas.bat stop" />
				</exec>
			</xsl:if>

		</target>
	</xsl:template>

	<xsl:template match="filter" mode="target">
		<echo message="Filter {@id} of node {../@id}" />
		<xsl:variable name="jmxObjectName">
			rfid:type=service,SymbolicName=FilterGenerator
		</xsl:variable>
		<configMBean ref="{../@id}.jmx.connection.ref"
			objectName="{$jmxObjectName}" attributeName="GatewayName"
			attributeValue="{../@id}" attributeType="java.lang.String" />
		<configMBean ref="{../@id}.jmx.connection.ref"
			objectName="{$jmxObjectName}" attributeName="SpecName"
			attributeValue="{@id}" attributeType="java.lang.String" />
		<xsl:apply-templates select="configuration" mode="target">
			<xsl:with-param name="jmxName" select="$jmxObjectName" />
		</xsl:apply-templates>
		<!--xsl:apply-templates select="configuration" mode="target"/-->
		<jmx:invoke ref="{../@id}.jmx.connection.ref"
			operation="addFilterComponent" name="{$jmxObjectName}">
		</jmx:invoke>
	</xsl:template>

	<xsl:template match="bridge" mode="target">
		<echo message="Bridge {@id} of node {../@id}" />
		<xsl:variable name="jmxObjectName">
			fr.imag.adele:name=ExportEvent
		</xsl:variable>
		<jmx:invoke ref="{../@id}.jmx.connection.ref" operation="start"
			name="{$jmxObjectName}">
			<arg
				value="org.objectweb.carol.jndi.spi.MultiOrbInitialContextFactory" />
			<arg value="${{{../@id}.{@id}.server}}" />
			<arg value="16400" />
			<arg value="JTCF" />
			<arg value="${{{../@id}.{@id}.providerurl}}" />
			<arg value="" />
			<arg value="" />
			<arg value="" />
			<arg value="" />
		</jmx:invoke>

		<!-- TODO -->
	</xsl:template>

	<xsl:template match="ear" mode="target">
		<echo message="Ear {@id} of node {../@id}" />
		<!-- TODO -->
	</xsl:template>

	<xsl:template match="reader" mode="target">
		<echo message="Reader {@id} of node {../@id}" />
		<xsl:variable name="jmxObjectName" />

		<xsl:choose>
			<xsl:when test='@type="tiris6350"'>
				<xsl:variable name="jmxObjectName">
					rfid:type=service,SymbolicName=tiris6350Driver
				</xsl:variable>
				<startBundle ref="{../@id}.jmx.connection.ref"
					bundleurl="${{{../@id}.rfid.url}}rfidbundles\rfid\rxtx.jar" />
				<startBundle ref="{../@id}.jmx.connection.ref"
					bundleurl="${{{../@id}.rfid.url}}rfidbundles\rfid\{@type}.jar" />
				<configMBean ref="{../@id}.jmx.connection.ref"
					objectName="{$jmxObjectName}" attributeName="LogicalName"
					attributeValue="{@id}" attributeType="java.lang.String" />
				<xsl:apply-templates select="configuration"
					mode="target">
					<xsl:with-param name="jmxName"
						select="$jmxObjectName" />
				</xsl:apply-templates>
				<jmx:invoke ref="{../@id}.jmx.connection.ref"
					operation="newReader" name="{$jmxObjectName}">
				</jmx:invoke>
			</xsl:when>
			<xsl:when test='@type="lx00media"'>
				<xsl:variable name="jmxObjectName">
					rfid:type=service,SymbolicName=lx00mediaDriver
				</xsl:variable>
				<startBundle ref="{../@id}.jmx.connection.ref"
					bundleurl="${{{../@id}.rfid.url}}rfidbundles\rfid\rxtx.jar" />
				<startBundle ref="{../@id}.jmx.connection.ref"
					bundleurl="${{{../@id}.rfid.url}}rfidbundles\rfid\{@type}.jar" />
				<configMBean ref="{../@id}.jmx.connection.ref"
					objectName="{$jmxObjectName}" attributeName="logicalName"
					attributeValue="{@id}" attributeType="java.lang.String" />
				<xsl:apply-templates select="configuration"
					mode="target">
					<xsl:with-param name="jmxName"
						select="$jmxObjectName" />
				</xsl:apply-templates>
				<jmx:invoke ref="{../@id}.jmx.connection.ref"
					operation="newReader" name="{$jmxObjectName}">
				</jmx:invoke>
			</xsl:when>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="bundle" mode="target">
		<echo message="Bundle {@id} of node {../@id}" />

		<xsl:choose>
			<xsl:when test="@name">
				<deployBundle ref="{../@id}.jmx.connection.ref"
					bundleName="{@name}" />
			</xsl:when>
			<xsl:when test="@url">
				<startBundle ref="{../@id}.jmx.connection.ref"
					bundleUrl="{@url}" />
			</xsl:when>
			<xsl:otherwise>
				<!-- stop the generation -->
				<!-- <xsl:fail>the bundle <xsl:value-of select="@id"/> of node <xsl:value-of select="../@id"/> had not an url nor a name !</xsl:fail> -->
				<fail>
					the bundle
					<xsl:value-of select="@id" />
					of node
					<xsl:value-of select="../@id" />
					had not an url nor a name !
				</fail>
			</xsl:otherwise>
		</xsl:choose>

		<xsl:apply-templates select="configuration" mode="target" />
	</xsl:template>

	<xsl:template match="configuration" mode="target">
		<echo message="Configure {../@id} of node {../../@id}" />

		<!-- TODO set a set of attributes in one target ! -->

		<xsl:apply-templates select="attribute" mode="target" />
		<xsl:apply-templates select="operation" mode="target" />

	</xsl:template>

	<xsl:template match="configuration" mode="target">
		<xsl:param name="jmxName" />
		<echo message="Configure {../@id} of node {../../@id}" />

		<xsl:apply-templates select="attribute" mode="target">
			<xsl:with-param name="jmxName" select="$jmxName" />
		</xsl:apply-templates>
		<xsl:apply-templates select="operation" mode="target">
			<xsl:with-param name="jmxName" select="$jmxName" />
		</xsl:apply-templates>
	</xsl:template>

	<!--
		<xsl:template match="configuration" mode="target">
		<echo message="Configure {../@id} of node {../../@id}"/>
		
		<echo message="TODO Replace properties in configuration files"/>
		
		</xsl:template>
	-->

	<xsl:template match="attribute" mode="target">
		<xsl:param name="jmxName" />
		<echo message="Configure attribute {@name} of {../../@id}" />

		<xsl:choose>
			<xsl:when test="../@objectName">
				<configMBean ref="{../../../@id}.jmx.connection.ref"
					objectName="{../@objectName}" attributeName="{@name}"
					attributeValue="{text()}" attributeType="{@type}" />
			</xsl:when>
			<xsl:otherwise>
				<configMBean ref="{../../../@id}.jmx.connection.ref"
					objectName="{$jmxName}" attributeName="{@name}"
					attributeValue="{text()}" attributeType="{@type}" />
			</xsl:otherwise>
		</xsl:choose>

	</xsl:template>
	<xsl:template match="operation" mode="target">
		<echo message="Configure operation {@name} of {../../@id}" />

		<jmx:invoke ref="jmx.connection.{../../../@id}"
			operation="{@name}" name="{../@objectName}">
			<xsl:apply-templates select="arg" />
		</jmx:invoke>
	</xsl:template>

	<xsl:template match="arg">
		<arg value="{@value}" />
	</xsl:template>

	<!-- with the new version of the jmx:set task -->
	<!--
		<xsl:template match="configuration" mode="target">
		<echo message="Configure {../@id} of node {../../@id}"/>
		
		<xsl:choose>
		<xsl:when test="@objectName">
		<jmx:set
		ref="jmx.connection.{../../@id}"
		objectName="{@objectName}"
		echo="true"
		>
		<xsl:apply-templates select="attribute" mode="target"/>
		</jmx:set>
		</xsl:when>
		<xsl:otherwise>
		<jmx:set
		ref="jmx.connection.{../../@id}"
		objectName="TOFIND"
		echo="true"
		>
		<xsl:apply-templates select="attribute" mode="target"/>
		</jmx:set>
		</xsl:otherwise>
		</xsl:choose>		
		</xsl:template>
		
		<xsl:template match="attribute" mode="target">
		<attribute
		name="{@name}"
		value="{text()}"
		type="{@type}"
		/>
		
		</xsl:template>
	-->

	<!-- Extra templates -->

	<xsl:template match="testVariablesAndJavaFunctions" mode="target">
		<xsl:variable name="classname"
			select="java:GenerationUtility.classOf(./class)" />
		<xsl:variable name="packagename"
			select="java:GenerationUtility.packageOf(./class)" />
		<xsl:variable name="clazz" select="$classname" />
		<xsl:variable name="path"
			select="translate($packagename, '.', '/')" />

		<echo>{$classname}</echo>
		<echo>{$packagename}</echo>
		<echo>{$clazz}</echo>
		<echo message="{$clazz}" />
	</xsl:template>

	<xsl:template name="path">
		<xsl:param name="path" />
		<xsl:if test="contains($path,'/')">
			<xsl:text>../</xsl:text>
			<xsl:call-template name="path">
				<xsl:with-param name="path">
					<xsl:value-of select="substring-after($path,'/')" />
				</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
		<xsl:if test="not(contains($path,'/')) and not($path = '')">
			<xsl:text>../</xsl:text>
		</xsl:if>
	</xsl:template>

	<!--
		Replace DOS characters in a path.
		Replace '.' with '/'
	-->
	<xsl:template name="packageToPath">
		<xsl:param name="path" />
		<xsl:value-of select="translate($path, '.', '/')" />
	</xsl:template>

	<xsl:template match="*"></xsl:template>

</xsl:stylesheet>