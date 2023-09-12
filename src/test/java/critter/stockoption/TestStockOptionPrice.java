package critter.stockoption;

import critter.stock.Stock;
import critter.stock.StockPrice;
import critter.util.StockOptionUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import vega.financial.calculator.BlackScholes;

import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;
import static vega.financial.StockOption.OptionType.CALL;

public class TestStockOptionPrice {

    StockOptionPrice price;
    LocalDate curDate = LocalDate.of(2022, 5, 25);
    StockOptionUtil stockOptionUtil = new StockOptionUtil(curDate);

    @BeforeAll
    public void init() {
        var stock = new Stock();
        stock.setTicker("YAR");
        stock.setOid(StockOptionUtil.stockTickerToOid("YAR"));

        var stockPrice = new StockPrice(curDate, 482.0, 488.7, 481.8, 487.4, 10_000_000);
        stockPrice.setStock(stock);

        var stockOption = new StockOption("YAR2F470", CALL, 470.0, stock, curDate);


        var calculator = new BlackScholes();

        price = new StockOptionPrice(stockPrice, stockOption, 24.25,26.25, calculator);

    }

    @Test
    public void test_stockprice_risc() {
        var maybeRisc = price.riscStockPrice(480.0);

        assertThat(maybeRisc).isNotEmpty();
        StockOptionRisc risc = maybeRisc.get();
        assertThat(risc.getOptionPrice()).isEqualTo(19.1, offset(0.2));
        assertThat(risc.getStockPrice()).isEqualTo(480.0, offset(0.01));
        assertThat(risc.getRisc()).isEqualTo(7.15, offset(0.1));

        maybeRisc = price.riscOptionPrice(16.0);

        assertThat(maybeRisc).isNotEmpty();
        risc = maybeRisc.get();
        assertThat(risc.getOptionPrice()).isEqualTo(16.0, offset(0.01));
        assertThat(risc.getStockPrice()).isEqualTo(475.0, offset(0.1));

    }
}
