// Cell.java
package com.example.nonograms;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

public class Cell extends AppCompatButton {

    private boolean blackSquare; // 검정 블록 여부
    private boolean checked; // 'X' 표시 여부
    private static int numBlackSquares = 0; // 전체 검정 블록 개수

    public Cell(@NonNull Context context, boolean isBlack) {
        super(context);
        this.blackSquare = isBlack;
        setText(""); // 초기 텍스트는 빈칸
        if (blackSquare) {
            numBlackSquares++;
        }
    }

    public boolean markBlackSquare() {
        if (checked) {
            return false; // 이미 체크된 셀은 아무 동작하지 않음
        }
        if (blackSquare) {
            setBackgroundColor(0xFF000000); // 검정색으로 변경
            setEnabled(false); // 클릭 비활성화
            numBlackSquares--;
            return true;
        } else {
            setText("X"); // 잘못된 선택일 경우 'X'로 표시
            checked = true;
            return false;
        }
    }

    public boolean isBlackSquare() {
        return blackSquare;
    }

    public static int getNumBlackSquares() {
        return numBlackSquares;
    }

    public void toggleX() {
        checked = !checked;
        setText(checked ? "X" : "");
    }
}
