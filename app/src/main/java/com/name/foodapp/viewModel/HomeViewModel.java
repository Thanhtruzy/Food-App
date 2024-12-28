package com.name.foodapp.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.name.foodapp.model.CategoryModel;
import com.name.foodapp.model.MealModel;
import com.name.foodapp.repository.CategoryRepository;
import com.name.foodapp.repository.MealRepository;

public class HomeViewModel extends ViewModel {
    private CategoryRepository categoryRepository;
    private MealRepository mealRepository;
    public HomeViewModel() {
        categoryRepository = new CategoryRepository();
        mealRepository = new MealRepository();
    }
    public MutableLiveData<CategoryModel> categoryModelMutableLiveData(){
        return categoryRepository.getCategory();
    }
    public MutableLiveData<MealModel> mealsModelMutableLiveData(int idcate){
        return (MutableLiveData<MealModel>) mealRepository.getMeals(idcate);
    }
}
