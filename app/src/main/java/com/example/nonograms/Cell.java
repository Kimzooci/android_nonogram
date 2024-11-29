package com.example.nonograms;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

public class Cell extends AppCompatButton {

    private boolean blackSquare;
    private boolean checked;
    private static int numBlackSquares = 0;

    public Cell(@NonNull Context context, boolean isBlack) {
        super(context);
        this.blackSquare = isBlack;
        this.checked = false;
        setBackgroundResource(R.drawable.cell_selector);

        if (blackSquare) {
            numBlackSquares++;
        }
    }

    public boolean markBlackSquare() {
        if (checked) return false;

        if (blackSquare) {
            setBackgroundResource(R.drawable.cell_pressed_shape);
            setEnabled(false);
            numBlackSquares--;
            return true;
        } else {
            setText("X");
            checked = true;
            return false;
        }
    }

    public void toggleX() {
        checked = !checked;
        setText(checked ? "X" : "");
    }

    public static void resetNumBlackSquares() {
        numBlackSquares = 0;
    }

    public boolean isBlackSquare() {
        return blackSquare;
    }

    public static int getNumBlackSquares() {
        return numBlackSquares;
    }


}
