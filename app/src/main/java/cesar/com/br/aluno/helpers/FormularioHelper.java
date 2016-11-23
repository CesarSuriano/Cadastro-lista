package cesar.com.br.aluno.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;

import cesar.com.br.aluno.activities.FormularioActivity;
import cesar.com.br.aluno.R;
import cesar.com.br.aluno.models.bean.Aluno;

/**
 * Created by Cesar on 16/10/2016.
 */

//Classe responsavel por instanciar os elementos de tela da Activity FormularioActivity
public class FormularioHelper {
    private EditText edtNome;
    private EditText edtTelefone;
    private EditText edtSite;
    private EditText edtEmail;
    private EditText edtEndereco;
    private SeekBar  seekNota;
    private ImageView foto;
    private Aluno aluno;

    //Construtor responsável por associar as variaveis aos respectivos objetos
    public FormularioHelper(FormularioActivity activity) {
        edtNome     = (EditText)activity.findViewById(R.id.edtNome);
        edtTelefone = (EditText)activity.findViewById(R.id.edtTelefone);
        edtSite     = (EditText)activity.findViewById(R.id.edtSite);
        edtEmail    = (EditText)activity.findViewById(R.id.edtEmail);
        edtEndereco = (EditText)activity.findViewById(R.id.edtEndereco) ;
        seekNota    = (SeekBar)activity.findViewById (R.id.seekNota);
        foto        = (ImageView)activity.findViewById(R.id.foto);

        aluno = new Aluno();
    }

    //Define as propriedade do aluno e o retorna em seguida
    public Aluno getAluno(){
        aluno.setNome(edtNome.getText().toString());
        aluno.setTelefone(edtTelefone.getText().toString());
        aluno.setSite(edtSite.getText().toString());
        aluno.setEmail(edtEmail.getText().toString());
        aluno.setEndereco(edtEndereco.getText().toString());
        aluno.setNota(Double.valueOf(seekNota.getProgress()));

        return aluno;
    }

    public void setAluno(Aluno aluno){
        edtNome.setText(aluno.getNome());
        edtTelefone.setText(aluno.getTelefone());
        edtEndereco.setText(aluno.getEndereco());
        edtSite.setText(aluno.getSite());
        edtEmail.setText(aluno.getEmail());
        seekNota.setProgress(aluno.getNota().intValue());

        this.aluno = aluno;

        //Carregar foto do aluno
        if (aluno.getFoto() != null) {
            carregarFoto(aluno.getFoto());
        }
    }

    public ImageView getFoto(){
        return foto;
    }

    //Método responsável por carregar a foto
    public void carregarFoto(String localFoto){

        //Carregar imagem da camera
        Bitmap imagemFoto = BitmapFactory.decodeFile(localFoto);

        //Gerar imagem reduzida
        Bitmap imagemReduzida = Bitmap.createScaledBitmap(imagemFoto, 100, 100, true);

        //Guarda o caminho da foto do aluno
        aluno.setFoto(localFoto);

        //atualiza a  imagem exibida na tela de formulário
        foto.setImageBitmap(imagemReduzida);
    }
}
