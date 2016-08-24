#!/bin/bash

#define configurations
#sql server <host>:<port>
dbhost=localhost:3306
#database name, default=stock
dbname=stock
#username to access db
dbuser=tuandao2
#password
dbpass=password2
#path to tomcat webapp folder
tomcatPath=/Users/tuandao/tomcat/apache-tomcat-8.0.36/webapps/

#define variables
webconfig=StockWebApp/src/main/webapp/WEB-INF/web.xml

if [ ! -f $ $webconfig ]; then
	echo "Cannot find StockWebApp/src/main/webapp/WEB-INF/web.xml, please check path name"
	exit 1
fi

echo "Change values in web.xml file"
#change sql server
sed -i -e "s,<\!-- dbhost -->.*,<\!-- dbhost --><env-entry-value>"$dbhost"<\/env-entry-value>,g"  $webconfig
sed -i -e "s,<\!-- dbname -->.*,<\!-- dbname --><env-entry-value>"$dbname"<\/env-entry-value>,g"  $webconfig
sed -i -e "s,<\!-- dbuser -->.*,<\!-- dbuser --><env-entry-value>"$dbuser"<\/env-entry-value>,g"  $webconfig
sed -i -e "s,<\!-- dbpassword -->.*,<\!-- dbpassword --><env-entry-value>"$dbpass"<\/env-entry-value>,g"  $webconfig

dconf=daemon.conf
if [ ! -f $ $dconf ]; then
	echo "Cannot find daemon.conf, please check path name"
	exit 1
fi
echo "Change values in daemon.conf file"
sed -i -e "s,dbhost=.*,dbhost="$dbhost",g" $dconf
sed -i -e "s,dbname=.*,dbhost="$dbname",g" $dconf
sed -i -e "s,dbuser=.*,dbhost="$dbuser",g" $dconf
sed -i -e "s,dbpassword=.*,dbpassword="$dbpass",g" $dconf

echo "Build Daemon Process..."
mvn install -Dmaven.test.skip=true -f StockDaemon/pom.xml
if [ $? -ne 0 ];then
	echo "Building daemon fails, exit"
	exit 1
fi
echo "Build WebApp ..."
mvn package -f StockWebApp/pom.xml
if [ $? -ne 0 ];then
	echo "Building web app fails, exit"
	exit 1
fi
echo "Copy web app to tomcat folder"
cp StockWebApp/target/StockWebApp.war $tomcatPath 