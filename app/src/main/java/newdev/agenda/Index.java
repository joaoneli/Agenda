package newdev.agenda;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import newdev.agenda.WebServices.WebServiceControle;
import newdev.agenda.WebServices.content.Item;
import newdev.agenda.WebServices.content.TesteSquidexInfo;

public class Index extends AppCompatActivity {

    public static final String EXTRA_REGISTRO = "newdev.agenda.Index.EXTRA_REGISTRO";
    private ListView lvContatos;
    private SwipeRefreshLayout srContatos;
    private FloatingActionButton fabConfirmar;
    private TesteSquidexInfo listContatos;
    private String Telefone;
    private String Endereco;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        inicializaComponenetes();
        criaAdapterLista();
        registerForContextMenu(lvContatos);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        carregaListaContatos();
    }

    private void inicializaComponenetes()
    {
        lvContatos = findViewById(R.id.lvContatos);
        srContatos = findViewById(R.id.srContatos);
        fabConfirmar = findViewById(R.id.fabConfirmar);
        srContatos.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                carregaListaContatos();
            }
        });
        //
        fabConfirmar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Index.this, Cadastro.class));
            }
        });
        //
        lvContatos.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Item item = listContatos.getItems()[position];
                          Intent intent = new Intent(Index.this, Cadastro.class);
                intent.putExtra(EXTRA_REGISTRO, item);
                startActivity(intent);
            }
        });

        lvContatos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                Telefone = listContatos.getItems()[position].getData().getTelefone().getIv();
                Endereco = listContatos.getItems()[position].getData().getEndereco().getIv();
                return false;
            }
        });

    }

    private void criaAdapterLista()
    {
        lvContatos.setAdapter(new ArrayAdapter<Object>(this, 0)
                                   {
                                       class ViewHolder
                                       {
                                           TextView tvNome;
                                           TextView tvEndereco;
                                           TextView tvTelefone;
                                           TextView tvEmail;
                                       }

                                       @Override
                                       public int getCount()
                                       {
                                           if (listContatos != null)
                                               return listContatos.getTotal().intValue();
                                           else
                                               return 0;
                                       }

                                       @NonNull
                                       @Override
                                       public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
                                       {
                                           ViewHolder viewHolder;
                                           Item item = listContatos.getItems()[position];
                                           //
                                           if (convertView == null)
                                           {
                                               convertView = getLayoutInflater().inflate(R.layout.item_listagem, null);
                                               viewHolder = new ViewHolder();
                                               convertView.setTag(viewHolder);
                                               //
                                               viewHolder.tvNome = convertView.findViewById(R.id.tvNome);
                                               viewHolder.tvEndereco = convertView.findViewById(R.id.tvEndereco);
                                               viewHolder.tvTelefone = convertView.findViewById(R.id.tvTelefone);
                                               viewHolder.tvEmail = convertView.findViewById(R.id.tvEmail);

                                           }
                                           else
                                               viewHolder = (ViewHolder) convertView.getTag();
                                           //
                                           viewHolder.tvNome.setText(item.getData().getNome().getIv());
                                           viewHolder.tvEndereco.setText(item.getData().getEndereco().getIv());
                                           viewHolder.tvTelefone.setText(item.getData().getTelefone().getIv());
                                           viewHolder.tvEmail.setText(item.getData().getEmail().getIv());

                                           //
                                           return convertView;
                                       }
                                   }
        );
    }

    private void carregaListaContatos()
    {
        srContatos.setRefreshing(true);
        new WebServiceControle().carregaListaContatos(this, new WebServiceControle.carregaListaContatosListener()
        {
            @Override
            public void onResultOk(TesteSquidexInfo teste)
            {
                listContatos = teste;
                ((ArrayAdapter) lvContatos.getAdapter()).notifyDataSetChanged();
                srContatos.setRefreshing(false);
            }

            @Override
            public void onErro()
            {
                listContatos = null;
                ((ArrayAdapter) lvContatos.getAdapter()).notifyDataSetChanged();
                srContatos.setRefreshing(false);
            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        menu.setHeaderTitle("Escolha uma ação:");

    }
    @Override
    public boolean onContextItemSelected(MenuItem item){


        if(item.getItemId()==R.id.call) {


                if (ActivityCompat.checkSelfPermission(Index.this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Index.this,
                            new String[]{Manifest.permission.CALL_PHONE}, 123);
                } else {
                    Intent intentLigar = new Intent(Intent.ACTION_CALL);
                    intentLigar.setData(Uri.parse("tel:" + Telefone));
                    startActivity(intentLigar);
                    Toast.makeText(getApplicationContext(), "Telefonando", Toast.LENGTH_LONG).show();
                }
            }

        else if(item.getItemId()==R.id.sms){
            Intent intentSMS = new Intent(Intent.ACTION_VIEW);
            intentSMS.setData(Uri.parse("sms:" + Telefone));
            startActivity(intentSMS);
            Toast.makeText(getApplicationContext(),"Abrindo mensagens",Toast.LENGTH_LONG).show();
        }
        else if(item.getItemId()==R.id.mapa){
            Intent intentMapa = new Intent(Intent.ACTION_VIEW);
            intentMapa.setData(Uri.parse("geo:0,0?q=" + Endereco));
            startActivity(intentMapa);
            Toast.makeText(getApplicationContext(),"Abrindo mapa",Toast.LENGTH_LONG).show();
        }
        else{
            return false;
        }
        return true;
    }



}
