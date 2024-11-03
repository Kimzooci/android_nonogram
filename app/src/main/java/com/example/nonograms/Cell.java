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
        this.checked = false; // 초기에는 체크되지 않은 상태
        setBackgroundResource(R.drawable.cell_selector); // selector를 통한 배경 설정
        if (blackSquare) {
            numBlackSquares++;
        }
    }

    /**
     * 검정 사각형으로 표시하는 메서드
     * @return 선택한 셀이 검정 블록인지 여부 반환
     */
    public boolean markBlackSquare() {
        if (checked) {
            return false; // 이미 체크된 셀은 아무 동작하지 않음
        }
        if (blackSquare) {
            setBackgroundResource(R.drawable.cell_pressed_shape); // 검정색으로 변경
            setEnabled(false); // 클릭 비활성화
            numBlackSquares--;
            return true;
        } else {
            setText("X"); // 잘못된 선택일 경우 'X'로 표시
            checked = true;
            return false;
        }
    }

    /**
     * 셀을 비활성화하는 메서드, 게임 종료 시 호출
     */
    public void disableCell() {
        setEnabled(false); // 셀 비활성화
    }

    /**
     * 'X' 표시를 토글하는 메서드
     */
    public void toggleX() {
        checked = !checked;
        setText(checked ? "X" : "");
    }

    /**
     * numBlackSquares를 초기화하는 메서드, 게임 재시작 시 호출
     */
    public static void resetNumBlackSquares() {
        numBlackSquares = 0;
    }

    /**
     * 검정 블록 여부 반환 메서드
     * @return 셀이 검정 블록인지 여부
     */
    public boolean isBlackSquare() {
        return blackSquare;
    }

    /**
     * 현재 남은 검정 블록 개수 반환 메서드
     * @return 남은 검정 블록 개수
     */
    public static int getNumBlackSquares() {
        return numBlackSquares;
    }
}
