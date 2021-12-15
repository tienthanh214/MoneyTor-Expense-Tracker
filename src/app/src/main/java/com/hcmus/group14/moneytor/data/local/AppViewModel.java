package com.hcmus.group14.moneytor.data.local;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.hcmus.group14.moneytor.data.model.*;
import com.hcmus.group14.moneytor.data.model.relation.*;
import java.util.List;

public abstract class AppViewModel extends AndroidViewModel {
    protected AppRepository appRepository;

    public AppViewModel(@NonNull Application application) {
        super(application);
        appRepository = new AppRepository(application);
    }

}
