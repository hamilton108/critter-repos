package critterrepos.beans;

import oahu.financial.StockOptionPrice;
import oahu.financial.Stock;
import oahu.financial.StockPrice;
import oahu.financial.StockTicker;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

//import org.joda.time.DateMidnight;

public class StockPriceBean implements StockPrice {
    //private SimpleStringProperty ticker = new SimpleStringProperty();
    private LocalDate localDx;
    private LocalTime tm;
    private double opn;
    private double hi;
    private double lo;
    private double cls;
    private double marketValue;
    private long volume;
    private int oid = -1;
    private List<StockOptionPrice> optionPrices;


    public StockPriceBean() {
    }

    public StockPriceBean(Date dx,
                          double opn,
                          double hi,
                          double lo,
                          double cls,
                          long volume) {
        this(dx.toLocalDate(), opn, hi, lo, cls, volume);
    }
    public StockPriceBean(LocalDate dx,
                          double opn,
                          double hi,
                          double lo,
                          double cls,
                          long volume) {
        this.localDx = dx;
        this.opn = opn;
        this.hi = hi;
        this.lo = lo;
        this.cls = cls;
        this.volume = volume;
    }
    public StockPriceBean(LocalDate dx,
                          LocalTime tm,
                          double opn,
                          double hi,
                          double lo,
                          double cls,
                          long volume) {
        this(dx,opn,hi,lo,cls,volume);
        this.setTm(tm);
    }


    private Stock stock = null;
    public void setStock(Stock stock) {
        this.stock = stock;
    }
    @Override
    public Stock getStock() {
        return stock;
    }

    @Override
    public int getStockId() {
        return stock == null ? -1 : stock.getOid();
    }
    @Override
    public void assign(StockPrice other) {
        this.setCls(other.getCls());
        this.setHi(other.getHi());
        this.setLo(other.getLo());
        this.setOpn(other.getOpn());
        this.setVolume(other.getVolume());
    }

    @Override
    public List<StockOptionPrice> getOptionPrices() {
        return optionPrices;
    }
    public void setOptionPrices(List<StockOptionPrice> value) {
        this.optionPrices = value;
    }


    //region Time

    @Override
    public LocalTime getTm() {
        return tm;
    }
    public void setTm(LocalTime tm) {
        this.tm = tm;
    }
    public Time getSqlTime() {
        return new Time(tm.getHour(), tm.getMinute(), tm.getSecond());
    }

    public Date getDx() {
        return Date.valueOf(localDx);
    }

    public void setDx(Date value) {
        localDx = value.toLocalDate();
    }

    @Override
    public LocalDate getLocalDx() {
       return localDx;
    }

    public void setLocalDx(LocalDate value) {
        this.localDx = value;
    }
    //endregion Time


    @Override
    public String toString() {
        return String.format("[%s] dx: %s, tm: %s",
                stock == null ? "-" :stock.getTicker(),
                localDx,
                tm);
    }

    @Override
    public double getOpn() {
        return opn;
    }
    public void setOpn(double value) {
        opn = value;
    }

    @Override
    public double getHi() {
        return hi;
    }
    public void setHi(double value) {
        hi = value;
    }

    @Override
    public double getLo() {
        return lo;
    }
    public void setLo(double value) {
        lo = value;
    }

    @Override
    public double getCls() {
        return cls;
    }
    public void setCls(double value) {
        cls = value;
    }

    @Override
    public long getVolume() {
        return volume;
    }
    public void setVolume(long value) {
        volume = value;
    }

    @Override
    public double getValue() {
        return cls;
    }

    @Override
    public double getMarketValue() {
        return marketValue;
    }
    public void setMarketValue(double value) {
        marketValue = value;
    }
    @Override
    public int getOid() {
        return oid;
    }

    //region Non-interface Properties
    public void setOid(int oid) {
        this.oid = oid;
    }

    public String getTicker() {
        return getStock() == null ? "N/A": getStock().getTicker();
    }

    public int getTickerId() {
        return getStock().getOid();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;

        if (!(obj instanceof StockPriceBean)) return false;

        if (obj == this) return true;

        StockPriceBean other = (StockPriceBean)obj;

        if (!localDx.isEqual(other.getLocalDx())) {
            return false;
        }
        if (tm != null) {
            if (other.getTm() == null)   {
                return false;
            }
            if (!tm.equals(other.getTm())) {
                return false;
            }
        }
        if (!isEqual(opn,other.getOpn())) {
            return false;
        }
        if (!isEqual(hi,other.getHi())) {
            return false;
        }
        if (!isEqual(lo,other.getLo())) {
            return false;
        }
        if (!isEqual(cls,other.getCls())) {
            return false;
        }
        if (volume != other.volume) {
            return false;
        }
        return true;
    }
    private static final double TOLERANCE = 0.01;
    private boolean isEqual(double v1, double v2) {
        double diff = Math.abs(v1 - v2);
        return diff < TOLERANCE;
    }
    //endregion
}
