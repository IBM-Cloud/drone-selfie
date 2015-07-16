Bluemix Selfie Drone
================================================================================

The [drone-selfie](https://github.com/IBM-Bluemix/drone-selfie) project contains an application to take selfies via a [Parrot AR Drone 2.0](http://ardrone2.parrot.com/). Via navigation buttons in a web application the drone can be steered and a series of pictures can be taken. The pictures are stored for later review. Additionally faces on the pictures are recognized and portraits are cropped out which can be tweeted. Check out the screenshot of the [web application](https://raw.githubusercontent.com/IBM-Bluemix/drone-selfie/master/pictures/selfie-drone1.jpg).

![alt text](https://raw.githubusercontent.com/IBM-Bluemix/drone-selfie/master/pictures/selfie-drone2.jpg "Bluemix Selfie Drone")

The application has been implemented via [IBM Bluemix](https://bluemix.net/) and the [Internet of Things](https://console.ng.bluemix.net/catalog/internet-of-things/) service. The pictures are stored in a <[Cloudant NoSQL database](https://console.ng.bluemix.net/catalog/cloudant-nosql-db/). The [Alchemy Face Recognition API](http://www.alchemyapi.com/products/alchemyvision/face-detection) is used to find the faces.

Author: Niklas Heidloff [@nheidloff](http://twitter.com/nheidloff)

Note: This application requires certain hardware and software. Read the documentation of the [Parrot Drone Sample](https://github.com/IBM-Bluemix/parrot-sample), set up the local Node.js application and follow the steps to configure your device with the Internet of Things service.


Setup of the Application on Bluemix via CF Maven Plugin
================================================================================

In addition to the setup of the [drone controller](https://github.com/IBM-Bluemix/parrot-sample) and the IoT registration of your device you need additional credentials.

* [Alchemy API Key](http://www.alchemyapi.com/api/register.html)
* [Twitter application](https://apps.twitter.com/app/new): consumer key and secret and access token and secret

The easiest way to configure and deploy the application is to use Maven, the Cloud Foundry Maven plugin and Bower. Make sure the following tools are installed and on your path.

* [Java](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Maven](https://maven.apache.org/install.html)
* [Bower](http://bower.io/#install-bower)
* [Git](https://git-scm.com/downloads)
* [Cloud Foundry CLI](https://github.com/cloudfoundry/cli#downloads)

You also need to configure the Cloud Foundry Maven plugin and provide your Bluemix username and password. If you have no Bluemix account yet, [register](https://apps.admin.ibmcloud.com/manage/trial/bluemix.html) for a trial account. If you don't have a file Settings.xml in ~/.m2 copy the one from the project. In any case define your Bluemix credentials in this file. After this the following commands can be executed to build and deploy the application. Replace the credentials in the last command with your own credentials and configuration.


> git clone https://github.com/IBM-Bluemix/drone-selfie.git

> bower install

> mvn -P deploy -DAPPNAME=drone-selfie -DORG=niklas_heidloff@de.ibm.com -DSPACE=demo -DDS_IOT_APIKEY=a-faaaaa-mbnaaaaaa -DDS_IOT_APITOKEN=hjfwI25O3aaaaaaa -DDS_IOT_DEVICEID=nik -DDS_IOT_DEVICETYPE=drone -DDS_TW_ACCESS_TOKEN=3368883473-2EbSoTVzVDTWW6mEzZTZESUxaaaaaaaaaaaaaaa -DDS_TW_ACCESS_TOKEN_SECRET=YwmdtftrViHNbv8AFIIcas92Uaaaaaaaaaaaaaaaaaaaa -DDS_TW_CONSUMER_KEY=ecO6SqapEB9Jjloaaaaaaaaaa -DDS_TW_CONSUMER_SECRET=W7lIOxATyQjHqhPlcB9AFPLCEfm3Mgaaaaaaaaaaaaaaaaaaaa -DDS_ALCHEMY=266f81cad4ca273aaaaaaaaaaaaaaaaaaaaaaaaa

After this you can open the application via http://[APPNAME].mybluemix.net.


Manual Setup of the Application on Bluemix
================================================================================

Alternatively you can also set up the application without the Cloud Foundry Maven plugin.

* Create a new application and choose [Liberty for Java](https://console.ng.bluemix.net/catalog/liberty-for-java/) as runtime.
* Add the [Cloudant NoSQL DB](https://console.ng.bluemix.net/catalog/cloudant-nosql-db/) service.
* Add the [AlchemyAPI](https://console.ng.bluemix.net/catalog/alchemyapi/) service and register for an API key.
* You can, but don't have to, bind the [Internet of Things](https://console.ng.bluemix.net/catalog/internet-of-things/) service to your application which you set up earlier for the [drone controller](https://github.com/IBM-Bluemix/parrot-sample).

When running on a server the application picks up the configuration from (most of) these services automatically. However some manual steps need to be done. In order to pass additional configuration to the application running on Bluemix an [user provided service](http://docs.cloudfoundry.org/devguide/services/user-provided.html) is used. The name of this service needs to start with "config-drone". After you have created the user defined service you need to bind it with your Bluemix application.

> cf cups config-drone-selfie -p "DS_TW_CONSUMER_KEY, DS_TW_CONSUMER_SECRET, DS_TW_ACCESS_TOKEN, DS_TW_ACCESS_TOKEN_SECRET, DS_IOT_DEVICEID, DS_IOT_DEVICETYPE, DS_IOT_APIKEY, DS_IOT_APITOKEN"

In the last step you need to build and deploy the application.

> git clone https://github.com/IBM-Bluemix/drone-selfie.git

> bower install

> mvn install

> cf push drone-selfie -p target/drone-selfie.war

> cf set-env drone-selfie JBP_CONFIG_LIBERTY "app_archive: {features: [websocket-1.1, servlet-3.1]}"

> cf restage drone-selfie


Run the Application locally
================================================================================

Setup your Java IDE.

* Install [Install Eclipse IDE for Java Developers](http://www.eclipse.org/downloads/)
* Install [Java](https://developer.ibm.com/wasdev/downloads/liberty-profile-using-eclipse)
* Install [Liberty profile](https://developer.ibm.com/wasdev/downloads/liberty-profile-using-eclipse)

Get the code and the JavaScript dependencies.

> git clone https://github.com/IBM-Bluemix/drone-selfie.git

> bower install

After this import the project as Maven project in your IDE>

Configure the Liberty server (server.xml):

       <server description="new server">
           <featureManager>
               <feature>localConnector-1.0</feature>
               <feature>websocket-1.0</feature>
               <feature>servlet-3.1</feature>
           </featureManager>
           <httpEndpoint host="localhost" httpPort="9080" httpsPort="9443" id="defaultHttpEndpoint"/>
           <applicationMonitor updateTrigger="mbean"/>
           <webApplication id="drone-selfie" location="drone-selfie.war" name="drone-selfie"/>
       </server>

The application can be run locally so that it can be tested and debugged before changes are deployed to Bluemix. To run it locally the following environment variables need to be set.

* NA_LOCAL true
* NA_APPNAME drone-selfie

Cloudant (copy from Bluemix dashboard):

* DS_DB_HOST 'your cloudant host' e.g. '1234567890-bluemix.cloudant.com'
* DS_DB_PASSWORD 'your cloudant password' e.g. 'adfadsfa0b4d208e0e2452180e0db4132f3639bd8bbdae17355eaaaaaaaaaaaaaaa'
* DS_DB_USERNAME 'your cloudant username' e.g. 'adfadsfdf984-bluemix'

Twitter:

* DS_TW_CONSUMER_KEY 'your consumer key' e.g. 'ecO6SqapEB9Jjloaaaaaaaaaa'
* DS_TW_CONSUMER_SECRET 'your consumer secret' e.g. 'W7lIOxATyQjHqhPlcB9AFPLCEfm3Mgaaaaaaaaaaaaaaaaaaaa'
* DS_TW_ACCESS_TOKEN 'your acess token' e.g. '3368883473-2EbSoTVzVDTWW6mEzZTZESUxaaaaaaaaaaaaaaa'
* DS_TW_ACCESS_TOKEN_SECRET 'your access token secret' e.g. 'YwmdtftrViHNbv8AFIIcas92Uaaaaaaaaaaaaaaaaaaaa'

Internet of Things:

* DS_IOT_DEVICEID 'your device id' e.g. 'nik'
* DS_IOT_DEVICETYPE 'your device type' e.g. 'drone'
* DS_IOT_APIKEY 'your api key' e.g. 'a-faaaaa-mbnaaaaaa'
* DS_IOT_APITOKEN 'your api token' e.g. 'hjfwI25O3aaaaaaa'

Alchemy:

* DS_ALCHEMY 266f81cad4ca273aaaaaaaaaaaaaaaaaaaaaaaaa

After you've set these variables you can run the application locally via [localhost:9080](http://localhost:9080).

You can deploy your changes by building the war file, e.g. via export function in Eclipse and then invoking the following commands.

> cf api https://api.ng.bluemix.net

> cf login
 
> cf push drone-selfie -p drone-selfie.war

> cf set-env drone-selfie JBP_CONFIG_LIBERTY "app_archive: {features: [websocket-1.1, servlet-3.1]}"

> cf restage drone-selfie


Screenshot of the Application
================================================================================

![alt text](https://raw.githubusercontent.com/IBM-Bluemix/drone-selfie/master/pictures/selfie-drone1.jpg "Bluemix Selfie Drone")