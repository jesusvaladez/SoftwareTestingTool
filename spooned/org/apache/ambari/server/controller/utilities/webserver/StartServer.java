package org.apache.ambari.server.controller.utilities.webserver;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
public class StartServer {
    public static void main(java.lang.String[] args) throws java.io.IOException {
        java.util.Map<java.lang.String, java.lang.String> mapArgs = org.apache.ambari.server.controller.utilities.webserver.StartServer.parseArgs(args);
        java.lang.System.out.println("Starting Ambari API server using the following properties: " + mapArgs);
        java.lang.System.setProperty("ambariapi.dbfile", mapArgs.get("db"));
        com.sun.jersey.api.core.ResourceConfig config = new com.sun.jersey.api.core.PackagesResourceConfig("org.apache.ambari.server.api.services");
        java.lang.System.out.println(("Starting server: http://localhost:" + mapArgs.get("port")) + '/');
        com.sun.net.httpserver.HttpServer server = com.sun.jersey.api.container.httpserver.HttpServerFactory.create(("http://localhost:" + mapArgs.get("port")) + '/', config);
        server.start();
        java.lang.System.out.println(("SERVER RUNNING: http://localhost:" + mapArgs.get("port")) + '/');
        java.lang.System.out.println("Hit return to stop...");
        java.lang.System.in.read();
        java.lang.System.out.println("Stopping server");
        server.stop(0);
        java.lang.System.out.println("Server stopped");
    }

    private static java.util.Map<java.lang.String, java.lang.String> parseArgs(java.lang.String[] args) {
        java.util.Map<java.lang.String, java.lang.String> mapProps = new java.util.HashMap<>();
        mapProps.put("port", "9998");
        mapProps.put("db", "/var/db/hmc/data/data.db");
        for (int i = 0; i < args.length; i += 2) {
            java.lang.String arg = args[i];
            if (arg.equals("-p")) {
                mapProps.put("port", args[i + 1]);
            } else if (arg.equals("-d")) {
                mapProps.put("db", args[i + 1]);
            } else {
                org.apache.ambari.server.controller.utilities.webserver.StartServer.printUsage();
                throw new java.lang.RuntimeException("Unexpected argument, See usage message.");
            }
        }
        return mapProps;
    }

    public static void printUsage() {
        java.lang.System.err.println("Usage: java StartServer [-p portNum] [-d abs path to ambari db file]");
        java.lang.System.err.println("Default Values: portNum=9998, ambariDb=/var/db/hmc/data/data.db");
    }
}