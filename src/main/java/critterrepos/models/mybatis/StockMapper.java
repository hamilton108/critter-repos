package critterrepos.models.mybatis;

import oahu.financial.Stock;
import oahu.financial.StockPrice;
import org.apache.ibatis.annotations.Param;

import java.sql.Date;
import java.util.List;
import java.util.Map;


public interface StockMapper {
    void insertStockPrice(StockPrice bean);

    List<Map<Integer,Date>> selectMaxDate();

    List<Stock> selectStocks();

    List<StockPrice> selectStockPrices(@Param("tickerId") int tickerId,
                                  @Param("fromDx") Date fromDx);

    /*
    List<Stock> selectStocksWithPrices(@Param("tickerIds") List<Integer> tickerIds,
                                      @Param("fromDx") Date fromDx);

    Stock selectStockWithPrices(@Param("oid") int oid,
                                       @Param("fromDx") Date fromDx);

    */
}
