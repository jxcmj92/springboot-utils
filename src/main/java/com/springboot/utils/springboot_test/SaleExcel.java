package com.springboot.utils.springboot_test;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * @description:
 * @author: chenmingjian
 * @date: 19-8-2 15:01
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class SaleExcel extends BaseRowModel {

    @ExcelProperty(value = "订单编号", index = 1)
    private String orderId;

    @ExcelProperty(value = "订单日期", index = 2)
    private String date;

    @ExcelProperty(value = "SKU和中文名", index = 3)
    private String sku;

    @ExcelProperty(value = "数量", index = 4)
    private Integer sum;
//
//    @ExcelProperty(value = "产品售价", index = 5)
//    private double salePrice;
//
//    @ExcelProperty(value = "成交收费", index = 6)
//    private double ymxCostPrice;
//
//    @ExcelProperty(value = "促销收费", index = 7)
//    private double couponPrice;
//
//    @ExcelProperty(value = "拿货价", index = 8)
//    private double costPrice;
//
//    @ExcelProperty(value = "国内运费", index = 9)
//    private double homeFreight;
//
//    @ExcelProperty(value = "国际运费", index = 10)
//    private double internationalFreight;
//
//    @ExcelProperty(value = "毛利", index = 11)
//    private double profit;
//
//    @ExcelProperty(value = "状态", index = 12)
//    private String status;
//
//    public double getYmxCostPrice() {
//        return (this.salePrice - this.couponPrice) * 0.1 + 397;
//    }

}
