package com.fireblaze.evento.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.fireblaze.evento.Constants;
import com.fireblaze.evento.R;
import com.fireblaze.evento.databinding.ActivityNewEventBinding;
import com.fireblaze.evento.models.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewEventActivity extends BaseActivity {
    public static final String TAG = NewEventActivity.class.getName();
    private DatabaseReference mDatabase;
    ActivityNewEventBinding binding;
    private final int REQ_UPLOAD_IMAGE = 4;

    String imagePath;

    @Override
    public View getContainer() {
        return findViewById(R.id.container);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_new_event);
        getViews();
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
        binding.btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageFromGallery();
            }
        });
    }
    private void getImageFromGallery(){
        Intent i = new Intent(NewEventActivity.this,UploadImageActivity.class);
        i.putExtra(UploadImageActivity.UPLOAD_PATH_KEYWORD,
                "/"+Constants.EVENT_IMAGE+"/"+getUid());
        startActivityForResult(i,REQ_UPLOAD_IMAGE);
    }
    private void getViews(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        
     }
    private void submitForm(){
        if(!validateName())
            return;
        if(!validateDescription())
            return;
        if(!validateVenue())
            return;
        if(!validatePrize())
            return;
        if(!validateCategory())
            return;
        if(!validateFees())
            return;
        if(!validateDuration())
            return;
        if(!validateImage())
            return;

        uploadNewEvent(imagePath);
    }

    private void uploadNewEvent(String imagePath){
        String key = mDatabase.child(Constants.EVENTS_KEYWORD).push().getKey();
        Event event = new Event(key,getUid(),binding.inputName.getText().toString().trim(),
                binding.inputDescription.getText().toString().trim(),
                binding.inputCategory.getText().toString().trim(),
                imagePath,
                binding.inputVenue.getText().toString().trim().toLowerCase(),
                "NA",
                Double.parseDouble(binding.inputFees.getText().toString()),
                Double.parseDouble(binding.inputPrize.getText().toString()),
                binding.inputDuration.getText().toString().trim()
        );
        mDatabase.child(Constants.EVENTS_KEYWORD).child(key).setValue(event)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    setResult(RESULT_OK);
                    finish();
                } else
                    Toast.makeText(NewEventActivity.this,"Error",Toast.LENGTH_LONG).show();
            }
        });

    }
    private boolean validateImage(){
        return imagePath!=null;
    }
    private boolean validateDuration(){
        if(binding.inputDuration.getText().toString().trim().isEmpty()){
            binding.inputLayoutDuration.setError(getString(R.string.err_duration));
            requestFocus(binding.inputCategory);
            return false;
        } else {
            binding.inputLayoutDuration.setErrorEnabled(false);
        }
        return true;
    }
    private boolean validateCategory(){
        String category = binding.inputCategory.getText().toString().trim();
        String[] validCategories = getResources().getStringArray(R.array.event_categories);
        boolean valid = false;
        for(String s: validCategories){
            if(s.equals(category))
                valid = true;
        }
        if(category.isEmpty()){
            binding.inputLayoutCategory.setError(getString(R.string.err_category));
            requestFocus(binding.inputCategory);
            return false;
        } else if(!valid){
            binding.inputLayoutCategory.setError(getString(R.string.err_category));
            requestFocus(binding.inputCategory);
            return false;
        }

        else {
            binding.inputLayoutCategory.setErrorEnabled(false);
        }
        return true;
    }
    private boolean validateName(){
        if(binding.inputName.getText().toString().trim().isEmpty()){
            binding.inputLayoutName.setError(getString(R.string.err_name));
            requestFocus(binding.inputName);
            return false;
        } else {
            binding.inputLayoutName.setErrorEnabled(false);
        }
        return true;
    }
    private boolean validateDescription(){
        if(binding.inputDescription.getText().toString().trim().isEmpty()){
            binding.inputLayoutDescription.setError(getString(R.string.err_description));
            requestFocus(binding.inputDescription);
            return false;
        } else {
            binding.inputLayoutDescription.setErrorEnabled(false);
        }
        return true;
    }
    private boolean validateVenue(){
        if(binding.inputVenue.getText().toString().trim().isEmpty()){
            binding.inputLayoutVenue.setError(getString(R.string.err_venue));
            requestFocus(binding.inputVenue);
            return false;
        } else {
            binding.inputLayoutVenue.setErrorEnabled(false);
        }
        return true;
    }
    private boolean validatePrize(){
        String input = binding.inputPrize.getText().toString().trim();
        if(input.isEmpty()){
            binding.inputLayoutPrize.setError(getString(R.string.err_prize));
            requestFocus(binding.inputVenue);
            return false;
        } else if(Integer.parseInt(input)==0){
            binding.inputLayoutPrize.setError("Enter a non-zero value");
            requestFocus(binding.inputPrize);
            return false;
        } else {
            binding.inputLayoutPrize.setErrorEnabled(false);
        }
        return true;
    }
    private boolean validateFees(){
        String input = binding.inputFees.getText().toString().trim();
        if(input.isEmpty()){
            binding.inputLayoutFees.setError(getString(R.string.err_prize));
            requestFocus(binding.inputVenue);
            return false;
        } else if(Integer.parseInt(input)==0){
            binding.inputLayoutFees.setError("Enter a non-zero value");
            requestFocus(binding.inputFees);
            return false;
        } else {
            binding.inputLayoutFees.setErrorEnabled(false);
        }
        return true;
    }
    private void requestFocus(View view){
        if(view.requestFocus()){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQ_UPLOAD_IMAGE:
                if(resultCode == RESULT_OK && data != null){
                    imagePath = data.getStringExtra(UploadImageActivity.DOWNLOAD_URL_RESULT);
                    Toast.makeText(NewEventActivity.this,"Upload Complete",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
