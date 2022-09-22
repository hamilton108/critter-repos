package critter.utils;

import critter.util.StockOptionUtil;
import oahu.dto.Tuple2;
import oahu.dto.Tuple3;
import org.junit.Test;
import vega.financial.StockOption;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class TestStockOptionUtils {
    //private StockOptionUtil stockOptionUtils;

    /*
    @Before
    public void init() {
        stockOptionUtils = new StockOptionUtil();
    }
     */


    @Test
    public void test_series() {
        assertThat(StockOptionUtil.seriesAsDate("1A")).isEqualTo(LocalDate.of(2021, 1, 15));
        assertThat(StockOptionUtil.seriesAsDate("1C")).isEqualTo(LocalDate.of(2021, 3, 19));
        assertThat(StockOptionUtil.seriesAsDate("1J")).isEqualTo(LocalDate.of(2021, 10, 15));
        assertThat(StockOptionUtil.seriesAsDate("1L")).isEqualTo(LocalDate.of(2021, 12, 17));

        assertThat(StockOptionUtil.seriesAsDate("1M")).isEqualTo(LocalDate.of(2021, 1, 15));
        assertThat(StockOptionUtil.seriesAsDate("1P")).isEqualTo(LocalDate.of(2021, 4, 16));
        assertThat(StockOptionUtil.seriesAsDate("1S")).isEqualTo(LocalDate.of(2021, 7, 16));
        assertThat(StockOptionUtil.seriesAsDate("1W")).isEqualTo(LocalDate.of(2021, 11, 19));

        assertThat(StockOptionUtil.seriesAsDate("2B")).isEqualTo(LocalDate.of(2022, 2, 18));
        assertThat(StockOptionUtil.seriesAsDate("2Q")).isEqualTo(LocalDate.of(2022, 5, 20));
    }
    @Test
    public void test_stockoption_info_from_ticker() {
        var call1 = new Tuple3<>(2, "EQNR", StockOption.OptionType.CALL);
        assertThat(StockOptionUtil.stockOptionInfoFromTicker("EQNR1L320")).isEqualTo(call1);
        var put1 = new Tuple3<>(2, "EQNR", StockOption.OptionType.PUT);
        assertThat(StockOptionUtil.stockOptionInfoFromTicker("EQNR1X320")).isEqualTo(put1);

        var call2 = new Tuple3<>(3, "YAR", StockOption.OptionType.CALL);
        assertThat(StockOptionUtil.stockOptionInfoFromTicker("YAR1G540")).isEqualTo(call2);
        var put2 = new Tuple3<>(3, "YAR", StockOption.OptionType.PUT);
        assertThat(StockOptionUtil.stockOptionInfoFromTicker("YAR1S540")).isEqualTo(put2);
    }
}
