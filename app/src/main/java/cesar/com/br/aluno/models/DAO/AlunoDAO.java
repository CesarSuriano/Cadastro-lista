package cesar.com.br.aluno.models.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cesar.com.br.aluno.models.bean.Aluno;

/**
 * Created by Cesar on 16/10/2016.
 */

//Classe responsável pela persistencia
public class AlunoDAO extends SQLiteOpenHelper{

    //Constantes para o auxílio no controle de versão do banco
    private static final int    VERSAO   = 1;
    private static final String TABELA   = "Aluno";
    private static final String DATABASE = "MPAlunos";
    //Constante para o log no logcat
    private static final String TAG = "CADASTRO_ALUNO";

    //construtor
    public AlunoDAO(Context context){
        //Construtor da classe mãe, que é quem vai tratar os dados de verdade
        super(context, DATABASE, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Criação da tabela
        String sql = "CREATE TABLE " + TABELA + "("
                + "id INTEGER PRIMARY KEY, "
                + "nome TEXT, telefone TEXT, endereco TEXT, site TEXT ,"
                + "email TEXT, foto TEXT, nota REAL)";

        //chamando o método para criar a tabela
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Sql para apagar a tabela
        String sql = "DROP TABLE IF EXISTS " + TABELA;

        //Chamada do metodo para executar o SQL
        db.execSQL(sql);

        //Chamada do metodo onCreate para a criação da nova tabela
        onCreate(db);
    }

    //Método de cadastro
    public void cadastrar(Aluno aluno){

        //Objeto para armazenar os valores dos campos
        ContentValues values = new ContentValues();

        //Definição de valores dos campos da tabela
        values.put("nome", aluno.getNome());
        values.put("telefone", aluno.getTelefone());
        values.put("endereco", aluno.getEndereco());
        values.put("site", aluno.getSite());
        values.put("email", aluno.getEmail());
        values.put("foto", aluno.getFoto());
        values.put("nota", aluno.getNota());

        //inserindo no banco
        getWritableDatabase().insert(TABELA, null, values);
        Log.i(TAG, "Aluno cadastrado" + aluno.getNome());
    }

    public List<Aluno> listar(){
        //Criando a coleção de alunos
        List<Aluno> lista = new ArrayList<Aluno>();

        //Instrução SQL para retornar os alunos
        String sql = "SELECT * FROM Aluno ORDER BY nome";

        //Objeto que recebe os registros do banco de dados
        Cursor cursor = getReadableDatabase().rawQuery(sql, null);

        try{
            while(cursor.moveToNext()){
                //Criação de nova instancia para Aluno
                Aluno aluno = new Aluno();

                //Carregar os dados de Aluno com os dados do BD
                aluno.setId(cursor.getLong(0));
                aluno.setNome(cursor.getString(1));
                aluno.setTelefone(cursor.getString(2));
                aluno.setEndereco(cursor.getString(3));
                aluno.setSite(cursor.getString(4));
                aluno.setEmail(cursor.getString(5));
                aluno.setFoto(cursor.getString(6));
                aluno.setNota(cursor.getDouble(7));

                //Adicionar novo aluno a lista
                lista.add(aluno);
            }
        }catch (SQLException e){
            Log.e(TAG, e.getMessage());
        } finally {
            cursor.close();
        }
        return lista;
    }

    //Método para exclusão de registros
    public void deletar (Aluno aluno){
        //Array de parametros
        String []args = {aluno.getId().toString()};

        //exclusão do aluno
        getWritableDatabase().delete(TABELA, "id=?", args);

        Log.i(TAG, "Aluno deletado: "+ aluno.getNome());
    }

    //Método para alteração dos dados de aluno
    public void alterar (Aluno aluno){
        ContentValues values = new ContentValues();

        values.put("nome", aluno.getNome());
        values.put("telefone", aluno.getTelefone());
        values.put("endereco", aluno.getEndereco());
        values.put("site", aluno.getSite());
        values.put("email", aluno.getEmail());
        values.put("foto", aluno.getFoto());
        values.put("nota", aluno.getNota());

        //Coleção de valores de parametros do SQL
        String[] args = {aluno.getId().toString()};

        //Altera os dados do aluno no banco
        getWritableDatabase().update(TABELA, values, "id=?", args);
        Log.i(TAG, "Aluno alterado");
    }
}
