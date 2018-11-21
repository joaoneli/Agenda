package newdev.agenda;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import org.json.JSONException;

import newdev.agenda.WebServices.WebServiceControle;
import newdev.agenda.WebServices.content.Data;
import newdev.agenda.WebServices.content.Item;
import newdev.agenda.WebServices.content.StringValue;
import newdev.agenda.dialogs.PopupInformacao;

public class Cadastro extends AppCompatActivity {

    private TextInputLayout tilNome;
    private TextInputEditText etNome;
    private TextInputLayout tilEndereco;
    private TextInputEditText etEndereco;
    private TextInputLayout tilTelefone;
    private TextInputEditText etTelefone;
    private TextInputLayout tilEmail;
    private TextInputEditText etEmail;

    //
    private Item item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        inicializaComponentes();
        //
        item = (Item) getIntent().getSerializableExtra(Index.EXTRA_REGISTRO);
        //
        carregaValores();
    }

    private void inicializaComponentes()
    {
        tilNome = findViewById(R.id.tilNome);
        etNome = findViewById(R.id.etNome);
        tilEndereco = findViewById(R.id.tilEndereco);
        etEndereco = findViewById(R.id.etEndereco);
        tilTelefone = findViewById(R.id.tilTelefone);
        etTelefone = findViewById(R.id.etTelefone);
        tilEmail = findViewById(R.id.tilEmail);
        etEmail = findViewById(R.id.etEmail);


        FloatingActionButton fabConfirmar = findViewById(R.id.fabConfirmar);
        FloatingActionButton fabDeletar = findViewById(R.id.fabDeletar);
        //
        etNome.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                tilNome.setError(null);
            }
        });
        etEndereco.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                tilEndereco.setError(null);
            }
        });
        etTelefone.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                tilTelefone.setError(null);
            }
        });

        etEmail.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                tilEmail.setError(null);
            }
        });

        //
        fabConfirmar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                confirmaTela();
            }
        });
        //
        fabDeletar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                deletaRegistro();
            }
        });
    }

    private boolean validaTela()
    {
        boolean retorno = true;
        //
        if (etNome.getText().toString().trim().length() == 0)
        {
            tilNome.setError("Informe o nome do contato");
            retorno = false;
        }
        //
        if (etEndereco.getText().toString().trim().length() == 0)
        {
            tilEndereco.setError("Informe o endereco");
            retorno = false;
        }

        if (etTelefone.getText().toString().trim().length() > 11|| etTelefone.getText().toString().trim().length() < 10) {
            tilTelefone.setError("Telefone Invalido");
            retorno = false;
        }
        //
        if (etEmail.getText().toString().trim().length() == 0)
        {
            tilEmail.setError("Informe o email");
            retorno = false;
        }
        return retorno;
    }

    private void confirmaTela()
    {
        if (!validaTela())
            return;

        salvaRegistro();
    }

    private void salvaRegistro()
    {
        Data data = new Data();
        data.setNome(new StringValue(etNome.getText().toString()));
        data.setEndereco(new StringValue(etEndereco.getText().toString()));
        data.setTelefone(new StringValue(etTelefone.getText().toString()));
        data.setEmail(new StringValue(etEmail.getText().toString()));


        try
        {
            if(item == null)
            {
                new WebServiceControle().criaLista(this, data, new WebServiceControle.UpdateListener()
                {
                    @Override
                    public void onResultOk()
                    {
                        Cadastro.this.finish();
                    }

                    @Override
                    public void onErro()
                    {
                        PopupInformacao.mostraMensagem(Cadastro.this, "Falha ao salvar registro");
                    }
                });
            }
            else
            {
                new WebServiceControle().atualizaLista(this, data, item.getId(), new WebServiceControle.UpdateListener()
                {
                    @Override
                    public void onResultOk()
                    {
                        Cadastro.this.finish();
                    }

                    @Override
                    public void onErro()
                    {
                        PopupInformacao.mostraMensagem(Cadastro.this, "Falha ao salvar registro");
                    }
                });
            }
        }
        catch (JSONException e)
        {
            PopupInformacao.mostraMensagem(Cadastro.this, "Falha ao salvar registro");
        }
        //

    }

    private void carregaValores()
    {
        if(item != null) {
            etEndereco.setText(item.getData().getEndereco().getIv());
            etNome.setText(item.getData().getNome().getIv());
            etTelefone.setText(item.getData().getTelefone().getIv());
            etEmail.setText(item.getData().getEmail().getIv());
        }
    }

    private void deletaRegistro()
    {
        if(item != null)
        {
            new WebServiceControle().deletaLista(this, item.getId(), new WebServiceControle.UpdateListener()
            {
                @Override
                public void onResultOk()
                {
                    Cadastro.this.finish();
                }

                @Override
                public void onErro()
                {
                    PopupInformacao.mostraMensagem(Cadastro.this, "Falha ao salvar registro");
                }
            });
        }
    }
}
