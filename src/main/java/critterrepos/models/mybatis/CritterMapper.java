package critterrepos.models.mybatis;


import oahu.financial.OptionPurchase;
import org.apache.ibatis.annotations.Param;
import critterrepos.beans.critters.*;
import critterrepos.beans.options.OptionPurchaseBean;
import critterrepos.beans.options.OptionSaleBean;

import java.util.List;


public interface CritterMapper {
    void toggleAcceptRule(@Param("oid") int oid, @Param("isActive") String isActive);
    void toggleDenyRule(@Param("oid") int oid, @Param("isActive") String isActive);

    List<OptionPurchaseBean> activePurchases(@Param("tickerIds") List<Integer> tickerIds,
                                                   @Param("purchaseType") int purchaseType);

    List<OptionPurchaseBean> activePurchasesAll(@Param("purchaseType") int purchaseType);

    List<OptionPurchaseBean> purchasesWithSales(@Param("stockId") int stockId,
                                                      @Param("purchaseType") int purchaseType,
                                                      @Param("status") int status,
                                                      @Param("optype") String optype);

    List<OptionPurchaseBean> purchasesWithSalesAll(
                                                      @Param("purchaseType") int purchaseType,
                                                      @Param("status") int status,
                                                      @Param("optype") String optype);

    OptionPurchaseBean findPurchase(@Param("purchaseId") int purchaseId);

    OptionPurchaseBean findPurchaseForCritId(@Param("critterId") int critterId);

    OptionPurchaseBean findPurchaseForAccId(@Param("accId") int accId);

    void insertPurchase(OptionPurchaseBean purchase);

    void insertCritter(CritterBean critter);

    void insertGradientRule(GradientRuleBean rule);

    void insertAcceptRule(AcceptRuleBean rule);

    void insertDenyRule(DenyRuleBean rule);

    List<RuleTypeBean> ruleTypes();

    void registerCritterClosedWithSale(CritterBean critter);

    void registerPurchaseFullySold(OptionPurchase purchase);

    void insertCritterSale(OptionSaleBean sale);
}
