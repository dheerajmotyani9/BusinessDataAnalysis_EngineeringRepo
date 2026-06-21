package mapitgis.jalnigam;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import org.json.JSONObject;

import java.util.Locale;
import java.util.Objects;

import mapitgis.jalnigam.core.API;
import mapitgis.jalnigam.core.ApiCaller;
import mapitgis.jalnigam.core.Data;
import mapitgis.jalnigam.core.Login;
import mapitgis.jalnigam.core.SqLite;
import mapitgis.jalnigam.core.Utility;
import mapitgis.jalnigam.databinding.ActivityAssetBinding;

public class AssetActivity extends BaseActivity {
    private AssetAdapter assetAdapter;
    private ActivityAssetBinding binding;
//    private GoogleMap googleMap;
//    private LatLng latLng;
//    private Geocoder geocoder;
//    private boolean show_message;
//    private JSONObject jsonObjectTraverse;


    //    private File photoFile;
    private Login login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAssetBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);//findViewById(R.id.toolbar)
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        login = SqLite.instance(this).getLogin();

        binding.listView.setEmptyView(binding.viewEmpty);

//        geocoder = new Geocoder(this, Locale.getDefault());
//
//        binding.mapView.setVisibility(View.VISIBLE);
//        binding.mapView.onCreate(savedInstanceState);
//        binding.mapView.onResume();// needed to get the map to display immediately
//        binding.mapView.getMapAsync(googleMap -> {
//            this.googleMap = googleMap;
//            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            this.googleMap.setMyLocationEnabled(true);
//            this.googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
//
//            this.googleMap.setOnCameraMoveListener(() -> binding.scrollView.requestDisallowInterceptTouchEvent(true));
//            this.googleMap.setOnCameraIdleListener(() -> binding.scrollView.requestDisallowInterceptTouchEvent(false));
//        });
//
//        binding.imageViewCurrentLocation.setVisibility(View.VISIBLE);
//        binding.imageViewCurrentLocation.setOnClickListener(v -> {
//            if (mFusedLocationClient == null) {
//                show_message = true;
//                binding.textViewLocation.setText("");
//                latLng = null;
//                RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                rotate.setDuration(1000);
//                rotate.setInterpolator(new LinearInterpolator());
//                rotate.setRepeatCount(Animation.INFINITE);
//                rotate.setRepeatMode(Animation.ABSOLUTE);
//                binding.imageViewCurrentLocation.startAnimation(rotate);
//                buildGoogleApiClient();
//            }
//        });

        binding.cardViewButton.setOnClickListener(view -> {
            int type = (int) view.getTag();
//            if (type == 1) {
//                SpinnerItem scheme, esr, component, point;
//                String lastPointID;
//                String review;
//                if (binding.linearLayoutScheme.getTag() == null) {
//                    Utility.show(this, Utility.select(this, R.string.scheme));
//                    return;
//                }
//                scheme = ((SpinnerItem) binding.linearLayoutScheme.getTag());
//                if (binding.linearLayoutESR.getTag() == null) {
////                    Utility.show(this, Utility.select(this, R.string.esr));
////                    return;
//                    esr = new SpinnerItem("", "");
//                } else {
//                    esr = ((SpinnerItem) binding.linearLayoutESR.getTag());
//                }
//                if (binding.linearLayoutComponent.getTag() == null) {
//                    Utility.show(this, Utility.select(this, R.string.component));
//                    return;
//                }
//                component = ((SpinnerItem) binding.linearLayoutComponent.getTag());
//
//                if (binding.linearLayoutPoints.getVisibility() == View.VISIBLE) {
//                    if (binding.linearLayoutPoints.getTag() == null) {
//                        Utility.show(this, Utility.select(this, R.string.points));
//                        return;
//                    }
//                    point = ((SpinnerItem) binding.linearLayoutPoints.getTag());
//                } else {
//                    point = new SpinnerItem("", "");
//                }
//
//                String diameter = "", material = "", depth_cover = "", line_json = "";
//                if (binding.linearLayoutPastPoints.getTag() == null) {
//                    lastPointID = "0";
//                } else {
//                    lastPointID = ((SpinnerItem) binding.linearLayoutPastPoints.getTag()).getKeyString();
//                }
//
//                if (binding.buttonTraverse.getTag() != null) {
//                    if (jsonObjectTraverse == null) {
//                        Utility.show(this, R.string.traverse_msg);
//                        return;
//                    }
//                    diameter = jsonObjectTraverse.optString("diameter");//binding.editTextPipeDiameter.getText().toString();
//                    material = jsonObjectTraverse.optString("material");//binding.editTextMaterialType.getText().toString();
//                    depth_cover = jsonObjectTraverse.optString("depth");
//                    JSONArray jsonArray = jsonObjectTraverse.optJSONArray("traverse");
//                    if (jsonArray == null) {
//                        Utility.show(this, R.string.traverse_msg);
//                        return;
//                    }
//                    line_json = jsonArray.toString();
//                }
//
//                review = binding.editTextReview.getText().toString();
//                if (review.isEmpty()) {
//                    Utility.show(this, Utility.check(this, R.string.review));
//                    return;
//                }
//
//                if (latLng == null) {
//                    Utility.show(this, Utility.check(this, R.string.location));
//                    buildGoogleApiClient();
//                    return;
//                }
//
//                String photo = "";
//                if (binding.linearLayoutImage.getVisibility() == View.VISIBLE) {
//                    if (binding.imageViewPhoto.getTag() == null) {
//                        Utility.show(this, Utility.select(this, R.string.photo));
//                        return;
//                    }
//                    photo = binding.imageViewPhoto.getTag().toString();
//                }
//
//                SqLite.instance(this).addAsset(new Asset(0, lastPointID, scheme.getKeyString(), scheme.getValue(), esr.getKeyString(), esr.getValue(), point.getKeyString(), point.getValue(), component.getKeyString(), component.getValue(), String.valueOf(latLng.latitude), String.valueOf(latLng.longitude), review, photo, Utility.getCurrentTimeUsingDate(), depth_cover, diameter, material, line_json, false), login.getMobile());
//                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
//                dlgAlert.setMessage(R.string.saved_successfully);
//                dlgAlert.setTitle(R.string.success);
//                dlgAlert.setPositiveButton(R.string.ok, (dialog, which) -> showList());
//                dlgAlert.setCancelable(true);
//                dlgAlert.create().show();
//            } else
            if (type == 2) {
                // TODO: 01-08-2024 : this line comment for testing
                showAdd();

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Upload/Discard Confirmation?");
                builder.setMessage("Do you really want to take a action");
                builder.setNeutralButton(R.string.cancel, (d, v) -> d.dismiss());
                builder.setPositiveButton(R.string.upload, null);
                builder.setNegativeButton(R.string.discard, null);
                builder.setCancelable(false);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> upload(alertDialog, null));
                alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(v -> discard(alertDialog, null));
            }
        });

