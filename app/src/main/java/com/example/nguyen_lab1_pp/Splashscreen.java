package com.example.nguyen_lab1_pp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Splashscreen extends AppCompatActivity {

    final int size = 1000000;
    static final String key = "ValueKey";
    ArrayList<String> values = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            makeArray(size, values);//СТРОКА 1
        }
        setContentView(R.layout.activity_main);
        RecyclerView list = (RecyclerView) findViewById(R.id.list);
        list.setAdapter(new CustomAdapter(this, R.layout.list_item, size));
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

        private LayoutInflater inflater;
        private int number;

        CustomAdapter(Context context, int textRes, int number) {
            //this.numbers = numbers;
            this.number = number;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = inflater.inflate(R.layout.list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.nameView.setText(WordsRus(position+1)); //строка задаёт текст
            holder.view.setBackgroundColor((position & 1) == 1 ? Color.WHITE : Color.LTGRAY);
        }

        @Override
        public int getItemCount() {
            //return numbers.size();
            return number;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            View view;
            final ImageView imageView;
            final TextView nameView;

            ViewHolder(View view) {
                super(view);
                this.view = view;
                imageView = (ImageView) view.findViewById(R.id.image);
                nameView = (TextView) view.findViewById(R.id.label);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putStringArrayList(key, values);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        values = savedInstanceState.getStringArrayList(key);
    }

    protected void makeArray(int n, ArrayList<String> values) {
        for (int i = 0; i < n; i++) {
            values.add(WordsRus(i + 1));
        }
    }

    // Перевод числа в диапазоне в текстовую форму
    private static int billion;
    private static int million;
    private static int thousand;
    private static int toThousand;
    private static long numberA;
    private static long numberMax = 999999999999L;
    private static String numText;// число в виде текста

    private static int indexA;
    private static int units;          // единичные значение
    private static int decimal;        // десятичное значение
    private static int hundreds;       // сотни

    private static final String[][] sampleText = {{"", "од", "дв", "три", "четыре", "пять", "шесть", "семь", "восемь", "девять"},
            {"", "десять ", "двадцать ", "тридцать ", "сорок ", "пятьдесят ", "шестьдесят ", "семьдесят ", "восемьдесят ", "девяносто "},
            {"", "сто ", "двести ", "триста ", "четыреста ", "пятьсот ", "шестьсот ", "семьсот ", "восемьсот ", "девятьсот "}};

    private static final String[] sample11to19 = {"десять ", "одинадцать ", "двенадцать ", "тринадцать ", "четырнадцать ", "пятнадцать ",
            "шеснадцать ", "семьнадцать ", "восемьнадцать ", "девятнадцать ", "девятнадцать "};

    private static final String[][] textMillion = {{"", "", "", ""},
            {"миллиардов ", "миллионов ", "тысяч ", ""},
            {"миллиард ", "миллион ", "тысяча ", ""},
            {"миллиарда ", "миллиона ", "тысячи ", ""},
            {"миллиардов ", "миллионов ", "тысяч ", ""}};

    public static String WordsRus(long number) {
        numberA = number;

        numText = "";
        if (numberA < -numberMax || numberA > numberMax) {
            return numText = "Число выходит за рамки указанного диапазона";
        }
        if (numberA == 0) {
            return numText = "ноль ";
        }
        if (number < 0) {
            numText = "минус ";
            numberA = -numberA;
        } //делаем позитивное значение number

// разбиваем число на миллиарды,миллионы,тысячи и единицы
        billion = (int) (numberA / 1000000000);
        million = (int) (numberA - (billion * 1000000000)) / 1000000;
        thousand = (int) (numberA - (billion * 1000000000) - (million * 1000000)) / 1000;
        toThousand = (int) (numberA % 1000);

        // формируем текст числа прописью
        numText = numText + WordsToThousand(billion, 0) + WordsToThousand(million, 1) + WordsToThousand(thousand, 2) + WordsToThousand(toThousand, 3);
        return numText;

    }

    private static String WordsToThousand(int numericalValue, int index) {
// разбиваем образец числа на составляющие
        hundreds = numericalValue / 100;
        decimal = (numericalValue - (hundreds * 100)) / 10;
        units = numericalValue % 10;

// формируем число без степени числа
        numText = "";
        if (decimal == 1) numText = sampleText[2][hundreds] + sample11to19[units];
        else numText = sampleText[2][hundreds] + sampleText[1][decimal] + sampleText[0][units];

        // формируем окончания в единицах
        if (index == 2) {
            if (units == 1 && decimal != 1) numText = numText + "на ";
            else if (units == 2 & decimal != 1) numText = numText + "е ";
            if (units > 1 && decimal != 1) numText = numText + " ";
        } else {
            if (units == 1 && decimal != 1) numText = numText + "ин ";
            if (units == 2 & decimal != 1) {
                numText = numText + "а ";
            } else if (units != 0 & decimal != 1 && units!=1 ) numText = numText + " ";
        }

        // дописываем степень числа
        indexA = 0;
        if (numericalValue != 0) {
            if (units == 0 || decimal == 1) indexA = 1;
            else if (units == 1) indexA = 2;
            else if (units > 1 & units < 5) indexA = 3;
            else indexA = 4;
        }
        numText = numText + textMillion[indexA][index];
        return numText;
    }
}
