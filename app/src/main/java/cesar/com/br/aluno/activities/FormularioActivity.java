package cesar.com.br.aluno.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.net.URI;

import cesar.com.br.aluno.R;
import cesar.com.br.aluno.helpers.FormularioHelper;
import cesar.com.br.aluno.models.DAO.AlunoDAO;
import cesar.com.br.aluno.models.bean.Aluno;


public class FormularioActivity extends AppCompatActivity {

    private Button botao;
    private FormularioHelper helper;
    private Aluno alunoParaSerAlterado;

    //Variaveis para o controle da camera
    private String localArquivo;
    //Constante usada como requestCode
    private static final int FAZER_FOTO = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        //Criação do Helper
        helper = new FormularioHelper(this);
        botao = (Button)findViewById(R.id.btnSalvar);

        //Clique da foto
        helper.getFoto().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localArquivo = Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".jpg";

                File arquivo = new File(localArquivo);

                //URI que informa onde o arquivo resultado deve ser salvo
                Uri localFoto = Uri.fromFile(arquivo);

                Intent irParaCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                irParaCamera.putExtra(MediaStore.EXTRA_OUTPUT, localFoto);

                startActivityForResult(irParaCamera, FAZER_FOTO);
            }
        });

        //Busca o aluno a ser alterado
        alunoParaSerAlterado = (Aluno)getIntent().getSerializableExtra("ALUNO_SELECIONADO");

        if (alunoParaSerAlterado != null) {
            //Atualiza a tela
            helper.setAluno(alunoParaSerAlterado);
        }

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Utilizando o Helper para recuperar dados do aluno
                Aluno aluno = helper.getAluno();

                //Criação do objeto DAO. Inicio da conexão com o banco
                AlunoDAO dao = new AlunoDAO(FormularioActivity.this);

                //Verificando se é para salvar ou alterar o aluno
                if(aluno.getId() == null) {
                    //chamada do método de cadastro
                    dao.cadastrar(aluno);
                }else{
                    dao.alterar(aluno);
                }

                //Fechamento da conexão
                dao.close();

                //Encerramento da Activity
                finish();
            }
        });

    }

    //Método responsavel por verificar o resultado retornado pela camera
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        //Verificação do resultado da requisição
        if(requestCode == FAZER_FOTO){
            if(requestCode == Activity.RESULT_OK){
                helper.carregarFoto(this.localArquivo);
            }else{
                localArquivo = null;
            }
        }
    }
}
