package com.example.nonograms;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int BOARD_SIZE = 5;
    private TextView lifeTextView;
    private ToggleButton modeToggle;
    private Button restartButton;
    private int life = 3;
    private Cell[][] cells = new Cell[BOARD_SIZE][BOARD_SIZE];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lifeTextView = findViewById(R.id.lifeTextView);
        modeToggle = findViewById(R.id.modeToggle);
        restartButton = findViewById(R.id.restartButton);

        modeToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            modeToggle.setText(isChecked ? "검정 사각형 모드" : "X 표시 모드");
        });

        restartButton.setOnClickListener(v -> restartGame());
        restartButton.setVisibility(View.GONE);

        initializeBoard();
    }

    private void initializeBoard() {
        TableLayout tableLayout = findViewById(R.id.tableLayout);
        tableLayout.removeAllViews();

        Random random = new Random();

        // 각 셀을 먼저 초기화
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                cells[row][col] = new Cell(this, random.nextBoolean());
            }
        }

        // 위쪽 힌트 설정 (열 힌트)
        TableRow topHintRow = new TableRow(this);
        topHintRow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        topHintRow.addView(new TextView(this)); // 첫 번째 빈칸

        // 각 열에 대한 힌트 설정
        for (int col = 0; col < BOARD_SIZE; col++) {
            List<Integer> columnHints = generateHintsForColumn(col);
            String hintText = formatHint(columnHints);
            TextView hintTextView = new TextView(this);
            hintTextView.setText(hintText);
            hintTextView.setPadding(4, 4, 4, 4); // 패딩을 조정하여 잘림 방지
            hintTextView.setGravity(android.view.Gravity.CENTER);
            topHintRow.addView(hintTextView);
        }
        tableLayout.addView(topHintRow);

        // 좌측 힌트와 보드 셀 생성
        for (int row = 0; row < BOARD_SIZE; row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            // 각 행에 대한 힌트 설정
            List<Integer> rowHints = generateHintsForRow(row);
            String hintText = formatHint(rowHints);
            TextView sideHintTextView = new TextView(this);
            sideHintTextView.setText(hintText);
            sideHintTextView.setPadding(4, 4, 4, 4); // 패딩을 조정하여 잘림 방지
            sideHintTextView.setGravity(android.view.Gravity.CENTER);
            tableRow.addView(sideHintTextView);

            // 셀 생성 및 이벤트 설정
            for (int col = 0; col < BOARD_SIZE; col++) {
                Cell cell = cells[row][col];
                cell.setOnClickListener(v -> handleCellClick(cell));
                tableRow.addView(cell);
            }
            tableLayout.addView(tableRow);
        }

        life = 3;
        updateLifeText();
    }

    private void handleCellClick(Cell cell) {
        if (modeToggle.isChecked()) {
            cell.toggleX();
        } else {
            if (cell.markBlackSquare()) {
                checkGameEnd();
            } else {
                decreaseLife();
            }
        }
    }

    private void decreaseLife() {
        life--;
        updateLifeText();
        if (life <= 0) {
            gameOver();
        }
    }

    private void updateLifeText() {
        lifeTextView.setText(getString(R.string.life_text, life));
    }

    private void gameOver() {
        lifeTextView.setText(R.string.game_over);
        disableBoard();
        restartButton.setVisibility(View.VISIBLE);
    }

    private void checkGameEnd() {
        if (Cell.getNumBlackSquares() == 0) {
            lifeTextView.setText(R.string.you_win);
            disableBoard();
        }
    }

    private void disableBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                cells[row][col].setEnabled(false);
            }
        }
    }

    private void restartGame() {
        life = 3;
        initializeBoard();
        restartButton.setVisibility(View.GONE);
        lifeTextView.setText(getString(R.string.life_text, life));
    }

    // 행의 힌트 생성
    private List<Integer> generateHintsForRow(int row) {
        List<Integer> hints = new ArrayList<>();
        int count = 0;
        for (int col = 0; col < BOARD_SIZE; col++) {
            if (cells[row][col].isBlackSquare()) {
                count++;
            } else if (count > 0) {
                hints.add(count);
                count = 0;
            }
        }
        if (count > 0) hints.add(count);
        return hints.isEmpty() ? List.of(0) : hints;
    }

    // 열의 힌트 생성
    private List<Integer> generateHintsForColumn(int col) {
        List<Integer> hints = new ArrayList<>();
        int count = 0;
        for (int row = 0; row < BOARD_SIZE; row++) {
            if (cells[row][col].isBlackSquare()) {
                count++;
            } else if (count > 0) {
                hints.add(count);
                count = 0;
            }
        }
        if (count > 0) hints.add(count);
        return hints.isEmpty() ? List.of(0) : hints;
    }

    // 힌트를 포맷팅하여 빈칸으로 구분된 문자열로 반환
    private String formatHint(List<Integer> hints) {
        StringBuilder hintText = new StringBuilder();
        for (int i = 0; i < hints.size(); i++) {
            hintText.append(hints.get(i));
            if (i < hints.size() - 1) {
                hintText.append(" ");
            }
        }
        return hintText.toString();
    }
}
