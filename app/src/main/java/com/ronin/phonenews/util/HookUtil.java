package com.ronin.phonenews.util;

import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by donghailong on 2018/3/8.
 */

public class HookUtil {

    public void hookOnClick(View view) {
        try {
            Class<?> viewClass = Class.forName("android.view.View");
            Method declaredMethod = viewClass.getDeclaredMethod("getListenerInfo");
            declaredMethod.setAccessible(true);
            Object listenerInfoInstance = declaredMethod.invoke(view);

            Class mClassListenerInfo = Class.forName("android.view.View$ListenerInfo");
            Field field = mClassListenerInfo.getDeclaredField("mOnClickListener");
            field.setAccessible(true);
            View.OnClickListener onClickListener = (View.OnClickListener) field.get(listenerInfoInstance);

            field.set(listenerInfoInstance, new OnClickListenerProxy(onClickListener));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }

    public interface IHookOnClickListener {
        void onPrepare();
    }

    static class OnClickListenerProxy implements View.OnClickListener {
        private View.OnClickListener onClickListener;
        private IHookOnClickListener iHookOnClickListener = new IHookOnClickListener() {
            @Override
            public void onPrepare() {

            }
        };

        public OnClickListenerProxy(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }

        @Override
        public void onClick(View v) {
            if (null != iHookOnClickListener) {
                iHookOnClickListener.onPrepare();
            }
            if (null != onClickListener) {
                onClickListener.onClick(v);
            }
        }
    }
}
