package com.lxyg.app.business.bean;

/**
 * 一般常量接口
 * @author 王沛栋
 *
 */
public interface Constants {
	//一些用到的常量定义
	public final static int MAP_REQUESTCODE = 1101;
	public final static int DISTRICT_REQUESTCODE = 1102;
	public final static String INTENT_ID = "INTENT_ID";
	public final static String TYPE_ID = "TYPE_ID";
	public final static String NOTIFY_TV = "NOTIFY_TV";

	//缺货预警的值
	public final static int QUE_HUO_NUM = 10;

	public final static int ORDER_STATUS_ALL = 1;//订单列表状态
	public final static int ORDER_STATUS1 = 1;//订单列表状态
	public final static int ORDER_STATUS2 = 2;//订单列表状态
	public final static int ORDER_STATUS3 = 3;//订单列表状态
	public final static int ORDER_STATUS4 = 4;//订单列表状态
	public final static int ORDER_STATUS5 = 5;//订单列表状态
	public final static int ORDER_STATUS6 = 6;//订单列表状态


	//地址
	public final static String LAT = "LAT";
	public final static String LNG = "LNG";
	public final static String ADDRESS = "ADDRESS";
	public final static String CITY = "CITY";
	public final static String COUNTRY = "COUNTRY";
	public final static String DISTRICT = "DISTRICT";
	public final static String PROVINCE = "PROVINCE";
	public final static String DISTRICT_ID = "DISTRICT_ID";
	public final static String DISTRICT_NAME = "DISTRICT_NAME";

	public final static String USER_INFO = "USER_INFO";
	public final static String USER_INFO_PHONE = "USER_INFO_PHONE";
	public final static String USER_INFO_PASS = "USER_INFO_PASS";
	public final static String SHOP_INFO1 = "SHOP_INFO1";

	public final static String SECRET = "caca51";
	public final static String JSONG_KEY = "info";

	public final static String BASE_URL = "http://192.168.0.210/LXYG/";
//	public final static String BASE_URL = "http://www.lexiangyungou.cn/LXYG/";

//	public final static String PUBLIC_KEY = "android_pub";
	public final static String PUBLIC_KEY = "android_in_test";


	//请求都和地址
	public final static String HOST_HEADER = BASE_URL+ "app/";
	public final static String HOST_IMG_HEADER = BASE_URL+ "res/";

	public final static String ADD_SHOP_INFO = "addShopInfo";//添加店铺信息
	public final static String SHOP_ZIZHI = "addShopIdenti";//资质认证
	public final static String GET_CODE = "messageCode";//发送验证码
	public final static String GET_CODE2 = "isExists";//发送验证码
	public final static String REGISTER = "register";//注册
	public final static String FORGET = "resetPassword";//注册
	public final static String LOGIN = "login";//登录
	public final static String GOODS_CATAGORY = "showCatagoryList";//获取货品信息
//	public final static String GOODS_LIST = "showProductList";//获取货品信息
	public final static String GOODS_LIST = "showGoodsList";//获取货品信息
	public final static String GOODS_ADD = "addProducts";//获取货品信息
	public final static String GOODS_REPERTORY = "repertory";//上货
	public final static String ORDER_LIST = "orderByStatus";//获取货品信息
	public final static String ORDER_RANG = "letOrderGo";//获取货品信息
	public final static String ORDER_GRAB = "robOrder";//抢单
	public final static String ORDER_INFO = "orderInfo";//订单详情
	public final static String ORDER_UPDATE_STATUS = "updateOrderStatus";//订单详情
	public final static String SHOP_INFO = "loadShopInfo";
	public final static String ORDER_MANAGE = "manageOrder";
	public final static String USER_MANAGE = "manageUser";
	public final static String UPDATE_PASS = "updatePassword";
	public final static String ACCOUNT_MANAGER = "accountManager";
	public final static String HOME = "Home";
	public final static String FEED_BACK = "feedBack";
	public final static String SHOW_IDENTI = "showIdenti";
	public final static String TI_XIAN = "withDraw";//withDraw
	public final static String ADD_OR_UPDATE_ACCOUNT_PASS = "addWithPwd";
	public final static String UPLOAD_LOC = "orderTail";
	public final static String ACCONT_DETAIL = "accontDetail";//账务明细

	public final static String UPLOAD_IMG = "addImg";//上传图片

	//新开接口
	public final static String NONSTANDAR_PRODUCT_ADD = "v2/addFBProduct";//添加非标产品
	public final static String NONSTANDAR_PRODUCT = "v2/FBProducts";//非标产品列表
	public final static String NONSTANDAR_PRODUCT_LIST_UPDATE = "v2/FBProductSort";//非标产品排序
	public final static String NONSTANDAR_PRODUCT_DETAILS = "v2/FBProduct";//非标产品详情
	public final static String NONSTANDAR_PRODUCT_UPDATE = "v2/updateFBProduct";//非标产品排序
	public final static String NONSTANDAR_PRODUCT_DELETE = "v2/delFBProduct";//非标产品排序
	public final static String DISTRICTS ="v2/districts";//根据区域id获取小区
	public final static String GOODS_CATEGORY = "v2/productTypes";//全部产品分类 包含一级二级分类
	public final static String GOOG_KUCUN = "checkProductNum";// 查看库存
	public final static String UPDATE_KUCUN = "modifyProductNum";//修改库存

	public final static String SHOP_TYPE = "v2/shopTypes";//非标产品排序
	public final static String UPDATE_SHOP_INFO = "v2/updateShopInfo";//非标产品排序
}
