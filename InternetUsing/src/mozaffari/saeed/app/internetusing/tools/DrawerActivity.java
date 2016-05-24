package mozaffari.saeed.app.internetusing.tools;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import mozaffari.saeed.app.internetusing.ActivityMain;
import mozaffari.saeed.app.internetusing.R;


@SuppressLint({ "NewApi", "InflateParams" })
public class DrawerActivity extends ActionBarActivity {

    private DrawerLayout             mDrawerLayout;
    private ListView                 mDrawerList;
    private ActionBarDrawerToggle    mDrawerToggle;
    protected RelativeLayout         _completeLayout, _activityLayout;
    private CharSequence             mDrawerTitle;
    private CharSequence             mTitle;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter     adapter;
    private String[]                 navMenuTitles;
    private ActionBarRtlizer         rtlizer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        rtlizer = new ActionBarRtlizer(this);
        ViewGroup actionBarView = rtlizer.getActionBarView();
        ViewGroup homeView = (ViewGroup) rtlizer.findViewByClass("HomeView", actionBarView);

        rtlizer.flipActionBarUpIconIfAvailable(homeView);
        RtlizeEverything.rtlize(actionBarView);
        RtlizeEverything.rtlize(homeView);
    }


    public void set() {

        mTitle = mDrawerTitle = getTitle();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        navMenuTitles = getResources().getStringArray(R.array.items);

        for (int i = 0; i < navMenuTitles.length; i++) {
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[i]));
        }

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);

        View header = getLayoutInflater().inflate(R.layout.nav_head, null);
        TextView txtHead = (TextView) header.findViewById(R.id.txt_head);
        txtHead.setTypeface(G.persianFont);

        ImageView imghead = (ImageView) header.findViewById(R.id.img_head);
        imghead.setImageResource(R.drawable.ic_launcher);

        mDrawerList.addHeaderView(header);
        mDrawerList.setAdapter(adapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_launcher, R.string.drawer_title) {

            @Override
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                supportInvalidateOptionsMenu();
            }


            @Override
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                supportInvalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }


    // =====================SlideMenuClick===================

    private class SlideMenuClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            // display view for selected nav drawer item

            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    G.handler.post(new Runnable() {

                        @Override
                        public void run() {
                            displayView(position);
                        }
                    });
                }
            });
            thread.start();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getSupportMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                mDrawerLayout.closeDrawer(mDrawerList);
            } else {
                mDrawerLayout.openDrawer(mDrawerList);
            }
        }

        return super.onOptionsItemSelected(item);
    }


    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        // boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        // menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }


    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    private void displayView(int position) {
        // update the main content by replacing fragments

        switch (position) {

            case 0:
                //nothing
                break;
            case 1:
                Intent startupIntent = new Intent(DrawerActivity.this, ActivityMain.class);
                startActivity(startupIntent);
                finish();
                break;
            case 2:
                Intent favoritIntent = new Intent(DrawerActivity.this, ActivityMain.class);
                startActivity(favoritIntent);
                finish();
                break;
            case 3:
                Intent visitSortIntent = new Intent(DrawerActivity.this, ActivityMain.class);
                startActivity(visitSortIntent);
                finish();
                break;
            case 4:
                Intent userWordsIntent = new Intent(DrawerActivity.this, ActivityMain.class);
                startActivity(userWordsIntent);
                finish();
                break;
            case 5:
                Intent aboutIntent = new Intent(DrawerActivity.this, ActivityMain.class);
                startActivity(aboutIntent);
                finish();
                break;
            case 6:
                finish();
                break;
        }
        mDrawerList.setItemChecked(position, true);
        mDrawerList.setSelection(position);
        mDrawerLayout.closeDrawer(mDrawerList);
    }


    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        // getActionBar().setTitle(mTitle);
    }


    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}