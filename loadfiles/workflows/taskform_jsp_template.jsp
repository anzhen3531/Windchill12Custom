<%@ page import="java.util.HashMap" %>
<%@ page import="com.ptc.netmarkets.util.beans.NmURLFactoryBean" %>
<%@ page import="com.ptc.netmarkets.util.misc.NetmarketURL" %>
<%@ page import="ext.common.util.ToolUtils" %>
<%@ page import="wt.fc.Persistable" %>
<%@ page import="wt.workflow.work.WorkItem" %>
<%@ page import="wt.workflow.engine.WfActivity" %>
<%@ page import="wt.workflow.engine.WfProcess" %>
<%@ taglib uri="http://www.ptc.com/windchill/taglib/components" prefix="jca" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>

<%@taglib uri="http://www.ptc.com/windchill/taglib/workItem" prefix="workItem" %>

<%@ include file="/netmarkets/jsp/util/begin.jspf" %>

<workItem:MyWorkItem/>
<c:if test="${myWorkItem != null}">
    <%
        String processManagerUrl = "/apps/processmanager/customProcessmanagergraph.jsp";
        String oid = request.getParameter("oid");
        Persistable objectByOid = ToolUtils.getObjectByOid(oid);
        if (objectByOid instanceof WorkItem) {
            WorkItem workItem = (WorkItem) objectByOid;
            WfActivity activity = (WfActivity) workItem.getSource().getObject();
            try {
                WfProcess process = activity.getParentProcess();
                String orOid = ToolUtils.getOROid(process);
                HashMap urlParam = new HashMap();
                urlParam.put("oid", orOid);
                NmURLFactoryBean bean = new NmURLFactoryBean();
                processManagerUrl = bean.getFullyQualifiedHREF(processManagerUrl, urlParam);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    %>


    <script type="text/javascript">
        function resizeIframe() {
            var height = 0;
            console.log(window.innerHeight);

            if (window.innerHeight) {
                height = window.innerHeight;
            } else if (document.documentElement && document.documentElement.clientHeight) {
                height = document.documentElement.clientHeight;
            } else if (document.body) {
                height = document.body.clientHeight;
            }
            height -= document.getElementById('outerframe').offsetTop;
            console.log(" document.getElementById('outerframe').offsetTop" + document.getElementById('outerframe').offsetTop);
            if (document.getElementById('outerframe').offsetTop == 0) {
                height -= 200;
            } else {
                height -= 10;
            }

            console.log(height);
            document.getElementById('outerframe').style.height = height + "px";

        }
    </script>

    <%-- There are two steps to render a custom activity variable on the task page.

    1) Place the name of the activity variable that you want to display on the
    page in the comma delimited custom_variables list.

    Height and/or width can be specified using CSS style syntax.
    For example "variable_name{height:1;width:2}".
    Specifying "all_activity_variables" will display all visible activity
    variables not named "special_instructions", or "instructions".

    2) To specify where to render the particular variable on the page you must add
    the following line.

    <tags:taskPanelValue propertyModel="${propertyModel}" attrs="variable_name"/>

    Note: by default all_activty_variables are rendered.  If you add a custom
    activity variable and have all_activity_variables the variable will show up twice.

    --%>

    <tags:workItemInfo custom_variables="all_activity_variables"/>
    <BR>

    <%--    信息展示 div --%>
    <div>
        <table border="0" cellpadding="0" cellspacing="0" width="100%">
            <tr>
                <td rowspan="3" width="12"><IMG SRC="netmarkets/images/sp.gif" height="1" width="12"></td>
                <td rowspan="3" class="tableborderbg" width="1"><IMG SRC="netmarkets/images/sp.gif" height="1"
                                                                     width="1"></td>
                <td class="tableborderbg"><IMG SRC="netmarkets/images/sp.gif" height="1" width="1"></td>
                <td rowspan="3" class="tableborderbg" width="1"><IMG SRC="netmarkets/images/sp.gif" height="1"
                                                                     width="1"></td>
                <td rowspan="3" width="12"><IMG SRC="netmarkets/images/sp.gif" height="1" width="12"></td>
            </tr>
            <tr class="detailsboxbg">
                <td>

                    <table cellpadding="0" cellspacing="0" border="0" align="right" width=100%>
                        <tr>
                            <td> &nbsp;</td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr class="detailsboxbg">
                <td>
                    <table border="0" cellpadding="0" cellspacing="0">
                        <tr>
                            <td><tags:taskPanel propertyModel="${propertyModel}" attrs="workitem_detailaction"/></td>
                            <td width="12"><IMG SRC="netmarkets/images/sp.gif" height="1" width="12"></td>
                            <td><font class="projectnamefont"><IMG SRC="netmarkets/images/open_work.gif">
                                    <tags:taskPanelValue propertyModel="${propertyModel}"
                                                         attrs="workitem_activityname"/>
                            </td>
                        </tr>
                    </table>
                    <div align="left">

                        <table border="0" cellpadding="1" width=100%>
                            <!-- Task Information Section -->
                            <span class="x-reset">
                                <tr>
                                    <tags:taskPanel propertyModel="${propertyModel}" attrs="workitem_instructions"/>
                                </tr>
                                </span>
                            <tr>

                            <tr>
                                <td align="right" valign="top" nowrap><FONT class=tabledatafont>
                                    <tags:taskPanelLabel propertyModel="${propertyModel}"
                                                         attrs="workitem_processname"/></FONT>
                                </td>
                                <td valign="top"><FONT class=tabledatafont>
                                        <tags:taskPanelValue propertyModel="${propertyModel}"
                                                             attrs="workitem_lightweightprocessmonitor"/>
                                        <tags:taskPanelValue propertyModel="${propertyModel}"
                                                             attrs="workitem_processname"/>
                                </td>
                            </tr>

                            <tr>
                                <tags:taskPanel propertyModel="${propertyModel}" attrs="workitem_processinitiator"/>
                            </tr>

                            <tr>
                                <tags:taskPanel propertyModel="${propertyModel}" attrs="workitem_assignee"/>
                            </tr>
                            <tr>
                                <td align="right" valign="top" nowrap><FONT class=tabledatafont>
                                    <tags:taskPanelLabel propertyModel="${propertyModel}" attrs="workitem_role"/></FONT>
                                </td>
                                <td valign="top"><FONT class=tabledatafont>
                                        <tags:taskPanelValue propertyModel="${propertyModel}" attrs="workitem_role"/>
                                </td>
                            </tr>
                            <tr>
                                <tags:taskPanel propertyModel="${propertyModel}" attrs="workitem_priority"/>
                            </tr>
                            <tr>
                                <tags:taskPanel propertyModel="${propertyModel}" attrs="workitem_deadline"/>
                            </tr>
                            <tr>
                                <tags:taskPanel propertyModel="${propertyModel}" attrs="workitem_pbolink"/>
                            </tr>

                            <tr>
                                <tags:taskPanel propertyModel="${propertyModel}" attrs="workitem_state"/>
                            </tr>

                            <tags:taskPanelValue propertyModel="${propertyModel}" attrs="all_activity_variables"/>

                            <tr>

                                <td valign="middle" colspan="3">
                                    <FONT class=wizardbuttonfont>
                                        <tags:adhocAct/></FONT></td>
                            </tr>


                            <tr>
                                <td valign="middle" colspan="3">
                                    <hr size="1" width="100%">
                                </td>
                            </tr>

                            <tags:workItemActions/>

                            <tr>
                                <td valign="middle" colspan="3">
                                    <hr size="1" width="100%">
                                </td>
                            </tr>

                            <tr>
                                <tags:taskPanel propertyModel="${propertyModel}" attrs="workitem_esignature"/>
                            </tr>
                        </table>

                    </div>
                </td>
            </tr>
            <tr>
                <td></td>
                <td></td>
                <td class="tableborderbg"><IMG SRC="netmarkets/images/sp.gif" height="1" width="1"></td>
            </tr>
        </table>
    </div>

    <div class="main-container">
        <jsp:include page="/netmarkets/jsp/customtemplates/completeButton.jsp"/>
    </div>

    <%--  流程地图  --%>
    <div>
        <iframe width="100%" height="100%" marginheight="100%" onload="resizeIframe()"
                frameborder="0" id="outerframe" src=<%=processManagerUrl%>>
        </iframe>
    </div>

    <!-- PBO Info -->
    <div>
        <!-- show the entire routing history & reassignment history tables -->
        <tags:routingStatus dispProcess="ALL"/>
        <!-- show the reassignment history within a table -->
        <!-- tags:reassignHistory showRH="table"/ -->

        <!-- displayType options are "table" or "link".  This tag only works when PBO implements interface SubjectOfNotebook -->
            <%--<workItem:notebook displayType="table"/>--%>

        <!-- displayType options are "table" or "link".  This tag only works when PBO implements interface SubjectOfForum -->
            <%--<workItem:discussions displayType="table"/>--%>
    </div>

    <BR>
</c:if>




<%@ include file="/netmarkets/jsp/util/end.jspf" %>