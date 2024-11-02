// MainActivity.java
package com.example.nonograms;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private final int BOARD_SIZE = 8;
    private Cell[][] cells = new Cell[BOARD_SIZE][BOARD_SIZE];
    private int life = 3;
    private TextView lifeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 생명력 텍스트뷰 설정
        lifeTextView = findViewById(R.id.lifeTextView);

        // 보드 초기화
        TableLayout tableLayout = findViewById(R.id.tableLayout);
        initializeBoard(tableLayout);
    }

    private void initializeBoard(TableLayout tableLayout) {
        Random random = new Random();

        for (int i = 0; i < BOARD_SIZE; i++) {
            TableRow tableRow = new TableRow(this);
            for (int j = 0; j < BOARD_SIZE; j++) {
                Cell cell = new Cell(this, random.nextBoolean());
                cells[i][j] = cell;
                tableRow.addView(cell);

                // 클릭 이벤트 설정
                cell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleCellClick((Cell) v);
                    }
                });
            }
            tableLayout.addView(tableRow);
        }
    }

    private void handleCellClick(Cell cell) {
        if (cell.markBlackSquare()) {
            // 검정 사각형을 찾음
            checkGameEnd();
        } else {
            // 잘못된 클릭 시 생명력 감소
            life--;
            lifeTextView.setText("Life: " + life);
            if (life <= 0) {
                gameOver();
            }
        }
    }

    private void gameOver() {
        lifeTextView.setText("Game Over");
        // 모든 셀을 클릭하지 못하게 비활성화
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                cells[i][j].setEnabled(false);
            }
        }
    }

    private void checkGameEnd() {
        int remainingBlackSquares = Cell.getNumBlackSquares();
        if (remainingBlackSquares == 0) {
            lifeTextView.setText("You Win!");
        }
    }
}
