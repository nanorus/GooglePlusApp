package com.official.nanorus.googleplusapp.presentation.view.businessman_info;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.official.nanorus.googleplusapp.R;
import com.official.nanorus.googleplusapp.entity.business.api.Address;
import com.official.nanorus.googleplusapp.entity.business.api.Businessman;
import com.official.nanorus.googleplusapp.entity.business.api.Geo;
import com.official.nanorus.googleplusapp.model.repository.business.BusinessRepository;
import com.official.nanorus.googleplusapp.presentation.presenter.BusinessmanInfoPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BusinessmanInfoActivity extends AppCompatActivity implements OnMapReadyCallback, IBusinessmanInfoView {
    private String TAG = this.getClass().getName();
    public static final String INTENT_BUSINESSMAN_ID = "businessmanId";
    public static final int PERMISSION_REQUEST_LOCATION = 1;

    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private BusinessmanInfoPresenter presenter;
    @BindView(R.id.tv_name)
    TextView nameTextView;
    @BindView(R.id.tv_username)
    TextView usernameTextView;
    @BindView(R.id.tv_email)
    TextView emailTextView;
    @BindView(R.id.tv_address)
    TextView addressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_businessman_info);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int businessmanId = getIntent().getIntExtra(INTENT_BUSINESSMAN_ID, -1);
        presenter = new BusinessmanInfoPresenter();
        presenter.bindView(this);
        presenter.start(businessmanId);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        presenter.onMapReady();
    }

    @Override
    public Activity getView() {
        return this;
    }

    @Override
    public void checkLocationPermissions() {
        presenter.onLocationPermissionsChecked(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void setMarker(Businessman businessman) {
        Geo geo = businessman.getAddress().getGeo();
        LatLng latLng = new LatLng(Double.valueOf(geo.getLat()), Double.valueOf(geo.getLng()));
        map.addMarker(new MarkerOptions().position(latLng).title(businessman.getName()));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
    }

    @Override
    public void fillInfo(Businessman businessman) {
        nameTextView.setText(businessman.getName());
        emailTextView.setText(businessman.getEmail());
        usernameTextView.setText(businessman.getUsername());
        Address address = businessman.getAddress();
        addressTextView.setText(address.getCity() + ", " + address.getStreet() + ", " + address.getZipcode());
        getSupportActionBar().setTitle(businessman.getName());
    }

    @Override
    public void showMyLocationButton(boolean show) {
        map.setMyLocationEnabled(show);
    }

    @Override
    public void requestLocationPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                PERMISSION_REQUEST_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_LOCATION: {
                presenter.onLocationPermissionsRequestResult(
                        grantResults.length > 0
                                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                                && grantResults[1] == PackageManager.PERMISSION_GRANTED
                );
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                presenter.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.releasePresenter();
    }
}