//        ActivityResultLauncher<Intent> activityResultPhoto = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//            if (result.getResultCode() == RESULT_OK) {// && null != result.getData()
//                try {
////                    Bundle extras = result.getData().getExtras();
////                    Bitmap bitmap = (Bitmap) extras.get("data");
//                    Bitmap bitmap = Utility.getResizedBitmap(BitmapFactory.decodeFile(photoFile.getAbsolutePath()));
//                    //noinspection ResultOfMethodCallIgnored
//                    photoFile.delete();
//                    binding.imageViewPhoto.setVisibility(View.VISIBLE);
//                    binding.imageViewPhoto.setImageBitmap(bitmap);
//                    binding.imageViewPhoto.setTag(Utility.base64(bitmap));
//                } catch (Exception e) {
//                    Utility.show(this, e);
//                    binding.imageViewPhoto.setVisibility(View.GONE);
//                    binding.imageViewPhoto.setTag(null);
//                }
//            } else {
//                Utility.show(this, Utility.select(this, R.string.photo));
//                binding.imageViewPhoto.setVisibility(View.GONE);
//                binding.imageViewPhoto.setTag(null);
//            }
//        });
//
//        binding.linearLayoutImage.setOnClickListener(v -> {
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            photoFile = Utility.createImageFile(this);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, String.format("%s.fileProvider", BuildConfig.APPLICATION_ID), photoFile));
//            activityResultPhoto.launch(intent);
//        });

