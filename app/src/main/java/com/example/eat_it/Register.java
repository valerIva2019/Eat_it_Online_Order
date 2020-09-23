package com.example.eat_it;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    EditText txtName,txtEmail,txtPhone,txtPassword;
    Button btnRegister;
    ProgressDialog lodingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtName = (EditText) findViewById(R.id.editTextTextPersonName);
        txtEmail = (EditText) findViewById(R.id.editTextTextEmail);
        txtPhone = (EditText) findViewById(R.id.editTextTextPhone);
        txtPassword = (EditText) findViewById(R.id.editPassword);

        btnRegister = (Button) findViewById(R.id.button);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createCustomerAccount();
            }
        });
    }

    private void createCustomerAccount() {

        String name = txtName.getText().toString();
        String email = txtEmail.getText().toString();
        String phone = txtPhone.getText().toString();
        String password = txtPassword.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), "Please Enter the Full Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please Enter the Email Address", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(getApplicationContext(), "Please Enter the Contact Number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please Enter the Password", Toast.LENGTH_SHORT).show();
        } else {
            lodingbar.setTitle("Creating The Account");
            lodingbar.setMessage("Please Wait");
            lodingbar.setCanceledOnTouchOutside(false);
            lodingbar.show();

            ValidateEmailAddress(name, email, phone, password);

        }
    }

    private void ValidateEmailAddress(final String name, final String email, final String phone, final String password) {
        final DatabaseReference dbref;
        dbref= FirebaseDatabase.getInstance().getReference();

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!(snapshot.child("Customer").child(phone).exists())){

                    HashMap<String,Object> customerDataMap =new HashMap<>();
                    customerDataMap.put("phone",phone);
                    customerDataMap.put("name",name);
                    customerDataMap.put("email",email);

                    customerDataMap.put("password",password);

                    dbref.child("Customer").child(phone).updateChildren(customerDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(Register.this,"Your Account created Successfully",Toast.LENGTH_SHORT).show();
                                        lodingbar.dismiss();

                                        Intent intent3 = new Intent(Register.this, Menu.class);
                                        startActivity(intent3);
                                    }
                                    else{
                                        lodingbar.dismiss();
                                        Toast.makeText(Register.this,"Network Error",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
                else{
                    Toast.makeText(Register.this,"This "+phone+" number already exist",Toast.LENGTH_SHORT).show();
                    lodingbar.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}