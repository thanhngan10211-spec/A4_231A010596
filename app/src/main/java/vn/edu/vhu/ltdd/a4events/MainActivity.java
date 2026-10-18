package vn.edu.vhu.ltdd.a4events;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "A4_231A010596";

    private EditText edtSoA, edtSoB, edtCanNang, edtChieuCao;
    private TextView tvKetQua, tvBmi, tvPhanLoai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        edtSoA = findViewById(R.id.edtSoA);
        edtSoB = findViewById(R.id.edtSoB);
        tvKetQua = findViewById(R.id.tvKetQua);
        edtCanNang = findViewById(R.id.edtCanNang);
        edtChieuCao = findViewById(R.id.edtChieuCao);
        tvBmi = findViewById(R.id.tvBmi);
        tvPhanLoai = findViewById(R.id.tvPhanLoai);
        tvLichSu = findViewById(R.id.tvLichSu);

        Button btnCong = findViewById(R.id.btnCong);
        Button btnTru = findViewById(R.id.btnTru);
        Button btnNhan = findViewById(R.id.btnNhan);
        Button btnChia = findViewById(R.id.btnChia);
        Button btnXoa = findViewById(R.id.btnXoa);
        Button btnTinhBmi = findViewById(R.id.btnTinhBmi);
        Button btnPhanTram = findViewById(R.id.btnPhanTram);
        Button btnDaoDau = findViewById(R.id.btnDaoDau);

        btnPhanTram.setOnClickListener(v -> tinhToan('%'));
        btnDaoDau.setOnClickListener(v -> daoDauSoA());

        btnCong.setOnClickListener(v -> tinhToan('+'));
        btnTru.setOnClickListener(v -> tinhToan('-'));

        View.OnClickListener chung = v -> {
            int id = v.getId();
            if (id == R.id.btnNhan) {
                tinhToan('*');
            } else if (id == R.id.btnChia) {
                tinhToan('/');
            }
        };
        btnNhan.setOnClickListener(chung);
        btnChia.setOnClickListener(chung);

        btnXoa.setOnClickListener(v -> xoaTrang());
        btnTinhBmi.setOnClickListener(v -> tinhBmi());
    }

    private void tinhToan(char phepToan) {
        String chuoiA = edtSoA.getText().toString().trim();
        String chuoiB = edtSoB.getText().toString().trim();

        if (chuoiA.isEmpty()) {
            edtSoA.setError(getString(R.string.err_empty));
            edtSoA.requestFocus();
            return;
        }
        if (chuoiB.isEmpty()) {
            edtSoB.setError(getString(R.string.err_empty));
            edtSoB.requestFocus();
            return;
        }

        double a, b;
        try {
            a = Double.parseDouble(chuoiA);
            b = Double.parseDouble(chuoiB);
        } catch (NumberFormatException e) {
            Log.e(TAG, "Dữ liệu nhập không phải số: '" + chuoiA + "', '" + chuoiB + "'", e);

            Toast.makeText(this, "Lỗi 4: Vui lòng nhập đúng định dạng số ở máy tính!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phepToan == '/' && b == 0) {
            edtSoB.setError(getString(R.string.err_divide_zero));
            Toast.makeText(this, R.string.err_divide_zero, Toast.LENGTH_SHORT).show();
            return;
        }

        double ketQua;
        switch (phepToan) {
            case '+': ketQua = a + b; break;
            case '-': ketQua = a - b; break;
            case '*': ketQua = a * b; break;
            case '%': ketQua = a * (b / 100.0); break;
            default:  ketQua = a / b; break;
        }

        tvKetQua.setText(String.format(Locale.getDefault(), "%.2f %c %.2f = %.2f",
                a, phepToan, b, ketQua));
        Log.d(TAG, "Phép tính: " + a + " " + phepToan + " " + b + " = " + ketQua);

        String ketQuaHienTai = String.format(Locale.getDefault(), "%.2f %c %.2f = %.2f", a, phepToan, b, ketQua);

        danhSachLichSu.add(0, ketQuaHienTai);

        if (danhSachLichSu.size() > 5) {
            danhSachLichSu.remove(danhSachLichSu.size() - 1);
        }

        StringBuilder sb = new StringBuilder("Lịch sử gần đây:\n");
        for (String item : danhSachLichSu) {
            sb.append("- ").append(item).append("\n");
        }
        tvLichSu.setText(sb.toString());}

    private void xoaTrang() {
        edtSoA.setText("");
        edtSoB.setText("");
        edtSoA.setError(null);
        edtSoB.setError(null);
        tvKetQua.setText(R.string.result_placeholder);
        edtSoA.requestFocus();
    }

    private void tinhBmi() {
        try {
            double canNang = Double.parseDouble(edtCanNang.getText().toString().trim());
            double chieuCao = Double.parseDouble(edtChieuCao.getText().toString().trim());

            if (canNang <= 0 || chieuCao <= 0) {
                Toast.makeText(this, R.string.err_positive, Toast.LENGTH_SHORT).show();
                return;
            }
            // Cho phép nhập 1.70 (mét) hoặc 170 (cm)
            if (chieuCao > 3) {
                chieuCao = chieuCao / 100.0;
            }

            double bmi = canNang / (chieuCao * chieuCao);
            tvBmi.setText(String.format(Locale.getDefault(), "BMI = %.1f", bmi));
            tvPhanLoai.setText(phanLoai(bmi));
        } catch (NumberFormatException e) {
            Log.e(TAG, "Lỗi nhập liệu BMI", e);
            Toast.makeText(this, R.string.err_not_number, Toast.LENGTH_SHORT).show();
        }
    }

    private String phanLoai(double bmi) {
        if (bmi < 18.5) return getString(R.string.bmi_under);
        if (bmi < 23) return getString(R.string.bmi_normal);
        if (bmi < 25) return getString(R.string.bmi_over);
        return getString(R.string.bmi_obese);
    }
    private void daoDauSoA() {
        String chuoiA = edtSoA.getText().toString().trim();

        if (chuoiA.isEmpty()) {
            edtSoA.setError(getString(R.string.err_empty));
            edtSoA.requestFocus();
            return;
        }

        if (chuoiA.equals("-")) {
            edtSoA.setText("");
            edtSoA.setError(null);
            return;
        }

        try {
            double val = Double.parseDouble(chuoiA);
            val = val * -1;


            if (val == (long) val) {
                edtSoA.setText(String.format(Locale.getDefault(), "%d", (long) val));
            } else {
                edtSoA.setText(String.format(Locale.getDefault(), "%s", val));
            }


            edtSoA.setSelection(edtSoA.getText().length());
            edtSoA.setError(null);

        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.err_not_number, Toast.LENGTH_SHORT).show();
        }
    }
    private ArrayList<String> danhSachLichSu = new ArrayList<>();
    private TextView tvLichSu;
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("KEY_LICHSU", danhSachLichSu);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ArrayList<String> savedList = savedInstanceState.getStringArrayList("KEY_LICHSU");
        if (savedList != null) {
            danhSachLichSu = savedList;
            StringBuilder sb = new StringBuilder("Lịch sử gần đây:\n");
            for (String item : danhSachLichSu) {
                sb.append("- ").append(item).append("\n");
            }
            tvLichSu.setText(sb.toString());
        }
    }
}

