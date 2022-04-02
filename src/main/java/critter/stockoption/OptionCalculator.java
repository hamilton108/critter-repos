package critter.stockoption;

public interface OptionCalculator {

    double delta(StockOptionPrice d);
    double spread(StockOptionPrice d);
    double breakEven(StockOptionPrice d);
    double stockPriceFor(double optionPrice, StockOptionPrice o);
    double iv(StockOptionPrice d, int priceType);

    double ivCall(double spot, double strike, double yearsExpiry, double optionPrice);
    double ivPut(double spot, double strike, double yearsExpiry, double optionPrice);
    double callPrice(double spot, double strike, double yearsExpiry, double sigma);
    double putPrice(double spot, double strike, double yearsExpiry, double sigma);
}
