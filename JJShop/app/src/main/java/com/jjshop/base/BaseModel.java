package com.jjshop.base;


/**
 * Created by youseikanbou on 17/12/4.
 */

public class BaseModel implements IModel{

    public BasePresenter basePresenter;

    public BaseModel(BasePresenter basePresenter) {
        this.basePresenter = basePresenter;
    }

}
