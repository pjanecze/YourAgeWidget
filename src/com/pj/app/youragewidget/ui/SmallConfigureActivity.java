package com.pj.app.youragewidget.ui;

import com.pj.app.youragewidget.MediumWidgetProvider;
import com.pj.app.youragewidget.SmallWidgetProvider;

/**
 * Created by IntelliJ IDEA.
 * User: pjaneczek
 * Date: 20.03.2012
 * Time: 11:02
 * To change this template use File | Settings | File Templates.
 */
public class SmallConfigureActivity extends  WidgetConfigureActivity {
    @Override
    protected int getWidgetWidth() {
        return 2;
    }

    @Override
    protected Class getProviderClass() {
        return SmallWidgetProvider.class;
    }
}
