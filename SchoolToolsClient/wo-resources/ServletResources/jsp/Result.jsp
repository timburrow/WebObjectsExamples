<%@ taglib uri="/WOtaglib" prefix="wo" %>
<jsp:useBean id='soapHook' class='webobjectsexamples.SchoolTools.SoapHook' scope='request'/>
<%
   soapHook.setOrderList((java.util.Vector)                                     session.getAttribute("orderList"));
   soapHook.setPin(      (String)                                               session.getAttribute("pin"));
   soapHook.setWSClient( (com.webobjects.webservices.client.WOWebServiceClient) session.getAttribute("wsClient"));
   soapHook.sendMessage();
%>
<wo:component className="webobjectsexamples.SchoolTools.Results">
      <wo:binding key="confirmationNumber" value='<%= soapHook.getConfirmationNumber() %>' />
      <wo:binding key="status"             value='<%= soapHook.getStatus() %>' />
</wo:component>
