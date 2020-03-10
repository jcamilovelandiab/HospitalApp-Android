package com.example.appfragments.ui.eliminar;

import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.appfragments.R;
import com.example.appfragments.SQL.SQLite;

import java.io.File;

public class EliminarFragment extends Fragment {

    Button btn_eliminar;
    EditText et_idPaciente;
    private SQLite sqlite;

    private EliminarViewModel eliminarViewModel;
    String g1 = null, g2 = null, g3 = null, g4 = null,g5 = null,g6 = null,g7 = null,g8 = null,g9 = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        eliminarViewModel =
                ViewModelProviders.of(this).get(EliminarViewModel.class);
        View root = inflater.inflate(R.layout.fragment_eliminar, container, false);

        btn_eliminar = (Button) root.findViewById(R.id.eliminar_btn_eliminar);
        et_idPaciente = (EditText) root.findViewById(R.id.eliminar_et_idPaciente);

        configureBtnEliminar();

        return root;
    }

    private void configureBtnEliminar(){
        btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sqlite=new SQLite(getContext());
                sqlite.abrir();

                if(et_idPaciente.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Ingrese el id del paciente", Toast.LENGTH_SHORT).show();
                } else {
                    if (sqlite.getCant(Integer.parseInt(et_idPaciente.getText().toString())).getCount() == 1) {

                        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getContext());

                        if (!et_idPaciente.getText().toString().equals("")){
                            if(sqlite.getCant(Integer.parseInt(et_idPaciente.getText().toString())).getCount()==1) {

                                int f = Integer.parseInt(et_idPaciente.getText().toString());
                                Cursor cursor = sqlite.getCant(f);
                                if (cursor.moveToFirst()) {
                                    do {
                                        g1 = cursor.getString(1);
                                        g2 = cursor.getString(2);
                                        g3 = cursor.getString(3);
                                        g4 = cursor.getString(4);
                                        g5 = cursor.getString(5);
                                        g6 = cursor.getString(6);
                                        g7 = cursor.getString(7);
                                        g8 = cursor.getString(8);
                                        g9 = cursor.getString(9);
                                    } while (cursor.moveToNext());
                                }
                            }
                        }

                        View dialogView=LayoutInflater.from(getContext()).inflate(R.layout.dialog_paciente,null);
                        ((TextView)dialogView.findViewById(R.id.dialog_paciente_tv_datos)).setText("¿ Desea eliminar el registro? \n" +
                                "ID:              ["+et_idPaciente.getText()+" ]\n" +
                                "Área:         ["+g1+"]\n"+
                                "Doctor:     ["+g2+"]\n"+
                                "Nombre:   [ "+g3+"]\n"+
                                "Sexo:        ["+g4+"]\n"+
                                "F. Ingreso: ["+g5+"]\n"+
                                "Edad:        ["+g6+" Años]\n"+
                                "Estatura:   ["+g7+" Cm]\n"+
                                "Peso:         ["+g8+ " Kg]\n");
                        ImageView image=dialogView.findViewById(R.id.dialog_paciente_iv_picture);
                        cargarImagen(g9,image);
                        dialogo1.setTitle("Importante");
                        dialogo1.setView(dialogView);
                        dialogo1.setCancelable(false);
                        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                aceptar();
                            }
                        });
                        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                Toast.makeText(getContext(), "Cancelado", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialogo1.show();
                    }else{
                        Toast.makeText(getContext(), "Error: No existe ese ID" +
                                "", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void aceptar(){
        sqlite.Eliminar(et_idPaciente.getText());
        Toast.makeText(getContext(), "Registro Eliminado", Toast.LENGTH_SHORT).show();
    }

    //cargar imagen
    public void cargarImagen(String imagen, ImageView iv){
        try{
            File filePhoto=new File(imagen);
            Uri uriPhoto = FileProvider.getUriForFile(getContext(),"com.example.appfragments",filePhoto);
            iv.setImageURI(uriPhoto);
        }catch (Exception ex){
            Toast.makeText(getContext(), "Ocurrio un error al cargar la imagen", Toast.LENGTH_SHORT).show();
            Log.d("Cargar Imagen","Error al cargar imagen: "+imagen+"\nMensaje: "+ex.getMessage()+"\nCausa: "+ex.getCause());
        }
    }

}