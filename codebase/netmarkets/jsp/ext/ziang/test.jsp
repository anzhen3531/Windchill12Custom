<%@ page import="wt.content.ApplicationData" %>
<%@ page import="wt.fc.PersistenceHelper" %>
<%@ page import="wt.content.ContentHelper" %>
<%@ page import="wt.content.ContentServerHelper" %>
<%
    // 需要指定stream id
    ApplicationData applicationData = new ApplicationData();

    applicationData.setStreamData();
    // 需要设置一个必填属性 ， 电子仓库属性 ，不然无法进行上传
    PersistenceHelper.manager.save(applicationData);
    ContentHelper.service.updateAppData()

//    QuerySpec qs = new QuerySpec();
//    int htc = qs.appendClassList(HolderToContent.class, false);
//    int ad = qs.appendClassList(ApplicationData.class, false);
//    int fi = qs.appendClassList(FvItem.class, false);
//    int fm = qs.appendClassList(FvMount.class, false);
//    qs.appendSelectAttribute("path", fm, false);
//    qs.appendSelectAttribute("uniqueSequenceNumber", fi, false);
//    qs.appendWhere(new SearchCondition(HolderToContent.class, WTAttributeNameIfc.ROLEB_OBJECT_ID, ApplicationData.class, WTAttributeNameIfc.ID_NAME), new int[]{htc, ad});
//    qs.appendAnd();
//    qs.appendWhere(new SearchCondition(HolderToContent.class, WTAttributeNameIfc.ROLEA_OBJECT_ID, new long[]{6614102}), new int[]{htc});
    // ID of the ContentHolder (eg WTDocument)
    // qs.appendAnd();
    // qs.appendWhere(new SearchCondition(FvItem.class, "folderReference.key.id", FvMount.class, WTAttributeNameIfc.ROLEA_OBJECT_ID), new int[] {fi, fm});
    // qs.appendAnd();
    // qs.appendWhere(new SearchCondition(ApplicationData.class, "streamData.key.id", FvItem.class, WTAttributeNameIfc.ID_NAME), new int[] {ad, fi});
    // qs.appendAnd();
    // qs.appendWhere(new SearchCondition(ApplicationData.class, ApplicationData.ROLE, SearchCondition.EQUAL, ContentRoleType.PRIMARY), new int[] {ad});
    // Use ContentRoleType.SECONDARY for attachments
    // QueryResult qr = PersistenceHelper.manager.find(qs);
    // while (qr.hasMoreElements()) {
    // Object[] p = (Object[]) qr.nextElement();
    // String path = (String) p[0];
    // BigDecimal fileBigDecimal = (BigDecimal) p[1];
    // String file = org.apache.commons.lang3.StringUtils.leftPad(fileBigDecimal.toBigInteger().toString(16), 14, '0');
    // System.out.println("Result: " + path + File.separator + file);
    // }

%>