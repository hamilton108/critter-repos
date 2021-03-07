package critterrepos.mybatis;

import critterrepos.beans.StockPriceBean;
import critterrepos.beans.options.StockOptionBean;
import critterrepos.beans.options.StockOptionPriceBean;
import critterrepos.beans.options.SpotOptionPriceSummaryBean;
import oahu.financial.SpotOptionPrice;
import oahu.financial.Stock;
import org.apache.ibatis.annotations.Param;

import java.sql.Date;
import java.util.List;


public interface DerivativeMapper {
    void insertSpot(StockPriceBean s);
    void insertDerivativePrice(StockOptionPriceBean d);
    void insertDerivative(StockOptionBean d);

    int countDerivative(String ticker);

    int countIvForSpot(StockPriceBean s);

    int countOpxPricesForSpot(StockPriceBean s);

    Integer findSpotId(StockPriceBean s);

    Integer findDerivativeId(String ticker);

    StockOptionBean findDerivative(String ticker);

    List<SpotOptionPrice> spotsOprices();
    List<SpotOptionPrice> spotsOpricesOpxId(@Param("opxId") int opxId);
    List<SpotOptionPrice> spotsOpricesStockId(@Param("stockId") int stockId,
                                                  @Param("fromDx") Date fromDx,
                                                  @Param("toDx") Date toDx);
    List<SpotOptionPrice> spotsOpricesStockIds(@Param("stockIds") List<Integer> stockIds,
                                              @Param("fromDx") Date fromDx,
                                              @Param("toDx") Date toDx);
    List<SpotOptionPrice> spotsOpricesStockTix(@Param("stockTix") List<String> stockTix,
                                               @Param("fromDx") Date fromDx,
                                               @Param("toDx") Date toDx);
    List<SpotOptionPriceSummaryBean> spotOptionPriceSummary();
    void insertBlackScholes(@Param("oid") int oid,
                            @Param("ivBuy") double ivBuy,
                            @Param("ivSell") double ivSell);

    Stock calls(@Param("tickerId") int tickerId,
                           @Param("fromDx") Date fromDx);

    Stock puts(@Param("tickerId") int tickerId,
               @Param("fromDx") Date fromDx);

}