//        binding.radioGroup.setOnCheckedChangeListener((radioGroup, i) -> showList());
//        ActivityResultLauncher<Intent> activityResultMain = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//            try {
//                if (result.getResultCode() == RESULT_OK) {
//                    assert result.getData() != null;
//                    jsonObjectTraverse = new JSONObject(Objects.requireNonNull(result.getData().getStringExtra(Utility.G_KEY1)));
//                    showCurrentLine();
//                } else {
//                    jsonObjectTraverse = null;
//                    assert result.getData() != null;
//                    Utility.show(this, Objects.requireNonNull(result.getData().getStringExtra(Utility.G_KEY)));
//                }
//            } catch (Exception e) {
//                Utility.show(this, e);
//                jsonObjectTraverse = null;
//            }
//        });
//
//
//        binding.buttonTraverse.setOnClickListener(v -> {
//            Intent intent = new Intent(this, UpdateLocationActivity.class);
//            if (binding.linearLayoutPoints.getVisibility() == View.VISIBLE) {
//                if (binding.linearLayoutPoints.getTag() == null) {
//                    Utility.show(this, Utility.select(this, R.string.points));
//                    return;
//                }
//                SpinnerItem pointC = ((SpinnerItem) binding.linearLayoutPoints.getTag());
//                if (binding.linearLayoutPastPoints.getTag() == null) {
//                    Utility.show(this, Utility.select(this, R.string.linked_with_last_point));
//                    return;
//                }
//                SpinnerItem pointP = ((SpinnerItem) binding.linearLayoutPastPoints.getTag());
//                intent.putExtra(Utility.G_KEY, (Point) pointP.getExtra());
//                intent.putExtra(Utility.G_KEY1, (Point) pointC.getExtra());
//            }
//            activityResultMain.launch(intent);
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        showList();
    }

    private void upload(@NonNull AlertDialog alertDialog, @Nullable Asset asset) {
        int type = 0;
        if (asset == null) {
            type = 1;
            asset = assetAdapter.getCheckedItem();
            if (asset == null) {
                alertDialog.dismiss();
                Utility.show(this, "Successfully upload all data");
                return;
            }
        }
        Data data = new Data();
        data.put("imei", login.getMobile());// Utility.getDeviceID(this));
        data.put("SchemeID", asset.getSchemeId());
        data.put("ESRID", asset.getEsrId());
        data.put("esr_name", asset.getEsrName());
        data.put("ComponentID", asset.getComponentId());
        data.put("PointId", asset.getPointId());
        data.put("point_name", asset.getPointName());
        data.put("PastPointId", asset.getPastPointId());
        data.put("Remark", asset.getRemark());
        data.put("Latitude", asset.getLat());
        data.put("Longitude", asset.getLng());
        data.put("CapturedDateTime", asset.getDate());

        data.put("depth_cover", asset.getDepthCover());
        data.put("diameter", asset.getDiameter());
        data.put("material", asset.getMaterial());
        data.put("alignment", asset.getAlignment());

        data.put("material_info_name", asset.getMaterialInfoName());
        data.put("material_info_value", asset.getMaterialInfoValue());

        data.put("line_json", asset.getLineJson());
        data.put("base64String", asset.getPhoto());

        int finalType = type;
        Asset finalAsset = asset;
        new ApiCaller(this, (response, key) -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getBoolean(Utility.SUCCESS)) {
                    assetAdapter.remove(finalAsset);
                    SqLite.instance(this).setAssetUploaded(finalAsset.getSerial());
                    if (finalType == 0) {
                        Utility.show(this, jsonObject.getString(Utility.MESSAGE));
                        alertDialog.dismiss();
                    } else {
                        upload(alertDialog, null);
                    }
                } else {
                    Utility.show(this, jsonObject.getString(Utility.MESSAGE));
                }
            } catch (Exception e) {
                Utility.show(this, e);
            }
        }, 1, data.toString(), getString(R.string.please_wait)).start(API.SaveAsset);
    }

    private void discard(@NonNull AlertDialog alertDialog, @Nullable Asset asset) {
        int type = 0;
        if (asset == null) {
            type = 1;
            asset = assetAdapter.getCheckedItem();
            if (asset == null) {
                alertDialog.dismiss();
                Utility.show(this, "Successfully discard all data");
                return;
            }
        }
        assetAdapter.remove(asset);
        SqLite.instance(this).discardAsset(asset.getSerial());
        if (type == 1) discard(alertDialog, null);
        else alertDialog.dismiss();
    }

    private void showAdd() {
        Utility.open(this, AddAssetActivity.class);
//        clearForm();
//        binding.scrollView.setVisibility(View.VISIBLE);
//        binding.scrollView.scrollTo(0, 0);
//        binding.viewList.setVisibility(View.GONE);
//
//        binding.textViewTitle.setText(R.string.new_asset);
//
//        binding.cardViewButton.setVisibility(View.VISIBLE);
//        binding.cardViewButton.setTag(1);
//        binding.textViewButton.setText(R.string.save);
//        binding.imageViewButton.setImageResource(R.drawable.check);
//
//        SpinnerManager spinnerManager = new SpinnerManager(binding.linearLayoutScheme, 1, this, SqLite.instance(this).GET_SCHEME());
//        if (spinnerManager.getSpinnerItems().size() == 1) {
//            set(spinnerManager.getRequestCode(), spinnerManager.getSpinnerItems().get(0));
//        }
////        new SpinnerManager(binding.linearLayoutPipeDepthCover, 6, this, SqLite.instance(this).GET_DEPTH_COVER());
////        new SpinnerManager(binding.linearLayoutMaterialType, 7, this, SqLite.instance(this).GET_MATERIAL_TYPE());
//        buildGoogleApiClient();
    }

