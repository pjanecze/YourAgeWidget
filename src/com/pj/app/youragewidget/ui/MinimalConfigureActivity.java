package com.pj.app.youragewidget.ui;

import com.pj.app.youragewidget.MediumWidgetProvider;
import com.pj.app.youragewidget.MinimalWidgetProvider;

/**
 * Created by IntelliJ IDEA.
 * User: pjaneczek
 * Date: 20.03.2012
 * Time: 11:01
 * To change this template use File | Settings | File Templates.
 */
public class MinimalConfigureActivity extends  WidgetConfigureActivity {
    @Override
    protected int getWidgetWidth() {
        return 1;
    }

    @Override
    protected Class getProviderClass() {
        return MinimalWidgetProvider.class;
    }
}
