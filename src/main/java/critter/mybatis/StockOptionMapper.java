package critter.mybatis;

import critter.stock.Stock;
import critter.stock.StockPrice;
import critter.stockoption.StockOption;
import critter.stockoption.StockOptionPrice;
import org.apache.ibatis.annotations.Param;

import java.sql.Date;


public interface StockOptionMapper {
    void insertSpot(StockPrice s);
    void insertDerivativePrice(StockOptionPrice d);
    void insertDerivative(StockOption d);

    int countDerivative(String ticker);

    int countIvForSpot(StockPrice s);

    int countOpxPricesForSpot(StockPrice s);

    Integer findSpotId(StockPrice s);

    Integer findDerivativeId(String ticker);

    StockOption findDerivative(String ticker);

    /*
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
     */

    void insertBlackScholes(@Param("oid") int oid,
                            @Param("ivBuy") double ivBuy,
                            @Param("ivSell") double ivSell);

    Stock calls(@Param("tickerId") int tickerId,
                @Param("fromDx") Date fromDx);

    Stock puts(@Param("tickerId") int tickerId,
               @Param("fromDx") Date fromDx);

}
