package critterrepos.beans.options;

import oahu.financial.StockOption;
import oahu.financial.StockPrice;
import oahu.financial.OptionCalculator;

import java.util.Optional;

public class SimpleStockOptionPriceBean extends StockOptionPriceBean {
    public SimpleStockOptionPriceBean() {
    }

    public SimpleStockOptionPriceBean(StockPrice stockPrice,
                                      StockOption stockOption,
                                      double buy,
                                      double sell) {
        super(stockPrice, stockOption, buy, sell, null);
    }

    public SimpleStockOptionPriceBean(StockPrice stockPrice,
                                      StockOption stockOption,
                                      double buy,
                                      double sell,
                                      OptionCalculator calculator) {
        super(stockPrice, stockOption, buy, sell, calculator);
    }

    @Override
    public Optional<Double> getIvBuy() {
        return Optional.of(0.0);
    }
    @Override
    public Optional<Double> getIvSell() {
        return Optional.of(0.0);
    }
}
