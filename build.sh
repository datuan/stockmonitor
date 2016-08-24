#!/bin/bash

#define configurations
#sql server <host>:<port>
dbhost=localhost:3306
#database name, default=stock
dbname=stock
#username to access db
dbuser=root
#password
dbpass=tuandao
#path to tomcat webapp folder
tomcatPath=/Users/tuandao/tomcat/apache-tomcat-8.0.36/webapps
#path to web server, for JUnit test the rest api, by default http://localhost:8080/StockWebApp/
weburl=http://localhost:8080/StockWebApp/

#define file locations
webconfig=StockWebApp/src/main/webapp/WEB-INF/web.xml

if [ ! -f $webconfig ]; then
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
if [ ! -f $dconf ]; then
	echo "Cannot find daemon.conf, please check path name"
	exit 1
fi
echo "Change values in daemon.conf file"
sed -i -e "s,dbhost=.*,dbhost="$dbhost",g" $dconf
sed -i -e "s,dbname=.*,dbname="$dbname",g" $dconf
sed -i -e "s,dbuser=.*,dbuser="$dbuser",g" $dconf
sed -i -e "s,dbpassword=.*,dbpassword="$dbpass",g" $dconf


echo "*********************************"
echo "*********************************"
echo "Build Daemon Process..."
echo "*********************************"
echo "*********************************"


mvn install -Dmaven.test.skip=true -f StockDaemon/pom.xml
if [ $? -ne 0 ];then
	echo "Building daemon fails, exit"
	exit 1
fi


echo "*********************************"
echo "*********************************"
echo "Build WebApp ..."
echo "*********************************"
echo "*********************************"


mvn package -f StockWebApp/pom.xml
if [ $? -ne 0 ];then
	echo "Building web app fails, exit"
	exit 1
fi

echo "*********************************"
echo "*********************************"
echo "Copy web app to tomcat folder"
echo "*********************************"
echo "*********************************"


if [ ! -d $tomcatPath ];then
	echo "Cannot find tomcat folder, please manually deploy webapp"
	exit 1
fi
cp StockWebApp/target/StockWebApp.war $tomcatPath

wd=$(pwd)
test_conf=$wd/daemon.conf
echo "config file=$test_conf"
echo "Sleep for 10 seconds, waiting for web app to be deployed"
sleep 10
echo "Running test ... "
#wd=$(pwd)
#test_conf=$wd/daemon.conf
if [ ! -f $test_conf ];then
	echo "Cannot find $test_conf file for running the test. Exit"
	exit 1
fi
mvn test -f StockDaemon/pom.xml -DconfigFile=$test_conf
echo "Please open browser to test the GUI interface"
#want to run the daemon process?
#java -jar StockDaemon/target/stockdaemon-1.0-SNAPSHOT-jar-with-dependencies.jar daemon.conf
