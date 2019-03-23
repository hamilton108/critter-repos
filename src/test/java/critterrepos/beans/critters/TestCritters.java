package critterrepos.beans.critters;

import oahu.financial.critters.RuleType;
import oahu.financial.critters.SellRuleArgs;
import org.junit.Test;
import static  org.assertj.core.api.Assertions.assertThat;


public class TestCritters {
    //---------------------------------------------------------------------------
    //------------------------- ACCEPT RULES ------------------------------------
    //---------------------------------------------------------------------------
    @Test
    public void testAcceptRule_diffFromBought() {
        AcceptRuleBean acc = new AcceptRuleBean(7, 2.0, RuleType.DFB.getKind()) ;

        SellRuleArgs args = new SellRuleArgs(1.9,0,0,0);
        assertThat(acc.pass(args)).isFalse();

        SellRuleArgs args2 = new SellRuleArgs(2.1,0,0,0);
        assertThat(acc.pass(args2)).isTrue();
    }

    @Test
    public void testAcceptRule_diffFromWatermark() {
        AcceptRuleBean acc = new AcceptRuleBean(6, 2.0, RuleType.DFW.getKind()) ;
        SellRuleArgs args = new SellRuleArgs(0,1.9,0,0);
        SellRuleArgs args2 = new SellRuleArgs(0,2.1,0,0);

        assertThat(acc.pass(args)).isFalse();
        assertThat(acc.pass(args2)).isTrue();
    }

    @Test
    public void testAcceptRule_denyRules_diffFromWatermark() {
        AcceptRuleBean acc = new AcceptRuleBean(6, 2.0, RuleType.DFW.getKind()) ;

        SellRuleArgs args = new SellRuleArgs(0,1.9,0,0);
        SellRuleArgs args2 = new SellRuleArgs(0,2.1,0,0);

        DenyRuleBean d1 = new DenyRuleBean(5, 12.0, RuleType.OP_ROOF.getKind(), "y", "n");

        assertThat(acc.pass(args)).isFalse();
        assertThat(acc.pass(args2)).isTrue();
    }

    //-------------------------------------------------------------------------
    //------------------------- DENY RULES ------------------------------------
    //-------------------------------------------------------------------------
    @Test
    public void testDenyRule_opRoof_withMemory() {
        DenyRuleBean d = new DenyRuleBean(5, 12.0, RuleType.OP_ROOF.getKind(), "y", "y");

        SellRuleArgs args = new SellRuleArgs(0,0,0,11.9);
        SellRuleArgs args2 = new SellRuleArgs(0,0,0,12.1);

        assertThat(d.block(args2)).isFalse();
        assertThat(d.block(args)).isTrue();
        assertThat(d.block(args2)).isTrue();
    }

    @Test
    public void testDenyRule_opRoof_withoutMemory() {
        DenyRuleBean d = new DenyRuleBean(3, 12.0, RuleType.OP_ROOF.getKind(), "y", "n");

        SellRuleArgs args = new SellRuleArgs(0,0,0,11.9);
        SellRuleArgs args2 = new SellRuleArgs(0,0,0,12.1);

        assertThat(d.block(args2)).isFalse();
        assertThat(d.block(args)).isTrue();
        assertThat(d.block(args2)).isFalse();
    }
    @Test
    public void testDenyRule_opFloor_withoutMemory() {
        DenyRuleBean d = new DenyRuleBean(3, 12.0, RuleType.OP_FLOOR.getKind(), "y", "n");

        SellRuleArgs args = new SellRuleArgs(0,0,0,11.9);
        SellRuleArgs args2 = new SellRuleArgs(0,0,0,12.1);

        assertThat(d.block(args2)).isTrue();
        assertThat(d.block(args)).isFalse();
        assertThat(d.block(args2)).isTrue();
    }
    @Test
    public void testDenyRule_stockpriceRoof_withoutMemory() {
        DenyRuleBean d = new DenyRuleBean(2, 100.0, RuleType.SP_ROOF.getKind(), "y", "n");

        SellRuleArgs args = new SellRuleArgs(0,0,100.2,0);
        SellRuleArgs args2 = new SellRuleArgs(0,0,99.9,0);

        assertThat(d.block(args2)).isTrue();
        assertThat(d.block(args)).isFalse();
        assertThat(d.block(args2)).isTrue();
    }
    @Test
    public void testDenyRule_stockpriceFloor_withoutMemory() {
        DenyRuleBean d = new DenyRuleBean(2, 100.0, RuleType.SP_FLOOR.getKind(), "y", "n");

        SellRuleArgs args = new SellRuleArgs(0,0,100.2,0);
        SellRuleArgs args2 = new SellRuleArgs(0,0,99.9,0);

        assertThat(d.block(args2)).isFalse();
        assertThat(d.block(args)).isTrue();
        assertThat(d.block(args2)).isFalse();
    }

}
