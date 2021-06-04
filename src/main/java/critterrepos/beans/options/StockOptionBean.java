package critterrepos.beans.options;

import critterrepos.utils.StockOptionUtils;
import oahu.financial.StockOption;
import oahu.financial.Stock;

import java.sql.Date;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.time.temporal.ChronoUnit.DAYS;

public class StockOptionBean implements StockOption {

    private StockOptionUtils stockOptionUtils;
    private final Pattern p = Pattern.compile("\\D+(\\d\\D)\\d+");
    private LocalDate currentDate;
    private Stock stock;


    public StockOptionBean() {
    }


    public StockOptionBean(String ticker,
                           OptionType opType,
                           double x,
                           Stock stock,
                           StockOptionUtils stockOptionUtils) {
        this.ticker = ticker;
        this.opType = opType;
        this.x = x;
        this.stock = stock;
        this.stockOptionUtils = stockOptionUtils;
    }

    /*
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder(getTicker());
           buf.append("\n\texpiry: ").append(getExpiry())
           .append("\n\toption type: ").append(getOpType() == OptionType.CALL ? "apply" : "put")
           .append("\n\tx: ").append(getX())
           //.append("\n\tbuy ").append(getBuy())
           //.append("\n\tsell ").append(getSell())
           .append("\n\tseries ").append(getSeries());
           //.append("\n\twatermark ").append(getWatermark());
        return buf.toString();
    }
    //*/

    //--------------------------------------------------
    //------------- Parent
    //--------------------------------------------------
    @Override
    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }
    private Integer _stockId = null;
    public int getStockId() {
        return stock == null ? _stockId : stock.getOid();
    }
    public void setStockId(int value) {
        _stockId = value;
    }


    //--------------------------------------------------
    //------------- Id
    //--------------------------------------------------
    private int oid;
    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    //--------------------------------------------------
    //------------- Expiry
    //--------------------------------------------------
    private LocalDate expiry;
    @Override
    public LocalDate getExpiry() {
        if (expiry == null) {
            String series = getSeries();
            expiry = stockOptionUtils.seriesAsDate(series);
        }
        return expiry;
    }
    public void setExpiry(LocalDate value) {
        expiry = value;
    }

    public Date getExpirySql() {
        return Date.valueOf(expiry);
    }

    public void setExpirySql(Date expirySql) {
        this.expiry = expirySql.toLocalDate();
    }

    //--------------------------------------------------
    //------------- OpType
    //--------------------------------------------------
    private OptionType opType;
    @Override
    public OptionType getOpType() {
        return opType;
    }


    public void setOpType(OptionType value) {
            opType = value;
    }

    public String getOpTypeStr() {
        return getOpType() == OptionType.CALL ? "c" : "p";
    }
    public void setOpTypeStr(String value) {
        if (value.equals("c")) {
            setOpType(OptionType.CALL);
        }
        else {
            setOpType(OptionType.PUT);
        }
    }

    //--------------------------------------------------
    //--------------- Life Cycle -----------------------
    //--------------------------------------------------
    private LifeCycle lifeCycle;
    @Override
    public LifeCycle getLifeCycle() {
        return lifeCycle;
    }
    public void setLifeCycle(LifeCycle lifeCycle) {
        this.lifeCycle = lifeCycle;
    }


    //--------------------------------------------------
    //------------- Series
    //--------------------------------------------------

    private String series;
    public String getSeries() {
        if (series == null) {
            Matcher m = p.matcher(getTicker());
            if (m.find()) {
                series = m.group(1);
            }
            else {
                //series = "??";
                throw new RuntimeException("Series undefined for " + ticker);
            }
        }
        return series;
    }

    @Override
    public long getDays() {
        return DAYS.between(getCurrentDate(),getExpiry());
    }

    public void setSeries(String value) {
        series = value;
    }

    //--------------------------------------------------
    //------------- X
    //--------------------------------------------------
    private double x;
    @Override
    public double getX() {
        return x;
    }
    public void setX(double value) {
        x = value;
    }


    //--------------------------------------------------
    //------------- Ticker
    //--------------------------------------------------
    private String ticker;
    public String getTicker() {
        return ticker;
    }



    public void setTicker(String value) {
        ticker = value;
    }

    public void setStockOptionUtils(StockOptionUtils stockOptionUtils) {
        this.stockOptionUtils = stockOptionUtils;
    }

    public LocalDate getCurrentDate() {
        if (currentDate == null) {
            //currentDate = getExpiry().minusDays(60);
            currentDate = stockOptionUtils.getCurrentDate();
        }
        return currentDate;
    }

    public void setCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
    }
}
