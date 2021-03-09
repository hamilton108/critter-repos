package critterrepos.beans.options;

import oahu.exceptions.BinarySearchException;
import oahu.financial.StockOption;
import oahu.financial.StockOptionPrice;
import oahu.financial.OptionCalculator;
import oahu.financial.StockPrice;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class StockOptionPriceBean implements StockOptionPrice {
    private static boolean DEBUG = false;
    private StockOption stockOption;
    private StockPrice stockPrice;
    private double buy;
    private double sell;
    private int oid;
    private OptionCalculator calculator;

    public StockOptionPriceBean() {
    }

    public StockOptionPriceBean(StockPrice stockPrice,
                                StockOption stockOption,
                                double buy,
                                double sell,
                                OptionCalculator calculator) {
        this.stockPrice = stockPrice;
        this.stockOption = stockOption;
        this.buy = buy;
        this.sell = sell;
        this.calculator = calculator;
    }

    public StockOptionPriceBean(StockOption stockOption,
                                double buy,
                                double sell,
                                OptionCalculator calculator) {
        this.stockOption = stockOption;
        this.buy = buy;
        this.sell = sell;
        this.calculator = calculator;
    }
    @Override
    public double getBuy() {
        return buy;
    }

    @Override
    public double getSell() {
        return sell;
    }


    private Optional<Double> _breakEven = null;
    @Override
    public Optional<Double> getBreakEven() {
        try {
            if (_breakEven == null) {
                _breakEven = Optional.of(calculator.stockPriceFor(getSell(), this));
            }
        }
        catch (BinarySearchException ex) {
            System.out.println(String.format("[%s] %s",getTicker(),ex.getMessage()));
            _breakEven = Optional.empty();
        }
        return _breakEven;
    }

    private double _currentRiscOptionValue;
    private Double _currentRiscStockPrice = null;
    @Override
    public Optional<Double> stockPriceFor(double optionValue) {
        try {
            //Double result = calculator.stockPriceFor(getSell() - optionValue,this);
            Double result = calculator.stockPriceFor(optionValue,this);
            _currentRiscOptionValue = optionValue;
            _currentRiscStockPrice = result;
            return Optional.of(_currentRiscStockPrice);
        }
        catch (BinarySearchException ex) {
            System.out.println(String.format("[%s] %s",getTicker(),ex.getMessage()));
            return Optional.empty();
        }
    }

    @Override
    public double optionPriceFor(double curStockPrice) {
        double strike = stockOption.getX();
        double expiry = getDays()/365.0;
        Optional<Double> ivBuy = getIvBuy();
        if (ivBuy.isPresent()) {
            _currentRiscOptionValue = stockOption.getOpType() == StockOption.OptionType.CALL ?
                    calculator.callPrice( curStockPrice,strike,expiry,ivBuy.get()) :
                    calculator.putPrice( curStockPrice,strike,expiry,ivBuy.get());
            _currentRiscStockPrice =  curStockPrice;
            return _currentRiscOptionValue;
        }
        else {
            return -1;
        }
    }

    @Override
    public double getCurrentRiscOptionValue() {
        return _currentRiscOptionValue;
    }

    @Override
    public double getCurrentRisc() {
        /*
        System.out.println("getCurrentRisc sell: " + sell);
        System.out.println("getCurrentRisc _currentRiscOptionValue: " + _currentRiscOptionValue);
        */
        return sell - _currentRiscOptionValue;
    }

    @Override
    public Optional<Double> getCurrentRiscStockPrice(){
        if (_currentRiscStockPrice == null) {
            return Optional.empty();
        }
        else {
            return Optional.of(_currentRiscStockPrice);
        }
    }

    @Override
    public void resetRiscCalc() {
        _currentRiscStockPrice = null;
    }

    @Override
    public int getOid() {
        return oid;
    }

    @Override
    public int getStockId() {
        return stockPrice == null ? -1 : stockPrice.getStockId();
    }

    @Override
    public void setOid(int oid) {
        this.oid = oid;
    }

    @Override
    public String getTicker() {
        return stockOption == null ? null : stockOption.getTicker();
    }

    @Override
    public StockOption getDerivative() {
        return stockOption;
    }

    public void setDerivative(StockOption stockOption) {
        this.stockOption = stockOption;
    }

    public int getDerivativeId() {
        return stockOption == null ? -1 : stockOption.getOid();
    }

    @Override
    public StockPrice getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(StockPrice stockPrice) {
        this.stockPrice = stockPrice;
    }

    public int getStockPriceId() {
        return stockPrice == null ? -1 : stockPrice.getOid();
    }

    @Override
    public double getDays() {
        return getDerivative().getDays();
        /*
        if (DEBUG) {
            return 209;
        }
        else {
            LocalDate dx = getStockPrice().getLocalDx();

            //LocalDate x = getDerivative().getExpiry().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            LocalDate x = getDerivative().getExpiry();

            return ChronoUnit.DAYS.between(dx, x);
        }
         */
    }

    private Optional<Double> _ivBuy = null;
    @Override
    public Optional<Double> getIvBuy() {
        if (_ivBuy == null) {
            try {
                _ivBuy = Optional.of(calculator.iv(this, StockOption.BUY));
            }
            catch (BinarySearchException ex) {
                System.out.println(String.format("[%s] %s",getTicker(),ex.getMessage()));
                _ivBuy = Optional.empty();
            }
        }
        return _ivBuy;
    }

    private Optional<Double> _ivSell = null;
    @Override
    public Optional<Double> getIvSell() {
        if (_ivSell == null) {
            try {
                _ivSell = Optional.of(calculator.iv(this, StockOption.SELL));
            }
            catch (BinarySearchException ex) {
                System.out.println(String.format("[%s] %s",getTicker(),ex.getMessage()));
                _ivSell = Optional.empty();
            }
        }
        return _ivSell;
    }


    public void setBuy(double buy) {
        this.buy = buy;
    }

    public void setSell(double sell) {
        this.sell = sell;
    }

    public void setCalculator(OptionCalculator calculator) {
        this.calculator = calculator;
    }
}
