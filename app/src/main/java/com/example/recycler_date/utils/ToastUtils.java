package com.example.recycler_date.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recycler_date.R;

public class ToastUtils {
    /**
     * 描述：Toast提示文本.
     * Toast的样式可以自定义，Toast的位置也可以自定义 只用时间是只能用系统的
     * @param text 文本
     */
    public static void showToast(Context context, String text) {
        // 获取布局解析器
        LayoutInflater inflate = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (!TextUtils.isEmpty(text) && null != inflate) {
            // 解析布局
            View layout = inflate.inflate(R.layout.layout_toast, null);
            TextView tvMsg = (TextView) layout.findViewById((R.id.tv_msg));
            tvMsg.setText(text);
            Toast toast = new Toast(context.getApplicationContext());
            // 底部距离150
            toast.setGravity(Gravity.BOTTOM, 0, 150);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }

    }


    /**
     * 描述：Toast提示文本.
     *
     * @param resId 文本的资源ID
     */
    public static void showToast(Context context, int resId) {
        Context mContext = context.getApplicationContext();
        showToast(mContext, mContext.getResources().getString(resId));
    }
}
