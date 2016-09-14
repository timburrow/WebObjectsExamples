<%
    // Clear the JSP/Servlet session
    // because this is the first page of the application
    // We never want sessions to be continued past this point.
    HttpSession aSession = request.getSession(false);
    if (aSession != null) {
	try {
	    aSession.invalidate();
	} catch (IllegalStateException ise) {
	}
    }

    response.addHeader("Pragma", "No-cache");
    response.addHeader("Cache-Control", "no-cache");
    response.addHeader("Expires", "1");
%>
<%@ taglib uri="/WOtaglib" prefix="wo" %>
<HTML>
<wo:component className="webobjectsexamples.SchoolTools.Main">
</wo:component>
</HTML>
