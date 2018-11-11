package com.official.nanorus.googleplusapp.presentation.view.business;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.official.nanorus.googleplusapp.R;
import com.official.nanorus.googleplusapp.entity.auth.Account;
import com.official.nanorus.googleplusapp.entity.business.api.Businessman;
import com.official.nanorus.googleplusapp.presentation.presenter.BusinessPresenter;
import com.official.nanorus.googleplusapp.presentation.ui.adapters.BusinessmenRecyclerViewAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.gpu.BrightnessFilterTransformation;

public class BusinessActivity extends AppCompatActivity implements IBusinessView {
    private String TAG = this.getClass().getName();

    FirebaseAuth auth;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    ImageView drawerHeaderAvatarImageView;
    TextView drawerHeaderNameTextView;
    TextView drawerHeaderEmailTextView;
    ImageView drawerHeaderBackgroundImageView;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    ConstraintLayout navigationHeader;
    ActionBarDrawerToggle drawerToggle;

    BusinessPresenter presenter;
    BusinessmenRecyclerViewAdapter adapter;
    @BindView(R.id.rv_businessmen)
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();

        Account account = Account.map(auth.getCurrentUser());
        setupNavigationDrawer(account);

        initList();
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.onRefresh());
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.swipe_refresh_color_1),
                getResources().getColor(R.color.swipe_refresh_color_2), getResources().getColor(R.color.swipe_refresh_color_3));

        presenter = new BusinessPresenter();
        presenter.bindView(this);
    }

    private void initList() {
        layoutManager = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
        adapter = new BusinessmenRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onViewStart();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupNavigationDrawer(Account account) {
        View headerLayout = navigationView.getHeaderView(0);
        drawerHeaderAvatarImageView = headerLayout.findViewById(R.id.drawer_header_avatar);
        drawerHeaderNameTextView = headerLayout.findViewById(R.id.drawer_header_name);
        drawerHeaderEmailTextView = headerLayout.findViewById(R.id.drawer_header_email);
        drawerHeaderBackgroundImageView = headerLayout.findViewById(R.id.drawer_header_background);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationHeader = (ConstraintLayout) navigationView.getHeaderView(0);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                supportInvalidateOptionsMenu();
            }
        };
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerLayout.setDrawerListener(drawerToggle);

        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_item_entrepreneurs:
                    Toast.makeText(this, "entrepreneurs", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.menu_item_log_out:
                    Toast.makeText(this, "log_out", Toast.LENGTH_SHORT).show();
                    presenter.onLogOutClicked();
                    break;
            }
            return false;
        });

        drawerHeaderNameTextView.setText(account.getName());
        drawerHeaderEmailTextView.setText(account.getEmail());

        Glide.with(this)
                .load(account.getAvatar())
                .apply(RequestOptions.circleCropTransform())
                .into(drawerHeaderAvatarImageView);

        MultiTransformation<Bitmap> multiTransformation = new MultiTransformation<>(
                new BlurTransformation(60),
                new BrightnessFilterTransformation(-0.2f),
                new CenterCrop());
        Glide.with(this)
                .load(account.getAvatar())
                .apply(RequestOptions.bitmapTransform(multiTransformation))
                .into(drawerHeaderBackgroundImageView);

        drawerLayout.closeDrawers();
    }

    @Override
    public Activity getView() {
        return this;
    }

    @Override
    public void checkAuth() {
        presenter.onAuthChecked(auth.getCurrentUser() != null);
    }

    @Override
    public void clearBusinessmenList() {
        adapter.clearList();
    }

    @Override
    public void updateBusinessmenList(List<Businessman> businessmenList) {
        adapter.updateList(businessmenList);
        adapter.notifyDataSetChanged();
        for (Businessman businessman : businessmenList) {
            Log.d(TAG, businessman.getEmail());
        }
    }

    @Override
    public void updateBusinessmenList(Businessman businessman) {
        adapter.addData(businessman);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showProgress(boolean b) {
        swipeRefreshLayout.setRefreshing(b);
        adapter.suspendItemClickListener(b);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.releasePresenter();
        presenter = null;
    }
}

