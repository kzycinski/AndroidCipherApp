package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String EDIT_VIEW_INITIAL_MESSAGE = "Text";
    private static final String TRANSLATED_VIEW_INITIAL_MESSAGE = "Translation";
    private Spinner spinner2;
    private Button button;
    private TextView textView, editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addItemsOnSpinners();
        addListenerOnButton();
    }


    // add items into spinner dynamically
    public void addItemsOnSpinners() {
        spinner2 = findViewById(R.id.spinner2);
        List<Cipher> list = new ArrayList<>();
        list.add(new PairCipher("GADERY POLUKI"));
        list.add(new PairCipher("POLITYKA RENU"));
        list.add(new PairCipher("OKULAR MINETY"));
        ArrayAdapter<Cipher> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);
    }


    public void addListenerOnButton() {

        spinner2 = findViewById(R.id.spinner2);
        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        editText = findViewById(R.id.editText);
        textView.setTextColor(Color.parseColor("#D3D3D3"));
        editText.setTextColor(Color.parseColor("#D3D3D3"));



        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().equals(EDIT_VIEW_INITIAL_MESSAGE))
                    editText.setText("");
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateTranslatedView();
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (editText.getText().toString().equals(EDIT_VIEW_INITIAL_MESSAGE))
                    editText.setText("");
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTranslatedView();
            }

        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!textView.getText().toString().equals(TRANSLATED_VIEW_INITIAL_MESSAGE))
                    updateTranslatedView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void updateTranslatedView() {
        textView.setText(((Cipher) spinner2.getSelectedItem()).translate(editText.getText().toString()));
    }
}


