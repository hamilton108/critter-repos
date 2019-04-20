package critterrepos.beans.options;

import oahu.dto.Tuple;
import oahu.dto.Tuple3;
import oahu.financial.*;
import oahu.financial.critters.Critter;
import oahu.financial.critters.SellRuleArgs;
import oahu.financial.repository.ChachedEtradeRepository;
import critterrepos.beans.critters.CritterBean;
import oahu.financial.repository.EtradeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

public class OptionPurchaseBean implements OptionPurchase {
    Logger log = LoggerFactory.getLogger("critterrepos.beans.options");
    //region Init
    private OptionCalculator calculator;
    private int oid;
    private LocalDate localDx;
    private int status;
    private double price;
    private double buyAtPurchase;
    private long volume;

    private String ticker;
    private String optionName;
    private String optionType;

    private int optionId;
    private int purchaseType;
    private double spotAtPurchase;
    //private Derivative myDerivative;

    // private EtradeRepository<Tuple<String>,Tuple2<String,File>> repository;
    private EtradeRepository<Tuple<String>> repository;
    private List<CritterBean> critters;
    private List<OptionSaleBean> sales;

    public OptionPurchaseBean() {}

    //endregion Init

    //region Properties
    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    //region Dates
    public Date getDx() {
        return Date.valueOf(getLocalDx());
    }

    public void setDx(Date dx) {
        this.localDx = dx.toLocalDate();
    }
    public LocalDate getLocalDx() {
        if (localDx == null) {
            localDx = java.time.LocalDate.now();
        }
        return localDx;
    }

    public void setLocalDx(LocalDate localDx) {
        this.localDx = localDx;
    }

    //endregion Dates
    //--------------------------------------------------
    //------------- Expiry
    //--------------------------------------------------
    private LocalDate expiry;
    public LocalDate getExpiry() {
        return expiry;
    }

    public Date getExpirySql() {
        return Date.valueOf(expiry);
    }

    public void setExpirySql(Date expirySql) {
        this.expiry = expirySql.toLocalDate();
    }
    //--------------------------------------------------
    //------------- X
    //--------------------------------------------------
    private double x;
    public double getX() {
        return x;
    }

    public void setX(double value) {
        x = value;
    }
    //--------------------------------------------------
    //------------- ivBuy
    //--------------------------------------------------
    /*
    private Optional<Double> _ivBuy = null;
    public Optional<Double> getIvBuy() {
        if (_ivBuy == null) {
            try {
                //_ivBuy = Optional.of(calculator.iv(this,Derivative.BUY));
            }
            catch (BinarySearchException ex) {
                System.out.println(String.format("[%s] %s",getTicker(),ex.getMessage()));
                _ivBuy = Optional.empty();
            }
        }
        return _ivBuy;
    }
    //*/


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public List<CritterBean> getCritters() {
        if (critters == null) {
            critters = new ArrayList<>();
        }
        return critters;
    }

    public void setCritters(List<CritterBean> critters) {
        this.critters = critters;
    }

    public List<OptionSaleBean> getSales() {
        return sales;
    }
    public void setSales(List<OptionSaleBean> sales) {
        this.sales = sales;
    }
    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    /*
    public String getOptionName() {
        return optionName;
    }
    */

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public int getOptionId() {
        return optionId;
    }
    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public int getPurchaseType() {
        return purchaseType;
    }
    public void setPurchaseType(int purchaseType) {
        this.purchaseType = purchaseType;
    }

    public double getSpotAtPurchase() {
        return spotAtPurchase;
    }
    public void setSpotAtPurchase(double spotAtPurchase) {
        this.spotAtPurchase = spotAtPurchase;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getBuyAtPurchase() {
        return buyAtPurchase;
    }

    public void setBuyAtPurchase(double buyAtPurchase) {
        this.buyAtPurchase = buyAtPurchase;
    }

    public String getOptionType() {
        return optionType;
    }

    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }

    public EtradeRepository getRepository() {
        return repository;
    }

    public void setRepository(EtradeRepository repository) {
        this.repository = repository;
    }

    public void setCalculator(OptionCalculator calculator) {
        this.calculator = calculator;
    }
    //endregion Properties

