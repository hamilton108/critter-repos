package critter.repos;

import critter.stock.Stock;
import critter.stockoption.StockOption;

import java.util.Optional;

public interface StockMarketRepository<T,T2> {
    Stock findStock(T stockInfo);
    Optional<StockOption> findStockOption(T2 stockOptInfo);
}
