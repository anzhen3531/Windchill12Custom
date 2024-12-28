<%@ page import="java.util.HashMap,
                 com.ptc.netmarkets.util.beans.NmURLFactoryBean" %>

<frameset rows="55%" id="processmanagerframeset" framespacing="1" bordercolor="#BDBDBD">
    <%
        HashMap urlParam = new HashMap();
        urlParam.put("oid", request.getParameter("oid"));
        NmURLFactoryBean urlFactoryBean = new NmURLFactoryBean();
        String url1 = "/apps/processmanager/customProcessmanagergraph.jsp";
        String strURL1 = urlFactoryBean.getFullyQualifiedHREF(url1, urlParam);
        System.out.println("strURL1 = " + strURL1);
    %>
    <frame src="<%= strURL1 %>" name="main" id="main" scrolling=no>
</frameset> 
