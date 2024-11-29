package com.example.menu_electronics.constant;

import com.example.menu_electronics.dto.request.ProductInitRequest;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Constants {

//    public static final Map<String, ProductInitRequest> LIST_PRODUCT = new HashMap<>() {{
//        put("Coffee", new ProductInitRequest("Phin Milk Coffee","Traditional drip coffee with condensed milk, rich and aromatic", new BigDecimal(14),"https://bizweb.dktcdn.net/thumb/1024x1024/100/487/455/products/phin-sua-da-1698982829291.jpg?v=1724205217697",true,7, Double.parseDouble("4")));
//        put("Tea", new ProductInitRequest("Golden Lotus Tea","Green tea with lotus seeds, refreshing and nutritious", new BigDecimal(16),"https://www.google.com/url?q=https://www.highlandscoffee.com.vn/vnt_upload/product/06_2023/HLC_New_logo_5.1_Products__TSV.jpg&sa=D&source=editors&ust=1732423943950199&usg=AOvVaw0P0AFkEcdx8UXoIjpMpi3H",true,5, Double.parseDouble("3")));
//    }};

    public static final List<ProductInitRequest> LIST_PRODUCT_INIT = List.of(
            new ProductInitRequest("Phin Milk Coffee","Traditional drip coffee with condensed milk, rich and aromatic", new BigDecimal(14),"https://bizweb.dktcdn.net/thumb/1024x1024/100/487/455/products/phin-sua-da-1698982829291.jpg?v=1724205217697",true,4, Double.parseDouble("4"),"Coffee"),
            new ProductInitRequest("Golden Lotus Tea","Green tea with lotus seeds, refreshing and nutritious", new BigDecimal(16),"https://www.highlandscoffee.com.vn/vnt_upload/product/06_2023/HLC_New_logo_5.1_Products__TSV.jpg",true,5, Double.parseDouble("3"),"Tea"),
            new ProductInitRequest("Black Drip Coffee","Pure black drip coffee, strong and unsweetened", new BigDecimal(12),"https://bizweb.dktcdn.net/100/487/455/products/phin-den-da-1698982738181.jpg?v=1724205162483",true,7, Double.parseDouble("5"),"Coffee"),
            new ProductInitRequest("Mocha","Coffee blended with chocolate, sweet and flavorful", new BigDecimal(13),"https://bizweb.dktcdn.net/thumb/1024x1024/100/487/455/products/mocha-da-1698982926789.jpg?v=1724205842580",true,9, Double.parseDouble("4"),"Coffee"),
            new ProductInitRequest("Americano","Diluted black coffee, suitable for a lighter taste.", new BigDecimal(11),"https://bizweb.dktcdn.net/100/487/455/products/hlc-new-logo-5-1-products-caramel-macchiatto-1695804851222.jpg?v=1724205397400",true,5, Double.parseDouble("3"),"Coffee"),
            new ProductInitRequest("Caramel Macchiato","Coffee combined with sweet caramel, fragrant and rich.", new BigDecimal(16),"https://bizweb.dktcdn.net/thumb/1024x1024/100/487/455/products/caramel-macchiatto-1698986485003.jpg?v=1724205662227",true,4, Double.parseDouble("4"),"Coffee"),
            new ProductInitRequest("Hot Milk Coffee","Drip coffee with condensed milk, hot and creamy.", new BigDecimal(17),"https://www.highlandscoffee.com.vn/vnt_upload/product/11_2022/BR_Drink/HLC__PHIN_SUA_NONG.jpg",true,7, Double.parseDouble("2"),"Coffee"),
            new ProductInitRequest("Lychee Jelly Tea","Cooling tea with crunchy and fragrant lychee jelly.", new BigDecimal(18),"https://www.highlandscoffee.com.vn/vnt_upload/product/HLCPOSTOFFICE_DRAFT/PNG_FINAL/3_MENU_NGUYEN_BAN/Tra_Thach_Vai.jpg",true,6, Double.parseDouble("4"),"Tea"),
            new ProductInitRequest("Pink Guava Tea","Delicious pink guava combined with tea creates a refreshing taste", new BigDecimal(22),"https://www.highlandscoffee.com.vn/vnt_upload/product/HLCPOSTOFFICE_DRAFT/PNG_FINAL/2_SPECIALTY_TEA/Tra_Oi_Hong.jpg",true,5, Double.parseDouble("5"),"Tea"),
            new ProductInitRequest("lemon Ice Cubes","Delicious summer cooling dish", new BigDecimal(14),"https://www.highlandscoffee.com.vn/vnt_upload/product/HLCPOSTOFFICE_DRAFT/PNG_FINAL/3_MENU_NGUYEN_BAN/Chanh_Da_Vien.jpg",true,7, Double.parseDouble("3"),"Tea"),
            new ProductInitRequest("Peach Jelly Tea","Peach tea combined with orange and lemongrass, fresh and sweet.", new BigDecimal(20),"https://www.highlandscoffee.com.vn/vnt_upload/product/HLCPOSTOFFICE_DRAFT/PNG_FINAL/3_MENU_NGUYEN_BAN/Tra_Thach_Dao.jpg",true,7, Double.parseDouble("4"),"Tea"),
            new ProductInitRequest("Chocolate Freeze","Smooth blended chocolate drink, rich and sweet.", new BigDecimal(14),"https://www.highlandscoffee.com.vn/vnt_upload/product/06_2023/HLC_New_logo_5.1_Products__PHINDI_KEM_SUA.jpg",true,3, Double.parseDouble("2"),"Freeze"),
            new ProductInitRequest("Cookie Freeze","Smooth blended drink with a delightful cookie flavor.", new BigDecimal(12),"https://bizweb.dktcdn.net/thumb/1024x1024/100/487/455/products/classic-phin-freeze-1699345070574.jpg?v=1724205768520",true,9, Double.parseDouble("4"),"Freeze"),
            new ProductInitRequest("Matcha Ice Blend","Blended drink with matcha green tea, cool and flavorful.", new BigDecimal(23),"https://www.highlandscoffee.com.vn/vnt_upload/product/06_2023/HLC_New_logo_5.1_Products__FREEZE_TRA_XANH.jpg",true,8, Double.parseDouble("3"),"Freeze"),
            new ProductInitRequest("Lemon Frost","Blended fresh lemon, refreshing and cooling.", new BigDecimal(25),"https://www.highlandscoffee.com.vn/vnt_upload/product/06_2023/HLC_New_logo_5.1_Products__TRA_THANH_DAO-08.jpg",true,4, Double.parseDouble("5"),"Frost"),
            new ProductInitRequest("Strawberry Frost","Blended strawberry drink, sweet and icy.", new BigDecimal(27),"https://www.highlandscoffee.com.vn/vnt_upload/product/HLCPOSTOFFICE_DRAFT/PNG_FINAL/2_SPECIALTY_TEA/Tra_Qua_Mong_Anh_Dao.jpg",true,6, Double.parseDouble("4"),"Frost"),
            new ProductInitRequest("Hot Black Coffee","Hot pure drip coffee, rich and aromatic.", new BigDecimal(15),"https://www.highlandscoffee.com.vn/vnt_upload/product/11_2022/BR_Drink/HLC_PHIN_DEN_NONG.jpg",true,5, Double.parseDouble("4"),"Frost"),
            new ProductInitRequest("White Coffee","Drip coffee combined with fresh milk, light and creamy.", new BigDecimal(11),"https://www.highlandscoffee.com.vn/vnt_upload/product/04_2023/New_product/HLC_New_logo_5.1_Products__MOCHA.jpg",true,7, Double.parseDouble("3"),"Frost")
    );
}
