package com.name.foodapp.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.name.foodapp.model.MessModel;
import com.name.foodapp.repository.CartRepository;

public class CartViewModel extends ViewModel {
    private CartRepository cartRepository;
    private MutableLiveData<MessModel> mutableLiveData;

    public void init(){
        cartRepository = new CartRepository();
        mutableLiveData = cartRepository.messModelMutableLiveData();
    }

    public void checkOut(int iduser, int amount, double total, String detail) {
        cartRepository.checkOut(iduser, amount, total, detail);
    }

    public MutableLiveData<MessModel> messModelMutableLiveData() {
        return mutableLiveData;
    }
}
