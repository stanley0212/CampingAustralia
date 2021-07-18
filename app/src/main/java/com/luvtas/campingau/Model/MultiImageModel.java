package com.luvtas.campingau.Model;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.luvtas.campingau.CallBack.MultiImageCallbackListener;

import java.util.ArrayList;
import java.util.List;

public class MultiImageModel extends ViewModel implements MultiImageCallbackListener {

    private MutableLiveData<List<MultiImageModel>> mutableLiveData;
    private MutableLiveData<String> messageError;
    private MultiImageCallbackListener multiImageCallbackListener;

    public MultiImageModel(){
        multiImageCallbackListener = this;
    }

    public MutableLiveData<List<MultiImageModel>> getMultiImageList(String key){
        if(mutableLiveData == null){
            mutableLiveData = new MutableLiveData<>();
            loadMultiImage(key);
        }
        return  mutableLiveData;
    }

    private void loadMultiImage(String key) {
        List<MultiImageModel> templist = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Campsite").child(key).child("CamperSiteImage");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot itemSnapShot:dataSnapshot.getChildren()){
                    MultiImageModel multiImageModel = itemSnapShot.getValue(MultiImageModel.class);
                    templist.add(multiImageModel);
                }
                multiImageCallbackListener.onMultiLoadSuccess(templist);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onMultiLoadSuccess(List<MultiImageModel> multiImageModels) {

    }

    @Override
    public void onMultiLoadFailed(String message) {

    }

    public MutableLiveData<String> getMessageError() {
        return messageError;
    }
}
