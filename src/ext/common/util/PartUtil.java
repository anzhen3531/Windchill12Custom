package ext.common.util;

import wt.associativity.EquivalenceLink;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.part.WTPart;
import wt.part.WTPartMaster;
import wt.part.WTPartStandardConfigSpec;
import wt.part.WTPartUsageLink;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;
import wt.vc.config.ConfigHelper;
import wt.vc.views.View;
import wt.vc.views.ViewHelper;

import java.util.*;

public class PartUtil {


    /**
     * 根据零部件主数据(master)和视图获取对应最新版本零部件
     *
     * @param master
     * @param view
     * @return
     * @throws WTException
     * @author yyang @author yyang
     */
    public static WTPart getWTPartByMasterAndView(WTPartMaster master, String view) throws WTException {
        WTPart part = null;
        //根据视图名称获取视图对象
        View viewObj = ViewHelper.service.getView(view);
        //根据视图构造产品结构配置规范
        WTPartStandardConfigSpec standardConfig = WTPartStandardConfigSpec.newWTPartStandardConfigSpec(viewObj, null);
        try {
            standardConfig.setView(viewObj);
        } catch (WTPropertyVetoException wpve) {
            throw new WTException("设置view失败，" + wpve.getLocalizedMessage());
        }
        //根据master和视图获取对应最新的视图版本零部件
        QueryResult result = ConfigHelper.service.filteredIterationsOf(master, standardConfig);
        if (result.hasMoreElements()) {
            part = (WTPart) result.nextElement();
        }
        return part;
    }


    /**
     * 创建多视图
     *
     * @param part     物料
     * @param viewName 视图名称
     * @throws WTException
     * @throws WTPropertyVetoException
     */
    public static void createNewView(WTPart part, String viewName) throws WTException, WTPropertyVetoException {
        WTPart wtPartByMasterAndView = PartUtil.getWTPartByMasterAndView(part.getMaster(), viewName);
        if (Objects.nonNull(wtPartByMasterAndView) && wtPartByMasterAndView.getViewName().equals(viewName)) {
            return;
        }
        WTPart newPartView = (WTPart) ViewHelper.service.newBranchForView(part, viewName);
        // 查询子项目
        newPartView = (WTPart) PersistenceHelper.manager.save(newPartView);
        EquivalenceLink equivalenceLink = EquivalenceLink.newEquivalenceLink(part, newPartView);
        equivalenceLink.setIsConsumable(true);
        equivalenceLink.setUpstreamContextRef(part.getView());
        equivalenceLink.setDownstreamContextRef(newPartView.getView());
        PersistenceHelper.manager.save(equivalenceLink);
    }


    /**
     * 获取BOM结构
     *
     * @param part 需要查询的部件
     * @return
     * @throws WTException
     */
    public static Set<WTPart> findChildPart(WTPart part) throws WTException {
        Set<WTPart> set = new HashSet<>();
        QueryResult qr = PersistenceHelper.manager.navigate(part, WTPartUsageLink.USES_ROLE, WTPartUsageLink.class, true);
        while (qr.hasMoreElements()) {
            WTPartMaster master = (WTPartMaster) qr.nextElement();
            WTPart childPart = getWTPartByMasterAndView(master, part.getViewName());
            set.add(childPart);
        }
        return set;
    }


    /**
     * 获取部件bom关系
     *
     * @param part 需要查询的部件
     * @return
     * @throws WTException
     */
    public static Set<WTPartUsageLink> findUsageLink(WTPart part) throws WTException {
        Set<WTPartUsageLink> set = new HashSet<>();
        QueryResult qr = PersistenceHelper.manager.navigate(part, WTPartUsageLink.USES_ROLE, WTPartUsageLink.class, false);
        while (qr.hasMoreElements()) {
            WTPartUsageLink link = (WTPartUsageLink) qr.nextElement();
            set.add(link);
        }
        return set;
    }

    /**
     * 获取所有的物料
     *
     * @param part
     * @return
     * @throws WTException
     */
    public static List<WTPart> findAllChildPart(WTPart part) throws WTException {
        List<WTPart> result = new ArrayList<>();
        result.add(part);
        findChildPart(part, result);
        return result;
    }

    /**
     * 获取子项
     *
     * @param part
     * @param partList
     * @throws WTException
     */
    private static void findChildPart(WTPart part, List<WTPart> partList) throws WTException {
        if (Objects.isNull(part)) {
            return;
        }
        Set<WTPart> childPart = findChildPart(part);
        for (WTPart wtPart : childPart) {
            partList.add(wtPart);
            findChildPart(wtPart, partList);
        }
    }

}
