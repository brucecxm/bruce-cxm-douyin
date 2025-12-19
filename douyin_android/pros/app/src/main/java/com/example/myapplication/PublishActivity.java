package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.myapplication.data.LocalDataManager;
import com.example.myapplication.model.ApiResponse;
import com.example.myapplication.utils.ApiConstants;
import com.example.myapplication.utils.OkHttpManager;
import com.google.gson.Gson;

import java.util.Map;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PublishActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;

    private ImageView productImageView;
    private EditText nameEditText;
    private EditText descriptionEditText;
    private EditText priceEditText;
    private EditText phoneEditText;
    private Spinner categorySpinner;
    private RadioGroup conditionRadioGroup;
    private Button selectImageButton;
    private Button publishButton;

    private LocalDataManager dataManager;
    private String selectedCategory;
    private String selectedCondition = "全新";
    private String currentPhotoPath;
    private String uploadedImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        dataManager = LocalDataManager.getInstance();
        initViews();
        setupCategorySpinner();
        setupConditionRadioGroup();
        setupListeners();
    }

    private void initViews() {
        productImageView = findViewById(R.id.productImageView);
        nameEditText = findViewById(R.id.nameEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        priceEditText = findViewById(R.id.priceEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        conditionRadioGroup = findViewById(R.id.conditionRadioGroup);
        selectImageButton = findViewById(R.id.selectImageButton);
        publishButton = findViewById(R.id.publishButton);

        // 设置默认联系电话
        phoneEditText.setText(dataManager.getCurrentUser().getPhone());
    }

    private void setupCategorySpinner() {
        List<String> categories = dataManager.getCategories();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (categories.size() > 0) {
                    selectedCategory = categories.get(0);
                }
            }
        });
    }

    private void setupConditionRadioGroup() {
        conditionRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.newRadioButton) {
                selectedCondition = "全新";
            } else if (checkedId == R.id.likeNewRadioButton) {
                selectedCondition = "九成新";
            } else if (checkedId == R.id.goodRadioButton) {
                selectedCondition = "八成新";
            } else if (checkedId == R.id.fairRadioButton) {
                selectedCondition = "七成新";
            }
        });
    }

    private void setupListeners() {
        selectImageButton.setOnClickListener(v -> {
            showImageSourceDialog();
        });

        publishButton.setOnClickListener(v -> {
            if (validateForm()) {
                if (uploadedImageUrl == null) {
                    Toast.makeText(this, "请先选择并上传图片", Toast.LENGTH_SHORT).show();
                } else {
                    publishProduct();
                }
            }
        });
    }

    private void showImageSourceDialog() {
        CharSequence[] options = { "拍照", "从相册选择", "取消" };
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("选择图片来源");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("拍照")) {
                dispatchTakePictureIntent();
            } else if (options[item].equals("从相册选择")) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, REQUEST_IMAGE_PICK);
            } else if (options[item].equals("取消")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.myapplication.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                // 拍照结果
                File imgFile = new File(currentPhotoPath);
                if (imgFile.exists()) {
                    Glide.with(this).load(imgFile).into(productImageView);
                    uploadImage(imgFile);
                }
            } else if (requestCode == REQUEST_IMAGE_PICK) {
                // 相册选择结果
                if (data != null && data.getData() != null) {
                    Uri selectedImage = data.getData();
                    try {
                        File imgFile = new File(getPathFromUri(selectedImage));
                        currentPhotoPath = imgFile.getAbsolutePath();
                        Glide.with(this).load(selectedImage).into(productImageView);
                        uploadImage(imgFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "图片处理失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private String getPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        android.database.Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        return uri.getPath();
    }

    private void uploadImage(File imageFile) {
        Toast.makeText(this, "图片上传中...", Toast.LENGTH_SHORT).show();
        OkHttpManager.uploadFile(ApiConstants.UPLOAD_IMAGE_URL, imageFile, Object.class, new OkHttpManager.NetworkCallback<Object>() {
            @Override
            public void onSuccess(Object response) {
                runOnUiThread(() -> {
                    try {
                        // 直接解析响应为Map，处理服务器返回的success和fileUrl字段
                        Gson gson = new Gson();
                        String jsonResponse = gson.toJson(response);
                        Map<String, Object> responseMap = gson.fromJson(jsonResponse, Map.class);
                        
                        // 检查success字段或fileUrl字段
                        if (Boolean.TRUE.equals(responseMap.get("success")) || responseMap.containsKey("fileUrl")) {
                            uploadedImageUrl = (String) responseMap.get("fileUrl");
                            Toast.makeText(PublishActivity.this, "图片上传成功", Toast.LENGTH_SHORT).show();
                        } else {
                            String errorMsg = responseMap.get("message") != null ? responseMap.get("message").toString() : "上传失败";
                            Toast.makeText(PublishActivity.this, "图片上传失败：" + errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e("UploadImage", "解析响应失败: " + e.getMessage());
                        Toast.makeText(PublishActivity.this, "图片上传失败：解析响应失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(PublishActivity.this, "图片上传失败：" + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private boolean validateForm() {
        boolean isValid = true;

        // 验证名称
        if (TextUtils.isEmpty(nameEditText.getText().toString().trim())) {
            nameEditText.setError("请输入物品名称");
            isValid = false;
        } else {
            nameEditText.setError(null);
        }

        // 验证价格
        if (TextUtils.isEmpty(priceEditText.getText().toString().trim())) {
            priceEditText.setError("请输入物品价格");
            isValid = false;
        } else {
            try {
                double price = Double.parseDouble(priceEditText.getText().toString().trim());
                if (price <= 0) {
                    priceEditText.setError("价格必须大于0");
                    isValid = false;
                } else {
                    priceEditText.setError(null);
                }
            } catch (NumberFormatException e) {
                priceEditText.setError("请输入有效的价格");
                isValid = false;
            }
        }

        // 验证联系电话
        if (TextUtils.isEmpty(phoneEditText.getText().toString().trim())) {
            phoneEditText.setError("请输入联系电话");
            isValid = false;
        } else {
            phoneEditText.setError(null);
        }

        return isValid;
    }

    private void publishProduct() {
        // 创建与后端Shop实体匹配的商品对象
        ShopRequest shopRequest = new ShopRequest();
        shopRequest.setShopName(nameEditText.getText().toString().trim());
        // 后端shopPrice是Integer类型，需要将前端double转换为int
        shopRequest.setShopPrice((int) Double.parseDouble(priceEditText.getText().toString().trim()));
        shopRequest.setShopDesc(descriptionEditText.getText().toString().trim());
        // 使用上传后的图片URL
        shopRequest.setShopImg(uploadedImageUrl);
        // 使用商品描述作为商品详情
        shopRequest.setShopInfo(descriptionEditText.getText().toString().trim());
        // 使用分类作为商品类型
        shopRequest.setShopType(selectedCategory);

        // 调用后端API发布商品
        OkHttpManager.postJson(ApiConstants.CREATE_SHOP_URL, shopRequest, ApiResponse.class, new OkHttpManager.NetworkCallback<ApiResponse>() {
            @Override
            public void onSuccess(ApiResponse response) {
                if (response.isSuccess()) {
                    // 显示发布成功提示
                    runOnUiThread(() -> {
                        Toast.makeText(PublishActivity.this, "物品发布成功", Toast.LENGTH_SHORT).show();
                        
                        // 返回主页面
                        Intent intent = new Intent(PublishActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    });
                } else {
                    // 显示发布失败提示
                    runOnUiThread(() -> {
                        Toast.makeText(PublishActivity.this, "发布失败：" + response.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onFailure(String error) {
                // 显示网络失败提示
                runOnUiThread(() -> {
                    Toast.makeText(PublishActivity.this, "发布失败：" + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    
    // 与后端Shop实体匹配的请求类
    private static class ShopRequest {
        private String shopName;
        private Integer shopPrice;
        private String shopDesc;
        private String shopImg;
        private String shopInfo;
        private String shopType;
        
        // Getters and Setters
        public String getShopName() {
            return shopName;
        }
        
        public void setShopName(String shopName) {
            this.shopName = shopName;
        }
        
        public Integer getShopPrice() {
            return shopPrice;
        }
        
        public void setShopPrice(Integer shopPrice) {
            this.shopPrice = shopPrice;
        }
        
        public String getShopDesc() {
            return shopDesc;
        }
        
        public void setShopDesc(String shopDesc) {
            this.shopDesc = shopDesc;
        }
        
        public String getShopImg() {
            return shopImg;
        }
        
        public void setShopImg(String shopImg) {
            this.shopImg = shopImg;
        }
        
        public String getShopInfo() {
            return shopInfo;
        }
        
        public void setShopInfo(String shopInfo) {
            this.shopInfo = shopInfo;
        }
        
        public String getShopType() {
            return shopType;
        }
        
        public void setShopType(String shopType) {
            this.shopType = shopType;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}