<?xml version="1.0"?>
<xsl:stylesheet xmlns:jmx="antlib:org.apache.catalina.ant.jmx"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
	xmlns:java="http://xml.apache.org/xslt/java"
	xmlns:redirect="org.apache.xalan.xslt.extensions.Redirect"
	extension-element-prefixes="redirect" exclude-result-prefixes="java">
	<xsl:output indent="yes" standalone="yes" version="1.0" />

	<!-- author : Didier Donsez, 2006-2008 -->

	<!-- Parameter declaration -->
	<xsl:param name="date" />
	<xsl:param name="author" />

	<xsl:variable name="jmxObjectName" />

	<!-- Templates -->
	<xsl:template match="agents">

		<project
      name="jmxml"
			default="main" basedir=".">
			<description>JMXML for {@id} (generated by {$author} at {$date})</description>

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
			
			
      <macrodef name="dumpProperties">
				<attribute name="id" />
				<sequential>
          <!-- TODO -->
          <script language="javascript"> <![CDATA[

            for (i=1; i<=10; i++) {
              echo = jmxml.createTask("echo");
              echo.setMessage(@{id});
              echo.perform();
            }

          ]]> </script>
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
					<xsl:apply-templates select="agent" mode="calltarget" />
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

			<xsl:apply-templates select="agent" mode="target" />

			<target name="clean"
				description="clean generated files and backup files">
				<delete dir="{@id}" />
			</target>

		</project>
	</xsl:template>

	<xsl:template match="agent" mode="calltarget">
				<antcall target="exec.{@id}" />
	</xsl:template>

	<xsl:template match="agent" mode="target">

    <xsl:comment>Target for agent <xsl:value-of select="@id"/></xsl:comment>
		<target name="exec.{@id}">
		
				<openConnection ref="{@id}.jmx.connection.ref"
					serviceurl="${{{@id}.jmx.serviceurl}}"
					username="${{{@id}.jmx.username}}"
					password="${{{@id}.jmx.password}}" />
					
      <xsl:apply-templates select="parallel|sequential|mbean" mode="target">
        <xsl:with-param name="id" select="@id"  />
      </xsl:apply-templates>
					
			<!--		
      <closeConnection ref="{@id}.jmx.connection.ref"/>
      
      <dumpProperties id="{@id}"/>
      -->
		</target>

	</xsl:template>
	
	<xsl:template match="parallel" mode="target">
      <xsl:param name="id" />
			<parallel>
      <xsl:apply-templates select="parallel|sequential|mbean" mode="target">
        <xsl:with-param name="id" select="$id" />
      </xsl:apply-templates>
			</parallel>
	</xsl:template>
	
  <xsl:template match="sequential" mode="target">
      <xsl:param name="id" />
			<sequential>
      <xsl:apply-templates select="parallel|sequential|mbean" mode="target">
        <xsl:with-param name="id" select="$id" />
      </xsl:apply-templates>
			</sequential>
	</xsl:template>
	
	<xsl:template match="mbean" mode="target">
      <xsl:param name="id" />
      <xsl:apply-templates select="attribute|operation" mode="target">
        <xsl:with-param name="id" select="$id" />
      </xsl:apply-templates>
	</xsl:template>

<!--
	<xsl:template match="attributes/attribute" mode="target">
      <xsl:param name="id" />
			<jmx:set ref="{$id}.jmx.connection.ref" name="{../../@objectName}"
						attribute="{@name}" value="{text()}"
						type="{@type}" echo="true" />
	</xsl:template>
-->

	<xsl:template match="attribute[@resultProperty]" mode="target">
      <xsl:param name="id" />
          <jmx:get
            ref="{$id}.jmx.connection.ref" name="{../@objectName}"
						attribute="{@name}"
            resultproperty="{@resultProperty}"
            echo="true"
        />
	</xsl:template>

	<xsl:template match="attribute" mode="target">
      <xsl:param name="id" />
        <jmx:set ref="{$id}.jmx.connection.ref" name="{../@objectName}"
						attribute="{@name}"
						value="{text()}"
						type="{@type}"
						echo="true"
						/>
	</xsl:template>

	<xsl:template match="operation[@resultProperty]" mode="target">
      <xsl:param name="id" />
		<jmx:invoke ref="{$id}.jmx.connection.ref"
			operation="{@name}"
			name="{../@objectName}"
			resultproperty="{@resultProperty}">
			<xsl:apply-templates select="arg" />
		</jmx:invoke>
	</xsl:template>

	<xsl:template match="operation" mode="target">
      <xsl:param name="id" />
		<jmx:invoke ref="{$id}.jmx.connection.ref"
			operation="{@name}"
			name="{../@objectName}">
			<xsl:apply-templates select="arg"/>
		</jmx:invoke>
	</xsl:template>

	<xsl:template match="arg[@type]">
      <arg type="@type" value="{text()}"/>
	</xsl:template>

	<xsl:template match="arg">
      <arg value="{text()}"/>
	</xsl:template>

<!--
	<xsl:template match="*"></xsl:template>
-->
</xsl:stylesheet>
