package com.example.nonograms;

import android.os.Bundle;
import android.view.Gravity;
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
    private TableLayout tableLayout; // TableLayout 참조 추가
    private TextView lifeTextView;
    private ToggleButton modeToggle;
    private Button restartButton;
    private int life = 3;
    private Cell[][] cells = new Cell[BOARD_SIZE][BOARD_SIZE];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI 요소 초기화
        tableLayout = findViewById(R.id.tableLayout); // XML에서 tableLayout 연결
        lifeTextView = findViewById(R.id.lifeTextView);
        modeToggle = findViewById(R.id.modeToggle);
        restartButton = findViewById(R.id.restartButton);

        // 모드 토글 버튼 이벤트 설정
        modeToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            modeToggle.setText(isChecked ? "검정 사각형 모드" : "X 표시 모드");
        });

        // 재시작 버튼 이벤트 설정
        restartButton.setOnClickListener(v -> restartGame());
        restartButton.setVisibility(View.GONE); // 초기에는 숨김

        // 보드 초기화
        initializeBoard();
    }

    private void initializeBoard() {
        tableLayout.removeAllViews(); // 기존 보드를 제거하여 중복 방지

        Random random = new Random();

        // 셀 초기화
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                cells[row][col] = new Cell(this, random.nextBoolean());
            }
        }

        // LayoutParams 설정
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );

        TableRow.LayoutParams hintParams = new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT,
                0.5f
        );

        TableRow.LayoutParams cellParams = new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT,
                1f
        );

        // 상단 힌트 생성
        TableRow topHintRow = new TableRow(this);
        topHintRow.setLayoutParams(rowParams);

        // 첫 번째 빈칸
        TextView emptyTopLeft = new TextView(this);
        emptyTopLeft.setLayoutParams(hintParams);
        topHintRow.addView(emptyTopLeft);

        for (int col = 0; col < BOARD_SIZE; col++) {
            List<Integer> columnHints = generateHintsForColumn(col);
            TextView hintTextView = new TextView(this);
            hintTextView.setText(formatHint(columnHints));
            hintTextView.setPadding(8, 8, 8, 8);
            hintTextView.setGravity(Gravity.CENTER);
            hintTextView.setLayoutParams(hintParams);
            topHintRow.addView(hintTextView);
        }
        tableLayout.addView(topHintRow);

        // 좌측 힌트와 셀 생성
        for (int row = 0; row < BOARD_SIZE; row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(rowParams);

            // 좌측 힌트 생성
            List<Integer> rowHints = generateHintsForRow(row);
            TextView sideHintTextView = new TextView(this);
            sideHintTextView.setText(formatHint(rowHints));
            sideHintTextView.setPadding(8, 8, 8, 8);
            sideHintTextView.setGravity(Gravity.CENTER);
            sideHintTextView.setLayoutParams(hintParams);
            tableRow.addView(sideHintTextView);

            // 셀 생성 및 이벤트 설정
            for (int col = 0; col < BOARD_SIZE; col++) {
                Cell cell = cells[row][col];
                cell.setOnClickListener(v -> handleCellClick(cell));
                cell.setLayoutParams(cellParams);
                tableRow.addView(cell);
            }
            tableLayout.addView(tableRow);
        }

        // 생명력 초기화
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
        lifeTextView.setText("Life: " + life);
    }

    private void gameOver() {
        lifeTextView.setText("Game Over");
        disableBoard();
        restartButton.setVisibility(View.VISIBLE);
    }

    private void checkGameEnd() {
        if (Cell.getNumBlackSquares() == 0) {
            lifeTextView.setText("You Win!");
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
        Cell.resetNumBlackSquares();
        initializeBoard();
        restartButton.setVisibility(View.GONE);
        lifeTextView.setText("Life: " + life);
    }

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
