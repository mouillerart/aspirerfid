#!/bin/sh
cd ../anttasks ;
ant -f start.edge.build.xml -Dedge-properties=./../edge3/edge.properties ; 
ant -f start.edge.build.xml -Dedge-properties=./../edge2/edge.properties ; 
ant -f start.edge.build.xml -Dedge-properties=./../edge1/edge.properties ;
