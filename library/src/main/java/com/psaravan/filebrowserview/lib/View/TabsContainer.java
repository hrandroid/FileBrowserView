/*
 * Copyright (C) 2014 Saravan Pantham
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.psaravan.filebrowserview.lib.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.psaravan.filebrowserview.lib.GridLayout.GridLayoutView;
import com.psaravan.filebrowserview.lib.ListLayout.ListLayoutView;
import com.psaravan.filebrowserview.lib.R;

import java.io.File;

/**
 * Container view for tabbed browsing. Includes the TabHost,
 * TabWidget, and the actual tab content. If tabbed browsing
 * is enabled, this view becomes the direct child of the
 * parent FileBrowserView instance. Each tab will then host
 * either a {@link com.psaravan.filebrowserview.lib.ListLayout.ListLayoutView} view
 * or a {@link com.psaravan.filebrowserview.lib.GridLayout.GridLayoutView} view.
 *
 * @author Saravan Pantham
 */
public class TabsContainer extends View {

    private Context mContext;
    private FileBrowserView mFileBrowserView;
    protected TabHost mTabHost;
    protected TabWidget mTabWidget;
    protected FrameLayout mTabContentLayout;

    public TabsContainer(Context context, FileBrowserView fileBrowserView) {
        super(context);
        mContext = context;
        mFileBrowserView = fileBrowserView;

    }

    public TabsContainer(Context context, AttributeSet attrs, FileBrowserView fileBrowserView) {
        super(context, attrs);
        mContext = context;
        mFileBrowserView = fileBrowserView;

    }

    /**
     * Initializes this view instance and opens the default tab/directory structure. Also
     * attaches a "New Tab" button to the TabWidget.
     *
     * @return The {@link com.psaravan.filebrowserview.lib.View.BaseLayoutView} instance that
     *         is inflated inside the default tab.
     *
     */
    public BaseLayoutView init() {

        //Initialize the tabbed container.
        View view = View.inflate(mContext, R.layout.tabbed_browser_container, mFileBrowserView);
        mTabHost = (TabHost) view.findViewById(R.id.tabHost);
        mTabWidget = (TabWidget) view.findViewById(android.R.id.tabs);
        mTabContentLayout = (FrameLayout) view.findViewById(android.R.id.tabcontent);

        //Initialize the TabHost.
        mTabHost.setup();

        //Add a "New Tab" button.
        addNewTabButton();

        //Open the default tab.
        return openNewBrowserTab(mFileBrowserView.getDefaultDirectory());
    }

    /**
     * Creates a new default tab that will allow the user to open other new
     * tabs. This tab will not have any view attached to it.
     */
    protected void addNewTabButton() {
        if (mTabHost==null)
            return;

        //Add the new tab button.
        TabHost.TabSpec newTabSpec = mTabHost.newTabSpec("new_tab_button");
        newTabSpec.setIndicator("", mContext.getResources().getDrawable(R.drawable.ic_action_new));
        newTabSpec.setContent(R.id.dummy);
        mTabHost.addTab(newTabSpec);

    }

    /**
     * Opens a brand new browser tab.
     *
     * @param directory The directory to open when this tab is initialized.
     */
    protected BaseLayoutView openNewBrowserTab(File directory) {


        //Inflate the view's layout based on the selected layout.
        BaseLayoutView contentView = null;
        if (mFileBrowserView.getFileBrowserLayoutType()==FileBrowserView.FILE_BROWSER_LIST_LAYOUT)
            contentView = new ListLayoutView(mContext, mFileBrowserView.getAttributeSet(), mFileBrowserView).init();
        else
            contentView = new GridLayoutView(mContext, mFileBrowserView.getAttributeSet(), mFileBrowserView).init();

        //Add a new layout to the TabHost.
        contentView.setId(mTabWidget.getTabCount() + 1);
        mTabContentLayout.addView(contentView);

        //Add the new tab to the TabHost.
        String directoryName = directory.getName();
        TabHost.TabSpec newTabSpec = mTabHost.newTabSpec(directoryName);
        newTabSpec.setIndicator(directoryName);
        newTabSpec.setContent(mTabHost.getTabWidget().getTabCount() + 1);
        mTabHost.addTab(newTabSpec);

        return contentView;
    }

}