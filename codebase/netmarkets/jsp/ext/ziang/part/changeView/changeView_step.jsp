<%@ page import="java.util.ArrayList" %>
<%@ page import="wt.vc.views.ViewHelper" %>
<%@ page import="wt.vc.views.View" %>
<%@page pageEncoding="UTF-8"%>
<%@ page import="java.util.Objects" %>
<%@ taglib uri="http://www.ptc.com/windchill/taglib/components" prefix="jca" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"   %>
<%@ include file="/netmarkets/jsp/util/begin.jspf" %>
<%@ taglib uri="http://www.ptc.com/windchill/taglib/wrappers" prefix="w"%>


<%
    ArrayList<String> viewNameList = new ArrayList<>();
    View design = ViewHelper.service.getView("Design");
    View[] viewNames = ViewHelper.service.getAllChildren(design);
    System.out.println("viewNames = " + viewNames);
    for (View view : viewNames) {
        System.out.println("view.getName() = " + view.getName());
        if (Objects.equals(view.getName(), "Manufacturing") ||
                Objects.equals(view.getName(), "Service")) {
            continue;
        }
        viewNameList.add(view.getName());
    }
    System.out.println("viewNameList = " + viewNameList);
%>

<c:set var="viewNameList" value="<%=viewNameList %>"/>

<jca:renderPropertyPanel>
    <w:comboBox id="newVersionView" propertyLabel="请选择视图" name="newVersionView"
                size="1" required="true"
                multiSelect="false"
                internalValues="${viewNameList}"
                displayValues="${viewNameList}"/>
</jca:renderPropertyPanel>


<%@ include file="/netmarkets/jsp/util/end.jspf" %>
