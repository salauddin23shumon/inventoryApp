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
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.sss.myinventoryapp.R;
import com.sss.myinventoryapp.responces.FirebaseAuthResponse;
import com.sss.myinventoryapp.viewmodels.SignUpViewModel;


import static com.basgeekball.awesomevalidation.ValidationStyle.TEXT_INPUT_LAYOUT;


public class SignUpFragment extends Fragment {

    private AwesomeValidation mAwesomeValidation;
    private Context context;
    private TextView signinTV;
    private FirebaseAuth firebaseAuth;
    private TextInputEditText nameET, emailET, passET;
    private TextInputLayout nameTIL, emailTIL, passTIL;
    private Button btnSignup;
    private ProgressBar progressBar;
    private SignUpViewModel signUpViewModel;
    public static final String TAG = "SignupFragment";
    private NavController navController;


    public SignUpFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        firebaseAuth = FirebaseAuth.getInstance();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameET = view.findViewById(R.id.nameET);
        emailET = view.findViewById(R.id.emailET);
        passET = view.findViewById(R.id.passwordET);
        nameTIL = view.findViewById(R.id.nameWrapper);
        emailTIL = view.findViewById(R.id.emailWrapper);
        passTIL = view.findViewById(R.id.passwordWrapper);
        btnSignup = view.findViewById(R.id.signupBtn);
        signinTV = view.findViewById(R.id.signinTV);
        progressBar = view.findViewById(R.id.progress_signup);

        navController = Navigation.findNavController(view);

        signinTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_signUpFragment_to_signInFragment);
            }
        });

        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

        signUpViewModel.getUserLiveData().observe(getViewLifecycleOwner(), new Observer<FirebaseAuthResponse>() {
            @Override
            public void onChanged(FirebaseAuthResponse firebaseAuthResponse) {
                if (firebaseAuthResponse.getFirebaseUser() != null) {
                    Log.e(TAG, "onChanged: " + firebaseAuthResponse.getFirebaseUser().getUid());
                    navController.navigate(R.id.action_signUpFragment_to_welcomeSplashFragment);
                }
                if (firebaseAuthResponse.getToastMessage() != null) {
                    Toast.makeText(context, "" + firebaseAuthResponse.getToastMessage(), Toast.LENGTH_SHORT).show();
                }
                if (firebaseAuthResponse.isProgressLoading()) {
                    progressBar.setVisibility(View.VISIBLE);
                    btnSignup.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.GONE);
                    btnSignup.setVisibility(View.VISIBLE);
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

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameET.getText().toString();
                String email = emailET.getText().toString();
                String pass = passET.getText().toString();

                progressBar.setVisibility(View.VISIBLE);
                btnSignup.setVisibility(View.GONE);
                if (mAwesomeValidation.validate()) {
                    signUpViewModel.doSignUp(name, email, pass);
                } else {
                    Log.d(TAG, "onClick: validation incomplete");
                    progressBar.setVisibility(View.GONE);
                    btnSignup.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void addValidationForTextInputLayout() {
        mAwesomeValidation.addValidation(nameTIL,
                "[a-zA-Z0-9_@.]*",
                context.getResources().getString(R.string.name_err_msg));
        mAwesomeValidation.addValidation(nameTIL,
                RegexTemplate.NOT_EMPTY,
                context.getResources().getString(R.string.name_err_msg));
        mAwesomeValidation.addValidation(emailTIL,
                Patterns.EMAIL_ADDRESS,
                context.getResources().getString(R.string.email_err_msg));
        mAwesomeValidation.addValidation(passTIL,
                ".{6,}",
                context.getResources().getString(R.string.password_err_msg));
    }

}