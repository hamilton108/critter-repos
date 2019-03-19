package critterrepos.models.impl;

import oahu.financial.Stock;
import oahu.financial.StockPrice;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CachedStockMarketReposImpl extends StockMarketReposImpl {
    private HashMap<String, Collection<StockPrice>> prices = new HashMap<>();
    private Collection<Stock> stocks;
    private Map<Integer,String> tixMap;


    public void emptyCache() {
        prices = new HashMap<>();
    }

    @Override
    public Collection<StockPrice> findStockPrices(String ticker, LocalDate fromDx) {
        Collection<StockPrice> curPrices = prices.get(ticker);

        if (curPrices == null) {
            curPrices = super.findStockPrices(ticker, fromDx);
            prices.put(ticker, curPrices);
        }
        else {
        }
        return curPrices;
    }


    @Override
    public Collection<Stock> getStocks() {
        if (stocks == null) {
            stocks = super.getStocks();
        }
        return stocks;
    }

    @Override
    public String getTickerFor(int stockId) {
        if (tixMap == null) {
            Collection<Stock> sx = getStocks();
            tixMap = new HashMap<>();
            for (Stock s : sx) {
                tixMap.put(s.getOid(),s.getTicker());
            }
        }
        return tixMap.get(stockId);
    }
}
