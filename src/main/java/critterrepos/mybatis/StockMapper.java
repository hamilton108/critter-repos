package critterrepos.mybatis;

import oahu.financial.Stock;
import oahu.financial.StockPrice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Date;
import java.util.List;
import java.util.Map;


@Mapper
public interface StockMapper {
    void insertStockPrice(StockPrice bean);

    List<Map<Integer, Date>> selectMaxDate();

    List<Stock> selectStocks();

    List<StockPrice> selectStockPrices(@Param("tickerId") int tickerId,
                                  @Param("fromDx") Date fromDx);

}
