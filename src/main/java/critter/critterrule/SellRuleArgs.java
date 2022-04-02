package critter.critterrule;

public class SellRuleArgs {
    private final double diffFromBought;
    private final double diffFromWatermark;
    private final double spot;
    private final double optionPrice;

    public SellRuleArgs(double diffFromBought,
                        double diffFromWatermark,
                        double spot,
                        double optionPrice) {
        this.diffFromBought = diffFromBought;
        this.diffFromWatermark = diffFromWatermark;
        this.spot = spot;
        this.optionPrice = optionPrice;
    }

    public double getDiffFromWatermark() {
        return diffFromWatermark;
    }

    public double getDiffFromBought() {
        return diffFromBought;
    }

    public double getSpot() {
        return spot;
    }

    public double getOptionPrice() {
        return optionPrice;
    }

    @Override
    public String toString(){
        return String.format("[SellRuleArgs] DFB: %.2f, DFW: %.2f, option price: %.2f, spot: %.2f",
                diffFromBought, diffFromWatermark, optionPrice, spot);
    }
}
