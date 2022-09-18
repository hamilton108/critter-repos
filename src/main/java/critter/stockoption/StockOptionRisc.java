package critter.stockoption;

public class StockOptionRisc {
    private final double stockPrice;
    private final double optionPrice;

    private final double volatility;
    private final String optionTicker;

    public StockOptionRisc(String optionTicker,
                           double stockPrice,
                           double optionPrice,
                           double volatility) {
        this.optionTicker = optionTicker;
        this.stockPrice = stockPrice;
        this.optionPrice = optionPrice;
        this.volatility = volatility;
    }

    public double getStockPrice() {
        return stockPrice;
    }

    public double getOptionPrice() {
        return optionPrice;
    }

    public String getOptionTicker() {
        return optionTicker;
    }

    public double getVolatility() {
        return volatility;
    }
}
