package critterrepos.mybatis;

import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface DerivativeMapper {

}
/*
public interface DerivativeMapper {
    void insertSpot(StockPriceBean s);
    void insertDerivativePrice(DerivativePriceBean d);
    void insertDerivative(DerivativeBean d);

    int countDerivative(String ticker);

    int countIvForSpot(StockPriceBean s);

    int countOpxPricesForSpot(StockPriceBean s);

    Integer findSpotId(StockPriceBean s);

    Integer findDerivativeId(String ticker);

    DerivativeBean findDerivative(String ticker);

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
*/
