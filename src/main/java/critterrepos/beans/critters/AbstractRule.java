package critterrepos.beans.critters;

import oahu.financial.critters.RuleType;

public abstract class AbstractRule {
    protected int rtyp;

    protected RuleType rtypEnum;
    protected RuleType getRtypEnum() {
        if (rtypEnum == null) {
            //rtypEnum = RuleType.values()[rtyp];
            rtypEnum = RuleType.valueOf(rtyp);
        }
        return rtypEnum;
    }

    public int getRtyp() {
        return rtyp;
    }

    public void setRtyp(int rtyp) {
        this.rtyp = rtyp;
    }


}
