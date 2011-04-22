Camel Router Project
====================

To run this router either embed the jar inside Spring
or to run the route from within maven try

    mvn camel:run

For more help see the Apache Camel documentation

    http://camel.apache.org/


To deploy to ServiceMix, you need to have external client and "back-end" server. For this example, we'll use the socat
network utility.

To start "back-end" echo service
> socat PIPE TCP4-LISTEN:5001


To deploy in ServiceMix
> <ServiceMix Home>/bin/servicemix

Install bundle from SerivceMix Console
karaf@root> features:addUrl mvn:com.fusesource.examples/tcpipproxy/0.0.1-SNAPSHOT/xml/features
karaf@root> features:install tcpipproxy

This will install the 'camel-mina' feature and the projects osgi bundle (com.fusesource.examples/tcpipproxy)


To start test client - it will receive input from stdin, and you should see the input echoed back on stdout
> socat - tcp:localhost:5000

