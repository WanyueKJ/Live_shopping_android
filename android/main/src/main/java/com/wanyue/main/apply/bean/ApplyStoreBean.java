package com.wanyue.main.apply.bean;

import android.text.TextUtils;
import com.wanyue.common.upload.FileBundle;
import java.util.ArrayList;
import java.util.List;


public class ApplyStoreBean {
    private String realname;	//text	是		姓名
    private String tel;//text	是		电话
    private String cer_no;//text	是		证件号
    private FileBundle cer_f;//text	是		证件正面照链接
    private FileBundle cer_b;//text	是		证件反面照链接
    private FileBundle cer_h;//text	是		证件手持照链接
    private FileBundle business;//text	是		营业执照链接
    private FileBundle license;//text	否		许可证链接
    private FileBundle other;//text	否		其他证件链接


    public String getRealname() {
        return realname;
    }
    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCer_no() {
        return cer_no;
    }

    public void setCer_no(String cer_no) {
        this.cer_no = cer_no;
    }


    public FileBundle getCer_f() {
        if( cer_f==null){
            cer_f=new FileBundle();
        }
        return cer_f;
    }

    public void setCer_f(FileBundle cer_f) {
        this.cer_f = cer_f;
    }

    public FileBundle getCer_b() {
        if(cer_b==null){
           cer_b=new FileBundle();
        }
        return cer_b;
    }

    public void setCer_b(FileBundle cer_b) {
        this.cer_b =cer_b;
    }

    public FileBundle getCer_h() {
        if( cer_h==null){
            cer_h=new FileBundle();
        }
        return cer_h;
    }

    public void setCer_h(FileBundle cer_h) {
        this.cer_h = cer_h;
    }

    public FileBundle getBusiness() {
        if( business==null){
            business=new FileBundle();
        }
        return business;
    }

    public void setBusiness(FileBundle business) {
        this.business = business;
    }

    public FileBundle getLicense() {
        if( license==null){
            license=new FileBundle();
        }
        return license;
    }

    public void setLicense(FileBundle license) {
        this.license = license;
    }

    public FileBundle getOther() {
        if( other==null){
            other=new FileBundle();
        }
        return other;
    }

    public void setOther(FileBundle other) {
        this.other = other;
    }



    public List<FileBundle>getFileBundle(){
        List<FileBundle>list=new ArrayList<>();
        if(cer_f!=null){
            list.add(cer_f);
        }
        if(cer_b!=null){
            list.add(cer_b);
        }
        if(cer_h!=null){
            list.add(cer_h);
        }
        if(business!=null){
            list.add(business);
        }
        if(license!=null){
            list.add(license);
        }
        if(other!=null){
           list.add(other);
        }
       return list;
    }


    public boolean isHaveUploadThumb(){
        return cer_f!=null&&cer_f.url!=null;
    }


    public String check(){
        if(TextUtils.isEmpty(realname)){
           return "请输入姓名";
        }else if(TextUtils.isEmpty(tel)){
            return "请输入正确的手机号";
        }else if(TextUtils.isEmpty(cer_no)){
            return "请输入身份证号";
        }else if(cer_f==null){
            return "请上传证件正面照";
        }else if(cer_b==null){
            return "请上传证件背面照";
        }else if(cer_h==null){
            return "请上传手持证件照";
        }else if(business==null){
            return "请上传营业执照";
        }
        return null;
    }


}
