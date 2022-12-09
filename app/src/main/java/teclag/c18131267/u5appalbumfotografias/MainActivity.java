package teclag.c18131267.u5appalbumfotografias;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import teclag.c18131267.u5appalbumfotografias.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private RecyclerView listaFotos;
    private FotoAdapter adapter;

    private List<Bitmap> fotos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater(),null,false);
        setContentView(binding.getRoot());

        cargarFotos();
        adapter=new FotoAdapter(fotos, this);



        listaFotos.setLayoutManager(new GridLayoutManager(this,4));
        listaFotos.setAdapter(adapter);

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hacerFoto();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }


    private File archivo;
    public void hacerFoto() {
        try {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            archivo = crearFichero();
            Uri foto = FileProvider.getUriForFile(this,"com.able.u5appalbumfotografias.fileprovider",archivo );
            i.putExtra(MediaStore.EXTRA_OUTPUT,foto);
            startActivityForResult(i, 1);
        }
        catch (IOException e){
            Toast.makeText(this,"Error" +e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

    public void cargarFotos(){
        File directorio = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] fichero = directorio.listFiles();
        for (File i: fichero){
            if(i.getName().endsWith(".jpg")) {
                Bitmap image = BitmapFactory.decodeFile(i.getAbsolutePath());
                fotos.add(image);
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        if(requestCode==1){
            if(resultCode== Activity.RESULT_OK){
                Bitmap image = BitmapFactory.decodeFile(archivo.getAbsolutePath());
                fotos.add(image);
                adapter.notifyDataSetChanged();
            }else{
                archivo.delete();
            }
        }else{
            super.onActivityResult(requestCode,resultCode,data);
        }
    }

    private File crearFichero() throws IOException {
        String pre="foto_";
        File directorio = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File img = File.createTempFile(pre+ UUID.randomUUID().toString(),"jpg",directorio);
        return img;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}