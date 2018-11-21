package newdev.agenda.WebServices.content;

import java.io.Serializable;

public class Data implements Serializable
{

    private StringValue nome;
    private StringValue endereco;
    private StringValue telefone;
    private StringValue email;


    public StringValue getNome() {
        return nome;
    }

    public void setNome(StringValue nome) {
        this.nome = nome;
    }

    public StringValue getEndereco() {
        return endereco;
    }

    public void setEndereco(StringValue endereco) {
        this.endereco = endereco;
    }

    public StringValue getTelefone() {
        return telefone;
    }

    public void setTelefone(StringValue telefone) {
        this.telefone = telefone;
    }

    public StringValue getEmail() {
        return email;
    }

    public void setEmail(StringValue email) {
        this.email = email;
    }
}
