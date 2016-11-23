package cesar.com.br.aluno.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cesar.com.br.aluno.R;
import cesar.com.br.aluno.models.DAO.AlunoDAO;
import cesar.com.br.aluno.models.bean.Aluno;

//Classe de listagem
public class ListaAlunosActivity extends AppCompatActivity {

    //Constantes usadas para ajuda na restauração da activity quando o usuario gira o device
    private final String TAG = "CADASTRO_ALUNO";
    private final String ALUNOS_KEY = "LISTA";


    //Declaração dos atributos para que sejam visíveis para classe toda
    private EditText edtNome;
    private Button btnCadastrar;
    private ListView lvListagem;

    //Coleção de alunos a serem exibidos na tela
    private List<Aluno> listaAlunos;

    //Converte as listas do java para que possam ser visiveis na listView
    private ArrayAdapter<Aluno> adapter;

    //Layout da listView
    private int adapterLayout = android.R.layout.simple_list_item_1;

    //Aluno selecionado no click longo da listView
    private Aluno alunoSelecionado = null;


    /*
    Toda vez que o usuário gira o celular, o android chama o metodo onCreate novamente.
    como consequencia, todas as informações que estão lá são perdidas. Para que não tenhamos o
    problema de ter toda a nossa lista apagada toda vez que giramos o celular definimos 2 métodos:
    onSaveInstanceState() e onRestoreInstanceState()
     */

//    //Responsável por salvar o estado da aplicação
//    @Override
//    protected void onSaveInstanceState(Bundle outState){
//        //inclusão da lista de alunos no objeto Bundle.Map
//        outState.putStringArrayList(ALUNOS_KEY, (ArrayList<String>) listaAlunos);
//
//        //persistencia do objeto Bundle
//        super.onSaveInstanceState(outState);
//
//        //
//        //lançamentos de mensagens de log
//        Log.i(TAG, "onSaveInstanceState(): "+ listaAlunos);
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //Inicialização dos atributos
        lvListagem = (ListView)findViewById(R.id.lvListagem);


        //Informa que a listView tem um menu de contexto
        registerForContextMenu(lvListagem);

        //Clique simples na listView
        lvListagem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent form = new Intent(ListaAlunosActivity.this, FormularioActivity.class);

                alunoSelecionado = (Aluno)lvListagem.getItemAtPosition(position);

                form.putExtra("ALUNO_SELECIONADO", alunoSelecionado);

                startActivity(form);
            }
        });

        //Clique longo na listView, aquele que você clica e segura
        lvListagem.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                alunoSelecionado = (Aluno) adapter.getItem(position);
                return false;
            }
        });



        //        edtNome = (EditText)findViewById(R.id.edtNomeListagem);
//        btnCadastrar = (Button)findViewById(R.id.btnAddListagem);

//        //Iniciaização da coleção de alunos caso a aplicação esteja sendo executada pela primeira vez
//        if(savedInstanceState == null) {
//            listaAlunos = new ArrayList<String>();
//        }else{
//            listaAlunos = savedInstanceState.getStringArrayList(ALUNOS_KEY);
//        }
//
//        //associando o nossa coleção com a listView]
//        adapter = new ArrayAdapter<String>(this, adapterLayout, listaAlunos);
//        lvListagem.setAdapter(adapter);


    }

    //Método de Exclusão da activity
    private void excluirAluno(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Confirma a exclusão de: " + alunoSelecionado.getNome());

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlunoDAO dao = new AlunoDAO(ListaAlunosActivity.this);
                dao.deletar(alunoSelecionado);
                dao.close();
                carregaLista();;
                alunoSelecionado = null;
            }
        });

        builder.setNegativeButton("Não", null);
        AlertDialog dialog = builder.create();
        dialog.setTitle("Confirmação da operação");
        dialog.show();
    }

    //Método para carregar a lista
    public void carregaLista(){
        //Criação do objeto DAO. Início da conexão com o BD
        AlunoDAO dao = new AlunoDAO(this);

        //Chamada do método listar
        this.listaAlunos = dao.listar();

        //Fim da conexão
        dao.close();

        //passando pro adaptador fazer a conversão da Lista para listView
        this.adapter = new ArrayAdapter<Aluno>(this, adapterLayout, listaAlunos);

        //Associação do adapter com a listView
        this.lvListagem.setAdapter(adapter);
    }

    //Definiçao do menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = this.getMenuInflater();

        inflater.inflate(R.menu.menu_principal, menu);

        return true;
    }

    //Clique nos itens de menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){

            case R.id.menu_novo:
                Intent it = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                startActivity(it);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Menu de contexto
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, view, menuInfo);

        getMenuInflater().inflate(R.menu.menu_contexto, menu);
    }

    //Item Clique do contextMenu
    @Override
    public boolean onContextItemSelected(MenuItem item){
        Intent intent;
        switch (item.getItemId()){
            case R.id.menuDeletar:
                excluirAluno();
                break;
//            case R.id.menuLigar:
//                intent = new Intent(Intent.ACTION_CALL);
//                intent.setData(Uri.parse("tel:" + alunoSelecionado.getTelefone()));
//                //startActivity(intent);
//                break;
//            case R.id.menuEnviarSMS:
//                intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse("sms:" + alunoSelecionado.getTelefone()));
//                intent.putExtra("sms_body", "Mensagem de boas vindas");
//                startActivity(intent);
//                break;
//            case R.id.menuAcharNoMapa:
//                intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse("geo:0,0?z=14&q=" + alunoSelecionado.getEndereco()));
//                intent.putExtra("sms_body", "Mensagem de boas vindas");
//                startActivity(intent);
//                break;
//            case R.id.menuNavegar:
//                intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse("http:" + alunoSelecionado.getSite()));
//                startActivity(intent);
//                break;
//            case R.id.menuEnviarEmail:
//                intent = new Intent(Intent.ACTION_VIEW);
//                intent.setType("message/rtc822");
//                intent.putExtra(Intent.EXTRA_EMAIL,
//                        new String[] {alunoSelecionado.getEmail()});
//                intent.putExtra(Intent.EXTRA_TEXT, "O curso foi da hora");
//
//                startActivity(intent);
//                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onResume(){
        super.onResume();

        //Carregar coleção
        this.carregaLista();
    }
}
