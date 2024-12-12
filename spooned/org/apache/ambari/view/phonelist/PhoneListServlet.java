package org.apache.ambari.view.phonelist;
public class PhoneListServlet extends javax.servlet.http.HttpServlet {
    private org.apache.ambari.view.ViewContext viewContext;

    private org.apache.ambari.view.DataStore dataStore = null;

    @java.lang.Override
    public void init(javax.servlet.ServletConfig config) throws javax.servlet.ServletException {
        super.init(config);
        javax.servlet.ServletContext context = config.getServletContext();
        viewContext = ((org.apache.ambari.view.ViewContext) (context.getAttribute(org.apache.ambari.view.ViewContext.CONTEXT_ATTRIBUTE)));
        dataStore = (java.lang.Boolean.parseBoolean(viewContext.getProperties().get("data.store.enabled"))) ? viewContext.getDataStore() : null;
    }

    @java.lang.Override
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
        java.lang.String name = request.getParameter("name");
        java.lang.String surname = request.getParameter("surname");
        java.lang.String phone = request.getParameter("phone");
        try {
            if ((((name != null) && (name.length() > 0)) && (phone != null)) && (phone.length() > 0)) {
                if (request.getParameter("add") != null) {
                    addUser(name, surname, phone);
                } else if (request.getParameter("update") != null) {
                    updateUser(name, surname, phone);
                } else if (request.getParameter("delete") != null) {
                    removeUser(name, surname, phone);
                }
            }
            listAll(request, response);
        } catch (java.lang.Exception e) {
            throw new javax.servlet.ServletException(e);
        }
    }

    @java.lang.Override
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
        response.setContentType("text/html");
        response.setStatus(javax.servlet.http.HttpServletResponse.SC_OK);
        java.io.PrintWriter writer = response.getWriter();
        try {
            java.lang.String name = request.getParameter("name");
            org.apache.ambari.view.phonelist.PhoneUser phone = getUser(name);
            if (phone != null) {
                editNumber(writer, name, phone.getSurname(), phone.getPhone(), request);
            } else {
                listAll(request, response);
            }
        } catch (org.apache.ambari.view.PersistenceException e) {
            throw new javax.servlet.ServletException(e);
        }
    }

    private void enterNumber(java.io.PrintWriter writer, javax.servlet.http.HttpServletRequest request) {
        writer.println(("<form name=\"input\" action = \"" + request.getRequestURI()) + "\" method=\"POST\">");
        writer.println("<table>");
        writer.println("<tr>");
        writer.println("<td>Name:</td><td><input type=\"text\" name=\"name\"></td><br/>");
        writer.println("</tr>");
        writer.println("<tr>");
        writer.println("<td>Surname:</td><td><input type=\"text\" name=\"surname\"></td><br/>");
        writer.println("</tr>");
        writer.println("<tr>");
        writer.println("<td>Phone Number:</td><td><input type=\"text\" name=\"phone\"></td><br/><br/>");
        writer.println("</tr>");
        writer.println("</table>");
        writer.println("<input type=\"submit\" value=\"Add\" name=\"add\">");
        writer.println("</form>");
    }

    private void editNumber(java.io.PrintWriter writer, java.lang.String name, java.lang.String surname, java.lang.String phone, javax.servlet.http.HttpServletRequest request) {
        writer.println(("<form name=\"input\" action = \"" + request.getRequestURI()) + "\" method=\"POST\">");
        writer.println("<table>");
        writer.println("<tr>");
        writer.println(("<td>Name:</td><td><input type=\"text\" name=\"name\" value=\"" + name) + "\" readonly></td><br/>");
        writer.println("</tr>");
        writer.println("<tr>");
        writer.println(("<td>Surname:</td><td><input type=\"text\" name=\"Surname\" value=\"" + surname) + "\" readonly></td><br/>");
        writer.println("</tr>");
        writer.println("<tr>");
        writer.println(("<td>Phone Number:</td><td><input type=\"text\" name=\"phone\" value=\"" + phone) + "\"></td><br/><br/>");
        writer.println("</tr>");
        writer.println("</table>");
        writer.println("<input type=\"submit\" value=\"Update\" name=\"update\">");
        writer.println("<input type=\"submit\" value=\"Delete\" name=\"delete\">");
        writer.println("</form>");
    }

    private void listAll(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws java.io.IOException, org.apache.ambari.view.PersistenceException {
        java.io.PrintWriter writer = response.getWriter();
        writer.println(("<h1>Phone List :" + viewContext.getInstanceName()) + "</h1>");
        writer.println("<table border=\"1\" style=\"width:300px\">");
        writer.println("<tr>");
        writer.println("<td>Name</td>");
        writer.println("<td>Phone Number</td>");
        writer.println("</tr>");
        java.util.Collection<org.apache.ambari.view.phonelist.PhoneUser> phoneUsers = getAllUsers();
        for (org.apache.ambari.view.phonelist.PhoneUser phoneUser : phoneUsers) {
            java.lang.String name = phoneUser.getName();
            writer.println("<tr>");
            writer.println(((((("<td><A href=" + request.getRequestURI()) + "?name=") + name) + ">") + name) + "</A></td>");
            writer.println(("<td>" + phoneUser.getPhone()) + "</td>");
            writer.println("</tr>");
        }
        writer.println("</table><br/><hr/>");
        enterNumber(writer, request);
    }

    private boolean userExists(java.lang.String name) throws org.apache.ambari.view.PersistenceException {
        return ((dataStore != null) && (dataStore.find(org.apache.ambari.view.phonelist.PhoneUser.class, name) != null)) || (viewContext.getInstanceData(name) != null);
    }

    private void addUser(java.lang.String name, java.lang.String surname, java.lang.String phone) throws org.apache.ambari.view.PersistenceException {
        if (userExists(name)) {
            throw new java.lang.IllegalArgumentException(("A number for " + name) + " already exists.");
        }
        updateUser(name, surname, phone);
    }

    private void updateUser(java.lang.String name, java.lang.String surname, java.lang.String phone) throws org.apache.ambari.view.PersistenceException {
        if (dataStore != null) {
            dataStore.store(new org.apache.ambari.view.phonelist.PhoneUser(name, surname, phone));
        } else {
            viewContext.putInstanceData(name, (surname + ";") + phone);
        }
    }

    private void removeUser(java.lang.String name, java.lang.String surname, java.lang.String phone) throws org.apache.ambari.view.PersistenceException {
        if (dataStore != null) {
            dataStore.remove(new org.apache.ambari.view.phonelist.PhoneUser(name, surname, phone));
        } else {
            viewContext.removeInstanceData(name);
        }
    }

    private org.apache.ambari.view.phonelist.PhoneUser getUser(java.lang.String name) throws org.apache.ambari.view.PersistenceException {
        if ((name != null) && (name.length() > 0)) {
            if (dataStore != null) {
                return dataStore.find(org.apache.ambari.view.phonelist.PhoneUser.class, name);
            } else {
                java.lang.String[] userInfo = viewContext.getInstanceData(name).split(";");
                return new org.apache.ambari.view.phonelist.PhoneUser(name, userInfo[0], userInfo[1]);
            }
        }
        return null;
    }

    private java.util.Collection<org.apache.ambari.view.phonelist.PhoneUser> getAllUsers() throws org.apache.ambari.view.PersistenceException {
        if (dataStore != null) {
            return dataStore.findAll(org.apache.ambari.view.phonelist.PhoneUser.class, null);
        }
        java.util.Map<java.lang.String, java.lang.String> data = new java.util.LinkedHashMap<java.lang.String, java.lang.String>(viewContext.getInstanceData());
        java.util.Collection<org.apache.ambari.view.phonelist.PhoneUser> users = new java.util.HashSet<org.apache.ambari.view.phonelist.PhoneUser>();
        for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : data.entrySet()) {
            java.lang.String[] userInfo = entry.getValue().split(";");
            users.add(new org.apache.ambari.view.phonelist.PhoneUser(entry.getKey(), userInfo[0], userInfo[1]));
            entry.getKey();
        }
        return users;
    }
}