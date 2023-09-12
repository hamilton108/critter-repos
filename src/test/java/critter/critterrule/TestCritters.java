package critter.critterrule;

import org.junit.jupiter.api.Test;
import static  org.assertj.core.api.Assertions.assertThat;


public class TestCritters {

    @Test
    public void testSelRuleArgs_equals() {
        double pb = 0.2;
        double pw = 0.0;
        double ps = 30.4;
        double po = 10.4;
        var a = new SellRuleArgs(pb+0.00002,pw, ps, po);
        var b = new SellRuleArgs(pb,pw, ps, po);
        assertThat(a).isEqualTo(b);
        var c = new SellRuleArgs(pb+0.4,pw, ps, po);
        assertThat(a).isNotEqualTo(c);
    }

    @Test
    public void testAccValue_description() {
        AcceptRule acc = new AcceptRule(7, 2.0, RuleTypeEnum.DFB.getKind()) ;

        assertThat(acc.getRtypDesc()).isEqualTo("Diff from bought");
    }

    @Test
    public void testCritterEnum() {

       var critter = new Critter();

        assertThat(critter.getStatus()).isEqualTo(0);
        assertThat(critter.getStatusEnum()).isEqualTo(CritterEnum.NA);

        critter.setStatusEnum(CritterEnum.ACTIVE);
        assertThat(critter.getStatus()).isEqualTo(7);

        critter.setStatusEnum(CritterEnum.CRITTER_SOLD);
        assertThat(critter.getStatus()).isEqualTo(9);

    }
    //---------------------------------------------------------------------------
    //------------------------- ACCEPT RULES ------------------------------------
    //---------------------------------------------------------------------------
    @Test
    public void testAcceptRule_diffFromBought() {
        AcceptRule acc = new AcceptRule(7, 2.0, RuleTypeEnum.DFB.getKind()) ;

        SellRuleArgs args = new SellRuleArgs(1.9,0,0,0);
        assertThat(acc.pass(args)).isFalse();

        SellRuleArgs args2 = new SellRuleArgs(2.1,0,0,0);
        assertThat(acc.pass(args2)).isTrue();
    }

    @Test
    public void testAcceptRule_denyrules_diffFromBought() {
        AcceptRule acc = new AcceptRule(7, 2.0, RuleTypeEnum.DFB.getKind()) ;
        var d1 = new DenyRule(5, 12.0, RuleTypeEnum.OP_ROOF.getKind(), "y", "n");
        acc.addDenyRule(d1);

        SellRuleArgs args = new SellRuleArgs(1.9,0,0,0);
        assertThat(acc.pass(args)).isFalse();
    }

    @Test
    public void testAcceptRule_diffFromWatermark() {
        AcceptRule acc = new AcceptRule(6, 2.0, RuleTypeEnum.DFW.getKind()) ;
        SellRuleArgs args = new SellRuleArgs(0,1.9,0,0);
        SellRuleArgs args2 = new SellRuleArgs(0,2.1,0,0);

        assertThat(acc.pass(args)).isFalse();
        assertThat(acc.pass(args2)).isTrue();
    }

    @Test
    public void testAcceptRule_denyRules_diffFromWatermark() {
        AcceptRule acc = new AcceptRule(6, 2.0, RuleTypeEnum.DFW.getKind()) ;

        SellRuleArgs args = new SellRuleArgs(0,1.9,0,0);
        SellRuleArgs args2 = new SellRuleArgs(0,2.1,0,0);

        var d1 = new DenyRule(5, 12.0, RuleTypeEnum.OP_ROOF.getKind(), "y", "n");

        assertThat(acc.pass(args)).isFalse();
        assertThat(acc.pass(args2)).isTrue();
    }

    //-------------------------------------------------------------------------
    //------------------------- DENY RULES ------------------------------------
    //-------------------------------------------------------------------------
    @Test
    public void testDenyRule_opRoof_withMemory() {
        var d = new DenyRule(5, 12.0, RuleTypeEnum.OP_ROOF.getKind(), "y", "y");

        SellRuleArgs args = new SellRuleArgs(0,0,0,11.9);
        SellRuleArgs args2 = new SellRuleArgs(0,0,0,12.1);

        assertThat(d.block(args2)).isFalse();
        assertThat(d.block(args)).isTrue();
        assertThat(d.block(args2)).isTrue();
    }

    @Test
    public void testDenyRule_opRoof_withoutMemory() {
        var d = new DenyRule(3, 12.0, RuleTypeEnum.OP_ROOF.getKind(), "y", "n");

        SellRuleArgs args = new SellRuleArgs(0,0,0,11.9);
        SellRuleArgs args2 = new SellRuleArgs(0,0,0,12.1);

        assertThat(d.block(args2)).isFalse();
        assertThat(d.block(args)).isTrue();
        assertThat(d.block(args2)).isFalse();
    }
    @Test
    public void testDenyRule_opFloor_withoutMemory() {
        var d = new DenyRule(3, 12.0, RuleTypeEnum.OP_FLOOR.getKind(), "y", "n");

        SellRuleArgs args = new SellRuleArgs(0,0,0,11.9);
        SellRuleArgs args2 = new SellRuleArgs(0,0,0,12.1);

        assertThat(d.block(args2)).isTrue();
        assertThat(d.block(args)).isFalse();
        assertThat(d.block(args2)).isTrue();
    }
    @Test
    public void testDenyRule_stockpriceRoof_withoutMemory() {
        var d = new DenyRule(2, 100.0, RuleTypeEnum.SP_ROOF.getKind(), "y", "n");

        SellRuleArgs args = new SellRuleArgs(0,0,100.2,0);
        SellRuleArgs args2 = new SellRuleArgs(0,0,99.9,0);

        assertThat(d.block(args2)).isTrue();
        assertThat(d.block(args)).isFalse();
        assertThat(d.block(args2)).isTrue();
    }
    @Test
    public void testDenyRule_stockpriceFloor_withoutMemory() {
        var d = new DenyRule(2, 100.0, RuleTypeEnum.SP_FLOOR.getKind(), "y", "n");

        SellRuleArgs args = new SellRuleArgs(0,0,100.2,0);
        SellRuleArgs args2 = new SellRuleArgs(0,0,99.9,0);

        assertThat(d.block(args2)).isFalse();
        assertThat(d.block(args)).isTrue();
        assertThat(d.block(args2)).isFalse();
    }

}
