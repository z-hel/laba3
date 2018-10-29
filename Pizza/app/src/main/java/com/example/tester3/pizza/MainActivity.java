package com.example.tester3.pizza;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.IntegerRes;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private CheckBox mushroomsTaste;
    private CheckBox hamTaste;
    private CheckBox chickenTaste;
    private CheckBox cheesesTaste;
    private CheckBox checkBoxEmail;

    private TextView cost1;
    private TextView cost2;
    private Spinner paymentMethod;
    private RadioGroup selectedSize;

    private LinearLayout layoutExpiry;
    private LinearLayout layoutCVV;
    private LinearLayout layoutCardNumber;
    private LinearLayout layoutEmail;
    private TextInputEditText inputEmail;

    private TextInputEditText inputFirstName;
    private TextInputEditText inputPhone;

    private Button sendOrder;
    private int sizeCost = 0;
    private int tasteCost = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mushroomsTaste = findViewById(R.id.mushrooms);
        hamTaste = findViewById(R.id.ham);
        chickenTaste = findViewById(R.id.chicken);
        cheesesTaste = findViewById(R.id.cheeses);
        paymentMethod = findViewById(R.id.spinnerPay);
        layoutExpiry = findViewById(R.id.layoutExpiry);
        layoutCVV = findViewById(R.id.layoutCVV);
        layoutCardNumber = findViewById(R.id.layoutCardNumber);
        layoutEmail = findViewById(R.id.layoutEmail);
        inputEmail = findViewById(R.id.inputEmail);
        inputFirstName = findViewById(R.id.inputFirstName);
        inputPhone = findViewById(R.id.inputPhone);

        findViewById(R.id.orderButton).setOnClickListener(v -> {
            String message;
            if (checkIfTasteSelected())
                message = getString(R.string.orderMessage,
                        inputFirstName.getText().toString(),
                        inputPhone.getText().toString(),
                        getPrice(),
                        paymentMethod.getSelectedItem(),
                        getTaste(),
                        getSelectedSize(),
                        String.format("email: %s", getEmail()));
            else
                message = getString(R.string.tasteNotSelectedMessage);
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();

        });
        checkBoxEmail = findViewById(R.id.checkBoxEmail);
        checkBoxEmail.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked)
                layoutEmail.setVisibility(View.VISIBLE);
            else
                layoutEmail.setVisibility(View.GONE);

        });

        cost1 = findViewById(R.id.cost1);
        cost2 = findViewById(R.id.cost2);

        selectedSize = findViewById(R.id.pizzaSize);
        selectedSize.setOnCheckedChangeListener((group, checkedId) -> updateSizeCost(checkedId));

        updateSizeCost(selectedSize.getCheckedRadioButtonId());

        mushroomsTaste.setOnClickListener(radioButtonClickListener);
        hamTaste.setOnClickListener(radioButtonClickListener);
        cheesesTaste.setOnClickListener(radioButtonClickListener);
        chickenTaste.setOnClickListener(radioButtonClickListener);

        PaymentMethod method = PaymentMethod.fromString((String) paymentMethod.getSelectedItem());
        updateBankInfoVisibility(method);

        paymentMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PaymentMethod method = PaymentMethod.fromString((String) paymentMethod.getItemAtPosition(position));
                updateBankInfoVisibility(method);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                hideBankInputVisibility();
            }
        });

    }

    View.OnClickListener radioButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mushrooms:
                    if (mushroomsTaste.isChecked())
                        tasteCost += getResources().getInteger(R.integer.mushroomsCost);
                    else tasteCost -= getResources().getInteger(R.integer.mushroomsCost);
                    break;
                case R.id.ham:
                    if (hamTaste.isChecked())
                        tasteCost += getResources().getInteger(R.integer.hamCost);
                    else tasteCost -= getResources().getInteger(R.integer.hamCost);
                    break;
                case R.id.cheeses:
                    if (cheesesTaste.isChecked())
                        tasteCost += getResources().getInteger(R.integer.cheesesCost);
                    else tasteCost -= getResources().getInteger(R.integer.cheesesCost);
                    break;
                case R.id.chicken:
                    if (chickenTaste.isChecked())
                        tasteCost += getResources().getInteger(R.integer.chickenCost);
                    else tasteCost -= getResources().getInteger(R.integer.chickenCost);
                    break;
            }

            updateCost();
        }
    };

    private void updateSizeCost(@IdRes int checkedId) {

        switch (checkedId) {
            case R.id.sm30:
                sizeCost = Utils.getIntRes(MainActivity.this, R.integer.sm30Cost);
                break;
            case R.id.sm40:
                sizeCost = Utils.getIntRes(MainActivity.this, R.integer.sm40Cost);
                break;
            case R.id.sm50:
                sizeCost = Utils.getIntRes(MainActivity.this, R.integer.sm50Cost);
                break;
        }
        updateCost();
    }

    private int getSelectedSize() {

        int btnId = selectedSize.getCheckedRadioButtonId();

        switch (btnId) {
            case R.id.sm30:
                return 30;
            case R.id.sm40:
                return 40;
            case R.id.sm50:
                return 50;
        }

        return 0;
    }

    private String getTaste() {

        StringBuilder builder = new StringBuilder();

        if (mushroomsTaste.isChecked())
            builder.append(" грибы ");

        if (hamTaste.isChecked())
            builder.append(" ветчина ");

        if (chickenTaste.isChecked())
            builder.append(" курица ");

        if (cheesesTaste.isChecked())
            builder.append(" 4 сыра ");

        return builder.toString();
    }

    private int getPrice() {
        return sizeCost + tasteCost;
    }

    private String getEmail() {

        if (checkBoxEmail.isChecked())
            return inputEmail.getText().toString();
        else return "";

    }

    private void updateBankInfoVisibility(PaymentMethod method) {
        if (method == PaymentMethod.BankCard)
            showBankInputVisibility();
        else
            hideBankInputVisibility();
    }

    private void updateCost() {
        cost1.setText(String.format(Locale.getDefault(), "%d", getPrice()));
        cost2.setText(String.format(Locale.getDefault(), "%d", getPrice()));

    }

    private void showBankInputVisibility() {
        layoutCardNumber.setVisibility(View.VISIBLE);
        layoutExpiry.setVisibility(View.VISIBLE);
        layoutCVV.setVisibility(View.VISIBLE);
    }

    private void hideBankInputVisibility() {
        layoutCardNumber.setVisibility(View.GONE);
        layoutExpiry.setVisibility(View.GONE);
        layoutCVV.setVisibility(View.GONE);
    }

    private boolean checkIfTasteSelected() {
        return mushroomsTaste.isChecked() || hamTaste.isChecked() || chickenTaste.isChecked() || cheesesTaste.isChecked();
    }

}