//    private void clearForm() {
//        binding.imageViewPhoto.setVisibility(View.GONE);
//        binding.imageViewPhoto.setTag(null);
//
//        binding.editTextReview.setText("");
//
//        setEmpty(binding.linearLayoutScheme);
//        setEmpty(binding.linearLayoutComponent);
//        setEmpty(binding.linearLayoutESR);
//        setEmpty(binding.linearLayoutPoints);
//        setEmpty(binding.linearLayoutPastPoints);
//        binding.buttonTraverse.setVisibility(View.GONE);
//        binding.buttonTraverse.setTag(null);
////        setEmpty(binding.linearLayoutPipeDepthCover);
////        setEmpty(binding.linearLayoutMaterialType);
////        setEmpty(binding.linearLayoutPipeDiameter);
////        ((TextView) binding.linearLayoutComponent.getChildAt(0)).setText(R.string.select);
////        binding.linearLayoutComponent.setTag(null);
//    }

    private void showList() {
        binding.viewList.setVisibility(View.VISIBLE);
        binding.listView.scrollTo(0, 0);
//        binding.scrollView.setVisibility(View.GONE);

        binding.textViewTitle.setText(R.string.asset_list);

        binding.cardViewButton.setVisibility(View.VISIBLE);
        binding.cardViewButton.setTag(2);
        binding.textViewButton.setText(R.string.add_new);
        binding.imageViewButton.setImageResource(R.drawable.ic_add_circle_outline);

        boolean all = false;//findViewById(binding.radioGroup.getCheckedRadioButtonId()).getTag().toString().equals("2");

        assetAdapter = new AssetAdapter(this, all) {
            @Override
            protected void onUpload(@NonNull Asset asset) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AssetActivity.this);
                builder.setTitle("Are you sure?");
                builder.setMessage("Do you really want to upload");
                builder.setNegativeButton(R.string.cancel, (d, v) -> d.dismiss());
                builder.setPositiveButton(R.string.upload, null);
                builder.setCancelable(false);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> upload(alertDialog, asset));
            }

            @Override
            protected void onDiscard(@NonNull Asset asset) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AssetActivity.this);
                builder.setTitle("Are you sure?");
                builder.setMessage("Do you really want to discard");
                builder.setNegativeButton(R.string.cancel, (d, v) -> d.dismiss());
                builder.setPositiveButton(R.string.discard, null);
                builder.setCancelable(false);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> discard(alertDialog, asset));
            }

            @Override
            protected void onChange(int count) {
                if (count == 0) {
                    binding.cardViewButton.setTag(2);
                    binding.textViewButton.setText(R.string.add_new);
                    binding.imageViewButton.setImageResource(R.drawable.ic_add_circle_outline);
                } else {
                    binding.cardViewButton.setTag(3);
                    binding.textViewButton.setText(String.format(Locale.UK, "%s (%d)", getString(R.string.upload_discard), count));
                    binding.imageViewButton.setImageResource(R.drawable.ic_upload);
                }
            }
        };
        binding.listView.setAdapter(assetAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
//        if (binding.viewList.getVisibility() == View.GONE) {
//            showList();
//        } else {
        super.onBackPressed();
        finish();
//        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 100) {
//            if (data != null && resultCode == RESULT_OK) {
//                set(data.getIntExtra("requestCode", 0), (SpinnerItem) data.getSerializableExtra("spinnerItem"));
//            }
//        }
//    }
//
//    private void set(int code, SpinnerItem spinnerItem) {
//        SpinnerManager spinnerManager;
//        switch (code) {
//            case 1:
//                ((TextView) binding.linearLayoutScheme.getChildAt(0)).setText(spinnerItem.getValue());
//                binding.linearLayoutScheme.setTag(spinnerItem);
//
//                spinnerManager = new SpinnerManager(binding.linearLayoutComponent, 2, this, SqLite.instance(this).GET_COMPONENT(spinnerItem.getKeyString()));
//                if (spinnerManager.getSpinnerItems().size() == 1) {
//                    set(spinnerManager.getRequestCode(), spinnerManager.getSpinnerItems().get(0));
//                } else {
//                    setEmpty(binding.linearLayoutComponent);
//                    setEmpty(binding.linearLayoutESR);
//                    setEmpty(binding.linearLayoutPoints);
//                    setEmpty(binding.linearLayoutPastPoints);
//                    latLngPoint = null;
//                    namePoint = "";
//                    comp_id = 0;
//                }
//                break;
//            case 2:
//                ((TextView) binding.linearLayoutComponent.getChildAt(0)).setText(spinnerItem.getValue());
//                binding.linearLayoutComponent.setTag(spinnerItem);
//                comp_id = Integer.parseInt(spinnerItem.getKeyString());
//
//                if (comp_id == 5) {
//                    binding.textViewPastPoints.setVisibility(View.VISIBLE);
//                    binding.linearLayoutPastPoints.setVisibility(View.VISIBLE);
//                } else {
//                    binding.textViewPastPoints.setVisibility(View.GONE);
//                    binding.linearLayoutPastPoints.setVisibility(View.GONE);
//                }
//
//                if (comp_id == 9 || comp_id == 10 || comp_id == 11) {
//
//                    binding.textViewPhoto.setVisibility(View.GONE);
//                    binding.linearLayoutImage.setVisibility(View.GONE);
//                    binding.imageViewPhoto.setVisibility(View.GONE);
//                    binding.linearLayoutImage.setTag(null);
//
//                    binding.textViewPoints.setVisibility(View.GONE);
//                    binding.linearLayoutPoints.setVisibility(View.GONE);
//                    setEmpty(binding.linearLayoutPoints);
//
//                    binding.textViewESR.setVisibility(View.GONE);
//                    binding.linearLayoutESR.setVisibility(View.GONE);
//                    setEmpty(binding.linearLayoutESR);
//
//                    binding.buttonTraverse.setVisibility(View.VISIBLE);
//                    binding.buttonTraverse.setTag(1);
//
//                    setEmpty(binding.linearLayoutPastPoints);
//                } else {
//                    binding.textViewPhoto.setVisibility(View.VISIBLE);
//                    binding.linearLayoutImage.setVisibility(View.VISIBLE);
//                    binding.linearLayoutImage.setTag(null);
//
//                    binding.textViewPoints.setVisibility(View.VISIBLE);
//                    binding.linearLayoutPoints.setVisibility(View.VISIBLE);
//                    setEmpty(binding.linearLayoutPoints);
//
//                    binding.textViewESR.setVisibility(View.VISIBLE);
//                    binding.linearLayoutESR.setVisibility(View.VISIBLE);
//                    setEmpty(binding.linearLayoutESR);
//
//                    binding.buttonTraverse.setVisibility(View.GONE);
//                    binding.buttonTraverse.setTag(null);
//
//
//                    new SpinnerManager(binding.linearLayoutPastPoints, 5, this, SqLite.instance(this).GET_PAST_POINT(spinnerItem.getKeyString()));
//                    setEmpty(binding.linearLayoutPastPoints);
//
//                    SpinnerItem spinnerItem1 = (SpinnerItem) binding.linearLayoutScheme.getTag();
//                    spinnerManager = new SpinnerManager(binding.linearLayoutESR, 3, this, SqLite.instance(this).GET_ESR_NEW(spinnerItem1.getKeyString(), spinnerItem.getKeyString()));
//                    if (spinnerManager.getSpinnerItems().size() == 1) {
//                        set(spinnerManager.getRequestCode(), spinnerManager.getSpinnerItems().get(0));
//                    } else {
//                        setEmpty(binding.linearLayoutESR);
////                    setEmpty(linearLayoutPoints);
//
//                        spinnerManager = new SpinnerManager(binding.linearLayoutPoints, 4, this, SqLite.instance(this).GET_POINTS(spinnerItem1.getKeyString(), spinnerItem.getKeyString(), null));
//                        if (spinnerManager.getSpinnerItems().size() == 1) {
//                            set(spinnerManager.getRequestCode(), spinnerManager.getSpinnerItems().get(0));
//                        } else {
//                            setEmpty(binding.linearLayoutPoints);
//                            latLngPoint = null;
//                            namePoint = "";
//                        }
////                    latLngPoint = null;
////                    namePoint = "";
//                    }
//                }
//
//                jsonObjectTraverse = null;
//                showPoint(true);
//                break;
//            case 3:
//                ((TextView) binding.linearLayoutESR.getChildAt(0)).setText(spinnerItem.getValue());
//                binding.linearLayoutESR.setTag(spinnerItem);
//                SpinnerItem spinnerItem2 = (SpinnerItem) binding.linearLayoutScheme.getTag();
//                SpinnerItem spinnerItem3 = (SpinnerItem) binding.linearLayoutComponent.getTag();
//                spinnerManager = new SpinnerManager(binding.linearLayoutPoints, 4, this, SqLite.instance(this).GET_POINTS(spinnerItem2.getKeyString(), spinnerItem3.getKeyString(), spinnerItem.getKeyString()));
//                if (spinnerManager.getSpinnerItems().size() == 1) {
//                    set(spinnerManager.getRequestCode(), spinnerManager.getSpinnerItems().get(0));
//                } else {
//                    setEmpty(binding.linearLayoutPoints);
//                    latLngPoint = null;
//                    namePoint = "";
//                }
//                break;
//            case 4:
//                ((TextView) binding.linearLayoutPoints.getChildAt(0)).setText(spinnerItem.getValue());
//                binding.linearLayoutPoints.setTag(spinnerItem);
//                if (spinnerItem.getExtra() != null) {
//                    Point point = (Point) spinnerItem.getExtra();
//                    latLngPoint = new LatLng(point.getLat(), point.getLng());
//                    namePoint = spinnerItem.getValue();
//                } else {
//                    latLngPoint = null;
//                    namePoint = "";
//                }
//                showPoint(true);
//                break;
//
//            case 5:
//                ((TextView) binding.linearLayoutPastPoints.getChildAt(0)).setText(spinnerItem.getValue());
//                binding.linearLayoutPastPoints.setTag(spinnerItem);
//                if (spinnerItem.getKeyString().isEmpty()) {
//                    binding.buttonTraverse.setVisibility(View.GONE);
//                    binding.buttonTraverse.setTag(null);
//                    jsonObjectTraverse = null;
//
////                    setEmpty(binding.linearLayoutPipeDepthCover);
////                    setEmpty(binding.linearLayoutPipeDiameter);
////                    setEmpty(binding.linearLayoutMaterialType);
//
////                    binding.editTextMaterialType.setText("");
////                    binding.editTextPipeDiameter.setText("");
//                } else {
//                    binding.buttonTraverse.setVisibility(View.VISIBLE);
//                    binding.buttonTraverse.setTag(1);
//                    jsonObjectTraverse = null;
//                }
//                showPoint(false);
//                break;
//        }
//    }
//
//    private LatLng latLngPoint;
//    private String namePoint;
//    private int comp_id;

//    private void showPoint(boolean navigate) {
//        if (googleMap != null) {
//            googleMap.clear();
//            if (comp_id > 0) {
//                TileProvider tileProvider = TileProviderFactory.getTileProvider(TileProviderFactory.LAYERS[comp_id - 1]);
//                this.googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider));
//            }
//
//            TileProvider tileProvider1 = TileProviderFactory.getTileProvider(TileProviderFactory.LAYER_1);
//            this.googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider1));
//
//            TileProvider tileProvider2 = TileProviderFactory.getTileProvider(TileProviderFactory.LAYER_2);
//            this.googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider2));
//
//            TileProvider tileProvider3 = TileProviderFactory.getTileProvider(TileProviderFactory.LAYER_3);
//            this.googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider3));
//
//            Marker marker2;
//
//            if (latLngPoint != null) {
//                marker2 = googleMap.addMarker(new MarkerOptions().position(latLngPoint).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(namePoint));
//            } else {
//                marker2 = null;
//            }
////            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//            googleMap.getUiSettings().setZoomControlsEnabled(true);
//
////            if (latLng != null && latLngPoint != null) {
////                LatLngBounds.Builder builder = new LatLngBounds.Builder();
////                builder.include(latLngPoint).include(latLng);
////                googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100), 2000, null);//.zoomTo(15), 2000, null);
////            } else
//            if (navigate) {
//                if (latLngPoint != null) {
//                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngPoint, 17), 2000, null);//.zoomTo(15), 2000, null);
//                } else if (latLng != null) {
//                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17), 2000, null);//.zoomTo(15), 2000, null);
//                }
//            }
//
//            showPastLine();
//
//            if (latLng != null) {
//                googleMap.addMarker(new MarkerOptions().position(latLng).title("Your Location"));
//            }
//
//            showCurrentLine();
//
//
//            new Handler().postDelayed(() -> {
//                try {
////                    if (marker1 != null) {
////                        marker1.showInfoWindow();
////                    }
//                    if (marker2 != null) {
//                        marker2.showInfoWindow();
//                    }
//                } catch (Exception ignore) {
//                }
//            }, 2000);
//        }
//    }

//    private void showCurrentLine() {
//        try {
//            if (jsonObjectTraverse != null) {
//                binding.buttonTraverse.setVisibility(View.GONE);
//                PolylineOptions polylineOptions = new PolylineOptions();
//                polylineOptions.color(Color.BLUE);
//                polylineOptions.width(15);
//                JSONArray jsonArray = jsonObjectTraverse.getJSONArray("traverse");
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                    polylineOptions.add(new LatLng(jsonObject.getDouble("lat"), jsonObject.getDouble("lng")));
//                }
//                googleMap.addPolyline(polylineOptions);
//            }
//        } catch (Exception ignore) {
//        }
//    }

//    private void showPastLine() {
//        List<Line> lines = SqLite.instance(this).getAssetLine();
//        for (Line line : lines) {
//            googleMap.addMarker(new MarkerOptions().position(line.getPoint1()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).title(line.getName()));
//            if (line.getPoint2() != null) {
//
//                PolylineOptions polylineOptions = new PolylineOptions();
//                polylineOptions.color(Color.BLUE);
//                polylineOptions.width(15);
//                polylineOptions.add(line.getPoint2());
//
//                try {
//                    JSONArray jsonArray = line.getJsonArray();
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        polylineOptions.add(new LatLng(jsonObject.getDouble("lat"), jsonObject.getDouble("lng")));
//                    }
//                } catch (Exception ignore) {
//                }
//
//                polylineOptions.add(line.getPoint1());
//                googleMap.addPolyline(polylineOptions);
//            }
//        }
//    }

//    private void setEmpty(@NonNull LinearLayout linearLayout) {
//        ((TextView) linearLayout.getChildAt(0)).setText(R.string.select);
//        linearLayout.setTag(null);
//    }
//
//
//    private void connectGoogleClient() {
//        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
//        int resultCode = googleAPI.isGooglePlayServicesAvailable(this);
//        if (resultCode == ConnectionResult.SUCCESS) {
//            mGoogleApiClient.connect();
//        }
//    }
//
//    @SuppressWarnings("deprecation")
//    private GoogleApiClient mGoogleApiClient;
//    private FusedLocationProviderClient mFusedLocationClient;
//    private LocationCallback mLocationCallback;
//    private LocationRequest mLocationRequest;
//
//    @SuppressWarnings("deprecation")
//    private void buildGoogleApiClient() {
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        SettingsClient mSettingsClient = LocationServices.getSettingsClient(this);
//        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
//            @Override
//            public void onConnected(@Nullable Bundle bundle) {
//                mLocationRequest = new LocationRequest();
//                mLocationRequest.setInterval(10 * 1000);
//                mLocationRequest.setFastestInterval(5 * 1000);
//                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
//                builder.addLocationRequest(mLocationRequest);
//                builder.setAlwaysShow(true);
//                LocationSettingsRequest mLocationSettingsRequest = builder.build();
//
//                mSettingsClient.checkLocationSettings(mLocationSettingsRequest).addOnSuccessListener(locationSettingsResponse -> requestLocationUpdate()).addOnFailureListener(e -> {
//                    int statusCode = ((ApiException) e).getStatusCode();
//                    if (statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
//                        try {
//                            int REQUEST_CHECK_SETTINGS = 214;
//                            ResolvableApiException rae = (ResolvableApiException) e;
//                            rae.startResolutionForResult(AssetActivity.this, REQUEST_CHECK_SETTINGS);
//                        } catch (Exception ignore) {
//                        }
//                        //                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                    }
//                }).addOnCanceledListener(() -> {
//
//                });
//            }
//
//            @Override
//            public void onConnectionSuspended(int i) {
//                connectGoogleClient();
//            }
//        }).addOnConnectionFailedListener(connectionResult -> buildGoogleApiClient()).addApi(LocationServices.API).build();
//
//        connectGoogleClient();
//
//        mLocationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(@NonNull LocationResult locationResult) {
//                super.onLocationResult(locationResult);
//                Location location = locationResult.getLastLocation();
//                assert location != null;
//                if (isCorrect(location)) {
//                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                    showLocation();
//                    binding.imageViewCurrentLocation.setAnimation(null);
//                    if (show_message) {
//                        show_message = false;
//                        Utility.show(AssetActivity.this, "Location Refreshed");
//                    }
//
//                    if (mFusedLocationClient != null) {
//                        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
//                        mFusedLocationClient = null;
//                    }
//                }
//            }
//        };
//    }
//
//    private boolean isCorrect(@NonNull Location location) {
//        return !(location.getAccuracy() > 15);
//    }
//
//    @SuppressLint("MissingPermission")
//    private void requestLocationUpdate() {
//        if (mFusedLocationClient != null)
//            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (mFusedLocationClient != null) {
//            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
//            mFusedLocationClient = null;
//        }
//    }
//
//    private void showLocation() {
//        if (latLngPoint != null) {
//            if (Utility.distance(latLng, latLngPoint) <= 20) {
//                latLng = latLngPoint;
//            }
//        }
//
//        showPoint(false);
////        if (googleMap != null && latLng != null) {
////            googleMap.clear();
////            googleMap.addMarker(new MarkerOptions().position(latLng).title("Your Location"));
//////            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
////            googleMap.getUiSettings().setZoomControlsEnabled(true);
////            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17), 2000, null);//.zoomTo(15), 2000, null);
////        }
//        String address = "";
//        try {
//            if (latLng != null) {
//                List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
//                assert addresses != null;
//                String area = addresses.get(0).getSubLocality();
//                String city = addresses.get(0).getLocality();
//                String street = addresses.get(0).getAddressLine(0);
//                address = area;
//                address += " " + city;
//                address += " " + street;
//                address = address.replace("null", "").trim();
//            }
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        }
//        binding.textViewLocation.setText(address.equals("") ? (latLng == null ? "Location is null." : ("Lat:" + latLng.latitude + " Long:" + latLng.longitude)) : address);
//    }
}
