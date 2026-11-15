package com.mohavic.tp1_eniad;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mohavic.tp1_eniad.domain.GreetingResolution;
import com.mohavic.tp1_eniad.domain.ServiceLocator;

public class ActivatePrincipale extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /** Resolves multilingual greeting metadata through the decoupled domain container. */
    public GreetingResolution resolveGreeting(String languageTag) {
        return ServiceLocator.defaultInstance().greetingService().resolve(languageTag);
    }
}
