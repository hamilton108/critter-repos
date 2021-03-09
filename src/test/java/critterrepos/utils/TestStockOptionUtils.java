package critterrepos.utils;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class TestStockOptionUtils {
    private StockOptionUtils stockOptionUtils;

    @Before
    public void init() {
        stockOptionUtils = new StockOptionUtils();
    }


    @Test
    public void test_series() {
        assertThat(stockOptionUtils.seriesAsDate("1A")).isEqualTo(LocalDate.of(2021, 1, 15));
        assertThat(stockOptionUtils.seriesAsDate("1C")).isEqualTo(LocalDate.of(2021, 3, 19));
        assertThat(stockOptionUtils.seriesAsDate("1J")).isEqualTo(LocalDate.of(2021, 10, 15));
        assertThat(stockOptionUtils.seriesAsDate("1L")).isEqualTo(LocalDate.of(2021, 12, 17));

        assertThat(stockOptionUtils.seriesAsDate("1M")).isEqualTo(LocalDate.of(2021, 1, 15));
        assertThat(stockOptionUtils.seriesAsDate("1P")).isEqualTo(LocalDate.of(2021, 4, 16));
        assertThat(stockOptionUtils.seriesAsDate("1S")).isEqualTo(LocalDate.of(2021, 7, 16));
        assertThat(stockOptionUtils.seriesAsDate("1W")).isEqualTo(LocalDate.of(2021, 11, 19));

        assertThat(stockOptionUtils.seriesAsDate("2B")).isEqualTo(LocalDate.of(2022, 2, 18));
        assertThat(stockOptionUtils.seriesAsDate("2Q")).isEqualTo(LocalDate.of(2022, 5, 20));
    }
}
