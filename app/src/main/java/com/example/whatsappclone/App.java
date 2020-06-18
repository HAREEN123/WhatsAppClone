package com.example.whatsappclone;

import com.parse.Parse;
import android.app.Application;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("Wn1kCY0RKTetf5Pg7oJ8EZYy7FC3vOoK3kgjocyB")
                // if defined
                .clientKey("Llk96chPdrtbPTmzif1McvKwEW1c6Z35QJBkYbKg")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
