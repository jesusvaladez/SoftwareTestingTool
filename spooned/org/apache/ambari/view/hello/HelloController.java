package org.apache.ambari.view.hello;
@org.springframework.stereotype.Controller
@org.springframework.web.bind.annotation.RequestMapping("/")
public class HelloController {
    @org.springframework.web.bind.annotation.RequestMapping(method = org.springframework.web.bind.annotation.RequestMethod.GET)
    public java.lang.String printHello(org.springframework.ui.ModelMap model, javax.servlet.http.HttpServletRequest request) {
        org.apache.ambari.view.ViewContext viewContext = ((org.apache.ambari.view.ViewContext) (request.getSession().getServletContext().getAttribute(org.apache.ambari.view.ViewContext.CONTEXT_ATTRIBUTE)));
        java.lang.String userName = viewContext.getUsername();
        model.addAttribute("greeting", ("Hello " + (userName == null ? "unknown user" : userName)) + "!");
        return "hello";
    }
}