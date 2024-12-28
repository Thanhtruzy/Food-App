package com.name.foodapp.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.name.foodapp.model.MealDetailModel;
import com.name.foodapp.repository.MealDetailRepository;

public class ShowDetailViewModel extends ViewModel {
    private MealDetailRepository mealDetailRepository;

    public ShowDetailViewModel() {
        mealDetailRepository = new MealDetailRepository();
    }
    public MutableLiveData<MealDetailModel> mealDetailModelMutableLiveData(int id){
        return mealDetailRepository.getMealDetail(id);

    }
}
