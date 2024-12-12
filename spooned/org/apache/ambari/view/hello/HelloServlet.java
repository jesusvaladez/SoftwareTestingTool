package org.apache.ambari.view.hello;
public class HelloServlet extends javax.servlet.http.HttpServlet {
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
        java.lang.String name = properties.get("name");
        if (name == null) {
            name = viewContext.getUsername();
            if ((name == null) || (name.length() == 0)) {
                name = "world";
            }
        }
        writer.println(("<h1>Hello " + name) + "!</h1>");
    }
}