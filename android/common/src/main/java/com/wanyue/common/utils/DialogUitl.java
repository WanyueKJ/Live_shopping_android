package com.wanyue.common.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import androidx.core.content.ContextCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wanyue.common.R;

import java.util.ArrayList;
import java.util.Calendar;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.widget.WheelView;

//import android.widget.DatePicker;


/**
 * Created by  on 2017/8/8.
 */

public class DialogUitl {

    public static final int INPUT_TYPE_TEXT = 0;
    public static final int INPUT_TYPE_NUMBER = 1;
    public static final int INPUT_TYPE_NUMBER_PASSWORD = 2;
    public static final int INPUT_TYPE_TEXT_PASSWORD = 3;

    public static final String GONE="@GONE";

    //第三方登录的时候用显示的dialog
    public static Dialog loginAuthDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(R.layout.dialog_login_loading);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    /**
     * 用于网络请求等耗时操作的LoadingDialog
     */
    public static Dialog loadingDialog(Context context, String text) {
        return loadingDialog(context,text,true);

    }
    public static Dialog loadingDialog(Context context, String text,boolean isCancel) {
        Dialog dialog = new Dialog(context, R.style.CustomProgressDialog);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.setCancelable(isCancel);
        dialog.setCanceledOnTouchOutside(isCancel);
        if (!TextUtils.isEmpty(text)) {
            TextView titleView =  dialog.findViewById(R.id.text);
            if (titleView != null) {
                titleView.setText(text);
            }
        }
        return dialog;
    }


    public static Dialog loadingDialog(Context context) {
        return loadingDialog(context, "");
    }

    public static void showSimpleTipDialog(Context context, String content) {
        showSimpleTipDialog(context, null, content);
    }

