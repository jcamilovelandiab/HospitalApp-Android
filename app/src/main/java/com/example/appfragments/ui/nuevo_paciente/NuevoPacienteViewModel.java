package com.example.appfragments.ui.nuevo_paciente;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NuevoPacienteViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NuevoPacienteViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}