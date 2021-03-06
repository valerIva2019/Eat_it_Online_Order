package com.example.eat_it;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eat_it.Model.Driver;
import com.example.eat_it.Prevalent.CurrentDriver;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class dr_login extends AppCompatActivity {

    private EditText InputNumber,InputPassword;
    private Button LoginButton;
    private ProgressDialog ladingBar;
    private String parentDbName= "Deliver";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dr_login);


        InputNumber = (EditText) findViewById(R.id.phoneU);
        InputPassword = (EditText) findViewById(R.id.passwordU);
        LoginButton = (Button)findViewById(R.id.login);
        ladingBar =new ProgressDialog(this);



        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginCustomer();
            }
        });
    }


    private void loginCustomer() {

        String phone= InputNumber.getText().toString();
        String password= InputPassword.getText().toString();

        if(TextUtils.isEmpty(phone)){
            Toast.makeText(getApplicationContext(),"Please Enter the Phone Number",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(getApplicationContext(),"Please Enter the Password",Toast.LENGTH_SHORT).show();
        }
        else{
            ladingBar.setTitle("Login Account");
            ladingBar.setMessage("Please Wait");
            ladingBar.setCanceledOnTouchOutside(false);
            ladingBar.show();


            AllowAccessToAccount(phone,password);
        }
    }

    private void AllowAccessToAccount(final String phone, final String password) {

        final DatabaseReference dbref;
        dbref= FirebaseDatabase.getInstance().getReference();

        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(parentDbName).child(phone).exists()){

                   Driver driverData = dataSnapshot.child(parentDbName).child(phone).getValue(Driver.class);

                    if(driverData.getPhone().equals(phone)){
                        if(driverData.getPassword().equals(password)){

                            Toast.makeText(dr_login.this,"Log-in Successfully",Toast.LENGTH_SHORT).show();
                            ladingBar.dismiss();

                            CurrentDriver.setPhoneNumb(phone);


                            Intent intent3 = new Intent(dr_login.this, dr_dashboard.class);


                            startActivity(intent3);
                        }

                        else{
                            Toast.makeText(dr_login.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                else {
                    Toast.makeText(dr_login.this,"Account with this "+phone+" does not exists",Toast.LENGTH_SHORT).show();
                    ladingBar.dismiss();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}