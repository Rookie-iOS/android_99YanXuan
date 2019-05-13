package com.jjshop.base;

import java.lang.ref.WeakReference;

/**
 * Created by youseikanbou on 17/12/4.
 */

public abstract class BasePresenter<V extends IView> implements IView{

    private WeakReference weakReference;

    public BasePresenter(IView iView) {
        weakReference = new WeakReference(iView);
    }

    public V getIView() {
        if (null != weakReference) {
            return (V) weakReference.get();
        }
        return null;
    }

    public void clearData() {
        if (null != weakReference) {
            weakReference.clear();
            weakReference = null;
        }
    }

    public abstract IModel loadBaseModel();
}
