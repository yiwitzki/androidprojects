package com.tp.Calculator.base;

import android.app.Activity;

/**
 * Created by tp on 2015/12/9.
 */
public abstract class BaseActivity extends Activity
{
    @Override
    public void setContentView(int layoutResID)
    {
        super.setContentView(layoutResID);
        findView();
        initView();
        setOnClick();
    }

    protected abstract void findView();

    protected abstract void initView();

    protected abstract void setOnClick();
}
