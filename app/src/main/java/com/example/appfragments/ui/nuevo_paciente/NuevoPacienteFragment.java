package com.example.appfragments.ui.nuevo_paciente;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.appfragments.R;
import com.example.appfragments.SQL.SQLite;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NuevoPacienteFragment extends Fragment {

    private NuevoPacienteViewModel nuevoPacienteViewModel;

    private int dia, mes, anio;
    private static final int REQUEST_TAKE_PHOTO=1;
    private Spinner sp_area, sp_doctor, sp_sexo;
    private EditText et_id, et_nombre, et_fecha, et_edad, et_estatura, et_peso;
    private String strArea, strDoctor, strSexo, pathImage = "", currentPhotoPath="";
    private Button btn_guardar, btn_limpiar, btn_fecha;
    private ImageView iv_foto;
    Uri photoURI;
    SQLite sqLite;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        nuevoPacienteViewModel =
                ViewModelProviders.of(this).get(NuevoPacienteViewModel.class);
        View root = inflater.inflate(R.layout.fragment_nuevo_paciente, container, false);

        // Enlazamos interfaz con el modelo
        sp_area = root.findViewById(R.id.nuevo_paciente_sp_areaDeIngreso);
        sp_doctor = root.findViewById(R.id.nuevo_paciente_sp_doctorCrear);
        sp_sexo = root.findViewById(R.id.nuevo_paciente_sp_sexo);

        et_id = root.findViewById(R.id.nuevo_paciente_et_idPaciente);
        et_nombre = root.findViewById(R.id.nuevo_paciente_et_nombrePaciente);
        et_fecha = root.findViewById(R.id.nuevo_paciente_et_fechaIngreso);
        et_edad = root.findViewById(R.id.nuevo_paciente_et_edadPaciente);
        et_estatura = root.findViewById(R.id.nuevo_paciente_et_estaturaPaciente);
        et_peso = root.findViewById(R.id.nuevo_paciente_et_pesoPaciente);

        btn_fecha = root.findViewById(R.id.nuevo_paciente_btn_fechaIngreso);
        btn_limpiar = root.findViewById(R.id.nuevo_paciente_btn_limpiar);
        btn_guardar = root.findViewById(R.id.nuevo_paciente_btn_guardar);

        iv_foto = root.findViewById(R.id.nuevo_paciente_iv_foto);

        // Conexión con base de datos
        sqLite = new SQLite(getContext());
        sqLite.abrir();

        configureSpinnerAreaDoctor();
        configureSpinnerSexo();
        configureFecha();

        configureTomarFoto();
        configureBtnGuardar();
        configureBtnLimpiar();

        return root;
    }



    private void configureSpinnerAreaDoctor(){

        final ArrayAdapter<CharSequence> areaAdapter = ArrayAdapter.createFromResource(
                getContext(), R.array.opciones,
                android.R.layout.simple_spinner_item
        );
        // Preparando los spinner del área y doctor. El spinner del doctor depende del área que seleccionó el usuario
        sp_area.setAdapter(areaAdapter);
        sp_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String opcion = String.valueOf(sp_area.getSelectedItemId());
                int op = Integer.parseInt(opcion);
                System.out.println(opcion);
                if (op==0){
                }else if (op ==1){
                    final ArrayAdapter<CharSequence> doctorAdapter =
                            ArrayAdapter.createFromResource(
                                    getContext(),
                                    R.array.o1,
                                    android.R.layout.simple_spinner_item);

                    sp_doctor.setAdapter(doctorAdapter);
                    strArea = areaAdapter.getItem(1).toString();
                    sp_doctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String opcion = String.valueOf(sp_doctor.getSelectedItemId());
                            int op = Integer.parseInt(opcion);
                            System.out.println(opcion);
                            if (op==0){
                            }else if (op==1){
                                strDoctor=doctorAdapter.getItem(1).toString();
                            }else if(op==2){
                                strDoctor=doctorAdapter.getItem(2).toString();
                            }else if(op==3){
                                strDoctor=doctorAdapter.getItem(3).toString();
                            }else if(op==4){
                                strDoctor=doctorAdapter.getItem(4).toString();
                            }
                            if(op!=0) Toast.makeText(getContext(),strArea+" "+strDoctor, Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                }//Primera selección
                else if (op ==2){
                    final ArrayAdapter<CharSequence> doctorAdapter =
                            ArrayAdapter.createFromResource(
                                    getContext(),
                                    R.array.o2, //Aereos
                                    android.R.layout.simple_spinner_item);
                    sp_doctor.setAdapter(doctorAdapter);
                    strArea = areaAdapter.getItem(2).toString(); //Clasificacion 2 - Aereos
                    sp_doctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String opcion = String.valueOf(sp_doctor.getSelectedItemId());
                            int op = Integer.parseInt(opcion);
                            System.out.println(opcion);
                            if (op==0){
                            }else if (op==1){
                                strDoctor=doctorAdapter.getItem(1).toString();
                            }else if(op==2){
                                strDoctor=doctorAdapter.getItem(2).toString();
                            }else if(op==3){
                                strDoctor=doctorAdapter.getItem(3).toString();
                            }else if(op==4){
                                strDoctor=doctorAdapter.getItem(4).toString();
                            }
                            if(op!=0) Toast.makeText(getContext(),strArea+" "+strDoctor, Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                }//Segunda selección
                else if (op ==3){
                    final ArrayAdapter<CharSequence> doctorAdapter =
                            ArrayAdapter.createFromResource(
                                    getContext(),
                                    R.array.o3, //Terrestres
                                    android.R.layout.simple_spinner_item);

                    sp_doctor.setAdapter(doctorAdapter);
                    strArea = areaAdapter.getItem(3).toString(); //Clasificacion 3 - Terrestres
                    sp_doctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String opcion = String.valueOf(sp_doctor.getSelectedItemId());
                            int op = Integer.parseInt(opcion);
                            System.out.println(opcion);
                            if (op==0){
                            }else if (op==1){
                                strDoctor=doctorAdapter.getItem(1).toString();
                            }else if(op==2){
                                strDoctor=doctorAdapter.getItem(2).toString();
                            }else if(op==3){
                                strDoctor=doctorAdapter.getItem(3).toString();
                            }else if(op==4){
                                strDoctor=doctorAdapter.getItem(4).toString();
                            }
                            if(op!=0) Toast.makeText(getContext(),strArea+" "+strDoctor, Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                }//Tercera selección
                else if (op ==4){
                    final ArrayAdapter<CharSequence> doctorAdapter =
                            ArrayAdapter.createFromResource(
                                    getContext(),
                                    R.array.o4, //Acuaticos
                                    android.R.layout.simple_spinner_item);

                    sp_doctor.setAdapter(doctorAdapter);
                    strArea = areaAdapter.getItem(4).toString(); //Clasificacion 4 - Acuaticos
                    sp_doctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String opcion = String.valueOf(sp_doctor.getSelectedItemId());
                            int op = Integer.parseInt(opcion);
                            System.out.println(opcion);
                            if (op==0){
                            }else if (op==1){
                                strDoctor=doctorAdapter.getItem(1).toString();
                            }else if(op==2){
                                strDoctor=doctorAdapter.getItem(2).toString();
                            }else if(op==3){
                                strDoctor=doctorAdapter.getItem(3).toString();
                            }else if(op==4){
                                strDoctor=doctorAdapter.getItem(4).toString();
                            }
                            if(op!=0) Toast.makeText(getContext(),strArea+" "+strDoctor, Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                }//Cuarta selección
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void configureSpinnerSexo() {
        //Spinner de SEXO
        final ArrayAdapter<CharSequence> sexoAdapter =
                ArrayAdapter.createFromResource(
                        getContext(),
                        R.array.sx,
                        android.R.layout.simple_spinner_item);
        sp_sexo.setAdapter(sexoAdapter);
        sp_sexo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String opcion = String.valueOf(sp_sexo.getSelectedItemId());
                int op = Integer.parseInt(opcion);
                System.out.println(opcion);
                if (op == 1){
                    strSexo = "MACULINO";
                }else if (op == 2){
                    strSexo = "FEMENINO";
                }
            }
            //Fin spinner sexo
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void configureFecha(){
        // Configurando la fecha - datepicker
        btn_fecha.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                final Calendar c= Calendar.getInstance();
                dia=c.get(Calendar.DAY_OF_MONTH);
                mes=c.get(Calendar.MONTH);
                anio = c.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear , int dayOfMonth) {
                        //     sFecha=Integer.toString(2019-year);
                        et_fecha.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
                    }
                },anio,mes,dia);
                datePickerDialog.show();
            }
        });
    }

    private void configureTomarFoto(){
        //Tomar fotografia
        iv_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tomarfoto=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //Se comprueba que se encontro una actividad para genera la foto
                if(tomarfoto.resolveActivity(getActivity().getPackageManager())!=null){
                    //se crea el archivo donde se guardara la imagen
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex){
                        Toast.makeText(getContext(), "Ocurrio un error mientras se generaba el archivo", Toast.LENGTH_SHORT).show();
                    }
                    //se comprueba que la imagen fue creada correctamente
                    if(photoFile != null){
                        photoURI = FileProvider.getUriForFile(getContext(),"com.example.appfragments",photoFile);
                        tomarfoto.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(tomarfoto,REQUEST_TAKE_PHOTO);
                    }
                }
            }
        });
    }

    //Crear archivo de imagen
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void configureBtnGuardar(){
        //Guardar registro
        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( !et_id.getText().toString().equals("") &&
                        !et_nombre.getText().toString().equals("") &&
                        !et_edad.getText().toString().equals("") &&
                        !et_estatura.getText().toString().equals("") &&
                        !et_peso.getText().toString().equals("") &&
                        !et_fecha.getText().toString().equals("") &&
                        !strArea.equals("") &&
                        !strDoctor.equals("")&&
                        !pathImage.equals("")){
                        //dentro de if
                        Toast.makeText(getContext(), strArea+" "+strDoctor+" "+
                            et_nombre.getText().toString().toUpperCase()+" "+
                            strSexo+" "+
                            et_edad.getText().toString().toUpperCase()+" "+
                            et_peso.getText().toString().toUpperCase()+" "+
                            et_estatura.getText().toString().toUpperCase()+" "+
                            et_fecha.getText().toString(), Toast.LENGTH_SHORT).show();

                        if (sqLite.addregistroPaciente(
                                Integer.parseInt(et_id.getText().toString()),
                                strArea,
                                strDoctor,
                                et_nombre.getText().toString().toUpperCase(),
                                strSexo,
                                et_fecha.getText().toString(),
                                et_edad.getText().toString().toUpperCase(),
                                et_estatura.getText().toString().toUpperCase(),
                                et_peso.getText().toString().toUpperCase(),
                                pathImage
                        )){//Dentro if agregar registro
                            Toast.makeText(getContext(), "REGISTRO AÑADIDO",Toast.LENGTH_SHORT).show();
                            limpiarCampos();
                        }else{
                            Toast.makeText(getContext(),
                                    "Error: compruebe que los datos sean correctos",
                                    Toast.LENGTH_SHORT).show();
                        }
                }else{
                    Toast.makeText(getContext(),
                            "Error: no puede haber campos vacios",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void configureBtnLimpiar(){
        btn_limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiarCampos();
            }
        });
    }

    private void limpiarCampos(){
        //Limpiar campos
        et_id.setText("");
        et_nombre.setText("");
        et_fecha.setText("");
        et_edad.setText("");
        et_estatura.setText("");
        et_peso.setText("");
        sp_area.setId(0);
        sp_doctor.setId(0);
        sp_sexo.setId(0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK){
            iv_foto.setImageURI(photoURI);
            pathImage=currentPhotoPath;
            Toast.makeText(getContext(), "Foto guardada en "+ pathImage, Toast.LENGTH_SHORT).show();
        }
    }

}