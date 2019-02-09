package critterrepos.beans.critters;

import oahu.financial.critters.RuleType;
import oahu.financial.critters.SellRuleArgs;
import org.junit.Rule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestCritters {

    @DisplayName("Test AcceptRule diff from bought")
    @Test
    public void testAcceptRule_diffFromBought() {
        AcceptRuleBean acc = new AcceptRuleBean(7, 2.0, RuleType.DFB.getKind()) ;
        SellRuleArgs args = new SellRuleArgs(1.9,0,0,0);
        assertEquals(false, acc.pass(args));
        SellRuleArgs args2 = new SellRuleArgs(2.1,0,0,0);
        assertEquals(true, acc.pass(args2));
    }

    @DisplayName("Test AcceptRule diff from watermark")
    @Test
    public void testAcceptRule_diffFromWatermark() {
        AcceptRuleBean acc = new AcceptRuleBean(6, 2.0, RuleType.DFW.getKind()) ;
        SellRuleArgs args = new SellRuleArgs(0,1.9,0,0);
        assertEquals(false, acc.pass(args));
        SellRuleArgs args2 = new SellRuleArgs(0,2.1,0,0);
        assertEquals(true, acc.pass(args2));
    }

    @DisplayName("Test DenyRule with memory")
    @Test
    public void testDenyRule_withMemory() {
        DenyRuleBean d = new DenyRuleBean(5, 12.0, RuleType.OP_ROOF.getKind(), "y", "y");
        SellRuleArgs args = new SellRuleArgs(0,0,0,11.9);
        assertEquals(true, d.block(args));
        SellRuleArgs args2 = new SellRuleArgs(0,0,0,12.1);
        assertEquals(true, d.block(args2));
    }
}
