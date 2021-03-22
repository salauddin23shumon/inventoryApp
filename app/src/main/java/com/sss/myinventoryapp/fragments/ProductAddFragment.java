package com.sss.myinventoryapp.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.format.DateFormat;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.sss.myinventoryapp.R;
import com.sss.myinventoryapp.models.Product;
import com.sss.myinventoryapp.models.User;
import com.sss.myinventoryapp.responces.ProductResponse;
import com.sss.myinventoryapp.utility.EventBusData;
import com.sss.myinventoryapp.utility.SessionManager;
import com.sss.myinventoryapp.viewmodels.ProductViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;
import static com.basgeekball.awesomevalidation.ValidationStyle.TEXT_INPUT_LAYOUT;
import static com.sss.myinventoryapp.utility.Utility.getImageBAOS;


public class ProductAddFragment extends Fragment {

    private EditText nameET, rateET, quantityET, priceET, dateET, unitET;
    private Button saveBtn, picBtn;
    private ImageView imageView;
    private ProductViewModel productViewModel;
    private Context context;
    private SessionManager manager;
    private byte[] imgByte;
    private Uri filePath;
    private int IMG_RQST_CODE = 444;
    private StorageReference imgStorageRef;
    private StorageTask storageTask;
    private NavController navController;
    private AwesomeValidation mAwesomeValidation;
    private EventBus bus = EventBus.getDefault();
    StorageReference fileReference;
    private int count=0;

    private Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener startingDate =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                    SimpleDateFormat sDF = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                    myCalendar.set(Calendar.YEAR, y);
                    myCalendar.set(Calendar.MONTH, m);
                    myCalendar.set(Calendar.DAY_OF_MONTH, d);
                    dateET.setText(sDF.format(myCalendar.getTime()));
                }
            };

    private static final String TAG = "ProductAddFragment";
    private DatabaseReference eventRef;


    public ProductAddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        manager = new SessionManager(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        productViewModel = new ViewModelProvider(getActivity()).get(ProductViewModel.class);

        eventRef = FirebaseDatabase.getInstance().getReference("users").child(manager.getUid()).child("product");
        imgStorageRef = FirebaseStorage.getInstance().getReference("product_images");
        return inflater.inflate(R.layout.fragment_product_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameET = view.findViewById(R.id.nameET);
        quantityET = view.findViewById(R.id.quantityET);
        rateET = view.findViewById(R.id.rateET);
        unitET = view.findViewById(R.id.unitET);
        priceET = view.findViewById(R.id.priceET);
        dateET = view.findViewById(R.id.dateET);
        saveBtn = view.findViewById(R.id.saveBtn);
        picBtn = view.findViewById(R.id.picBtn);
        imageView = view.findViewById(R.id.picIV);

        mAwesomeValidation = new AwesomeValidation(BASIC);
//        mAwesomeValidation.setTextInputLayoutErrorTextAppearance(R.style.TextInputLayoutErrorStyle);
        addValidationForTextInputLayout();

        navController = Navigation.findNavController(view);

//        productViewModel.getProductLiveData().observe(getViewLifecycleOwner(), new Observer<ProductResponse>() {
//            @Override
//            public void onChanged(ProductResponse response) {
//                if (response.getToastMessage()!=null){
//                    Toast.makeText(context, ""+response.getToastMessage(), Toast.LENGTH_SHORT).show();
//                    Navigation.findNavController(view).navigate(R.id.action_productAddFragment_to_nav_home);
//                }
//            }
//        });

        picBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Photo"), IMG_RQST_CODE);
            }
        });

        dateET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    DatePickerDialog datePiker = new DatePickerDialog
                            (context, startingDate, myCalendar
                                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                    myCalendar.get(Calendar.DAY_OF_MONTH));
                    datePiker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    datePiker.show();
                }
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameET.getText().toString();
                String quantity = quantityET.getText().toString();
                String rate = rateET.getText().toString();
                String unit = unitET.getText().toString();
                String price = priceET.getText().toString();
                String date = dateET.getText().toString();


                if (filePath!=null) {

                    if (mAwesomeValidation.validate()) {
                        Product product = new Product(null, name, Double.parseDouble(rate), Integer.parseInt(quantity), unit, Double.parseDouble(price), null, date);
                        saveProduct(product);
                    } else
                        Log.d(TAG, "onClick: validation err");
                }else {
                    Toast.makeText(context, "select product image", Toast.LENGTH_SHORT).show();
                }


//                Product product = new Product("", name, Double.parseDouble(rate), Integer.parseInt(quantity),unit, Double.parseDouble(price), date);
//                productViewModel.saveProduct(product);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_RQST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            Picasso.get().load(filePath).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    imageView.setImageBitmap(bitmap);
                    imgByte = getImageBAOS(bitmap);
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        }
    }

    private void saveProduct(Product product) {

        Date d = new Date();
        CharSequence s  = DateFormat.format("dd-mm-yyyy", d.getTime());

        fileReference = imgStorageRef.child("product_img_" +System.currentTimeMillis());
        storageTask = fileReference.putBytes(imgByte)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String keyId = eventRef.push().getKey();
                                Toast.makeText(context, "Upload successful", Toast.LENGTH_LONG).show();
                                product.setImgUrl(uri.toString());
                                product.setPid(keyId);


                                eventRef.child(keyId).setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.e(TAG, "onSuccess: ");
                                        try {
                                            navController.popBackStack();
                                        } catch(Exception e) {
                                            Log.d(TAG, "onSuccess: "+e.getMessage());
                                        }

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e(TAG, "onFailure: " + e.getMessage());
                                    }
                                });
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addValidationForTextInputLayout() {

        mAwesomeValidation.addValidation(nameET, RegexTemplate.NOT_EMPTY, "field can  not be empty");
        mAwesomeValidation.addValidation(quantityET, RegexTemplate.NOT_EMPTY, "field can  not be empty");
        mAwesomeValidation.addValidation(dateET, RegexTemplate.NOT_EMPTY, "field can  not be empty");
        mAwesomeValidation.addValidation(rateET, RegexTemplate.NOT_EMPTY, "field can  not be empty");
        mAwesomeValidation.addValidation(priceET, RegexTemplate.NOT_EMPTY, "field can  not be empty");
        mAwesomeValidation.addValidation(unitET, RegexTemplate.NOT_EMPTY, "field can  not be empty");

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusData data) {
        Log.d(TAG, "onEvent: " + data.isNetworkAvailable());
        if (!data.isNetworkAvailable())
            saveBtn.setEnabled(false);
        else
            saveBtn.setEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!bus.isRegistered(this)){
            bus.register(this);
        }
    }

    @Override
    public void onStop() {
        if (bus.isRegistered(this))
            bus.unregister(this);
        super.onStop();
    }
}