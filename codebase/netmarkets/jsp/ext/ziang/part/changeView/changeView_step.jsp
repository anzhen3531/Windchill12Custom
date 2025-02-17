<%@ taglib uri="http://www.ptc.com/windchill/taglib/components" prefix="jca"%>
<%@ taglib uri="http://www.ptc.com/windchill/taglib/fmt" prefix="fmt"%>

<%@ include file="/netmarkets/jsp/util/begin.jspf"%>
<%@ include file="/netmarkets/jsp/components/createEditUIText.jspf"%>
<%@ include file="/netmarkets/jsp/components/includeWizBean.jspf"%>

<fmt:setBundle basename="com.ptc.windchill.enterprise.part.partResource"/>
<fmt:message var="newVersionViewLabel" key="part.newViewVersion_step.SELECT_VIEW" />

<jca:describePropertyPanel var="defineItemStepAttributesPanelDescriptor" scope="request"
        id = "createNewViewVersion"
        type = "wt.part.WTPart"
        componentType="WIZARD_ATTRIBUTES_TABLE" 
        mode="EDIT">

  <jca:describeProperty id="newVersionView" label="${newVersionViewLabel}"/>
  <jca:describeProperty id="lifeCycle.id"/>
  <jca:describeProperty id="teamTemplate.id"/>
</jca:describePropertyPanel>

<jca:getModel var="propertyModel" descriptor="${defineItemStepAttributesPanelDescriptor}"
               serviceName="com.ptc.core.components.forms.CreateAndEditModelGetter"
               methodName="getItemAttributes">
   <jca:addServiceArgument value="${defineItemStepAttributesPanelDescriptor}" />
   <jca:addServiceArgument value="${commandBean}" />
   <jca:addServiceArgument value="${nmcontext.context}" />
</jca:getModel>

<jca:renderPropertyPanel model="${propertyModel}" />

<%@ include file="/netmarkets/jsp/util/end.jspf"%>
