package com.name.foodapp.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.name.foodapp.model.MealModel;
import com.name.foodapp.model.Meals;
import com.name.foodapp.repository.MealRepository;

import java.util.List;

public class CategoryViewModel extends ViewModel {
    private final MealRepository mealRepository;

    public CategoryViewModel() {
        mealRepository = MealRepository.getInstance();
    }

    public LiveData<MealModel> mealModelMutableLiveData(int idcate) {
        return mealRepository.getMeals(idcate);
    }

    public LiveData<List<Meals>> searchMeals(String query) {
        return mealRepository.searchMeals(query);
    }
}
