<%@ page import="wt.content.ApplicationData" %>
<%@ page import="wt.fc.PersistenceHelper" %>
<%@ page import="wt.content.ContentHelper" %>
<%@ page import="wt.content.ContentServerHelper" %>
<%
    // 需要指定stream id
    ApplicationData applicationData = new ApplicationData();
    // 需要设置一个必填属性 ， 电子仓库属性 ，不然无法进行上传
    PersistenceHelper.manager.save(applicationData);

    // 流程相关定制
%>