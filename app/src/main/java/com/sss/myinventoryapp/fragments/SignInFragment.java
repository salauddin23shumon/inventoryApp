package com.sss.myinventoryapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sss.myinventoryapp.R;
import com.sss.myinventoryapp.responces.FirebaseAuthResponse;
import com.sss.myinventoryapp.viewmodels.SignInViewModel;

import static com.basgeekball.awesomevalidation.ValidationStyle.TEXT_INPUT_LAYOUT;
import static com.sss.myinventoryapp.utility.Utility.hideKeyboard;


public class SignInFragment extends Fragment {

    private TextInputEditText emailET, passET;
    private TextInputLayout emailTIL, passTIL;
    private Button btnSignin;
    private ProgressBar progressBar;
    private AwesomeValidation mAwesomeValidation;
    private Context context;
    private NavController navController;
    private TextView signupTV;

    private SignInViewModel signInViewModel;
    public static final String TAG = "LoginFragment";


    public SignInFragment() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailET = view.findViewById(R.id.emailET);
        passET = view.findViewById(R.id.passwordET);
        emailTIL = view.findViewById(R.id.emailWrapper);
        passTIL = view.findViewById(R.id.passwordWrapper);
        btnSignin = view.findViewById(R.id.signinBtn);
        progressBar = view.findViewById(R.id.progress_signin);
        signupTV = view.findViewById(R.id.signupTV);

        navController= Navigation.findNavController(view);

        signInViewModel = new ViewModelProvider(this).get(SignInViewModel.class);

        signInViewModel.getUserLiveData().observe(getViewLifecycleOwner(), new Observer<FirebaseAuthResponse>() {
            @Override
            public void onChanged(FirebaseAuthResponse firebaseAuthResponse) {
                if (firebaseAuthResponse.getFirebaseUser() != null) {
                    Log.e(TAG, "onChanged: "+ firebaseAuthResponse.getFirebaseUser().getUid());
                    navController.navigate( R.id.action_signInFragment_to_welcomeSplashFragment);
//                    navController.navigate( R.id.action_signinFragment_to_profileFragment);
                }
                if (firebaseAuthResponse.getToastMessage()!=null){
                    Toast.makeText(context, ""+ firebaseAuthResponse.getToastMessage(), Toast.LENGTH_SHORT).show();
                }
                if (firebaseAuthResponse.isProgressLoading()){
                    progressBar.setVisibility(View.VISIBLE);
                    btnSignin.setVisibility(View.GONE);
                }else {
                    progressBar.setVisibility(View.GONE);
                    btnSignin.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAwesomeValidation = new AwesomeValidation(TEXT_INPUT_LAYOUT);
        mAwesomeValidation.setTextInputLayoutErrorTextAppearance(R.style.TextInputLayoutErrorStyle);
        addValidationForTextInputLayout();

        signupTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate( R.id.action_signInFragment_to_signUpFragment);
            }
        });

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailET.getText().toString();
                String pass = passET.getText().toString();

                if (mAwesomeValidation.validate()) {
                    btnSignin.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    signInViewModel.doSignIn(email,pass);
                    hideKeyboard(view);
                } else {
                    Log.d(TAG, "onClick: validation incomplete");
                    btnSignin.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }

            }
        });
    }

    private void addValidationForTextInputLayout() {
        mAwesomeValidation.addValidation(emailTIL,
                Patterns.EMAIL_ADDRESS,
                context.getResources().getString(R.string.email_err_msg));
        mAwesomeValidation.addValidation(passTIL,
                ".{6,}",
                context.getResources().getString(R.string.password_err_msg));
    }

}