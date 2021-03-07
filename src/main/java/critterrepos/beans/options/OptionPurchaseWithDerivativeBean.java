package critterrepos.beans.options;

import oahu.financial.StockOptionPrice;

import java.util.Optional;

public class OptionPurchaseWithDerivativeBean extends OptionPurchaseBean {
    private StockOptionPrice stockOptionPrice;


    public Optional<StockOptionPrice> getDerivativePrice() {
        return Optional.of(stockOptionPrice);
    }

    public void setDerivativePrice(StockOptionPrice stockOptionPrice) {
        this.stockOptionPrice = stockOptionPrice;
    }

    @Override
    public String getOptionName() {
        return stockOptionPrice.getDerivative().getTicker();
    }

    @Override
    public int getOptionId() {
        return stockOptionPrice.getOid();
    }

    @Override
    public double getPrice() {
        return stockOptionPrice.getSell();
    }
    @Override
    public double getBuyAtPurchase() {
        return stockOptionPrice.getBuy();
    }

    @Override
    public String toString() {
        return String.format("[ %s ] Op.id: %d, cat: %d, date: %s, vol: %d",
                stockOptionPrice.getDerivative().getTicker(),
                stockOptionPrice.getOid(),
                getPurchaseType(),
                getDx(),
                getVolume());
    }
}
