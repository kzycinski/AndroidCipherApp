package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {

    private static final String EDIT_VIEW_INITIAL_MESSAGE = "Tekst";
    private static final String TRANSLATED_VIEW_INITIAL_MESSAGE = "Tłumaczenie";
    List<Cipher> ciphers;

    private Spinner cipherSpinner;
    private TextView textView, editText;

    private String newCipher = "";
    private Cipher cipherToDelete;

    private RapidFloatingActionHelper rfabHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   //     this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);
        configContent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.default_item:
                restoreDefaults();
                break;
            default:
                Toast.makeText(this, "Clicked icon: " + item.getItemId() + ". Contact with administrator", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void configContent() {
        configSpinner();
        configClearButton();
        addFloatingActionButtonConfig();
        configTextView();
        configEditView();
    }

    public void configSpinner() {
        cipherSpinner = findViewById(R.id.cipherSpinner);
        setSpinnerData(cipherSpinner);
        setOnItemSelectedListener();
    }

    private void setSpinnerData(Spinner spinner) {
        ArrayAdapter<Cipher> dataAdapter = createCipherSpinnerContent();
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    private ArrayAdapter<Cipher> createCipherSpinnerContent() {
        ciphers = CipherProvider.getCiphers(this);
        return new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, ciphers);
    }

    private void setOnItemSelectedListener() {
        cipherSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!textView.getText().toString().equals(TRANSLATED_VIEW_INITIAL_MESSAGE))
                    updateTranslatedView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void configTextView() {
        textView = findViewById(R.id.textView);
        textView.setTextColor(Color.parseColor("#D3D3D3"));
        textView.setMovementMethod(new ScrollingMovementMethod());
    }

    private void configEditView() {
        editText = findViewById(R.id.editText);
        editText.setTextColor(Color.parseColor("#D3D3D3"));
        setOnClickListener();
        addTextChangedListener();
        setInitialMessageListener();
    }

    private void setOnClickListener() {
        editText.setOnClickListener(view -> {
            if (editText.getText().toString().equals(EDIT_VIEW_INITIAL_MESSAGE))
                editText.setText("");
        });
    }

    private void addTextChangedListener() {
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
    }

    private void setInitialMessageListener() {
        editText.setOnFocusChangeListener((view, b) -> {
            if (editText.getText().toString().equals(EDIT_VIEW_INITIAL_MESSAGE))
                editText.setText("");
        });
    }

    public void configClearButton() {
        Button clearButton = findViewById(R.id.button2);
        clearButton.setOnClickListener(v -> clearFields());
    }

    private void clearFields() {
        textView.setText("");
        editText.setText("");
        textView.scrollTo(0, 0);

    }

    private void updateTranslatedView() {
        try {
            textView.setText(((Cipher) cipherSpinner.getSelectedItem()).translate(editText.getText().toString()));
        } catch (IllegalArgumentException e) {
            editText.setError(e.getMessage());
        }
    }

    private void addFloatingActionButtonConfig() {
        RapidFloatingActionLayout rfaLayout = findViewById(R.id.activity_main_rfal);
        RapidFloatingActionButton rfaBtn = findViewById(R.id.activity_main_rfab);
        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(this);
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);

        rfaContent.setItems(getFABContent());

        rfabHelper = new RapidFloatingActionHelper(
                this,
                rfaLayout,
                rfaBtn,
                rfaContent
        ).build();
    }

    private List<RFACLabelItem> getFABContent() {
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Dodaj szyfr")
                .setResId(R.drawable.xdd)
                .setIconNormalColor(0xff00bcd4)
                .setIconPressedColor(0xff00bcd4)
                .setLabelColor(0xff00bcd4)
                .setWrapper(1)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Usuń szyfr")
                .setResId(R.drawable.xdd)
                .setIconNormalColor(0xff00bcd4)
                .setIconPressedColor(0xff00bcd4)
                .setLabelColor(0xff00bcd4)
                .setWrapper(1)
        );
        return items;
    }


    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        onRFACItemIconClick(position, item);
    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        switch(position) {
            case 0:
                addNewCipher();
                break;
            case 1:
                deleteCipher();
                break;
            case 2:
                restoreDefaults();
                break;
            default:
                Toast.makeText(this, "Clicked icon: " + position + ". Contact with administrator", Toast.LENGTH_SHORT).show();
        }
        rfabHelper.toggleContent();
    }


    private void addNewCipher() {
        String newCipher = getUserCipher(this);
        if (newCipher == null)
            return;
        createNewCipher(newCipher);

    }

    private Context getContext() {
        return this;
    }

    public String getUserCipher(Context context) {
        Handler handler = getHandler();

        AlertDialog.Builder alert = getCreateCipherAlertDialog(context, handler);

        alert.show();

        try {
            Looper.loop();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        return newCipher;
    }

    @SuppressLint("HandlerLeak")
    private static Handler getHandler() {
        return new Handler() {
            @Override
            public void handleMessage(Message msg) {
                throw new RuntimeException();
            }
        };
    }

    private AlertDialog.Builder getCreateCipherAlertDialog(Context context, Handler handler) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Dodaj szyfr");
        alert.setMessage("Wprowadź szyfr:");
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        alert.setView(input);
        alert.setPositiveButton("Ok", (dialog, id) -> {
            newCipher = input.getText().toString();
            handler.sendMessage(handler.obtainMessage());
        });
        alert.setNegativeButton("Anuluj", (dialog, id) -> {
            newCipher = null;
            handler.sendMessage(handler.obtainMessage());
        });
        return alert;

    }

    private void createNewCipher(String newCipher) {
        try {
            Cipher cipher = PairCipher.addCipher(newCipher, this);
            ciphers.add(cipher);
            cipherSpinner.setSelection(ciphers.indexOf(cipher));
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteCipher() {
        Cipher cipher = getDeleteCipher();
        if(cipher == null)
            return;
        CipherProvider.removeCipher(cipher, this);
        ciphers.remove(cipher);
        setSpinnerData(cipherSpinner);
    }

    private Cipher getDeleteCipher() {
        Handler handler = getHandler();

        AlertDialog.Builder alert = getDeleteCipherUserDialog(this, handler);
        AlertDialog alertDialog = alert.create();
        alertDialog.show();


        try {
            Looper.loop();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        return cipherToDelete;
    }

    private AlertDialog.Builder getDeleteCipherUserDialog(Context context, Handler handler) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        View view = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
        alert.setTitle("Usuń szyfr");
        Spinner spinner = view.findViewById(R.id.spinner);
        setSpinnerData(spinner);

        alert.setPositiveButton("Ok", (dialog, id) -> {
           cipherToDelete = (Cipher) spinner.getSelectedItem();
            handler.sendMessage(handler.obtainMessage());
        });
        alert.setNegativeButton("Anuluj", (dialog, id) -> {
            cipherToDelete = null;
            handler.sendMessage(handler.obtainMessage());
        });
        alert.setView(view);

        return alert;
    }

    private void restoreDefaults() {
        Handler handler = getHandler();

        AlertDialog.Builder alert = getRestoreDefaultsDialog(this, handler);
        AlertDialog alertDialog = alert.create();
        alertDialog.show();

        try {
            Looper.loop();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        setSpinnerData(cipherSpinner);
    }

    private AlertDialog.Builder getRestoreDefaultsDialog(Context context, Handler handler) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Przywróć domyślne szyfry");
        alert.setMessage("Uwaga! Operacja usunie wszystkie dane użytkownika i przywróci domyślne szyfry. Kontynuować?");

        alert.setPositiveButton("Tak", (dialog, id) -> {
            CipherProvider.restoreDefaults(this);
            handler.sendMessage(handler.obtainMessage());
        });
        alert.setNegativeButton("Nie", (dialog, id) -> {
            handler.sendMessage(handler.obtainMessage());
        });

        return alert;
    }
}


