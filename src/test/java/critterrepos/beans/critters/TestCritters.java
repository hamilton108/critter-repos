package critterrepos.beans.critters;

import critter.critterrule.*;
import org.junit.Test;
import static  org.assertj.core.api.Assertions.assertThat;


public class TestCritters {
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
