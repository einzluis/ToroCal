package com.einzluis.etorocalculator;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.text.DecimalFormat;

import org.w3c.dom.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("ToroCal - Evaluating instruments"); // Main Activity display name

        // Spinner + Adapter
        Spinner spInstruments = (Spinner)findViewById(R.id.spinInstruments);
        ArrayAdapter<String> spInstrumentsAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Instruments));

        spInstrumentsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spInstruments.setAdapter(spInstrumentsAdapter);

        // Other views
        TextView tInvested = (TextView)findViewById(R.id.txvInvested);
        TextView tValue = (TextView)findViewById(R.id.txvValue);
        TextView tPercent = (TextView)findViewById(R.id.txvPercentagePL);
        TextView tAmount = (TextView)findViewById(R.id.txvAmountPL);
        Button btnCompute = (Button)findViewById(R.id.btnCompute);
        EditText etShares = (EditText)findViewById(R.id.etShares);
        etShares.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(9, 2)});
        EditText etBuy = (EditText)findViewById(R.id.etBuy);
        etBuy.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(9, 2)});
        EditText etSell = (EditText)findViewById(R.id.etSell);
        etSell.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(9, 2)});

        // Setting the decimal format
        DecimalFormat df = new DecimalFormat("00.00");

        /*
        TO-DO:
        - Price Features
            > Show total amount invested---------------------------------------------------------------DONE
            > Show total amount invested (after computation)-------------------------------------------DONE
            > Profit and loss statement (P/L) in $-----------------------------------------------------DONE
            > Profit and loss statement (P/L) in %-----------------------------------------------------DONE
            > E7 bug for large numbers
            > App crashes when you click the button while having no values in it

        - Display
            > Display first 2 decimal places only------------------------------------------------------DONE
            > New text views (?) / Search how to concatenate texts and variables in text view.---------DONE
            > Display spread fee

        - TBA
            > Theme (Light/Dark) ----------------------------------------------------------------------DONE
            > Calculate different fees
                > Create a fee text view that is clickable
                    > Create a floating window to display the fees
                        > Overnight fees, Daily fees, Spread fees, etc.
            > Side Bar/Bottom Tabs
                > Asset/Instrument Picker
                    > Drop-down menu for markets (?)
                        > Crypto Coins, Indices, Major Currencies, ETFs, Commodities, Stocks
                    > Drop-down menu for different instruments inside those market
                        > Symbols
            > About Us


            Author: Einz Luis
            Started on 11/25/2020

            <!-- Practice project -->
         */

        // Set onClickListener for compute button
        btnCompute.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Double shares = Double.valueOf(df.format(Double.parseDouble(etShares.getText().toString())));
                        Double buy = Double.valueOf(df.format(Double.parseDouble(etBuy.getText().toString())));
                        Double sell = Double.valueOf(df.format(Double.parseDouble(etSell.getText().toString())));


                        Double sellPrice = shares * sell;
                        Double buyPrice = Double.valueOf(df.format(shares * buy));
                        Double amountgain = Double.valueOf(df.format(sellPrice - buyPrice));
                        Double percentgain = Double.valueOf(df.format((amountgain/buyPrice)*100));
                        Double value = Double.valueOf(df.format(buyPrice + amountgain));

                    }
                });
    }

    // Input filter - maximum will be 2 decimals places (See ln. 45-49)
    class DecimalDigitsInputFilter implements InputFilter {
        private Pattern mPattern;
        DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
            mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
        }
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches())
                return "";
            return null;
        }
    }
}