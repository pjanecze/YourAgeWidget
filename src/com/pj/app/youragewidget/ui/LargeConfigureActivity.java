package com.pj.app.youragewidget.ui;

import com.pj.app.youragewidget.LargeWidgetProvider;
import com.pj.app.youragewidget.MediumWidgetProvider;

/**
 * Created by IntelliJ IDEA.
 * User: pjaneczek
 * Date: 20.03.2012
 * Time: 11:02
 * To change this template use File | Settings | File Templates.
 */
public class LargeConfigureActivity extends  WidgetConfigureActivity{
    @Override
    protected int getWidgetWidth() {
        return 4;
    }

    @Override
    protected Class getProviderClass() {
        return LargeWidgetProvider.class;
    }
}
