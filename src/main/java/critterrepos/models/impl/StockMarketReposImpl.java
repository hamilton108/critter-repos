package critterrepos.models.impl;

import oahu.exceptions.FinancialException;
import oahu.financial.*;
import oahu.financial.repository.StockMarketRepository;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import critterrepos.beans.options.DerivativeBean;
import critterrepos.beans.options.OptionPurchaseBean;
import critterrepos.beans.options.OptionPurchaseWithDerivativeBean;
import critterrepos.beans.StockPriceBean;
import critterrepos.models.mybatis.CritterMapper;
import critterrepos.models.mybatis.DerivativeMapper;
import critterrepos.models.mybatis.StockMapper;
import critterrepos.utils.MyBatisUtils;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class StockMarketReposImpl implements StockMarketRepository {
    Logger log = Logger.getLogger(getClass().getPackage().getName());
    private HashMap<Integer,Stock> idLookup;
    private HashMap<String,Stock> tickerLookup;
    private List<Stock> stocks;

    public void setMybatisConfigFile(String mybatisConfigFile) {
       MyBatisUtils.setConfigFile(mybatisConfigFile);
    }

    @Override
    public void insertDerivative(Derivative derivative, Consumer<Exception> errorHandler) {
        DerivativeBean bean = (DerivativeBean)derivative;

        MyBatisUtils.withSessionConsumer((session) -> {
            DerivativeMapper dmapper = session.getMapper(DerivativeMapper.class);
            dmapper.insertDerivative(bean);
        }, errorHandler);
    }

    @Override
    public Optional<Derivative> findDerivative(String derivativeTicker) {
        if (idLookup == null) {
            populate();
        }

        Function<SqlSession,Derivative> c = (session) -> {
            DerivativeMapper mapper = session.getMapper(DerivativeMapper.class);
            Derivative result = mapper.findDerivative(derivativeTicker);

            if (result == null) {
                log.warn(String.format("[%s] No derivative in database", derivativeTicker));
                return null;
            }
            ((DerivativeBean) result).setStock(idLookup.get(((DerivativeBean) result).getStockId()));
            return result;
        };

        Derivative retResult = MyBatisUtils.withSession(c);
        if (retResult == null) {
            return Optional.empty();
        }
        else {
            return Optional.of(retResult);
        }
    }

    @Override
    public Stock findStock(String ticker) {
        if (tickerLookup == null) {
            populate();
        }
        return tickerLookup.get(ticker);
    }

    @Override
    public Collection<Stock> getStocks() {
        if (stocks == null) {
            populate();
        }
        return stocks;
    }

    @Override
    public Collection<StockPrice> findStockPrices(String ticker, LocalDate fromDx) {
        log.info(String.format("[findStockPrices] ticker: %s, fromDx: %s", ticker, fromDx));
        if (tickerLookup == null) {
            populate();
        }
        return MyBatisUtils.withSession((session) -> {
            Stock stock = tickerLookup.get(ticker);
            if (stock == null) {
                log.warn(String.format("[%s] No stock in database", ticker));
                return null;
            }
            StockMapper mapper = session.getMapper(StockMapper.class);
            List<StockPrice> result = mapper.selectStockPrices(stock.getOid(), java.sql.Date.valueOf(fromDx));
            for (StockPrice p : result) {
                ((StockPriceBean) p).setStock(stock);
            }
            return result;
        });
    }

    @Override
    public void registerOptionPurchase(DerivativePrice purchase, int purchaseType, int volume) {
        MyBatisUtils.withSessionConsumer((session) -> {
            DerivativeMapper dmapper = session.getMapper(DerivativeMapper.class);
            DerivativeBean dbBean = dmapper.findDerivative(purchase.getDerivative().getTicker());
            if (dbBean == null) {
                dbBean = new DerivativeBean();
                dbBean.setTicker(purchase.getDerivative().getTicker());
                dbBean.setExpiry(purchase.getDerivative().getExpiry());
                dbBean.setOpTypeStr(purchase.getDerivative().getOpTypeStr());
                dbBean.setStock(purchase.getDerivative().getStock());
                dmapper.insertDerivative(dbBean);
            }
            CritterMapper cmapper = session.getMapper(CritterMapper.class);
            OptionPurchaseWithDerivativeBean newPurchase = new OptionPurchaseWithDerivativeBean();
            purchase.setOid(dbBean.getOid());
            //newPurchase.setDerivative(purchase.getDerivative());
            //newPurchase.setDx(new java.sql.Date());
            newPurchase.setLocalDx(LocalDate.now());
            newPurchase.setVolume(volume);
            newPurchase.setStatus(1);
            newPurchase.setPurchaseType(purchaseType);
            newPurchase.setSpotAtPurchase(purchase.getStockPrice().getCls());
            cmapper.insertPurchase(newPurchase);
        });
    }

    @Override
    public OptionPurchase registerOptionPurchase(int purchaseType, String opName, double price, int volume, double spotAtPurchase, double buyAtPurchase)
            throws FinancialException {
        Optional<Derivative> derivative = findDerivative(opName);
        if (!derivative.isPresent()) {
            throw new FinancialException(String.format("Could not find derivative: %s", opName));
        }
        OptionPurchaseBean result = new OptionPurchaseBean();
        result.setOptionId(derivative.get().getOid());
        result.setLocalDx(LocalDate.now());
        result.setPrice(price);
        result.setVolume(volume);
        result.setStatus(1);
        result.setPurchaseType(purchaseType);
        result.setSpotAtPurchase(spotAtPurchase);
        result.setBuyAtPurchase(buyAtPurchase);

        MyBatisUtils.withSessionConsumer((session) -> {
            CritterMapper dmapper = session.getMapper(CritterMapper.class);
            dmapper.insertPurchase(result);
        });

        /*
        (.setOptionId (.getOid d))
        (.setDx (java.util.Date.))
        (.setPrice price)
        (.setVolume volume)
        (.setStatus 1)
        (.setPurchaseType 3)
        (.setSpotAtPurchase spot-at-purchase)
        (.setBuyAtPurchase buy-at-purchase))
        */
        return result;
    }


    @Override
    public Collection<SpotOptionPrice> findOptionPrices(int opxId) {
        return MyBatisUtils.withSession((session) -> {
            return session.getMapper(DerivativeMapper.class).spotsOpricesOpxId(opxId);
        });
    }

    @Override
    public Collection<SpotOptionPrice> findOptionPricesStockId(int stockId,
                                                               LocalDate fromDate,
                                                               LocalDate toDate) {
        return MyBatisUtils.withSession((session) -> {
            return session.getMapper(DerivativeMapper.class).spotsOpricesStockId(stockId,
                    Date.valueOf(fromDate),
                    Date.valueOf(toDate));
        });
    }

    @Override
    public Collection<SpotOptionPrice> findOptionPricesStockIds(List<Integer> stockIds,
                                                                LocalDate fromDate,
                                                                LocalDate toDate) {
        return MyBatisUtils.withSession((session) -> {
            return session.getMapper(DerivativeMapper.class).spotsOpricesStockIds(stockIds,
                    Date.valueOf(fromDate),
                    Date.valueOf(toDate));
        });
    }

    @Override
    public Collection<SpotOptionPrice> findOptionPricesStockTix(List<String> stockTix,
                                                                LocalDate fromDate,
                                                                LocalDate toDate) {
        return MyBatisUtils.withSession((session) -> {
            return session.getMapper(DerivativeMapper.class).spotsOpricesStockTix(stockTix,
                    Date.valueOf(fromDate),
                    Date.valueOf(toDate));
        });
    }

    @Override
    public String getTickerFor(int oid) {
        return null;
    }

    private void populate() {
        idLookup = new HashMap<>();
        tickerLookup = new HashMap<>();
        stocks = new ArrayList<>();

        MyBatisUtils.withSessionConsumer((session) -> {
            StockMapper mapper = session.getMapper(StockMapper.class);

            List<Stock> tix = mapper.selectStocks();
            for (Stock b : tix) {
                idLookup.put(b.getOid(), b);
                tickerLookup.put(b.getTicker(), b);
                stocks.add(b);
            }
        });
    }
}
