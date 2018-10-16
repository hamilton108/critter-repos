package critterrepos.beans.critters;

import oahu.financial.critters.Critter;
import oahu.financial.critters.SellRuleArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CritterBean implements Critter {
    private static Logger logger = LoggerFactory.getLogger(Critter.class);
    private int oid;
    private String name;
    private int sellVolume;

    private int status;
    private int critterType = 1;
    private int purchaseId;
    private int saleId;

    private List<AcceptRuleBean> acceptrules;

    //----------------- oid -------------------
    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    //----------------- status -------------------
    public int getStatus() {
        return status;
    }

    @Override
    public void setStatus(int value) {
        this.status = value;
    }

    //----------------- critterType -------------------
    public int getCritterType() {
        return critterType;
    }

    public void setCritterType(int value) {
        this.critterType = value;
    }

    //----------------- purchaseId -------------------
    public int getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(int value) {
        this.purchaseId = value;
    }

    //----------------- saleId -------------------
    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    //----------------- name -------------------
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //----------------- sellVolume -------------------
    @Override
    public int getSellVolume() {
        return sellVolume;
    }

    public void setSellVolume(int sellVolume) {
        this.sellVolume = sellVolume;
    }

    //----------------- acceptRules -------------------
    public List<AcceptRuleBean> getAcceptRules() {
        if (acceptrules == null) {
            acceptrules = new ArrayList<AcceptRuleBean>();
        }
        return acceptrules;
    }

    public void setAcceptRules(List<AcceptRuleBean> acceptrules) {
        this.acceptrules = acceptrules;
    }
    public void addAcceptRule(AcceptRuleBean acc) {
        if (acceptrules == null) {
            acceptrules = new ArrayList<AcceptRuleBean>();
        }
        acceptrules.add(acc);
    }

    @Override
    public boolean apply(SellRuleArgs args) {
        /*
        getAcceptRules().forEach(a ->{
            if (a.pass(args) == true) {
                return true;
            }
        });
        */
        for (AcceptRuleBean acc : getAcceptRules()) {
            if (acc.getActive().equals("n")) {
                logger.info(String.format("[Acc %d - %s] Accept rule inactive."), acc.getOid(),acc.getRtypDesc());
                continue;
            }
            if (acc.pass(args) == true) {
                logger.info(String.format("[Acc %d] Rule passes", acc.getOid()));
               return true;
            }
        }
        return false;
    }

    @Override
    public void inspect() {
        System.out.printf("\t[%d] Critter\n", oid);
        if (acceptrules == null || acceptrules.size() == 0) {
            System.out.println("\t\tNo accept rules");
        }
        else {
            for (AcceptRuleBean acc : acceptrules) {
                acc.inspect();
            }
        }
    }

}
