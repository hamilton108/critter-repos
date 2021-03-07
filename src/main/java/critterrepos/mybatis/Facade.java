package critterrepos.mybatis;

import critterrepos.beans.options.OptionPurchaseBean;
import critterrepos.utils.MyBatisUtils;

import java.util.List;

public class Facade {

    public List<OptionPurchaseBean> activePurchasesAll(int purchaseType) {
        return MyBatisUtils.withSession((session) -> {
            CritterMapper mapper = session.getMapper(CritterMapper.class);
            return mapper.activePurchasesAll(purchaseType);
        });
    }

    /*
    public List<CurrentInvoiceBean> selectInvoices() {
        return MyBatisUtils.withSession((session) -> {
            var mapper = session.getMapper(InvoiceMapper.class);
            return mapper.selectInvoices();
        });
    }
    public void insertInvoice(InvoiceBean invoiceBean) {
        MyBatisUtils.withSessionConsumer((session) -> {
            var mapper = session.getMapper(InvoiceMapper.class);
            mapper.insertInvoice(invoiceBean);
        });
    }
     */
}