    public static void showSimpleTipDialog(Context context, String title, String content) {
        final Dialog dialog = new Dialog(context, R.style.dialog2);
        dialog.setContentView(R.layout.dialog_simple_tip);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        if (!TextUtils.isEmpty(title)) {
            TextView titleView = (TextView) dialog.findViewById(R.id.title);
            titleView.setText(title);
        }
        if (!TextUtils.isEmpty(content)) {
            TextView contentTextView = (TextView) dialog.findViewById(R.id.content);
            contentTextView.setText(content);
        }
        dialog.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void showSimpleDialog(Context context, String content, SimpleCallback callback) {
        showSimpleDialog(context, content, true, callback);
    }

    public static void showSimpleDialog(Context context, String content, boolean dark, SimpleCallback callback) {
        showSimpleDialog(context, content, true, dark, callback);
    }

    public static void showSimpleDialog(Context context, String content, boolean cancelable, boolean dark, SimpleCallback callback) {
        new Builder(context)
                .setContent(content)
                .setCancelable(cancelable)
                .setBackgroundDimEnabled(dark)
                .setClickCallback(callback)

                .build()
                .show();
    }

    public static void showSimpleDialog(Context context, String title, String content, boolean cancelable, SimpleCallback callback) {
        new Builder(context)
                .setTitle(title)
                .setContent(content)
                .setCancelable(cancelable)
                .setClickCallback(callback)
                .build()
                .show();
    }

    public static void showSimpleInputDialog(Context context, String title, String hint, int inputType, int length, SimpleCallback callback) {
        new Builder(context).setTitle(title)
                .setCancelable(true)
                .setInput(true)
                .setHint(hint)
                .setInputType(inputType)
                .setLength(length)
                .setClickCallback(callback)
                .build()
                .show();
    }


    public static void showSimpleInputDialog(Context context, String title, int inputType, int length, SimpleCallback callback) {
        showSimpleInputDialog(context, title, null, inputType, length, callback);
    }

    public static void showSimpleInputDialog(Context context, String title, int inputType, SimpleCallback callback) {
        showSimpleInputDialog(context, title, inputType, 0, callback);
    }

    public static void showSimpleInputDialog(Context context, String title, SimpleCallback callback) {
        showSimpleInputDialog(context, title, INPUT_TYPE_TEXT, callback);
    }


    public static void showStringArrayDialog(Context context, Integer[] array, StringArrayDialogCallback callback) {
        showStringArrayDialog(context, array, true, callback);
    }

    public static void showStringArrayDialog(Context context, Integer[] array, boolean dark, StringArrayDialogCallback callback) {
        getStringArrayDialog(context, array, dark, callback).show();
    }

    public static Dialog getStringArrayDialog(Context context, Integer[] array, boolean dark, final StringArrayDialogCallback callback) {
        final Dialog dialog = new Dialog(context, dark ? R.style.dialog : R.style.dialog2);
        dialog.setContentView(R.layout.dialog_string_array);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.bottomToTopAnim);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
        LinearLayout container = (LinearLayout) dialog.findViewById(R.id.container);
        View.OnClickListener itemListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) v;
                if (callback != null) {
                    callback.onItemClick(textView.getText().toString(), (int) v.getTag());
                }
                dialog.dismiss();
            }
        };
        int globalColor = ContextCompat.getColor(context, R.color.global);
        for (int i = 0, length = array.length; i < length; i++) {
            TextView textView = new TextView(context);
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DpUtil.dp2px(50)));
            textView.setTextColor(globalColor);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            textView.setGravity(Gravity.CENTER);
            textView.setText(array[i]);
            textView.setTag(array[i]);
            textView.setOnClickListener(itemListener);
            container.addView(textView);
            if (i != length - 1) {
                View v = new View(context);
                v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DpUtil.dp2px(1)));
                v.setBackgroundColor(0xfff0f0f0);
                container.addView(v);
            }
        }
        dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }


    public static Dialog getStringArrayDialog(Context context, SparseArray<String> array, boolean dark, final StringArrayDialogCallback callback) {
        final Dialog dialog = new Dialog(context, dark ? R.style.dialog : R.style.dialog2);
        dialog.setContentView(R.layout.dialog_string_array);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.bottomToTopAnim);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
        LinearLayout container = (LinearLayout) dialog.findViewById(R.id.container);
        View.OnClickListener itemListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) v;
                if (callback != null) {
                    callback.onItemClick(textView.getText().toString(), (int) v.getTag());
                }
                dialog.dismiss();
            }
        };
        int globalColor = ContextCompat.getColor(context, R.color.global);
        for (int i = 0, length = array.size(); i < length; i++) {
            TextView textView = new TextView(context);
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DpUtil.dp2px(50)));
            textView.setTextColor(globalColor);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            textView.setGravity(Gravity.CENTER);
            textView.setText(array.valueAt(i));
            textView.setTag(array.keyAt(i));
            textView.setOnClickListener(itemListener);
            container.addView(textView);
            if (i != length - 1) {
                View v = new View(context);
                v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DpUtil.dp2px(1)));
                v.setBackgroundColor(0xfff0f0f0);
                container.addView(v);
            }
        }
        dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public static void showStringArrayDialog(Context context, SparseArray<String> array, boolean dark, StringArrayDialogCallback callback) {
        getStringArrayDialog(context, array, dark, callback).show();
    }

    public static void showStringArrayDialog(Context context, SparseArray<String> array, StringArrayDialogCallback callback) {
        showStringArrayDialog(context, array, true, callback);
    }

    /**
     * 城市选择
     */

    public static void showCityChooseDialog(Activity activity, ArrayList<Province> list,
                                            String province, String city, String district, AddressPicker.OnAddressPickListener listener) {
        AddressPicker picker = new AddressPicker(activity, list);
        picker.setTextColor(0xff323232);
        picker.setDividerColor(0xffdcdcdc);
        picker.setAnimationStyle(R.style.bottomToTopAnim);
        picker.setCancelTextColor(0xff323232);
        picker.setSubmitTextColor(0xff323232);
        picker.setTopLineColor(0xfff5f5f5);
        picker.setLabel("123","123123");
        picker.setTopBackgroundColor(0xffffffff);
        picker.setHeight(DpUtil.dp2px(250));
        picker.setOffset(5);
        picker.setTitleText(R.string.select_area);
        picker.setHideProvince(false);
        picker.setHideCounty(false);
        picker.setColumnWeight(3 / 9.0f, 3 / 9.0f, 3 / 9.0f);
        if (TextUtils.isEmpty(province)) {
            province = "北京市";
        }
        if (TextUtils.isEmpty(city)) {
            city = "北京市";
        }
        if (TextUtils.isEmpty(district)) {
            district = "东城区";
        }
        picker.setSelectedItem(province, city, district);
        picker.setOnAddressPickListener(listener);
        picker.show();
    }

    /**
     * 星座
     */
    public static void showXinZuoDialog(Activity activity, String[] xinZuoArray, OptionPicker.OnOptionPickListener listener) {
        OptionPicker picker = new OptionPicker(activity, xinZuoArray);
//        picker.setCycleDisable(false);//不禁用循环
        picker.setTopBackgroundColor(0xfff5f5f5);
        picker.setHeight(DpUtil.dp2px(250));
        picker.setTopLineVisible(false);
        picker.setCancelTextColor(0xff969696);
        picker.setSubmitTextColor(0xff4998F7);
        picker.setTextColor(0xff323232, 0xffc8c8c8);
        picker.setTextSize(16);
        WheelView.DividerConfig config = new WheelView.DividerConfig();
        config.setColor(0xffdcdcdc);//线颜色
        config.setRatio(1);//线比率
        picker.setDividerConfig(config);
        picker.setBackgroundColor(0xffffffff);
        picker.setSelectedIndex(0);
        picker.setAnimationStyle(R.style.bottomToTopAnim);
        picker.setCanceledOnTouchOutside(true);
        picker.setOnOptionPickListener(listener);
        picker.show();
    }

    public static void showDatePicker(Activity activity, final DataPickerCallback callback) {
        DatePicker picker = new DatePicker(activity);
        picker.setLabel("", "", "", "", "");
        picker.setAnimationStyle(R.style.bottomToTopAnim);
        picker.getWindow().setDimAmount(0.5f);
        picker.setCanceledOnTouchOutside(true);
        picker.setTextColor(0xff323232);
        picker.setDividerColor(0xfff0f0f0);
        picker.setTopLineColor(0xffeeeeee);
        picker.setTopBackgroundColor(0xffeeeeee);
        picker.setCancelTextColor(0xff969696);
        picker.setSubmitTextColor(0xff4998f7);
        picker.setResetWhileWheel(false);
        picker.setRangeStart(1900, 1, 1);
        picker.setRangeEnd(2050, 12, 31);
        picker.setSelectedItem(2000, 1, 1);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                Calendar c = Calendar.getInstance();
                c.set(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
                long time=System.currentTimeMillis();
                if (c.getTime().getTime() >time ) {
                    ToastUtil.show(WordUtil.getString(R.string.edit_profile_right_date));
                } else {
                    if (callback != null) {
                        callback.onConfirmClick(year, month, day);
                    }
                }
            }
        });
        picker.show();
    }


    /**
     * 选择订单时间
     */
    public static void showOrderTimeDialog(Activity activity, final ArrayList<Province> list, final int dayIndex, final int hourIndex, final int minIndex, AddressPicker.OnAddressPickListener listener) {
        final AddressPicker picker = new AddressPicker(activity, list);
        picker.setTextColor(0xff323232);
        picker.setDividerColor(0xffdcdcdc);
        picker.setAnimationStyle(R.style.bottomToTopAnim);
        picker.setCancelTextColor(0xff969696);
        picker.setSubmitTextColor(0xff4998F7);
        picker.setTopLineColor(0xfff5f5f5);
        picker.setTopBackgroundColor(0xfff5f5f5);
        picker.setHeight(DpUtil.dp2px(250));
        picker.setOffset(5);
        picker.setHideProvince(false);
        picker.setHideCounty(false);
        picker.setColumnWeight(3 / 9.0f, 3 / 9.0f, 3 / 9.0f);
        picker.setSelectedIndex(dayIndex, hourIndex, minIndex);
        picker.setOnAddressPickListener(listener);
        picker.show();
    }


    public static void dismissDialog(Dialog dialog){
        if(dialog!=null&&dialog.isShowing()){
            dialog.dismiss();
        }
    }

    public static boolean isShow(Dialog dialog) {
        if(dialog!=null&&dialog.isShowing()){
            return true;
        }
        return false;
    }

    public static class Builder {

        private Context mContext;
        private String mTitle;
        private String mContent;
        private String mConfrimString;
        private String mCancelString;
        private boolean mCancelable;
        private boolean mBackgroundDimEnabled=true;//显示区域以外是否使用黑色半透明背景
        private boolean mInput;//是否是输入框的
        private String mHint;
        private int mInputType;
        private int mLength;
        private SimpleCallback mClickCallback;
        private int mLayoutId;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setTitle(String title) {
            mTitle = title;
            return this;
        }

        public Builder setContent(String content) {
            mContent = content;
            return this;
        }

        public Builder setConfrimString(String confrimString) {
            mConfrimString = confrimString;
            return this;
        }

        public Builder setCancelString(String cancelString) {
            mCancelString = cancelString;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            mCancelable = cancelable;
            return this;
        }

        public Builder setBackgroundDimEnabled(boolean backgroundDimEnabled) {
            mBackgroundDimEnabled = backgroundDimEnabled;
            return this;
        }

        public Builder setInput(boolean input) {
            mInput = input;
            return this;
        }

        public Builder setHint(String hint) {
            mHint = hint;
            return this;
        }

        public Builder setInputType(int inputType) {
            mInputType = inputType;
            return this;
        }

        public Builder setLength(int length) {
            mLength = length;
            return this;
        }

        public Builder setLayoutId(int layoutId) {
            mLayoutId = layoutId;
            return this;
        }

        public Builder setClickCallback(SimpleCallback clickCallback) {
            mClickCallback = clickCallback;
            return this;
        }

        public Dialog build() {
            final Dialog dialog = new Dialog(mContext, mBackgroundDimEnabled ? R.style.dialog : R.style.dialog2);
            if(mLayoutId==0){
               dialog.setContentView(mInput ? R.layout.dialog_input : R.layout.dialog_simple);

            }else{
               dialog.setContentView(mLayoutId);
            }
            dialog.setCancelable(mCancelable);
            dialog.setCanceledOnTouchOutside(mCancelable);
            TextView titleView = (TextView) dialog.findViewById(R.id.title);
            if (!TextUtils.isEmpty(mTitle)) {
                titleView.setText(mTitle);
            }
            Window window = dialog.getWindow();
            window.setWindowAnimations(R.style.dialog_animation);

            final TextView content =dialog.findViewById(R.id.content);


            if (!TextUtils.isEmpty(mHint)) {
                content.setHint(mHint);
            }
            if (!TextUtils.isEmpty(mContent)) {
                content.setText(mContent);
            }
            if (mInputType == INPUT_TYPE_NUMBER) {
                content.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else if (mInputType == INPUT_TYPE_NUMBER_PASSWORD) {
                content.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            } else if (mInputType == INPUT_TYPE_TEXT_PASSWORD) {
                if (ModelUtils.isEMUI() && android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                    content.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                    content.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else{
                    content.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD |  InputType.TYPE_CLASS_TEXT);
                }
            }
            if (mLength > 0 && content instanceof EditText) {
                content.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mLength)});
            }
            TextView btnConfirm = (TextView) dialog.findViewById(R.id.btn_confirm);
            if (!TextUtils.isEmpty(mConfrimString)) {
                btnConfirm.setText(mConfrimString);
            }
            TextView btnCancel = (TextView) dialog.findViewById(R.id.btn_cancel);
            if(mCancelString!=null&&mCancelString.equals(GONE)){
                btnCancel.setVisibility(View.GONE);
            }else if(!TextUtils.isEmpty(mCancelString)){
                btnCancel.setText(mCancelString);
            }

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.btn_confirm) {
                        if (mClickCallback != null) {
                            if (mInput) {
                                mClickCallback.onConfirmClick(dialog, content.getText().toString());
                            } else {
                                dialog.dismiss();
                                mClickCallback.onConfirmClick(dialog, "");
                            }
                        } else {
                            dialog.dismiss();
                        }
                    } else {
                        dialog.dismiss();
                        if (mClickCallback instanceof SimpleCallback2) {
                            ((SimpleCallback2) mClickCallback).onCancelClick();
                        }
                    }
                }
            };
            btnConfirm.setOnClickListener(listener);
            btnCancel.setOnClickListener(listener);
            return dialog;
        }

    }

    public interface DataPickerCallback {
        void onConfirmClick(String year, String month, String day);
    }

    public interface StringArrayDialogCallback {
        void onItemClick(String text, int tag);
    }

    public interface SimpleCallback {
        void onConfirmClick(Dialog dialog, String content);
    }

    public interface SimpleCallback2 extends SimpleCallback {
        void onCancelClick();
    }


    public interface OrderTimeListener extends SimpleCallback {
        void onTimeChoosed(Province province, City city, County county);
    }

}
