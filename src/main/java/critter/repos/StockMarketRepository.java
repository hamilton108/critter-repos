package critter.repos;

import critter.stock.Stock;
import critter.stockoption.StockOption;
import critter.stockoption.StockOptionPurchase;

import java.util.List;
import java.util.Optional;

public interface StockMarketRepository<T,T2> {
    Stock findStock(T stockInfo);
    Optional<StockOption> findStockOption(T2 stockOptInfo);
    List<StockOptionPurchase> activePurchasesWithCritters(int purchaseType);
    List<StockOptionPurchase> purchasesWithSalesAll(int purchaseType, int status, StockOption.OptionType ot);
}
