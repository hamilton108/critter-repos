package critterrepos.beans.critters;

import oahu.financial.critters.RuleType;
import oahu.financial.critters.SellRuleArgs;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCritters {

    //---------------------------------------------------------------------------
    //------------------------- ACCEPT RULES ------------------------------------
    //---------------------------------------------------------------------------
    @DisplayName("Test AcceptRule diff from bought")
    @Test
    public void testAcceptRule_diffFromBought() {
        AcceptRuleBean acc = new AcceptRuleBean(7, 2.0, RuleType.DFB.getKind()) ;
        SellRuleArgs args = new SellRuleArgs(1.9,0,0,0);
        assertFalse(acc.pass(args));
        SellRuleArgs args2 = new SellRuleArgs(2.1,0,0,0);
        assertTrue(acc.pass(args2));
    }

    @DisplayName("Test AcceptRule diff from watermark")
    @Test
    public void testAcceptRule_diffFromWatermark() {
        AcceptRuleBean acc = new AcceptRuleBean(6, 2.0, RuleType.DFW.getKind()) ;
        SellRuleArgs args = new SellRuleArgs(0,1.9,0,0);
        SellRuleArgs args2 = new SellRuleArgs(0,2.1,0,0);

        assertFalse(acc.pass(args));
        assertTrue(acc.pass(args2));
    }

    @DisplayName("Test AcceptRule with deny rules diff from watermark")
    @Test
    public void testAcceptRule_denyRules_diffFromWatermark() {
        AcceptRuleBean acc = new AcceptRuleBean(6, 2.0, RuleType.DFW.getKind()) ;

        SellRuleArgs args = new SellRuleArgs(0,1.9,0,0);
        SellRuleArgs args2 = new SellRuleArgs(0,2.1,0,0);

        DenyRuleBean d1 = new DenyRuleBean(5, 12.0, RuleType.OP_ROOF.getKind(), "y", "n");

        assertFalse(acc.pass(args));
        assertTrue(acc.pass(args2));
    }
    //-------------------------------------------------------------------------
    //------------------------- DENY RULES ------------------------------------
    //-------------------------------------------------------------------------
    @DisplayName("Test DenyRule option roof with memory")
    @Test
    public void testDenyRule_opRoof_withMemory() {
        DenyRuleBean d = new DenyRuleBean(5, 12.0, RuleType.OP_ROOF.getKind(), "y", "y");

        SellRuleArgs args = new SellRuleArgs(0,0,0,11.9);
        SellRuleArgs args2 = new SellRuleArgs(0,0,0,12.1);

        assertFalse(d.block(args2), "Block args2 1. time should be false");
        assertTrue(d.block(args), "Block args should be true");
        assertTrue(d.block(args2), "Block args2 2. time should be true");
    }

    @DisplayName("Test DenyRule option roof without memory")
    @Test
    public void testDenyRule_opRoof_withoutMemory() {
        DenyRuleBean d = new DenyRuleBean(3, 12.0, RuleType.OP_ROOF.getKind(), "y", "n");

        SellRuleArgs args = new SellRuleArgs(0,0,0,11.9);
        SellRuleArgs args2 = new SellRuleArgs(0,0,0,12.1);

        assertFalse(d.block(args2), "Block args2 1. time should be false");
        assertTrue(d.block(args), "Block args should be true");
        assertFalse(d.block(args2), "Block args2 2. time should be false");
    }
    @DisplayName("Test DenyRule option floor without memory")
    @Test
    public void testDenyRule_opFloor_withoutMemory() {
        DenyRuleBean d = new DenyRuleBean(3, 12.0, RuleType.OP_FLOOR.getKind(), "y", "n");

        SellRuleArgs args = new SellRuleArgs(0,0,0,11.9);
        SellRuleArgs args2 = new SellRuleArgs(0,0,0,12.1);

        assertTrue(d.block(args2), "Block args2 1. time should be true");
        assertFalse(d.block(args), "Block args should be false");
        assertTrue(d.block(args2), "Block args2 2. time should be true");
    }
    @DisplayName("Test DenyRule stockprice roof without memory")
    @Test
    public void testDenyRule_stockpriceRoof_withoutMemory() {
        DenyRuleBean d = new DenyRuleBean(2, 100.0, RuleType.SP_ROOF.getKind(), "y", "n");

        SellRuleArgs args = new SellRuleArgs(0,0,100.2,0);
        SellRuleArgs args2 = new SellRuleArgs(0,0,99.9,0);

        assertTrue(d.block(args2), "Block args2 1. time should be true");
        assertFalse(d.block(args), "Block args should be false");
        assertTrue(d.block(args2), "Block args2 2. time should be true");
    }
    @DisplayName("Test DenyRule stockprice floor without memory")
    @Test
    public void testDenyRule_stockpriceFloor_withoutMemory() {
        DenyRuleBean d = new DenyRuleBean(2, 100.0, RuleType.SP_FLOOR.getKind(), "y", "n");

        SellRuleArgs args = new SellRuleArgs(0,0,100.2,0);
        SellRuleArgs args2 = new SellRuleArgs(0,0,99.9,0);

        assertFalse(d.block(args2), "Block args2 1. time should be false");
        assertTrue(d.block(args), "Block args should be true");
        assertFalse(d.block(args2), "Block args2 2. time should be false");
    }

}