    //region Diverse
    /*
    public Optional<DerivativePrice> getDerivativePrice() {
        return repository.findDerivativePrice(new Tuple<>(ticker,optionName));
    }
    */
    public Optional<StockPrice>  getSpot() {
        return repository.stockPrice(ticker);
    }
    private Double _watermark = null;
    public Double getWatermark() {
        Optional<DerivativePrice> curDeriv = getDerivativePrice();

        if (!curDeriv.isPresent()) return null;

        DerivativePrice price = curDeriv.get();
        if ((_watermark == null) || (price.getBuy() > _watermark)) {
            _watermark = price.getBuy();
        }
        //region Obsolete
        /*
        if (_watermark == null) {
            _watermark = curDeriv.getBuy();
        }
        else {
            if (curDeriv.getBuy() > _watermark) {
                _watermark = curDeriv.getBuy();
            }
        }
        */
        //endregion Obsolete
        return _watermark;
    }

    /*
    public Derivative getMyDerivative() {
        return myDerivative;
    }

    public void setMyDerivative(Derivative myDerivative) {
        this.myDerivative = myDerivative;
    }
    //*/


    //endregion

    //region Utility Methods
    public long remainingVolume() {
        return volume - volumeSold() - volumeActiveCritters();
    }
    public long volumeActiveCritters() {
        if ((critters == null) || (critters.size() == 0)) return 0;

        return critters.stream().mapToLong(crit -> {
            if (crit.getStatus() == 7) {
                return crit.getSellVolume();
            }
            else {
                return 0;
            }
        }).sum();
    }
    public long volumeSold() {
        if ((sales == null) || (sales.size() == 0)) return 0;

        return sales.stream().mapToLong(OptionSaleBean::getVolume).sum();
    }
    public void addSale(OptionSaleBean sale) {
        if (sales == null) {
            sales = new ArrayList<>();
        }
        sales.add(sale);
        if (isFullySold()) {
            status = 2;
        }
    }
    public boolean isFullySold() {
        return volumeSold() < volume ? false : true;
    }

    private Optional<SellRuleArgs> collectArgs() {
        Optional<DerivativePrice> dprice = getDerivativePrice();
        if (dprice.isPresent() == false) {
            return Optional.empty();
        }
        Optional<StockPrice> spot = getSpot();
        if (spot.isPresent() == false) {
            return Optional.empty();
        }

        DerivativePrice p = dprice.get();

        if ((_watermark == null) || (p.getBuy() > _watermark)) {
            log.info("Changing watermark from {} to {}",_watermark, p.getBuy());
            _watermark = p.getBuy();
        }
        double dfb = getPrice() - p.getBuy();
        double dfw = _watermark - p.getBuy();

        SellRuleArgs result = new SellRuleArgs(dfb,dfw,spot.get().getCls(),getPrice());
        return Optional.of(result);
    }

    public void inspect() {
        System.out.println("\n\n********************************* Critters **********************************");
        System.out.println(String.format("[%d] Option name: %s, price: %.2f, volume: %d, rem.vol: %d", oid, optionName, price, volume, remainingVolume()));
        if (critters == null || critters.size() == 0) {
            System.out.println("\tNo critters");
        }
        else {
            for (Critter c : critters) {
                c.inspect();
            }
        }
    }

    //endregion Utility Methods

    //region interface OptionPurchase
    @Override
    public List<Critter> acceptedForSale() {
        List<Critter> result = new ArrayList<>();
        if (isFullySold()) {
            return result;
        }
        List<CritterBean> crits = getCritters().stream().filter(c -> c.getStatus() == 7).collect(Collectors.toList());
        Optional<SellRuleArgs> args = collectArgs();
        if (args.isPresent() == false) {
            return result;
        }
        crits.forEach(c -> {
            if (c.apply(args.get())) {
                result.add(c);
            }
        });
        return result;
    }

    @Override
    public String getOptionName() {
        return optionName;
    }

    @Override
    public Optional<DerivativePrice> getDerivativePrice() {
        return repository.findDerivativePrice(new Tuple<>(ticker,optionName));
    }
    //endregion
}
