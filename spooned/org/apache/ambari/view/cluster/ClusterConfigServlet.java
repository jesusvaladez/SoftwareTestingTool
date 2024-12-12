package org.apache.ambari.view.cluster;
public class ClusterConfigServlet extends javax.servlet.http.HttpServlet {
    private org.apache.ambari.view.ViewContext viewContext;

    @java.lang.Override
    public void init(javax.servlet.ServletConfig config) throws javax.servlet.ServletException {
        super.init(config);
        javax.servlet.ServletContext context = config.getServletContext();
        viewContext = ((org.apache.ambari.view.ViewContext) (context.getAttribute(org.apache.ambari.view.ViewContext.CONTEXT_ATTRIBUTE)));
    }

    @java.lang.Override
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws java.io.IOException {
        response.setContentType("text/html");
        response.setStatus(javax.servlet.http.HttpServletResponse.SC_OK);
        java.io.PrintWriter writer = response.getWriter();
        java.util.Map<java.lang.String, java.lang.String> properties = viewContext.getProperties();
        writer.println("<h1>Cluster Configurations</h1>");
        writer.println("</br>hdfs_user       = " + properties.get("hdfs_user"));
        writer.println("</br>proxyuser_group = " + properties.get("proxyuser_group"));
    }
}