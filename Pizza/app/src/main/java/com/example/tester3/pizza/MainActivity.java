package com.example.tester3.pizza;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private CheckBox mushroomsTaste;
    private CheckBox hamTaste;
    private CheckBox chickenTaste;
    private CheckBox cheesesTaste;
    private CheckBox email;
    private TextView cost1;
    private TextView cost2;
    private Spinner paymentMethod;
    private LinearLayout layoutExpiry;
    private LinearLayout layoutCVV;
    private LinearLayout layoutCardNumber;
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
        email = findViewById(R.id.checkBoxEmail);
        cost1 = findViewById(R.id.cost1);
        cost2 = findViewById(R.id.cost2);

        ((RadioGroup) findViewById(R.id.pizzaSize)).setOnCheckedChangeListener((group, checkedId) -> {

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
        });

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

    private void updateBankInfoVisibility(PaymentMethod method) {
        if (method == PaymentMethod.BankCard)
            showBankInputVisibility();
        else
            hideBankInputVisibility();
    }

    private void updateCost() {
        cost1.setText(String.format(Locale.getDefault(), "%d", sizeCost + tasteCost));
        cost2.setText(String.format(Locale.getDefault(), "%d", sizeCost + tasteCost));

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
}
