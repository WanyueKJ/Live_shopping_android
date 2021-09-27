package com.wanyue.main.api;


import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.base.BodyRequest;
import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.bean.UserBean;
import com.wanyue.common.http.BaseHttpCallBack;
import com.wanyue.common.http.HttpCallback;
import com.wanyue.common.http.HttpClient;
import com.wanyue.common.mob.LoginData;
import com.wanyue.common.server.MapBuilder;
import com.wanyue.common.server.OkGoRequestMannger;
import com.wanyue.common.server.RequestFactory;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.live.bean.LiveBean;
import com.wanyue.main.R;
import com.wanyue.main.apply.bean.ApplyStoreBean;
import com.wanyue.main.bean.ApplyAnthorInfo;
import com.wanyue.main.bean.CommissionBankBean;
import com.wanyue.main.bean.CommissionSectionBean;
import com.wanyue.main.bean.CommitAdavanceBean;
import com.wanyue.main.bean.FeatureBean;
import com.wanyue.main.bean.GreateSelectBean;
import com.wanyue.main.bean.MainUserSectionBean;
import com.wanyue.main.bean.MenuBean;
import com.wanyue.main.bean.RegisterCommitBean;
import com.wanyue.main.bean.SpreadManRankBean;
import com.wanyue.main.store.bean.MyStoreMessageBean;
import com.wanyue.main.store.bean.SettlementRecordBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class MainAPI {
    /*获取验证码之前获取key*/
    public static final String VERIFY_KEY="verify_code";
    /*获取验证码*/
    public static final String VERIFY="register/verify";
    /*获取用户信息*/
    public static final String BASE_INFO="userinfo";
    /*登陆*/
    public static final String LOGIN="login/mobile";
    /*三方登录*/
    public static final String LOGIN_THIRD="thirdlogin";
    /*获取分类列表*/
    public static final String CATEGORY="category";
    /*获取精选等数据*/
    public static final String FEATURED="featured";
    public static final String CLASS_LIVE="classlive";
    public static final String LIVE_FOLLOW="livefollow";
    public static final String MENU_USER="menu/user";
    public static final String GREATE_GOODS="product/day";

    public static final String USER="user";
    public static final String EDIT_USER="user/edit";

    public static final String COMMISSION="commission"; //推广统计
    public static final String SPREAD_BANNER="spread/banner"; //推广海报
    public static final String SPREAD_PEOPLE="spread/people"; //推广人列表
    public static final String COMMISSION_DETAILED="spread/commission/"; //推广佣金明细

    public static final String SPREAD_COUNT="spread/count/"; //推广佣金/提现总和
    public static final String SPREAD_ORDER="spread/order"; //推广人订单

    public static  final String BROKERAGE_RANK="brokerage_rank"; //佣金排行
    public static  final String PERSON_RANK="rank"; //推广人排行
    public static  final  String  COMMISSION_BANK_EXTRACT="extract/cash"; //佣金提现
    public static final String GET_COMMISSION_BANK = "extract/bank";//获取佣金提现信息
    public static final String LIVE_SEARCH = "livesearch";//搜索直播
    public static final String MY_STORE="shop"; //我的店铺
    public static final String SHOP_CASH="shopcash"; //店铺收益
    public static final String BRING="bring"; //店铺收益
    public static final String SHOP_SETTLE="shopsettle"; //店铺收益结算记录
    public static final String BRING_SETTLE="bringsettle"; //代销收益结算记录

    public static final String SHOP_CASH_LIST="shopcashlist"; //店铺收益结算记录
    public static final String BRING_LIST="bringlist"; //代销提现记录
    public static final String GET_USER_ACCOUNT_LIST = "account";//提现列表
    public static final String ADD_CASH_ACCOUNT = "accountadd"; //添加账户
    public static final String DEL_CASH_ACCOUNT ="accountdel" ; //删除账户
    public static final String CASH_PROXY ="cashbring" ;//代销提现
    public static final String CASH_SHOP ="cashshop" ;//店铺提现
    public static final String CASH_GIFT ="cash" ;//礼物提现
    public static final String MY_BRING ="mybring" ;//代销统计
    public static final String SHOP_ADD_LIST ="shopaddlist" ;//代销待添加
    public static final String SHOP_ADD ="shopadd" ;//代销待添加


    public static final String SHOP_SHELF_LIST ="shoplist" ;//上架商品
    public static final String SHOP_SHELF_NUM ="shopnums" ;//上架商品总数
    public static final String SHOP_SHELF_LIST_NO ="shoplistno" ;//下架商品
    public static final String SHOP_SHELF_LIST_NO_NUM ="shopnumsno" ;//下架商品总数
    public static final String SHOP_SHELF_SET_DOWN ="shopedit" ;//下架
    public static final String SHOP_SHELF_DELETE ="shopdel" ;//删除代销商品
    public static final String REGISTER ="register" ;//注册

    public static final String OPEN_STORE_STATUS ="shopstatus" ;//开通店铺状态
    public static final String APPLY_OPEN_STORE ="shopapply" ;//申请开通店铺


    public static final String DO_CASH ="doCash" ;//提现
    public static final String GET_PROFIT ="getProfit" ;//收益






    /*获取用户基本信息*/
    public static void getBaseInfo(BaseHttpCallBack httpCallback) {
        HttpClient.getInstance().get(BASE_INFO, BASE_INFO)
                .execute(httpCallback);
    }


    /*获取用户基本信息*/
    public static Observable<UserBean> getBaseInfoByRx() {
        return RequestFactory.getRequestManager().valueGet(BASE_INFO,null,UserBean.class,true);
    }

    /**
     * 获取验证码接口需要的key
     */

    /*
     三方登录
    */

    public static void loginByThird(LoginData loginData, BaseHttpCallBack baseHttpCallBack){
        MapBuilder mapBuilder=MapBuilder.factory();
        mapBuilder.put("type",loginData.getType())
                .put("openid",loginData.getOpenID())
                .put("avatar",loginData.getAvatar())
                .put("nickname",loginData.getNickName())
                .put("unionid",loginData.getUnionid())
                .put("city",loginData.getCity())
                .put("country",loginData.getCountry())
                .put("province",loginData.getProvince())
                .put("gender",loginData.getGender());

        Map<String,Object>map=mapBuilder.build();
        String sign=StringUtil.createSign(map,"type","openid");
        mapBuilder.put("sign",sign);
        HttpParams httpParams=OkGoRequestMannger.parse(map);
        BodyRequest bodyRequest=HttpClient.getInstance().post(LOGIN_THIRD, LOGIN_THIRD);
        bodyRequest.params(httpParams);
        bodyRequest.execute(baseHttpCallBack);
    }

    public static void getVerifyKey(BaseHttpCallBack httpCallback) {
        HttpClient.getInstance().get(VERIFY_KEY, VERIFY_KEY)
                .execute(httpCallback);

    }

    /**
     * 获取验证码 0登录1注册2忘记密码
     */


    public static void getVerifyCode(String phone,String type,String key,BaseHttpCallBack httpCallback) {
        HttpClient.getInstance().post(VERIFY, VERIFY)
                .params("phone",phone)
                .params("type",type)
                .params("key",key)
                .execute(httpCallback);
    }

    public static void loginByCode(String phone,String captcha,BaseHttpCallBack httpCallback) {
        HttpClient.getInstance().post(LOGIN, LOGIN)
                .params("phone",phone)
                .params("spread",0)
                .params("captcha",captcha)
                .execute(httpCallback);
    }

    /**
     * 获取分类列表
     */

    public static void getCategory(BaseHttpCallBack httpCallback) {
        HttpClient.getInstance().get(CATEGORY, CATEGORY)
                .execute(httpCallback);
    }


    /*获取精选列表*/
    public static Observable<FeatureBean> getFeatured(int p){
        Map<String,Object>map=MapBuilder.factory().put("page",p).build();
        return RequestFactory.getRequestManager().valueGet(FEATURED,map, FeatureBean.class,false);
    }

    /**
     * 获取分类下直播列表
     */

    public static Observable<List<LiveBean>> getLiveListByClass(int id,int p){
        String path=CLASS_LIVE+"/"+id;
        Map<String,Object>map=MapBuilder.factory().put("page",p).build();
        return RequestFactory.getRequestManager().get(path,map, LiveBean.class,false);
    }

    public static Observable<List<LiveBean>> getLiveListBySearch(String keyword,int p){
        Map<String,Object>map=MapBuilder.factory().put("page",p).
                put("keyword",keyword).
                put("limit",20).
                build();
        return RequestFactory.getRequestManager().get(LIVE_SEARCH,map, LiveBean.class,false);
    }



    /*获取关注下直播*/
    public static Observable<List<LiveBean>> getLiveListByFollow(int p){
        Map<String,Object>map=MapBuilder.factory().put("page",p).build();
        return RequestFactory.getRequestManager().get(LIVE_FOLLOW,map, LiveBean.class,false);
    }

    /*获取个人中心列表*/
    public static Observable<JSONObject> getUserCenter(){
        return RequestFactory.getRequestManager().valueGet(USER,null, JSONObject.class,false);
    }


    /*获取个人中心菜单列表*/
    public static Observable<List<MainUserSectionBean>> getMenuUser(final List<MainUserSectionBean>list, final int serviceId, final int settingId){
      return RequestFactory.getRequestManager().valueGet(MENU_USER,null, JSONObject.class,false).map(new Function<JSONObject, List<MainUserSectionBean>>() {
          @Override
          public List<MainUserSectionBean> apply(JSONObject info) throws Exception {
              MainUserSectionBean sectionBean=new MainUserSectionBean();
              sectionBean.setTitle("我的服务");
              List<MenuBean>menuBeanList=new ArrayList<>();
              sectionBean.setMenuBeanList(menuBeanList);
              JSONArray jsonArray=info.getJSONArray("routine_my_menus");
              if(jsonArray!=null&&jsonArray.size()>0){
                  List<MenuBean>list=JSONArray.parseArray(jsonArray.toString(),MenuBean.class);
                  menuBeanList.addAll(list);
              }
              MenuBean menuBean=new MenuBean("联系客服",serviceId,R.drawable.icon_main_user_customer);
              menuBeanList.add(menuBean);
              menuBean=new MenuBean("个性设置",settingId,R.drawable.icon_main_user_setting);
              menuBeanList.add(menuBean);
              list.add(sectionBean);
              return list;
          }
      });
    }






    /*获取优选列表*/
    public static Observable<GreateSelectBean> getGreateSelectGoods(int p) {
        Map<String,Object>map=MapBuilder.factory().put("page",p).build();
        return RequestFactory.getRequestManager().valueGet(GREATE_GOODS,map, GreateSelectBean.class,false);
    }


    /*跟新用户信息*/
    public static Observable<Boolean> updateUserInfo(String avatar,String nickname) {

        Map<String,Object>map=MapBuilder.factory()
                .put("nickname",nickname)
                .build();
        if(!TextUtils.isEmpty(avatar)){
            map.put("avatar",avatar);
        }
        return RequestFactory.getRequestManager().commit(EDIT_USER,map, false);
    }

    //推广数据统计
    public static void getCommission(BaseHttpCallBack baseHttpCallBack){
        HttpClient.getInstance().get(COMMISSION, COMMISSION)
                .execute(baseHttpCallBack);
    }


    //获取海报banner
    public static void getSpreadBanner(BaseHttpCallBack baseHttpCallBack){
        HttpClient.getInstance().get(SPREAD_BANNER, SPREAD_BANNER)
                 .params("type",1)
                .execute(baseHttpCallBack);
    }

    //获取推广人列表
    public static Observable<JSONObject>getSpreadPeopleList(int page,int grade,String keyword,String sort){
        Map<String,Object>map=MapBuilder.factory().put("page",page).
                put("grade",grade).
                put("limit",20).
                put("keyword",keyword).
                put("sort",sort).
                build();

        return RequestFactory.getRequestManager().valuePost(SPREAD_PEOPLE,map, JSONObject.class,false);
    }


    //获取佣金明细
    public static Observable<List<CommissionSectionBean>>getCommissionList(int p,int type){
        Map<String,Object>map=MapBuilder.factory().put("page",p).
                put("limit",20).
                put("type",type).
                build();
        return RequestFactory.getRequestManager().get(COMMISSION_DETAILED+type,map, CommissionSectionBean.class,false);
    }


    public static  void getSpreadCount(int type,BaseHttpCallBack baseHttpCallBack){
        HttpClient.getInstance().get(SPREAD_COUNT+type, SPREAD_COUNT)
                .execute(baseHttpCallBack);
    }

    /*佣金排行*/
    public static Observable<JSONObject>getBrokerageRankList(int p,String type){
        Map<String,Object>map=MapBuilder.factory().put("page",p).
                put("limit",20).
                put("type",type).
                build();
        return RequestFactory.getRequestManager().valueGet(BROKERAGE_RANK,map, JSONObject.class,false);
    }

    /*推广订单*/
    public static Observable<JSONObject>getSpreadOrderList(int p){
        Map<String,Object>map=MapBuilder.factory().put("page",p).
                put("limit",20).
                build();
        return RequestFactory.getRequestManager().valuePost(SPREAD_ORDER,map, JSONObject.class,false);
    }

    /*推广人排行列表*/
    public static Observable<List<SpreadManRankBean>>getPersonRankList(int p,String type){
        Map<String,Object>map=MapBuilder.factory().put("page",p).
                put("limit",20).
                put("type",type).
                build();
        return RequestFactory.getRequestManager().get(PERSON_RANK,map, SpreadManRankBean.class,false);
    }

    /*获取提现信息*/
    public static Observable<CommissionBankBean>getCommissionBankMessage(){
        return RequestFactory.getRequestManager().valueGet(GET_COMMISSION_BANK,null, CommissionBankBean.class,false);
    }


    /*申请佣金提现*/
    public static Observable<Boolean>commissionBankExtract(CommitAdavanceBean adavanceBean){
        String json=JSON.toJSONString(adavanceBean);
        Map<String,Object>map=JSON.parseObject(json,Map.class);
        return RequestFactory.getRequestManager().commit(COMMISSION_BANK_EXTRACT,map, true);
    }


    /*店铺收益*/
    public static Observable<JSONObject>getStoreProfit(){
        return RequestFactory.getRequestManager().valueGet(SHOP_CASH,null, JSONObject.class,false);
    }

    /*代销收益*/
    public static Observable<JSONObject>getBringProfit(){
        return RequestFactory.getRequestManager().valueGet(BRING,null, JSONObject.class,false);
    }








    /**
     * 提现
     */
    public static void doCash(String route,String money, String accountID, BaseHttpCallBack httpCallback) {
        HttpClient.getInstance().post(route,route)
                .params("money", money)//提现的票数
                .params("accountid", accountID)//账号ID
                .execute(httpCallback);
    }



    /**
     * 获取 我的收益 可提现金额数
     */
    public static void getGiftProfit(HttpCallback callback) {
        HttpClient.getInstance().get("votes", GET_PROFIT)
                .params("uid", CommonAppConfig.getUid())
                .params("token", CommonAppConfig.getToken())
                .execute(callback);
    }

    /**
     * 获取 提现账户列表
     */
    public static void getCashAccountList(HttpCallback callback) {
        HttpClient.getInstance().get(GET_USER_ACCOUNT_LIST,GET_USER_ACCOUNT_LIST)
                .params("uid", CommonAppConfig.getUid())
                .params("token", CommonAppConfig.getToken())
                .execute(callback);
    }


    /**
     * 添加 提现账户
     */
    public static void addCashAccount(String account, String name, String bank, int type, HttpCallback callback) {
        HttpClient.getInstance().post(ADD_CASH_ACCOUNT, ADD_CASH_ACCOUNT)
                .params("uid", CommonAppConfig.getUid())
                .params("token", CommonAppConfig.getToken())
                .params("account", account)
                .params("name", name)
                .params("bank", bank)
                .params("type", type)
                .execute(callback);
    }

    /**
     * 删除 提现账户
     */
    public static void deleteCashAccount(String accountId, HttpCallback callback) {
        HttpClient.getInstance().post(DEL_CASH_ACCOUNT, DEL_CASH_ACCOUNT)
                .params("uid", CommonAppConfig.getUid())
                .params("token", CommonAppConfig.getToken())
                .params("accountid", accountId)
                .execute(callback);
    }
















    /**
     * 获取上架商品总数
     */
    public static void getShifListNum(BaseHttpCallBack callback){
        HttpClient.getInstance().get(SHOP_SHELF_NUM,SHOP_SHELF_NUM)
                .execute(callback);
    }

    /**
     * 获取下架商品总数
     */

    public static void getNoShifListNum(BaseHttpCallBack callback){
        HttpClient.getInstance().get(SHOP_SHELF_LIST_NO_NUM,SHOP_SHELF_LIST_NO_NUM)
                .execute(callback);
    }

    /**
     * 代销商品下架
     */

    public static Observable<Boolean>downConsignmentGoods(String id){
        Map<String,Object>map=MapBuilder.factory()
                .put("productid",id)
                .build();
        return RequestFactory.getRequestManager().commit(SHOP_SHELF_SET_DOWN,map,false);
    }

    public static Observable<Boolean>delShelfGoods(String id){
        Map<String,Object>map=MapBuilder.factory()
                .put("productid",id)
                .build();
        return RequestFactory.getRequestManager().commit(SHOP_SHELF_DELETE,map,false);
    }

    public static void cancle(String tag) {
        HttpClient.getInstance().cancel(tag);
    }

   /*  注册
   account	string	必须		手机号/账号
    captcha	string	必须		验证码
    password	string	必须		密码
    spread	string	必须		推广编号*/

    public static Observable<Boolean> register(RegisterCommitBean commitBean) {
        Map<String,Object>map=MapBuilder.factory()
                .put("account",commitBean.getPhone())
                .put("captcha",commitBean.getCode())
                .put("password",commitBean.getPwd())
                .put("spread",commitBean.getSpread())
                .build();
        return RequestFactory.getRequestManager().commit(REGISTER,map,true);
    }

    /*获取主持人申请情况*/
    public static Observable<ApplyAnthorInfo> getLiveApplyInfo() {
        return RequestFactory.getRequestManager().valueGet(OPEN_STORE_STATUS, null, ApplyAnthorInfo.class, false);
    }

    /*申请开通店铺*/
    public static Observable<Boolean> applyOpenStore(ApplyStoreBean applyStoreBean){
        MapBuilder mapBuilder=MapBuilder.factory()
                .put("realname",applyStoreBean.getRealname())
                .put("tel",applyStoreBean.getTel())
                .put("cer_no",applyStoreBean.getCer_no())
                ;
        mapBuilder
                .put("cer_f",applyStoreBean.getCer_f()==null?null:applyStoreBean.getCer_f().url)
                .put("cer_b",applyStoreBean.getCer_b()==null?null:applyStoreBean.getCer_b().url)
                .put("cer_h",applyStoreBean.getCer_h()==null?null:applyStoreBean.getCer_h().url)
                .put("business",applyStoreBean.getBusiness()==null?null:applyStoreBean.getBusiness().url)
                .put("license",applyStoreBean.getLicense()==null?null:applyStoreBean.getLicense().url)
                .put("other",applyStoreBean.getOther()==null?null:applyStoreBean.getOther().url)
                .build();
        return RequestFactory.getRequestManager().commit(APPLY_OPEN_STORE, mapBuilder.build(), true);
    }

    public static Observable<MyStoreMessageBean>getMyStore(){
        return RequestFactory.getRequestManager().valueGet(MY_STORE,null, MyStoreMessageBean.class,false);
    }
    /*收益结算记录*/
    public static Observable<List<SettlementRecordBean>>getSettlementRecords(String route, int p){
        Map<String,Object>map=MapBuilder.factory().put("page",p).build();
        return RequestFactory.getRequestManager().get(route,map, SettlementRecordBean.class,false);
    }

}
