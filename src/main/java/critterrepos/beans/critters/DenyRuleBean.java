package critterrepos.beans.critters;

import oahu.financial.critters.SellRuleArgs;

public class DenyRuleBean extends AbstractRule {
    /*
    oid        | integer   | not null default nextval('deny_rules_oid_seq'::regclass)
    denyValue      | price     | not null
    rtyp       | integer   | not null
    group_id   | integer   | not null
    active     | bool_type | default 'y'::bpchar
    has_memory | bool_type | default 'y'::bpchar
    */
    private int oid;
    private int groupId;
    private double denyValue;
    private String rtypDesc;
    private String active = "y";
    private String memory;

    public DenyRuleBean() {
    }

    public DenyRuleBean(int oid,
                        double denyValue,
                        int rtyp,
                        String active,
                        String memory) {
        this.oid = oid;
        this.denyValue = denyValue;
        this.rtyp = rtyp;
        this.active = active;
        this.memory = memory;
    }

    public DenyRuleBean(int oid,
                        double denyValue,
                        int rtyp) {
        this(oid,denyValue,rtyp,"y","y");
    }

    //---------------- oid ----------------
    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    //---------------- groupId ----------------
    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int value) {
        this.groupId = value;
    }

    //---------------- oid ----------------
    public double getDenyValue() {
        return denyValue;
    }

    public void setDenyValue(double denyValue) {
        this.denyValue = denyValue;
    }

    //---------------- oid ----------------
    public int getRtyp() {
        return rtyp;
    }

    public void setRtyp(int rtyp) {
        this.rtyp = rtyp;
    }

    //---------------- oid ----------------
    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    //---------------- oid ----------------
    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getRtypDesc() {
        return rtypDesc;
    }

    public void setRtypDesc(String rtypDesc) {
        this.rtypDesc = rtypDesc;
    }

    //---------------- block -----------------
    public boolean block(SellRuleArgs args) {
        switch (getRtypEnum()) {
            case SP_FLOOR: {
                if (args.getSpot() > denyValue) {
                    return true;
                }
                return false;
            }
            case SP_ROOF: {
                if (args.getSpot() < denyValue) {
                    return true;
                }
                return false;
            }
            case OP_FLOOR: {
                if (args.getOptionPrice() > denyValue) {
                    return true;
                }
                return false;
            }
            case OP_ROOF: {
                if (args.getOptionPrice() < denyValue) {
                    return true;
                }
                return false;
            }
            default: return false;
        }
    }

    public void inspect() {
        System.out.printf("\t\t[%d] Deny rule - rtyp: %s, denyValue: %.2f, active: %s, memory: %s  \n", oid, rtypDesc, denyValue, active, memory);
    }
}
