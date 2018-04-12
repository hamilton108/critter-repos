package critterrepos.aspects.log;

import critterrepos.beans.critters.AcceptRuleBean;
import critterrepos.beans.critters.DenyRuleBean;
import critterrepos.beans.options.OptionPurchaseBean;
import oahu.financial.DerivativePrice;
import oahu.financial.OptionPurchase;
import oahu.financial.critters.SellRuleArgs;
import critterrepos.beans.critters.CritterBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
//import java.util.logging.Logger;

public privileged aspect CritterLog {
    Logger log = LoggerFactory.getLogger("critterrepos.beans.critters");

    //---------------------------------- CritterBean -------------------------------------
    pointcut critterApply(CritterBean critter) :
            execution(boolean CritterBean.apply(SellRuleArgs)) && this(critter);

    after(CritterBean critter) returning (boolean result) :
            critterApply(critter) {
        /*
        log.info(String.format("[CritterBean] oid: %d, dfb: %.2f, dfw: %.2f, option price: %.2f, spot: %.2f",
                critter.getOid(),
                param.getDiffFromBought(),
                param.getDiffFromWatermark(),
                param.getOptionPrice(),
                param.getSpot()
                ));
                */
        log.info((String.format("[CritterBean] oid: %d, result: %s", critter.getOid(), result)));
    }

    //---------------------------------- AcceptRuleBean -------------------------------------
    pointcut acceptPass(AcceptRuleBean acc) :
            execution(boolean AcceptRuleBean.pass(SellRuleArgs)) && this(acc);

    after(AcceptRuleBean acc) returning(boolean result):
            acceptPass(acc) {
        log.info(String.format("[AcceptRuleBean] oid: %d, rtyp: %s, acc.val: %.2f result: %s",acc.getOid(),acc.getRtypDesc(),acc.getAccValue(),result));
    }


    //---------------------------------- DenyRuleBean -------------------------------------
    pointcut denyBlock(DenyRuleBean dny) :
            execution(boolean DenyRuleBean.block(SellRuleArgs)) && this(dny);

    after(DenyRuleBean dny) returning(boolean result):
            denyBlock(dny) {
        log.info(String.format("[DenyRuleBean] oid: %d, rtyp: %s, dny.val: %.2f, result: %s",dny.getOid(),dny.getRtypDesc(),dny.getDenyValue(),result));
    }

    //---------------------------------- OptionPurchase -------------------------------------
    pointcut purchaseGetPrice(OptionPurchase purchase) :
            execution(Optional<DerivativePrice> OptionPurchase.getDerivativePrice()) && this(purchase);


    after(OptionPurchase purchase) returning(Optional<DerivativePrice> result):
            purchaseGetPrice(purchase) {
        if (result.isPresent()) {
            DerivativePrice p = result.get();
            log.info(String.format("[OptionPurchase] Got derivative for %s, buy: %.2f, sell: %.2f",
                    purchase.getOptionName(),
                    p.getBuy(),
                    p.getSell()));

        }
        else {
            log.warn(String.format("[OptionPurchase] Did not find derivative for %s", purchase.getOptionName()));
        }
    }

    pointcut purchaseCollectArgs(OptionPurchaseBean purchase) :
           execution(Optional<SellRuleArgs> OptionPurchaseBean.collectArgs()) && this(purchase);

    after(OptionPurchaseBean purchase) returning (Optional<SellRuleArgs> result):
            purchaseCollectArgs(purchase) {
        if (result.isPresent()) {
            SellRuleArgs r = result.get();
            log.info(String.format("[OptionPurchase] [%d] SellRuleArgs for %s: DFB: %2f, DFW: %.2f, price: %.2f, spot: %.2f, current watermark: %.2f",
                    purchase.getOid(),
                    purchase.getOptionName(),
                    r.getDiffFromBought(),
                    r.getDiffFromWatermark(),
                    r.getOptionPrice(),
                    r.getSpot(),
                    purchase.getWatermark()));
        }
        else {
            log.warn(String.format("[OptionPurchase] [%d] Could not get SellRuleArgs for %s",
                    purchase.getOid(), purchase.getOptionName()));
        }
    }
}
