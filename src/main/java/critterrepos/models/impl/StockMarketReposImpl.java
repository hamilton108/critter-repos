package critterrepos.models.impl;

import critterrepos.beans.StockPriceBean;
import critterrepos.beans.options.DerivativeBean;
import critterrepos.beans.options.OptionPurchaseBean;
import critterrepos.beans.options.OptionPurchaseWithDerivativeBean;
import critterrepos.mybatis.CritterMapper;
import critterrepos.mybatis.DerivativeMapper;
import critterrepos.mybatis.StockMapper;
import critterrepos.utils.MyBatisUtils;
import oahu.exceptions.FinancialException;
import oahu.financial.*;
import oahu.financial.repository.StockMarketRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;

@Component
public class StockMarketReposImpl implements StockMarketRepository {
    private HashMap<Integer,Stock> idLookup;
    private HashMap<String,Stock> tickerLookup;
    private List<Stock> stocks;

    //public void setMybatisConfigFile(String mybatisConfigFile) {
    //  MyBatisUtils.setConfigFile(mybatisConfigFile);
    //}

    /*
    private final SqlSession session;

    @Autowired
    public StockMarketReposImpl(SqlSession session) {
        this.session = session;
    }

     */

    @Override
    public void insertDerivative(Derivative derivative, Consumer<Exception> errorHandler) {
        MyBatisUtils.withSessionConsumer((session) -> {
            DerivativeBean bean = (DerivativeBean) derivative;
            DerivativeMapper dmapper = session.getMapper(DerivativeMapper.class);
            dmapper.insertDerivative(bean);
        });
    }

    @Override
    public Optional<Derivative> findDerivative(String derivativeTicker) {
        return MyBatisUtils.withSession((session) -> {
            if (idLookup == null) {
                populate(session);
            }

            DerivativeMapper mapper = session.getMapper(DerivativeMapper.class);
            DerivativeBean result = mapper.findDerivative(derivativeTicker);

            if (result == null) {
                return Optional.empty();
            }
            result.setStock(idLookup.get(result.getStockId()));

            return Optional.of(result);
        });
    }

    @Override
    public Stock findStock(String ticker) {
        if (tickerLookup == null) {
            MyBatisUtils.withSessionConsumer(this::populate);
        }
        return tickerLookup.get(ticker);
    }

    @Override
    public Collection<Stock> getStocks() {
        if (stocks == null) {
            MyBatisUtils.withSessionConsumer(this::populate);
        }
        return stocks;
    }

    @Override
    public Collection<StockPrice> findStockPrices(String ticker, LocalDate fromDx) {
        return MyBatisUtils.withSession((session) -> {
            System.out.println("Fetching stockprices...");
            if (tickerLookup == null) {
                populate(session);
            }
            Stock stock = tickerLookup.get(ticker);
            if (stock == null) {
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
        return MyBatisUtils.withSession((session) -> {
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

            CritterMapper dmapper = session.getMapper(CritterMapper.class);
            dmapper.insertPurchase(result);

            //(.setOptionId (.getOid d))
            //(.setDx (java.util.Date.))
            //(.setPrice price)
            //(.setVolume volume)
            //(.setStatus 1)
            //(.setPurchaseType 3)
            //(.setSpotAtPurchase spot-at-purchase)
            //(.setBuyAtPurchase buy-at-purchase))

            return result;
        });
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
    public List<OptionPurchase> purchasesWithSalesAll(
        int purchaseType,
        int status,
        Derivative.OptionType ot) {
        return MyBatisUtils.withSession((session) -> {
            return session.getMapper(CritterMapper.class).purchasesWithSalesAll(
                    purchaseType,
                    status,
                    null);
        });
    }

    @Override
    public String getTickerFor(int oid) {
        if (idLookup == null) {
            MyBatisUtils.withSessionConsumer(this::populate);
        }
        Stock stock = idLookup.get(oid);
        return stock.getTicker();
    }

    private void populate(SqlSession session) {
        idLookup = new HashMap<>();
        tickerLookup = new HashMap<>();
        stocks = new ArrayList<>();

        StockMapper mapper = session.getMapper(StockMapper.class);

        List<Stock> tix = mapper.selectStocks();
        for (Stock b : tix) {
            idLookup.put(b.getOid(), b);
            tickerLookup.put(b.getTicker(), b);
            stocks.add(b);
        }
    }
}
