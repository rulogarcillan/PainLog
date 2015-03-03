package com.pain.log.painlog.negocio;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pain.log.painlog.BD.Consultas;
import com.pain.log.painlog.BD.MyDatabase;
import com.pain.log.painlog.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.pain.log.painlog.negocio.LogUtils.LOGI;

/**
 * Created by Rulo on 01/03/2015.
 */
public class DolActivity extends BaseActivity {

    private int clave, clave_i;
    private String servicio;
    private SeekBar seekBar;
    private Button btnNext;
    private TextView textDolor;
    private EditText textFecha, textNotas;

    private LinearLayout viewLay;
    private Logs item;
    // BBDD
    private MyDatabase myDB; //base de datos
    private Consultas consultas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dolencia);

        cargaDatos();
        myDB = new MyDatabase(this);
        consultas = new Consultas(this);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                               @Override
                                               public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                                                   if (progress <= 32) {

                                                       viewLay.setBackgroundColor(getResources().getColor(R.color.color_b));
                                                       textDolor.setText(R.string.dolor1);
                                                   } else if (progress >= 33 && progress <= 65) {
                                                       viewLay.setBackgroundColor(getResources().getColor(R.color.color_f));
                                                       textDolor.setText(R.string.dolor2);
                                                   } else if (progress >= 66) {
                                                       viewLay.setBackgroundColor(getResources().getColor(R.color.color_j));
                                                       textDolor.setText(R.string.dolor3);
                                                   }
                                               }

                                               @Override
                                               public void onStartTrackingTouch(SeekBar seekBar) {

                                               }

                                               @Override
                                               public void onStopTrackingTouch(SeekBar seekBar) {

                                               }
                                           }

        );

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFechaValida(textFecha.getText().toString())) {
                    if (servicio.equals("INS")) {
                        inserta();
                    } else if (servicio.equals("UPD")) {
                        actualiza();
                    }
                    finish();
                } else {
                    Toast.makeText(DolActivity.this, R.string.errorfecha, Toast.LENGTH_SHORT).show();
                }
            }
        });

        textFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(DolActivity.this,
                        new mDateSetListener(), mYear, mMonth, mDay);
                dialog.show();
            }
        });


        //que hacemos
        if (servicio.equals("INS")) {
            getSupportActionBar().setTitle(R.string.addLog);
        } else if (servicio.equals("UPD")) {
            item = consultas.getOneLog(clave_i, clave);
            getSupportActionBar().setTitle(R.string.editarTittle);

            textFecha.setText(item.getFecha());
            seekBar.setProgress(item.getIntensidad());
            textNotas.setText(item.getNotas());
        }
    }


    private void cargaDatos() {

        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        Bundle extras = getIntent().getExtras();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        textFecha = (EditText) findViewById(R.id.textFecha);
        textNotas = (EditText) findViewById(R.id.textNotas);
        textDolor = (TextView) findViewById(R.id.textDolor);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        btnNext = (Button) findViewById(R.id.btnNext);
        viewLay = (LinearLayout) findViewById(R.id.viewLay);

        clave = extras.getInt("CLAVE");
        clave_i = extras.getInt("CLAVE_I");
        LOGI("VALOR CLAVE", Integer.toString(clave));
        LOGI("VALOR CLAVE_I", Integer.toString(clave_i));

        servicio = extras.getString("SERVICIO");
        LOGI("VALOR SERVICIO", servicio);


        textFecha.setText(new StringBuilder()
                .append(String.format("%02d", mDay)).append("/").append(String.format("%02d", mMonth + 1)).append("/")
                .append(mYear).append(" "));


    }

    class mDateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            int mYear = year;
            int mMonth = monthOfYear;
            int mDay = dayOfMonth;
            textFecha.setText(new StringBuilder()
                    .append(String.format("%02d", mDay)).append("/").append(String.format("%02d", mMonth + 1)).append("/")
                    .append(mYear).append(" "));

        }
    }

    private void inserta() {
        Logs item;
        int claveLog = consultas.genKeyIdTablaReg();
        item = new Logs(claveLog, textFecha.getText().toString(), seekBar.getProgress(), textNotas.getText().toString(), clave);
        consultas.addRegistros(item);
    }

    private void actualiza() {

        item.setFecha(textFecha.getText().toString());
        item.setIntensidad(seekBar.getProgress());
        item.setNotas(textNotas.getText().toString());
        consultas.editlog(item);

    }

    public static boolean isFechaValida(String fecha) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            formatoFecha.setLenient(false);
            formatoFecha.parse(fecha);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }


}
